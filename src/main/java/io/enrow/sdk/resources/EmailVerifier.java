package io.enrow.sdk.resources;

import io.enrow.sdk.HttpClient;
import java.util.Map;

public class EmailVerifier {

    private final HttpClient http;

    public EmailVerifier(HttpClient http) {
        this.http = http;
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> single(Map<String, Object> params) {
        return http.post("/email/verify/single", params, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> get(String id) {
        return http.get("/email/verify/single?id=" + id, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> bulk(Map<String, Object> params) {
        return http.post("/email/verify/bulk", params, Map.class);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Object> getBulk(String id) {
        return http.get("/email/verify/bulk?id=" + id, Map.class);
    }
}
