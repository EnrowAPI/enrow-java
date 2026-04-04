# Enrow Java SDK

Find and verify professional emails, phone numbers, and contact information with the [Enrow API](https://enrow.io).

## Install

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
implementation 'io.enrow:enrow-sdk:1.0.0'
```

## Quick start

```java
import io.enrow.sdk.Enrow;
import java.util.Map;

Enrow enrow = new Enrow("your_api_key");

Map<String, Object> result = enrow.email.find(Map.of(
    "company_domain", "apple.com",
    "fullname", "Tim Cook"
));

System.out.println(result.get("email")); // tcook@apple.com
```

## Email Finder

```java
// Find a single email
Map<String, Object> search = enrow.email.find(Map.of(
    "company_domain", "apple.com",
    "fullname", "Tim Cook"
));

// Get by ID
Map<String, Object> result = enrow.email.get("search_abc123");

// Bulk
Map<String, Object> batch = enrow.email.findBulk(Map.of(
    "searches", List.of(
        Map.of("company_domain", "apple.com", "fullname", "Tim Cook"),
        Map.of("company_domain", "microsoft.com", "fullname", "Satya Nadella")
    )
));
Map<String, Object> results = enrow.email.getBulk((String) batch.get("batch_id"));
```

## Email Verifier

```java
Map<String, Object> verification = enrow.verify.single(Map.of("email", "tcook@apple.com"));
System.out.println(verification.get("qualification")); // "valid"

Map<String, Object> batch = enrow.verify.bulk(Map.of("emails", List.of("a@b.com", "c@d.com")));
Map<String, Object> results = enrow.verify.getBulk((String) batch.get("batch_id"));
```

## Phone Finder

```java
Map<String, Object> phone = enrow.phone.find(Map.of(
    "linkedin_url", "https://linkedin.com/in/timcook"
));
Map<String, Object> result = enrow.phone.get("phone_abc123");
```

## Reverse Email

```java
Map<String, Object> person = enrow.reverseEmail.find(Map.of("email", "tcook@apple.com"));
System.out.println(person.get("first_name")); // "Tim"
```

## Account

```java
Map<String, Object> info = enrow.account.info();
System.out.println(info.get("credits"));
```

## Error handling

```java
import io.enrow.sdk.exceptions.*;

try {
    enrow.email.find(Map.of("company_domain", "apple.com"));
} catch (RateLimitException e) {
    // 429
} catch (InsufficientBalanceException e) {
    // 402
} catch (AuthenticationException e) {
    // 401
} catch (EnrowException e) {
    System.out.println(e.status + ": " + e.getMessage());
}
```

## Credits

| Endpoint | Cost |
|----------|------|
| Email Finder | 1 credit/email |
| Email Verifier | 0.25 credit/email |
| Phone Finder | 50 credits/phone |
| Reverse Email | 5 credits/lookup |

## Links

- [API Documentation](https://docs.enrow.io)
- [Enrow](https://enrow.io)

## License

MIT
