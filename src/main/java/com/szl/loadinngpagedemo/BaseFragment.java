package com.szl.loadinngpagedemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.szl.loadinngpagedemo.utils.ViewUtils;
import com.szl.loadinngpagedemo.view.LoadingPage;
import com.szl.loadinngpagedemo.view.LoadingPage.LoadResult;

import java.util.List;

public abstract class BaseFragment extends Fragment {

    protected LoadingPage mLoadingPage;
    Context mContext;
    Activity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext=getActivity();
        mActivity=getActivity();
        if (mLoadingPage == null) {  // 之前的frameLayout 已经记录了一个爹了  爹是之前的ViewPager

            mLoadingPage = new LoadingPage(this.mContext) {

                @Override
                public View createSuccessView() {
                    return BaseFragment.this.createSuccessView();
                }
            };
        } else {
            ViewUtils.removeParent(mLoadingPage);// 移除frameLayout之前的爹
        }
        return mLoadingPage;  //  拿到当前viewPager 添加这个framelayout
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected void initData() {
    }

    protected void initView() {
    }

    /***
     * 创建成功的界面
     *
     * @return
     */
    public abstract View createSuccessView();


    public void show(LoadResult loadResult) {
        if (mLoadingPage != null) {
            mLoadingPage.show(loadResult);
        }
    }

    /**
     * 校验数据
     */
    public LoadResult checkData(List datas) {
        if (datas == null) {
            return LoadResult.error;//  请求服务器失败
        } else {
            if (datas.size() == 0) {
                return LoadingPage.LoadResult.empty;
            } else {
                return LoadingPage.LoadResult.success;
            }
        }

    }


}
