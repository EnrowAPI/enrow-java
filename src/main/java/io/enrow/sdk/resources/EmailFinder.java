package io.enrow.sdk.resources;

import io.enrow.sdk.HttpClient;
import java.util.List;
import java.util.Map;

public class EmailFinder {

    private final HttpClient http;

    public EmailFinder(HttpClient http) {
        this.http = http;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> find(Map<String, Object> params) {
        return http.post("/email/find/single", params, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String id) {
        return http.get("/email/find/single?id=" + id, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findBulk(Map<String, Object> params) {
        return http.post("/email/find/bulk", params, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getBulk(String id) {
        return http.get("/email/find/bulk?id=" + id, Map.class);
    }
}
