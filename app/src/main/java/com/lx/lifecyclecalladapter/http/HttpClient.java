package com.lx.lifecyclecalladapter.http;

import java.util.concurrent.TimeUnit;

import com.lx.lifecyclecalladapter.http.lifecycle.LifecycleCallAdapterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpClient {

  public static final String BASE_URL = "https://www.wanandroid.com/";
  private static volatile HttpClient sHttpClient;
  private Retrofit mRetrofit;

  public static HttpClient getInstance() {
    if (sHttpClient == null) {
      synchronized (HttpClient.class) {
        if (sHttpClient == null) {
          sHttpClient = new HttpClient();
        }
      }
    }
    return sHttpClient;
  }

  private HttpClient() {
    configRetrofit();
  }

  private void configRetrofit() {
    OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(50, TimeUnit.SECONDS)
        .writeTimeout(50, TimeUnit.SECONDS).readTimeout(50, TimeUnit.SECONDS).build();
    mRetrofit = new Retrofit.Builder().addCallAdapterFactory(LifecycleCallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL).client(okHttpClient)
        .build();
  }

  public  <T> T createService(Class<T> clazz) {
    return mRetrofit.create(clazz);
  }

  public static <T> T create(Class<T> clazz) {
    return getInstance().createService(clazz);
  }
}
