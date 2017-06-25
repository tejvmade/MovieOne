package com.example.movieone;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.example.movieone.models.MovieList;
import com.example.movieone.utils.NetworkUtils;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private Context context;
    private List<MovieList> movie_list;


    MovieListAdapter(List<MovieList> movies){
        this.movie_list = movies;
    }

    void setMovieData(List<MovieList> movieData){
        movie_list = movieData;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titleTextView, ratingTextView, voterTextView, releaseTextView;
        ImageView posterImageView;
        RatingBar ratingBar;

        ViewHolder(View view){
            super(view);
            view.setClickable(true);
            view.setOnClickListener(this);
            titleTextView = (TextView) view.findViewById(R.id.tv_movie_title);
            ratingTextView = (TextView) view.findViewById(R.id.rating_score);
            voterTextView = (TextView) view.findViewById(R.id.num_of_votes);
            releaseTextView = (TextView) view.findViewById(R.id.year);
            ratingBar = (RatingBar) view.findViewById(R.id.rating_bar);
            posterImageView = (ImageView) view.findViewById(R.id.iv_poster);
            ratingBar.setStepSize((float)0.1);
            context = view.getContext();
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent movieDetail = new Intent(context, DetailActivity.class);
            String MOVIE_TITLE = "title";
            movieDetail.putExtra(MOVIE_TITLE, movie_list.get(position).title);
            String MOVIE_OVERVIEW = "overview";
            movieDetail.putExtra(MOVIE_OVERVIEW, movie_list.get(position).overview);
            String MOVIE_POSTER = "poster_path";
            movieDetail.putExtra(MOVIE_POSTER, movie_list.get(position).posterPath);
            String MOVIE_BACK_DROP = "backdrop_path";
            movieDetail.putExtra(MOVIE_BACK_DROP, movie_list.get(position).backdropPath);
            String MOVIE_RELEASE = "release_date";
            movieDetail.putExtra(MOVIE_RELEASE, movie_list.get(position).releaseDate);
            String MOVIE_VOTE_AVERAGE = "vote_average";
            movieDetail.putExtra(MOVIE_VOTE_AVERAGE, String.valueOf(movie_list.get(position).voteAverage));
            String MOVIE_VOTE_COUNT = "vote_count";
            movieDetail.putExtra(MOVIE_VOTE_COUNT, String.valueOf(movie_list.get(position).voteCount));
            context.startActivity(movieDetail);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view;
        view = LayoutInflater.from(context).inflate(R.layout.movie_list_item, parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titleTextView.setText(movie_list.get(position).title);
        holder.voterTextView.setText("(" + movie_list.get(position).voteCount + ")");
        float rating_score = (float)(movie_list.get(position).voteAverage/10)*5;
        holder.ratingTextView.setText(String.format("%.1f",rating_score));
        holder.ratingBar.setRating(rating_score);
        holder.releaseTextView.setText(movie_list.get(position).releaseDate.substring(0,4));

        String url = NetworkUtils.buildMovieUrl(movie_list.get(position).posterPath).toString();
        Picasso.with(context).load(url)
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        if(null == movie_list) return 0;
        return movie_list.size();
    }

}