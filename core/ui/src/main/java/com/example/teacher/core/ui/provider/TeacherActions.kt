package com.example.teacher.core.ui.provider

import androidx.annotation.StringRes
import com.example.teacher.core.ui.R
import com.example.teacher.core.ui.model.TeacherAction
import com.example.teacher.core.ui.model.TeacherIcon

object TeacherActions {

    fun previous(onClick: () -> Unit, @StringRes text: Int = R.string.ui_navigate_before) =
        createAction(TeacherIcons.previous(), onClick, text)

    fun next(onClick: () -> Unit, @StringRes text: Int = R.string.ui_navigate_next) =
        createAction(TeacherIcons.next(), onClick, text)

    fun add(onClick: () -> Unit, @StringRes text: Int = R.string.ui_add) =
        createAction(TeacherIcons.add(), onClick, text)

    fun edit(onClick: () -> Unit, @StringRes text: Int = R.string.ui_edit) =
        createAction(TeacherIcons.edit(), onClick, text)

    fun delete(onClick: () -> Unit, @StringRes text: Int = R.string.ui_delete) =
        createAction(TeacherIcons.delete(), onClick, text)

    fun plus(onClick: () -> Unit, @StringRes text: Int = R.string.ui_plus) =
        createAction(TeacherIcons.plus(), onClick, text)

    fun minus(onClick: () -> Unit, @StringRes text: Int = R.string.ui_minus) =
        createAction(TeacherIcons.minus(), onClick, text)

    private fun createAction(
        teacherIcon: TeacherIcon,
        onClick: () -> Unit,
        @StringRes text: Int = R.string.ui_minus,
    ): TeacherAction = with(teacherIcon) {
        TeacherAction(
            text = text,
            imageVector = this.icon,
            contentDescription = this.text,
            onClick = onClick,
        )
    }
}