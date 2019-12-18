package com.example.cheaptrip.handlers.rest.geo;

import com.example.cheaptrip.handlers.rest.RestHandler;

import retrofit2.Response;

public class ORServiceResponseRestHandler extends RestHandler {
    /**
     * TODO: Document
     *
     * @param BASE_URL
     */
    public ORServiceResponseRestHandler(String BASE_URL) {
        super(BASE_URL);
    }

    @Override
    public Object extractDataFromResponse(Response response) {
        return null;
    }
}
