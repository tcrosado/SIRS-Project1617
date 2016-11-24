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
import java.io.IOException;
import java.io.StringReader;

import sirs.grupo7.securepayment.connections.UDP;

public class MakingTransactionActivity extends AppCompatActivity {

    private Button buttonDone;
    private String moneyToTransfer;
    private String myIBAN;
    private String destIBAN;
    private TextView textView;

    private String HOSTNAME = "localhost";
    private int PORT = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_making_transaction);
        final Activity activity = this;

        buttonDone = (Button) findViewById(R.id.button_done);
        textView = (TextView) findViewById(R.id.text_done);

        myIBAN = (String) getIntent().getExtras().get("myIBAN");
        destIBAN = (String) getIntent().getExtras().get("destIBAN");
        moneyToTransfer = (String) getIntent().getExtras().get("moneyToTransfer");

        UDP udp = new UDP(HOSTNAME, PORT);
        try {
            udp.makeTransaction(myIBAN, destIBAN, moneyToTransfer);
            textView.setText(moneyToTransfer + " " + getResources().getString(R.string.transferSuccess) + "\n" + destIBAN);
        } catch (IOException e) {
            textView.setText(getResources().getString(R.string.errorMakingTransaction));
            e.printStackTrace();
        }

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_making_transaction, menu);
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
}
