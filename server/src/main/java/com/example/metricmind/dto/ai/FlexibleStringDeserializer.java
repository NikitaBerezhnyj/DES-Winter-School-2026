package com.example.metricmind.dto.ai;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class FlexibleStringDeserializer extends StdDeserializer<String> {

    public FlexibleStringDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        if (p.currentToken() == JsonToken.VALUE_STRING) {
            return p.getText();
        }
        
        JsonNode node = p.getCodec().readTree(p);

        if (node.isTextual()) {
            return node.asText();
        }
        
        return flattenNode(node);
    }

    private String flattenNode(JsonNode node) {
        if (node.isTextual()) {
            return node.asText();
        }

        if (node.isArray()) {
            StringBuilder sb = new StringBuilder();
            node.forEach(child -> {
                String text = flattenNode(child);
                if (!text.isBlank()) {
                    if (!sb.isEmpty()) sb.append(" ");
                    sb.append(text);
                }
            });
            return sb.toString();
        }

        if (node.isObject()) {
            StringBuilder sb = new StringBuilder();
            node.fields().forEachRemaining(entry -> {
                String value = flattenNode(entry.getValue());
                if (!value.isBlank()) {
                    if (!sb.isEmpty()) sb.append(" ");
                    sb.append(value);
                }
            });
            return sb.toString();
        }

        return node.asText();
    }
}