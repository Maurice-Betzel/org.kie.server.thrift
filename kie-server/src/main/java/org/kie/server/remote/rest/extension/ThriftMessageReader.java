package org.kie.server.remote.rest.extension;

import org.kie.server.remote.io.StreamReader;
import org.kie.server.thrift.protocol.KieServicesRequest;
import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.kie.server.thrift.protocol.protocolConstants.APPLICATION_XTHRIFT;

/**
 * Created by x3.mbetzel on 12.06.2015.
 */
@Provider
@Consumes({APPLICATION_XTHRIFT})
public class ThriftMessageReader implements MessageBodyReader<KieServicesRequest> {

    private static final Logger logger = LoggerFactory.getLogger(ThriftMessageReader.class);

    private static final Queue<TDeserializer> T_DESERIALIZER_QUEUE = new ConcurrentLinkedQueue<>();
    private static final TProtocolFactory T_PROTOCOL_FACTORY = new TCompactProtocol.Factory();

    public ThriftMessageReader() {
        logger.info("ThriftMessageReader added");
    }

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return KieServicesRequest.class.isAssignableFrom(type);
    }

    @Override
    public KieServicesRequest readFrom(Class<KieServicesRequest> type, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws IOException, WebApplicationException {
        TDeserializer tDeserializer = null;
        KieServicesRequest kieServicesRequest = new KieServicesRequest();
        byte[] bytes = StreamReader.readFromStream(1024, entityStream);
        try {
            tDeserializer = pollTDeserializer();
            tDeserializer.deserialize(kieServicesRequest, bytes);
        } catch (TException e) {
            throw new IOException(e);
        } finally {
            if(tDeserializer != null) {
                T_DESERIALIZER_QUEUE.add(tDeserializer);
            }
        }
        return kieServicesRequest;
    }

    public static TDeserializer pollTDeserializer() {
        TDeserializer tDeserializer = T_DESERIALIZER_QUEUE.poll();
        if (tDeserializer == null) {
            tDeserializer = new TDeserializer(T_PROTOCOL_FACTORY);
        }
        return tDeserializer;
    }

    public static void addDeserializer(TDeserializer tDeserializer) {
        T_DESERIALIZER_QUEUE.add(tDeserializer);
    }

}