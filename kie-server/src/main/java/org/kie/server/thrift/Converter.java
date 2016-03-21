/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.kie.server.thrift;

import org.apache.thrift.*;
import org.kie.server.thrift.converters.BigDecimalConverter;
import org.kie.server.thrift.converters.BigIntegerConverter;
import org.kie.server.thrift.converters.CalendarConverter;
import org.kie.server.thrift.converters.DateConverter;
import org.kie.server.thrift.converters.spi.TBaseConverter;
import org.kie.server.remote.rest.extension.ThriftMessageReader;
import org.kie.server.thrift.protocol.*;

import static org.kie.server.thrift.java.javaConstants.JAVA_MODEL_NAMESPACE;

import java.nio.ByteBuffer;
import java.util.*;

/**
 * Created by mbetzel on 25.02.2016.
 */
public class Converter {

    private static final Map<Class,TBaseConverter> thriftDispatch = new HashMap();
    private static final Map<Class,TBaseConverter> javaDispatch = new HashMap();
    private static final BigDecimalConverter bigDecimalConverter = new BigDecimalConverter();
    private static final BigIntegerConverter bigIntegerConverter = new BigIntegerConverter();
    private static final CalendarConverter calendarConverter = new CalendarConverter();
    private static final DateConverter dateConverter = new DateConverter();


    static {
        thriftDispatch.put(org.kie.server.thrift.java.BigDecimal.class, bigDecimalConverter);
        thriftDispatch.put(org.kie.server.thrift.java.BigInteger.class, bigIntegerConverter);
        thriftDispatch.put(org.kie.server.thrift.java.Calendar.class, calendarConverter);
        thriftDispatch.put(org.kie.server.thrift.java.Date.class, dateConverter);
        javaDispatch.put(java.math.BigDecimal.class, bigDecimalConverter);
        javaDispatch.put(java.math.BigInteger.class, bigIntegerConverter);
        javaDispatch.put(java.util.Calendar.class, calendarConverter);
        javaDispatch.put(java.util.Date.class, dateConverter);
    }

    public static Object convertCommandPayLoad(InsertObjectCommand insertObjectCommand, ClassLoader kieContainerClassLoader) throws ClassNotFoundException, TException, InstantiationException, IllegalAccessException {
        if (insertObjectCommand.getClassCanonicalName().startsWith(JAVA_MODEL_NAMESPACE)) {
            return deserializeToJava(insertObjectCommand.getObject(), insertObjectCommand.getClassCanonicalName(), kieContainerClassLoader);
        } else {
            return deserializeToTBase(insertObjectCommand.getObject(), insertObjectCommand.getClassCanonicalName(), kieContainerClassLoader);
        }
    }

    public static Object convertCommandPayLoad(SetGlobalCommand setGlobalCommand, ClassLoader kieContainerClassLoader) throws ClassNotFoundException, TException, InstantiationException, IllegalAccessException {
        if (setGlobalCommand.getClassCanonicalName().startsWith(JAVA_MODEL_NAMESPACE)) {
            return deserializeToJava(setGlobalCommand.getObject(), setGlobalCommand.getClassCanonicalName(), kieContainerClassLoader);
        } else {
            return deserializeToTBase(setGlobalCommand.getObject(), setGlobalCommand.getClassCanonicalName(), kieContainerClassLoader);
        }
    }

    public static ArrayList<Object> convertCommandPayLoad(InsertElementsCommand insertElementsCommand, ClassLoader kieContainerClassLoader) throws ClassNotFoundException, TException, InstantiationException, IllegalAccessException {
        ArrayList<Object> instances = new ArrayList<>(insertElementsCommand.getObjects().size());
        if (insertElementsCommand.getClassCanonicalName().startsWith(JAVA_MODEL_NAMESPACE)) {
            for (ByteBuffer byteBuffer : insertElementsCommand.getObjects()) {
                instances.add(deserializeToJava(TBaseHelper.byteBufferToByteArray(byteBuffer), insertElementsCommand.getClassCanonicalName(), kieContainerClassLoader));
            }
            return instances;
        } else {
            for (ByteBuffer byteBuffer : insertElementsCommand.getObjects()) {
                instances.add(deserializeToTBase(TBaseHelper.byteBufferToByteArray(byteBuffer), insertElementsCommand.getClassCanonicalName(), kieContainerClassLoader));
            }
            return instances;
        }
    }

    private static Object deserializeToJava(byte[] bytes, String classCanonicalName, ClassLoader classLoader) throws ClassNotFoundException, IllegalAccessException, InstantiationException, TException {
        TDeserializer tDeserializer = ThriftMessageReader.pollTDeserializer();
        try {
            TBase tBase = (TBase) classLoader.loadClass(classCanonicalName).newInstance();
            tDeserializer.deserialize(tBase, bytes);
            return thriftDispatch.get(tBase.getClass()).convertToJava(tBase);
        } finally {
            if (tDeserializer != null) {
                ThriftMessageReader.addDeserializer(tDeserializer);
            }
        }
    }

    private static Object deserializeToTBase(byte[] bytes, String classCanonicalName, ClassLoader classLoader) throws ClassNotFoundException, IllegalAccessException, InstantiationException, TException {
        TDeserializer tDeserializer = ThriftMessageReader.pollTDeserializer();
        try {
            TBase tBase = (TBase) classLoader.loadClass(classCanonicalName).newInstance();
            tDeserializer.deserialize(tBase, bytes);
            return tBase;
        } finally {
            if (tDeserializer != null) {
                ThriftMessageReader.addDeserializer(tDeserializer);
            }
        }
    }

    public static TBase convertResult(Object result) {
        if(result instanceof TBase) {
            return (TBase) result;
        } else {
            return (TBase) javaDispatch.get(result.getClass()).convertToTBase(result);
        }
    }

    public static ArrayList<TBase> convertResultArray(Object resultList) {
        Iterator iterator = ((List)resultList).iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if(!(next instanceof TBase)) {
                next = convertResult(next);
            }
        }
        return (ArrayList<TBase>) resultList;
    }

}