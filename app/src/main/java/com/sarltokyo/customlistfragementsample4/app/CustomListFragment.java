package com.sarltokyo.customlistfragementsample4.app;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Created by osabe on 14/08/19.
 */
public class CustomListFragment extends ListFragment
        implements LoaderManager.LoaderCallbacks<List<Entry>>,
        AbsListView.OnScrollListener {

    private CustomAdapter mCustomAdapter;
    private boolean mIsLoading = false;
    private int mCount = 0;
    private ProgressBar mProgressBar;

    private final static String TAG = CustomListFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mProgressBar = (ProgressBar)getView().findViewById(R.id.progressBar);
        mCustomAdapter = new CustomAdapter(getActivity());
        setListAdapter(mCustomAdapter);


        // スクロールリスナーを設定
        getListView().setOnScrollListener(this);

//        setListShown(false);

        mIsLoading = true;

        // プログレスバーを表示する
        mProgressBar.setVisibility(View.VISIBLE);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return new CustomLoader(getActivity(), mCount);
    }

    @Override
    public void onLoadFinished(Loader<List<Entry>> loader, List<Entry> data) {
        mCustomAdapter.setData(data);
        mIsLoading = false;
//        setListShown(true);
        // プログレスバーを消す
        mProgressBar.setVisibility(View.GONE);
        mCount++;
    }

    @Override
    public void onLoaderReset(Loader<List<Entry>> loader) {
        mCustomAdapter.setData(null);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount == firstVisibleItem + visibleItemCount) {
            additionalReading();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void additionalReading() {
        Log.d(TAG, "mIsLoading = " + mIsLoading);
        // 既に読み込み中ならスキップ
        if (mIsLoading) {
            return;
        }
//        setListShown(false);
        // プログレスバーを表示する
        mProgressBar.setVisibility(View.VISIBLE);
        mIsLoading = true;
        getLoaderManager().initLoader(mCount, null, this);
    }
}
