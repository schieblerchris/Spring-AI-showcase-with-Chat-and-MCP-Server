package com.github.sc.apps.saisc.chatmodel.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.sc.apps.saisc.shared.infra.UnixTimestampDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class OpenAIAdapter {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public OpenAIAdapter(@Qualifier("openAIRestClient") RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public Set<ModelData> getModels() {
        var response = restClient.method(HttpMethod.GET).uri("/v1/models").retrieve();
        var body = response.body(String.class);
        try {
            return objectMapper.readValue(body, ModelListResponse.class).data().stream().filter(Objects::nonNull).collect(Collectors.toCollection(TreeSet::new));
        } catch (JsonProcessingException e) {
            return Collections.emptySet();
        }
    }

    public record ModelListResponse(
            @JsonProperty("object")
            String object,
            @JsonProperty("data")
            List<ModelData> data
    ) {
    }

    public record ModelData(
            @JsonProperty("id")
            String id,
            @JsonProperty("object")
            String object,
            @JsonProperty("created")
            @JsonDeserialize(using = UnixTimestampDeserializer.class)
            LocalDateTime created,
            @JsonProperty("owned_by")
            String ownedBy
    ) implements Comparable<ModelData> {

        public static final Comparator<ModelData> COMPARATOR = Comparator.comparing(ModelData::id);

        @Override
        public int compareTo(OpenAIAdapter.@NotNull ModelData o) {
            return COMPARATOR.compare(this, o);
        }
    }


}
