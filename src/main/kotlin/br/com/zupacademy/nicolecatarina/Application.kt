package br.com.zupacademy.nicolecatarina

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
			.args(*args)
			.packages("br.com.zupacademy.nicolecatarina")
			.start()
}

