package com.example.p8

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * interface that defines how to fetch movie details
 */

interface OmdbApi {
    /**
     * gets the movie details from the OMDb API
     *
     * @param title the title of the movie to search for
     * @return a call object that handles the response
     */
    @GET("?apikey=6938dbef")
    fun getMovieDetails(@Query("t") title: String): Call<MovieDetails>

    /**
     * searches for movies that match the query from the API
     *
     * @param query this is the search keyword used to query the API for movies
     * @return a call object that handles the response and cotains a list of movie summaries
     */
    @GET("?apikey=6938dbef")
    fun searchMovies(@Query("s") query: String): Call<MovieSearchResult>
}