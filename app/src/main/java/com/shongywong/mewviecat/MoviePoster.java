package com.shongywong.mewviecat;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shongywong on 11/8/2016.
 */
public class MoviePoster implements Parcelable
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

    private MoviePoster(Parcel in)
    {
        mTitle = in.readString();
        mPosterPath = in.readString();
        mReleaseDate = in.readString();
        mPopularity = in.readDouble();
        mVoteAverage = in.readDouble();
        mSummary = in.readString();
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeString(mTitle);
        parcel.writeString(mPosterPath);
        parcel.writeString(mReleaseDate);
        parcel.writeDouble(mPopularity);
        parcel.writeDouble(mVoteAverage);
        parcel.writeString(mSummary);
    }

    public final Parcelable.Creator<MoviePoster> CREATOR = new Parcelable.Creator<MoviePoster>()
    {
        @Override
        public MoviePoster createFromParcel(Parcel parcel)
        {
            return new MoviePoster(parcel);
        }

        @Override
        public MoviePoster[] newArray(int i)
        {
            return new MoviePoster[i];
        }
    };
}
