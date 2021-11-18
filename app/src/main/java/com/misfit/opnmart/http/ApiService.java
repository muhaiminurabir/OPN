package com.misfit.opnmart.http;

import com.misfit.opnmart.model.Productdatum;
import com.misfit.opnmart.model.Storedata;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    //1 get store information
    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @GET("storeInfo")
    Call<Storedata> get_storeinfo();

    //2 get product information
    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @GET("products")
    Call<List<Productdatum>> get_productlist();

    //3 get checkout information
    //@FormUrlEncoded
    @Headers({"Content-Type:application/json", "Accept: application/json"})
    @POST("order")
    Call<Void> send_checkout(@Body HashMap sendCart);
}
