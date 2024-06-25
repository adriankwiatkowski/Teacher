package com.example.teacher.core.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GenerateSchoolYearNameUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(firstTerm: String, secondTerm: String): String {
        return context.getString(R.string.domain_school_year_name, firstTerm, secondTerm)
    }
}