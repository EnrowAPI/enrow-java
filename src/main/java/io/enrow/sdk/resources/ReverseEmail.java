package io.enrow.sdk.resources;

import io.enrow.sdk.HttpClient;
import java.util.Map;

public class ReverseEmail {

    private final HttpClient http;

    public ReverseEmail(HttpClient http) {
        this.http = http;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> find(Map<String, Object> params) {
        return http.post("/reverse-email/single", params, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String id) {
        return http.get("/reverse-email/single/" + id, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> findBulk(Map<String, Object> params) {
        return http.post("/reverse-email/bulk", params, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getBulk(String id) {
        return http.get("/reverse-email/bulk/" + id, Map.class);
    }
}
