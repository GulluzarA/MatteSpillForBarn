package com.s349571s322991.mattespillforbarn;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.os.LocaleListCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SpillActivity extends AppCompatActivity {

    ApplicationClass application;
    Intent intentStartActivity; // Brukt til navigering mellom aktiviteter.
    Intent intentResultatActivity;

    ArrayList<Integer[]> regnestykker = new ArrayList<>(); // Variabel til å holde alle regnestykkene.
    ArrayList<Integer[]> randomRegnestykker = new ArrayList<>(); // Randomized liste...
    Integer[] aktivtRegnestykke = null;
    int antallSporsmaalFraPrefs;
    int sporsmaalIndeks; // Defaulter til spørsmål 1 på indeks 0.
    int tall1 = -1;
    int tall2 = -1;
    int regnestykkeSvar = -1; // Svaret på regnestykket.
    int antallRiktige = 0;
    TextView spillTekstProgress;
    TextView spillTekstTittel;
    TextView spillTekstTall1;
    TextView spillTekstTall2;
    TextView spillTekstSvar;
    ImageButton spillKnappAvslutt;
    ImageButton spillKnappSlett,spillKnappGo;
    ImageButton spillKnapp0, spillKnapp1, spillKnapp2, spillKnapp3, spillKnapp4, spillKnapp5, spillKnapp6, spillKnapp7, spillKnapp8, spillKnapp9;
    Toast toast;
    AlertDialog popupSvar; // Popup Dialog når et spørsmål er ferdig...
    LayoutInflater inflater;
    TextView dialogTittel;
    TextView dialogRiktigSvar;
    TextView dialogDittSvar;
    ImageView dialogBilde;
    TextView dialogOversikt;
    Drawable drawableFeil;
    Drawable drawableRiktig;
    // Lage egen AlertDialog for å avslutte tidlig under SpillActivity.
    AlertDialog popupAvslutt;
    boolean ferdigMedSporsmaal = false;
    ImageButton spillFlaggNo, spillFlaggDe;

    void getApplicationClass(){
        application = (ApplicationClass) getApplicationContext();
        Log.d("Erlend", "SpillActivity ~ application: "+ application); // Sjekke om den er null eller ikke.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spill);

        getApplicationClass();

        // Må defineres utenfor onClickListener.
        intentStartActivity = new Intent(this, StartActivity.class);
        intentResultatActivity = new Intent(this, ResultatActivity.class);

        inflater = this.getLayoutInflater(); // For AlertDialogene (inflate layouts).

        spillTekstProgress = findViewById(R.id.spillTekstProgress);
        spillTekstTittel = findViewById(R.id.SpillTekstTittel);
        spillTekstTall1 = findViewById(R.id.spillTekstTall1);
        spillTekstTall2 = findViewById(R.id.spillTekstTall2);
        spillTekstSvar = findViewById(R.id.spillTekstSvar);
        spillKnappAvslutt = findViewById(R.id.SpillKnappAvslutt);
        spillKnappSlett = findViewById(R.id.spillKnappSlett);
        spillKnappGo = findViewById(R.id.spillKnappGo);
        spillKnapp0 = findViewById(R.id.spillKnapp0);
        spillKnapp1 = findViewById(R.id.spillKnapp1);
        spillKnapp2 = findViewById(R.id.spillKnapp2);
        spillKnapp3 = findViewById(R.id.spillKnapp3);
        spillKnapp4 = findViewById(R.id.spillKnapp4);
        spillKnapp5 = findViewById(R.id.spillKnapp5);
        spillKnapp6 = findViewById(R.id.spillKnapp6);
        spillKnapp7 = findViewById(R.id.spillKnapp7);
        spillKnapp8 = findViewById(R.id.spillKnapp8);
        spillKnapp9 = findViewById(R.id.spillKnapp9);
        spillFlaggNo = findViewById(R.id.spillFlaggNo);
        spillFlaggDe = findViewById(R.id.spillFlaggDe);

        definerDrawables();
        lagAvsluttDialog();
        lagSvarDialog();

        if(!application.ongoingRunde){
            application.ongoingRunde = true;
            application.antallRiktige = 0;
            application.regnestykker.clear();
            lagRegnestykkerFraArraysXml(); // Lager arrayliste med regnestykkene.
            application.clearRegnestykkeSvar(); // Sånn at den resetter seg for hvert nye spill.
        }

        // Oppdaterer regnestykket som vises. Bruker randomRegnestykker.
        settSporsmaalsVerdier(application.regnestykkeSvar.size());

        // Definerer antallSporsmaalFraPrefs her en gang.
        // Antar da at spilleren ikke kan forandre antall regnestykker mens et spill er aktivt.
        antallSporsmaalFraPrefs = application.getSharedPreferences().getInt(application.SHARED_ANTALL_SPORSMAAL, 5);
        oppdaterTekstProgress(application.regnestykkeSvar.size(), antallSporsmaalFraPrefs);
        aktiverFlaggFraPrefs();

        spillKnappAvslutt.setOnClickListener(v -> aapneAvsluttDialog());
        spillKnappSlett.setOnClickListener(v -> slettSpillerSvar());
        spillKnappGo.setOnClickListener(v -> {
            if(toast != null) toast.cancel();

            if(spillTekstSvar.getText().equals("")){
                toast = Toast.makeText(SpillActivity.this, getString(R.string.tomt_svar), Toast.LENGTH_SHORT);
                toast.show();
                slettSpillerSvar();
            } else {
                try{
                    int dittSvar = Integer.parseInt(spillTekstSvar.getText().toString());
                    int sporsmaalsNr = application.regnestykkeSvar.size()+1;
                    String tittel = sporsmaalsNr + " / " + antallSporsmaalFraPrefs;
                    String oversikt = "";
                    if(sporsmaalsNr >= antallSporsmaalFraPrefs){
                        ferdigMedSporsmaal = true; // Brukes i OnCancel / OnDismiss listener, slik at ResultActivity kan åpnes.
                        oversikt = getString(R.string.du_er_ferdig);
                    }

                    int erRiktig = 0;

                    if(dittSvar == regnestykkeSvar){
                        tittel += " " + getString(R.string.riktig);
                        visRiktigSvarDialog(regnestykkeSvar, dittSvar, tittel, oversikt);
                        erRiktig = 1;
                        antallRiktige++; // Brukes til ResultatActivity.
                    } else {
                        tittel += " " + getString(R.string.feil);
                        visFeilSvarDialog(regnestykkeSvar, dittSvar, tittel, oversikt);
                    }

                    application.leggTilRegnestykke(new Integer[]{
                            sporsmaalsNr, tall1, tall2, regnestykkeSvar, dittSvar, erRiktig
                    });

                }catch(Exception error){
                    Log.d("Erlend", "Noe gikk galt! error: " + error);
                    toast = Toast.makeText(SpillActivity.this, getString(R.string.noe_gikk_galt), Toast.LENGTH_SHORT);
                    toast.show();
                    slettSpillerSvar();
                }
            }
        });

        spillKnapp0.setOnClickListener(v -> oppdaterTekstSvar("0"));
        spillKnapp1.setOnClickListener(v -> oppdaterTekstSvar("1"));
        spillKnapp2.setOnClickListener(v -> oppdaterTekstSvar("2"));
        spillKnapp3.setOnClickListener(v -> oppdaterTekstSvar("3"));
        spillKnapp4.setOnClickListener(v -> oppdaterTekstSvar("4"));
        spillKnapp5.setOnClickListener(v -> oppdaterTekstSvar("5"));
        spillKnapp6.setOnClickListener(v -> oppdaterTekstSvar("6"));
        spillKnapp7.setOnClickListener(v -> oppdaterTekstSvar("7"));
        spillKnapp8.setOnClickListener(v -> oppdaterTekstSvar("8"));
        spillKnapp9.setOnClickListener(v -> oppdaterTekstSvar("9"));

        spillFlaggNo.setOnClickListener(v -> trykketPaaFlagg("no"));
        spillFlaggDe.setOnClickListener(v -> trykketPaaFlagg("de"));

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void lagRegnestykkerFraArraysXml(){

        try {

            String[] regnestykkerString = getResources().getStringArray(R.array.regnestykkerString);

            for(int i = 0; i < regnestykkerString.length; i++){
                List<String> curRegnestykkeSplit = Arrays.asList(regnestykkerString[i].split(","));
                Integer[] curRegnestykke = new Integer[]{
                        Integer.parseInt(curRegnestykkeSplit.get(0)),
                        Integer.parseInt(curRegnestykkeSplit.get(1)),
                        Integer.parseInt(curRegnestykkeSplit.get(2))
                };
                regnestykker.add(curRegnestykke);
            }

            // NOTE: Kan bruke Arrays.toString() for å printe ut verdiene i en array.
            // Istedenfor en ID får man f.eks. [0, 0, 0]

        } catch(Exception error){
            Log.d("Erlend", "SpillActivity ~ lagRegnestykkerFraArraysXml ~ Error?! error-melding: " + error);
        }

        lagRandomRegnestykkerListe(); // Lage tilfeldigjort regnestykkeliste.
    }

    void lagRandomRegnestykkerListe(){
        randomRegnestykker.addAll(regnestykker);
        Collections.shuffle(randomRegnestykker);
        // Lagre i ApplicationClass!
        application.setRegnestykker(randomRegnestykker); // NOTE: Bruke application.regnestykker etter at de er laget.
        // debugPrintRegnestykkeListe(randomRegnestykker); // DEBUG
    }

    // DEBUG funksjon.
    void debugPrintRegnestykkeListe(ArrayList<Integer[]> liste){
        Log.d("Erlend", "Printer ut regnestykkeliste: ");
        for (Integer[] regnestykke : liste) {
            Log.d("Erlend", Arrays.toString(regnestykke));
        }
        Log.d("Erlend", "Ferdig med print-ut av regnestykkeliste.");
    }

    // Bruker verdier fra randomRegnestykker.
    // [0, 0, 0]
    void settSporsmaalsVerdier(int sporsmaalsIndeks){
        aktivtRegnestykke = application.regnestykker.get(sporsmaalsIndeks);
        tall1 = aktivtRegnestykke[0];
        tall2 = aktivtRegnestykke[1];
        regnestykkeSvar = aktivtRegnestykke[2];

        int regnestykkeNr = sporsmaalsIndeks+1;
        String tittel = getString(R.string.regnestykke) + " " + regnestykkeNr;
        spillTekstTittel.setText(tittel);
        spillTekstTall1.setText(String.valueOf(tall1));
        spillTekstTall2.setText(String.valueOf(tall2));
    }

    void forberedNesteSporsmaal(){
        int sporsmaalsNr = sporsmaalIndeks+1;
        if(sporsmaalsNr < antallSporsmaalFraPrefs){
            sporsmaalIndeks++; // Øke her...
            settSporsmaalsVerdier(sporsmaalIndeks); // Neste spørsmål.
        } // Hvis false så er spilleren ferdig med regnestykkene.
    }

    void oppdaterTekstSvar(String tallString){
        if(spillTekstSvar.getText().length() < 2){
            String nyTekst = spillTekstSvar.getText() + tallString;
            spillTekstSvar.setText(nyTekst);
        }
    }

    void slettSpillerSvar(){
        spillTekstSvar.setText("");
    }

    void definerDrawables(){
        drawableFeil = ContextCompat.getDrawable(SpillActivity.this, R.drawable.thinking_emoji);
        drawableRiktig = ContextCompat.getDrawable(SpillActivity.this, R.drawable.check_mark_green);
    }

    // NOTE: Lager avsluttDialog. Kalt i starten.
    void lagAvsluttDialog(){
//      // NOTE: Skal åpne en dialogboks her for at brukeren skal bekrefte at han/hun vil virkelig avslutte.
        AlertDialog.Builder builder = new AlertDialog.Builder(SpillActivity.this);
        builder.setCancelable(true);
        builder.setMessage(getString(R.string.avslutte_bekreft));
        Drawable drawableAvslutte = ContextCompat.getDrawable(SpillActivity.this, R.drawable.kitty_thinking);
        builder.setIcon(drawableAvslutte);

        builder.setPositiveButton(
                getString(R.string.ja),
                (dialog, id) -> {
                    dialog.cancel();
                    startActivity(intentStartActivity);
                    ActivityCompat.finishAffinity(SpillActivity.this);
                });
        builder.setNegativeButton(
                getString(R.string.nei),
                (dialog, id) -> dialog.cancel());

        // Custom layout.
        View dialogImageView = inflater.inflate(R.layout.dialog_avslutt_spill, null);
        builder.setView(dialogImageView);

        popupAvslutt = builder.create();
    }

    void aapneAvsluttDialog(){
        popupAvslutt.show();
    }

    void lagSvarDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SpillActivity.this);
        builder.setCancelable(true);
        Drawable drawablePlay = ContextCompat.getDrawable(SpillActivity.this, R.drawable.play_blue);
        builder.setPositiveButtonIcon(drawablePlay);
        builder.setPositiveButton(
                "",
                (dialog, id) -> dialog.cancel());
        // Trenger ikke NegativeButton her.
        builder.setOnCancelListener(dialog -> {
            if(ferdigMedSporsmaal){
                gaaTilResultatActivity();
            } else {
                forberedNesteSporsmaal();
                slettSpillerSvar();
                // OBS! sporsmaalIndeks blir oppdatert i forberedNesteSporsmaal()!
                oppdaterTekstProgress(application.regnestykkeSvar.size(), antallSporsmaalFraPrefs);
            }
        });
        builder.setOnDismissListener(dialog -> Log.d("Erlend", "svarDialog ~ onDismiss"));

        // Med custom layout:
        // CustomTitle
        View dialogTitleView = inflater.inflate(R.layout.dialog_tittel, null);
        dialogTittel = dialogTitleView.findViewById(R.id.dialogTittelTekst);
        builder.setCustomTitle(dialogTitleView);
        // Custom View
        View dialogView = inflater.inflate(R.layout.dialog_layout, null);
        builder.setView(dialogView);

        dialogRiktigSvar = dialogView.findViewById(R.id.dialogRiktigSvar);
        dialogDittSvar = dialogView.findViewById(R.id.dialogDittSvar);
        dialogBilde = dialogView.findViewById(R.id.dialogBilde);
        dialogOversikt = dialogView.findViewById(R.id.dialogTekstOversikt);
        dialogBilde.setImageDrawable(drawableFeil);

        popupSvar = builder.create();
    }

    void oppdaterOgVisSvarDialog(int riktigSvar, int dittSvar, Drawable bilde, String tittel, String oversikt){
        dialogRiktigSvar.setText(String.valueOf(riktigSvar));
        dialogDittSvar.setText(String.valueOf(dittSvar));
        dialogBilde.setImageDrawable(bilde);
        dialogTittel.setText(tittel);
        dialogOversikt.setText(oversikt);
        popupSvar.show();
    }

    // Færre parametre. Svaret er sjekket og er riktig.
    void visRiktigSvarDialog(int riktigSvar, int dittSvar, String tittel, String oversikt){
        dialogRiktigSvar.setText(String.valueOf(riktigSvar));
        dialogDittSvar.setTextColor(getColor(R.color.green));
        dialogDittSvar.setText(String.valueOf(dittSvar));
        dialogBilde.setImageDrawable(drawableRiktig);
        dialogTittel.setText(tittel);
        dialogOversikt.setText(oversikt);
        popupSvar.show();
    }

    // Færre parametre. Svaret er sjekket og er feil.
    void visFeilSvarDialog(int riktigSvar, int dittSvar, String tittel, String oversikt){
        dialogRiktigSvar.setText(String.valueOf(riktigSvar));
        dialogDittSvar.setTextColor(getColor(R.color.orangeTomato));
        dialogDittSvar.setText(String.valueOf(dittSvar));
        dialogBilde.setImageDrawable(drawableFeil);
        dialogTittel.setText(tittel);
        dialogOversikt.setText(oversikt);
        popupSvar.show();
    }

    void oppdaterTekstProgress(int currentSporsmaalIndeks, int antallSporsmaal){
        String tekstProgress = currentSporsmaalIndeks+1 + " / " + antallSporsmaal;
        spillTekstProgress.setText(tekstProgress);
    }

    void aktiverFlaggFraPrefs() {
        String prefsSpraak = application.getSharedPreferences().getString(application.SHARED_SPRAAK, application.SHARED_SPRAAK_NORSK);
        switch (prefsSpraak) {
            case "no":
                spillFlaggNo.setImageResource(R.drawable.norway_flag_cute);
                break;
            case "de":
                spillFlaggDe.setImageResource(R.drawable.germany_flag_cute);
                break;
            default:
                break;
        }
    }

    void trykketPaaFlagg(String knappVerdi){
        String prefsSpraak = application.getSharedPreferences().getString(application.SHARED_SPRAAK, application.SHARED_SPRAAK_NORSK);
        String spraakTag = "no-NO";
        if(!Objects.equals(knappVerdi, prefsSpraak)){
            application.oppdaterSpraak(knappVerdi);
            switch(knappVerdi){
                case "no":
                    spillFlaggNo.setImageResource(R.drawable.norway_flag_cute);
                    spillFlaggDe.setImageResource(R.drawable.germany_flag_cute_inactive);
                    spraakTag = "no-NO";
                    break;
                case "de":
                    spillFlaggNo.setImageResource(R.drawable.norway_flag_cute_inactive);
                    spillFlaggDe.setImageResource(R.drawable.germany_flag_cute);
                    spraakTag = "de-DE";
                    break;
                default:
                    break;
            }
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(spraakTag);
            AppCompatDelegate.setApplicationLocales(appLocale);
        }
    }

    void gaaTilResultatActivity(){
        // Forberedelser for resultatActivity med regnestykker.
        application.setAntallRiktige(antallRiktige); // Brukes i ResultatActivity!
        Log.d("Erlend", String.valueOf(application.getAntallRiktige()));
        // Går til ResultatActivity.
        startActivity(intentResultatActivity);
        finish(); // Tar bort spillActivity fra stack.
    }

}
