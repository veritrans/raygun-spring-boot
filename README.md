# raygun-spring-boot

## Features

- Auto-configures a `RaygunTemplate` bean.
- Integrations:
    - Spring Web MVC controllers in a Servlet environment
    - Spring Web Services endpoints
- Can exclude exception types sent.
- Customizable properties in the [properties documentation](PROPERTIES.md).
- Messages sending is asynchronous by default.
- Auto-configures a mock `RaygunClientFactory` bean in tests.

## Usage

Add the dependency in the implementation configuration.

```
dependencies {
    implementation 'com.midtrans:raygun-spring-boot-starter:0.7.3'
}
```

Set the `raygun.api-key` property or `RAYGUN_APIKEY` environment variable with the Raygun API key retrieved from the Raygun Application settings page.

A `RaygunTemplate` bean is auto-configured and can be autowired.

```java
@Component
class UserService {
    private final RaygunTemplate;

    UserService(RaygunTemplate raygunTemplate) {
        this.raygunTemplate = raygunTemplate;
    }

    void businessLogic() {
        try {
            //some business logic...
        } catch (Exception ex) {
            raygunTemplate.send(ex);
        }
    }
}
```

The Javadoc can be accessed in [javadoc.io](https://javadoc.io/doc/com.midtrans/raygun-spring-boot-starter).

## Support

| Spring Boot Version | Tested | Supported |
|---------------------|--------|-----------|
| 2.7.x               | yes    | yes       |
| 2.6.x               | yes    | no        |
| 2.5.x               | yes    | no        |
| < 2.5.x             | no     | no        |

This starter has been tested with Spring Boot version `2.5.x`, `2.6.x`, and `2.7.x`.

Usage with Spring Boot version below `2.5.x` and above `2.7.x` should work, but not tested.

Feature requests and bug fixes are only supported for Spring Boot version `2.7.x`.

## Integrations

### Web MVC Servlet Integration

#### Web MVC Servlet Integration in Main

Uncaught exceptions thrown from `@Controller` and `@RestController` methods are logged and sent to Raygun.

Responses can be built using Spring Web MVC exception handling mechanism using exception annotated with `@ResponseStatus` or using `@ExceptionHandler` method in a `@Controller` or a `@ControllerAdvice`  as documented in the [reference documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-exceptionhandlers).

If the uncaught exceptions are sent in `@ExceptionHandler` methods, the uncaught exceptions will be sent to Raygun twice.

```java
@RestController
class UserRestController {

    //The IndexOutOfBoundsException will be sent to Raygun once
    @GetMapping("/uncaught")
    void uncaught() {
        throw new IndexOutOfBoundsException();
    }

    //The NullPointerException will be sent to Raygun twice
    @GetMapping("/controllerAdvice")
    void controllerAdvice() {
        throw new NullPointerException();
    }

    //The response will be depends on the annotation and the custom Exception will be sent to Raygun
    @GetMapping("/responseStatus")
    void responseStatus() {
        throw new ResponseStatusException();
    }
}

@ControllerAdvice
class UserControllerAdvice {

    @Autowired
    RaygunTemplate raygunTemplate;

    @ExceptionHandler
    ResponseEntity<?> handle(NullPointerException ex) {
        raygunTemplate.send(ex);
        return ResponseEntity.internalServerError().build();
    }
}

@ResponseStatus(code = HttpStatus.BAD_GATEWAY, reason = "Bad Gateway")
class ResponseStatusException extends RuntimeException {

}
```

#### Web MVC Servlet Integration in Tests

The Web MVC integration is configured when using the [@WebMvcTest annotation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications.spring-mvc-tests).

Uncaught exceptions will still be caught and logged, but they are not sent to Raygun. Please refer to the [testing section](#testing).

```java
@WebMvcTest
class UserWebMvcTest {

    @Autowired
    MockMvc mockMvc;

    //Exceptions thrown by controller methods are caught and logged, but not sent to Raygun
    @Test
    void responseStatus() throws Exception {
        mockMvc.perform(get("/responseStatus"))
            .andExpect(status().isBadGateway());
    }
}
```

### Web Services Integration

#### Web Services Integration in Main

Uncaught exceptions thrown from `@Endpoint` methods are logged and sent to Raygun.

By default, responses for uncaught exceptions are SOAP Fault with the exception's message as the fault string. Custom responses for uncaught exceptions can be built using Spring Web Services exception handling mechanism using exceptions annotated with `@SoapFault` as documented in the [reference documentation](https://docs.spring.io/spring-ws/docs/current/reference/html/#server-endpoint-exception-resolver).

```java
@Endpoint
class UserEndpoint {

    //The Fault response reason will be the exception message.
    @PayloadRoot(localPart = "uncaught")
    void uncaught() {
        throw new IndexOutOfBoundsException();
    }

    //The Fault response reason will be the annotation's faultStringOrReason if set or the exception message if not set.
    @PayloadRoot(localPart = "soapFault")
    void soapFault() {
        throw new SoapFaultException();
    }
}

@SoapFault(faultCode = FaultCode.SERVER, faultStringOrReason = "soapFault")
class SoapFaultException extends RuntimeException {

}
```

#### Web Services Integration in Tests

The Web Services integration is configured when using the [@WebServiceServerTest annotation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.testing.spring-boot-applications.autoconfigured-webservices.server)

Uncaught exceptions will still be caught and logged, but they are not sent to Raygun. Please refer to the [testing section](#testing).

```java
@WebServiceServerTest
class UserWebServiceServerTest {

    @Autowired
    MockWebServiceClient mockWebServiceClient;

    @Test //Exceptions thrown by endpoint methods are caught and logged, but not sent to Raygun
    void uncaught() {
        mockWebServiceClient
            .sendRequest(RequestCreators.withPayload(new StringSource("<uncaught></uncaught>")))
            .andExpect(ResponseMatchers.serverOrReceiverFault());
    }
}
```

## Exceptions Exclusion

To exclude exceptions being sent, register exception types through a `RaygunExceptionExcludeRegistrar` bean.

```java
@Component
class UserRaygunExcludeExceptionRegistrar implements RaygunExceptionExcludeRegistrar {

    @Override
    public void registerExceptions(RaygunExceptionExcludeRegistry registry) {
        registry.registerException(RuntimeException.class);
    }
}
```

## Messages Sending

`RaygunTemplate` will use a `TaskExecutor` to send the Raygun mesages.

In an idiomatic Spring Boot application, a `ThreadPoolTaskExecutor` bean is auto-configured with a sensible defaults and can be customized as documented in the [reference documentation](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#features.task-execution-and-scheduling).

Depending on how many `TaskExecutor` beans configured in the `ApplicationContext`, the behavior is different as below:

- no `TaskExecutor` bean configured, `RaygunTemplate` will use a `SyncTaskExecutor` to send the messages synchronously,
- one `TaskExecutor` bean configured, `RaygunTemplate` will use the configured `TaskExecutor` bean,
- multiple `TaskExecutor` beans configured
    - no bean named `raygunTaskExecutor` configured, `RaygunTemplate` will use a `SyncTaskExecutor` to send the messages synchronously,
    - one `TaskExecutor` bean named `raygunTaskExecutor` configured, `RaygunTemplate` will use the `raygunTaskExecutor` bean.

To know the behaviors and tradeoffs of using `ThreadPoolTaskExecutor` please refer to `RaygunTemplateMessagesSendingTest` and `RaygunTemplateMessagesRejectionTest`.

## Testing

In tests, `RaygunTemplate` bean is mocked and does not send exceptions to Raygun.

This will apply to `@SpringBootTest` and test slices.

```java
@SpringBootTest
class UserTest {

    @Autowired
    RaygunTemplate raygunTemplate;

    //The RaygunTemplate does not send the exception to Raygun
    @Test
    void contextLoads() {
        raygunTemplate.send(new IllegalArgumentException());
    }
}
```

## Example Applications

There are example Spring Boot applications provided in the repository.

Please add the `raygun.api-key` in the application's `src/main/resources/application.properties` file.

Commands for MacOS and Linux:
- Command Line Application: `./gradlew raygun-spring-boot-starter:bootRun`
- Web Servlets Application: `./gradlew raygun-spring-boot-starter-web:bootRun`
- Web Services Application: `./gradlew raygun-spring-boot-starter-web-services:bootRun`

## Contributing

Please read the [contributing guide](CONTRIBUTING.md).
