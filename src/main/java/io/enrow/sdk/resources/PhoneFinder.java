package io.enrow.sdk.resources;

import io.enrow.sdk.HttpClient;
import java.util.Map;

public class PhoneFinder {

    private final HttpClient http;

    public PhoneFinder(HttpClient http) {
        this.http = http;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> find(Map<String, Object> params) {
        return http.post("/phone/single", params, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String id) {
        return http.get("/phone/single?id=" + id, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findBulk(Map<String, Object> params) {
        return http.post("/phone/bulk", params, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getBulk(String id) {
        return http.get("/phone/bulk?id=" + id, Map.class);
    }
}
