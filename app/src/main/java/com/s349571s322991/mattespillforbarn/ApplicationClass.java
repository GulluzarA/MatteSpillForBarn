package com.s349571s322991.mattespillforbarn;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;
import java.util.ArrayList;
import java.util.Arrays;

public class ApplicationClass extends Application {

    private SharedPreferences sharedPreferences; // Preferanser for alle aktiviteter.
    // NOTE: BARE INNSTILLINGER lagres i sharedPreferences.
    // IKKE lister, tall og strenger for regnestykker.
    ArrayList<Integer[]> regnestykker = new ArrayList<>(); // Lages ved nytt spill.
    ArrayList<Integer[]> regnestykkeSvar = new ArrayList<>(); // Regnestykker + svar. Brukt til ResultatActivity.
    int antallRiktige = 0;
    boolean ongoingRunde = false; // false når appen starter.
    public String SHARED_SPRAAK = "spraak";
    public String SHARED_SPRAAK_NORSK = "no";
    public String SHARED_SPRAAK_TYSK = "de";
    public String SHARED_ANTALL_SPORSMAAL = "sporsmaal";
    public String SHARED_CURRENT_SPORSMAAL = "sporsmaalsNr";

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
    public void setSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        Log.d("Erlend", "setSharedPreferences trigget!");
    }

    // Regnestykkene.
    // For tilstandsbevaring, trenger ikke lagre den i sharedPreferences.
    // Den vil være her etter at mobilen roteres.
    public ArrayList<Integer[]> getRegnestykker(){
        return this.regnestykker;
    }
    public void setRegnestykker(ArrayList<Integer[]> regnestykker){
        this.regnestykker = regnestykker;
        // debugPrintRegnestykkeListe(this.regnestykker); //DEBUG
    }
    void debugPrintRegnestykkeListe(ArrayList<Integer[]> liste){
        Log.d("Erlend", "ApplicationClass ~ Printer ut regnestykkeliste: ");
        for (Integer[] regnestykke : liste) {
            Log.d("Erlend", Arrays.toString(regnestykke));
        }
        Log.d("Erlend", "Ferdig med print-ut av regnestykkeliste.");
    }

    //
    public ArrayList<Integer[]> getRegnestykkeSvar(){
        return this.regnestykkeSvar;
    }
    public void setRegnestykkeSvar(ArrayList<Integer[]> regnestykkeSvar){
        this.regnestykkeSvar = regnestykkeSvar;
    }
    public void leggTilRegnestykke(Integer[] regnestykke){
        Log.d("Erlend", "leggTilRegnestykke: " + Arrays.toString(regnestykke));
        this.regnestykkeSvar.add(regnestykke);
    }
    public void clearRegnestykkeSvar(){
        this.regnestykkeSvar.clear(); // Tømmer arraylisten.
    }

    public int getAntallRiktige(){
        return this.antallRiktige;
    }
    public void setAntallRiktige(int antallRiktigeNy){
        this.antallRiktige = antallRiktigeNy;
    }
    public void inkrementerAntallRiktige(){
        this.antallRiktige++;
    }

    // Lagre verdier i sharedPreferences!
    public void oppdaterSpraak(String nyttSpraak){
        this.sharedPreferences.edit().putString(SHARED_SPRAAK, nyttSpraak).apply();
    }
    public void oppdaterAntallSporsmaal(int nyttAntall){
        this.sharedPreferences.edit().putInt(SHARED_ANTALL_SPORSMAAL, nyttAntall).apply();
    }

}
