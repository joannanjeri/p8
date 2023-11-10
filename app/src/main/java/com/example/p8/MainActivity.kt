package com.example.p8

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * the main activity handles the creation and setup of searching and displaying
 * movie details from the OMBd API
 */

class MainActivity : AppCompatActivity() {

    private lateinit var movieSearchEditText: EditText
    private lateinit var searchButton: Button
    private lateinit var posterImageView: ImageView
    private lateinit var titleTextView: TextView
    private lateinit var yearTextView: TextView
    private lateinit var ratingTextView: TextView
    private lateinit var runtimeTextView: TextView
    private lateinit var genreTextView: TextView
    private lateinit var imdbTextView: TextView
    private lateinit var linkTextView: TextView
    private lateinit var shareButton: Button
    private lateinit var feedbackButtonContainer: LinearLayout

    /**
     * onCreate calls the setContentView to inflate the UI using findViewById
     * with widgets in the UI, setting up listeners, etc.
     *
     * @param savedInstanceState this bundles contains the most recent data in the state
     * otherwise, it's null
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieSearchEditText = findViewById(R.id.movieSearchEditText)
        searchButton = findViewById(R.id.searchButton)
        posterImageView = findViewById(R.id.posterImageView)
        titleTextView = findViewById(R.id.titleTextView)
        yearTextView = findViewById(R.id.yearTextView)
        ratingTextView = findViewById(R.id.ratingTextView)
        runtimeTextView = findViewById(R.id.runtimeTextView)
        genreTextView = findViewById(R.id.genreTextView)
        imdbTextView = findViewById(R.id.imdbTextView)
        linkTextView = findViewById(R.id.linkTextView)
        shareButton = findViewById(R.id.shareButton)
        feedbackButtonContainer = findViewById(R.id.feedbackButtonContainer)

        // configuring retrofit for network operations with the OMDb API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://www.omdbapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val omdbApi = retrofit.create(OmdbApi::class.java)

        // OnClickListener for the search button to start the movie search process
        searchButton.setOnClickListener {
            val movieTitle = movieSearchEditText.text.toString()
            if (movieTitle.isNotEmpty()) {
                omdbApi.getMovieDetails(movieTitle).enqueue(object : Callback<MovieDetails> {
                    override fun onResponse(
                        call: Call<MovieDetails>,
                        response: Response<MovieDetails>
                    ) {
                        // handles the successful response from the OMDb API
                        if (response.isSuccessful) {
                            val movieDetails = response.body()
                            movieDetails?.let {
                                titleTextView.text = "Title: ${it.title}"
                                yearTextView.text = "Year: ${it.year}"
                                ratingTextView.text = "Rated: ${it.rated}"
                                runtimeTextView.text = "Runtime: ${it.runtime}"
                                genreTextView.text = "Genre: ${it.genre}"
                                imdbTextView.text = "IMDB Rating: ${it.imdbRating}"
                                linkTextView.text = "IMDB Link"
                                Glide.with(this@MainActivity).load(it.poster).into(posterImageView)

                                // onClickListeners for the link and share buttons
                                linkTextView.setOnClickListener { _ ->
                                    val intent = Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse("https://www.imdb.com/title/${it.imdbID}/")
                                    )
                                    startActivity(intent)
                                }

                                shareButton.setOnClickListener { _ ->
                                    val shareIntent = Intent().apply {
                                        action = Intent.ACTION_SEND
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "${it.title} - https://www.imdb.com/title/${it.imdbID}/"
                                        )
                                        type = "text/plain"
                                    }
                                    startActivity(Intent.createChooser(shareIntent, "Share via"))
                                }
                            }
                        } else {
                            Toast.makeText(
                                this@MainActivity,
                                "Failed to get data",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            } else {
                Toast.makeText(this, "Please enter a movie title", Toast.LENGTH_SHORT).show()
            }
        }

        // OnClickListener for the feedback button to allow users to send feedback via email
        feedbackButtonContainer.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("njerijoanna1@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "Feedback for Movie App")
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(intent, "Choose an Email client :"))
            } else {
                Toast.makeText(this, "No email app found", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
