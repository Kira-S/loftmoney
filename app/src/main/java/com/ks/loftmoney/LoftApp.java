package com.ks.loftmoney;

import android.app.Application;
import android.content.SharedPreferences;

import com.ks.loftmoney.remote.AuthApi;
import com.ks.loftmoney.remote.ItemsApi;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoftApp extends Application {

    private ItemsApi itemsApi;
    private AuthApi authApi;

    public static String TOKEN_KEY = "token";

    public ItemsApi getItemsApi() {
        return itemsApi;
    }

    public AuthApi getAuthApi() {
        return authApi;
    }

    public SharedPreferences getSharedPreferences() {
        return getSharedPreferences(getString(R.string.app_name), 0);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        configureNetwork();
    }

    private void configureNetwork() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BuildConfig.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();


        itemsApi = retrofit.create(ItemsApi.class);
        authApi = retrofit.create(AuthApi.class);
    }
}
