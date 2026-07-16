package com.Vedang.careerflow.model

data class JobApplication(
    val id: Int,
    val jobId: Int,
    val status: String,
    val notes: String?,
    val appliedDate: String?
)
