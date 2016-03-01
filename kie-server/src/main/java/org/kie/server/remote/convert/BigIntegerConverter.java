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