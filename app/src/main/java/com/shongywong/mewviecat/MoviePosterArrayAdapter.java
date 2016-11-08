package com.shongywong.mewviecat;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_movie_poster, parent, false);
        }

        ImageView posterView = (ImageView)convertView.findViewById(R.id.list_item_movie_poster_imageview);
        Picasso.with(getContext()).load("").into(posterView);

        TextView movieTitleView = (TextView)convertView.findViewById(R.id.list_item_movie_title_textview);
        movieTitleView.setText(moviePoster.mTitle);

        TextView voteCountView = (TextView)convertView.findViewById(R.id.list_item_vote_count_textview);
        voteCountView.setText(moviePoster.mVoteCount);

        return convertView;
    }
}
