package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;

public class LoginActivity extends Activity {

    private int MAX_PASS_LENGHT = 4;
    //public final static String MY_IBAN = "PT09876543210987654321098";
    //public final static String MY_IBAN = "PT12345678901234567890123";
    private int count;
    private TextView[] textViews;
    private String password;
    //private String USER_PASSWORD = "9513";
    private int BAD_PASSWORD_TRIES = 3;
    private String codeToConfirm;
    private int startTries;

    private String fromWhere;

    private TextView mainView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        this.count = 0;
        this.password = "";
        this.textViews = new TextView[MAX_PASS_LENGHT];
        this.textViews[0] = (TextView) findViewById(R.id.textPass0);
        this.textViews[1] = (TextView) findViewById(R.id.textPass1);
        this.textViews[2] = (TextView) findViewById(R.id.textPass2);
        this.textViews[3] = (TextView) findViewById(R.id.textPass3);


        try {
            fromWhere = (String) getIntent().getExtras().get("fromWhere");
        } catch (NullPointerException e) {
            fromWhere = "new";
        }

        if (fromWhere.equals("start")) {
            Button log = (Button) findViewById(R.id.buttonLogin);
            log.setText(getResources().getString(R.string.login_confirm1));
            mainView = (TextView) findViewById(R.id.textView);
            mainView.setText(getString(R.string.start_login_main));
            codeToConfirm = "";
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void buttonPressed(View v) {
        String buttonNumber = v.getTag().toString();
        passwordControl(buttonNumber);
    }

    private void passwordControl(String number) {
        if (this.count == 4) {
        } else {
            this.password += number;
            textViews[this.count].setText("*");
            this.count += 1;
        }
    }

    public void loginUser(View view) {
        //if (BAD_PASSWORD_TRIES == -1) {
        //    toastPrinter("       Account Blocked\n     Please contact your\nbank for more information", Toast.LENGTH_LONG);
        //
        //} else
        if (this.password.length() == 0) {
            toastPrinter("You need to provide a password", Toast.LENGTH_SHORT);
        } else {
            if (this.password.length() == 4) { // && this.password.equals(this.USER_PASSWORD)) {
                switch (fromWhere) {
                    case "transaction": {
                        String destIBAN = (String) getIntent().getExtras().get("destIBAN");
                        //System.out.println(";;;;;;;;;;;;;;; ");
                        Button log = (Button) findViewById(R.id.buttonLogin);
                        log.setText(getResources().getString(R.string.login_confirm3));
                        TextView text = (TextView) findViewById(R.id.textView);
                        text.setText(getResources().getString(R.string.login_confirm4));
                        String moneyToTransfer = (String) getIntent().getExtras().get("moneyToTransfer");
                        Intent intent = new Intent(LoginActivity.this, MakingTransactionActivity.class);
                        intent.putExtra("nowWhat", "normal");
                        //intent.putExtra("myIBAN", MY_IBAN);
                        intent.putExtra("destIBAN", destIBAN);
                        intent.putExtra("moneyToTransfer", moneyToTransfer);
                        //System.out.println("LOGIN MY_IBAN = " + MY_IBAN);
                        intent.putExtra("cod", this.password);
                        startActivity(intent);

                        break;
                    }
                    case "start": {
                        if (codeToConfirm == "") {
                            codeToConfirm = this.password;
                            resetPassword();
                            mainView.setText(getString(R.string.start_login_main2));
                            Button log = (Button) findViewById(R.id.buttonLogin);
                            log.setText(getResources().getString(R.string.login_confirm2));
                        } else {
                            if (codeToConfirm.equals(password)) {
                                Intent intent = new Intent(LoginActivity.this, StartSecond.class);
                                intent.putExtra("code", this.password);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                            } else {
                                codeToConfirm = "";
                                resetPassword();
                                mainView.setText(getString(R.string.start_login_main3));
                            }
                        }

                        break;
                    }
                    case "finish": {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("cod", this.password);
                        startActivity(intent);

                        break;
                    }
                    default: {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("cod", this.password);
                        startActivity(intent);

                        break;
                    }
                }
            } else {
                resetPassword();
                toastPrinter(getString(R.string.login_password_less), Toast.LENGTH_LONG);

                //if (BAD_PASSWORD_TRIES == 0) {
                //    toastPrinter("       Account Blocked\n     Please contact your\nbank for more information", Toast.LENGTH_LONG);
                //} else {
                //    toastPrinter("      Bad Password\nYou have " + BAD_PASSWORD_TRIES + " more tries", Toast.LENGTH_SHORT);
                //}
                //BAD_PASSWORD_TRIES -= 1;
            }
        }
    }

    public void clearKey(View v) {
        resetPassword();
    }

    private void resetPassword() {
        password = "";
        for (TextView textView : this.textViews) {
            textView.setText("");
        }
        this.count = 0;
    }

    private void toastPrinter(CharSequence text, int duration){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

}
