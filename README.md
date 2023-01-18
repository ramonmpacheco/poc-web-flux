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