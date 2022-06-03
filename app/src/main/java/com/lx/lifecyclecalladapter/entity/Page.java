package com.lx.lifecyclecalladapter.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Page<T> {

  private boolean over;

  @SerializedName("datas")
  private List<T> data;

  public boolean isOver() {
    return over;
  }

  public void setOver(boolean over) {
    this.over = over;
  }

  public List<T> getData() {
    return data;
  }

  public void setData(List<T> data) {
    this.data = data;
  }
}
