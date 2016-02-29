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