package com.Vedang.careerflow.data.api

import com.Vedang.careerflow.data.dto.ActionResponseDto
import com.Vedang.careerflow.data.dto.BackendStatusDto
import com.Vedang.careerflow.data.dto.JobDto
import com.Vedang.careerflow.data.dto.JobSearchResponseDto
import com.Vedang.careerflow.data.dto.ScrapeJobsResponseDto
import com.Vedang.careerflow.data.dto.ToggleTrackingResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface JobsApiService {

    @GET("/")
    suspend fun getBackendStatus(): BackendStatusDto

    @GET("api/jobs/")
    suspend fun getJobs(
        @Query("skip") skip: Int = 0,
        @Query("limit") limit: Int = 100
    ): List<JobDto>

    @GET("api/jobs/{jobId}")
    suspend fun getJob(@Path("jobId") jobId: Int): JobDto

    @GET("api/jobs/search/")
    suspend fun searchJobs(
        @Query("keyword") keyword: String? = null,
        @Query("location") location: String? = null,
        @Query("source") source: String? = null
    ): JobSearchResponseDto

    @POST("api/jobs/scrape/")
    suspend fun scrapeJobs(
        @Query("keyword") keyword: String,
        @Query("location") location: String,
        @Query("sources") sources: List<String> = listOf("linkedin")
    ): ScrapeJobsResponseDto

    @GET("api/jobs/tracked/")
    suspend fun getTrackedJobs(): List<JobDto>

    @GET("api/jobs/untracked/")
    suspend fun getUntrackedJobs(): List<JobDto>

    @POST("api/jobs/{jobId}/track/")
    suspend fun trackJob(@Path("jobId") jobId: Int): ActionResponseDto

    @POST("api/jobs/{jobId}/untrack/")
    suspend fun untrackJob(@Path("jobId") jobId: Int): ActionResponseDto

    @POST("api/jobs/{jobId}/toggle/")
    suspend fun toggleJobTracking(@Path("jobId") jobId: Int): ToggleTrackingResponseDto
}
