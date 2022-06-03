package com.lx.lifecyclecalladapter.http.lifecycle;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.SkipCallbackExecutor;

public class LifecycleCallAdapterFactory extends CallAdapter.Factory {

  public static LifecycleCallAdapterFactory create() {
    return new LifecycleCallAdapterFactory();
  }

  @Nullable
  @Override
  public CallAdapter<?, ?> get(@NonNull Type returnType, @NonNull Annotation[] annotations,
      @NonNull Retrofit retrofit) {
    if (getRawType(returnType) != LifecycleCall.class) {
      return null;
    }
    if (!(returnType instanceof ParameterizedType)) {
      throw new IllegalArgumentException(
          "Call return type must be parameterized as Call<Foo> or Call<? extends Foo>");
    }
    final Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);

    final Executor executor = isAnnotationPresent(annotations) ? null : retrofit.callbackExecutor();

    return new CallAdapter<Object, Call<?>>() {
      @NonNull
      @Override
      public Type responseType() {
        return responseType;
      }

      @NonNull
      @Override
      public Call<Object> adapt(@NonNull Call<Object> call) {
        return executor == null ? call : new LifecycleCall<>(retrofit.callbackExecutor(), call);
      }
    };
  }

  static boolean isAnnotationPresent(Annotation[] annotations) {
    for (Annotation annotation : annotations) {
      if (annotation instanceof SkipCallbackExecutor) {
        return true;
      }
    }
    return false;
  }
}
