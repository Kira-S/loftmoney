package com.ks.loftmoney.remote;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemsApi {

    @GET("./items")
    Single<List<Item>> getItems(@Query("auth-token") String token, @Query("type") String type);

    @POST("./items/add")
    @FormUrlEncoded
    Completable addBudget(@Field("auth-token") String token,
                          @Field("name") String price,
                          @Field("price") String name,
                          @Field("type") String type);

    @POST("./items/remove")
    Completable removeItem(@Query("id") String id, @Query("auth-token") String token);

    @GET("./balance")
    Single<BalanceResponse> getBalance(@Query("auth-token") String token);
}
