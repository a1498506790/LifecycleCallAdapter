package com.lx.lifecyclecalladapter.http.callback;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.Keep;

@Keep
public class Base<T> {

  @SerializedName("errorMsg")
  private String msg;

  @SerializedName("errorCode")
  private String code;

  @SerializedName("data")
  private T data;

  public String getMsg() {
    return msg;
  }

  public String getCode() {
    return code;
  }

  public T getData() {
    return data;
  }

  public static final String SUCCESS_CODE = "0";

  public boolean isSuccessful() {
    return SUCCESS_CODE.equals(code);
  }
}
