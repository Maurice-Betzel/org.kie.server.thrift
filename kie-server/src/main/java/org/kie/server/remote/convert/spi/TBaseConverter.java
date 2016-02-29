package org.kie.server.remote.convert.spi;

/**
 * Created by mbetzel on 25.02.2016.
 * A class that implements this interface can be used to convert
 * TBase object state into a Java representation and back again.
 *
 * @param <TBase>  the type of the TBase object
 * @param <J>  the type of the Java object
 */
public interface TBaseConverter<TBase, J> {

    /**
     * Converts the value stored in the TBase attribute into the
     * Java representation.
     *
     * @param tBase  the TBase object to be converted
     * @return  the converted Java object
     */
    public J convertToJava (TBase tBase);

    /**
     * Converts the Java object to TBase object.
     *
     * @param object  the Java object to be converted
     * @return  the converted TBase object
     */
    public TBase convertToTBase (J object);

}