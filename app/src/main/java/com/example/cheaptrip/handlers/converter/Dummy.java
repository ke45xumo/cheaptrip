package com.example.cheaptrip.handlers.converter;

class Dummy<Generic> {
    private Class<Generic> SomeClass;

    public Dummy(Class<Generic> someClass){
        this.SomeClass = someClass;
    }

    public Generic getInstance() throws Exception {
        return SomeClass.newInstance();
    }
}
