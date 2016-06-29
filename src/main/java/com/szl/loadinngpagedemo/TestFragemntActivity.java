package com.szl.loadinngpagedemo;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.szl.loadinngpagedemo.utils.ScreenUtils;
import com.szl.loadinngpagedemo.view.LoadingPage;

import java.util.ArrayList;

public class TestFragemntActivity extends BaseActivity {

    ViewPager mViewPager;
    private ArrayList<Fragment> mFragments;
    private BaseFragmentAdapter mFragmentAdapter;
    private View mSuccessView;
    private View mLine;
    private ImageButton mBack;

    @Override
    protected View createSuccessView() {
        mSuccessView = View.inflate(mContext, R.layout.activity_test_fragemnt, null);
        mViewPager= (ViewPager) mSuccessView.findViewById(R.id.view_pager);
        mLine = mSuccessView.findViewById(R.id.line);
        mBack= (ImageButton) mSuccessView.findViewById(R.id.back_button);
        return mSuccessView;
    }



    @Override
    protected void initListener() {
        final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams)mLine.getLayoutParams();
        final int childCount=3;
        DisplayMetrics displayMetrics = ScreenUtils.getDisplayMetrics(this);
        final int mScreenWidth=displayMetrics.widthPixels;
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                layoutParams.leftMargin=(int) ((position + positionOffset) * mScreenWidth / childCount);;
                mLine.setLayoutParams(layoutParams);
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
//        调用这个方法显示成功的界面
        show(LoadingPage.LoadResult.success);

        mFragments = new ArrayList<>();
        EmptyFragment emptyFragment = new EmptyFragment();
        mFragments.add(emptyFragment);
        ErrorFragment errorFragment = new ErrorFragment();
        mFragments.add(errorFragment);

        SuccessFragment successFragment = new SuccessFragment();
        mFragments.add(successFragment);

        mFragmentAdapter = new BaseFragmentAdapter(getSupportFragmentManager(), mFragments);
        mViewPager.setOffscreenPageLimit(0);
        mViewPager.setAdapter(mFragmentAdapter);
    }
}
