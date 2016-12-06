package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Pattern;

public class StartFourth extends Activity {

    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private Button prev;
    private Button next;
    private Button scan;
    private String MYIBAN;
    private String MYCODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_fourth);
        final Activity activity = this;

        MYIBAN = (String) getIntent().getExtras().get("MYIBAN");

        scan = (Button) findViewById(R.id.codeQRCode);
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

        prev = (Button) findViewById(R.id.button_start_fourth_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartFourth.this, StartSecond.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        next = (Button) findViewById(R.id.button_start_fourth_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parseCode()) {
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
                if (isCode(result.getContents())) {
                    MYCODE = result.getContents();
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
        intent.putExtra("MYCODE", MYCODE);
        startActivity(intent);
        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
    }

    private boolean isCode(String possibleCode) {
        String patternCode = "[a-zA-Z0-9]{23}";
        Pattern p = Pattern.compile(patternCode);
        return p.matches(patternCode, possibleCode);
    }

    private boolean parseCode() {
        TextView textViewRegion = (TextView) findViewById(R.id.iban_region);
        TextView textViewNumbers = (TextView) findViewById(R.id.iban_numbers);

        String region = textViewRegion.getText().toString().toUpperCase();
        String numbers = textViewNumbers.getText().toString();

        String patternNumbers = "[a-zA-Z0-9]{23}";

        Pattern pCode = Pattern.compile(patternNumbers);

        boolean pC = pCode.matches(patternNumbers, numbers);

        if (pC) {
            MYIBAN = region + numbers;
            return true;
        } else {
            Toast.makeText(this, "Invalid Code\nTry Again", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
