package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TransactionConfirmationActivity extends Activity {

    private String destIBAN;
    private String current = "";
    private Button buttonConfirm;
    private Button buttonCancel;
    private TextView textView;
    private String text;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_confirmation);

        // FULL SCREEN NOT WORKING -- DONT KNOW WHY
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        buttonConfirm = (Button) findViewById(R.id.transaction_confirmaton_confirm);
        buttonCancel = (Button) findViewById(R.id.transaction_confirmaton_cancel);
        textView = (TextView) findViewById(R.id.transaction_confirmaton_message);

        destIBAN = (String) getIntent().getExtras().get("destIBAN");
        current = (String) getIntent().getExtras().get("current");
        text = (String) getIntent().getExtras().get("text");
        flag = (Boolean) getIntent().getExtras().get("flag");

        if ((Boolean) getIntent().getExtras().get("flag")) {
            buttonConfirm.setBackgroundColor(Color.GREEN);
            buttonCancel.setBackgroundColor(Color.RED);
            String message = getResources().getString(R.string.transaction_confirmaton_confirm_message1) + text + " " +
                             getResources().getString(R.string.transaction_confirmaton_confirm_message2) + destIBAN + "?";
            textView.setText(message);
        } else {
            buttonConfirm.setBackgroundColor(Color.GREEN);
            buttonCancel.setBackgroundColor(Color.RED);
            String message = getResources().getString(R.string.transaction_confirmaton_cancel_message1) + "?";
            textView.setText(message);
        }

        final Activity activity = this;
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra("fromTransaction", true);
                    startActivity(intent);
                } else {
                    allAbortedRemoveEverythingBeforeTheAttackersRealise(activity);
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag) {
                    allAbortedRemoveEverythingBeforeTheAttackersRealise(activity);
                } else {
                    Intent intent = new Intent(activity, Transaction2Activity.class);
                    intent.putExtra("destIBAN", destIBAN);
                    intent.putExtra("text", text);
                    startActivity(intent);
                }
            }
        });
    }

    private void allAbortedRemoveEverythingBeforeTheAttackersRealise(Activity activity) {
        getIntent().removeExtra("destIBAN");
        getIntent().removeExtra("current");
        getIntent().removeExtra("text");
        getIntent().removeExtra("flag");
        String mText = getResources().getString(R.string.transaction_canceled);
        Toast.makeText(activity, mText, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(activity, MainActivity.class);
        startActivity(intent);
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
