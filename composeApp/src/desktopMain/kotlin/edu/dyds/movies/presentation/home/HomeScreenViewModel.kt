package edu.dyds.movies.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.dyds.movies.domain.entity.QualifiedMovie
import edu.dyds.movies.domain.usecase.GetPopularMoviesUseCase
import edu.dyds.movies.domain.usecase.sortAndMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HomeScreenViewModel (
    private val popularMovieGetter: GetPopularMoviesUseCase
) : ViewModel() {

    private val moviesStateMutableStateFlow = MutableStateFlow(MoviesUiState())

    val moviesStateFlow: Flow<MoviesUiState> = moviesStateMutableStateFlow

    fun getAllMovies() {
        viewModelScope.launch {
            moviesStateMutableStateFlow.emit(
                MoviesUiState(isLoading = true)
            )
            moviesStateMutableStateFlow.emit(
                MoviesUiState(
                    isLoading = false,
                    movies = popularMovieGetter.getPopularMovies()
                )
            )
        }
    }


    data class MoviesUiState(
        val isLoading: Boolean = false,
        val movies: List<QualifiedMovie> = emptyList(),
    )

}
