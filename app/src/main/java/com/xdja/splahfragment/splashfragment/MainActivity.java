package com.xdja.splahfragment.splashfragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private ProgressBar progressBar;
    private MyHandler myHandler = new MyHandler(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_main);
        final ViewStub mainLayout = (ViewStub) findViewById(R.id.content_view_stub);
        //1.上来先显示启动页
        final SplashFragment splashFragment = new SplashFragment();
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        if (supportFragmentManager != null) {
            FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.container, splashFragment);
                fragmentTransaction.commit();
            }

        }
        //2.处理主界面的网络操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                long id = Thread.currentThread().getId();
                Log.d(TAG, "id3:" + id);
                SystemClock.sleep(3000);
                myHandler.sendEmptyMessage(0);
            }
        }).start();
        long id = Thread.currentThread().getId();
        Log.d(TAG, "id1:" + id);
        //3.加载主界面布局
        //获取窗体的顶级视图 Android 中基本上每个视图控件都有自己的消息队列或者handler对象，
        // 这些用来更新ui，显然比自己创建ui的方式更好些，因为避免了再次为控件申请内存
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                long id1 = Thread.currentThread().getId();
                Log.d(TAG, "id2:" + id1);
                Log.d(TAG, "getWindow().getDecorView()");
                View inflate = mainLayout.inflate();
                initView(inflate);
            }
        });

        //4.启动页有动画，延迟一下，播放完动画 执行remove
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                myHandler.postDelayed(new DelayRunableImpl(MainActivity.this, splashFragment), 2000);
            }
        });
    }

    private void initView(View inflate) {
        if (inflate != null) {
            progressBar = (ProgressBar) inflate.findViewById(R.id.progress_bar);
            progressBar.setVisibility(View.VISIBLE);
            TextView viewById = (TextView) inflate.findViewById(R.id.text_splash);
            viewById.setText("测试SplashActivity1111");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> weakReference;

        private MyHandler(MainActivity weakReference) {
            this.weakReference = new WeakReference<>(weakReference);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity mainActivity = weakReference.get();
            if (mainActivity != null) {
                mainActivity.progressBar.setVisibility(View.GONE);
            }
        }
    }

    private class DelayRunableImpl implements Runnable {
        WeakReference<Context> contextWeakReference;
        WeakReference<Fragment> fragmentWeakReference;

        private DelayRunableImpl(Context contextWeakReference, Fragment fragmentWeakReference) {
            this.contextWeakReference = new WeakReference<>(contextWeakReference);
            this.fragmentWeakReference = new WeakReference<>(fragmentWeakReference);
        }

        @Override
        public void run() {
            FragmentActivity fragmentActivity = (FragmentActivity) contextWeakReference.get();
            if (fragmentActivity != null) {
                FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
                if (supportFragmentManager != null) {
                    FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
                    SplashFragment splashFragment = (SplashFragment) fragmentWeakReference.get();
                    if (splashFragment != null) {
                        if (isNetworkConnected(MainActivity.this)) {
                            fragmentTransaction.remove(splashFragment);
                            fragmentTransaction.commit();
                        } else {
                            Log.d(TAG, "当前无网络连接");
                        }

                    }
                }

            }
        }
    }

    /**
     * 判断是否有网络连接
     *
     * @param context 调用者上下文句柄
     */

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (myHandler != null) {
            myHandler.removeCallbacksAndMessages(null);
        }
    }
}
