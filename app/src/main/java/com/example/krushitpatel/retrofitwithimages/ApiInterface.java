package com.example.krushitpatel.retrofitwithimages;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by krushitpatel on 2017-05-20.
 */

public interface ApiInterface {
    @GET("tutorial/jsonparsetutorial.txt")
    Call<Country> getAllDetails();
}
