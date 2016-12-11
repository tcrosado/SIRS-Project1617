package sirs.grupo7.securepayment;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.NoSuchAlgorithmException;

import sirs.grupo7.securepayment.connections.UDP;
import sirs.grupo7.securepayment.readwrite.ReadWriteInfo;


public class MainActivity extends AppCompatActivity {

    private String MYIBAN;
    private Button buttonIBAN;
    private String CURRENT_BALANCE;
    private String cod;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private class CommunicationTask extends AsyncTask<Void, Void, Void> {

        String res;
        TextView textViewShowBalance;
        boolean r;

        @Override
        protected void onPreExecute() {
            textViewShowBalance = (TextView) findViewById(R.id.textViewCurrentBalance);
        }

        @Override
        protected Void doInBackground(Void... params) {

            showCurrentBalance();

            try {
                //Context context = getApplicationContext();
                //Toast toast = Toast.makeText(context, "START", Toast.LENGTH_SHORT);
                //toast.show();
                //System.err.println("================== START");

                //String mode = "USE_SKIP_DH_PARAMS";

                //DHExchanger keyAgree = new DHExchanger();

                //keyAgree.run(mode);
                //System.err.println("================== STOP");

                //Toast toast2 = Toast.makeText(context, "STOP", Toast.LENGTH_SHORT);
                //toast2.show();
            } catch (Exception e) {
                System.err.println("Error: " + e);
            }

            return null;
        }

        public void showCurrentBalance() {

            UDP udp = new UDP(read(ReadWriteInfo.IP), getApplicationContext());

            try {
                CURRENT_BALANCE = udp.showBalance(MYIBAN, cod);
                res = CURRENT_BALANCE + " €";
            } catch (IOException e) {
                res = getResources().getString(R.string.errorGettingBalance);
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            textViewShowBalance.setText(res);
            if (!r) {
                textViewShowBalance.setTextSize(40);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MYIBAN = read(ReadWriteInfo.IBAN);

        new CommunicationTask().execute();

        cod = (String) getIntent().getExtras().get("cod");
        System.out.println("CODE = " + cod);

        buttonIBAN = (Button) findViewById(R.id.buttonShowIBAN);
        final Context context = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        buttonIBAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
                try {
                    BitMatrix bitMatrix = multiFormatWriter.encode(MYIBAN, BarcodeFormat.QR_CODE, 200, 200);
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                    Intent intent = new Intent(context, QrActivity.class);
                    intent.putExtra("picIBAN", bitmap);
                    intent.putExtra("cod", cod);
                    intent.putExtra("textShowIBAN", MYIBAN);
                    context.startActivity(intent);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    /**
     * Called when the user clicks the Transaction button
     */
    public void goToTransaction(View view) {
        // Do something in response to button
        Intent intent = new Intent(MainActivity.this, TransactionActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Balance button
     */
    public void goToBalance(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, BalanceActivity.class);
        intent.putExtra("cod", cod);
        startActivity(intent);
    }

    /**
     * Called when the user clicks the Balance button
     */
    public void goToInitial(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
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
