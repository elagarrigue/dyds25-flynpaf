package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository


interface GetMovieDetailsUseCase{
    suspend fun getMovieDetails(title: String): Movie?
}

class GetMovieDetailsUseCaseImpl(
    private val repository: MoviesRepository
): GetMovieDetailsUseCase{
    override suspend fun getMovieDetails(title: String) = repository.getMovieDetailByTitle(title)

}