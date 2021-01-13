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
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

/**
 * Handles compound drawable touch events.
 * Will intercept every event that happened inside (calculated) compound drawable bounds, extended by fuzz.
 *
 * @see TextView#getCompoundDrawables()
 * @see TextView#setCompoundDrawablesRelativeWithIntrinsicBounds(int, int, int, int)
 */
@SuppressWarnings("WeakerAccess")
public abstract class STCompoundDrawableTouchListener implements View.OnTouchListener {

    @SuppressWarnings("FieldCanBeLocal")
    private final String LOG_TAG = "CmpDrawableTouch";

    private final int fuzz;

    public static final int LEFT = 0;
    public static final int TOP = 1;
    public static final int RIGHT = 2;
    public static final int BOTTOM = 3;
    private static final int[] DRAWABLE_INDEXES = {LEFT, TOP, RIGHT, BOTTOM};

    /**
     * Default constructor
     */
    public STCompoundDrawableTouchListener() {
        this(0);
    }

    /**
     * Constructor with fuzz
     *
     * @param fuzz desired fuzz in px
     */
    public STCompoundDrawableTouchListener(int fuzz) {
        this.fuzz = fuzz;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (!(view instanceof TextView)) {
            Log.e(LOG_TAG, "attached view is not instance of TextView");
            return false;
        }

        TextView textView = (TextView) view;
        Drawable[] drawables = textView.getCompoundDrawables();
        int x = (int) event.getX();
        int y = (int) event.getY();

        for (int i : DRAWABLE_INDEXES) {
            if (drawables[i] == null) continue;
            Rect bounds = getRelativeBounds(i, drawables[i], textView);
            Rect fuzzedBounds = addFuzz(bounds);

            if (fuzzedBounds.contains(x, y)) {
                MotionEvent relativeEvent = MotionEvent.obtain(
                        event.getDownTime(),
                        event.getEventTime(),
                        event.getAction(),
                        event.getX() - bounds.left,
                        event.getY() - bounds.top,
                        event.getMetaState());
                return onDrawableTouch(view, i, bounds, relativeEvent);
            }
        }

        return false;
    }

    /**
     * Calculates compound drawable bounds relative to wrapping view
     *
     * @param index    compound drawable index
     * @param drawable the drawable
     * @param view     wrapping view
     * @return {@link Rect} with relative bounds
     */
    private Rect getRelativeBounds(int index, @NonNull Drawable drawable, View view) {
        Rect drawableBounds = drawable.getBounds();
        Rect bounds = new Rect(drawableBounds);

        switch (index) {
            case LEFT:
                bounds.offsetTo(view.getPaddingLeft(),
                        view.getHeight() / 2 - bounds.height() / 2);
                break;

            case TOP:
                bounds.offsetTo(view.getWidth() / 2 - bounds.width() / 2,
                        view.getPaddingTop());
                break;

            case RIGHT:
                bounds.offsetTo(view.getWidth() - view.getPaddingRight() - bounds.width(),
                        view.getHeight() / 2 - bounds.height() / 2);
                break;

            case BOTTOM:
                bounds.offsetTo(view.getWidth() / 2 - bounds.width() / 2,
                        view.getHeight() - view.getPaddingBottom() - bounds.height());
                break;
        }

        return bounds;
    }

    /**
     * Expands {@link Rect} by given value in every direction relative to its center
     *
     * @param source given {@link Rect}
     * @return result {@link Rect}
     */
    private Rect addFuzz(Rect source) {
        Rect result = new Rect();
        result.left = source.left - fuzz;
        result.right = source.right + fuzz;
        result.top = source.top - fuzz;
        result.bottom = source.bottom + fuzz;
        return result;
    }

    /**
     * Compound drawable touch-event handler
     *
     * @param v              wrapping view
     * @param drawableIndex  index of compound drawable which recicved the event
     * @param drawableBounds {@link Rect} with compound drawable bounds relative to wrapping view. Fuzz not included
     * @param event          event with coordinated relative to wrapping view - i.e. within {@code drawableBounds}. If using fuzz, may return negative coordinates.
     */
    protected abstract boolean onDrawableTouch(View v, int drawableIndex, Rect drawableBounds, MotionEvent event);
}