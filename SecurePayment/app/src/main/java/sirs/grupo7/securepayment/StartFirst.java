package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import sirs.grupo7.securepayment.readwrite.ReadWriteInfo;

public class StartFirst extends Activity {

    private EditText ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_first);
        Button next = (Button) findViewById(R.id.button_start_first_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ip = (EditText) findViewById(R.id.ip);

                String[] input = ip.getText().toString().split("-");
                int l = input.length;

                if (l == 2 && checkIP(input[0]) && checkNumber(input[1])) {
                    try {
                        write(ReadWriteInfo.IP, input[0]);
                        write(ReadWriteInfo.NUMBER, input[1]);
                    } catch (IOException e) {
                        toastPrinter("SOMETHING WRONG WTH IP AND PHONE NUMBER", Toast.LENGTH_LONG);
                    }
                    Intent intent = new Intent(StartFirst.this, LoginActivity.class);
                    intent.putExtra("fromWhere", "start");
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                } else {
                    toastPrinter("ENTER IP AND PHONE-NUMBER", Toast.LENGTH_LONG);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if (previouslyStarted) {
            //edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            //edit.commit();
            final Activity activity = this;
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        // Nothing
    }

    private boolean checkIP(String ip) {
        // This is for debug. Check the ip
        String patternIP = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                           "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                           "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                           "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

        Pattern pIP = Pattern.compile(patternIP);
        return pIP.matches(patternIP, ip);
    }

    private boolean checkNumber(String number) {
        // This is for debug. Check the number
        String patternNumber = "[0-9]{9}";

        Pattern pIP = Pattern.compile(patternNumber);
        return pIP.matches(patternNumber, number);
    }

    private void toastPrinter(CharSequence text, int duration){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void write(String filename, String message) throws IOException {
        FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
        fileOutputStream.write(message.getBytes());
        fileOutputStream.close();
        System.out.println("\n\nDONE " + filename + "\n\n");
    }
}
