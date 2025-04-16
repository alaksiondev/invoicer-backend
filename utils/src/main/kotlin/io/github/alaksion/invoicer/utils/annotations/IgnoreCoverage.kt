package io.github.alaksion.invoicer.utils.annotations

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.FILE)
@Retention(AnnotationRetention.SOURCE)
annotation class IgnoreCoverage()
