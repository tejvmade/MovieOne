package com.example.movieone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.movieone.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

@SuppressWarnings("deprecation")

public class DetailActivity extends AppCompatActivity {

    String MOVIE_TITLE = "title";
    String MOVIE_OVERVIEW = "overview";
    String MOVIE_RELEASE = "release_date";
    String MOVIE_POSTER = "poster_path";
    String MOVIE_VOTE_AVERAGE = "vote_average";
    String MOVIE_VOTE_COUNT = "vote_count";
    String MOVIE_BACK_DROP = "backdrop_path";

    private TextView titleTextView, releaseDateTextView, ratingTextView, voteCountTextView, overviewTextView, voteAverageTextView;
    private ImageView posterImageView, backDropImageView;
    private RatingBar ratingBar;
    private CollapsingToolbarLayout collapsingToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent intent = this.getIntent();

        TextView titleTextView = (TextView) findViewById(R.id.title_details);
        TextView releaseDateTextView = (TextView) findViewById(R.id.year_details);
        TextView ratingTextView = (TextView) findViewById(R.id.rating_score_detail);
        TextView voteAverageTextView = (TextView) findViewById(R.id.average_vote);
        TextView overviewTextView = (TextView) findViewById(R.id.plot_synopsis);
        TextView voteCountTextView = (TextView) findViewById(R.id.num_of_votes_detail);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.rating_bar_detail);
        ImageView posterImageView = (ImageView) findViewById(R.id.iv_movie_poster_details);
        ImageView backDropImageView = (ImageView) findViewById(R.id.iv_backdrop);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);}


    private void populate() {

        Intent intent = getIntent();
        ActionBar actionBar;

        collapsingToolbar.setExpandedTitleColor(getResources().getColor(android.R.color.darker_gray));
        collapsingToolbar.setCollapsedTitleTextColor(getResources().getColor(android.R.color.white));
        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbar.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));

        if (intent.hasExtra("title")) {
            setTitle(intent.getStringExtra(MOVIE_TITLE));
            titleTextView.setText(intent.getStringExtra(MOVIE_TITLE));
        }
        releaseDateTextView.setText(intent.getStringExtra(MOVIE_RELEASE));
        voteCountTextView.setText("(" + String.valueOf(intent.getStringExtra(MOVIE_VOTE_COUNT)) + ")");
        overviewTextView.setText(intent.getStringExtra(MOVIE_OVERVIEW));
        voteAverageTextView.setText(intent.getStringExtra(MOVIE_VOTE_AVERAGE) + "/10");
        String url1 = NetworkUtils.buildMovieUrl(intent.getStringExtra(MOVIE_POSTER)).toString();
        Picasso.with(this)
                .load(url1)
                .into(posterImageView);

        String url2 = NetworkUtils.buildMovieUrl(intent.getStringExtra(MOVIE_BACK_DROP)).toString();
        Picasso.with(this)
                .load(url2)
                .into(backDropImageView);

        double voteAverage = Double.parseDouble(intent.getStringExtra(MOVIE_VOTE_AVERAGE));
        voteAverage = (voteAverage / 10) * 5;
        @SuppressLint("DefaultLocale") String rating = String.format("%.1f", voteAverage);
        voteAverage = Double.parseDouble(rating);

        ratingTextView.setText(rating);
        ratingBar.setRating((float) voteAverage);
        ratingBar.setStepSize((float) 0.1);


    }
}



