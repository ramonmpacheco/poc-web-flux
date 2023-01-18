package com.ramonmpacheco.pocwebflux

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PocWebFluxApplication

fun main(args: Array<String>) {
	runApplication<PocWebFluxApplication>(*args)
}
