# Enrow Java SDK

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)
[![GitHub stars](https://img.shields.io/github/stars/EnrowAPI/enrow-java)](https://github.com/EnrowAPI/enrow-java)
[![Last commit](https://img.shields.io/github/last-commit/EnrowAPI/enrow-java)](https://github.com/EnrowAPI/enrow-java/commits)

Java SDK for the [Enrow API](https://enrow.io): find professional emails and phone numbers, and verify emails.

## Install

The SDK is not published to Maven Central yet: install it from this repository into your local Maven repository. It needs Java 11 or later.

```bash
git clone https://github.com/EnrowAPI/enrow-java.git
cd enrow-java && mvn install
```

Then add the dependency to your project.

### Maven

```xml
<dependency>
    <groupId>io.enrow</groupId>
    <artifactId>enrow-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Gradle

```groovy
repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    implementation 'io.enrow:enrow-sdk:1.0.0'
}
```

## Quick start

Searches are asynchronous: `find` returns an `id` right away, then `get` returns the result, with `qualification` set to `ongoing` until the search is done. You can also have the result sent to a webhook (`settings.webhook`).

```java
import io.enrow.sdk.Enrow;
import java.util.Map;

Enrow enrow = new Enrow("YOUR_API_KEY");

Map<String, Object> search = enrow.email.find(Map.of(
    "fullname", "Dwight Schrute",
    "company_domain", "dundermifflin.com"
));
String id = (String) search.get("id");

Map<String, Object> result = enrow.email.get(id);
System.out.println(result.get("qualification")); // "valid", "invalid", or "ongoing" while the search runs
System.out.println(result.get("email"));         // dwight.schrute@dundermifflin.com
```

Requests and responses are maps: you pass the API's own field names (`fullname`, `company_domain`, `settings`...) and read the JSON response with `get`. Nested objects come back as `Map`, arrays as `List`, and numbers as `Double`. The examples below also use `java.util.List`.

## Email Finder

```java
// From a full name and a company: "company_domain" or "company_name"
Map<String, Object> search = enrow.email.find(Map.of(
    "fullname", "Dwight Schrute",
    "company_name", "Dunder Mifflin",
    "custom", Map.of("crm_id", "lead_001"),
    "settings", Map.of(
        "country_code", "US",
        "retrieve_gender", true,
        "webhook", "https://your-app.com/webhooks/enrow"
    )
));

// Or from a LinkedIn profile URL (single searches only)
Map<String, Object> fromLinkedin = enrow.email.find(Map.of(
    "linkedin_url", "https://www.linkedin.com/in/dwight-schrute"
));

// "qualification" is "ongoing" until the search is done, then "valid" or "invalid"
Map<String, Object> result = enrow.email.get((String) search.get("id"));

// Bulk: up to 5,000 searches per batch
Map<String, Object> batch = enrow.email.findBulk(Map.of(
    "searches", List.of(
        Map.of("fullname", "Dwight Schrute", "company_domain", "dundermifflin.com", "custom", "lead_001"),
        Map.of("fullname", "Jim Halpert", "company_domain", "dundermifflin.com", "custom", "lead_002")
    ),
    "settings", Map.of("webhook", "https://your-app.com/webhooks/enrow")
));

Map<String, Object> results = enrow.email.getBulk((String) batch.get("id"));
Map<?, ?> general = (Map<?, ?>) results.get("general");
System.out.println(general.get("status")); // "ongoing", then "completed"
List<?> rows = (List<?>) results.get("results"); // present once the batch is completed
```

## Email Verifier

```java
Map<String, Object> verification = enrow.verify.single(Map.of(
    "email", "dwight.schrute@dundermifflin.com"
));
Map<String, Object> result = enrow.verify.get((String) verification.get("id"));
System.out.println(result.get("qualification")); // "valid", "invalid", or "ongoing"

// Bulk: up to 5,000 addresses per batch, in "verifications"
Map<String, Object> batch = enrow.verify.bulk(Map.of(
    "verifications", List.of("dwight.schrute@dundermifflin.com", "jim.halpert@dundermifflin.com")
));
Map<String, Object> results = enrow.verify.getBulk((String) batch.get("id"));
```

## Phone Finder

```java
// From a LinkedIn profile URL (recommended)
Map<String, Object> search = enrow.phone.find(Map.of(
    "linkedin_url", "https://www.linkedin.com/in/michael-scott"
));

// Or from "firstname", "lastname" and a company: "company_domain" or "company_name"
Map<String, Object> byName = enrow.phone.find(Map.of(
    "firstname", "Michael",
    "lastname", "Scott",
    "company_domain", "dundermifflin.com"
));

Map<String, Object> result = enrow.phone.get((String) search.get("id"));
System.out.println(result.get("qualification")); // "found", "not_found", or "ongoing"
System.out.println(result.get("number"));        // +15705551234 when found

// Bulk: up to 3,000 searches per batch
Map<String, Object> batch = enrow.phone.findBulk(Map.of(
    "searches", List.of(
        Map.of("linkedin_url", "https://www.linkedin.com/in/michael-scott"),
        Map.of("firstname", "Dwight", "lastname", "Schrute", "company_domain", "dundermifflin.com")
    )
));
Map<String, Object> results = enrow.phone.getBulk((String) batch.get("id"));
```

## Account

```java
Map<String, Object> info = enrow.account.info();
System.out.println(info.get("credits"));  // credit balance
System.out.println(info.get("webhooks")); // default webhook URLs
```

## Error handling

Every API error raises an `EnrowException` (a `RuntimeException`) carrying the HTTP status in `status` and the API's message in `getMessage()`. A search that is still running is not an error: `get` returns it with `qualification` set to `ongoing`, and `getBulk` with `general.status` set to `ongoing`.

```java
import io.enrow.sdk.exceptions.*;

try {
    enrow.email.find(Map.of("fullname", "Dwight Schrute", "company_domain", "dundermifflin.com"));
} catch (InsufficientBalanceException e) {
    // 402: not enough credits
} catch (RateLimitException e) {
    // 429: more than 10 requests per second on a POST endpoint, retry with backoff
} catch (AuthenticationException e) {
    // 401 or 403: missing or invalid API key
} catch (EnrowException e) {
    // Any other error, such as a 400 for an invalid payload
    System.out.println(e.status + ": " + e.getMessage());
}
```

## Credits

| Endpoint | Cost |
|----------|------|
| Email Finder | 1 credit per email found |
| Email Verifier | 0.25 credit per check |
| Phone Finder | 40 credits per phone found (50 with legacy plans) |

See [Credits & billing](https://docs.enrow.io/credits-billing) for the details, such as the cost of a search from a LinkedIn profile URL.

## Links

- [API documentation](https://docs.enrow.io)
- [Java SDK page](https://docs.enrow.io/sdks/java)
- [Enrow](https://enrow.io)

## License

MIT
