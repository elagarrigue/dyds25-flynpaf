package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.repository.MoviesRepository

private const val MIN_VOTE_AVERAGE = 6.0

interface GetPopularMoviesUseCase{
    suspend fun getPopularMovies():List<RemoteMovie>
}

class GetPopularMoviesUseCaseImpl(
    private val repository: MoviesRepository
): GetPopularMoviesUseCase {
    private val cacheMovies: MutableList<RemoteMovie> = mutableListOf()

    override suspend fun getPopularMovies() =
        if (cacheMovies.isNotEmpty()) {
            cacheMovies
        } else initializeMovieCache()


    private suspend fun initializeMovieCache(): List<RemoteMovie> =
        try {
            initializeMovieCacheUnsafe()
        } catch (e: Exception) {
            emptyList()
        }

    private suspend fun initializeMovieCacheUnsafe(): List<RemoteMovie> =
        repository.getPopularMovies().apply {
            cacheMovies.clear()
            cacheMovies.addAll(this)
        }
    }


fun List<RemoteMovie>.sortAndMap(): List<QualifiedMovie> {
    return this
        .sortedByDescending { it.voteAverage }
        .map {
            QualifiedMovie(
                movie = it.toDomainMovie(),
                isGoodMovie = it.voteAverage >= MIN_VOTE_AVERAGE
            )
        }
}