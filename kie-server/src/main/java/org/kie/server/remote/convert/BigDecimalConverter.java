package org.kie.server.remote.convert;

import org.kie.server.remote.convert.spi.TBaseConverter;


/**
 * Created by mbetzel on 25.02.2016.
 */
public class BigDecimalConverter implements TBaseConverter<org.kie.server.thrift.java.BigDecimal, java.math.BigDecimal> {


    @Override
    public java.math.BigDecimal convertToJava(org.kie.server.thrift.java.BigDecimal bigDecimal) {
        return new java.math.BigDecimal(bigDecimal.getValue());
    }

    @Override
    public org.kie.server.thrift.java.BigDecimal convertToTBase(java.math.BigDecimal bigDecimal) {
        return new org.kie.server.thrift.java.BigDecimal(bigDecimal.toPlainString());
    }

}