package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import sirs.grupo7.securepayment.readwrite.ReadWriteInfo;

public class StartSecond extends Activity {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private String MYIBAN;
    private String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_second);

        toastPrinter(read(ReadWriteInfo.IP), Toast.LENGTH_LONG);

        final Activity activity = this;

        code = (String) getIntent().getExtras().get("code");

        Button scan = (Button) findViewById(R.id.transactionQRCode);
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

        Button prev = (Button) findViewById(R.id.button_start_second_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartSecond.this, LoginActivity.class);
                intent.putExtra("fromWhere", "start");
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        Button next = (Button) findViewById(R.id.button_start_second_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parseIBAN()) {
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
                if (isIBAN(result.getContents())) {
                    MYIBAN = result.getContents();
                    //Toast.makeText(this, "Success\nDestiny IBAN: " + result.getContents(), Toast.LENGTH_LONG).show();
                    goToNextActivity();
                } else {
                    Toast.makeText(this, "Error\nInput is not an IBAN", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    private void goToNextActivity() {
        Intent intent = new Intent(this, StartFourth.class);
        intent.putExtra("MYIBAN", MYIBAN);
        intent.putExtra("code", code);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    private boolean isIBAN(String possibleIBAN) {
        String patternIBAN = "[A-Z]{2}[0-9]{23}";
        Pattern p = Pattern.compile(patternIBAN);
        return p.matches(patternIBAN, possibleIBAN);
    }

    private boolean parseIBAN() {
        TextView textViewRegion = (TextView) findViewById(R.id.iban_region);
        TextView textViewNumbers = (TextView) findViewById(R.id.iban_numbers);

        String region = textViewRegion.getText().toString().toUpperCase();
        String numbers = textViewNumbers.getText().toString();

        String patternRegion = "[A-Z]{2}";
        String patternNumbers = "[0-9]{23}";

        Pattern pRegion = Pattern.compile(patternRegion);
        Pattern pNumbers = Pattern.compile(patternNumbers);

        boolean pR = pRegion.matches(patternRegion, region);
        boolean pN = pNumbers.matches(patternNumbers, numbers);

        if (pR && pN) {
            MYIBAN = region + numbers;
            return true;
        } else if (!pR && !pN) {
            Toast.makeText(this, "Invalid IBAN", Toast.LENGTH_LONG).show();
        } else if (pR) {
            Toast.makeText(this, "Invalid IBAN\nCheck the numbers", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Invalid IBAN\nCheck the region", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        // Nothing
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

    private void toastPrinter(CharSequence text, int duration){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
}