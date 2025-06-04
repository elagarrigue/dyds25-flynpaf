package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository


interface GetMovieDetailsUseCase{
    suspend fun getMovieDetails(id: Int): Movie?
}

class GetMovieDetailsUseCaseImpl(
    private val repository: MoviesRepository
): GetMovieDetailsUseCase{
    override suspend fun getMovieDetails(id: Int) = repository.getMovieDetailByid(id)

}