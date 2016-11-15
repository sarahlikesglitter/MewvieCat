package com.shongywong.mewviecat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by shongywong on 11/8/2016.
 */
public class MoviePosterArrayAdapter extends ArrayAdapter<MoviePoster>
{
    private static final String LOG_TAG = MoviePosterArrayAdapter.class.getSimpleName();

    public MoviePosterArrayAdapter(Activity context, List<MoviePoster> moviePosters)
    {
        super(context, 0, moviePosters);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        MoviePoster moviePoster = getItem(position);

        if(convertView == null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie_poster, parent, false);
        }

        final ProgressBar progressBar = (ProgressBar)convertView.findViewById(R.id.progressBar_grid_item_movie_poster);
        progressBar.setVisibility(View.VISIBLE);

        final TextView titleView = (TextView)convertView.findViewById(R.id.textview_grid_item_movie_poster_title);
        final String titleText = moviePoster.mTitle;

        final ImageView posterView = (ImageView)convertView.findViewById(R.id.imageview_grid_item_movie_poster);

        final String SIZE = "w185/";
        Picasso.with(getContext())
                .load(Constants.BASE_URL_MOVIE_POSTER+SIZE+moviePoster.mPosterPath)
                .error(R.drawable.movie_poster_placeholder)
                .into(posterView, new Callback()
                {
                    @Override
                    public void onSuccess()
                    {
                        progressBar.setVisibility(View.GONE);
                        titleView.setText("");
                    }

                    @Override
                    public void onError()
                    {
                        titleView.setText(titleText);
                        progressBar.setVisibility(View.GONE);
                        posterView.setAlpha(100);
                    }
                });

        return convertView;
    }
}
