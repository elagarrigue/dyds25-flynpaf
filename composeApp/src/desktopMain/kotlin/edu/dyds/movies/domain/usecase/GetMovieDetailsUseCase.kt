package edu.dyds.movies.domain.usecase

import edu.dyds.movies.domain.entity.RemoteMovie
import edu.dyds.movies.domain.repository.MoviesRepository


interface GetMovieDetailsUseCase{
    suspend fun getMovieDetails(id: Int): RemoteMovie?
}

class GetMovieDetailsUseCaseImpl(
    private val repository: MoviesRepository
): GetMovieDetailsUseCase{
    override suspend fun getMovieDetails(id: Int) = repository.getMovieDetailByid(id)

}