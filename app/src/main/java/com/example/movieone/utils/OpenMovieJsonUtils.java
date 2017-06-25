package com.example.movieone.utils;


import android.content.Context;

import com.example.movieone.MovieListAdapter;
import com.example.movieone.models.MovieList;
import com.example.movieone.models.MoviesResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class OpenMovieJsonUtils {

    private static MoviesResponse moviesResponse = new MoviesResponse();
    private static List<MovieList> movie_list;
    private static MovieListAdapter movieListAdapter;

    public static void getSimpleMovieStringsFromJson(Context context, String movieJsonStr)
            throws JSONException {
        JSONObject movieJson = new JSONObject(movieJsonStr);

        moviesResponse.setPage(movieJson.getDouble("page"));

        JSONArray results = movieJson.getJSONArray("results");

        moviesResponse.setTotalPages(movieJson.getDouble("total_pages"));

        moviesResponse.setTotalResults(movieJson.getDouble("total_results"));

        for (int i=0; i<results.length(); i++){
            JSONObject obj = results.getJSONObject(i);

            MovieList addMovie = new MovieList();
            addMovie.posterPath = obj.getString("poster_path");
            addMovie.voteCount = obj.getLong("vote_count");
            addMovie.overview = obj.getString("overview");
            addMovie.voteAverage = obj.getDouble("vote_average");
            addMovie.releaseDate = obj.getString("release_date");
            addMovie.id = obj.getInt("id");

            movie_list.add(addMovie);
        }
    }
}
