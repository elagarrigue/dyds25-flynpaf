package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class RemoteResult(
    val page: Int,
    val results: List<RemoteMovie>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class RemoteMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("release_date") val releaseDate: String,
    @SerialName("poster_path") val posterPath: String,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val popularity: Double,
    @SerialName("vote_average") val voteAverage: Double,
    )

    fun toDomainMovie(remoteMovie: RemoteMovie): Movie {
        return Movie(
            id = remoteMovie.id,
            title = remoteMovie.title,
            overview = remoteMovie.overview,
            releaseDate = remoteMovie.releaseDate,
            poster = "https://image.tmdb.org/t/p/w185${remoteMovie.posterPath}",
            backdrop = remoteMovie.backdropPath.let { "https://image.tmdb.org/t/p/w780$it" },
            originalTitle = remoteMovie.originalTitle,
            originalLanguage = remoteMovie.originalLanguage,
            popularity = remoteMovie.popularity,
            voteAverage = remoteMovie.voteAverage
        )
    }

