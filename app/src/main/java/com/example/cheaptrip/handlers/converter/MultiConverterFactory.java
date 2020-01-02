package com.example.cheaptrip.handlers.converter;


import com.example.cheaptrip.handlers.converter.annotations.Csv;
import com.example.cheaptrip.handlers.converter.annotations.Json;
import com.example.cheaptrip.handlers.converter.annotations.Raw;
import com.example.cheaptrip.handlers.converter.annotations.Xml;
import com.google.gson.GsonBuilder;

import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class MultiConverterFactory<ReferencedClass> extends Converter.Factory {

    Class<ReferencedClass> myClass;
    public MultiConverterFactory(Class myClass){
        this.myClass = myClass;
    }

    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == Xml.class) {
                return SimpleXmlConverterFactory.createNonStrict(
                        new Persister(new AnnotationStrategy())).responseBodyConverter(type, annotations, retrofit);
            }
            if (annotation.annotationType() == Json.class) {
                return GsonConverterFactory.create(new GsonBuilder().setLenient().excludeFieldsWithoutExposeAnnotation().create()).responseBodyConverter(type, annotations, retrofit);
            }

            if (annotation.annotationType() == Csv.class) {
                return new CsvConverter<ReferencedClass>(myClass);
            }

            if (annotation.annotationType() == Raw.class) {
                return ScalarsConverterFactory.create().responseBodyConverter(type,annotations,retrofit);
            }
        }
        return GsonConverterFactory.create(new GsonBuilder().setLenient().excludeFieldsWithoutExposeAnnotation().create()).responseBodyConverter(type, annotations, retrofit);
    }
}

