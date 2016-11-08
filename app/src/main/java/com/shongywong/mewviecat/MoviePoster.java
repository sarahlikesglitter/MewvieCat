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
    int mVoteCount;
    double mVoteAverage;

    public MoviePoster(String title, String posterPath, String releaseDate, double popularity,
                       int voteCount, double voteAverage)
    {
        mTitle = title;
        mPosterPath = posterPath;
        mReleaseDate = releaseDate;
        mPopularity = popularity;
        mVoteCount = voteCount;
        mVoteAverage = voteAverage;
    }
}
