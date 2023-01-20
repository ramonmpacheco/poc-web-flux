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