package com.dreamteam.octodrive.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.activity.admin.AdminActivity;

public class MainActivity extends AppCompatActivity {

    private String userId;
    private boolean userIsAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.containsKey("userId")) {
            userId = bundle.getString("userId");
            userIsAdmin = bundle.getBoolean("userIsAdmin", false);
        }

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(MainActivity.this, R.string.error_invalid_intent, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if (userIsAdmin) {
            Button btnAdmin = (Button)findViewById(R.id.button_admin);
            btnAdmin.setVisibility(View.VISIBLE);
            btnAdmin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                    intent.putExtra("userId", userId);
                    startActivity(intent);
                }
            });
        }

        Button btnPractice = (Button)findViewById(R.id.button_practice);
        btnPractice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("isPractice", true);
                startActivity(intent);
            }
        });

        Button btnExam = (Button)findViewById(R.id.button_exam);
        btnExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("isPractice", false);
                startActivity(intent);
            }
        });
    }
}
