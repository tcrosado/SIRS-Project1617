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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import sirs.grupo7.securepayment.readwrite.ReadWriteInfo;

public class StartFirst extends Activity {

    private EditText ip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_first);
        final Button next = (Button) findViewById(R.id.button_start_first_next);

        /*try {
            write(ReadWriteInfo.KEY, Base64.encode("ola rosado".getBytes(), Base64.NO_WRAP));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("======================");
        System.out.println(Arrays.toString(Base64.decode(read(ReadWriteInfo.KEY), Base64.NO_WRAP)));
        System.out.println("======================");


        AESFileEncryption aes = new AESFileEncryption();
        try {
            System.out.println("---------------------------");
            byte[] o = aes.encrypt(makeHash("9999"), "ola rosado".getBytes());
            write(ReadWriteInfo.KEY, Base64.encode(o, Base64.NO_WRAP));
            byte[] p = aes.decrypt(makeHash("9999"), Base64.decode(read(ReadWriteInfo.KEY), Base64.NO_WRAP));
            System.out.println(Arrays.toString(p));
            System.out.println("---------------------------");
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidParameterSpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        */
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ip = (EditText) findViewById(R.id.ip);

                String[] input = ip.getText().toString().split("-");
                int l = input.length;

                if (l == 2 && checkIP(input[0]) && checkNumber(input[1])) {
                    try {
                        write(ReadWriteInfo.IP, input[0].getBytes());
                        write(ReadWriteInfo.NUMBER, input[1].getBytes());
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

    private byte[] makeHash(String code) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(code.getBytes());
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


    public void write(String filename, byte[] message) throws IOException {
        FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
        fileOutputStream.write(message);
        fileOutputStream.close();
        System.out.println("\n\nDONE " + filename + "\n\n");
    }

    public byte[] read(String filename, int n) {
        byte[] message = new byte[256];
        ByteArrayOutputStream bb = new ByteArrayOutputStream();
        try {
            FileInputStream fileInputStream = openFileInput(filename);
            int i = fileInputStream.read(message);
            //InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            //BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            //StringBuffer stringBuffer = new StringBuffer();
            while (i != -1) {
                bb.write(message);
                i = fileInputStream.read(message);
            }
            //return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bb.toByteArray();
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
