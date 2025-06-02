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

    override suspend fun getPopularMovies() =
        repository.getPopularMovies()

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