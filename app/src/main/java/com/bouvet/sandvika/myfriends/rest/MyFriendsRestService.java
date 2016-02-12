package com.bouvet.sandvika.myfriends.rest;

import com.bouvet.sandvika.myfriends.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MyFriendsRestService {


    @POST("user")
    Call<Void> createUser(@Body User user);

    @FormUrlEncoded
    @POST("user/{id}/updatePosition")
    Call<Void> updateLocation(@Path("id") String userId, @Field("position") double[] position);
}
