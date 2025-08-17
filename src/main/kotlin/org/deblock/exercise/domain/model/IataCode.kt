package org.deblock.exercise.domain.model

import com.fasterxml.jackson.annotation.JsonValue

@JvmInline
value class IataCode(@JsonValue val name: String) {

    init {
        require(name.matches(Regex("^[A-Z]{3}$"))) {
            "IataCode must be a 3-letter uppercase code, but was: $name"
        }
    }
}