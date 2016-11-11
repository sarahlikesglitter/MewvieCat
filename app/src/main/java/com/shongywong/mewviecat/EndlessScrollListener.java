package com.shongywong.mewviecat;

import android.widget.AbsListView;

/**
 * Created by shongywong on 11/11/2016.
 */
public abstract class EndlessScrollListener implements AbsListView.OnScrollListener
{
    private int mVisibleThreshold = 12;
    private int mCurrentPage = 0;
    private int mPrevTotalItemCount = 0;
    private boolean mLoading = true;
    private int mStartPageIndex = 0;

    public EndlessScrollListener(){}

    public EndlessScrollListener(int visibleThreshold)
    {
        mVisibleThreshold = visibleThreshold;
    }

    public EndlessScrollListener(int visibleThreshold, int startPageIndex)
    {
        mVisibleThreshold = visibleThreshold;
        mStartPageIndex = startPageIndex;
        mCurrentPage = startPageIndex;
    }

    public abstract boolean onLoadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i){}

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleIndex,
                         int visibleItemCount, int totalItemCount)
    {
        if(totalItemCount < mPrevTotalItemCount)
        {
            mCurrentPage = mStartPageIndex;
            mPrevTotalItemCount = totalItemCount;
            if(totalItemCount == 0)
                mLoading = true;
        }

        if(mLoading && (totalItemCount > mPrevTotalItemCount))
        {
            mLoading = false;
            mPrevTotalItemCount = totalItemCount;
            mCurrentPage++;
        }

        if(!mLoading && (firstVisibleIndex + visibleItemCount + mVisibleThreshold) >= totalItemCount)
        {
            mLoading = onLoadMore(mCurrentPage+1, totalItemCount);
        }
    }
}
