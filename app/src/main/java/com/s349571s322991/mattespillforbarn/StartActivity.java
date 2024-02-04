package com.s349571s322991.mattespillforbarn;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    ApplicationClass application;
    SharedPreferences sharedPrefs;
    ImageButton imageButtonStart;
    ImageButton imageButtonOm;
    ImageButton imageButtonPreferanser;

    void getApplicationClass(){
        application = (ApplicationClass) getApplicationContext();
        Log.d("Erlend", "StartActivity ~ application: "+ application); // Sjekke om den er null eller ikke.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        getApplicationClass();
        setupSharedPreferences();

        Intent intentSpillActivity = new Intent(this, SpillActivity.class);
        Intent intentOmActivity = new Intent(this, OmActivity.class);
        Intent intentPreferanserActivity = new Intent(this, PreferanserActivity.class);

        imageButtonStart = findViewById(R.id.imageStartSpillet);
        imageButtonOm = findViewById(R.id.imageOmSpillet);
        imageButtonPreferanser = findViewById(R.id.imagePreferanser);

        imageButtonStart.setOnClickListener(v -> startActivity(intentSpillActivity));
        imageButtonOm.setOnClickListener(v -> startActivity(intentOmActivity));
        imageButtonPreferanser.setOnClickListener(v -> startActivity(intentPreferanserActivity));
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Resetter spillet når StartActivity starter.
        application.ongoingRunde = false;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    void setupSharedPreferences(){
        // slettSharedPreferences(); // For å resette.

        sharedPrefs = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
        if(sharedPrefs == null){
            Log.d("Erlend", "MinePreferanser eksisterer ikke. Lager ny!");
            lagSharedPreferences();
        } else {
            Log.d("Erlend", "MinePreferanser eksisterer!");
            sjekkSharedPreferences();
        }
        lagreSharedPreferencesIApplicationClass(); // Lagrer sharedPrefs i ApplicationClass.
    }

    void slettSharedPreferences(){
        getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE).edit().clear().apply();

        if(sharedPrefs == null) {
            Log.d("Erlend", "slettSharedPreferences ~ sharedPrefs er null!");
        } else {
            Log.d("Erlend", "slettSharedPreferences ~ sharedPrefs er ikke null.");
        }
    }

    // Sette default verdier for preferansene.
    // Preferansene er:
    // 1. Antall spørsmål - 5, 10, 15.      // Int
    // 2. Språk: no (default), de (Tysk)    // String
    void lagSharedPreferences(){
        sharedPrefs = getSharedPreferences("MinePreferanser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefs.edit();

        editor.putString(application.SHARED_SPRAAK, application.SHARED_SPRAAK_NORSK);
        editor.putInt(application.SHARED_ANTALL_SPORSMAAL, 5);
        editor.putInt(application.SHARED_CURRENT_SPORSMAAL, 0); // Spørsmål nr 0 tilsvarer spørsmål "1". Men på indeks 0.
        editor.apply();
        // sjekkSharedPreferences(); // DEBUG: Sjekker.
    }

    void sjekkSharedPreferences(){
        if(sharedPrefs != null){
            String spraak = sharedPrefs.getString("spraak", "no");
            int sporsmaal = sharedPrefs.getInt("sporsmaal" , 5);
            Log.d("Erlend", "språk:" + spraak + ", spørsmål: " + sporsmaal);
        } else {
            Log.d("Erlend", "sjekkSharedPreferences ~ sharedPrefs er null!");
        }
    }

    // NOTE: Debug funksjon.
    void sjekkSharedPreferencesFraApplicationClass(){
        if(application != null){
            SharedPreferences shared = application.getSharedPreferences();
            if(shared != null){
                Log.d("Erlend", "shared eksisterer!");
                String spraak = shared.getString("spraak", "no");
                int sporsmaal = shared.getInt("sporsmaal" , 5);
                Log.d("Erlend", "språk:" + spraak + ", spørsmål: " + sporsmaal);
            } else {
                Log.d("Erlend", "shared er null!");
            }
        } else {
            Log.d("Erlend", "sjekkSharedPreferencesFraApplicationClass ~ application er null!");
        }
    }

    void lagreSharedPreferencesIApplicationClass(){
        application.setSharedPreferences(sharedPrefs);
        // SharedPreferences shared = application.getSharedPreferences(); // DEBUG
        // sjekkSharedPreferencesFraApplicationClass(); // DEBUG
    }

}
