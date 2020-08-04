package com.ks.loftmoney;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        findViewById(R.id.hello_text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newActivity = new Intent(getApplicationContext(), BudgetActivity.class);
                startActivity(newActivity);
            }
        });

        checkToken();
    }

    private void checkToken() {
        String token = ((LoftApp) getApplication()).getSharedPreferences().getString(LoftApp.TOKEN_KEY, "");

        if(TextUtils.isEmpty(token)) {
            routeToLogin();
        } else {
            routeToBudget();
        }
    }

    private void routeToBudget() {
        Intent budgetIntent = new Intent(getApplicationContext(), BudgetActivity.class);
        startActivity(budgetIntent);
    }

    private void routeToLogin() {
        Intent loginIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(loginIntent);
    }
}