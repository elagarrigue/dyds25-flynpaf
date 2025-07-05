package edu.dyds.movies.domain.fakes

import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.repository.MoviesRepository

class FakeMovieRepository(
    private val fakeMovieList: List<Movie>
) : MoviesRepository {

    override suspend fun getMovieDetailByTitle(title: String): Movie? {
        return fakeMovieList.find { it.title == title }
    }

    override suspend fun getPopularMovies(): List<Movie> {
        return fakeMovieList
    }
}