package com.xdja.splahfragment.splashfragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * <p>Summary:</p>
 * <p>Description:</p>
 * <p>Package:com.xdja.splahfragment.splashfragmenttest</p>
 * <p>Author:yusenkui</p>
 * <p>Date:2017/7/11</p>
 * <p>Time:14:23</p>
 */


public class SplashFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_splash, container, false);
        initView(inflate);
        return inflate;
    }

    private void initView(View inflate) {
        if (inflate != null) {
            ImageView imageView = (ImageView) inflate.findViewById(R.id.image_splash);
            playAnimator(imageView);

        }
    }

    private void playAnimator(ImageView imageView) {
//        if (imageView != null) {
//            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, 0.7f, 0.1f);
//            ObjectAnimator.ofPropertyValuesHolder(imageView, alpha).setDuration(2000).start();
//        }

    }
}
