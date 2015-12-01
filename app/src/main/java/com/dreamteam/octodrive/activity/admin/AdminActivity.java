package com.dreamteam.octodrive.activity.admin;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dreamteam.octodrive.R;
import com.dreamteam.octodrive.model.Question;
import com.dreamteam.octodrive.model.User;
import com.dreamteam.octodrive.webservice.ParseWebservice;
import com.dreamteam.octodrive.webservice.WebserviceConstants;

public class AdminActivity extends AppCompatActivity {

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
