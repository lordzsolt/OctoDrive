package com.dreamteam.octodrive.activity.admin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.constants.KeyConstants;
import com.dreamteam.octodrive.model.User;

public class UserDetailsActivity extends AppCompatActivity {

    private User _user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);

        Intent intent = getIntent();
        _user = intent.getExtras().getParcelable(KeyConstants.kKEY_CONSTANT_ADMIN_EDIT_OBJECT);

        TextView messageEdit = (TextView)findViewById(R.id.textView_user_name);
        messageEdit.setText(_user.name());

        TextView answer1Edit = (TextView)findViewById(R.id.textView_user_email);
        answer1Edit.setText(_user.email());

        CheckBox checkBoxActive = (CheckBox)findViewById(R.id.checkbox_admin);
        checkBoxActive.setChecked(_user.isAdmin());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_question_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // TODO save here, this handy toast will also let you know:
                Toast.makeText(UserDetailsActivity.this,
                               "n-ai scris codul pentru salvare, bă prostule", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }

        return true;
    }
}
