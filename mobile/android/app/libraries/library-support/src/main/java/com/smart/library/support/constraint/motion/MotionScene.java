package com.smart.library.support.constraint.motion;

import android.graphics.*;
import android.content.*;
import java.io.*;
import org.xmlpull.v1.*;
import android.content.res.*;
import android.view.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.StateSet;
import com.smart.library.support.constraint.motion.utils.*;
import android.view.animation.*;
import com.smart.library.support.constraint.*;
import java.util.*;
import android.util.*;
import android.view.animation.Interpolator;

public class MotionScene
{
    public static final String TAG = "MotionScene";
    private static final boolean DEBUG = false;
    static final int TRANSITION_BACKWARD = 0;
    static final int TRANSITION_FORWARD = 1;
    private static final int SPLINE_STRING = -1;
    private static final int INTERPOLATOR_REFRENCE_ID = -2;
    public static final int UNSET = -1;
    private final MotionLayout mMotionLayout;
    StateSet mStateSet;
    Transition mCurrentTransition;
    private boolean mDisableAutoTransition;
    private ArrayList<Transition> mTransitionList;
    private SparseArray<ConstraintSet> mConstraintSetMap;
    private HashMap<String, Integer> mConstraintSetIdMap;
    private SparseIntArray mDeriveMap;
    private boolean DEBUG_DESKTOP;
    private int mDefaultDuration;
    private MotionEvent mLastTouchDown;
    private boolean mMotionOutsideRegion;
    private MotionLayout.MotionTracker mVelocityTracker;
    float mLastTouchX;
    float mLastTouchY;
    static final int EASE_IN_OUT = 0;
    static final int EASE_IN = 1;
    static final int EASE_OUT = 2;
    static final int LINEAR = 3;
    static final int ANTICIPATE = 4;
    static final int BOUNCE = 5;
    
    void setTransition(final int beginId, final int endId) {
        int start = beginId;
        int end = endId;
        if (this.mStateSet != null) {
            int tmp = this.mStateSet.stateGetConstraintID(beginId, -1, -1);
            if (tmp != -1) {
                start = tmp;
            }
            tmp = this.mStateSet.stateGetConstraintID(endId, -1, -1);
            if (tmp != -1) {
                end = tmp;
            }
        }
        for (final Transition transition : this.mTransitionList) {
            if ((transition.mConstraintSetEnd == end && transition.mConstraintSetStart == start) || (transition.mConstraintSetEnd == endId && transition.mConstraintSetStart == beginId)) {
                this.mCurrentTransition = transition;
                return;
            }
        }
        final Transition t = new Transition(this);
        t.mConstraintSetStart = start;
        t.mConstraintSetEnd = end;
        t.mDuration = this.mDefaultDuration;
        this.mTransitionList.add(t);
        this.mCurrentTransition = t;
    }
    
    public void addTransition(final Transition transition) {
        final int index = this.getIndex(transition);
        if (index == -1) {
            this.mTransitionList.add(transition);
        }
        else {
            this.mTransitionList.set(index, transition);
        }
    }
    
    public void removeTransition(final Transition transition) {
        final int index = this.getIndex(transition);
        if (index != -1) {
            this.mTransitionList.remove(index);
        }
    }
    
    private int getIndex(final Transition transition) {
        final int id = transition.mId;
        if (id == -1) {
            throw new IllegalArgumentException("The transition must have an id");
        }
        for (int index = 0; index < this.mTransitionList.size(); ++index) {
            if (this.mTransitionList.get(index).mId == id) {
                return index;
            }
        }
        return -1;
    }
    
    public boolean validateLayout(final MotionLayout layout) {
        return layout == this.mMotionLayout && layout.mScene == this;
    }
    
    public void setTransition(final Transition transition) {
        this.mCurrentTransition = transition;
    }
    
    private int getRealID(final int stateid) {
        if (this.mStateSet != null) {
            final int tmp = this.mStateSet.stateGetConstraintID(stateid, -1, -1);
            if (tmp != -1) {
                return tmp;
            }
        }
        return stateid;
    }
    
    public List<Transition> getTransitionsWithState(int stateid) {
        stateid = this.getRealID(stateid);
        final ArrayList<Transition> ret = new ArrayList<Transition>();
        for (final Transition transition : this.mTransitionList) {
            if (transition.mConstraintSetStart == stateid || transition.mConstraintSetEnd == stateid) {
                ret.add(transition);
            }
        }
        return ret;
    }
    
    public void addOnClickListeners(final MotionLayout motionLayout, final int currentState) {
        for (final Transition transition : this.mTransitionList) {
            if (transition.mOnClicks.size() > 0) {
                for (final Transition.TransitionOnClick onClick : transition.mOnClicks) {
                    if (currentState == transition.mConstraintSetStart || currentState == transition.mConstraintSetEnd) {
                        onClick.addOnClickListeners(motionLayout);
                    }
                    else {
                        onClick.removeOnClickListeners(motionLayout);
                    }
                }
            }
        }
    }
    
    public Transition bestTransitionFor(final int currentState, final float dx, final float dy, final MotionEvent mLastTouchDown) {
        List<Transition> candidates = null;
        if (currentState != -1) {
            candidates = this.getTransitionsWithState(currentState);
            float max = 0.0f;
            Transition best = null;
            final RectF cache = new RectF();
            for (final Transition transition : candidates) {
                if (transition.mDisable) {
                    continue;
                }
                if (transition.mTouchResponse == null) {
                    continue;
                }
                final RectF region = transition.mTouchResponse.getTouchRegion(this.mMotionLayout, cache);
                if (region != null && !region.contains(mLastTouchDown.getX(), mLastTouchDown.getY())) {
                    continue;
                }
                float val = transition.mTouchResponse.dot(dx, dy);
                if (transition.mConstraintSetEnd == currentState) {
                    val *= -1.0f;
                }
                if (val <= max) {
                    continue;
                }
                max = val;
                best = transition;
            }
            return best;
        }
        return this.mCurrentTransition;
    }
    
    public ArrayList<Transition> getDefinedTransitions() {
        return this.mTransitionList;
    }
    
    public Transition getTransitionById(final int id) {
        for (final Transition transition : this.mTransitionList) {
            if (transition.mId == id) {
                return transition;
            }
        }
        return null;
    }
    
    public int[] getConstraintSetIds() {
        final int[] ids = new int[this.mConstraintSetMap.size()];
        for (int i = 0; i < ids.length; ++i) {
            ids[i] = this.mConstraintSetMap.keyAt(i);
        }
        return ids;
    }
    
    boolean autoTransition(final MotionLayout motionLayout, final int currentState) {
        if (this.isProcessingTouch()) {
            return false;
        }
        if (this.mDisableAutoTransition) {
            return false;
        }
        for (final Transition transition : this.mTransitionList) {
            if (transition.mConstraintSetStart == 0) {
                continue;
            }
            if (currentState == transition.mConstraintSetStart && (transition.mAutoTransition == 4 || transition.mAutoTransition == 2)) {
                motionLayout.setTransition(transition);
                if (transition.mAutoTransition == 4) {
                    motionLayout.transitionToEnd();
                }
                else {
                    motionLayout.setProgress(1.0f);
                }
                return true;
            }
            if (currentState == transition.mConstraintSetEnd && (transition.mAutoTransition == 3 || transition.mAutoTransition == 1)) {
                motionLayout.setTransition(transition);
                if (transition.mAutoTransition == 3) {
                    motionLayout.transitionToStart();
                }
                else {
                    motionLayout.setProgress(0.0f);
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean isProcessingTouch() {
        return this.mVelocityTracker != null;
    }
    
    public MotionScene(final MotionLayout layout) {
        this.mStateSet = null;
        this.mCurrentTransition = null;
        this.mDisableAutoTransition = false;
        this.mTransitionList = new ArrayList<Transition>();
        this.mConstraintSetMap = (SparseArray<ConstraintSet>)new SparseArray();
        this.mConstraintSetIdMap = new HashMap<String, Integer>();
        this.mDeriveMap = new SparseIntArray();
        this.DEBUG_DESKTOP = false;
        this.mDefaultDuration = 100;
        this.mMotionOutsideRegion = false;
        this.mMotionLayout = layout;
    }
    
    MotionScene(final Context context, final MotionLayout layout, final int resourceID) {
        this.mStateSet = null;
        this.mCurrentTransition = null;
        this.mDisableAutoTransition = false;
        this.mTransitionList = new ArrayList<Transition>();
        this.mConstraintSetMap = (SparseArray<ConstraintSet>)new SparseArray();
        this.mConstraintSetIdMap = new HashMap<String, Integer>();
        this.mDeriveMap = new SparseIntArray();
        this.DEBUG_DESKTOP = false;
        this.mDefaultDuration = 100;
        this.mMotionOutsideRegion = false;
        this.mMotionLayout = layout;
        this.load(context, resourceID);
        this.mConstraintSetMap.put(R.id.motion_base,new ConstraintSet());
        this.mConstraintSetIdMap.put("motion_base", R.id.motion_base);
    }
    
    private void load(final Context context, final int resourceId) {
        final Resources res = context.getResources();
        final XmlPullParser parser = (XmlPullParser)res.getXml(resourceId);
        String document = null;
        String tagName = null;
        try {
            Transition transition = null;
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 0: {
                        document = parser.getName();
                        break;
                    }
                    case 2: {
                        tagName = parser.getName();
                        if (this.DEBUG_DESKTOP) {
                            System.out.println("parsing = " + tagName);
                        }
                        final String s = tagName;
                        switch (s) {
                            case "MotionScene": {
                                this.parseMotionSceneTags(context, parser);
                                break;
                            }
                            case "Transition": {
                                this.mTransitionList.add(transition = new Transition(this, context, parser));
                                if (this.mCurrentTransition == null) {
                                    this.mCurrentTransition = transition;
                                    break;
                                }
                                break;
                            }
                            case "OnSwipe": {
                                if (transition == null) {
                                    final String name = context.getResources().getResourceEntryName(resourceId);
                                    final int line = parser.getLineNumber();
                                    Log.v("MotionScene", " OnSwipe (" + name + ".xml:" + line + ")");
                                }
                                transition.mTouchResponse = new TouchResponse(context, this.mMotionLayout, parser);
                                break;
                            }
                            case "OnClick": {
                                transition.addOnClick(context, parser);
                                break;
                            }
                            case "StateSet": {
                                this.mStateSet = new StateSet(context, parser);
                                break;
                            }
                            case "ConstraintSet": {
                                this.parseConstraintSet(context, parser);
                                break;
                            }
                            case "KeyFrameSet": {
                                final KeyFrames keyFrames = new KeyFrames(context, parser);
                                transition.mKeyFramesList.add(keyFrames);
                                break;
                            }
                            default: {
                                Log.v("MotionScene", "WARNING UNKNOWN ATTRIBUTE " + tagName);
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
    
    private void parseMotionSceneTags(final Context context, final XmlPullParser parser) {
        final AttributeSet attrs = Xml.asAttributeSet(parser);
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MotionScene);
        for (int count = a.getIndexCount(), i = 0; i < count; ++i) {
            final int attr = a.getIndex(i);
            if (attr == R.styleable.MotionScene_defaultDuration) {
                this.mDefaultDuration = a.getInt(attr, this.mDefaultDuration);
            }
        }
        a.recycle();
    }
    
    private int getId(final Context context, final String idString) {
        int id = -1;
        if (idString.contains("/")) {
            final String tmp = idString.substring(idString.indexOf(47) + 1);
            id = context.getResources().getIdentifier(tmp, "id", context.getPackageName());
            if (this.DEBUG_DESKTOP) {
                System.out.println("id getMap res = " + id);
            }
        }
        if (id == -1) {
            if (idString != null && idString.length() > 1) {
                id = Integer.parseInt(idString.substring(1));
            }
            else {
                Log.e("MotionScene", "error in parsing id");
            }
        }
        return id;
    }
    
    private void parseConstraintSet(final Context context, final XmlPullParser parser) {
        final ConstraintSet set = new ConstraintSet();
        set.setForceId(false);
        final int count = parser.getAttributeCount();
        int id = -1;
        int derivedId = -1;
        for (int i = 0; i < count; ++i) {
            final String name = parser.getAttributeName(i);
            final String value = parser.getAttributeValue(i);
            if (this.DEBUG_DESKTOP) {
                System.out.println("id string = " + value);
            }
            final String s = name;
            switch (s) {
                case "id": {
                    id = this.getId(context, value);
                    this.mConstraintSetIdMap.put(stripID(value), id);
                    break;
                }
                case "deriveConstraintsFrom": {
                    derivedId = this.getId(context, value);
                    break;
                }
            }
        }
        if (id != -1) {
            if (this.mMotionLayout.mDebugPath != 0) {
                set.setValidateOnParse(true);
            }
            set.load(context, parser);
            if (derivedId != -1) {
                this.mDeriveMap.put(id, derivedId);
            }
            this.mConstraintSetMap.put(id,set);
        }
    }
    
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
    }
    
    public ConstraintSet getConstraintSet(final Context context, final String id) {
        if (this.DEBUG_DESKTOP) {
            System.out.println("id " + id);
            System.out.println("size " + this.mConstraintSetMap.size());
        }
        for (int i = 0; i < this.mConstraintSetMap.size(); ++i) {
            final int key = this.mConstraintSetMap.keyAt(i);
            final String IdAsString = context.getResources().getResourceName(key);
            if (this.DEBUG_DESKTOP) {
                System.out.println("Id for <" + i + "> is <" + IdAsString + "> looking for <" + id + ">");
            }
            if (id.equals(IdAsString)) {
                return (ConstraintSet)this.mConstraintSetMap.get(key);
            }
        }
        return null;
    }
    
    ConstraintSet getConstraintSet(final int id) {
        return this.getConstraintSet(id, -1, -1);
    }
    
    ConstraintSet getConstraintSet(int id, final int width, final int height) {
        if (this.DEBUG_DESKTOP) {
            System.out.println("id " + id);
            System.out.println("size " + this.mConstraintSetMap.size());
        }
        if (this.mStateSet != null) {
            final int cid = this.mStateSet.stateGetConstraintID(id, width, height);
            if (cid != -1) {
                id = cid;
            }
        }
        if (this.mConstraintSetMap.get(id) == null) {
            return (ConstraintSet)this.mConstraintSetMap.get(this.mConstraintSetMap.keyAt(0));
        }
        return (ConstraintSet)this.mConstraintSetMap.get(id);
    }
    
    public void setConstraintSet(final int id, final ConstraintSet set) {
        this.mConstraintSetMap.put(id,set);
    }
    
    public void getKeyFrames(final MotionController motionController) {
        if (this.mCurrentTransition == null) {
            return;
        }
        for (final KeyFrames keyFrames : this.mCurrentTransition.mKeyFramesList) {
            keyFrames.addFrames(motionController);
        }
    }
    
    Key getKeyFrame(final Context context, final int type, final int target, final int position) {
        if (this.mCurrentTransition == null) {
            return null;
        }
        for (final KeyFrames keyFrames : this.mCurrentTransition.mKeyFramesList) {
            for (final Integer integer : keyFrames.getKeys()) {
                if (target == integer) {
                    final ArrayList<Key> keys = keyFrames.getKeyFramesForView(integer);
                    for (final Key key : keys) {
                        if (key.mFramePosition == position && key.mType == type) {
                            return key;
                        }
                    }
                }
            }
        }
        return null;
    }
    
    int getTransitionDirection(final int stateId) {
        for (final Transition transition : this.mTransitionList) {
            if (transition.mConstraintSetStart == stateId) {
                return 0;
            }
        }
        return 1;
    }
    
    boolean hasKeyFramePosition(final View view, final int position) {
        if (this.mCurrentTransition == null) {
            return false;
        }
        for (final KeyFrames keyFrames : this.mCurrentTransition.mKeyFramesList) {
            final ArrayList<Key> framePoints = keyFrames.getKeyFramesForView(view.getId());
            for (final Key framePoint : framePoints) {
                if (framePoint.mFramePosition == position) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public void setKeyframe(final View view, final int position, final String name, final Object value) {
        if (this.mCurrentTransition == null) {
            return;
        }
        for (final KeyFrames keyFrames : this.mCurrentTransition.mKeyFramesList) {
            final ArrayList<Key> framePoints = keyFrames.getKeyFramesForView(view.getId());
            for (final Key framePoint : framePoints) {
                if (framePoint.mFramePosition == position) {
                    float v = 0.0f;
                    if (value != null) {
                        v = (float)value;
                    }
                    if (v == 0.0f) {
                        v = 0.01f;
                    }
                    if (name.equalsIgnoreCase("app:PerpendicularPath_percent")) {}
                }
            }
        }
    }
    
    public float getPathPercent(final View view, final int position) {
        return 0.0f;
    }
    
    boolean supportTouch() {
        for (final Transition transition : this.mTransitionList) {
            if (transition.mTouchResponse != null) {
                return true;
            }
        }
        return this.mCurrentTransition != null && this.mCurrentTransition.mTouchResponse != null;
    }
    
    void processTouchEvent(final MotionEvent event, final int currentState, final MotionLayout motionLayout) {
        final RectF cache = new RectF();
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = this.mMotionLayout.obtainVelocityTracker();
        }
        this.mVelocityTracker.addMovement(event);
        if (currentState != -1) {
            switch (event.getAction()) {
                case 0: {
                    this.mLastTouchX = event.getRawX();
                    this.mLastTouchY = event.getRawY();
                    this.mLastTouchDown = event;
                    if (this.mCurrentTransition.mTouchResponse != null) {
                        final RectF region = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, cache);
                        if (region != null && !region.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY())) {
                            this.mMotionOutsideRegion = true;
                        }
                        else {
                            this.mMotionOutsideRegion = false;
                        }
                        this.mCurrentTransition.mTouchResponse.setDown(this.mLastTouchX, this.mLastTouchY);
                    }
                    return;
                }
                case 2: {
                    final float dy = event.getRawY() - this.mLastTouchY;
                    final float dx = event.getRawX() - this.mLastTouchX;
                    if (dx == 0.0 && dy == 0.0) {
                        return;
                    }
                    final Transition transition = this.bestTransitionFor(currentState, dx, dy, this.mLastTouchDown);
                    if (transition != null) {
                        motionLayout.setTransition(transition);
                        final RectF region = this.mCurrentTransition.mTouchResponse.getTouchRegion(this.mMotionLayout, cache);
                        this.mMotionOutsideRegion = (region != null && !region.contains(this.mLastTouchDown.getX(), this.mLastTouchDown.getY()));
                        this.mCurrentTransition.mTouchResponse.setUpTouchEvent(this.mLastTouchX, this.mLastTouchY);
                        break;
                    }
                    break;
                }
            }
        }
        if (this.mCurrentTransition != null && this.mCurrentTransition.mTouchResponse != null && !this.mMotionOutsideRegion) {
            this.mCurrentTransition.mTouchResponse.processTouchEvent(event, this.mVelocityTracker, currentState, this);
        }
        this.mLastTouchX = event.getRawX();
        this.mLastTouchY = event.getRawY();
        if (event.getAction() == 1 && this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
            if (motionLayout.mCurrentState != -1) {
                this.autoTransition(motionLayout, motionLayout.mCurrentState);
            }
        }
    }
    
    void processScrollMove(final float dx, final float dy) {
        if (this.mCurrentTransition != null && this.mCurrentTransition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollMove(dx, dy);
        }
    }
    
    void processScrollUp(final float dx, final float dy) {
        if (this.mCurrentTransition != null && this.mCurrentTransition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.scrollUp(dx, dy);
        }
    }
    
    float getProgressDirection(final float dx, final float dy) {
        if (this.mCurrentTransition != null && this.mCurrentTransition.mTouchResponse != null) {
            return this.mCurrentTransition.mTouchResponse.getProgressDirection(dx, dy);
        }
        return 0.0f;
    }
    
    int getStartId() {
        if (this.mCurrentTransition == null) {
            return -1;
        }
        return this.mCurrentTransition.mConstraintSetStart;
    }
    
    int getEndId() {
        if (this.mCurrentTransition == null) {
            return -1;
        }
        return this.mCurrentTransition.mConstraintSetEnd;
    }
    
    public Interpolator getInterpolator() {
        switch (this.mCurrentTransition.mDefaultInterpolator) {
            case -1: {
                final Easing easing = Easing.getInterpolator(this.mCurrentTransition.mDefaultInterpolatorString);
                return (Interpolator)new Interpolator() {
                    public float getInterpolation(final float v) {
                        return (float)easing.get(v);
                    }
                };
            }
            case -2: {
                return AnimationUtils.loadInterpolator(this.mMotionLayout.getContext(), this.mCurrentTransition.mDefaultInterpolatorID);
            }
            case 0: {
                return (Interpolator)new AccelerateDecelerateInterpolator();
            }
            case 1: {
                return (Interpolator)new AccelerateInterpolator();
            }
            case 2: {
                return (Interpolator)new DecelerateInterpolator();
            }
            case 3: {
                return null;
            }
            case 4: {
                return (Interpolator)new AnticipateInterpolator();
            }
            case 5: {
                return (Interpolator)new BounceInterpolator();
            }
            default: {
                return null;
            }
        }
    }
    
    public int getDuration() {
        if (this.mCurrentTransition != null) {
            return this.mCurrentTransition.mDuration;
        }
        return this.mDefaultDuration;
    }
    
    public void setDuration(final int duration) {
        if (this.mCurrentTransition != null) {
            this.mCurrentTransition.setDuration(duration);
        }
        else {
            this.mDefaultDuration = duration;
        }
    }
    
    public float getStaggered() {
        if (this.mCurrentTransition != null) {
            return this.mCurrentTransition.mStagger;
        }
        return 0.0f;
    }
    
    float getMaxAcceleration() {
        if (this.mCurrentTransition != null && this.mCurrentTransition.mTouchResponse != null) {
            return this.mCurrentTransition.mTouchResponse.getMaxAcceleration();
        }
        return 0.0f;
    }
    
    float getMaxVelocity() {
        if (this.mCurrentTransition != null && this.mCurrentTransition.mTouchResponse != null) {
            return this.mCurrentTransition.mTouchResponse.getMaxVelocity();
        }
        return 0.0f;
    }
    
    void setupTouch() {
        if (this.mCurrentTransition != null && this.mCurrentTransition.mTouchResponse != null) {
            this.mCurrentTransition.mTouchResponse.setupTouch();
        }
    }
    
    boolean getMoveWhenScrollAtTop() {
        return this.mCurrentTransition != null && this.mCurrentTransition.mTouchResponse != null && this.mCurrentTransition.mTouchResponse.getMoveWhenScrollAtTop();
    }
    
    void readFallback(final MotionLayout motionLayout) {
        for (int i = 0; i < this.mConstraintSetMap.size(); ++i) {
            this.readConstraintChain(this.mConstraintSetMap.keyAt(i));
        }
        for (int i = 0; i < this.mConstraintSetMap.size(); ++i) {
            final ConstraintSet cs = (ConstraintSet)this.mConstraintSetMap.valueAt(i);
            cs.readFallback(motionLayout);
        }
    }
    
    private void readConstraintChain(final int key) {
        final int derivedFromId = this.mDeriveMap.get(key);
        if (derivedFromId > 0) {
            this.readConstraintChain(this.mDeriveMap.get(key));
            final ConstraintSet cs = (ConstraintSet)this.mConstraintSetMap.get(key);
            final ConstraintSet derivedFrom = (ConstraintSet)this.mConstraintSetMap.get(derivedFromId);
            cs.readFallback(derivedFrom);
            this.mDeriveMap.put(key, -1);
        }
    }
    
    public static String stripID(final String id) {
        if (id == null) {
            return "";
        }
        final int index = id.indexOf(47);
        if (index < 0) {
            return id;
        }
        return id.substring(index + 1);
    }
    
    public int lookUpConstraintId(final String id) {
        return this.mConstraintSetIdMap.get(id);
    }
    
    public String lookUpConstraintName(final int id) {
        for (final Map.Entry<String, Integer> entry : this.mConstraintSetIdMap.entrySet()) {
            if (entry.getValue() == id) {
                return entry.getKey();
            }
        }
        return null;
    }
    
    public void disableAutoTransition(final boolean disable) {
        this.mDisableAutoTransition = disable;
    }
    
    public static class Transition
    {
        private int mId;
        private int mConstraintSetEnd;
        private int mConstraintSetStart;
        private int mDefaultInterpolator;
        private String mDefaultInterpolatorString;
        private int mDefaultInterpolatorID;
        private int mDuration;
        private float mStagger;
        private final MotionScene mMotionScene;
        private ArrayList<KeyFrames> mKeyFramesList;
        private TouchResponse mTouchResponse;
        private ArrayList<TransitionOnClick> mOnClicks;
        private int mAutoTransition;
        public static final int AUTO_NONE = 0;
        public static final int AUTO_JUMP_TO_START = 1;
        public static final int AUTO_JUMP_TO_END = 2;
        public static final int AUTO_ANIMATE_TO_START = 3;
        public static final int AUTO_ANIMATE_TO_END = 4;
        private boolean mDisable;
        
        public void addOnClick(final Context context, final XmlPullParser parser) {
            this.mOnClicks.add(new TransitionOnClick(context, this, parser));
        }
        
        public int getEndConstraintSetId() {
            return this.mConstraintSetEnd;
        }
        
        public int getStartConstraintSetId() {
            return this.mConstraintSetStart;
        }
        
        public void setDuration(final int duration) {
            this.mDuration = duration;
        }
        
        public int getDuration() {
            return this.mDuration;
        }
        
        public float getStagger() {
            return this.mStagger;
        }
        
        public List<KeyFrames> getKeyFrameList() {
            return this.mKeyFramesList;
        }
        
        public List<TransitionOnClick> getOnClickList() {
            return this.mOnClicks;
        }
        
        public TouchResponse getTouchResponse() {
            return this.mTouchResponse;
        }
        
        public void setStagger(final float stagger) {
            this.mStagger = stagger;
        }
        
        public boolean isEnabled() {
            return !this.mDisable;
        }
        
        public void setEnable(final boolean enable) {
            this.mDisable = !enable;
        }
        
        public String debugString(final Context context) {
            String ret;
            if (this.mConstraintSetEnd == -1) {
                ret = "null";
            }
            else {
                ret = context.getResources().getResourceEntryName(this.mConstraintSetStart);
            }
            ret = ret + " -> " + context.getResources().getResourceEntryName(this.mConstraintSetEnd);
            return ret;
        }
        
        Transition(final MotionScene motionScene) {
            this.mId = -1;
            this.mConstraintSetEnd = 0;
            this.mConstraintSetStart = 0;
            this.mDefaultInterpolator = 0;
            this.mDefaultInterpolatorString = null;
            this.mDefaultInterpolatorID = -1;
            this.mDuration = 400;
            this.mStagger = 0.0f;
            this.mKeyFramesList = new ArrayList<KeyFrames>();
            this.mTouchResponse = null;
            this.mOnClicks = new ArrayList<TransitionOnClick>();
            this.mAutoTransition = 0;
            this.mDisable = false;
            this.mMotionScene = motionScene;
        }
        
        public Transition(final int id, final MotionScene motionScene, final int constraintSetStartId, final int constraintSetEndId) {
            this.mId = -1;
            this.mConstraintSetEnd = 0;
            this.mConstraintSetStart = 0;
            this.mDefaultInterpolator = 0;
            this.mDefaultInterpolatorString = null;
            this.mDefaultInterpolatorID = -1;
            this.mDuration = 400;
            this.mStagger = 0.0f;
            this.mKeyFramesList = new ArrayList<KeyFrames>();
            this.mTouchResponse = null;
            this.mOnClicks = new ArrayList<TransitionOnClick>();
            this.mAutoTransition = 0;
            this.mDisable = false;
            this.mId = id;
            this.mMotionScene = motionScene;
            this.mConstraintSetStart = constraintSetStartId;
            this.mConstraintSetEnd = constraintSetEndId;
        }
        
        Transition(final MotionScene motionScene, final Context context, final XmlPullParser parser) {
            this.mId = -1;
            this.mConstraintSetEnd = 0;
            this.mConstraintSetStart = 0;
            this.mDefaultInterpolator = 0;
            this.mDefaultInterpolatorString = null;
            this.mDefaultInterpolatorID = -1;
            this.mDuration = 400;
            this.mStagger = 0.0f;
            this.mKeyFramesList = new ArrayList<KeyFrames>();
            this.mTouchResponse = null;
            this.mOnClicks = new ArrayList<TransitionOnClick>();
            this.mAutoTransition = 0;
            this.mDisable = false;
            this.mDuration = motionScene.mDefaultDuration;
            this.fillFromAttributeList(this.mMotionScene = motionScene, context, Xml.asAttributeSet(parser));
        }
        
        private void fillFromAttributeList(final MotionScene motionScene, final Context context, final AttributeSet attrs) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.Transition);
            this.fill(motionScene, context, a);
            a.recycle();
        }
        
        private void fill(final MotionScene motionScene, final Context context, final TypedArray a) {
            for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.Transition_constraintSetEnd) {
                    this.mConstraintSetEnd = a.getResourceId(attr, this.mConstraintSetEnd);
                    final String type = context.getResources().getResourceTypeName(this.mConstraintSetEnd);
                    if ("layout".equals(type)) {
                        final ConstraintSet cSet = new ConstraintSet();
                        cSet.load(context, this.mConstraintSetEnd);
                        motionScene.mConstraintSetMap.append(this.mConstraintSetEnd,cSet);
                    }
                }
                else if (attr == R.styleable.Transition_constraintSetStart) {
                    this.mConstraintSetStart = a.getResourceId(attr, this.mConstraintSetStart);
                    final String type = context.getResources().getResourceTypeName(this.mConstraintSetStart);
                    if ("layout".equals(type)) {
                        final ConstraintSet cSet = new ConstraintSet();
                        cSet.load(context, this.mConstraintSetStart);
                        motionScene.mConstraintSetMap.append(this.mConstraintSetStart,cSet);
                    }
                }
                else if (attr == R.styleable.Transition_motionInterpolator) {
                    final TypedValue type2 = a.peekValue(attr);
                    if (type2.type == 1) {
                        this.mDefaultInterpolatorID = a.getResourceId(attr, -1);
                        if (this.mDefaultInterpolatorID != -1) {
                            this.mDefaultInterpolator = -2;
                        }
                    }
                    else if (type2.type == 3) {
                        this.mDefaultInterpolatorString = a.getString(attr);
                        if (this.mDefaultInterpolatorString.indexOf("/") > 0) {
                            this.mDefaultInterpolatorID = a.getResourceId(attr, -1);
                            this.mDefaultInterpolator = -2;
                        }
                        else {
                            this.mDefaultInterpolator = -1;
                        }
                    }
                    else {
                        this.mDefaultInterpolator = a.getInteger(attr, this.mDefaultInterpolator);
                    }
                }
                else if (attr == R.styleable.Transition_duration) {
                    this.mDuration = a.getInt(attr, this.mDuration);
                }
                else if (attr == R.styleable.Transition_staggered) {
                    this.mStagger = a.getFloat(attr, this.mStagger);
                }
                else if (attr == R.styleable.Transition_autoTransition) {
                    this.mAutoTransition = a.getInteger(attr, this.mAutoTransition);
                }
                else if (attr == R.styleable.Transition_android_id) {
                    this.mId = a.getResourceId(attr, this.mId);
                }
                else if (attr == R.styleable.Transition_transitionDisable) {
                    this.mDisable = a.getBoolean(attr, this.mDisable);
                }
            }
        }
        
        static class TransitionOnClick implements View.OnClickListener
        {
            private final Transition mTransition;
            int mTargetId;
            int mMode;
            public static final int ANIM_TO_END = 1;
            public static final int ANIM_TOGGLE = 17;
            public static final int ANIM_TO_START = 16;
            public static final int JUMP_TO_END = 256;
            public static final int JUMP_TO_START = 4096;
            
            public TransitionOnClick(final Context context, final Transition transition, final XmlPullParser parser) {
                this.mTargetId = -1;
                this.mMode = 17;
                this.mTransition = transition;
                final TypedArray a = context.obtainStyledAttributes(Xml.asAttributeSet(parser), R.styleable.OnClick);
                for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
                    final int attr = a.getIndex(i);
                    if (attr == R.styleable.OnClick_targetId) {
                        this.mTargetId = a.getResourceId(attr, this.mTargetId);
                    }
                    else if (attr == R.styleable.OnClick_clickAction) {
                        this.mMode = a.getInt(attr, this.mMode);
                    }
                }
                a.recycle();
            }
            
            public void addOnClickListeners(final MotionLayout motionLayout) {
                final View v = (View)((this.mTargetId == -1) ? motionLayout : motionLayout.findViewById(this.mTargetId));
                if (v == null) {
                    Log.e("MotionScene", " (*)  could not find id " + this.mTargetId);
                    return;
                }
                v.setOnClickListener((View.OnClickListener)this);
            }
            
            public void removeOnClickListeners(final MotionLayout motionLayout) {
                final View v = motionLayout.findViewById(this.mTargetId);
                if (v == null) {
                    Log.e("MotionScene", " (*)  could not find id " + this.mTargetId);
                    return;
                }
                v.setOnClickListener((View.OnClickListener)null);
            }
            
            boolean isTransitionViable(final Transition current, final boolean forward, final MotionLayout tl) {
                if (this.mTransition == current) {
                    return true;
                }
                final int dest = forward ? this.mTransition.mConstraintSetEnd : this.mTransition.mConstraintSetStart;
                final int from = forward ? this.mTransition.mConstraintSetStart : this.mTransition.mConstraintSetEnd;
                if (tl.getProgress() == 0.0f) {
                    return tl.mCurrentState == from;
                }
                return tl.getProgress() == 1.0f && tl.mCurrentState == dest;
            }
            
            public void onClick(final View view) {
                final MotionLayout tl = this.mTransition.mMotionScene.mMotionLayout;
                final Transition current = this.mTransition.mMotionScene.mCurrentTransition;
                boolean forward = (this.mMode & 0x1) != 0x0 || (this.mMode & 0x100) != 0x0;
                boolean backward = (this.mMode & 0x10) != 0x0 || (this.mMode & 0x1000) != 0x0;
                final boolean bidirectional = forward && backward;
                if (bidirectional) {
                    if (this.mTransition.mMotionScene.mCurrentTransition != this.mTransition) {
                        tl.setTransition(this.mTransition);
                    }
                    if (tl.getCurrentState() == tl.getEndState() || tl.getProgress() > 0.5f) {
                        forward = false;
                    }
                    else {
                        backward = false;
                    }
                }
                if (forward) {
                    if (this.isTransitionViable(current, true, tl) && (this.mMode & 0x1) != 0x0) {
                        tl.transitionToEnd();
                    }
                    else {
                        tl.setProgress(1.0f);
                    }
                }
                else if (backward) {
                    if (this.isTransitionViable(current, false, tl) && (this.mMode & 0x10) != 0x0) {
                        tl.transitionToStart();
                    }
                    else {
                        tl.setProgress(0.0f);
                    }
                }
            }
        }
    }
}
