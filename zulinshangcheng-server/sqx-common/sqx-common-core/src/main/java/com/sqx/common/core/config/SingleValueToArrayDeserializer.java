package com.sqx.common.core.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/9/25 10:41
 */
public class SingleValueToArrayDeserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        if (node.isArray()) {
            // Handle array case
            if (node.size() == 1) {
                return node.get(0).asLong();
            } else {
                throw new IOException("Expected array with exactly one element");
            }
        } else {
            // Single value case
            return node.asLong();
        }
    }
}
