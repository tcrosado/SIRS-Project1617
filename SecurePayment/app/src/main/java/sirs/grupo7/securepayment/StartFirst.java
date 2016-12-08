package sirs.grupo7.securepayment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class StartFirst extends Activity {

    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_first);
        next = (Button) findViewById(R.id.button_start_first_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartFirst.this, LoginActivity.class);
                intent.putExtra("fromStart", true);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor edit = prefs.edit();
        boolean previouslyStarted = prefs.getBoolean(getString(R.string.pref_previously_started), false);
        if (previouslyStarted) {
            //edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
            //edit.commit();
            final Activity activity = this;
            Intent intent = new Intent(activity, LoginActivity.class);
            startActivity(intent);
        }
    }
}
