package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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


public class TransactionActivity extends Activity {

    private String destIBAN;
    private static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        button = (Button) findViewById(R.id.transactionQRCode);
        final Activity activity = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        button.setOnClickListener(new View.OnClickListener() {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            //noinspection SimplifiableIfStatement
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void transactionSubmitIBAN(View view) {
        // TODO send destiny IBAN to server
        parseIBAN();
    }

    public void goToQRCode(View view) {
        Intent intent = new Intent(ACTION_SCAN);
        intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
        startActivityForResult(intent, 1);
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
                    destIBAN = result.getContents();
                    Toast.makeText(this, "Success\nDestiny IBAN: " + result.getContents(), Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Success\nDestiny IBAN: " + result.getContents(), Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "Success\nDestiny IBAN: " + result.getContents(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Error\nInput is not an IBAN", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }

    private void toastPrinter(CharSequence text, int duration){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private boolean isIBAN(String possibleIBAN) {
        String patternIBAN = "[A-Z]{2}[0-9]{23}";
        Pattern p = Pattern.compile(patternIBAN);
        return p.matches(patternIBAN, possibleIBAN);
    }

    private void parseIBAN() {
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
            destIBAN = region + numbers;
        } else if (!pR && !pN) {
            Toast.makeText(this, "Invalid IBAN", Toast.LENGTH_LONG).show();
        } else if (pR) {
            Toast.makeText(this, "Invalid IBAN\nCheck the numbers", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Invalid IBAN\nCheck the region", Toast.LENGTH_LONG).show();
        }
    }
}
