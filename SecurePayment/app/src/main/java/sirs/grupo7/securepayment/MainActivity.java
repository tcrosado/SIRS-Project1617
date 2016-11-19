package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MainActivity extends AppCompatActivity {

    public final static String MY_IBAN = "PT12345678912345678912345";
    public final static String EXTRA_MESSAGE = "sirs.grupo7.securepayment.MESSAGE";
    private Button buttonIBAN;
    private String CURRENT_BALANCE = "500,00 €";
    private TextView textViewShowBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewShowBalance = (TextView) findViewById(R.id.textViewCurrentBalance);
        textViewShowBalance.setText(CURRENT_BALANCE);
        buttonIBAN = (Button) findViewById(R.id.buttonShowIBAN);
        final Context context = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        buttonIBAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(MY_IBAN, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    Intent intent = new Intent(context, QrActivity.class);
                    intent.putExtra("picIBAN", bitmap);
                    intent.putExtra("textShowIBAN", MY_IBAN);
                    context.startActivity(intent);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Nothing
    }

    /** Called when the user clicks the Transaction button */
    public void goToTransaction(View view) {
        // Do something in response to button
        Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Balance button */
    public void goToBalance(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, BalanceActivity.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Balance button */
    public void goToInitial(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void showCurrentBalance() {
        // TODO request current balance to the server
    }
}
