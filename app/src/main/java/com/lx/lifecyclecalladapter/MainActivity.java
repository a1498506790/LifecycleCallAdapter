package com.lx.lifecyclecalladapter;

import android.os.Bundle;
import android.util.Log;

import com.lx.lifecyclecalladapter.entity.Article;
import com.lx.lifecyclecalladapter.entity.Page;
import com.lx.lifecyclecalladapter.http.ApiService;
import com.lx.lifecyclecalladapter.http.HttpClient;
import com.lx.lifecyclecalladapter.http.callback.MCallback;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    HttpClient.create(ApiService.class).article(2).bindLifecycle(getLifecycle())
        .enqueue(new MCallback<Page<Article>>() {
          @Override
          public void onSuccess(Page<Article> data) {
            for (Article article : data.getData()) {
              Log.e("Lx_log", "onSuccess: " + article.getTitle());
            }
          }

          @Override
          public void onFailure(String code, String msg) {
            Log.e("Lx_log", "onFailure: ");
          }
        });
  }
}