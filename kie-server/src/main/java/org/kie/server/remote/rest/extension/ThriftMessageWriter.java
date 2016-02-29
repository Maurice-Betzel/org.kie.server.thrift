package org.kie.server.remote.rest.extension;

import org.kie.server.thrift.protocol.KieServicesResponse;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.kie.server.thrift.protocol.protocolConstants.APPLICATION_XTHRIFT;

/**
 * Created by x3.mbetzel on 12.06.2015.
 */
@Provider
@Produces({APPLICATION_XTHRIFT})
public class ThriftMessageWriter implements MessageBodyWriter<KieServicesResponse> {

    private static final Logger logger = LoggerFactory.getLogger(ThriftMessageReader.class);

    private static final Queue<TSerializer> T_SERIALIZER_QUEUE = new ConcurrentLinkedQueue<>();
    private static final TProtocolFactory T_PROTOCOL_FACTORY = new TCompactProtocol.Factory();

    public ThriftMessageWriter() {
        logger.info("ThriftMessageWriter added");
    }

    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return KieServicesResponse.class.isAssignableFrom(type);
    }

    @Override
    public long getSize(KieServicesResponse pdfServicesRequest, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    @Override
    public void writeTo(KieServicesResponse kieServicesResponse, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream) throws IOException,
            WebApplicationException {
        TSerializer tSerializer = null;
        try {
            tSerializer = pollTSerializer();
            entityStream.write(tSerializer.serialize(kieServicesResponse));
        } catch (TException e) {
            throw new IOException(e);
        } finally {
            if(tSerializer != null) {
                T_SERIALIZER_QUEUE.add(tSerializer);
            }
        }
    }

    public static TSerializer pollTSerializer() {
        TSerializer tSerializer = T_SERIALIZER_QUEUE.poll();
        if (tSerializer == null) {
            tSerializer = new TSerializer(T_PROTOCOL_FACTORY);
        }
        return tSerializer;
    }

    public static void addSerializer(TSerializer tSerializer) {
        T_SERIALIZER_QUEUE.add(tSerializer);
    }

}