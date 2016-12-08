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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sirs.grupo7.securepayment.encryption.AESFileEncryption;
import sirs.grupo7.securepayment.readwrite.ReadWriteInfo;

public class StartThird extends Activity {

    private String MYIBAN;
    private String MYCODE;
    private String code;


    private class WritingDataTask extends AsyncTask<Void, Void, Void> {

        TextView textView;
        TextView textView2;
        TextView textView3;

        @Override
        protected void onPreExecute() {
            textView = (TextView) findViewById(R.id.start_third_text);
            textView2 = (TextView) findViewById(R.id.start_third_text2);
            textView3 = (TextView) findViewById(R.id.start_third_text3);
        }

        @Override
        protected Void doInBackground(Void... params) {
            AESFileEncryption aes = new AESFileEncryption();

            try {
                write(ReadWriteInfo.IBAN, MYIBAN);

                write(ReadWriteInfo.KEY, aes.encrypt(code, MYCODE.getBytes()));

            } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeySpecException | InvalidParameterSpecException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
                textView.setText(getResources().getString(R.string.start_third_error));
                textView2.setText(getResources().getString(R.string.start_third_error2));
                textView3.setText("");

                Button prev = (Button) findViewById(R.id.button_start_third_prev);
                prev.setVisibility(View.GONE);

                Button next = (Button) findViewById(R.id.button_start_third_next);
                next.setText(getResources().getString(R.string.button_next));
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(StartThird.this, StartFirst.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                    }
                });
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

        }

        public void write(String filename, String message) throws IOException {
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();
            System.out.println("\n\nDONE " + filename + "\n\n");
        }

        public void write(String filename, byte[] message) throws IOException {
            FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
            fileOutputStream.write(message);
            fileOutputStream.close();
            System.out.println("\n\n2222 " + filename + "\n\n");
        }

        public String read(String filename) {
            try {
                String message;
                FileInputStream fileInputStream = openFileInput(filename);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                while ((message = bufferedReader.readLine()) != null) {
                    stringBuffer.append(message);
                }
                return stringBuffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "ERROR";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_third);

        MYIBAN = (String) getIntent().getExtras().get("MYIBAN");
        MYCODE = (String) getIntent().getExtras().get("MYCODE");
        code = (String) getIntent().getExtras().get("code");

        new WritingDataTask().execute();

        Button prev = (Button) findViewById(R.id.button_start_third_prev);
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

        Button finish = (Button) findViewById(R.id.button_start_third_next);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                edit.commit();
                Intent intent = new Intent(StartThird.this, LoginActivity.class);
                intent.putExtra("fromWhere", "finish");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Nothing
    }
}
