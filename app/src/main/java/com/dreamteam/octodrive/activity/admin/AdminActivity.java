package com.dreamteam.octodrive.activity.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.model.Question;
import com.dreamteam.octodrive.model.User;
import com.dreamteam.octodrive.webservice.ParseWebservice;
import com.dreamteam.octodrive.webservice.WebserviceConstants;

public class AdminActivity extends FragmentActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        ParseWebservice.initialise(this, WebserviceConstants.kPARSE_APPLICATION_ID,
                                   WebserviceConstants.kPARSE_CLIENT_KEY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                    return ListFragment.newInstance(Question.class);
                }
                case 1: {
                    return ListFragment.newInstance(User.class);
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
