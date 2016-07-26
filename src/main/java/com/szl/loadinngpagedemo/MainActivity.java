package com.szl.loadinngpagedemo;

import android.view.View;

import com.szl.loadinngpagedemo.view.LoadingPage;

public class MainActivity extends BaseActivity {

    @Override
    protected View createSuccessView() {
        return View.inflate(mContext,R.layout.activity_main,null);
    }

    @Override
    protected void initData() {
        show(LoadingPage.LoadResult.success);
    }

    public void onButtonClick(View v){
        switch (v.getId()){
            case R.id.btn_avitity:
                readyGo(TestActivity.class);
                break;
            case R.id.btn_fragment:
                readyGo(TestFragemntActivity.class);
                break;
            case R.id.btn_multiLoadPage:
                readyGo(MultiLoadPageActivity.class);
                break;
            default:
                break;
        }
    }
}
