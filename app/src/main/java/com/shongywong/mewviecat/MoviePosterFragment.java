package com.shongywong.mewviecat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.GridView;
import android.widget.Toast;

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
    private ArrayList<MoviePoster> moviesList;
    private int mCurrentpage = 1;

    public MoviePosterFragment(){}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //TODO possible feature - save user's favorite movies or a list of movies
        //they want to want in the future
//        if(savedInstanceState == null || !savedInstanceState.containsKey("moviePosters"))
//        {
//            moviesList = new ArrayList<MoviePoster>()
//        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        updateMovies(mCurrentpage);
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

        GridView gridView = (GridView)rootView.findViewById(R.id.gridview_movie_poster);
        gridView.setAdapter(mArrayAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l)
            {
                MoviePoster moviePoster = mArrayAdapter.getItem(index);
                Intent movieDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
                movieDetailIntent.putExtra(Constants.PARCEL_MOVIE_DETAIL_STR, moviePoster);
                startActivity(movieDetailIntent);
            }
        });
        gridView.setOnScrollListener(new EndlessScrollListener()
        {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount)
            {
                Log.v("ScrollListener ", "onloadmore Calling updateMovies..");
                mCurrentpage = page;
                updateMovies(mCurrentpage);
                return true;
            }
        });

        return rootView;
    }

    private void updateMovies(int page)
    {
        if(isOnline())
        {
            FetchMoviePostersTask fetchMoviePostersTask = new FetchMoviePostersTask();
            String filter = getFilterForURI();
            fetchMoviePostersTask.execute(filter, page+"");
        }
        else
        {
            Toast.makeText(getActivity(), "Error refreshing movies", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isOnline()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private String getFilterForURI()
    {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String filterStr = sharedPreferences.getString(Constants.PREF_FILTER_STR, Constants.PREF_KEY_FILTER_NOW_PLAYING_STR);
        String result = Constants.PREF_VAL_FILTER_NOW_PLAYING_STR;

        if(filterStr.equals(Constants.PREF_KEY_FILTER_NOW_PLAYING_STR))
            result = Constants.PREF_VAL_FILTER_NOW_PLAYING_STR;
        else if(filterStr.equals(Constants.PREF_KEY_FILTER_POPULAR_STR))
            result = Constants.PREF_VAL_FILTER_POPULAR_STR;
        else
            result = Constants.PREF_VAL_FILTER_TOP_RATED_STR;

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
            String languageParam = "en-US";

            try
            {
                final String BASE_URL = "https://api.themoviedb.org/3/movie/";
                final String API_KEY_PARAM= "api_key";
                final String LANG_PARAM = "language";
                final String PAGE_PARAM = "page";
                String filterParam = params[0];
                String pageParam = params[1];

                Uri uri = Uri.parse(BASE_URL+filterParam+"?").buildUpon()
                        .appendQueryParameter(API_KEY_PARAM, BuildConfig.MOVIE_POSTER_API_KEY)
                        .appendQueryParameter(LANG_PARAM, languageParam)
                        .appendQueryParameter(PAGE_PARAM, pageParam)
                        .build();

                URL url = new URL(uri.toString());

                Log.d(LOG_TAG, "Built URL " + uri.toString());

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
                Log.d(LOG_TAG, "JSON response " + jsonMoviePostersStr);
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
                if(mCurrentpage == 1)
                    mArrayAdapter.clear();
                mArrayAdapter.addAll(params);
                mArrayAdapter.notifyDataSetChanged();
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
