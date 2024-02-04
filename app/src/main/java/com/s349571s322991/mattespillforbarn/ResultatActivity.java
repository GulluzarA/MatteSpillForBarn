package com.s349571s322991.mattespillforbarn;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ResultatActivity extends AppCompatActivity {

    ApplicationClass application;
    SharedPreferences sharedPrefs;
    ImageButton resultatKnappNyttSpill;
    ImageButton resultatKnappAvslutt;
    SpannableStringBuilder oppsummeringBuilder;
    SpannableStringBuilder spannableStringBuilder;
    TextView resultatTekstOppsummering;
    TextView resultatTekstAlleSvar;

    void getApplicationClass(){
        application = (ApplicationClass) getApplicationContext();
        Log.d("Erlend", "SpillActivity ~ application: "+ application); // Sjekke om den er null eller ikke.
    }
    void getSharedPrefs(){
        sharedPrefs = application.getSharedPreferences();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat);

        getApplicationClass();
        getSharedPrefs();

        // Når man åpner ResultatActivity så er runden ferdig!
        application.ongoingRunde = false;

        // Må defineres utenfor onClickListener.
        Intent intentSpillActivity =new Intent(this, SpillActivity.class);
        Intent intentStartActivity =new Intent(this, StartActivity.class);

        resultatKnappNyttSpill = findViewById(R.id.resultatKnappNyttSpill);
        resultatKnappAvslutt = findViewById(R.id.resultatKnappAvslutt);
        resultatTekstOppsummering = findViewById(R.id.resultatTekstOppsummering);
        resultatTekstAlleSvar = findViewById(R.id.resultatTekstAlleSvar);

        lagSpannableStringForResultatene();

        resultatKnappNyttSpill.setOnClickListener(v -> {
            startActivity(intentSpillActivity);
            finish(); // Ikke beholde resultatActivity i stack.
        });

        resultatKnappAvslutt.setOnClickListener(v -> {
            startActivity(intentStartActivity);
            ActivityCompat.finishAffinity(ResultatActivity.this);
        });
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

    void lagSpannableStringForResultatene(){
        spannableStringBuilder = new SpannableStringBuilder();

        int start = 0;
        int currentRegnestykke = 0; // Denne BLIR brukt! Ikke hør på Android Studio her!
        int antallRegnestykker = application.getRegnestykkeSvar().size();

        oppsummeringBuilder = new SpannableStringBuilder();

        // Først, legge til oppsummeringen:
        String resultatOversikt = getString(R.string.du_fikk_rett_paa) + " ";
        oppsummeringBuilder.append(resultatOversikt);
        start = oppsummeringBuilder.length();
        oppsummeringBuilder.append(String.valueOf(application.getAntallRiktige()));
        oppsummeringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, oppsummeringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        oppsummeringBuilder.setSpan(new ForegroundColorSpan(getColor(R.color.green)), start, oppsummeringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        String av = " " + getString(R.string.av) + " ";
        oppsummeringBuilder.append(av);
        start = oppsummeringBuilder.length();
        oppsummeringBuilder.append(String.valueOf(application.getSharedPreferences().getInt("sporsmaal", 5)));
        oppsummeringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, oppsummeringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        String regnestykkerStreng = " " + getString(R.string.resultat_regnestykker) + "!";
        oppsummeringBuilder.append(regnestykkerStreng);
        oppsummeringBuilder.setSpan(new RelativeSizeSpan(1.25f), 0, oppsummeringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        resultatTekstOppsummering.setText(oppsummeringBuilder);

        for(Integer[] regnestykke : application.getRegnestykkeSvar()){
            currentRegnestykke++;
            start = spannableStringBuilder.length();
            String regnestykkeTittelString = getString(R.string.resultat_regnestykke) + " " + regnestykke[0] + "\n\n";
            spannableStringBuilder.append(regnestykkeTittelString);
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            String utregningString = regnestykke[1] + " + " + regnestykke[2] + " = ";
            spannableStringBuilder.append(utregningString);
            start = spannableStringBuilder.length();
            spannableStringBuilder.append(regnestykke[3].toString());
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            String dittSvarStreng = "\n" + getString(R.string.resultat_ditt_svar) + " ";
            spannableStringBuilder.append(dittSvarStreng);
            start = spannableStringBuilder.length();
            spannableStringBuilder.append(regnestykke[4].toString());
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if(regnestykke[5] == 0){
                spannableStringBuilder.setSpan(new ForegroundColorSpan(getColor(R.color.orangeTomato)), start, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannableStringBuilder.setSpan(new ForegroundColorSpan(getColor(R.color.green)), start, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            spannableStringBuilder.append("\n\n\n");}

        resultatTekstAlleSvar.setText(spannableStringBuilder);
    }

}
