package com.Vedang.careerflow.data.dto

import com.Vedang.careerflow.model.JobApplication
import com.google.gson.annotations.SerializedName

data class ApplicationDto(
    val id: Int,
    @SerializedName("job_id") val jobId: Int,
    val status: String,
    val notes: String?,
    @SerializedName("applied_date") val appliedDate: String?
)

fun ApplicationDto.toJobApplication() = JobApplication(
    id = id,
    jobId = jobId,
    status = status,
    notes = notes,
    appliedDate = appliedDate
)
