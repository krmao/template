package com.smart.library.widget.shapeable;

/*
 * Copyright 2017, Andrey Makeev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * Handles compound drawable click events.
 * @see TextView#getCompoundDrawables()
 * @see TextView#setCompoundDrawablesRelativeWithIntrinsicBounds(int, int, int, int)
 * @see STCompoundDrawableTouchListener
 */
@SuppressWarnings("WeakerAccess")
public abstract class STCompoundDrawableClickListener extends STCompoundDrawableTouchListener {

    /**
     * Default constructor
     */
    public STCompoundDrawableClickListener() {
        super();
    }

    /**
     * Constructor with fuzz
     * @param fuzz desired fuzz in px
     */
    public STCompoundDrawableClickListener(int fuzz) {
        super(fuzz);
    }

    @Override
    protected boolean onDrawableTouch(View v, int drawableIndex, Rect drawableBounds, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) onDrawableClick(v, drawableIndex);
        return true;
    }

    /**
     * Compound drawable touch-event handler
     * @param v wrapping view
     * @param drawableIndex index of compound drawable which recicved the event
     */
    protected abstract void onDrawableClick(View v, int drawableIndex);
}