package sirs.grupo7.securepayment;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class TransactionConfirmationActivity extends AppCompatActivity {

    private String destIBAN;
    private String current = "";
    private Button buttonConfirm;
    private Button buttonCancel;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_confirmation);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        buttonConfirm = (Button) findViewById(R.id.transaction_confirmaton_confirm);
        buttonCancel = (Button) findViewById(R.id.transaction_confirmaton_cancel);
        destIBAN = (String) getIntent().getExtras().get("destIBAN");
        current = (String) getIntent().getExtras().get("current");
        textView = (TextView) findViewById(R.id.transaction_confirmaton_message);

        if ((Boolean) getIntent().getExtras().get("flag")) {
            buttonConfirm.setBackgroundColor(Color.GREEN);
            buttonCancel.setBackgroundColor(Color.RED);
            String message = getResources().getString(R.string.transaction_confirmaton_confirm_message1) + current +
                             getResources().getString(R.string.transaction_confirmaton_confirm_message2) + destIBAN + "?";
            textView.setText(message);
        } else {
            buttonConfirm.setBackgroundColor(Color.RED);
            buttonCancel.setBackgroundColor(Color.GREEN);
            String message = getResources().getString(R.string.transaction_confirmaton_cancel_message1) + "?";
            textView.setText(message);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_transaction_confirmation, menu);
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
