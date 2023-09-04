package com.example.teacher.core.ui.model

data class InputField<T>(
    val value: T,
    val supportingText: String? = null,
    val counter: Pair<Int, Int>? = null,
    val isError: Boolean = false,
    val isEdited: Boolean = false,
    val isRequired: Boolean = true,
) {
    val shouldShowError: Boolean
        get() = isError && isEdited

    val isValid: Boolean
        get() = !isError
}