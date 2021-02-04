package com.mobatia.bskl.activity.datacollection_p2.intefaces;

import android.webkit.WebResourceRequest;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataCollectionAPI {

    @GET("/bskl/api/submit_datacollection")
    Call<JsonObject> postData(@Field("overall_status") String overall_status
            , @Field("access_token") String token
            , @Field("user_ids") String UserID
            , @Field("data") String JsonData);

//    @POST("/bskl/api/submit_datacollection")
//    Call<Server>

}
