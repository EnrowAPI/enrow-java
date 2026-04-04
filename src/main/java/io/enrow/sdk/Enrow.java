package io.enrow.sdk;

import io.enrow.sdk.resources.*;

public class Enrow {

    public final EmailFinder email;
    public final EmailVerifier verify;
    public final PhoneFinder phone;
    public final ReverseEmail reverseEmail;
    public final Account account;

    public Enrow(String apiKey) {
        this(apiKey, "https://api.enrow.io");
    }

    public Enrow(String apiKey, String baseUrl) {
        HttpClient http = new HttpClient(apiKey, baseUrl);
        this.email = new EmailFinder(http);
        this.verify = new EmailVerifier(http);
        this.phone = new PhoneFinder(http);
        this.reverseEmail = new ReverseEmail(http);
        this.account = new Account(http);
    }
}
