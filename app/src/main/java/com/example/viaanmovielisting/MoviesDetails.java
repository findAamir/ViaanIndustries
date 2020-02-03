package com.example.viaanmovielisting;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MoviesDetails extends AppCompatActivity {
    ImageView moviePoster;
    TextView movieTitle,movieOverview,movieId,language,movierelease;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        moviePoster = findViewById(R.id.moviePoster);
        movieTitle = findViewById(R.id.movieTitle);
        movieOverview = findViewById(R.id.movieOverview);
        movieId = findViewById(R.id.moviesId);
        language = findViewById(R.id.language);
        movierelease = findViewById(R.id.releasedate);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {

           String Id = (String) bundle.get("movieId");
           String Title = (String) bundle.get("movieTitle");
           String movieThumbnail = (String) bundle.get("movieImage");
           String Overview=(String) bundle.get("movieOverView");
           String Language=(String) bundle.get("movieLanguage");
           String movieRelease = (String)bundle.get("movieRelease");
            Glide.with(getApplicationContext())
                    .load(movieThumbnail)
                    .thumbnail(0.1f)
                    .into(moviePoster);

            movieTitle.setText(Title);
            movieId.setText(Id);
            movieOverview.setText(Overview);
            language.setText(Language);
            movierelease.setText(movieRelease);
        }
    }
}
