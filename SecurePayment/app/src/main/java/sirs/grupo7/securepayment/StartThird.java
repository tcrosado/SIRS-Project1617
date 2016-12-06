package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartThird extends Activity {

    private Button prev;
    private Button next;
    private String MYIBAN;
    private String MYCODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_third);

        MYIBAN = (String) getIntent().getExtras().get("MYIBAN");
        MYCODE = (String) getIntent().getExtras().get("MYCODE");

        prev = (Button) findViewById(R.id.button_start_third_prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartThird.this, StartFourth.class);
                intent.putExtra("MYIBAN", MYIBAN);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
            }
        });

        next = (Button) findViewById(R.id.button_start_third_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor edit = prefs.edit();
                edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                edit.commit();
                Intent intent = new Intent(StartThird.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
    }
}
