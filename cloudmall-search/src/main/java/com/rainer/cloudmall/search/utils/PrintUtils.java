package com.rainer.cloudmall.search.utils;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.JsonpMapper;
import co.elastic.clients.json.JsonpSerializable;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import jakarta.json.stream.JsonGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.StringWriter;

@Slf4j
@Component
public class PrintUtils {

    private final JsonpMapper jsonpMapper;

    public PrintUtils(JsonpMapper jsonpMapper) {
        this.jsonpMapper = jsonpMapper;
    }

    public void printDSL(JsonpSerializable object) {
        try {
            StringWriter writer = new StringWriter();
            try (JsonGenerator generator = jsonpMapper.jsonProvider().createGenerator(writer)) {
                object.serialize(generator, jsonpMapper);
            }
            log.debug(writer.toString());
        } catch (Exception e) {
            log.error(e.toString(), e);
        }
    }
}
