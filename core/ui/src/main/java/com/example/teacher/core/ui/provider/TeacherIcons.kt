package com.example.teacher.core.ui.provider

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Details
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Grade
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlusOne
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material.icons.filled.Warning
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.model.TeacherIcon

object TeacherIcons {

    fun navigateBack(@StringRes text: Int = R.string.ui_navigate_back) = TeacherIcon(
        icon = TeacherIcons.ArrowBack,
        text = text,
    )

    fun previous(@StringRes text: Int = R.string.ui_navigate_before) = TeacherIcon(
        icon = TeacherIcons.NavigateBefore,
        text = text,
    )

    fun next(@StringRes text: Int = R.string.ui_navigate_next) = TeacherIcon(
        icon = TeacherIcons.NavigateNext,
        text = text,
    )

    fun schedule(@StringRes text: Int = R.string.ui_schedule) = TeacherIcon(
        icon = TeacherIcons.Schedule,
        text = text,
    )

    fun schoolClasses(@StringRes text: Int = R.string.ui_school_classes) = TeacherIcon(
        icon = TeacherIcons.School,
        text = text,
    )

    fun notes(@StringRes text: Int = R.string.ui_notes) = TeacherIcon(
        icon = TeacherIcons.Notes,
        text = text,
    )

    fun settings(@StringRes text: Int = R.string.ui_settings) = TeacherIcon(
        icon = TeacherIcons.Settings,
        text = text,
    )

    fun valid(@StringRes text: Int = R.string.ui_settings) = TeacherIcon(
        icon = TeacherIcons.CheckCircle,
        text = text,
    )

    fun add(@StringRes text: Int = R.string.ui_add) = TeacherIcon(
        icon = TeacherIcons.Add,
        text = text,
    )

    fun edit(@StringRes text: Int = R.string.ui_edit) = TeacherIcon(
        icon = TeacherIcons.Edit,
        text = text,
    )

    fun warning(@StringRes text: Int = R.string.ui_warning) = TeacherIcon(
        icon = TeacherIcons.Warning,
        text = text,
    )

    fun delete(@StringRes text: Int = R.string.ui_delete) = TeacherIcon(
        icon = TeacherIcons.Delete,
        text = text,
    )

    fun plus(@StringRes text: Int = R.string.ui_plus) = TeacherIcon(
        icon = TeacherIcons.AddCircle,
        text = text,
    )

    fun minus(@StringRes text: Int = R.string.ui_minus) = TeacherIcon(
        icon = TeacherIcons.RemoveCircle,
        text = text,
    )

    fun subject(@StringRes text: Int = R.string.ui_subject) = TeacherIcon(
        icon = TeacherIcons.Subject,
        text = text,
    )

    fun person(@StringRes text: Int = R.string.ui_person) = TeacherIcon(
        icon = TeacherIcons.Person,
        text = text,
    )

    fun people(@StringRes text: Int = R.string.ui_group_of_people) = TeacherIcon(
        icon = TeacherIcons.People,
        text = text,
    )

    fun registerBook(@StringRes text: Int = R.string.ui_register_book) = TeacherIcon(
        icon = TeacherIcons.Book,
        text = text,
    )

    fun email(@StringRes text: Int = R.string.ui_email) = TeacherIcon(
        icon = TeacherIcons.Email,
        text = text,
    )

    fun phone(@StringRes text: Int = R.string.ui_phone) = TeacherIcon(
        icon = TeacherIcons.Phone,
        text = text,
    )

    fun copy(@StringRes text: Int = R.string.ui_content_copy) = TeacherIcon(
        icon = TeacherIcons.ContentCopy,
        text = text,
    )

    fun details(@StringRes text: Int = R.string.ui_details) = TeacherIcon(
        icon = TeacherIcons.Details,
        text = text,
    )

    fun grades(@StringRes text: Int = R.string.ui_grades) = TeacherIcon(
        icon = TeacherIcons.Grade,
        text = text,
    )

    fun attendance(@StringRes text: Int = R.string.ui_attendance) = TeacherIcon(
        icon = TeacherIcons.Groups,
        text = text,
    )

    fun activity(@StringRes text: Int = R.string.ui_activity) = TeacherIcon(
        icon = TeacherIcons.PlusOne,
        text = text,
    )

    fun chart(@StringRes text: Int = R.string.ui_activity) = TeacherIcon(
        icon = TeacherIcons.BarChart,
        text = text,
    )

    private val TeacherIcons = Icons.Default
}