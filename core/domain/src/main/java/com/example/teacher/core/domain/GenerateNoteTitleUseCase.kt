package com.example.teacher.core.domain

import android.content.Context
import com.example.teacher.core.common.utils.TimeUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GenerateNoteTitleUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {
    operator fun invoke(): String {
        val date = TimeUtils.format(TimeUtils.currentDate())
        return context.getString(R.string.domain_note_title, date)
    }
}