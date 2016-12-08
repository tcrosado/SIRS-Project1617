package sirs.grupo7.securepayment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Start extends Activity {

    private Button submit;
    private TextView toAsk1;
    private TextView toAsk2;
    private TextView toAsk3;
    private String cod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        toAsk1 = (TextView) findViewById(R.id.matrix_cod1);
        toAsk2 = (TextView) findViewById(R.id.matrix_cod2);
        toAsk3 = (TextView) findViewById(R.id.matrix_cod3);

        cod = (String) getIntent().getExtras().get("cod");

        toAsk1.setText((String) getIntent().getExtras().get("toAsk1"));
        toAsk2.setText((String) getIntent().getExtras().get("toAsk2"));
        toAsk3.setText((String) getIntent().getExtras().get("toAsk3"));
        final String tid = (String) getIntent().getExtras().get("tid");

        submit = (Button) findViewById(R.id.button_start_third_next);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText cod1 = (EditText) findViewById(R.id.matrix_input_cod1);
                EditText cod2 = (EditText) findViewById(R.id.matrix_input_cod2);
                EditText cod3 = (EditText) findViewById(R.id.matrix_input_cod3);

                if (cod1.getText().length() == 1 && cod2.getText().length() == 1 && cod3.getText().length() == 1) {
                    Intent intent = new Intent(Start.this, MakingTransactionActivity.class);
                    intent.putExtra("cod1", cod1.getText());
                    intent.putExtra("cod2", cod2.getText());
                    intent.putExtra("cod3", cod3.getText());
                    intent.putExtra("tid", tid);
                    intent.putExtra("cod", cod);
                    intent.putExtra("nowWhat", "cr");
                    startActivity(intent);
                } else {
                    toastPrinter("Please fill all parameters", Toast.LENGTH_LONG);
                }
            }
        });
    }

    private void toastPrinter(CharSequence text, int duration){
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    @Override
    public void onBackPressed() {
        // Nothing
    }
}
