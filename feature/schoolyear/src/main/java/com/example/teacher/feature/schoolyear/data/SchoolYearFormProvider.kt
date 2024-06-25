package com.example.teacher.feature.schoolyear.data

import androidx.core.text.trimmedLength
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.domain.GenerateSchoolYearNameUseCase
import com.example.teacher.core.ui.model.FormStatus
import com.example.teacher.core.ui.model.InputField
import java.time.LocalDate

internal object SchoolYearFormProvider {

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

        var changed = newTermForms[changedDateIndex]
        val isEndDateInvalid = !changed.endDate.date.isAfter(changed.startDate.date)
        val isStartDateInvalid = !changed.startDate.date.isBefore(changed.endDate.date)

        if (isStartDateChanged && isEndDateInvalid) {
            newTermForms[changedDateIndex] =
                changed.copy(endDate = createInputDate(nextDay(changed.startDate)))
        } else if (!isStartDateChanged && isStartDateInvalid) {
            newTermForms[changedDateIndex] =
                changed.copy(startDate = createInputDate(prevDay(changed.endDate)))
        }

        changed = newTermForms[changedDateIndex]
        fixPrevDates(changed, changedDateIndex, newTermForms)
        fixNextDates(changed, changedDateIndex, newTermForms)

        return newTermForms
    }

    fun createDefaultForm(
        generateSchoolYearNameUseCase: GenerateSchoolYearNameUseCase,
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
        val name = schoolYearName ?: generateSchoolYearNameUseCase(
            startYear.toString(),
            endYear.toString()
        )

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
            startDate = InputDate(date, TimeUtils.format(date)),
            endDate = InputDate(date, TimeUtils.format(date)),
        )
    }

    private fun fixPrevDates(
        changed: TermForm,
        changedDateIndex: Int,
        newTermForms: MutableList<TermForm>,
    ) {
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

    private fun fixNextDates(
        changed: TermForm,
        changedDateIndex: Int,
        newTermForms: MutableList<TermForm>,
    ) {
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

    private fun createInputDate(date: LocalDate): InputDate {
        return InputDate(date, TimeUtils.format(date))
    }

    private fun prevDay(date: InputDate): LocalDate = date.date.minusDays(1L)

    private fun nextDay(date: InputDate): LocalDate = date.date.plusDays(1L)
}