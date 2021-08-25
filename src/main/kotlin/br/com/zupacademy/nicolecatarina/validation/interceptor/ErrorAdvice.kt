package br.com.zupacademy.nicolecatarina.validation.interceptor

import io.micronaut.aop.Around

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Around
annotation class ErrorAdvice()
