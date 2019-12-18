package com.example.cheaptrip.handlers.rest;

public interface RestListener<RestResult> {
    public  void OnRestSuccess(RestResult result);
    public  void OnRestFail();
}
