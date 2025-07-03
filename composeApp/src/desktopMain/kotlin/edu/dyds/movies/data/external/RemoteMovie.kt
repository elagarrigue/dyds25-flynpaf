package edu.dyds.movies.data.external

import edu.dyds.movies.domain.entity.Movie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class RemoteResult(
    val page: Int,
    val results: List<tmdbRemoteMovie>,
    @SerialName("total_pages") val totalPages: Int,
    @SerialName("total_results") val totalResults: Int
)

@Serializable
data class tmdbRemoteMovie(
    val id: Int,
    val title: String,
    val overview: String,
    @SerialName("release_date") val releaseDate: String?,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("original_title") val originalTitle: String,
    @SerialName("original_language") val originalLanguage: String,
    val popularity: Double?,
    @SerialName("vote_average") val voteAverage: Double?,

    ) {
    fun toDomainMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview,
            releaseDate = releaseDate ?: "",
            poster = "https://image.tmdb.org/t/p/w185$posterPath",
            backdrop = backdropPath.let { "https://image.tmdb.org/t/p/w780$it" },
            originalTitle = originalTitle,
            originalLanguage = originalLanguage,
            popularity = popularity ?: 0.0,
            voteAverage = voteAverage ?: 0.0
        )
    }
}


@Serializable
data class RemoteMovie(
    @SerialName("Title") val title: String,
    @SerialName("Plot") val plot: String,
    @SerialName("Released") val released: String,
    @SerialName("Year") val year: String,
    @SerialName("Poster") val poster: String,
    @SerialName("Language") val language: String,
    @SerialName("Metascore") val metaScore: String,
    val imdbRating: Double
) {
    fun toDomainMovie(): Movie {
        return Movie(
            id = title.hashCode(),
            title = title,
            overview = plot,
            releaseDate = if (released.isNotEmpty() && released != "N/A") released else year,
            poster = poster,
            backdrop = poster,
            originalTitle = title,
            originalLanguage = language,
            popularity = imdbRating,
            voteAverage = if (metaScore.isNotEmpty() && metaScore != "N/A") metaScore.toDouble() else 0.0
        )
    }
}

