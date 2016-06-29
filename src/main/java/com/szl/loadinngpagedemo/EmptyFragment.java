package com.szl.loadinngpagedemo;

import android.view.View;

import com.szl.loadinngpagedemo.view.LoadingPage;

/**
 * @ explain:
 * @ author：xujun on 2016-6-29 11:33
 * @ email：gdutxiaoxu@163.com
 */
public class EmptyFragment extends BaseFragment {

    @Override
    public View createSuccessView() {
        return View.inflate(mContext,R.layout.fragemnt_test,null);
    }

    @Override
    protected void initData() {
        mLoadingPage.postDelayed(new Runnable() {
            @Override
            public void run() {
               show(LoadingPage.LoadResult.empty);
            }
        },2500);
    }
}
