package com.example.metricmind.ai.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiResponse {

    @JsonDeserialize(using = FlexibleStringDeserializer.class)
    private String summary;

    @JsonDeserialize(using = FlexibleStringDeserializer.class)
    private String explanation;

    @JsonDeserialize(using = FlexibleStringDeserializer.class)
    private String recommendation;
}