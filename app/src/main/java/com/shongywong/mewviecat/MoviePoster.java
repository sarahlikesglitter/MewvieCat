package com.shongywong.mewviecat;

/**
 * Created by shongywong on 11/8/2016.
 */
public class MoviePoster
{
    String mTitle;
    String mPosterPath;
    String mReleaseDate;
    double mPopularity;
    double mVoteAverage;
    String mSummary;

    public MoviePoster(String title, String posterPath, String releaseDate, double popularity,
                       double voteAverage, String summary)
    {
        mTitle = title;
        mPosterPath = posterPath;
        mReleaseDate = releaseDate;
        mPopularity = popularity;
        mVoteAverage = voteAverage;
        mSummary = summary;
    }
}
