package com.smart.library.map.layer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gyf.barlibrary.ImmersionBar;
import com.smart.library.base.STBaseFragment;
import com.smart.library.map.R;
import com.smart.library.support.constraint.motion.MotionLayout;

import org.jetbrains.annotations.NotNull;

public class STPanelFragment extends STBaseFragment {
    SignleTouchMotionLayout mTouchMotionLayout;
    ImageView mCloseView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ImmersionBar.with(this)
                .transparentStatusBar()
                .statusBarDarkFont(true, 0.2f)
                .init();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.st_panel_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTouchMotionLayout = (SignleTouchMotionLayout) view.findViewById(R.id.bottom_motionlayout);
        mCloseView = (ImageView) view.findViewById(R.id.content_close);
        mCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mTouchMotionLayout.setTransitionListener(new MotionLayout.TransitionListener() {

            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {
                float progress = Math.abs(v);
                if (progress > SignleTouchMotionLayout.PROGRESS_BOTTOM) {
                    mCloseView.setVisibility(View.INVISIBLE);
                } else {
                    mCloseView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });

        setHasMiddle(true);

    }

    public void setHasMiddle(boolean hasMiddle) {
        mTouchMotionLayout.setHasMiddle(hasMiddle);
    }

}
