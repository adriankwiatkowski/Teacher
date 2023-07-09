package com.example.teacherapp.ui.screens.schoolyear.data

import androidx.core.text.trimmedLength
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputDate
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.utils.format
import java.time.LocalDate

object SchoolYearFormProvider {

    fun validateSchoolYearName(
        name: String,
        isEdited: Boolean = true,
    ): InputField<String> {
        val charCountLimit = 60
        val trimmedLength = name.trimmedLength()
        return InputField(
            name,
            counter = trimmedLength to charCountLimit,
            isError = trimmedLength !in 1..charCountLimit,
            isEdited = isEdited,
        )
    }

    fun validateTermName(name: String, isEdited: Boolean = true): InputField<String> {
        val charCountLimit = 20
        return InputField(
            name,
            counter = name.trimmedLength() to charCountLimit,
            isError = name.trimmedLength() !in 1..charCountLimit,
            isEdited = isEdited,
        )
    }

    fun validateStartDate(date: LocalDate): InputDate {
        return createInputDate(date)
    }

    fun validateEndDate(date: LocalDate): InputDate {
        return createInputDate(date)
    }

    fun sanitizeDates(
        termForms: List<TermForm>,
        changedDateIndex: Int,
        isStartDateChanged: Boolean,
    ): List<TermForm> {
        val newTermForms = termForms.toMutableList()

        val prevDay = { date: InputDate -> date.date.minusDays(1L) }
        val nextDay = { date: InputDate -> date.date.plusDays(1L) }

        val fixNextDates = { changed: TermForm ->
            var prev = changed

            for (i in (changedDateIndex + 1) until newTermForms.size) {
                var current = newTermForms[i]
                val isStartDateInvalid = !current.startDate.date.isAfter(prev.endDate.date)
                if (isStartDateInvalid) {
                    newTermForms[i] =
                        current.copy(startDate = createInputDate(nextDay(prev.endDate)))
                    current = newTermForms[i]

                    val isEndDateInvalid = !current.endDate.date.isAfter(current.startDate.date)
                    if (isEndDateInvalid) {
                        newTermForms[i] =
                            current.copy(endDate = createInputDate(nextDay(current.startDate)))
                        current = newTermForms[i]

                        prev = current
                        continue
                    }
                }

                break
            }
        }

        val fixPrevDates = { changed: TermForm ->
            var next = changed

            for (i in (changedDateIndex - 1) downTo 0) {
                var current = newTermForms[i]
                val isEndDateInvalid = !current.endDate.date.isBefore(next.startDate.date)
                if (isEndDateInvalid) {
                    newTermForms[i] =
                        current.copy(endDate = createInputDate(prevDay(next.startDate)))
                    current = newTermForms[i]

                    val isStartDateInvalid =
                        !current.startDate.date.isBefore(current.endDate.date)
                    if (isStartDateInvalid) {
                        newTermForms[i] =
                            current.copy(startDate = createInputDate(prevDay(current.endDate)))
                        current = newTermForms[i]

                        next = current
                        continue
                    }
                }

                break
            }
        }

        run {
            var changed = newTermForms[changedDateIndex]
            val isEndDateInvalid = !changed.endDate.date.isAfter(changed.startDate.date)
            val isStartDateInvalid = !changed.startDate.date.isBefore(changed.endDate.date)

            if (isStartDateChanged && isEndDateInvalid) {
                newTermForms[changedDateIndex] =
                    changed.copy(endDate = createInputDate(nextDay(changed.startDate)))
            } else if (!isStartDateChanged && isStartDateInvalid) {
                newTermForms[changedDateIndex] =
                    changed.copy(startDate = createInputDate(prevDay(changed.endDate)))
            } else {
                return newTermForms
            }

            changed = newTermForms[changedDateIndex]
            fixPrevDates(changed)
            fixNextDates(changed)
        }

        return newTermForms
    }

    fun createDefaultForm(
        firstTermName: String? = null,
        secondTermName: String? = null,
        schoolYearName: String? = null,
        isEdited: Boolean = false,
        status: FormStatus = FormStatus.Idle,
    ): SchoolYearForm {
        val firstTermForm = createDefaultTermForm(term = firstTermName ?: "I")
        val secondTermForm = createDefaultTermForm(term = secondTermName ?: "II")

        val startYear = LocalDate.now().year
        val endYear = startYear + 1
        val name = schoolYearName ?: "Rok $startYear/$endYear"

        val termForms = sanitizeDates(
            termForms = listOf(firstTermForm, secondTermForm),
            changedDateIndex = 0,
            isStartDateChanged = true,
        )

        return SchoolYearForm(
            schoolYearName = validateSchoolYearName(name = name, isEdited = isEdited),
            termForms = termForms,
            status = status,
        )
    }

    private fun createDefaultTermForm(term: String): TermForm {
        val date = LocalDate.now()

        return TermForm(
            name = validateTermName(name = term, isEdited = false),
            startDate = InputDate(date, date.format()),
            endDate = InputDate(date, date.format()),
        )
    }

    private fun createInputDate(date: LocalDate): InputDate {
        return InputDate(date, date.format())
    }
}