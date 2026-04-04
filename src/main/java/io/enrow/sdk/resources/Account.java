package io.enrow.sdk.resources;

import io.enrow.sdk.HttpClient;
import java.util.Map;

public class Account {

    private final HttpClient http;

    public Account(HttpClient http) {
        this.http = http;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> info() {
        return http.get("/account/info", Map.class);
    }
}
