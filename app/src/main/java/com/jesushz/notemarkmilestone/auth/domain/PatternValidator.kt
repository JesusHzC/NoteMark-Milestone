package com.jesushz.notemarkmilestone.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}
