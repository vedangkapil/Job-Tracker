package com.Vedang.careerflow.data.dto

import com.Vedang.careerflow.model.Job
import com.google.gson.annotations.SerializedName

/**
 * Mirrors the job JSON returned by the FastAPI backend.
 *
 * The SerializedName annotations handle the backend's snake_case field names.
 */
data class JobDto(
    val id: Int,
    val title: String,
    val company: String,
    val location: String,
    val description: String?,
    val salary: String?,
    @SerializedName("job_url") val jobUrl: String,
    val source: String,
    val keywords: String?,
    @SerializedName("is_tracked") val isTracked: Boolean,
    @SerializedName("scraped_at") val scrapedAt: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("updated_at") val updatedAt: String?
)

fun JobDto.toJob(): Job = Job(
    id = id,
    title = title,
    company = company,
    location = location,
    description = description,
    salary = salary,
    jobUrl = jobUrl,
    source = source,
    keywords = keywords,
    isTracked = isTracked,
    scrapedAt = scrapedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)
