package uk.co.informaticslab.molab3dwxds.spock.extensions.httpclient

import groovy.json.JsonBuilder
import groovyx.net.http.EncoderRegistry
import groovyx.net.http.HttpResponseException
import groovyx.net.http.ParserRegistry
import groovyx.net.http.RESTClient
import org.apache.commons.io.IOUtils
import org.apache.http.HttpEntity
import org.apache.http.client.ClientProtocolException
import org.apache.http.entity.ByteArrayEntity
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.json.simple.parser.ContainerFactory
import org.json.simple.parser.JSONParser

class HTTPClientHelper extends RESTClient {

    def HTTPClientHelper(defaultURI) {
        super(defaultURI)

        ContainerFactory containerFactory = new ContainerFactory() {

            def List creatArrayContainer() {
                return new LinkedList();
            }

            def Map createObjectContainer() {
                return new LinkedHashMap();
            }
        }

        ParserRegistry parserRegistry = new ParserRegistry()

        addAsJsonParsers(parserRegistry, containerFactory,
                ["application/hal+json"])

        parserRegistry.putAt("video/mp4") { resp ->

            HttpEntity entity = resp.getEntity()
            return IOUtils.toByteArray(entity.content, entity.contentLength)
        }

        setParserRegistry(parserRegistry)


        EncoderRegistry encoderRegistry = new EncoderRegistry()

        addAsJsonEncoders(encoderRegistry,
                ["application/hal+json"])

        encoderRegistry.putAt("video/mp4") { payload ->

            def contentType = ContentType.create("video/mp4")
            def bytes = new byte[payload.size()]
            for (int i = 0; i < payload.size(); i++) {
                bytes[i] = payload[i]
            }
            return new ByteArrayEntity(bytes, contentType)
        }

        setEncoderRegistry(encoderRegistry)
    }

    def addAsJsonParsers(ParserRegistry parserRegistry, ContainerFactory containerFactory, List mediaTypes) {
        for (mediaType in mediaTypes) {
            parserRegistry.putAt(mediaType) { resp ->

                HttpEntity entity = resp.getEntity()
                JSONParser parser = new JSONParser();
                return parser.parse(new InputStreamReader(entity.content), containerFactory)
            }
        }
    }

    def addAsJsonEncoders(EncoderRegistry encoderRegistry, List mediaTypes) {
        for (mediaType in mediaTypes) {
            def contentType = ContentType.create(mediaType)
            encoderRegistry.putAt(mediaType) { payload ->
                def json = new JsonBuilder(payload).toString()
                return new StringEntity(json, contentType)
            }
        }
    }

    def Object get(Map<String, ?> args) throws ClientProtocolException, IOException, URISyntaxException {
        try {
            /*
             * To help with testing all query string parameters and headers that are null are removed. This prevents
             * them from appearing on the request. By default any query string that was given
             * a null value ends up as an empty string value on the server side. So in terms of testing
             * a null query param means not supplied.
             */

            Map queryMap = args.get("query")
            if (queryMap) {
                queryMap.iterator().with { iterator ->
                    iterator.each { entry ->
                        if (entry.value == null) {
                            iterator.remove()
                        }
                    }
                }
            }

            Map headersMap = args.get("headers")
            if (headersMap) {
                headersMap.iterator().with { iterator ->
                    iterator.each { entry ->
                        if (entry.value == null) {
                            iterator.remove()
                        }
                    }
                }
            }

            return super.get(args)
        } catch (HttpResponseException ex) {
            return ex.response
        }
    }

    def Object post(Map<String, ?> args) throws URISyntaxException, ClientProtocolException, IOException {
        try {
            return super.post(args)
        } catch (HttpResponseException ex) {
            return ex.response
        }
    }

    def Object put(Map<String, ?> args) throws URISyntaxException, ClientProtocolException, IOException {
        try {
            return super.put(args)
        } catch (HttpResponseException ex) {
            return ex.response
        }
    }

    def Object delete(Map<String, ?> args) throws URISyntaxException, ClientProtocolException, IOException {
        try {
            return super.delete(args)
        } catch (HttpResponseException ex) {
            return ex.response
        }
    }
}
