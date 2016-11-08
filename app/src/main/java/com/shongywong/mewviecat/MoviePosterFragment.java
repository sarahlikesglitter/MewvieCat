package com.shongywong.mewviecat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MoviePosterFragment extends Fragment
{
    private MoviePosterArrayAdapter mArrayAdapter;

    public MoviePosterFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_movie_poster, container, false);

        mArrayAdapter = new MoviePosterArrayAdapter(
                getActivity(),
                new ArrayList<MoviePoster>()
        );

        ListView listView = (ListView)rootView.findViewById(R.id.listview_movie_poster);
        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l)
            {
                MoviePoster moviePoster = mArrayAdapter.getItem(index);
                Context context = getActivity();

            }
        });

        return rootView;
    }

    private void updateMovies()
    {
        FetchMoviePostersTask fetchMoviePostersTask = new FetchMoviePostersTask();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String filter = sharedPreferences.getString("filter", "Now Playing");
        fetchMoviePostersTask.execute(filter);
    }

    public class FetchMoviePostersTask extends AsyncTask<String, Void, String[]>
    {
        private final String LOG_TAG = FetchMoviePostersTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(String... params)
        {
            if(params == null)
                return null;

            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String jsonMoviePostersStr = null;
            String language = "en-US";

            try
            {
                final String BASE_URL = "https://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM= "api_key";
                final String LANG_PARAM = "language";
                String filterParam = params[0];

            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            }
            finally
            {
                if(urlConnection != null)
                    urlConnection.disconnect();

                if(bufferedReader != null)
                {
                    try
                    {
                        bufferedReader.close();
                    }
                    catch (final IOException e)
                    {
                        Log.e(LOG_TAG, "Error closing stream ", e);
                    }
                }
            }
        }
    }

}
