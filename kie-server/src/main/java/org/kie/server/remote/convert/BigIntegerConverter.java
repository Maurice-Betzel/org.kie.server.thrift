package org.kie.server.remote.convert;

import org.kie.server.remote.convert.spi.TBaseConverter;

/**
 * Created by mbetzel on 25.02.2016.
 */
public class BigIntegerConverter implements TBaseConverter< org.kie.server.thrift.java.BigInteger, java.math.BigInteger> {


    @Override
    public java.math.BigInteger convertToJava( org.kie.server.thrift.java.BigInteger bigInteger) {
        return new java.math.BigInteger(bigInteger.getValue(), bigInteger.getRadix());
    }

    @Override
    public  org.kie.server.thrift.java.BigInteger convertToTBase(java.math.BigInteger bigInteger) {
        org.kie.server.thrift.java.BigInteger bi = new  org.kie.server.thrift.java.BigInteger();
        bi.setValue(bigInteger.toString());
        return bi;
    }

}