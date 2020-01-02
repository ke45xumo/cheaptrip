package com.example.cheaptrip.handlers.rest;

public interface RestListener<RestResult> {
    void OnRestSuccess(RestResult result);
    void OnRestFail();
}
