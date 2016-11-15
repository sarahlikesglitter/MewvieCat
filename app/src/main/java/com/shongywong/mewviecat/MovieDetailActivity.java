package com.shongywong.mewviecat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class MovieDetailActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieDetailFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id == R.id.action_filter)
        {
            startActivity(new Intent(this, FilterActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.movie_detail_menu, menu);
        return true;
    }

    public static class MovieDetailFragment extends Fragment
    {
        private MoviePoster mMoviePoster;
        private final String LOG_TAG = MovieDetailFragment.class.getSimpleName();

        public MovieDetailFragment()
        {
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            Intent intent = getActivity().getIntent();
            View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

            if(intent != null && intent.hasExtra(Constants.PARCEL_MOVIE_DETAIL_STR))
            {
                mMoviePoster = intent.getParcelableExtra(Constants.PARCEL_MOVIE_DETAIL_STR);

                final ProgressBar progressBar = (ProgressBar)rootView.findViewById(R.id.progressBar_frag_movie_detail);
                progressBar.setVisibility(View.VISIBLE);

                ImageView posterView = (ImageView)rootView.findViewById(R.id.imageView_frag_movie_detail_poster);
                final String SIZE = "w342";
                Picasso.with(getContext())
                        .load(Constants.BASE_URL_MOVIE_POSTER+SIZE+mMoviePoster.mPosterPath)
                        .error(R.drawable.movie_poster_placeholder)
                        .into(posterView, new Callback()
                        {
                            @Override
                            public void onSuccess()
                            {
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError()
                            {
                                progressBar.setVisibility(View.GONE);
                            }
                        });

                //BUG with showing the details. the title textview gets overwritten by whatever comes after
                TextView titleTextView = (TextView)rootView.findViewById(R.id.textView_frag_movie_detail_title);
                titleTextView.setText(mMoviePoster.mTitle);

                TextView releaseDateTextView = (TextView)rootView.findViewById(R.id.textView2_frag_movie_detail_date);
                releaseDateTextView.setText("Released: " + mMoviePoster.mReleaseDate);

                TextView ratingTextView = (TextView)rootView.findViewById(R.id.textView3_frag_movie_detail_rating);
                ratingTextView.setText("Viewer Rating: " + mMoviePoster.mVoteAverage);

                ScrollView scrollView = (ScrollView)rootView.findViewById((R.id.scrollView));

                WebView summaryWebView = (WebView)scrollView.findViewById(R.id.webView_frag_movie_detail_summary);
                StringBuilder summaryText = new StringBuilder();
                summaryText.append("<html><body><p align=\"justify\">");
                summaryText.append(mMoviePoster.mSummary);
                summaryText.append("</p></body></html>");
                summaryWebView.setBackgroundColor(Color.TRANSPARENT);
                summaryWebView.loadData(summaryText.toString(), "text/html", "utf-8");
            }
            return rootView;
        }
    }
}
