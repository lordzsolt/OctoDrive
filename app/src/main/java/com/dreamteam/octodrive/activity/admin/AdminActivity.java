package com.dreamteam.octodrive.activity.admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.activity.LoginActivity;
import com.dreamteam.octodrive.activity.QuestionActivity;
import com.dreamteam.octodrive.constants.KeyConstants;
import com.dreamteam.octodrive.model.Question;
import com.dreamteam.octodrive.model.User;
import com.dreamteam.octodrive.webservice.ParseWebservice;
import com.dreamteam.octodrive.webservice.WebserviceConstants;

public class AdminActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private Menu menu;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null && bundle.containsKey("userId")) {
            userId = bundle.getString("userId");
        }

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(AdminActivity.this, R.string.error_invalid_intent, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                MenuItem item = menu.findItem(R.id.action_new);
                item.setVisible(position == 0);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        ParseWebservice.initialise(this, WebserviceConstants.kPARSE_APPLICATION_ID,
                                   WebserviceConstants.kPARSE_CLIENT_KEY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_new:
                switch (mViewPager.getCurrentItem()) {
                    case 0: { // Question
                        Intent intent = new Intent(AdminActivity.this, QuestionDetailsActivity.class);
                        intent.putExtra(KeyConstants.kKEY_CONSTANT_ADMIN_NEW_QUESTION, true);
                        startActivity(intent);
                    } break;
                }
                break;

            default:
                break;
        }

        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private String[] titles;

        public SectionsPagerAdapter( FragmentManager fm) {
            super(fm);
            titles = new String[] {getString(R.string.admin_page_title_questions),
                                   getString(R.string.admin_page_title_users),
                                   getString(R.string.admin_page_title_settings)};
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: {
                    return ListFragment.newInstance(Question.class, true);
                }
                case 1: {
                    return ListFragment.newInstance(User.class, false);
                }
                default: {
                    return SettingsFragment.newInstance();
                }
            }
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position < titles.length) {
                return titles[position];
            }
            return null;
        }
    }
}
