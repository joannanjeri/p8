package com.example.p8

import com.google.gson.annotations.SerializedName

/**
 * the structure of a movie's details returned by the OMDb API
 * @property title the title of the movie
 * @property poster the URL of the movie's poster image
 * @property year the release year of the movie
 * @property rated the age rating of the movie
 * @property runtime the runtime of the movie
 * @property genre the genre of the movie
 * @property imdbRating the IMDb rating of the movie
 * @property imdbID the IMDb ID of the movie, useful for creating IMDb URL links
 */

data class MovieDetails(
    @SerializedName("Title") val title: String,
    @SerializedName("Poster") val poster: String,
    @SerializedName("Year") val year: String,
    @SerializedName("Rated") val rated: String,
    @SerializedName("Runtime") val runtime: String,
    @SerializedName("Genre") val genre: String,
    @SerializedName("imdbRating") val imdbRating: String,
    @SerializedName("imdbID") val imdbID: String
)

/**
 * a summary of a movie's details for a search result from the API
 * this displays a list of movies based on a search
 */

data class MovieSummary(
    @SerializedName("Title") val title: String,
    @SerializedName("Year") val year: String,
    @SerializedName("imdbID") val imdbID: String,
    @SerializedName("Type") val type: String,
    @SerializedName("Poster") val poster: String
)

/**
 * this is the result of a movie search returned by the API
 * it contains a list of movie summaries
 */

data class MovieSearchResult (
    @SerializedName("Search") val search: List<MovieSummary>,
    @SerializedName("totalResults") val totalResults: String,
    @SerializedName("Response") val response: String
)