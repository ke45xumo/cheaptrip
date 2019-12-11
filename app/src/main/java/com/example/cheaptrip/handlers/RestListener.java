package com.example.cheaptrip.handlers;

public interface RestListener<RestResult> {
    public  void OnRestSuccess(RestResult result);
    public  void OnRestFail();
}
