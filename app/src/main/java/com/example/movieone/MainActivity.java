package com.example.movieone;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import com.example.movieone.models.MovieList;
import com.example.movieone.models.MoviesResponse;
import com.example.movieone.utils.NetworkUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ProgressBar mProgressBar;
    private RecyclerView mRecyclerView;
    private ArrayList<MovieList> movie_list;
    int currentPageNo = 1, totalPageNo = 1;
    private CoordinatorLayout coordinatorLayout;
    private String SORT_POPULAR = "movie/popular";
    String sortType = SORT_POPULAR;
    MovieListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Popular Movies");
        setSupportActionBar(toolbar);

        movie_list = new ArrayList<>();
        MoviesResponse moviesResponse = new MoviesResponse();
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movie_posters);
        mRecyclerView.setHasFixedSize(true);


        //Setting the layout manager for the RecyclerView


        final int columns = getResources().getInteger(R.integer.grid_columns);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, columns, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        //Setting the adapter for the RecyclerView

        adapter = new MovieListAdapter(movie_list);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        fetchMovies(sortType, currentPageNo);
    }

    public void fetchMovies(final String sort, final int page) {
        if (isNetworkAvailable()) {
            loadMovies(sort, page);

        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRecyclerView.setVisibility(View.INVISIBLE);
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No Internet Connect, Please" +
                    " Turn ON data and click RETRY", Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMovies(sort, page);
                }
            });
            snackbar.show();
        }
    }

    public void showViews() {
    }

    public void setTextViews() {
    }

    public void updateUI() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    public void loadMovies(String sort, int page) {
        URL movieRequestUrl = NetworkUtils.buildUrl(sort, page);
        new FetchMovieTask().execute(movieRequestUrl);
    }

    private class FetchMovieTask extends AsyncTask<URL, Void, Void> {
        long totalPages = 0;
        long totalResults = 0;

        @Override
        protected void onPreExecute() {
            showLoading();
        }

        @Override
        protected Void doInBackground(URL... params) {
            URL movieRequestUrl = params[0];
            try {
                String jsonResponse =
                        NetworkUtils.getResponseFromHttpUrl(movieRequestUrl);
                try {
                    movie_list.clear();
                    JSONObject object = new JSONObject(jsonResponse);
                    totalPages = object.getLong("total_pages");
                    totalPageNo = (int) totalPages;
                    totalResults = object.getLong("total_results");
                    JSONArray jsonArray = object.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);


                        MovieList addMovies = new MovieList();
                        addMovies.overview = obj.getString("overview");
                        addMovies.releaseDate = obj.getString("release_date");
                        addMovies.title = obj.getString("title");
                        addMovies.voteAverage = obj.getDouble("vote_average");
                        addMovies.voteCount = obj.getLong("vote_count");
                        addMovies.id = obj.getInt("id");
                        addMovies.posterPath = obj.getString("poster_path");
                        addMovies.backdropPath = obj.getString("backdrop_path");
                        movie_list.add(addMovies);

                    }

                } catch (JSONException j) {
                    j.printStackTrace();
                    return null;
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (movie_list != null) {
                updateUI();
                showViews();
                adapter.setMovieData(movie_list);

            }
        }
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                //will start about activity yet to be added
                return true;
            case R.id.action_settings:
                //if setting will be later added this will implement it
                return true;
            case R.id.action_sort_popular:
                if (!Objects.equals(sortType, SORT_POPULAR)) {
                    sortType = SORT_POPULAR;
                    fetchMovies(sortType, currentPageNo);
                }
                return true;
            case R.id.action_sort_top_rated:
                String SORT_RATED = "movie/top_rated";
                if (!Objects.equals(sortType, SORT_RATED)) {
                    sortType = SORT_RATED;
                    fetchMovies(sortType, currentPageNo);
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isNetworkAvailable() {
        boolean status;
        try {
            ConnectivityManager cm =
                    (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            status = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;

    }
}
