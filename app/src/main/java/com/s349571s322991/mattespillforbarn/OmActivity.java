package com.s349571s322991.mattespillforbarn;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class OmActivity extends AppCompatActivity {

    ApplicationClass application;
    ImageButton omKnappTilbake;
    Intent intentStartActivity; // Brukt til navigering mellom aktiviteter.
    TextView omSpill, innhold, hvordanSpille, spillForklaring, spillMaal, maalInnhold;

    void getApplicationClass(){
        application = (ApplicationClass) getApplicationContext();
        Log.d("Erlend", "StartActivity ~ application: "+ application); // Sjekke om den er null eller ikke.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_om);

        getApplicationClass();

        omSpill = findViewById(R.id.om_spillet);
        innhold = findViewById(R.id.forklaring);
        hvordanSpille = findViewById(R.id.hvordan_spille);
        spillForklaring = findViewById(R.id.spill_forklaring);
        spillMaal = findViewById(R.id.spill_maalet);
        maalInnhold = findViewById(R.id.spillmaal_innhold);


        // Må defineres utenfor onClickListener.
        intentStartActivity =new Intent(this, StartActivity.class);

        omKnappTilbake = findViewById(R.id.OmKnappTilbake);
        omKnappTilbake.setOnClickListener(v -> {
            startActivity(intentStartActivity);
            ActivityCompat.finishAffinity(OmActivity.this);
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
}
