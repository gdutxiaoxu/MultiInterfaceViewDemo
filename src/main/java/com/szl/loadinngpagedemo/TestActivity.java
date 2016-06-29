package com.szl.loadinngpagedemo;

import android.view.View;

import com.szl.loadinngpagedemo.view.LoadingPage;

public class TestActivity extends BaseActivity {

    private View mSuccessView;

    @Override
    protected View createSuccessView() {
        mSuccessView = View.inflate(mContext, R.layout.activity_test, null);
        return mSuccessView;
    }

    @Override
    protected void initData() {
        show(LoadingPage.LoadResult.loading);
        mLoadingPage.postDelayed(new Runnable() {
            @Override
            public void run() {
                show(LoadingPage.LoadResult.error);
            }
        }, 2000);

        mLoadingPage.postDelayed(new Runnable() {
            @Override
            public void run() {
                show(LoadingPage.LoadResult.empty);
            }
        }, 4000);

        mLoadingPage.postDelayed(new Runnable() {
            @Override
            public void run() {
                show(LoadingPage.LoadResult.success);
            }
        }, 6000);
    }
}
