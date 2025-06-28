package edu.dyds.movies.domain.fakes

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class FakeMovieRepository(
    private val fakeMovieList: List<Movie>
) : MoviesRepository {

    override suspend fun getMovieDetailById(id: Int): Movie? {
        return fakeMovieList.find { it.id == id }
    }

    override suspend fun getPopularMovies(): List<Movie> {
        return fakeMovieList
    }
}