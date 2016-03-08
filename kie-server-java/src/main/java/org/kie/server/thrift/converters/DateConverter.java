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
public class DateConverter implements TBaseConverter<org.kie.server.thrift.java.Date, java.util.Date> {


    @Override
    public java.util.Date convertToJava(org.kie.server.thrift.java.Date date) {
        return DateConverter.toJava(date);
    }

    @Override
    public org.kie.server.thrift.java.Date convertToTBase(java.util.Date date) {
        return DateConverter.toTBase(date);
    }

    public static java.util.Date toJava(org.kie.server.thrift.java.Date date) {
        return new java.util.Date(date.getValue());
    }

    public static org.kie.server.thrift.java.Date toTBase(java.util.Date date) {
        return new org.kie.server.thrift.java.Date(date.getTime());
    }

}