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
package org.kie.server.remote.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by x3.mbetzel on 05.01.2016.
 */
public class StreamReader
{
    /**
     * Stuff the contents of a InputStream into a byte buffer.  Reads until EOF (-1).
     *
     * @param bufferSize
     * @param entityStream
     * @return
     * @throws IOException
     */
    public static byte[] readFromStream(int bufferSize, InputStream entityStream)
            throws IOException
    {
        FastByteArrayOutputStream baos = new FastByteArrayOutputStream();

        byte[] buffer = new byte[bufferSize];
        int wasRead = 0;
        do
        {
            wasRead = entityStream.read(buffer);
            if (wasRead > 0)
            {
                baos.write(buffer, 0, wasRead);
            }
        } while (wasRead > -1);
        return baos.getByteArray();
    }

}