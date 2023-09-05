package com.example.teacher.core.ui.provider

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Subject
import androidx.compose.material.icons.filled.Warning
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.model.TeacherIcon

object TeacherIcons {

    fun navigateBack(@StringRes text: Int = R.string.navigate_back) = TeacherIcon(
        icon = TeacherIcons.ArrowBack,
        text = text,
    )

    fun previous(@StringRes text: Int = R.string.navigate_before) = TeacherIcon(
        icon = TeacherIcons.NavigateBefore,
        text = text,
    )

    fun next(@StringRes text: Int = R.string.navigate_next) = TeacherIcon(
        icon = TeacherIcons.NavigateNext,
        text = text,
    )

    fun schedule(@StringRes text: Int = R.string.schedule) = TeacherIcon(
        icon = TeacherIcons.Schedule,
        text = text,
    )

    fun schoolClasses(@StringRes text: Int = R.string.school_classes) = TeacherIcon(
        icon = TeacherIcons.School,
        text = text,
    )

    fun notes(@StringRes text: Int = R.string.notes) = TeacherIcon(
        icon = TeacherIcons.Notes,
        text = text,
    )

    fun settings(@StringRes text: Int = R.string.settings) = TeacherIcon(
        icon = TeacherIcons.Settings,
        text = text,
    )

    fun valid(@StringRes text: Int = R.string.settings) = TeacherIcon(
        icon = TeacherIcons.CheckCircle,
        text = text,
    )

    fun add(@StringRes text: Int = R.string.add) = TeacherIcon(
        icon = TeacherIcons.Add,
        text = text,
    )

    fun edit(@StringRes text: Int = R.string.edit) = TeacherIcon(
        icon = TeacherIcons.Edit,
        text = text,
    )

    fun warning(@StringRes text: Int = R.string.warning) = TeacherIcon(
        icon = TeacherIcons.Warning,
        text = text,
    )

    fun delete(@StringRes text: Int = R.string.delete) = TeacherIcon(
        icon = TeacherIcons.Delete,
        text = text,
    )

    fun plus(@StringRes text: Int = R.string.plus) = TeacherIcon(
        icon = TeacherIcons.AddCircle,
        text = text,
    )

    fun minus(@StringRes text: Int = R.string.minus) = TeacherIcon(
        icon = TeacherIcons.RemoveCircle,
        text = text,
    )

    fun subject(@StringRes text: Int = R.string.subject) = TeacherIcon(
        icon = TeacherIcons.Subject,
        text = text,
    )

    fun person(@StringRes text: Int = R.string.person) = TeacherIcon(
        icon = TeacherIcons.Person,
        text = text,
    )

    fun email(@StringRes text: Int = R.string.email) = TeacherIcon(
        icon = TeacherIcons.Email,
        text = text,
    )

    fun phone(@StringRes text: Int = R.string.phone) = TeacherIcon(
        icon = TeacherIcons.Phone,
        text = text,
    )

    fun copy(@StringRes text: Int = R.string.content_copy) = TeacherIcon(
        icon = TeacherIcons.ContentCopy,
        text = text,
    )

    private val TeacherIcons = Icons.Default
}