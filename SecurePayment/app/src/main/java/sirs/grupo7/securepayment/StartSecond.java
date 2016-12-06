package sirs.grupo7.securepayment;

import android.app.Activity;
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

import java.util.regex.Pattern;

public class StartSecond extends Activity {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private Button prev;
    private Button next;
    private Button scan;
    private String MYIBAN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_second);

        final Activity activity = this;

        scan = (Button) findViewById(R.id.transactionQRCode);
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

        prev = (Button) findViewById(R.id.button_start_second_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartSecond.this, StartFirst.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        next = (Button) findViewById(R.id.button_start_second_next);
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
        Intent intent = new Intent(this, StartThird.class);
        intent.putExtra("MYIBAN", MYIBAN);
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

}