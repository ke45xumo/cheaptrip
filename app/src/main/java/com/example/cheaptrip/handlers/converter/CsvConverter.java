package com.example.cheaptrip.handlers.converter;

import android.util.Log;

import androidx.annotation.Nullable;


import com.example.cheaptrip.models.tankerkoenig.Station;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import java.lang.reflect.Type;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class CsvConverter<ReferencedClass> implements Converter<ResponseBody,List<ReferencedClass>> {

    Class<ReferencedClass> myClass;
    public CsvConverter(Class<ReferencedClass> myClass){
        this.myClass = myClass;
    }
    @Nullable
    @Override
    public List<ReferencedClass> convert(ResponseBody responseBody) throws IOException {
        String body = responseBody.string();
        return convert(body);
    }

    public  List<ReferencedClass> convert(String body) throws IOException {
        List<ReferencedClass> classList = new ArrayList<>();

        if (body == null || body.length() <= 0){
            return classList;
        }

        BufferedReader reader = new BufferedReader(new StringReader(body));

        // First Row (Headers)
        String strHeaders = reader.readLine();
        String[] arrHeaders = strHeaders.split(",");


        // Read GasStation Lines
        String currLine;
        while((currLine = reader.readLine()) != null) {
            String[] arrCurrLine = currLine.split(",");

            ReferencedClass referencedObject = null;

            try {
                referencedObject = myClass.newInstance();
            } catch (Exception e) {
                Log.e("CHEAPTRIP", "Error initializing Object referencedClass: " + e.getStackTrace());
                return null;
            }

            for (Field field : Station.class.getDeclaredFields()) {
                String varName = field.getName();
                Type varType = field.getType();

                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation.annotationType() == SerializedName.class) {
                        for (int i = 0; i < arrHeaders.length; i++) {
                            String header = arrHeaders[i];
                            String value = arrCurrLine[i];

                            SerializedName serializedName = field.getAnnotation(SerializedName.class);

                            if (serializedName.value().compareTo(header) == 0) {
                                field.setAccessible(true);

                                if (referencedObject == null) {
                                    Log.e("CHEAPTRIP", "Empty Object referencedClass.");
                                    return null;
                                }


                                try {
                                    if (varType == Double.class) {
                                        Double fieldDouble = Double.parseDouble(value);
                                        field.set(referencedObject, fieldDouble);
                                    } else if (varType == String.class) {
                                        field.set(referencedObject, value);
                                    } else if (varType == Integer.class) {
                                        Integer fieldInteger = Integer.parseInt(value);
                                        field.set(referencedObject, fieldInteger);
                                    } else if (varType == Float.class) {
                                        Float fieldFloat = Float.parseFloat(value);
                                        field.set(referencedObject, fieldFloat);
                                    } else if (varType == Short.class) {
                                        Short fieldShort = Short.parseShort(value);
                                        field.set(referencedObject, fieldShort);
                                    } else if (varType == Long.class) {
                                        Long fieldLong = Long.parseLong(value);
                                        field.set(referencedObject, fieldLong);
                                    } else {
                                        Log.e("CHEAPTRIP", "Class " + varType.toString() + " not supported");
                                        return null;
                                    }


                                } catch (NumberFormatException e) {
                                    Log.e("CHEAPTRIP", "Could not parse Double: " + e.getStackTrace());
                                    return null;
                                } catch (IllegalAccessException e) {
                                    Log.e("CHEAPTRIP", "Could set Double Field " + varName + ": " + e.getStackTrace());
                                    return null;
                                }
                            }
                        }

                    }
                }

            } // END for Field
            classList.add(referencedObject);
        }// End while readLine()
        return classList;
    }
}
