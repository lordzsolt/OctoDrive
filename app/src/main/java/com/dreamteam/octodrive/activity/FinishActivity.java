package com.dreamteam.octodrive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.octodrive.R;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);

        boolean passed = false;
        int correct = -1, total = -1;

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.containsKey("passed")) {
            passed  = bundle.getBoolean("passed");
            correct = bundle.getInt("correct", -1);
            total   = bundle.getInt("total", -1);
        }

        if (correct == -1 || total == -1) {
            Toast.makeText(FinishActivity.this, R.string.error_invalid_intent, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(FinishActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            TextView tvHead = (TextView)findViewById(R.id.textView_finish_header);
            tvHead.setText(passed ? R.string.text_passed : R.string.text_failed);

            TextView tvSubhead = (TextView)findViewById(R.id.textView_finish_subheader);
            tvSubhead.setText(String.format(getString(R.string.text_correct_ans), correct, total));
        }
    }

}
