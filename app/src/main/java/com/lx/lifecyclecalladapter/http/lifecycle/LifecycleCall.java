package com.lx.lifecyclecalladapter.http.lifecycle;

import java.io.IOException;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import okhttp3.Request;
import okio.Timeout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LifecycleCall<T> implements Call<T>, DefaultLifecycleObserver {

  private final Executor callbackExecutor;
  private final Call<T> delegate;
  private Lifecycle lifecycle;

  public LifecycleCall(Executor callbackExecutor, Call<T> delegate) {
    this.callbackExecutor = callbackExecutor;
    this.delegate = delegate;
  }

  @NonNull
  @Override
  public Response<T> execute() throws IOException {
    return delegate.execute();
  }

  @Override
  public void enqueue(@NonNull Callback<T> callback) {
    if (isDestroyed() || delegate.isCanceled()) {
      return;
    }
    delegate.enqueue(new Callback<T>() {
      @Override
      public void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        callbackExecutor.execute(() -> {
          if (isDestroyed() || delegate.isCanceled()) {
            return;
          }
          callback.onResponse(call, response);
        });
      }

      @Override
      public void onFailure(@NonNull Call<T> call, @NonNull Throwable t) {
        callbackExecutor.execute(() -> {
          if (isDestroyed() || delegate.isCanceled()) {
            return;
          }
          callback.onFailure(LifecycleCall.this, t);
        });
      }
    });
  }

  @Override
  public boolean isExecuted() {
    return delegate.isExecuted();
  }

  @Override
  public void cancel() {
    delegate.cancel();
  }

  @Override
  public boolean isCanceled() {
    return delegate.isCanceled();
  }

  @SuppressWarnings("all")
  @NonNull
  @Override
  public Call<T> clone() {
    return new LifecycleCall<T>(callbackExecutor, delegate);
  }

  @NonNull
  @Override
  public Request request() {
    return delegate.request();
  }

  @NonNull
  @Override
  public Timeout timeout() {
    return delegate.timeout();
  }

  public LifecycleCall<T> bindLifecycle(@NonNull Lifecycle lifecycle) {
    (this.lifecycle = lifecycle).addObserver(this);
    return this;
  }

  @Override
  public void onDestroy(@NonNull LifecycleOwner owner) {
    cancel();
  }

  public boolean isDestroyed() {
    return lifecycle != null && lifecycle.getCurrentState() == Lifecycle.State.DESTROYED;
  }
}
