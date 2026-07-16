package com.Vedang.careerflow.data.repository

import com.Vedang.careerflow.data.api.JobsApiService
import com.Vedang.careerflow.data.api.RetrofitClient
import com.Vedang.careerflow.data.dto.toJob
import com.Vedang.careerflow.data.dto.toJobApplication
import com.Vedang.careerflow.model.Job
import com.Vedang.careerflow.model.JobApplication
import com.Vedang.careerflow.model.JobSearchResult
import com.Vedang.careerflow.model.ScrapeJobsResult
import com.Vedang.careerflow.model.TrackingStatus
import com.Vedang.careerflow.util.AppResult
import retrofit2.HttpException
import java.io.IOException

/**
 * The only layer that ViewModels use to retrieve or update job data.
 */
class JobRepository(
    private val api: JobsApiService = RetrofitClient.jobsApi
) {
    suspend fun getApplications(): AppResult<List<JobApplication>> = safeApiCall {
        api.getApplications().map { it.toJobApplication() }
    }

    suspend fun getJobs(skip: Int = 0, limit: Int = 100): AppResult<List<Job>> = safeApiCall {
        api.getJobs(skip, limit).map { it.toJob() }
    }

    suspend fun getJob(jobId: Int): AppResult<Job> = safeApiCall {
        api.getJob(jobId).toJob()
    }

    suspend fun searchJobs(
        keyword: String? = null,
        location: String? = null,
        source: String? = null
    ): AppResult<JobSearchResult> = safeApiCall {
        api.searchJobs(keyword, location, source).let { response ->
            JobSearchResult(
                jobs = response.jobs.map { it.toJob() },
                count = response.count
            )
        }
    }

    suspend fun scrapeJobs(
        keyword: String,
        location: String,
        sources: List<String> = listOf("linkedin")
    ): AppResult<ScrapeJobsResult> = safeApiCall {
        api.scrapeJobs(keyword, location, sources).let { response ->
            ScrapeJobsResult(
                message = response.message,
                jobs = response.jobs.map { it.toJob() },
                keyword = response.keyword,
                location = response.location,
                sources = response.sources
            )
        }
    }

    suspend fun getTrackedJobs(): AppResult<List<Job>> = safeApiCall {
        api.getTrackedJobs().map { it.toJob() }
    }

    suspend fun getUntrackedJobs(): AppResult<List<Job>> = safeApiCall {
        api.getUntrackedJobs().map { it.toJob() }
    }

    suspend fun trackJob(jobId: Int): AppResult<String> = safeApiCall {
        api.trackJob(jobId).message
    }

    suspend fun untrackJob(jobId: Int): AppResult<String> = safeApiCall {
        api.untrackJob(jobId).message
    }

    suspend fun toggleJobTracking(jobId: Int): AppResult<TrackingStatus> = safeApiCall {
        api.toggleJobTracking(jobId).let { response ->
            TrackingStatus(
                message = response.message,
                isTracked = response.isTracked
            )
        }
    }

    private suspend fun <T> safeApiCall(request: suspend () -> T): AppResult<T> = try {
        AppResult.Success(request())
    } catch (error: IOException) {
        AppResult.Error(
            message = "Couldn't reach the server. Check that the backend is running.",
            cause = error
        )
    } catch (error: HttpException) {
        AppResult.Error(
            message = "The server returned an error (${error.code()}).",
            cause = error
        )
    } catch (error: Exception) {
        AppResult.Error(
            message = "Something went wrong. Please try again.",
            cause = error
        )
    }
}
