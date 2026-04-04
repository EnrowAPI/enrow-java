package io.enrow.sdk;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.enrow.sdk.exceptions.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClient {

    private final String apiKey;
    private final String baseUrl;
    private final java.net.http.HttpClient client;
    private final Gson gson;

    public HttpClient(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.client = java.net.http.HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public <T> T get(String path, Class<T> type) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .header("x-api-key", apiKey)
                    .GET()
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            handleError(response);
            return gson.fromJson(response.body(), type);
        } catch (EnrowException e) {
            throw e;
        } catch (IOException | InterruptedException e) {
            throw new EnrowException(500, "HttpError", e.getMessage());
        }
    }

    public <T> T post(String path, Object body, Class<T> type) {
        try {
            String json = gson.toJson(body);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl + path))
                    .header("x-api-key", apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            handleError(response);
            return gson.fromJson(response.body(), type);
        } catch (EnrowException e) {
            throw e;
        } catch (IOException | InterruptedException e) {
            throw new EnrowException(500, "HttpError", e.getMessage());
        }
    }

    private void handleError(HttpResponse<String> response) {
        int status = response.statusCode();
        if (status < 400) return;

        String message;
        String error;
        try {
            JsonObject body = gson.fromJson(response.body(), JsonObject.class);
            message = body.has("message") ? body.get("message").getAsString() : "Unknown error";
            error = body.has("error") ? body.get("error").getAsString() : "UnknownError";
        } catch (Exception e) {
            message = response.body();
            error = "UnknownError";
        }

        switch (status) {
            case 401: throw new AuthenticationException(message);
            case 422: throw new InsufficientBalanceException(message);
            case 429: throw new RateLimitException(message);
            default: throw new EnrowException(status, error, message);
        }
    }

    public Gson getGson() {
        return gson;
    }
}
