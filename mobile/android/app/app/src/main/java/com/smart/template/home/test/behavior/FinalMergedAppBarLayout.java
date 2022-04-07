package com.smart.template.home.test.behavior;

import android.content.Context;
import com.google.android.material.appbar.AppBarLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;

import com.smart.template.R;

/**
 * Created by miguel.hincapie on 20/01/2018
 */

public class FinalMergedAppBarLayout extends AppBarLayout {

    protected Toolbar toolbar;
    protected View background;

    public FinalMergedAppBarLayout(Context context) {
        super(context);
        init();
    }

    public FinalMergedAppBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.final_behavior_mergedappbarlayout, this);
        //to avoid expose xml attributes to the final programmer user, I added some of them here
        setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        getContext().setTheme(R.style.FinalBehaviorAppTheme_AppBarOverlay);

        toolbar = findViewById(R.id.expanded_toolbar);
        background = findViewById(R.id.background);
    }
}
