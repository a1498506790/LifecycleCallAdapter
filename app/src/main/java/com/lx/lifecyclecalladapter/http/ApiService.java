package com.lx.lifecyclecalladapter.http;

import java.util.List;

import com.lx.lifecyclecalladapter.entity.Article;
import com.lx.lifecyclecalladapter.entity.Banner;
import com.lx.lifecyclecalladapter.entity.Page;
import com.lx.lifecyclecalladapter.http.callback.Base;
import com.lx.lifecyclecalladapter.http.lifecycle.LifecycleCall;

import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {

  @GET("banner/json")
  LifecycleCall<Base<List<Banner>>> banner();

  @GET("article/list/{page}/json")
  LifecycleCall<Base<Page<Article>>> article(@Path("page") int page);
}
