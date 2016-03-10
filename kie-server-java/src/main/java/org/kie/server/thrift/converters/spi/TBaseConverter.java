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
package org.kie.server.thrift.converters.spi;

/**
 * Created by mbetzel on 25.02.2016.
 * A class that implements this interface can be used to convert
 * TBase object state into a Java representation and back again.
 *
 * @param <TBase>  the type of the TBase object
 * @param <Object>  the type of the Java object
 */
public interface TBaseConverter<TBase, Object> {

    /**
     * Converts the value stored in the TBase attribute into the
     * Java representation.
     *
     * @param tBase  the TBase object to be converted
     * @return  the converted Java object
     */
     Object convertToJava(TBase tBase);

    /**
     * Converts the Java object to TBase object.
     *
     * @param object  the Java object to be converted
     * @return  the converted TBase object
     */
     TBase convertToTBase (Object object);

}