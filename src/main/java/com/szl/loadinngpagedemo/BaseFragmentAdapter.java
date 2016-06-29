package com.szl.loadinngpagedemo;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @ explain:
 * @ author：xujun on 2016/4/28 17:34
 * @ email：gdutxiaoxu@163.com
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {

    protected List<Fragment> fragmentList = new ArrayList<Fragment>();

    public BaseFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return isEmpty() ? null : fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return isEmpty() ? 0 : fragmentList.size();
    }

    public boolean isEmpty() {
        return fragmentList == null;

    }
  /*  @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }*/




}
