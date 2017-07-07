package com.echopen.asso.echopen.webservices;

import com.echopen.asso.echopen.webservices.models.Token;

import okhttp3.Credentials;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by matthieu on 06/07/17.
 */

public interface APIServices {

    public static final String URL_API = "http://uat-echopen.herokuapp.com/api";

    @POST("/add")
    Call<Token> postDatas(@Body Credentials credentials);
}