package com.lx.lifecyclecalladapter.http.callback;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public abstract class MCallback<T> implements Callback<Base<T>> {

  private boolean mAllowDataNull;

  public MCallback() {
  }

  public MCallback(boolean allowDataNull) {
    mAllowDataNull = allowDataNull;
  }

  @Override
  public void onResponse(@NonNull Call<Base<T>> call, @NonNull Response<Base<T>> response) {
    String code = "", msg = "";
    try {
      Base<T> base = response.body();
      if (!response.isSuccessful() || base == null) {
        throw new Exception();
      }
      if (!base.isSuccessful()) {
        code = base.getCode();
        msg = base.getMsg();
        throw new Exception();
      }
      if (!mAllowDataNull && base.getData() == null) {
        throw new Exception();
      }
      onSuccess(base.getData());
    } catch (Exception e) {
      onFailure(code, msg);
    }
  }

  @Override
  public void onFailure(@NonNull Call<Base<T>> call, @NonNull Throwable t) {
    onFailure("", "");
  }

  public abstract void onSuccess(T data);

  public abstract void onFailure(String code, String msg);
}
