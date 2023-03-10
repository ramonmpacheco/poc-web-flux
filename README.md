# POC-WEB-FLUX

## Springboot + Webflux

<br />

> Reactive Programing is a new programing paradigm
>
> Asynchronous and non blocking (not blocking the thread)
>
> Data flows as an Event/Message driven stream

> WHEN TO USE REACTIVE PROGRAMMING?
>
> When there is need to build and support high load with the available resources

> Reactive Streams are the foundation for Reactive Programming
>
> It has four interfaces:
> - Publisher
> - Subscriber
> - Subscription
> - Processor

[Project Reactor](https://projectreactor.io/)

[Project Reactor Reference Guide](https://projectreactor.io/docs/)

> [FLUX](https://projectreactor.io/docs/core/release/reference/#flux) AND [MONO](https://projectreactor.io/docs/core/release/reference/#mono)
>
> They are reactive types that implements the Reactive Streams Specification
>
> They are part of the reactor-core module
>
> **Flux** represents 0 to N elements, eg:. making a request to the db the is going to return more than 1 element
>
> **Mono** represents 0 to 1 element, eg:. making a request to the db the is going to return 1 element

> SIMPLE FLUX EXAMPLE
> ~~~java
> import reactor.core.publisher.Flux;
> public Flux<String> namesFlux() {
>    return Flux.fromIterable(List.of("Avocado", "Banana", "Orange"));
> }
>~~~
> The best way to consume from a flux is subscribing
>~~~java
>var fluxAndMonoGeneratorService = new FluxAndMonoGeneratorService();
>fluxAndMonoGeneratorService.namesFlux().subscribe(System.out::println);
>~~~
> This command will print one line for each name

> SIMPLE MONO EXAMPLE
> ~~~java
> import reactor.core.publisher.Mono;
> public Mono<String> nameMono() {
>    return Mono.just("Apple");
> }
> 
> fluxAndMonoGeneratorService.nameMono().subscribe(System.out::println);
> This command will print the name Apple
>~~~

> BEHIND THE FLUX SCENES
> add the function call log() | ```return Flux.fromIterable(List.of("Abacate", "Banana", "Laranja")).log();```
> ~~~
> INFO reactor.Flux.Iterable.1 - | onSubscribe([Synchronous Fuseable] FluxIterable.IterableSubscription)
> INFO reactor.Flux.Iterable.1 - | request(unbounded)
> INFO reactor.Flux.Iterable.1 - | onNext(Abacate)
> INFO reactor.Flux.Iterable.1 - | onNext(Banana)
> INFO reactor.Flux.Iterable.1 - | onNext(Laranja)
> INFO reactor.Flux.Iterable.1 - | onComplete()
>~~~

> HOW TO DO A SIMPLE TEST
>~~~java
> @Test
> void namesFlux() {
>
>    Flux<String> namesFlux = fluxAndMonoGeneratorService.namesFlux();
>
>    StepVerifier.create(namesFlux)
>     .expectNext("Avocado", "Banana", "Orange")
>     .verifyComplete();
> }
>~~~

> Map Operator
> it's used to transform the element from one form to another in a reactive stream
> ~~~
> public Flux<String> namesFlux_map() {
>     return Flux.fromIterable(List.of("Avocado", "Banana", "Orange"))
>         .map(String::toUpperCase);
> }
> ~~~

> Reactive streams are immutable

> Filter Operator
> it's used to filter elements in a reactive streams
> ~~~
> public Flux<String> namesFlux_filter() {
>     return Flux.fromIterable(List.of("Avocado", "Banana", "Orange"))
>         .map(String::toUpperCase)
>         .filter(it -> it.length() > 6);
> }
> ~~~
> When we have chained operator we call it pipeline

> Flatmap Operator
> It transforms one source element to a flux of 1 to n elements
> ~~~
> public Flux<String> namesFlux_flatmap() {
>     return Flux.fromIterable(List.of("Avocado", "Banana"))
>         .map(String::toUpperCase)
>         .flatMap(this::splitstream);
> }
>
> public Flux<String> splitstream(String name) {
>     var charArray = name.split("");
>     return Flux.fromArray(charArray);
>}
> ~~~

> ConcatMap Operator
> It works similar to flatmap, but preserves the ordering sequence of the reactive streams

> Transform operator
> It's used to transform from one type to another

> DefaultIfEmpty e SwitchIfEmpty
> It's not mandatory for data source to emit data all the time
> ```.defaultIfEmpty("Default value");```
>
> ``` .switchIfEmpty(Flux.just("default"));```

> Concat And ConcatWith Operators
> Used to combine two reactive streams into one
> ~~~
> Flux<String> flux1 = Flux.just("a", "b", "c");
> Flux<String> flux2 = Flux.just("d", "e", "f");
> return Flux.concat(flux1, flux2);
> ~~~

> Merge and MergeWith
> similar with concat, but do not in sequence

<hr>

## Spring WebFlux

> [Docs](https://docs.spring.io/spring-framework/docs/5.0.0.M5/spring-framework-reference/html/web-reactive.html)
> 
> There are two flavors when it comes to building restful API's in Spring WebFlux: [Annotated Controllers, Functional Endpoints]
> ~~~java
> // Annotated Controllers
> 
> @RestController
> public class FluxAndMonoController {
>     @GetMapping("/flux")
>     public Flux<Integer> flux() {
>         return Flux.just(1,2,3);
>     }
>
>    @GetMapping("/mono")
>    public Mono<String> mono() {
>        return Mono.just("Hello World");
>    }
>
>}
> ~~~

> How to Test
> ~~~java
> import org.junit.jupiter.api.Assertions;
> import org.junit.jupiter.api.Test;
> import org.springframework.beans.factory.annotation.Autowired;
> import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
> import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
> import org.springframework.test.web.reactive.server.WebTestClient;
> import reactor.test.StepVerifier;
>
> import java.util.Objects;
>
> @WebFluxTest(controllers = FluxAndMonoController.class)
> @AutoConfigureWebTestClient
> class FluxAndMonoControllerTest {
> @Autowired
> WebTestClient webTestClient;
>
>    @Test
>    void flux() {
>        webTestClient
>                .get()
>                .uri("/flux")
>                .exchange()
>                .expectStatus()
>                .is2xxSuccessful()
>                .expectBodyList(Integer.class)
>                .hasSize(3);
>    }
>
>    @Test
>     void flux_aproach_2() {
>         var result = webTestClient
>                .get()
>                .uri("/flux")
>                .exchange()
>                .expectStatus()
>                .is2xxSuccessful()
>                .returnResult(Integer.class)
>                .getResponseBody();
>        StepVerifier.create(result)
>                .expectNext(1,2,3)
>                .verifyComplete();
>    }
>
>    @Test
>    void flux_aproach_3() {
>        var result = webTestClient
>                .get()
>                .uri("/flux")
>                .exchange()
>                .expectStatus()
>                .is2xxSuccessful()
>                .expectBodyList(Integer.class)
>                .consumeWith(listEntityExchangeResult -> {
>                    var responseBody = listEntityExchangeResult.getResponseBody();
>                    assert Objects.requireNonNull(responseBody).size() == 3;
>                });
>    }
>
>    @Test
>    void mono() {
>        var result = webTestClient
>                .get()
>                .uri("/mono")
>                .exchange()
>                .expectStatus()
>                .is2xxSuccessful()
>                .expectBody(String.class)
>                .consumeWith(stringEntityExchangeResult -> {
>                    var responseBody = stringEntityExchangeResult.getResponseBody();
>                    Assertions.assertEquals("Hello World", responseBody);
>                });
>    }
>}
> ~~~