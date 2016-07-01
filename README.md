# 常用的自定义View例子（MultiInterfaceView多界面处理）

标签（空格分隔）： 自定义View 多界面处理（包括空界面，错误界面，加载中的界面）

---

**最近在做项目的时候，刚开始没有考虑空界面，错误界面的处理，一开始是想为每个界面在布局文件中都天剑一个错误界面，空界面，但仔细一想，这样的工作量太大了，而且也不方便处理，于是我想能不能做出一个自定义控件出来，想了听就，终于做出来了，现在将其分享出来，有什么不足的请各位指点。**


[本文固定链接：](https://www.zybuluo.com/xujun94/note/421494)https://www.zybuluo.com/xujun94/note/421494

[源码下载地址：](https://github.com/gdutxiaoxu/MultiInterfaceViewDemo.git)

[转载请注明原博客地址：](http://blog.csdn.net/gdutxiaoxu/article/details/51784960)

##老规矩，废话不多说，大家先来看一下效果图
* 图一是多个界面的展示的动画图

![](http://ww4.sinaimg.cn/large/9fe4afa0gw1f5c7iu248dg208p0fkwio.gif)

* 图二是空界面的展示图
![](http://7xvjnq.com2.z0.glb.qiniucdn.com/d9mzlams3x6cr0l66pppsn9ffa.png)

* 图三是错误界面的展示图
![](http://7xvjnq.com2.z0.glb.qiniucdn.com/h48mw370oiy91w5ifudnyqpw7m.png)

##大家先来看一下源码
```java
package com.szl.loadinngpagedemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.szl.loadinngpagedemo.R;

/***
 * 创建了自定义帧布局 把baseFragment 一部分代码 抽取到这个类中
 *
 * @author itcast
 */
public abstract class LoadingPage extends FrameLayout {

    public static final int STATE_UNKOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;
    public int state = STATE_UNKOWN;
    private Context mContext;

    private View loadingView;// 加载中的界面
    private View errorView;// 错误界面
    private View emptyView;// 空界面
    private View successView;// 加载成功的界面

    public LoadingPage(Context context) {
        this(context,null,0);

    }
    public LoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init();
    }



    private void init() {
        loadingView = createLoadingView(); // 创建了加载中的界面
        if (loadingView != null) {
            this.addView(loadingView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        errorView = createErrorView(); // 加载错误界面
        if (errorView != null) {
            this.addView(errorView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        emptyView = createEmptyView(); // 加载空的界面
        if (emptyView != null) {
            this.addView(emptyView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        /**
         * 注意这里依赖是就初始化成功的界面，也可以在状态成功的时候才初始化成功的界面
         */
        successView = createSuccessView();
        if(successView!=null){
            this.addView(successView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }
        showPage();// 根据不同的状态显示不同的界面
    }

    // 根据不同的状态显示不同的界面
    private void showPage() {
        if (loadingView != null) {
            loadingView.setVisibility(state == STATE_UNKOWN
                    || state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
        if (errorView != null) {
            errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
        }
        if (emptyView != null) {
            emptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
                    : View.INVISIBLE);
        }

        if (state == STATE_SUCCESS) {
            if (successView == null) {
                successView = createSuccessView();
                this.addView(successView, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }
            successView.setVisibility(View.VISIBLE);
        } else {
            if (successView != null) {
                successView.setVisibility(View.INVISIBLE);
            }
        }
    }

    /* 创建了空的界面 */
    private View createEmptyView() {
        View view = View.inflate(mContext, R.layout.loadpage_empty,
                null);
        return view;
    }

    /* 创建了错误界面 */
    private View createErrorView() {
        View view = View.inflate(mContext, R.layout.loadpage_error,
                null);
        Button page_bt = (Button) view.findViewById(R.id.page_bt);
        page_bt.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
        return view;
    }

    /* 创建加载中的界面 */
    private View createLoadingView() {
        View view = View.inflate(mContext,
                R.layout.loadpage_loading, null);
        return view;
    }

    /**
     * 枚举类，对应相应的状态码，用来表示各种状态
     */
    public enum LoadResult {
        loading(1),error(2), empty(3), success(4);

        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }

    /**
     * 注意请求成功后必须调用show方法，会根据这个压面显示相应的数据
     * @param loadResult
     */
    public void show(LoadResult loadResult) {
        state = loadResult.getValue();
        showPage();

    }

    /***
     * 创建成功的界面
     *
     * @return
     */
    public abstract View createSuccessView();


}

```
##思路解析
1. 首先在构造方法里面调用init（）方法初始化各个界面，包括加载中的界面,错误界面, 空界面,加载成功的界面
```java
   private View loadingView;// 加载中的界面
   private View errorView;// 错误界面
   private View emptyView;// 空界面
   private View successView;// 加载成功的界面

  errorView = createErrorView(); // 加载错误界面

      
   emptyView = createEmptyView(); // 加载空的界面
      
 
   successView = createSuccessView();//加载成功的界面
      
```
 注意在init（）方法里面我们可以知道 createSuccessView()是一个抽象方法，因为每个Activity或Fragment的成功界面一般是变异羊的，我们交给子类自己去实现
2. 接着我们调用showPage（）方法根据不同的状态显示不同的界面，默认显示的状态是STATE_UNKOWN,所以显示的状态是
```java
 public int state = STATE_UNKOWN;

 loadingView.setVisibility(state == STATE_UNKOWN
                    || state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);

 errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
 -------
```

效果图如下：

![](http://7xvjnq.com2.z0.glb.qiniucdn.com/4rocapgqqtuleixvlqudqac434.png)

**当然我们只需调用void show(LoadResult loadResult)这个方法即可根据相应的状态显示相应的界面**

## LoadingPage的源码分析到此为止
----

## 下面我们来看一下我们是怎样结合Fragment来是用的，首先我们抽取一个BaseFragemnt，源码如下
```java
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
```

**BaseFragment的代码很简单，就是将我们的LoadPager这个自定义View设置为根布局，然后创建成功的界面由子类自己去实现**
##下面我们来看一下他的子类ErrorFragment的代码
```java
public class ErrorFragment extends BaseFragment {

    @Override
    public View createSuccessView() {
        return View.inflate(mContext,R.layout.fragemnt_test,null);
    }

    @Override
    protected void initData() {
        mLoadingPage.postDelayed(new Runnable() {
            @Override
            public void run() {
               show(LoadingPage.LoadResult.error);
            }
        },2500);
    }
}
```
**我们可以看到我们所做的就是创建自己的成功界面，如果想显示别的界面，就调用void show(LoadResult loadResult)这个方法，这个我们延时之后调用 show(LoadingPage.LoadResult.error);来显示错误界面**

##看了ErrorFragment的代码以后，我们可以轻易地向导我们的EmptyFragment是怎样操作的，没错就是调用 show(LoadingPage.LoadResult.empty);这个方法而已
```java
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
```
**同理SuccessFragemnt的代码你也能轻易的想象得到，这里就不贴出来了**
##下面我们来看一下BaseActivity的代码,下面只给出主要代码，详细的代码请点击 [源码下载地址：](https://github.com/gdutxiaoxu/MultiInterfaceViewDemo.git)

```java 
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


    /**
     * 创造成功的页面
     *
     * @return
     */
    protected abstract View createSuccessView();

   ----
```



##总结
思路其实很简单，一开始给LoadPager初始化各种布局，包括错误界面，加载中的界面，空界面，其中成功的界面交友子类自己去实现，如果我们想显示别的界面的话，我们只需要调用void show(LoadResult loadResult)这个方法而已
##待改进的地方
1. 由于时间关系，没有给错误界面和空界面统一集成一个自定义控件，这样我们可以利用自定义属性统一处理要显示界面的信息
2. 没有提供更换空界面，错误界面的方法，这个很简单，大家需要的话就自己去实现就好，这里我就实现了，有时间的话会统一处理这些问题，大家有兴趣的话可以关注我github上面仓库的变化。

[源码下载地址：](https://github.com/gdutxiaoxu/MultiInterfaceViewDemo.git)

[转载请注明原博客地址：](http://blog.csdn.net/gdutxiaoxu/article/details/51784960)
