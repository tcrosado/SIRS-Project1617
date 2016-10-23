package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blackcat.currencyedittext.CurrencyEditText;

import java.text.NumberFormat;
import java.util.Currency;

public class Transaction2Activity extends Activity {

    private String destIBAN;
    private TextView textViewDestIBAN;
    private CurrencyEditText currencyEditText;
    private Button buttonCancel;
    private Button buttonConfirm;

    private String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_transaction2);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        destIBAN = (String) getIntent().getExtras().get("destIBAN");
        textViewDestIBAN = (TextView) findViewById(R.id.transactionTextViewDestIBAN);
        textViewDestIBAN.setText(destIBAN);

        currencyEditText = (CurrencyEditText) findViewById(R.id.editTextCurrencyInput);

        buttonConfirm = (Button) findViewById(R.id.button_transaction2_confirm);
        buttonCancel = (Button) findViewById(R.id.button_transaction2_cancel);
        final Activity activity = this;
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = Long.toString(currencyEditText.getRawValue());
                //Toast.makeText(activity, current, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, TransactionConfirmationActivity.class);
                intent.putExtra("destIBAN", destIBAN);
                intent.putExtra("current", current);
                intent.putExtra("flag", true);
                startActivity(intent);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = Long.toString(currencyEditText.getRawValue());
                Intent intent = new Intent(activity, TransactionConfirmationActivity.class);
                intent.putExtra("destIBAN", destIBAN);
                intent.putExtra("current", current);
                intent.putExtra("flag", false);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction2, menu);
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
}
