package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class QrActivity extends AppCompatActivity {

    private ImageView imageViewIBAN;
    private Button buttonGoBack;
    private TextView textShowIBAN;
    private String IBAN;
    private String cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        cod = (String) getIntent().getExtras().get("cod");
        imageViewIBAN = (ImageView) this.findViewById(R.id.imageViewIBAN);
        Bitmap bitmap = getIntent().getParcelableExtra("picIBAN");
        imageViewIBAN.setImageBitmap(bitmap);
        buttonGoBack = (Button) findViewById(R.id.button_qrcode_go_back);
        IBAN = (String) getIntent().getExtras().get("textShowIBAN");
        textShowIBAN = (TextView) findViewById(R.id.textShowIBAN);
        textShowIBAN.setText(IBAN);

        final Activity activity = this;
        buttonGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, MainActivity.class);
                intent.putExtra("cod", cod);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qr, menu);
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
