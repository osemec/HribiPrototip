package net.hribi.fri;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class Launcher extends AppCompatActivity {
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_activity);

        sharedPref = getApplicationContext().getSharedPreferences("hribi", 0);
        boolean logged = sharedPref.getBoolean("logged", false);
        if(logged){
            Intent i = new Intent(this,Intro.class);
            startActivity(i);
            finish();
        } else {
            Intent i = new Intent(this,Login.class);
            startActivity(i);
            finish();
        }
    }
}
