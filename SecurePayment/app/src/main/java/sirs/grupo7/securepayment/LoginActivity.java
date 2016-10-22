package sirs.grupo7.securepayment;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class LoginActivity extends Activity {

    private int MAX_PASS_LENGHT = 4;
    private int count;
    private TextView[] textViews;
    private String password;


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

    public void buttonPressed(View v) {
        String buttonNumber = v.getTag().toString();
        passwordControl(buttonNumber);
    }

    private void passwordControl(String number) {
        if (this.count == 4) {
            password = "";
            for (TextView textView : this.textViews) {
                textView.setText("");
            }
            this.count = 0;
        }
        this.password += number;
        textViews[this.count].setText("*");
        this.count += 1;
    }
}
