package org.kie.server.remote.convert;

import org.kie.server.remote.convert.spi.TBaseConverter;

/**
 * Created by mbetzel on 25.02.2016.
 */
public class DateConverter implements TBaseConverter<org.kie.server.thrift.java.Date, java.util.Date> {


    @Override
    public java.util.Date convertToJava(org.kie.server.thrift.java.Date date) {
        return new java.util.Date(date.getValue());
    }

    @Override
    public org.kie.server.thrift.java.Date convertToTBase(java.util.Date date) {
        return new org.kie.server.thrift.java.Date(date.getTime());
    }

}