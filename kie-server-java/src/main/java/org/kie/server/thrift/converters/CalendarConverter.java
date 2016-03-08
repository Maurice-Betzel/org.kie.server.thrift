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
public class CalendarConverter implements TBaseConverter<org.kie.server.thrift.java.Calendar, java.util.Calendar> {


    @Override
    public java.util.Calendar convertToJava(org.kie.server.thrift.java.Calendar calendar) {
        return CalendarConverter.toJava(calendar);
    }

    @Override
    public org.kie.server.thrift.java.Calendar convertToTBase(java.util.Calendar calendar) {
       return CalendarConverter.toTBase(calendar);
    }

    public static java.util.Calendar toJava(org.kie.server.thrift.java.Calendar calendar) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        c.set(calendar.getYear(), calendar.getMonth(), calendar.day_of_month, calendar.getHour_of_day(), calendar.getMinute(), calendar.getSecond());
        return c;
    }

    public static org.kie.server.thrift.java.Calendar toTBase(java.util.Calendar calendar) {
        org.kie.server.thrift.java.Calendar c = new org.kie.server.thrift.java.Calendar(calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH), calendar.get(java.util.Calendar.HOUR_OF_DAY),
                calendar.get(java.util.Calendar.MINUTE), calendar.get(java.util.Calendar.SECOND));
        return c;
    }

}