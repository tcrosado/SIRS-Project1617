package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sirs.grupo7.securepayment.encryption.AESFileEncryption;
import sirs.grupo7.securepayment.readwrite.ReadWriteInfo;

public class StartFourth extends Activity {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    //private String MYIBAN;
    private String MYCODE;
    private String IV;
    private String code;
    private String qr_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_fourth);
        final Activity activity = this;

        //MYIBAN = (String) getIntent().getExtras().get("MYIBAN");
        code = (String) getIntent().getExtras().get("code");

        Button scan = (Button) findViewById(R.id.codeQRCode);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        Button prev = (Button) findViewById(R.id.button_start_fourth_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartFourth.this, StartSecond.class);
                intent.putExtra("code", code);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        Button next = (Button) findViewById(R.id.button_start_fourth_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCode(getText())) {
                    System.out.println("INPUT = " + getText());
                    parseCode();
                    goToNextActivity();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                qr_input = result.getContents();
                if (isCode(qr_input)) {
                    //Toast.makeText(this, "Success\nDestiny IBAN: " + result.getContents(), Toast.LENGTH_LONG).show();
                    System.out.println("INPUT = " + qr_input);
                    parseCode(qr_input);
                    goToNextActivity();
                } else {
                    Toast.makeText(this, "Error\nInput is not a code", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    private void goToNextActivity() {

        AESFileEncryption aes = new AESFileEncryption(getApplicationContext());

        try {
            write(ReadWriteInfo.IV, IV.getBytes());
            byte[] o = aes.encrypt(makeHash(code), MYCODE.getBytes(), Base64.decode(IV.getBytes(), Base64.NO_WRAP));
            write(ReadWriteInfo.KEY, Base64.encode(o, Base64.NO_WRAP));
            String r = read(ReadWriteInfo.KEY);
            byte[] sb = r.getBytes();
            //System.out.println("BEFORE");
            //System.out.println(aes.decrypt(code, sb));
            //System.out.println("AFTER");
            //System.out.println(Arrays.toString(aes.decrypt(code.getBytes(), read(ReadWriteInfo.KEY).getBytes())));
        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | BadPaddingException | InvalidParameterSpecException | InvalidKeySpecException | IllegalBlockSizeException | InvalidKeyException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, StartThird.class);
        //intent.putExtra("MYIBAN", MYIBAN);
        //intent.putExtra("MYCODE", MYCODE);
        //intent.putExtra("IV", IV);
        intent.putExtra("code", code);

        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    private byte[] makeHash(String code) throws NoSuchAlgorithmException, IOException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(code.getBytes());
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

    private boolean isCode(String input) {
        if (input.split("==").length == 2) {
            return true;
        } else {
            return false;
        }
    }

    private void parseCode() {
        String[] bases = getText().split("==");

        System.out.println("IV = " + bases[0] + "==");
        System.out.println("CODE = " + bases[1] + "==");

        IV = bases[0] + "==";
        MYCODE = bases[1] + "==";
    }

    private void parseCode(String toParse) {
        String[] bases = toParse.split("==");

        System.out.println("IV = " + bases[0] + "==");
        System.out.println("CODE = " + bases[1] + "==");

        IV = bases[0] + "==";
        MYCODE = bases[1] + "==";
    }

    @Override
    public void onBackPressed() {
        // Nothing
    }

    private String getText() {
        TextView textViewNumbers = (TextView) findViewById(R.id.iban_numbers);
        return textViewNumbers.getText().toString();
    }


    public void write(String filename, byte[] message) throws IOException {
        FileOutputStream fileOutputStream = openFileOutput(filename, MODE_PRIVATE);
        fileOutputStream.write(message);
        fileOutputStream.close();
        System.out.println("\n\nDONE " + filename + "\n\n");
    }
}
