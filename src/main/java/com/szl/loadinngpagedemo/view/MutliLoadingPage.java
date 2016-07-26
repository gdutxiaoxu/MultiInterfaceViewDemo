package com.szl.loadinngpagedemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.szl.loadinngpagedemo.R;

/***
 * @author xujun
 */
public class MutliLoadingPage extends FrameLayout {

    public static final String TAG = "tag";

    public static final int STATE_UNKOWN = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_ERROR = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_SUCCESS = 4;
    public static final int DEF_ICON_VALUE = -1;
    public int state = STATE_UNKOWN;
    private Context mContext;

    private View loadingView;// 加载中的界面
    private View errorView;// 错误界面
    private View emptyView;// 空界面
    private View successView;// 加载成功的界面

    private String mEmptyText;
    private int mEmptyIConId;
    private String mErrorText;
    private int mErrorIconId;
    private Button mErrorButton;
    private ImageView mErrorImageView;
    private OnClickListener mErrorButtonListener;

    public MutliLoadingPage(Context context) {
        this(context, null, 0);

    }

    public MutliLoadingPage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MutliLoadingPage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        obtainAttrs(attrs);
    }

    private void obtainAttrs(AttributeSet attrs) {
        TypedArray emptyArray = mContext.obtainStyledAttributes(attrs, R.styleable.empty);
        mEmptyText = emptyArray.getString(R.styleable.empty_emptyText);
        mEmptyIConId = emptyArray.getResourceId(R.styleable.empty_emptyIcon, DEF_ICON_VALUE);
        emptyArray.recycle();

        TypedArray errorArray = mContext.obtainStyledAttributes(attrs, R.styleable.error);
        mErrorText = errorArray.getString(R.styleable.error_errorText);
        mErrorIconId = errorArray.getResourceId(R.styleable.error_errorIcon, DEF_ICON_VALUE);
        errorArray.recycle();

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        Log.i(TAG, "MutliLoadingPage.class:onFinishInflate(): 48:" + childCount);
        if (childCount <= 0) {
            throw new IllegalStateException("You must have a Success View");
        }
        if (childCount > 1) {
            throw new IllegalStateException("You must have a child Success View at most");
        }
        successView = getChildAt(0);
        handleSuccessView();

        initdifferentView();
        childCount = getChildCount();
        Log.i(TAG, "MutliLoadingPage.class:onFinishInflate(): 55:" + childCount);
        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                show(LoadResult.success);
            }
        }, 2000);

    }

    public void setErrorButtonListener(OnClickListener errorButtonListener) {
        mErrorButtonListener = errorButtonListener;

    }

    private void initdifferentView() {
        loadingView = createLoadingView(); // 创建了加载中的界面
        if (loadingView != null) {
            this.addView(loadingView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

    }

    // 根据不同的状态显示不同的界面
    private void showPage() {
        handleLoadView();
        handleErrorView();
        handleEmptyView();
        handleSuccessView();

    }

    private void handleLoadView() {
        if (loadingView != null) {
            loadingView.setVisibility(state == STATE_UNKOWN
                    || state == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void handleSuccessView() {
        if (successView != null) {
            successView.setVisibility(state == STATE_SUCCESS ? View.VISIBLE : View.INVISIBLE);
        }
    }

    private void handleEmptyView() {

        if (emptyView == null && state == STATE_EMPTY) {
            emptyView = createEmptyView(); // 加载空的界面
            this.addView(emptyView, new LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        }

        if (state == STATE_EMPTY) {
            emptyView.setVisibility(state == STATE_EMPTY ? View.VISIBLE
                    : View.INVISIBLE);
        }


    }

    /*只有等到状态是state的时候才创建它*/
    private void handleErrorView() {
        if (errorView == null && state == STATE_ERROR) {
            errorView = createErrorView(); // 加载错误界面
            if (errorView != null) {
                this.addView(errorView, new LayoutParams(
                        LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            }


        }
        if (errorView != null) {
            errorView.setVisibility(state == STATE_ERROR ? View.VISIBLE
                    : View.INVISIBLE);
        }

    }

    /* 创建了空的界面 */
    @NonNull
    private View createEmptyView() {
        View view = View.inflate(mContext, R.layout.loadpage_empty,
                null);
        TextView tv = (TextView) view.findViewById(R.id.tv_empty);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_empty);
        if (mEmptyIConId != DEF_ICON_VALUE) {
            iv.setImageResource(mEmptyIConId);
        }
        if (!TextUtils.isEmpty(mEmptyText)) {
            tv.setText(mEmptyText);
        }
        return view;
    }

    /* 创建了错误界面 */
    @NonNull
    private View createErrorView() {
        final View view = View.inflate(mContext, R.layout.loadpage_error,
                null);
        mErrorButton = (Button) view.findViewById(R.id.page_bt);
        mErrorImageView = (ImageView) view.findViewById(R.id.page_iv);
        if (mEmptyIConId != DEF_ICON_VALUE) {
            mErrorImageView.setImageResource(mEmptyIConId);
        }
        if (!TextUtils.isEmpty(mErrorText)) {
            mErrorButton.setText(mEmptyText);
        }

        mErrorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mErrorButtonListener != null) {
                    mErrorButtonListener.onClick(v);
                }
            }
        });
        return view;
    }

    /* 创建加载中的界面 */
    @NonNull
    private View createLoadingView() {
        View view = View.inflate(mContext,
                R.layout.loadpage_loading, null);
        return view;
    }

    /**
     * 注意请求成功后必须调用show方法，会根据这个压面显示相应的数据
     *
     * @param loadResult
     */
    public void show(LoadResult loadResult) {
        state = loadResult.getValue();
        showPage();

    }

    public enum LoadResult {
        loading(1), error(2), empty(3), success(4);

        int value;

        LoadResult(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

    }


}
