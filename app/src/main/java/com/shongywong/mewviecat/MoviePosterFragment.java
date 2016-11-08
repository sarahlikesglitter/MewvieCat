package com.shongywong.mewviecat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public void onStart()
    {
        super.onStart();
        updateMovies();
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
        String filter = getFilterForURI();
        fetchMoviePostersTask.execute(filter);
    }

    private String getFilterForURI()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String filterStr = sharedPreferences.getString("filter", "Now Playing");
        String result = "now_playing";

        if(filterStr.equals("Now Playing"))
            result = "now_playing";
        else if(filterStr.equals("Popular"))
            result = "popular";
        else
            result = "top_rated";

        return result;
    }

    public class FetchMoviePostersTask extends AsyncTask<String, Void, MoviePoster[]>
    {
        private final String LOG_TAG = FetchMoviePostersTask.class.getSimpleName();

        @Override
        protected MoviePoster[] doInBackground(String... params)
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

                Uri uri = Uri.parse(BASE_URL+filterParam+"?").buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_POSTER_API_KEY)
                        .appendQueryParameter(LANG_PARAM, language)
                        .build();

                URL url = new URL(uri.toString());

                Log.v(LOG_TAG, "Built URL " + uri.toString());

                urlConnection = (HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if(inputStream == null)
                    return null;

                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while((line = bufferedReader.readLine()) != null)
                    buffer.append(line + "\n");

                if(buffer.length() == 0)
                    return null;

                jsonMoviePostersStr = buffer.toString();

                MoviePoster[] parsedMoviePosters = parseMoviePosterJSON(jsonMoviePostersStr);
                Log.v(LOG_TAG, "JSON response " + jsonMoviePostersStr);
                return parsedMoviePosters;
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

        @Override
        public void onPostExecute(MoviePoster[] params)
        {
            if(params != null)
            {
                mArrayAdapter.clear();
                mArrayAdapter.addAll(params);
            }

        }

        private MoviePoster[] parseMoviePosterJSON(String jsonStr)
        {
            try
            {


                JSONObject jsonObject = new JSONObject(jsonStr);
                JSONArray jsonMoviesArray = jsonObject.getJSONArray("results");
                MoviePoster[] moviePosterArray = new MoviePoster[jsonMoviesArray.length()];
                JSONObject jsonMovie;

                for(int i = 0; i < jsonMoviesArray.length(); i++)
                {
                    jsonMovie = jsonMoviesArray.getJSONObject(i);

                    moviePosterArray[i] = new MoviePoster(
                            jsonMovie.getString("original_title"),
                            jsonMovie.getString("poster_path"),
                            jsonMovie.getString("release_date"),
                            jsonMovie.getDouble("popularity"),
                            jsonMovie.getDouble("vote_average"),
                            jsonMovie.getString("overview")
                            );
                }

                return moviePosterArray;
            }
            catch(JSONException e)
            {
                Log.e(LOG_TAG, "Error " + e);
                return null;
            }
        }
    }

}
