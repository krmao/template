package com.smart.library.support.constraint;

import android.content.*;
import org.xmlpull.v1.*;
import java.io.*;
import android.util.*;
import android.content.res.*;
import java.util.*;

import com.smart.library.support.R;

public class StateSet
{
    public static final String TAG = "ConstraintLayoutStates";
    private static final boolean DEBUG = false;
    int mDefaultState;
    ConstraintSet mDefaultConstraintSet;
    int mCurrentStateId;
    int mCurrentConstraintNumber;
    private SparseArray<State> mStateList;
    private SparseArray<ConstraintSet> mConstraintSetMap;
    private ConstraintsChangedListener mConstraintsChangedListener;
    
    public StateSet(final Context context, final XmlPullParser parser) {
        this.mDefaultState = -1;
        this.mCurrentStateId = -1;
        this.mCurrentConstraintNumber = -1;
        this.mStateList = (SparseArray<State>)new SparseArray();
        this.mConstraintSetMap = (SparseArray<ConstraintSet>)new SparseArray();
        this.mConstraintsChangedListener = null;
        this.load(context, parser);
    }
    
    private void load(final Context context, final XmlPullParser parser) {
        final AttributeSet attrs = Xml.asAttributeSet(parser);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateSet);
        for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
            final int attr = a.getIndex(i);
            if (attr == R.styleable.StateSet_defaultState) {
                this.mDefaultState = a.getResourceId(attr, this.mDefaultState);
            }
        }
        String tagName = null;
        try {
            String document = null;
            State state = null;
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 0: {
                        document = parser.getName();
                        break;
                    }
                    case 2: {
                        final String name;
                        tagName = (name = parser.getName());
                        switch (name) {
                            case "LayoutDescription": {
                                break;
                            }
                            case "StateSet": {
                                break;
                            }
                            case "State": {
                                state = new State(context, parser);
                                this.mStateList.put(state.mId, state);
                                break;
                            }
                            case "Variant": {
                                final Variant match = new Variant(context, parser);
                                if (state != null) {
                                    state.add(match);
                                    break;
                                }
                                break;
                            }
                            default: {
                                Log.v("ConstraintLayoutStates", "unknown tag " + tagName);
                                break;
                            }
                        }
                        break;
                    }
                    case 3: {
                        if ("StateSet".equals(parser.getName())) {
                            return;
                        }
                        tagName = null;
                        break;
                    }
                }
            }
        }
        catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
    
    public boolean needsToChange(final int id, final float width, final float height) {
        if (this.mCurrentStateId != id) {
            return true;
        }
        final State state = (State)((id == -1) ? this.mStateList.valueAt(0) : ((State)this.mStateList.get(this.mCurrentStateId)));
        return (this.mCurrentConstraintNumber == -1 || !state.mVariants.get(this.mCurrentConstraintNumber).match(width, height)) && this.mCurrentConstraintNumber != state.findMatch(width, height);
    }
    
    public void setOnConstraintsChanged(final ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
    }
    
    public int stateGetConstraintID(final int id, final int width, final int height) {
        return this.updateConstraints(-1, id, width, height);
    }
    
    public int convertToConstraintSet(final int currentConstrainSettId, final int stateId, final float width, final float height) {
        final State state = (State)this.mStateList.get(stateId);
        if (state == null) {
            return stateId;
        }
        if (width == -1.0f || height == -1.0f) {
            if (state.mConstraintID == currentConstrainSettId) {
                return currentConstrainSettId;
            }
            for (final Variant mVariant : state.mVariants) {
                if (currentConstrainSettId == mVariant.mConstraintID) {
                    return currentConstrainSettId;
                }
            }
            return state.mConstraintID;
        }
        else {
            Variant match = null;
            for (final Variant mVariant2 : state.mVariants) {
                if (mVariant2.match(width, height)) {
                    if (currentConstrainSettId == mVariant2.mConstraintID) {
                        return currentConstrainSettId;
                    }
                    match = mVariant2;
                }
            }
            if (match != null) {
                return match.mConstraintID;
            }
            return state.mConstraintID;
        }
    }
    
    public int updateConstraints(final int currentid, final int id, final float width, final float height) {
        if (currentid == id) {
            State state;
            if (id == -1) {
                state = (State)this.mStateList.valueAt(0);
            }
            else {
                state = (State)this.mStateList.get(this.mCurrentStateId);
            }
            if (state == null) {
                return -1;
            }
            if (this.mCurrentConstraintNumber != -1 && state.mVariants.get(currentid).match(width, height)) {
                return currentid;
            }
            final int match = state.findMatch(width, height);
            if (currentid == match) {
                return currentid;
            }
            return (match == -1) ? state.mConstraintID : state.mVariants.get(match).mConstraintID;
        }
        else {
            final State state = (State)this.mStateList.get(id);
            if (state == null) {
                return -1;
            }
            final int match = state.findMatch(width, height);
            return (match == -1) ? state.mConstraintID : state.mVariants.get(match).mConstraintID;
        }
    }
    
    static class State
    {
        int mId;
        ArrayList<Variant> mVariants;
        int mConstraintID;
        boolean mIsLayout;
        
        public State(final Context context, final XmlPullParser parser) {
            this.mVariants = new ArrayList<Variant>();
            this.mConstraintID = -1;
            this.mIsLayout = false;
            final AttributeSet attrs = Xml.asAttributeSet(parser);
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.State);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.State_android_id) {
                    this.mId = a.getResourceId(attr, this.mId);
                }
                else if (attr == R.styleable.State_constraints) {
                    this.mConstraintID = a.getResourceId(attr, this.mConstraintID);
                    final String type = context.getResources().getResourceTypeName(this.mConstraintID);
                    final String name = context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(type)) {
                        this.mIsLayout = true;
                    }
                }
            }
            a.recycle();
        }
        
        void add(final Variant size) {
            this.mVariants.add(size);
        }
        
        public int findMatch(final float width, final float height) {
            for (int i = 0; i < this.mVariants.size(); ++i) {
                if (this.mVariants.get(i).match(width, height)) {
                    return i;
                }
            }
            return -1;
        }
    }
    
    static class Variant
    {
        int mId;
        float mMinWidth;
        float mMinHeight;
        float mMaxWidth;
        float mMaxHeight;
        int mConstraintID;
        boolean mIsLayout;
        
        public Variant(final Context context, final XmlPullParser parser) {
            this.mMinWidth = Float.NaN;
            this.mMinHeight = Float.NaN;
            this.mMaxWidth = Float.NaN;
            this.mMaxHeight = Float.NaN;
            this.mConstraintID = -1;
            this.mIsLayout = false;
            final AttributeSet attrs = Xml.asAttributeSet(parser);
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Variant);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.Variant_constraints) {
                    this.mConstraintID = a.getResourceId(attr, this.mConstraintID);
                    final String type = context.getResources().getResourceTypeName(this.mConstraintID);
                    final String name = context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(type)) {
                        this.mIsLayout = true;
                    }
                }
                else if (attr == R.styleable.Variant_region_heightLessThan) {
                    this.mMaxHeight = a.getDimension(attr, this.mMaxHeight);
                }
                else if (attr == R.styleable.Variant_region_heightMoreThan) {
                    this.mMinHeight = a.getDimension(attr, this.mMinHeight);
                }
                else if (attr == R.styleable.Variant_region_widthLessThan) {
                    this.mMaxWidth = a.getDimension(attr, this.mMaxWidth);
                }
                else if (attr == R.styleable.Variant_region_widthMoreThan) {
                    this.mMinWidth = a.getDimension(attr, this.mMinWidth);
                }
                else {
                    Log.v("ConstraintLayoutStates", "Unknown tag");
                }
            }
            a.recycle();
        }
        
        boolean match(final float widthDp, final float heightDp) {
            return (Float.isNaN(this.mMinWidth) || widthDp >= this.mMinWidth) && (Float.isNaN(this.mMinHeight) || heightDp >= this.mMinHeight) && (Float.isNaN(this.mMaxWidth) || widthDp <= this.mMaxWidth) && (Float.isNaN(this.mMaxHeight) || heightDp <= this.mMaxHeight);
        }
    }
}
