package sirs.grupo7.securepayment;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import sirs.grupo7.securepayment.connections.UDP;

public class BalanceActivity extends AppCompatActivity {

    private String MY_IBAN;
    private TextView textViewBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        textViewBalance = (TextView) findViewById(R.id.textViewBalance);
        textViewBalance.setText("WILL\nWE\nDO\nIT?");
        //textViewBalance.setText(requestBalance(MY_IBAN));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_balance, menu);
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

    public String requestBalance(String IBAN) {
        UDP udp = new UDP();
        try {
            String message = udp.showHistory(MY_IBAN);
            if (message.equals("ERROR")) {
                return getResources().getString(R.string.errorGettingHistory);
            } else {
                return message;
            }
        } catch (IOException e) {
            return getResources().getString(R.string.errorGettingHistory);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
