package com.Vedang.careerflow.data.dto

import com.google.gson.annotations.SerializedName

/** Response from GET /api/jobs/search/. */
data class JobSearchResponseDto(
    val jobs: List<JobDto>,
    val count: Int
)

/** Response from POST /api/jobs/scrape/. */
data class ScrapeJobsResponseDto(
    val message: String,
    val jobs: List<JobDto>,
    val keyword: String,
    val location: String,
    val sources: List<String>
)

/** Response from the track and untrack endpoints. */
data class ActionResponseDto(
    val message: String
)

/** Response from POST /api/jobs/{jobId}/toggle/. */
data class ToggleTrackingResponseDto(
    val message: String,
    @SerializedName("is_tracked") val isTracked: Boolean
)

/** Response from GET /. Used to verify that the backend is reachable. */
data class BackendStatusDto(
    val message: String,
    val version: String,
    val features: List<String>,
    val docs: String,
    val redoc: String
)
