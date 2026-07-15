package com.Vedang.careerflow.model

data class JobSearchResult(
    val jobs: List<Job>,
    val count: Int
)

data class ScrapeJobsResult(
    val message: String,
    val jobs: List<Job>,
    val keyword: String,
    val location: String,
    val sources: List<String>
)

data class TrackingStatus(
    val message: String,
    val isTracked: Boolean
)
