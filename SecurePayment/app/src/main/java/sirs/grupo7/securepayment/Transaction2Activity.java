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

        try {
            // Just to put the text in the currentEditText
            currencyEditText.setText((String) getIntent().getExtras().get("text"));
        } catch (NullPointerException e ){
            // Nothing
        }

        buttonConfirm = (Button) findViewById(R.id.button_transaction2_confirm);
        buttonCancel = (Button) findViewById(R.id.button_transaction2_cancel);

        final Activity activity = this;
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickHelper(activity, true);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickHelper(activity, false);
            }
        });
    }

    private void buttonClickHelper(Activity activity, boolean flag) {
        current = Long.toString(currencyEditText.getRawValue());
        if (!current.equals("0") || !flag) {
            Intent intent = new Intent(activity, TransactionConfirmationActivity.class);
            intent.putExtra("destIBAN", destIBAN);
            intent.putExtra("current", current);
            intent.putExtra("flag", flag);
            intent.putExtra("text", currencyEditText.getText().toString());
            startActivity(intent);
        } else {
            String message = getResources().getString(R.string.transaction2_input_no_money) + " " + currencyEditText.getCurrency();
            Toast.makeText(activity, message , Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
        // Nothing
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
