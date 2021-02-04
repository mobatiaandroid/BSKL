package com.mobatia.bskl.activity.datacollection_p2;

import com.google.gson.internal.GsonBuildConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataCollection_NetworkClient {

    public static String DataCollectionApi = "http://bskledu.mobatia.com";

    public static Retrofit retrofit;

    public static Retrofit UploadData(){
        if (retrofit == null) {
            //Defining the Retrofit using Builder
            retrofit = new Retrofit.Builder()
                    .baseUrl(DataCollectionApi) //This is the only mandatory call on Builder object.
                    .addConverterFactory(GsonConverterFactory.create()) // Convertor library used to convert response into POJO
                    .build();
        }
        return retrofit;
    }
}
