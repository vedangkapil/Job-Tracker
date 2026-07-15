package com.Vedang.careerflow.model

/**
 * A job as the Android app uses it after it has been mapped from the API response.
 */
data class Job(
    val id: Int,
    val title: String,
    val company: String,
    val location: String,
    val description: String?,
    val salary: String?,
    val jobUrl: String,
    val source: String,
    val keywords: String?,
    val isTracked: Boolean,
    val scrapedAt: String?,
    val createdAt: String?,
    val updatedAt: String?
)
