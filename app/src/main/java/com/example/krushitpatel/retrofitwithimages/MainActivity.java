package com.example.krushitpatel.retrofitwithimages;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String BASE_URL = "http://www.androidbegin.com/";
    //  private final static String API_KEY = "5902463023c6c5790885e78d";
    private RecyclerView recyclerView;
    private UserAdapter useaAdapter;
    List<Country.WorldpopulationBean> Users;
    RecyclerView.LayoutManager layoutManager;
    private int STORAGE_PERMISSION_CODE = 23;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview);
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        recyclerView = (RecyclerView)findViewById(R.id.movies_recycler_view);
    /*    if(API_KEY.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please obtain your API KEY",Toast.LENGTH_LONG).show();
            return;
        }*/
        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("Accept","Application/JSON").build();
                        return chain.proceed(request);
                    }
                }).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ApiInterface service = retrofit.create(ApiInterface.class);
        Call<Country> call = service.getAllDetails();
        call.enqueue(new Callback<Country>() {
            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                Log.d(TAG,"onResponse: "+response.code());
                if(response.isSuccessful()){

                        Country result = response.body();
                    Log.d(TAG,"result data"+response.body().toString());
                    Users = result.getWorldpopulation();
                    for(int i = 0; i<Users.size();i++){
                    System.out.println("Country: "+result.getWorldpopulation().get(i).getCountry());
                        System.out.println("Flag: "+result.getWorldpopulation().get(i).getFlag());
                        System.out.println("Population: "+result.getWorldpopulation().get(i).getPopulation());
                        System.out.println("Rank: "+result.getWorldpopulation().get(i).getRank());
                    }

                    try{
                        //   System.out.println(response.body().getResult().toString());
                    }catch(Exception e){
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                    }
                  useaAdapter = new UserAdapter(Users);
                    layoutManager = new LinearLayoutManager(getApplicationContext());
                   recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(useaAdapter);
                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                Log.d(TAG,"onFailure",t.fillInStackTrace());
            }
        });
    }
}
