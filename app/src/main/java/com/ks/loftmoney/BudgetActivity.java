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
    //private ViewPager viewPager;

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

        FloatingActionButton fab = findViewById(R.id.call_add_item_activity_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int activeFragmentIndex = viewPager.getCurrentItem();
                Fragment activeFragment = getSupportFragmentManager().getFragments().get(activeFragmentIndex);
                activeFragment.startActivityForResult(new Intent(BudgetActivity.this, AddItemActivity.class),
                        BudgetFragment.REQUEST_CODE);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        tabLayout.getTabAt(0).setText(R.string.expenses);
        tabLayout.getTabAt(1).setText(R.string.income);
        tabLayout.getTabAt(2).setText(R.string.balance);
//
//        if(tabLayout.getSelectedTabPosition() == 2) {
////        if (getSupportFragmentManager().getFragment().getArguments().getSerializable("type") == BudgetFragmentTags.EXPENSES) {
//            new  BalanceFragment();
//        }
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

//        if(getCurrentItem()) == 2) {
//            new BalanceFragment();
//        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            BudgetFragmentTags tag;
            if(position == 0) {
                tag = BudgetFragmentTags.EXPENSES;
//                fragment = BudgetFragment.newInstance(BudgetFragmentTags.EXPENSES);
            } else if(position == 1) {
                tag = BudgetFragmentTags.INCOME;
//                fragment = BudgetFragment.newInstance(BudgetFragmentTags.INCOME);
            } else {
                tag = BudgetFragmentTags.BALANCE;
               // fragment = new BalanceFragment();
            }
            return BudgetFragment.newInstance(tag);
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
