package com.example.teacher.feature.schoolclass

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.teacher.core.common.utils.TimeUtils
import com.example.teacher.core.model.data.SchoolYear
import com.example.teacher.core.model.data.Term
import com.example.teacher.core.testing.HiltTestActivity
import com.example.teacher.core.ui.paramprovider.SchoolYearPreviewParameterProvider
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.shadows.ShadowLog

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class SchoolClassDetailScreenTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = createAndroidComposeRule<HiltTestActivity>()

    @Before
    fun init() {
        hiltRule.inject()
        ShadowLog.stream = System.out // Redirect Logcat to console
    }

    @Test
    fun labelExists() {
        val schoolYear = SchoolYearPreviewParameterProvider().values.first()
        val schoolYearNameMatcher = hasText(schoolYear.name)
        val termNameMatcher = { term: Term ->
            hasText(rule.activity.getString(R.string.school_class_term_name, term.name))
        }
        val termStartDateMatcher = { term: Term ->
            hasText(
                rule.activity.getString(
                    R.string.school_class_term_start,
                    TimeUtils.format(term.startDate)
                )
            )
        }
        val termEndDateMatcher = { term: Term ->
            hasText(
                rule.activity.getString(
                    R.string.school_class_term_end,
                    TimeUtils.format(term.endDate),
                )
            )
        }
        rule.setContent { SchoolClassDetailScreen(schoolYear) }
        rule.onRoot().printToLog("School Class Detail")

        rule.onNode(schoolYearNameMatcher).assertExists()
        rule.onNode(termNameMatcher(schoolYear.firstTerm)).assertExists()
        rule.onNode(termStartDateMatcher(schoolYear.firstTerm)).assertExists()
        rule.onNode(termEndDateMatcher(schoolYear.firstTerm)).assertExists()
        rule.onNode(termNameMatcher(schoolYear.secondTerm)).assertExists()
        rule.onNode(termStartDateMatcher(schoolYear.secondTerm)).assertExists()
        rule.onNode(termEndDateMatcher(schoolYear.secondTerm)).assertExists()
    }

    @Suppress("TestFunctionName")
    @Composable
    private fun SchoolClassDetailScreen(schoolYear: SchoolYear) {
        SchoolClassDetailScreen(
            schoolYear = schoolYear,
            snackbarHostState = remember { SnackbarHostState() },
        )
    }
}