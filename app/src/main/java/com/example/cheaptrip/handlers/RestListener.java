package com.example.cheaptrip.handlers;

import android.content.Context;

import java.util.List;

public interface RestListener<RestResult> {
    public  void OnRestSuccess(RestResult result);
    public  void OnRestFail();
}
