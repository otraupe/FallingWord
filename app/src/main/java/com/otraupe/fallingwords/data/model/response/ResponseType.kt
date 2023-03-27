package com.otraupe.fallingwords.data.model.response

enum class ResponseType(val correct: Boolean) {
    CORRECT(true),
    WRONG(false),
    ELAPSED(false)
}