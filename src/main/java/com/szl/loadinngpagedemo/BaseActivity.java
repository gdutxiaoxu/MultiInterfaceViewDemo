package com.szl.loadinngpagedemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.szl.loadinngpagedemo.utils.ViewUtils;
import com.szl.loadinngpagedemo.view.LoadingPage;

/**
 * 博客地址：http://blog.csdn.net/gdutxiaoxu
 * @author xujun
 * @time 2016-6-29 14:52.
 */
public abstract class BaseActivity extends AppCompatActivity {


    protected Context mContext;

    protected ProgressDialog dialog;
    protected LoadingPage mLoadingPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initWindows();
        // base setup
        mContext = this;

        if (mLoadingPage == null) {
            mLoadingPage = new LoadingPage(this) {
                @Override
                public View createSuccessView() {
                    return BaseActivity.this.createSuccessView();
                }

            };
        } else {
            ViewUtils.removeParent(mLoadingPage);
        }

        setContentView(mLoadingPage);
        dialog = new ProgressDialog(this);
        initListener();
        initData();
    }

    public void show(LoadingPage.LoadResult loadResult){
        if(mLoadingPage!=null){
            mLoadingPage.show(loadResult);
        }
    }

    protected void initData() {
    }



    /**
     * 创造成功的页面
     *
     * @return
     */
    protected abstract View createSuccessView();

    protected void initListener() {
    }

    public <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            dialog.cancel();
        }

    }

    /**
     * 在setContentView前初始化Window设置
     */
    protected void initWindows() {
    }

    /**
     * 启动Activity
     */
    public void readyGo(Class<? extends Activity> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * 启动Activity并传递数据
     */
    public void readyGo(Class<? extends Activity> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void readyGo(Class<? extends Activity> clazz, String name, Parcelable parcelable) {
        Intent intent = new Intent(this, clazz);
        if (null != parcelable) {
            intent = intent.putExtra(name, parcelable);
        }
        startActivity(intent);
    }

    /**
     * 显示菊花
     */
    public void showProressDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    public void showProgressDialog(boolean cancelable, String msg) {
        if (dialog != null) {
            dialog.setCancelable(cancelable);
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    /**
     * 显示带信息的菊花
     */
    public void showProressDialog(String msg) {
        if (dialog != null) {
            dialog.setMessage(msg);
            dialog.show();
        }
    }

    /**
     * 隐藏菊花
     */
    public void hideProgressDialog() {
        if (dialog != null) {
            dialog.hide();
        }
    }

    /**
     * 根据字符串弹出Toast
     */
    protected void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 根据资源id弹出Toast
     */
    protected void showToast(int msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
