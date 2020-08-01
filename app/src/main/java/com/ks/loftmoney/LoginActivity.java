package com.ks.loftmoney;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ks.loftmoney.remote.AuthResponse;

import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    Button loginBtnView;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtnView = findViewById(R.id.login_btn_view);

        configureBtn();
    }

        private void configureBtn() {
            loginBtnView.setOnClickListener(new View.OnClickListener() {

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View view) {
                    String socialUserId = String.valueOf(new Random().nextInt());
                    compositeDisposable.add(((LoftApp) getApplication()).getAuthApi().performLogin(socialUserId)
                            .subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<AuthResponse>() {
                                @Override
                                public void accept(AuthResponse authResponse) throws Exception {
                                    ((LoftApp) getApplication()).getSharedPreferences()
                                            .edit()
                                            .putString(LoftApp.TOKEN_KEY, authResponse.getAuthToken())
                                    .apply();

                                    Intent budgetIntent = new Intent(getApplicationContext(), BudgetActivity.class);
                                    startActivity(budgetIntent);
                                }
                            }, new Consumer<Throwable>() {
                                @Override
                                public void accept(Throwable throwable) throws Exception {
                                    Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }));
                }
            });
        }
}