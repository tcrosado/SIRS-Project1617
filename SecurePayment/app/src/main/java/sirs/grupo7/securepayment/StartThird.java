package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import sirs.grupo7.securepayment.connections.UDP;
import sirs.grupo7.securepayment.encryption.AESFileEncryption;

public class StartThird extends Activity {

    private Button prev;
    private Button finish;
    private String MYIBAN;
    private String MYCODE;
    private String code;


    private class WritingDataTask extends AsyncTask<Void, Void, Void> {

        TextView textView;
        String res = "o";

        @Override
        protected void onPreExecute() {
            textView = (TextView) findViewById(R.id.text_done);
        }

        @Override
        protected Void doInBackground(Void... params) {
            AESFileEncryption aes = new AESFileEncryption("settings.txt");

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_third);

        MYIBAN = (String) getIntent().getExtras().get("MYIBAN");
        MYCODE = (String) getIntent().getExtras().get("MYCODE");
        code = (String) getIntent().getExtras().get("code");

        prev = (Button) findViewById(R.id.button_start_third_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartThird.this, StartFourth.class);
                intent.putExtra("MYIBAN", MYIBAN);
                intent.putExtra("code", code);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        finish = (Button) findViewById(R.id.button_start_third_next);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                edit.commit();
                Intent intent = new Intent(StartThird.this, LoginActivity.class);

                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
    }
}
