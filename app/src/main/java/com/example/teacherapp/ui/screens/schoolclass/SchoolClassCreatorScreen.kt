package com.example.teacherapp.ui.screens.schoolclass

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.example.teacherapp.data.models.entities.SchoolYear
import com.example.teacherapp.data.models.input.FormStatus
import com.example.teacherapp.data.models.input.InputField
import com.example.teacherapp.ui.components.form.FormOutlinedTextField
import com.example.teacherapp.ui.components.form.TeacherOutlinedButton
import com.example.teacherapp.ui.components.utils.PrefixTransformation
import com.example.teacherapp.ui.screens.paramproviders.SchoolYearsPreviewParameterProvider
import com.example.teacherapp.ui.screens.schoolclass.components.SchoolYearInput
import com.example.teacherapp.ui.screens.schoolclass.data.SchoolClassFormProvider
import com.example.teacherapp.ui.theme.TeacherAppTheme

@Composable
fun SchoolClassCreatorScreen(
    schoolClassName: InputField<String>,
    onSchoolClassNameChange: (String) -> Unit,
    schoolYears: List<SchoolYear>,
    schoolYear: InputField<SchoolYear?>,
    onSchoolYearChange: (SchoolYear?) -> Unit,
    status: FormStatus,
    isValid: Boolean,
    onAddSchoolYear: () -> Unit,
    onAddSchoolClass: () -> Unit,
    onSchoolClassAdd: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(status) {
        if (status is FormStatus.Success) {
            onSchoolClassAdd()
        }
    }

    Column(modifier = modifier.padding(8.dp)) {
        ClassNameInput(
            modifier = Modifier.fillMaxWidth(),
            schoolClassName = schoolClassName,
            onSchoolClassNameChange = onSchoolClassNameChange,
        )

        Card {
            SchoolYearInput(
                modifier = Modifier.fillMaxWidth(),
                schoolYears = schoolYears,
                schoolYear = schoolYear,
                onSchoolYearChange = onSchoolYearChange,
                onAddSchoolYear = onAddSchoolYear,
            )
        }

        TeacherOutlinedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onAddSchoolClass,
            enabled = isValid,
        ) {
            Text(text = "Dodaj klasę")
        }
    }
}

@Composable
private fun ClassNameInput(
    schoolClassName: InputField<String>,
    onSchoolClassNameChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    FormOutlinedTextField(
        modifier = modifier,
        inputField = schoolClassName,
        onValueChange = onSchoolClassNameChange,
        label = "Nazwa klasy",
        visualTransformation = PrefixTransformation("(Klasa) ")
    )
}

@Preview
@Composable
private fun SchoolClassCreatorScreenPreview(
    @PreviewParameter(SchoolYearsPreviewParameterProvider::class) schoolYears: List<SchoolYear>,
) {
    TeacherAppTheme {
        Surface {
            val form = SchoolClassFormProvider.createDefaultForm()
            SchoolClassCreatorScreen(
                modifier = Modifier.fillMaxSize(),
                schoolClassName = form.schoolClassName,
                onSchoolClassNameChange = {},
                schoolYears = schoolYears,
                schoolYear = form.schoolYear,
                onSchoolYearChange = {},
                status = form.status,
                isValid = form.isValid,
                onAddSchoolYear = {},
                onAddSchoolClass = {},
                onSchoolClassAdd = {},
            )
        }
    }
}