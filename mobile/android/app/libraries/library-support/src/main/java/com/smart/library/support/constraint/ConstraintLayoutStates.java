package com.smart.library.support.constraint;

import android.content.*;
import java.io.*;
import org.xmlpull.v1.*;
import java.util.*;
import android.util.*;
import android.content.res.*;

import com.smart.library.support.R;

public class ConstraintLayoutStates
{
    public static final String TAG = "ConstraintLayoutStates";
    private static final boolean DEBUG = false;
    private final ConstraintLayout mConstraintLayout;
    ConstraintSet mDefaultConstraintSet;
    int mCurrentStateId;
    int mCurrentConstraintNumber;
    private SparseArray<State> mStateList;
    private SparseArray<ConstraintSet> mConstraintSetMap;
    private ConstraintsChangedListener mConstraintsChangedListener;
    
    ConstraintLayoutStates(final Context context, final ConstraintLayout layout, final int resourceID) {
        this.mCurrentStateId = -1;
        this.mCurrentConstraintNumber = -1;
        this.mStateList = (SparseArray<State>)new SparseArray();
        this.mConstraintSetMap = (SparseArray<ConstraintSet>)new SparseArray();
        this.mConstraintsChangedListener = null;
        this.mConstraintLayout = layout;
        this.load(context, resourceID);
    }
    
    public boolean needsToChange(final int id, final float width, final float height) {
        if (this.mCurrentStateId != id) {
            return true;
        }
        final State state = (State)((id == -1) ? this.mStateList.valueAt(0) : ((State)this.mStateList.get(this.mCurrentStateId)));
        return (this.mCurrentConstraintNumber == -1 || !state.mVariants.get(this.mCurrentConstraintNumber).match(width, height)) && this.mCurrentConstraintNumber != state.findMatch(width, height);
    }
    
    public void updateConstraints(final int id, final float width, final float height) {
        if (this.mCurrentStateId == id) {
            State state;
            if (id == -1) {
                state = (State)this.mStateList.valueAt(0);
            }
            else {
                state = (State)this.mStateList.get(this.mCurrentStateId);
            }
            if (this.mCurrentConstraintNumber != -1 && state.mVariants.get(this.mCurrentConstraintNumber).match(width, height)) {
                return;
            }
            final int match = state.findMatch(width, height);
            if (this.mCurrentConstraintNumber == match) {
                return;
            }
            final ConstraintSet constraintSet = (match == -1) ? this.mDefaultConstraintSet : state.mVariants.get(match).mConstraintSet;
            final int cid = (match == -1) ? state.mConstraintID : state.mVariants.get(match).mConstraintID;
            if (constraintSet == null) {
                return;
            }
            this.mCurrentConstraintNumber = match;
            if (this.mConstraintsChangedListener != null) {
                this.mConstraintsChangedListener.preLayoutChange(-1, cid);
            }
            constraintSet.applyTo(this.mConstraintLayout);
            if (this.mConstraintsChangedListener != null) {
                this.mConstraintsChangedListener.postLayoutChange(-1, cid);
            }
        }
        else {
            this.mCurrentStateId = id;
            final State state = (State)this.mStateList.get(this.mCurrentStateId);
            final int match = state.findMatch(width, height);
            final ConstraintSet constraintSet = (match == -1) ? state.mConstraintSet : state.mVariants.get(match).mConstraintSet;
            final int cid = (match == -1) ? state.mConstraintID : state.mVariants.get(match).mConstraintID;
            if (constraintSet == null) {
                Log.v("ConstraintLayoutStates", "NO Constraint set found ! id=" + id + ", dim =" + width + ", " + height);
                return;
            }
            this.mCurrentConstraintNumber = match;
            if (this.mConstraintsChangedListener != null) {
                this.mConstraintsChangedListener.preLayoutChange(id, cid);
            }
            constraintSet.applyTo(this.mConstraintLayout);
            if (this.mConstraintsChangedListener != null) {
                this.mConstraintsChangedListener.postLayoutChange(id, cid);
            }
        }
    }
    
    public void setOnConstraintsChanged(final ConstraintsChangedListener constraintsChangedListener) {
        this.mConstraintsChangedListener = constraintsChangedListener;
    }
    
    private void load(final Context context, final int resourceId) {
        final Resources res = context.getResources();
        final XmlPullParser parser = (XmlPullParser)res.getXml(resourceId);
        String document = null;
        String tagName = null;
        try {
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
                            case "layoutDescription": {
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
                            case "ConstraintSet": {
                                this.parseConstraintSet(context, parser);
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
    
    private void parseConstraintSet(final Context context, final XmlPullParser parser) {
        final ConstraintSet set = new ConstraintSet();
        for (int count = parser.getAttributeCount(), i = 0; i < count; ++i) {
            if ("id".equals(parser.getAttributeName(i))) {
                final String s = parser.getAttributeValue(i);
                int id = -1;
                if (s.contains("/")) {
                    final String tmp = s.substring(s.indexOf(47) + 1);
                    id = context.getResources().getIdentifier(tmp, "id", context.getPackageName());
                }
                if (id == -1) {
                    if (s != null && s.length() > 1) {
                        id = Integer.parseInt(s.substring(1));
                    }
                    else {
                        Log.e("ConstraintLayoutStates", "error in parsing id");
                    }
                }
                set.load(context, parser);
                this.mConstraintSetMap.put(id,set);
                break;
            }
        }
    }
    
    static class State
    {
        int mId;
        ArrayList<Variant> mVariants;
        int mConstraintID;
        ConstraintSet mConstraintSet;
        
        public State(final Context context, final XmlPullParser parser) {
            this.mVariants = new ArrayList<Variant>();
            this.mConstraintID = -1;
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
                        (this.mConstraintSet = new ConstraintSet()).clone(context, this.mConstraintID);
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
        ConstraintSet mConstraintSet;
        
        public Variant(final Context context, final XmlPullParser parser) {
            this.mMinWidth = Float.NaN;
            this.mMinHeight = Float.NaN;
            this.mMaxWidth = Float.NaN;
            this.mMaxHeight = Float.NaN;
            this.mConstraintID = -1;
            final AttributeSet attrs = Xml.asAttributeSet(parser);
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Variant);
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.Variant_constraints) {
                    this.mConstraintID = a.getResourceId(attr, this.mConstraintID);
                    final String type = context.getResources().getResourceTypeName(this.mConstraintID);
                    final String name = context.getResources().getResourceName(this.mConstraintID);
                    if ("layout".equals(type)) {
                        (this.mConstraintSet = new ConstraintSet()).clone(context, this.mConstraintID);
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
