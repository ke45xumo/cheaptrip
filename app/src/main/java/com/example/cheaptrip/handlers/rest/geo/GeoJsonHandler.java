package com.example.cheaptrip.handlers.rest.geo;

import android.widget.Toast;

import com.example.cheaptrip.dao.ORServiceClient;
import com.example.cheaptrip.models.orservice.ORServiceResponse;
import com.example.cheaptrip.models.orservice.PostBody;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.osmdroid.bonuspack.kml.KmlDocument;
import org.osmdroid.util.BoundingBox;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.FolderOverlay;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GeoJsonHandler {

    private static String BASE_URL = "https://api.openrouteservice.org/v2/";

    List<List<Double>> coordinates;


    private Retrofit retrofit;

    public GeoJsonHandler(List<List<Double>> coordinates){
        this.coordinates = coordinates;


        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }


    private String formatToJSON(){
        PostBody postBody = new PostBody(coordinates,"km");
        Gson gson = new Gson();
        String json = gson.toJson(postBody);

        return json;
    }

    public String getJson(final MapView mapView){
        ORServiceClient orServiceClient = retrofit.create(ORServiceClient.class);

        String body = formatToJSON();
        Call<String> geoPoints = orServiceClient.getGeoJson(body);

        geoPoints.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                String geoJSON = response.body();
                if (geoJSON != null && geoJSON.length() > 0){
                    System.out.println("SUCCESS");
                    KmlDocument kmlDocument = new KmlDocument();
                    kmlDocument.parseGeoJSON(geoJSON);
                    FolderOverlay myOverLay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(mapView,null,null,kmlDocument);
                    mapView.getOverlays().add(myOverLay );
                    mapView.invalidate();

                    Gson gson = new Gson();
                    //JSONObject jsonObject = gson.fromJson(geoJSON,JSONObject.class);
                    JsonElement jsonElement = gson.fromJson(geoJSON, JsonElement.class);
                    ORServiceResponse orServiceResponse = gson.fromJson(geoJSON,ORServiceResponse.class);
                    List<Double> responseBbox = orServiceResponse.getBbox();

                    BoundingBox bbox =  new BoundingBox(responseBbox.get(3),responseBbox.get(2),responseBbox.get(1),responseBbox.get(0));


                    mapView.zoomToBoundingBox(bbox,true,150);

                    int a= 1;
                    a++;
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
        return null;
    }
}
