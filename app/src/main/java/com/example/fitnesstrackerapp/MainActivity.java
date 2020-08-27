package com.example.fitnesstrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.fitnesstrackerapp.CurrentRun_Package.CurrentRun;
import com.example.fitnesstrackerapp.RunsHistory_Package.RunsHistory;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        FragmentsAdapter fragmentsAdapter = new FragmentsAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(fragmentsAdapter);
        viewPager.addOnPageChangeListener(viewPagerListener);

        bottomNav = findViewById(R.id.bottom_navigation_bar);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private ViewPager.OnPageChangeListener viewPagerListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 0:
                    bottomNav.getMenu().findItem(R.id.nav_home).setChecked(true);
                    break;
                case 1:
                    bottomNav.getMenu().findItem(R.id.nav_current_run).setChecked(true);
                    break;
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            viewPager.setCurrentItem(0);
                            break;

                        case R.id.nav_current_run:
                            viewPager.setCurrentItem(1);
                            break;
                    }
                    return true;
                }
            };
}