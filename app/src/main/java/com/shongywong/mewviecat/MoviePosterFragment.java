package com.shongywong.mewviecat;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class MoviePosterFragment extends Fragment
{
    private ArrayAdapter<String> mArrayAdapter;

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

        mArrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_movie_poster,
                R.id.list_item_movie_poster_textview,
                new ArrayList<String>()
        );

        ListView listView = (ListView)rootView.findViewById(R.id.listview_movie_poster);
        listView.setAdapter(mArrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long l)
            {
                String text = mArrayAdapter.getItem(index);
                Context context = getActivity();

            }
        });

        return rootView;
    }

    public class FetchMoviePostersTask extends AsyncTask<Void, Void, String[]>
    {
        private final String LOG_TAG = FetchMoviePostersTask.class.getSimpleName();

        @Override
        protected String[] doInBackground(Void... voids)
        {
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;


            return new String[0];
        }
    }

}
