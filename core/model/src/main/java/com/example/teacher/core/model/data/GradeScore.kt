package com.example.teacher.core.model.data

data class GradeScore(
    val id: Long,
    val gradeTemplateId: Long,
    val twoMinusMinThreshold: Long,
    val twoMinThreshold: Long,
    val twoPlusMinThreshold: Long,
    val threeMinusMinThreshold: Long,
    val threeMinThreshold: Long,
    val threePlusMinThreshold: Long,
    val fourMinusMinThreshold: Long,
    val fourMinThreshold: Long,
    val fourPlusMinThreshold: Long,
    val fiveMinusMinThreshold: Long,
    val fiveMinThreshold: Long,
    val fivePlusMinThreshold: Long,
    val sixMinusMinThreshold: Long,
    val sixMinThreshold: Long,
)