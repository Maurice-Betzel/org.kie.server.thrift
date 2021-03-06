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
package org.kie.server.thrift.converters;

import org.kie.server.thrift.converters.spi.TBaseConverter;


/**
 * Created by mbetzel on 25.02.2016.
 */
public class BigDecimalConverter implements TBaseConverter<org.kie.server.thrift.java.BigDecimal, java.math.BigDecimal> {


    @Override
    public java.math.BigDecimal convertToJava(org.kie.server.thrift.java.BigDecimal tBigDecimal) {
        return BigDecimalConverter.toJava(tBigDecimal);
    }

    @Override
    public org.kie.server.thrift.java.BigDecimal convertToTBase(java.math.BigDecimal bigDecimal) {
        return BigDecimalConverter.toTBase(bigDecimal);
    }

    public static java.math.BigDecimal toJava(org.kie.server.thrift.java.BigDecimal tBigDecimal) {
        return new java.math.BigDecimal(tBigDecimal.getValue());
    }

    public static org.kie.server.thrift.java.BigDecimal toTBase(java.math.BigDecimal bigDecimal) {
        return new org.kie.server.thrift.java.BigDecimal(bigDecimal.toPlainString());
    }

}