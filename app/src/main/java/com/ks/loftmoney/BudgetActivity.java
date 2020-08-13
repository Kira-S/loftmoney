package com.ks.loftmoney;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class BudgetActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);

        tabLayout = findViewById(R.id.tabs);
        toolbar = findViewById(R.id.toolbar);

        final ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new BudgetPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //в зависимости от позиции скрыть или показать fab
                if(position == 2) {
                   fab.hide();
                } else {
                    fab.show();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        fab = findViewById(R.id.call_add_item_activity_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int activeFragmentIndex = viewPager.getCurrentItem();
                BudgetFragmentTags tag;
                if (activeFragmentIndex == 0) {
                    tag = BudgetFragmentTags.EXPENSES;
                } else {
                    tag = BudgetFragmentTags.INCOME;
                }
                Fragment activeFragment = getSupportFragmentManager().getFragments().get(activeFragmentIndex);
                activeFragment.startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class).putExtra("tag", tag),
                        BudgetFragment.REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        tabLayout.getTabAt(0).setText(R.string.expenses);
        tabLayout.getTabAt(1).setText(R.string.income);
        tabLayout.getTabAt(2).setText(R.string.balance);
    }


    @Override
    public void onActionModeStarted(android.view.ActionMode mode) {
        super.onActionModeStarted(mode);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray_blue));
    }

    @Override
    public void onSupportActionModeFinished(@NonNull ActionMode mode) {
        super.onSupportActionModeFinished(mode);
        tabLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    static class BudgetPagerAdapter extends FragmentPagerAdapter {

        public BudgetPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if(position == 0) {
                return BudgetFragment.newInstance(BudgetFragmentTags.EXPENSES);
            } else if(position == 1) {
                return BudgetFragment.newInstance(BudgetFragmentTags.INCOME);
            } else if(position == 2) {
               return BalanceFragment.newInstance();
            } else {
                return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
