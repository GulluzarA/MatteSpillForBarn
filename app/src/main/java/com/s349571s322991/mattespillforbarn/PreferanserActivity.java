package com.s349571s322991.mattespillforbarn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.os.LocaleListCompat;

import java.util.Objects;

public class PreferanserActivity extends AppCompatActivity {

    ApplicationClass application;
    ImageButton preferanserKnappTilbake,tall5, tall10, tall15;

    //Knapp for valg av språk
    ImageButton flaggNo, flaggDe;

    void getApplicationClass(){
        application = (ApplicationClass) getApplicationContext();
        Log.d("Erlend", "PreferanserActivity ~ application: "+ application); // Sjekke om den er null eller ikke.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferanser);

        getApplicationClass();

        // Må defineres utenfor onClickListener.
        Intent intentStartActivity =new Intent(this, StartActivity.class);

        preferanserKnappTilbake = findViewById(R.id.PreferanserKnappTilbake);
        tall5 = findViewById(R.id.tall5);
        tall10 = findViewById(R.id.tall10);
        tall15 = findViewById(R.id.tall15);
        flaggNo = findViewById(R.id.no);
        flaggDe = findViewById(R.id.de);

        aktiverDrawablesFraPreferanser();

        preferanserKnappTilbake.setOnClickListener(v -> {
            startActivity(intentStartActivity);
            ActivityCompat.finishAffinity(PreferanserActivity.this);
        });

        tall5.setOnClickListener(v -> trykkPaaKnappBilder(5));
        tall10.setOnClickListener(v -> trykkPaaKnappBilder(10));
        tall15.setOnClickListener(v -> trykkPaaKnappBilder(15));
        flaggNo.setOnClickListener(v -> trykkPaaFlagg("no"));
        flaggDe.setOnClickListener(v -> trykkPaaFlagg("de"));
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

    void aktiverDrawablesFraPreferanser(){
        switch(application.getSharedPreferences().getInt(application.SHARED_ANTALL_SPORSMAAL, 5)){
            case 5:
                tall5.setImageResource(R.drawable.colorful_five);
                break;
            case 10:
                tall10.setImageResource(R.drawable.colorful_ten);
                break;
            case 15:
                tall15.setImageResource(R.drawable.colorful_fifteen);
                break;
            default:
                break;
        }
        switch(application.getSharedPreferences().getString(application.SHARED_SPRAAK, application.SHARED_SPRAAK_NORSK)){
            case "no":
                flaggNo.setImageResource(R.drawable.norway_flag_cute);
                break;
            case "de":
                flaggDe.setImageResource(R.drawable.germany_flag_cute);
                break;
            default:
                break;
        }
    }

    void trykkPaaKnappBilder(int knappVerdi){
        int prefsAntallSporsmaal = application.getSharedPreferences().getInt(application.SHARED_ANTALL_SPORSMAAL, 5);

        if(knappVerdi != prefsAntallSporsmaal){
            // NOTE: I tilfelle man går ut av regnestykket med back-knappen og skifter på antall regnestykker,
            // så skal den resettes.
            application.ongoingRunde = false;
            application.oppdaterAntallSporsmaal(knappVerdi);
            switch(knappVerdi){
                case 5:
                    tall5.setImageResource(R.drawable.colorful_five);
                    tall10.setImageResource(R.drawable.colorful_ten_inactive);
                    tall15.setImageResource(R.drawable.colorful_fifteen_inactive);
                    break;
                case 10:
                    tall5.setImageResource(R.drawable.colorful_five_inactive);
                    tall10.setImageResource(R.drawable.colorful_ten);
                    tall15.setImageResource(R.drawable.colorful_fifteen_inactive);
                    break;
                case 15:
                    tall5.setImageResource(R.drawable.colorful_five_inactive);
                    tall10.setImageResource(R.drawable.colorful_ten_inactive);
                    tall15.setImageResource(R.drawable.colorful_fifteen);
                    break;
                default:
                    break;
            }
        }

    }

    void trykkPaaFlagg(String knappVerdi){
        String prefsSpraak = application.getSharedPreferences().getString(application.SHARED_SPRAAK, application.SHARED_SPRAAK_NORSK);

        if(!Objects.equals(knappVerdi, prefsSpraak)){
            application.oppdaterSpraak(knappVerdi);
            switch(knappVerdi){
                case "no":
                    flaggNo.setImageResource(R.drawable.norway_flag_cute);
                    flaggDe.setImageResource(R.drawable.germany_flag_cute_inactive);
                    settAppLocale("no");
                    break;
                case "de":
                    flaggNo.setImageResource(R.drawable.norway_flag_cute_inactive);
                    flaggDe.setImageResource(R.drawable.germany_flag_cute);
                    settAppLocale("de");
                    break;
                default:
                    break;
            }
        }
    }

    public void settAppLocale(String spraak) {
        String spraakTag = "no-NO";
        switch (spraak) {
            case "no":
                spraakTag = "no-NO";
                break;
            case "de":
                spraakTag = "de-DE";
                break;
            default:
                break;
        }
        LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(spraakTag);
        AppCompatDelegate.setApplicationLocales(appLocale);
    }

}
