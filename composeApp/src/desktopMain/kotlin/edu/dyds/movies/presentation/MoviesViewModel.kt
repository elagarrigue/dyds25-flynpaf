package edu.dyds.movies.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.Movie
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.sortAndMap
import edu.dyds.movies.domain.usecase.GetMovieDetailsUseCase
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

private const val MIN_VOTE_AVERAGE = 6.0

class MoviesViewModel(
    private val popularMovieGetter: GetPopularMoviesUseCase,
    private val movieDetailsGetter: GetMovieDetailsUseCase
) : ViewModel() {

    private val moviesStateMutableStateFlow = MutableStateFlow(MoviesUiState())

    private val movieDetailStateMutableStateFlow = MutableStateFlow(MovieDetailUiState())

    val moviesStateFlow: Flow<MoviesUiState> = moviesStateMutableStateFlow

    val movieDetailStateFlow: Flow<MovieDetailUiState> = movieDetailStateMutableStateFlow

    fun getAllMovies() {
        viewModelScope.launch {
            moviesStateMutableStateFlow.emit(
                MoviesUiState(isLoading = true)
            )
            moviesStateMutableStateFlow.emit(
                MoviesUiState(
                    isLoading = false,
                    movies = popularMovieGetter.getPopularMovies().sortAndMap()
                )
            )
        }
    }

    fun getMovieDetail(id: Int) {
        viewModelScope.launch {
            movieDetailStateMutableStateFlow.emit(
                MovieDetailUiState(isLoading = true)
            )
            movieDetailStateMutableStateFlow.emit(
                MovieDetailUiState(
                    isLoading = false,
                    movie = movieDetailsGetter.getMovieDetails(id)?.toDomainMovie()
                )
            )
        }
    }

    data class MoviesUiState(
        val isLoading: Boolean = false,
        val movies: List<QualifiedMovie> = emptyList(),
    )

    data class MovieDetailUiState(
        val isLoading: Boolean = false,
        val movie: Movie? = null,
    )
}


