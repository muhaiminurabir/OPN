package com.misfit.opnmart.http;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.misfit.opnmart.model.Productdatum;
import com.misfit.opnmart.model.SendCart;
import com.misfit.opnmart.model.Storedata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kotlinx.coroutines.channels.ProduceKt;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

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
