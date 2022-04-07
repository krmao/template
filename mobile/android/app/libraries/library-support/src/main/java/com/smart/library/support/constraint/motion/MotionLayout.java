package com.smart.library.support.constraint.motion;

import com.smart.library.support.constraint.motion.Debug;
import android.support.v4.view.*;
import android.view.animation.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.motion.utils.*;
import android.content.*;
import android.os.*;
import android.content.res.*;
import java.util.*;
import android.util.*;
import android.view.*;
import com.smart.library.support.constraint.*;
import com.smart.library.support.constraint.solver.widgets.*;
import com.smart.library.support.constraint.solver.widgets.Barrier;
import com.smart.library.support.constraint.solver.widgets.Guideline;
import com.smart.library.support.constraint.solver.widgets.VirtualLayout;

import android.view.animation.Interpolator;
import android.widget.*;
import android.graphics.*;

public class MotionLayout extends ConstraintLayout implements NestedScrollingParent2
{
    public static final int TOUCH_UP_COMPLETE = 0;
    public static final int TOUCH_UP_COMPLETE_TO_START = 1;
    public static final int TOUCH_UP_COMPLETE_TO_END = 2;
    public static final int TOUCH_UP_STOP = 3;
    public static final int TOUCH_UP_DECELERATE = 4;
    public static final int TOUCH_UP_DECELERATE_AND_COMPLETE = 5;
    static final String TAG = "MotionLayout";
    private static final boolean DEBUG = false;
    public static boolean IS_IN_EDIT_MODE;
    private static final boolean PREVENT_SCROLL_CALLS = false;
    MotionScene mScene;
    Interpolator mInterpolator;
    float mLastVelocity;
    private int mBeginState;
    int mCurrentState;
    private int mEndState;
    private int mLastWidthMeasureSpec;
    private int mLastHeightMeasureSpec;
    HashMap<View, MotionController> mFrameArrayList;
    private long mAnimationStartTime;
    private float mTransitionDuration;
    float mTransitionPosition;
    float mTransitionLastPosition;
    private long mTransitionLastTime;
    float mTransitionGoalPosition;
    private boolean mTransitionInstantly;
    boolean mInTransition;
    boolean mIndirectTransition;
    private TransitionListener mListener;
    private float lastPos;
    private float lastY;
    public static final int DEBUG_SHOW_NONE = 0;
    public static final int DEBUG_SHOW_PROGRESS = 1;
    public static final int DEBUG_SHOW_PATH = 2;
    int mDebugPath;
    static final int MAX_KEY_FRAMES = 50;
    DevModeDraw mDevModeDraw;
    private boolean mTemporalInterpolator;
    private StopLogic mStopLogic;
    private DecelerateInterpolator mDecelerateLogic;
    private DesignTool mDesignTool;
    boolean firstDown;
    int mOldWidth;
    int mOldHeight;
    int mLastLayoutWidth;
    int mLastLayoutHeight;
    View mScrollTarget;
    float mScrollTargetDX;
    float mScrollTargetDY;
    long mScrollTargetTime;
    float mScrollTargetDT;
    private boolean mKeepAnimating;
    private ArrayList<MotionHelper> mOnShowHelpers;
    private ArrayList<MotionHelper> mOnHideHelpers;
    private int mFrames;
    private long mLastDrawTime;
    private float mLastFps;
    private int mListenerState;
    private float mListenerPosition;
    public static final int VELOCITY_POST_LAYOUT = 0;
    public static final int VELOCITY_LAYOUT = 1;
    public static final int VELOCITY_STATIC_POST_LAYOUT = 2;
    public static final int VELOCITY_STATIC_LAYOUT = 3;
    int mStartWrapWidth;
    int mStartWrapHeight;
    int mEndWrapWidth;
    int mEndWrapHeight;
    int mWidthMeasureMode;
    int mHeightMeasureMode;
    float mPostInterpolationPosition;
    private KeyCache mKeyCache;
    Model mModel;
    private boolean mNeedsFireTransitionCompleted;
    private RectF mBoundsCheck;
    private View mRegionView;
    ArrayList<Integer> mTransitionCompleted;
    
    public MotionLayout(final Context context) {
        super(context);
        this.mLastVelocity = 0.0f;
        this.mBeginState = -1;
        this.mCurrentState = -1;
        this.mEndState = -1;
        this.mLastWidthMeasureSpec = 0;
        this.mLastHeightMeasureSpec = 0;
        this.mFrameArrayList = new HashMap<View, MotionController>();
        this.mAnimationStartTime = 0L;
        this.mTransitionDuration = 1.0f;
        this.mTransitionPosition = 0.0f;
        this.mTransitionLastPosition = 0.0f;
        this.mTransitionGoalPosition = 0.0f;
        this.mInTransition = false;
        this.mIndirectTransition = false;
        this.mDebugPath = 0;
        this.mTemporalInterpolator = false;
        this.mStopLogic = new StopLogic();
        this.mDecelerateLogic = new DecelerateInterpolator();
        this.firstDown = true;
        this.mKeepAnimating = false;
        this.mOnShowHelpers = null;
        this.mOnHideHelpers = null;
        this.mFrames = 0;
        this.mLastDrawTime = -1L;
        this.mLastFps = 0.0f;
        this.mListenerState = 0;
        this.mListenerPosition = 0.0f;
        this.mKeyCache = new KeyCache();
        this.mModel = new Model();
        this.mNeedsFireTransitionCompleted = false;
        this.mBoundsCheck = new RectF();
        this.mRegionView = null;
        this.mTransitionCompleted = new ArrayList<Integer>();
        this.init(null);
    }
    
    public MotionLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        this.mLastVelocity = 0.0f;
        this.mBeginState = -1;
        this.mCurrentState = -1;
        this.mEndState = -1;
        this.mLastWidthMeasureSpec = 0;
        this.mLastHeightMeasureSpec = 0;
        this.mFrameArrayList = new HashMap<View, MotionController>();
        this.mAnimationStartTime = 0L;
        this.mTransitionDuration = 1.0f;
        this.mTransitionPosition = 0.0f;
        this.mTransitionLastPosition = 0.0f;
        this.mTransitionGoalPosition = 0.0f;
        this.mInTransition = false;
        this.mIndirectTransition = false;
        this.mDebugPath = 0;
        this.mTemporalInterpolator = false;
        this.mStopLogic = new StopLogic();
        this.mDecelerateLogic = new DecelerateInterpolator();
        this.firstDown = true;
        this.mKeepAnimating = false;
        this.mOnShowHelpers = null;
        this.mOnHideHelpers = null;
        this.mFrames = 0;
        this.mLastDrawTime = -1L;
        this.mLastFps = 0.0f;
        this.mListenerState = 0;
        this.mListenerPosition = 0.0f;
        this.mKeyCache = new KeyCache();
        this.mModel = new Model();
        this.mNeedsFireTransitionCompleted = false;
        this.mBoundsCheck = new RectF();
        this.mRegionView = null;
        this.mTransitionCompleted = new ArrayList<Integer>();
        this.init(attrs);
    }
    
    public MotionLayout(final Context context, final AttributeSet attrs, final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mLastVelocity = 0.0f;
        this.mBeginState = -1;
        this.mCurrentState = -1;
        this.mEndState = -1;
        this.mLastWidthMeasureSpec = 0;
        this.mLastHeightMeasureSpec = 0;
        this.mFrameArrayList = new HashMap<View, MotionController>();
        this.mAnimationStartTime = 0L;
        this.mTransitionDuration = 1.0f;
        this.mTransitionPosition = 0.0f;
        this.mTransitionLastPosition = 0.0f;
        this.mTransitionGoalPosition = 0.0f;
        this.mInTransition = false;
        this.mIndirectTransition = false;
        this.mDebugPath = 0;
        this.mTemporalInterpolator = false;
        this.mStopLogic = new StopLogic();
        this.mDecelerateLogic = new DecelerateInterpolator();
        this.firstDown = true;
        this.mKeepAnimating = false;
        this.mOnShowHelpers = null;
        this.mOnHideHelpers = null;
        this.mFrames = 0;
        this.mLastDrawTime = -1L;
        this.mLastFps = 0.0f;
        this.mListenerState = 0;
        this.mListenerPosition = 0.0f;
        this.mKeyCache = new KeyCache();
        this.mModel = new Model();
        this.mNeedsFireTransitionCompleted = false;
        this.mBoundsCheck = new RectF();
        this.mRegionView = null;
        this.mTransitionCompleted = new ArrayList<Integer>();
        this.init(attrs);
    }
    
    protected long getNanoTime() {
        return System.nanoTime();
    }
    
    protected MotionTracker obtainVelocityTracker() {
        return MyTracker.obtain();
    }
    
    public void setTransition(final int beginId, final int endId) {
        if (this.mScene != null) {
            this.mBeginState = beginId;
            this.mEndState = endId;
            this.mScene.setTransition(beginId, endId);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(beginId), this.mScene.getConstraintSet(endId));
            this.rebuildScene();
            this.mTransitionLastPosition = 0.0f;
            this.transitionToStart();
        }
    }
    
    protected void setTransition(final MotionScene.Transition transition) {
        this.mScene.setTransition(transition);
        if (this.mCurrentState == this.mScene.getEndId()) {
            this.mTransitionLastPosition = 1.0f;
            this.mTransitionPosition = 1.0f;
            this.mTransitionGoalPosition = 1.0f;
        }
        else {
            this.mTransitionLastPosition = 0.0f;
            this.mTransitionPosition = 0.0f;
            this.mTransitionGoalPosition = 0.0f;
        }
        this.mTransitionLastTime = this.getNanoTime();
        final int newBeginState = this.mScene.getStartId();
        final int newEndState = this.mScene.getEndId();
        if (newBeginState == this.mBeginState && newEndState == this.mEndState) {
            return;
        }
        this.mBeginState = newBeginState;
        this.mEndState = newEndState;
        this.mScene.setTransition(this.mBeginState, this.mEndState);
        this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
        this.mModel.setMeasuredId(this.mBeginState, this.mEndState);
        this.mModel.reEvaluateState();
        this.rebuildScene();
        if (this.mListener != null) {
            this.mListener.onTransitionStarted(this, this.mBeginState, this.mEndState);
        }
    }
    
    @Override
    public void loadLayoutDescription(final int motionScene) {
        if (motionScene != 0) {
            try {
                this.mScene = new MotionScene(this.getContext(), this, motionScene);
                if (Build.VERSION.SDK_INT < 19 || this.isAttachedToWindow()) {
                    this.mScene.readFallback(this);
                    this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
                    this.rebuildScene();
                }
                return;
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("unable to parse MotionScene file", ex);
            }
        }
        this.mScene = null;
    }
    
    @Override
    public void setState(final int id, final int screenWidth, final int screenHeight) {
        this.mCurrentState = id;
        this.mBeginState = -1;
        this.mEndState = -1;
        if (this.mConstraintLayoutSpec != null) {
            this.mConstraintLayoutSpec.updateConstraints(id, screenWidth, screenHeight);
        }
        else if (this.mScene != null) {
            this.mScene.getConstraintSet(id).applyTo(this);
        }
    }
    
    public void setInterpolatedProgress(final float pos) {
        if (this.mScene != null) {
            final Interpolator interpolator = this.mScene.getInterpolator();
            if (interpolator != null) {
                this.setProgress(interpolator.getInterpolation(pos));
                return;
            }
        }
        this.setProgress(pos);
    }
    
    public void setProgress(final float pos) {
        if (pos <= 0.0f) {
            this.mCurrentState = this.mBeginState;
        }
        else if (pos >= 1.0f) {
            this.mCurrentState = this.mEndState;
        }
        else {
            this.mCurrentState = -1;
        }
        if (this.mScene == null) {
            return;
        }
        this.mTransitionInstantly = true;
        this.mTransitionGoalPosition = pos;
        this.mTransitionPosition = pos;
        this.mTransitionLastTime = this.getNanoTime();
        this.mAnimationStartTime = -1L;
        this.mInterpolator = null;
        this.mInTransition = true;
        this.invalidate();
    }
    
    private void setupMotionViews() {
        final int n = this.getChildCount();
        this.mModel.build();
        this.mInTransition = true;
        final int layoutWidth = this.getWidth();
        final int layoutHeight = this.getHeight();
        for (int i = 0; i < n; ++i) {
            final MotionController motionController = this.mFrameArrayList.get(this.getChildAt(i));
            if (motionController != null) {
                this.mScene.getKeyFrames(motionController);
                motionController.setup(layoutWidth, layoutHeight, this.mTransitionDuration, this.getNanoTime());
            }
        }
        float stagger = this.mScene.getStaggered();
        if (stagger != 0.0f) {
            final boolean flip = stagger < 0.0;
            boolean useMotionStagger = false;
            stagger = Math.abs(stagger);
            float min = Float.MAX_VALUE;
            float max = -3.4028235E38f;
            for (int j = 0; j < n; ++j) {
                final MotionController f = this.mFrameArrayList.get(this.getChildAt(j));
                if (!Float.isNaN(f.mMotionStagger)) {
                    useMotionStagger = true;
                    break;
                }
                final float x = f.getFinalX();
                final float y = f.getFinalY();
                final float mdist = flip ? (y - x) : (y + x);
                min = Math.min(min, mdist);
                max = Math.max(max, mdist);
            }
            if (useMotionStagger) {
                min = Float.MAX_VALUE;
                max = -3.4028235E38f;
                for (int j = 0; j < n; ++j) {
                    final MotionController f = this.mFrameArrayList.get(this.getChildAt(j));
                    if (!Float.isNaN(f.mMotionStagger)) {
                        min = Math.min(min, f.mMotionStagger);
                        max = Math.max(max, f.mMotionStagger);
                    }
                }
                for (int j = 0; j < n; ++j) {
                    final MotionController f = this.mFrameArrayList.get(this.getChildAt(j));
                    if (!Float.isNaN(f.mMotionStagger)) {
                        f.mStaggerScale = 1.0f / (1.0f - stagger);
                        if (flip) {
                            f.mStaggerOffset = stagger - stagger * ((max - f.mMotionStagger) / (max - min));
                        }
                        else {
                            f.mStaggerOffset = stagger - stagger * (f.mMotionStagger - min) / (max - min);
                        }
                    }
                }
            }
            else {
                for (int j = 0; j < n; ++j) {
                    final MotionController f = this.mFrameArrayList.get(this.getChildAt(j));
                    final float x = f.getFinalX();
                    final float y = f.getFinalY();
                    final float mdist = flip ? (y - x) : (y + x);
                    f.mStaggerScale = 1.0f / (1.0f - stagger);
                    f.mStaggerOffset = stagger - stagger * (mdist - min) / (max - min);
                }
            }
        }
    }
    
    public void touchAnimateTo(final int touchUpMode, float position, final float currentVelocity) {
        if (this.mScene == null) {
            return;
        }
        if (this.mTransitionLastPosition == position) {
            return;
        }
        this.mTemporalInterpolator = true;
        this.mAnimationStartTime = this.getNanoTime();
        this.mTransitionDuration = this.mScene.getDuration() / 1000.0f;
        this.mTransitionGoalPosition = position;
        this.mInTransition = true;
        switch (touchUpMode) {
            case 0:
            case 1:
            case 2: {
                if (touchUpMode == 1) {
                    position = 0.0f;
                }
                else if (touchUpMode == 2) {
                    position = 1.0f;
                }
                this.mStopLogic.config(this.mTransitionLastPosition, position, currentVelocity, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
                final int currentState = this.mCurrentState;
                this.setProgress((position == 0.0f) ? 1.0f : 0.0f);
                this.mCurrentState = currentState;
                this.mInterpolator = (Interpolator)this.mStopLogic;
            }
            case 4: {
                this.mDecelerateLogic.config(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
                this.mInterpolator = (Interpolator)this.mDecelerateLogic;
                break;
            }
            case 5: {
                if (willJump(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration())) {
                    this.mDecelerateLogic.config(currentVelocity, this.mTransitionLastPosition, this.mScene.getMaxAcceleration());
                    this.mInterpolator = (Interpolator)this.mDecelerateLogic;
                    break;
                }
                this.mStopLogic.config(this.mTransitionLastPosition, position, currentVelocity, this.mTransitionDuration, this.mScene.getMaxAcceleration(), this.mScene.getMaxVelocity());
                this.mLastVelocity = 0.0f;
                final int currentState = this.mCurrentState;
                this.setProgress((position == 0.0f) ? 1.0f : 0.0f);
                this.mCurrentState = currentState;
                this.mInterpolator = (Interpolator)this.mStopLogic;
                break;
            }
        }
        this.mTransitionInstantly = false;
        this.mAnimationStartTime = this.getNanoTime();
        this.invalidate();
    }
    
    private static boolean willJump(final float velocity, final float position, final float maxAcceleration) {
        if (velocity > 0.0f) {
            final float time = velocity / maxAcceleration;
            final float pos = velocity * time - maxAcceleration * time * time / 2.0f;
            return position + pos > 1.0f;
        }
        final float time = -velocity / maxAcceleration;
        final float pos = velocity * time + maxAcceleration * time * time / 2.0f;
        return position + pos < 0.0f;
    }
    
    void animateTo(final float position) {
        if (this.mScene == null) {
            return;
        }
        if (this.mTransitionLastPosition != this.mTransitionPosition && this.mTransitionInstantly) {
            this.mTransitionLastPosition = this.mTransitionPosition;
        }
        if (this.mTransitionLastPosition == position) {
            return;
        }
        this.mTemporalInterpolator = false;
        final float currentPosition = this.mTransitionLastPosition;
        this.mTransitionGoalPosition = position;
        this.mTransitionDuration = this.mScene.getDuration() / 1000.0f;
        this.setProgress(this.mTransitionGoalPosition);
        this.mInterpolator = this.mScene.getInterpolator();
        this.mTransitionInstantly = false;
        this.mAnimationStartTime = this.getNanoTime();
        this.mInTransition = true;
        this.mTransitionPosition = currentPosition;
        this.mTransitionLastPosition = currentPosition;
        this.invalidate();
    }
    
    private void computeCurrentPositions() {
        for (int n = this.getChildCount(), i = 0; i < n; ++i) {
            final View v = this.getChildAt(i);
            final MotionController frame = this.mFrameArrayList.get(v);
            if (frame != null) {
                frame.setStartCurrentState(v);
            }
        }
    }
    
    public void transitionToStart() {
        this.animateTo(0.0f);
    }
    
    public void transitionToEnd() {
        this.animateTo(1.0f);
    }
    
    public void transitionToState(final int id) {
        this.transitionToState(id, -1, -1);
    }
    
    public void transitionToState(int id, final int screenWidth, final int screenHeight) {
        if (this.mScene != null && this.mScene.mStateSet != null) {
            final int tmp_id = this.mScene.mStateSet.convertToConstraintSet(this.mCurrentState, id, screenWidth, screenHeight);
            if (tmp_id != -1) {
                id = tmp_id;
            }
        }
        if (this.mCurrentState == id) {
            return;
        }
        if (this.mBeginState == id) {
            this.animateTo(0.0f);
            return;
        }
        if (this.mEndState == id) {
            this.animateTo(1.0f);
            return;
        }
        this.mEndState = id;
        if (this.mCurrentState != -1) {
            this.setTransition(this.mCurrentState, id);
            this.animateTo(1.0f);
            this.mTransitionLastPosition = 0.0f;
            this.transitionToEnd();
            return;
        }
        this.mTemporalInterpolator = false;
        this.mTransitionGoalPosition = 1.0f;
        this.mTransitionPosition = 0.0f;
        this.mTransitionLastPosition = 0.0f;
        this.mTransitionLastTime = this.getNanoTime();
        this.mAnimationStartTime = this.getNanoTime();
        this.mTransitionInstantly = false;
        this.mInterpolator = null;
        this.mTransitionDuration = this.mScene.getDuration() / 1000.0f;
        final int startId = this.mScene.getStartId();
        final int targetID = id;
        final int n = this.getChildCount();
        this.mFrameArrayList.clear();
        for (int i = 0; i < n; ++i) {
            final View v = this.getChildAt(i);
            final MotionController f = new MotionController(v);
            this.mFrameArrayList.put(v, f);
        }
        this.mInTransition = true;
        this.mModel.initFrom(this.mLayoutWidget, null, this.mScene.getConstraintSet(id));
        this.rebuildScene();
        this.mModel.build();
        this.computeCurrentPositions();
        final int layoutWidth = this.getWidth();
        final int layoutHeight = this.getHeight();
        for (int j = 0; j < n; ++j) {
            final MotionController motionController = this.mFrameArrayList.get(this.getChildAt(j));
            this.mScene.getKeyFrames(motionController);
            motionController.setup(layoutWidth, layoutHeight, this.mTransitionDuration, this.getNanoTime());
        }
        final float stagger = this.mScene.getStaggered();
        if (stagger != 0.0f) {
            float min = Float.MAX_VALUE;
            float max = -3.4028235E38f;
            for (int k = 0; k < n; ++k) {
                final MotionController f2 = this.mFrameArrayList.get(this.getChildAt(k));
                final float x = f2.getFinalX();
                final float y = f2.getFinalY();
                min = Math.min(min, y + x);
                max = Math.max(max, y + x);
            }
            for (int k = 0; k < n; ++k) {
                final MotionController f2 = this.mFrameArrayList.get(this.getChildAt(k));
                final float x = f2.getFinalX();
                final float y = f2.getFinalY();
                f2.mStaggerScale = 1.0f / (1.0f - stagger);
                f2.mStaggerOffset = stagger - stagger * (x + y - min) / (max - min);
            }
        }
        this.mTransitionPosition = 0.0f;
        this.mTransitionLastPosition = 0.0f;
        this.mInTransition = true;
        this.invalidate();
    }
    
    public float getVelocity() {
        if (this.mInterpolator == null) {
            return this.mLastVelocity;
        }
        if (this.mInterpolator instanceof MotionInterpolator) {
            return ((MotionInterpolator)this.mInterpolator).getVelocity();
        }
        return 0.0f;
    }
    
    public void getViewVelocity(final View view, final float posOnViewX, final float posOnViewY, final float[] returnVelocity, final int type) {
        float v = this.mLastVelocity;
        float position = this.mTransitionLastPosition;
        if (this.mInterpolator != null) {
            final float deltaT = 1.0E-5f;
            final float dir = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
            float interpos = this.mInterpolator.getInterpolation(this.mTransitionLastPosition + deltaT);
            position = this.mInterpolator.getInterpolation(this.mTransitionLastPosition);
            interpos -= position;
            interpos /= deltaT;
            v = dir * interpos / this.mTransitionDuration;
        }
        if (this.mInterpolator instanceof MotionInterpolator) {
            v = ((MotionInterpolator)this.mInterpolator).getVelocity();
        }
        final MotionController f = this.mFrameArrayList.get(view);
        if ((type & 0x1) == 0x0) {
            f.getPostLayoutDvDp(position, view.getWidth(), view.getHeight(), posOnViewX, posOnViewY, returnVelocity);
        }
        else {
            f.getDpDt(position, posOnViewX, posOnViewY, returnVelocity);
        }
        if (type < 2) {
            final int n = 0;
            returnVelocity[n] *= v;
            final int n2 = 1;
            returnVelocity[n2] *= v;
        }
    }
    
    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        if (this.mScene == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        boolean recalc = this.mLastWidthMeasureSpec != widthMeasureSpec || this.mLastHeightMeasureSpec != heightMeasureSpec;
        if (this.mNeedsFireTransitionCompleted) {
            this.mNeedsFireTransitionCompleted = false;
            this.onNewStateAttachHandlers();
            this.processTransitionCompleted();
            recalc = true;
        }
        if (this.mDirtyHierarchy) {
            recalc = true;
        }
        this.mLastWidthMeasureSpec = widthMeasureSpec;
        this.mLastHeightMeasureSpec = heightMeasureSpec;
        final int startId = this.mScene.getStartId();
        final int endId = this.mScene.getEndId();
        if (recalc || this.mModel.isNotConfiguredWith(startId, endId)) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(startId), this.mScene.getConstraintSet(endId));
            this.mModel.reEvaluateState();
            this.mModel.setMeasuredId(startId, endId);
        }
        else {
            final int heightPadding = this.getPaddingTop() + this.getPaddingBottom();
            final int widthPadding = this.getPaddingLeft() + this.getPaddingRight();
            int androidLayoutWidth = this.mLayoutWidget.getWidth() + widthPadding;
            int androidLayoutHeight = this.mLayoutWidget.getHeight() + heightPadding;
            if (this.mWidthMeasureMode == Integer.MIN_VALUE) {
                androidLayoutWidth = (int)(this.mStartWrapWidth + this.mPostInterpolationPosition * (this.mEndWrapWidth - this.mStartWrapWidth));
                this.requestLayout();
            }
            if (this.mHeightMeasureMode == Integer.MIN_VALUE) {
                androidLayoutHeight = (int)(this.mStartWrapHeight + this.mPostInterpolationPosition * (this.mEndWrapHeight - this.mStartWrapHeight));
                this.requestLayout();
            }
            this.setMeasuredDimension(androidLayoutWidth, androidLayoutHeight);
        }
        this.evaluateLayout();
    }
    
    public boolean onStartNestedScroll(final View child, final View target, final int axes, final int type) {
        this.mScrollTarget = target;
        return true;
    }
    
    public void onNestedScrollAccepted(final View child, final View target, final int axes, final int type) {
    }
    
    public void onStopNestedScroll(final View target, final int type) {
        if (this.mScene == null) {
            return;
        }
        this.mScene.processScrollUp(this.mScrollTargetDX / this.mScrollTargetDT, this.mScrollTargetDY / this.mScrollTargetDT);
    }
    
    public void onNestedScroll(final View target, final int dxConsumed, final int dyConsumed, final int dxUnconsumed, final int dyUnconsumed, final int type) {
    }
    
    public void onNestedPreScroll(final View target, final int dx, final int dy, final int[] consumed, final int type) {
        if (this.mScene == null) {
            return;
        }
        final MotionScene.Transition currentTransition = this.mScene.mCurrentTransition;
        if (currentTransition != null && currentTransition.isEnabled()) {
            final TouchResponse touchResponse = currentTransition.getTouchResponse();
            if (touchResponse != null) {
                final int regionId = touchResponse.getTouchRegionId();
                if (regionId != -1 && this.mScrollTarget.getId() != regionId) {
                    return;
                }
            }
        }
        if (this.mScene != null && this.mScene.getMoveWhenScrollAtTop() && this.mTransitionPosition != 1.0f && target.canScrollVertically(-1)) {
            return;
        }
        final float progress = this.mTransitionPosition;
        final long time = this.getNanoTime();
        this.mScrollTargetDX = dx;
        this.mScrollTargetDY = dy;
        this.mScrollTargetDT = (float)((time - this.mScrollTargetTime) * 1.0E-9);
        this.mScrollTargetTime = time;
        this.mScene.processScrollMove(dx, dy);
        if (progress != this.mTransitionPosition) {
            consumed[0] = dx;
            consumed[1] = dy;
        }
        this.evaluate(false);
    }
    
    public boolean onNestedPreFling(final View target, final float velocityX, final float velocityY) {
        return false;
    }
    
    public boolean onNestedFling(final View target, final float velocityX, final float velocityY, final boolean consumed) {
        return false;
    }
    
    private void debugPos() {
        for (int i = 0; i < this.getChildCount(); ++i) {
            final View child = this.getChildAt(i);
            Log.v("MotionLayout", " " + com.smart.library.support.constraint.motion.Debug.getLocation() + " " + com.smart.library.support.constraint.motion.Debug.getName((View)this) + " " + com.smart.library.support.constraint.motion.Debug.getName(this.getContext(), this.mCurrentState) + " " + com.smart.library.support.constraint.motion.Debug.getName(child) + child.getLeft() + " " + child.getTop());
        }
    }
    
    @Override
    protected void dispatchDraw(final Canvas canvas) {
        this.evaluate(false);
        super.dispatchDraw(canvas);
        if (this.mScene == null) {
            return;
        }
        if ((this.mDebugPath & 0x1) == 0x1 && !this.isInEditMode()) {
            ++this.mFrames;
            final long currentDrawTime = this.getNanoTime();
            if (this.mLastDrawTime != -1L) {
                final long delay = currentDrawTime - this.mLastDrawTime;
                if (delay > 200000000L) {
                    final float fps = this.mFrames / (delay * 1.0E-9f);
                    this.mLastFps = (int)(fps * 100.0f) / 100.0f;
                    this.mFrames = 0;
                    this.mLastDrawTime = currentDrawTime;
                }
            }
            else {
                this.mLastDrawTime = currentDrawTime;
            }
            final Paint paint = new Paint();
            paint.setTextSize(42.0f);
            final float p = (int)(this.getProgress() * 1000.0f) / 10.0f;
            String str = this.mLastFps + " fps " + com.smart.library.support.constraint.motion.Debug.getState(this, this.mBeginState) + " -> ";
            str = str + com.smart.library.support.constraint.motion.Debug.getState(this, this.mEndState) + " (progress: " + p + " ) state=" + ((this.mCurrentState == -1) ? "undefined" : com.smart.library.support.constraint.motion.Debug.getState(this, this.mCurrentState));
            paint.setColor(-16777216);
            canvas.drawText(str, 11.0f, (float)(this.getHeight() - 29), paint);
            paint.setColor(-7864184);
            canvas.drawText(str, 10.0f, (float)(this.getHeight() - 30), paint);
        }
        if (this.mDebugPath > 1) {
            if (this.mDevModeDraw == null) {
                this.mDevModeDraw = new DevModeDraw();
            }
            this.mDevModeDraw.draw(canvas, this.mFrameArrayList, this.mScene.getDuration(), this.mDebugPath);
        }
    }
    
    private void evaluateLayout() {
        final float dir = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
        final long currentTime = this.getNanoTime();
        float deltaPos = 0.0f;
        if (!(this.mInterpolator instanceof StopLogic)) {
            deltaPos = dir * (currentTime - this.mTransitionLastTime) * 1.0E-9f / this.mTransitionDuration;
        }
        float position = this.mTransitionLastPosition + deltaPos;
        boolean done = false;
        if (this.mTransitionInstantly) {
            position = this.mTransitionGoalPosition;
        }
        if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
            position = this.mTransitionGoalPosition;
            done = true;
        }
        if (this.mInterpolator != null && !done) {
            if (this.mTemporalInterpolator) {
                final float time = (currentTime - this.mAnimationStartTime) * 1.0E-9f;
                position = this.mInterpolator.getInterpolation(time);
            }
            else {
                position = this.mInterpolator.getInterpolation(position);
            }
        }
        if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
            position = this.mTransitionGoalPosition;
        }
        this.mPostInterpolationPosition = position;
        final int n = this.getChildCount();
        final long time2 = this.getNanoTime();
        for (int i = 0; i < n; ++i) {
            final View child = this.getChildAt(i);
            final MotionController frame = this.mFrameArrayList.get(child);
            if (frame != null) {
                frame.interpolate(child, position, time2, this.mKeyCache);
            }
        }
        if (this.mWidthMeasureMode == Integer.MIN_VALUE || this.mHeightMeasureMode == Integer.MIN_VALUE) {
            this.requestLayout();
        }
    }
    
    void evaluate(final boolean force) {
        if (this.mTransitionLastPosition > 0.0f && this.mTransitionLastPosition < 1.0f) {
            this.mCurrentState = -1;
        }
        boolean newState = false;
        if (this.mKeepAnimating || (this.mInTransition && (force || this.mTransitionGoalPosition != this.mTransitionLastPosition))) {
            final float dir = Math.signum(this.mTransitionGoalPosition - this.mTransitionLastPosition);
            final long currentTime = this.getNanoTime();
            float deltaPos = 0.0f;
            if (!(this.mInterpolator instanceof StopLogic)) {
                deltaPos = dir * (currentTime - this.mTransitionLastTime) * 1.0E-9f / this.mTransitionDuration;
            }
            float position = this.mTransitionLastPosition + deltaPos;
            boolean done = false;
            if (this.mTransitionInstantly) {
                position = this.mTransitionGoalPosition;
            }
            if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
                position = this.mTransitionGoalPosition;
                this.mInTransition = false;
                done = true;
            }
            this.mTransitionLastPosition = position;
            this.mTransitionLastTime = currentTime;
            if (this.mListener != null) {
                this.fireTransitionChange();
            }
            if (this.mInterpolator != null && !done) {
                if (this.mTemporalInterpolator) {
                    final float time = (currentTime - this.mAnimationStartTime) * 1.0E-9f;
                    position = this.mInterpolator.getInterpolation(time);
                    this.mTransitionLastPosition = position;
                    this.mTransitionLastTime = currentTime;
                    if (this.mInterpolator instanceof MotionInterpolator) {
                        final float lastVelocity = ((MotionInterpolator)this.mInterpolator).getVelocity();
                        if (Math.abs(lastVelocity) <= 1.0E-4f) {
                            this.mInTransition = false;
                        }
                    }
                }
                else {
                    position = this.mInterpolator.getInterpolation(position);
                }
            }
            if ((dir > 0.0f && position >= this.mTransitionGoalPosition) || (dir <= 0.0f && position <= this.mTransitionGoalPosition)) {
                position = this.mTransitionGoalPosition;
                this.mInTransition = false;
            }
            if (position >= 1.0f || position <= 0.0f) {
                this.mInTransition = false;
            }
            final int n = this.getChildCount();
            this.mKeepAnimating = false;
            final long time2 = this.getNanoTime();
            this.mPostInterpolationPosition = position;
            for (int i = 0; i < n; ++i) {
                final View child = this.getChildAt(i);
                final MotionController frame = this.mFrameArrayList.get(child);
                if (frame != null) {
                    this.mKeepAnimating |= frame.interpolate(child, position, time2, this.mKeyCache);
                }
            }
            if (this.mWidthMeasureMode == Integer.MIN_VALUE || this.mHeightMeasureMode == Integer.MIN_VALUE) {
                this.requestLayout();
            }
            if (this.mKeepAnimating) {
                this.invalidate();
            }
            if (this.mInTransition) {
                this.invalidate();
            }
            if (position <= 0.0f && this.mBeginState != -1) {
                if (this.mCurrentState != this.mBeginState) {
                    newState = true;
                }
                this.mCurrentState = this.mBeginState;
                final ConstraintSet set = this.mScene.getConstraintSet(this.mBeginState);
                set.applyCustomAttributes(this);
            }
            if (position >= 1.0) {
                if (this.mCurrentState != this.mEndState) {
                    newState = true;
                }
                this.mCurrentState = this.mEndState;
                final ConstraintSet set = this.mScene.getConstraintSet(this.mEndState);
                set.applyCustomAttributes(this);
            }
        }
        if (this.mTransitionLastPosition >= 1.0f) {
            if (this.mCurrentState != this.mEndState) {
                newState = true;
            }
            this.mCurrentState = this.mEndState;
        }
        else if (this.mTransitionLastPosition <= 0.0f) {
            if (this.mCurrentState != this.mBeginState) {
                newState = true;
            }
            this.mCurrentState = this.mBeginState;
        }
        this.mNeedsFireTransitionCompleted |= newState;
        if (Build.VERSION.SDK_INT >= 18 && newState && !this.isInLayout()) {
            this.requestLayout();
        }
        if (newState) {
            this.fireTransitionCompleted();
        }
        this.mTransitionPosition = this.mTransitionLastPosition;
    }
    
    @Override
    protected void onLayout(final boolean changed, final int left, final int top, final int right, final int bottom) {
        if (this.mScene == null) {
            super.onLayout(changed, left, top, right, bottom);
            return;
        }
        final int w = right - left;
        final int h = bottom - top;
        if (this.mLastLayoutWidth != w || this.mLastLayoutHeight != h) {
            this.rebuildScene();
            this.evaluate(true);
        }
        this.mLastLayoutWidth = w;
        this.mLastLayoutHeight = h;
        this.mOldWidth = w;
        this.mOldHeight = h;
    }
    
    @Override
    protected void parseLayoutDescription(final int id) {
        this.mConstraintLayoutSpec = null;
    }
    
    private void init(final AttributeSet attrs) {
        MotionLayout.IS_IN_EDIT_MODE = this.isInEditMode();
        if (attrs != null) {
            final TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.MotionLayout);
            final int N = a.getIndexCount();
            boolean apply = true;
            for (int i = 0; i < N; ++i) {
                final int attr = a.getIndex(i);
                if (attr == R.styleable.MotionLayout_layoutDescription) {
                    final int n = a.getResourceId(attr, -1);
                    this.mScene = new MotionScene(this.getContext(), this, n);
                }
                else if (attr == R.styleable.MotionLayout_currentState) {
                    this.mCurrentState = a.getResourceId(attr, -1);
                }
                else if (attr == R.styleable.MotionLayout_motionProgress) {
                    this.mTransitionGoalPosition = a.getFloat(attr, 0.0f);
                    this.mInTransition = true;
                }
                else if (attr == R.styleable.MotionLayout_applyMotionScene) {
                    apply = a.getBoolean(attr, apply);
                }
                else if (attr == R.styleable.MotionLayout_showPaths) {
                    if (this.mDebugPath == 0) {
                        this.mDebugPath = (a.getBoolean(attr, false) ? 2 : 0);
                    }
                }
                else if (attr == R.styleable.MotionLayout_motionDebug) {
                    this.mDebugPath = a.getInt(attr, 0);
                }
            }
            a.recycle();
            if (this.mScene == null) {
                Log.e("MotionLayout", "WARNING NO app:layoutDescription tag");
            }
            if (!apply) {
                this.mScene = null;
            }
        }
        if (this.mDebugPath != 0) {
            this.checkStructure();
        }
        if (this.mCurrentState == -1 && this.mScene != null) {
            this.mCurrentState = this.mScene.getStartId();
            this.mBeginState = this.mScene.getStartId();
            this.mEndState = this.mScene.getEndId();
        }
    }
    
    public void setScene(final MotionScene scene) {
        this.mScene = scene;
        this.rebuildScene();
    }
    
    private void checkStructure() {
        if (this.mScene == null) {
            Log.e("MotionLayout", "CHECK: motion scene not set! set \"app:layoutDescription=\"@xml/file\"");
            return;
        }
        this.mBeginState = this.mScene.getStartId();
        Log.v("MotionLayout", "CHECK: start is " + com.smart.library.support.constraint.motion.Debug.getName(this.getContext(), this.mBeginState));
        this.mEndState = this.mScene.getEndId();
        Log.v("MotionLayout", "CHECK: end is " + com.smart.library.support.constraint.motion.Debug.getName(this.getContext(), this.mEndState));
        this.checkStructure(this.mBeginState, this.mScene.getConstraintSet(this.mBeginState));
        final SparseIntArray startToEnd = new SparseIntArray();
        final SparseIntArray endToStart = new SparseIntArray();
        for (final MotionScene.Transition definedTransition : this.mScene.getDefinedTransitions()) {
            if (definedTransition == this.mScene.mCurrentTransition) {
                Log.v("MotionLayout", "CHECK: CURRENT");
            }
            this.checkStructure(definedTransition);
            final int startId = definedTransition.getStartConstraintSetId();
            final int endId = definedTransition.getEndConstraintSetId();
            final String startString = com.smart.library.support.constraint.motion.Debug.getName(this.getContext(), startId);
            final String endString = com.smart.library.support.constraint.motion.Debug.getName(this.getContext(), endId);
            if (startToEnd.get(startId) == endId) {
                Log.e("MotionLayout", "CHECK: two transitions with the same start and end " + startString + "->" + endString);
            }
            if (endToStart.get(endId) == startId) {
                Log.e("MotionLayout", "CHECK: you can't have reverse transitions" + startString + "->" + endString);
            }
            startToEnd.put(startId, endId);
            endToStart.put(endId, startId);
            if (this.mScene.getConstraintSet(startId) == null) {
                Log.e("MotionLayout", " no such constraintSetStart " + startString);
            }
            if (this.mScene.getConstraintSet(endId) == null) {
                Log.e("MotionLayout", " no such constraintSetEnd " + startString);
            }
        }
    }
    
    private void checkStructure(final int csetId, final ConstraintSet set) {
        final String setName = com.smart.library.support.constraint.motion.Debug.getName(this.getContext(), csetId);
        for (int size = this.getChildCount(), i = 0; i < size; ++i) {
            final View v = this.getChildAt(i);
            final int id = v.getId();
            if (id == -1) {
                Log.w("MotionLayout", "CHECK: " + setName + " ALL VIEWS SHOULD HAVE ID's " + v.getClass().getName() + " does not!");
            }
            final ConstraintSet.Constraint c = set.getConstraint(id);
            if (c == null) {
                Log.w("MotionLayout", "CHECK: " + setName + " NO CONSTRAINTS for " + com.smart.library.support.constraint.motion.Debug.getName(v));
            }
        }
        final int[] ids = set.getKnownIds();
        for (int j = 0; j < ids.length; ++j) {
            final int id = ids[j];
            final String idString = com.smart.library.support.constraint.motion.Debug.getName(this.getContext(), id);
            if (null == this.findViewById(ids[j])) {
                Log.w("MotionLayout", "CHECK: " + setName + " NO View matches id " + idString);
            }
            if (set.getHeight(id) == -1) {
                Log.w("MotionLayout", "CHECK: " + setName + "(" + idString + ") no LAYOUT_HEIGHT");
            }
            if (set.getWidth(id) == -1) {
                Log.w("MotionLayout", "CHECK: " + setName + "(" + idString + ") no LAYOUT_HEIGHT");
            }
        }
    }
    
    private void checkStructure(final MotionScene.Transition transition) {
        Log.v("MotionLayout", "CHECK: transition = " + transition.debugString(this.getContext()));
        Log.v("MotionLayout", "CHECK: transition.setDuration = " + transition.getDuration());
        if (transition.getStartConstraintSetId() == transition.getEndConstraintSetId()) {
            Log.e("MotionLayout", "CHECK: start and end constraint set should not be the same!");
        }
    }
    
    public void setDebugMode(final int debugMode) {
        this.mDebugPath = debugMode;
        this.invalidate();
    }
    
    public void getDebugMode(final boolean showPaths) {
        this.mDebugPath = (showPaths ? 2 : 1);
        this.invalidate();
    }
    
    private boolean handlesTouchEvent(final float x, final float y, final View view, final MotionEvent event) {
        if (view instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup)view;
            for (int count = group.getChildCount(), i = 0; i < count; ++i) {
                final View child = group.getChildAt(i);
                if (this.handlesTouchEvent(x + view.getLeft(), y + view.getTop(), child, event)) {
                    return true;
                }
            }
        }
        this.mBoundsCheck.set(x + view.getLeft(), y + view.getTop(), x + view.getRight(), y + view.getBottom());
        if (event.getAction() == 0) {
            if (this.mBoundsCheck.contains(event.getX(), event.getY()) && view.onTouchEvent(event)) {
                return true;
            }
        }
        else if (view.onTouchEvent(event)) {
            return true;
        }
        return false;
    }
    
    public boolean onInterceptTouchEvent(final MotionEvent event) {
        if (this.mScene == null) {
            return false;
        }
        final MotionScene.Transition currentTransition = this.mScene.mCurrentTransition;
        if (currentTransition != null && currentTransition.isEnabled()) {
            final TouchResponse touchResponse = currentTransition.getTouchResponse();
            if (touchResponse != null) {
                final int regionId = touchResponse.getTouchRegionId();
                if (regionId != -1) {
                    if (this.mRegionView == null || this.mRegionView.getId() != regionId) {
                        this.mRegionView = this.findViewById(regionId);
                    }
                    if (this.mRegionView != null) {
                        this.mBoundsCheck.set((float)this.mRegionView.getLeft(), (float)this.mRegionView.getTop(), (float)this.mRegionView.getRight(), (float)this.mRegionView.getBottom());
                        if (this.mBoundsCheck.contains(event.getX(), event.getY()) && !this.handlesTouchEvent(0.0f, 0.0f, this.mRegionView, event)) {
                            return this.onTouchEvent(event);
                        }
                    }
                }
            }
        }
        return false;
    }
    
    public boolean onTouchEvent(final MotionEvent event) {
        if (this.mScene != null && this.mScene.supportTouch()) {
            this.mScene.processTouchEvent(event, this.getCurrentState(), this);
            return true;
        }
        return super.onTouchEvent(event);
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mScene != null && this.mCurrentState != -1) {
            final ConstraintSet cSet = this.mScene.getConstraintSet(this.mCurrentState);
            this.mScene.readFallback(this);
            if (cSet != null) {
                cSet.applyTo(this);
            }
        }
        this.onNewStateAttachHandlers();
    }
    
    private void onNewStateAttachHandlers() {
        if (this.mScene == null) {
            return;
        }
        if (this.mScene.autoTransition(this, this.mCurrentState)) {
            return;
        }
        if (this.mCurrentState != -1) {
            this.mScene.addOnClickListeners(this, this.mCurrentState);
        }
        if (this.mScene.supportTouch()) {
            this.mScene.setupTouch();
        }
    }
    
    public int getCurrentState() {
        return this.mCurrentState;
    }
    
    public float getProgress() {
        return this.mTransitionLastPosition;
    }
    
    void getAnchorDpDt(final int mTouchAnchorId, final float pos, final float locationX, final float locationY, final float[] mAnchorDpDt) {
        final View v;
        final MotionController f = this.mFrameArrayList.get(v = this.getViewById(mTouchAnchorId));
        if (f != null) {
            f.getDpDt(pos, locationX, locationY, mAnchorDpDt);
            final float y = v.getY();
            final float deltaPos = pos - this.lastPos;
            final float deltaY = y - this.lastY;
            final float dydp = (deltaPos != 0.0f) ? (deltaY / deltaPos) : Float.NaN;
            this.lastPos = pos;
            this.lastY = y;
        }
        else {
            final String idName = (v == null) ? ("" + mTouchAnchorId) : v.getContext().getResources().getResourceName(mTouchAnchorId);
            Log.w("MotionLayout", "WARNING could not find view id " + idName);
        }
    }
    
    public long getTransitionTimeMs() {
        if (this.mScene != null) {
            this.mTransitionDuration = this.mScene.getDuration() / 1000.0f;
        }
        return (long)this.mTransitionDuration * 1000L;
    }
    
    public void setTransitionListener(final TransitionListener listener) {
        this.mListener = listener;
    }
    
    public void fireTrigger(final int triggerId, final boolean positive, final float progress) {
        if (this.mListener != null) {
            this.mListener.onTransitionTrigger(this, triggerId, positive, progress);
        }
    }
    
    private void fireTransitionChange() {
        if (this.mListener != null && this.mListenerPosition != this.mTransitionPosition) {
            if (this.mListenerState != -1) {
                this.mListener.onTransitionStarted(this, this.mBeginState, this.mEndState);
            }
            this.mListenerState = -1;
            this.mListenerPosition = this.mTransitionPosition;
            this.mListener.onTransitionChange(this, this.mBeginState, this.mEndState, this.mTransitionPosition);
        }
    }
    
    protected void fireTransitionCompleted() {
        if (this.mListener != null && this.mListenerState == -1) {
            this.mListenerState = this.mCurrentState;
            int lastState = -1;
            if (!this.mTransitionCompleted.isEmpty()) {
                lastState = this.mTransitionCompleted.get(this.mTransitionCompleted.size() - 1);
            }
            if (lastState != this.mCurrentState) {
                this.mTransitionCompleted.add(this.mCurrentState);
            }
        }
    }
    
    private void processTransitionCompleted() {
        if (this.mListener == null) {
            return;
        }
        for (final Integer state : this.mTransitionCompleted) {
            this.mListener.onTransitionCompleted(this, state);
        }
        this.mTransitionCompleted.clear();
    }
    
    public DesignTool getDesignTool() {
        if (this.mDesignTool == null) {
            this.mDesignTool = new DesignTool(this);
        }
        return this.mDesignTool;
    }
    
    @Override
    public void onViewAdded(final View view) {
        super.onViewAdded(view);
        if (view instanceof MotionHelper) {
            final MotionHelper helper = (MotionHelper)view;
            if (helper.isUsedOnShow()) {
                if (this.mOnShowHelpers == null) {
                    this.mOnShowHelpers = new ArrayList<MotionHelper>();
                }
                this.mOnShowHelpers.add(helper);
            }
            if (helper.isUseOnHide()) {
                if (this.mOnHideHelpers == null) {
                    this.mOnHideHelpers = new ArrayList<MotionHelper>();
                }
                this.mOnHideHelpers.add(helper);
            }
        }
    }
    
    @Override
    public void onViewRemoved(final View view) {
        super.onViewRemoved(view);
        if (this.mOnShowHelpers != null) {
            this.mOnShowHelpers.remove(view);
        }
        if (this.mOnHideHelpers != null) {
            this.mOnHideHelpers.remove(view);
        }
    }
    
    public void setOnShow(final float progress) {
        if (this.mOnShowHelpers != null) {
            for (int count = this.mOnShowHelpers.size(), i = 0; i < count; ++i) {
                final MotionHelper helper = this.mOnShowHelpers.get(i);
                helper.setProgress(progress);
            }
        }
    }
    
    public void setOnHide(final float progress) {
        if (this.mOnHideHelpers != null) {
            for (int count = this.mOnHideHelpers.size(), i = 0; i < count; ++i) {
                final MotionHelper helper = this.mOnHideHelpers.get(i);
                helper.setProgress(progress);
            }
        }
    }
    
    public int[] getConstraintSetIds() {
        if (this.mScene == null) {
            return null;
        }
        return this.mScene.getConstraintSetIds();
    }
    
    public ConstraintSet getConstraintSet(final int id) {
        if (this.mScene == null) {
            return null;
        }
        return this.mScene.getConstraintSet(id);
    }
    
    @Deprecated
    public void rebuildMotion() {
        Log.e("MotionLayout", "This method is deprecated. Please call rebuildScene() instead.");
        this.rebuildScene();
    }
    
    public void rebuildScene() {
        this.mModel.reEvaluateState();
        this.invalidate();
    }
    
    public void updateState(final int stateId, final ConstraintSet set) {
        if (this.mScene != null) {
            this.mScene.setConstraintSet(stateId, set);
        }
        this.updateState();
    }
    
    public void updateState() {
        this.mModel.initFrom(this.mLayoutWidget, this.mScene.getConstraintSet(this.mBeginState), this.mScene.getConstraintSet(this.mEndState));
        this.rebuildScene();
    }
    
    public ArrayList<MotionScene.Transition> getDefinedTransitions() {
        if (this.mScene == null) {
            return null;
        }
        return this.mScene.getDefinedTransitions();
    }
    
    public int getStartState() {
        return this.mBeginState;
    }
    
    public int getEndState() {
        return this.mEndState;
    }
    
    public float getTargetPosition() {
        return this.mTransitionGoalPosition;
    }
    
    public void setTransitionDuration(final int milliseconds) {
        if (this.mScene == null) {
            Log.e("MotionLayout", "MotionScene not defined");
            return;
        }
        this.mScene.setDuration(milliseconds);
    }
    
    public MotionScene.Transition getTransition(final int id) {
        return this.mScene.getTransitionById(id);
    }
    
    int lookUpConstraintId(final String id) {
        if (this.mScene == null) {
            return 0;
        }
        return this.mScene.lookUpConstraintId(id);
    }
    
    String getConstraintSetNames(final int id) {
        if (this.mScene == null) {
            return null;
        }
        return this.mScene.lookUpConstraintName(id);
    }
    
    void disableAutoTransition(final boolean disable) {
        if (this.mScene == null) {
            return;
        }
        this.mScene.disableAutoTransition(disable);
    }
    
    private static class MyTracker implements MotionTracker
    {
        VelocityTracker tracker;
        private static MyTracker me;
        
        public static MyTracker obtain() {
            MyTracker.me.tracker = VelocityTracker.obtain();
            return MyTracker.me;
        }
        
        @Override
        public void recycle() {
            this.tracker.recycle();
            this.tracker = null;
        }
        
        @Override
        public void clear() {
            this.tracker.clear();
        }
        
        @Override
        public void addMovement(final MotionEvent event) {
            this.tracker.addMovement(event);
        }
        
        @Override
        public void computeCurrentVelocity(final int units) {
            this.tracker.computeCurrentVelocity(units);
        }
        
        @Override
        public void computeCurrentVelocity(final int units, final float maxVelocity) {
            this.tracker.computeCurrentVelocity(units, maxVelocity);
        }
        
        @Override
        public float getXVelocity() {
            return this.tracker.getXVelocity();
        }
        
        @Override
        public float getYVelocity() {
            return this.tracker.getYVelocity();
        }
        
        @Override
        public float getXVelocity(final int id) {
            return this.tracker.getXVelocity(id);
        }
        
        @Override
        public float getYVelocity(final int id) {
            return this.getYVelocity(id);
        }
        
        static {
            MyTracker.me = new MyTracker();
        }
    }
    
    class DecelerateInterpolator extends MotionInterpolator
    {
        float initalV;
        float currentP;
        float maxA;
        
        DecelerateInterpolator() {
            this.initalV = 0.0f;
            this.currentP = 0.0f;
        }
        
        public void config(final float velocity, final float position, final float maxAcceleration) {
            this.initalV = velocity;
            this.currentP = position;
            this.maxA = maxAcceleration;
        }
        
        @Override
        public float getInterpolation(float time) {
            if (this.initalV > 0.0f) {
                if (this.initalV / this.maxA < time) {
                    time = this.initalV / this.maxA;
                }
                MotionLayout.this.mLastVelocity = this.initalV - this.maxA * time;
                final float pos = this.initalV * time - this.maxA * time * time / 2.0f;
                return pos + this.currentP;
            }
            if (-this.initalV / this.maxA < time) {
                time = -this.initalV / this.maxA;
            }
            MotionLayout.this.mLastVelocity = this.initalV + this.maxA * time;
            final float pos = this.initalV * time + this.maxA * time * time / 2.0f;
            return pos + this.currentP;
        }
        
        @Override
        public float getVelocity() {
            return MotionLayout.this.mLastVelocity;
        }
    }
    
    class Model
    {
        ConstraintWidgetContainer mLayoutStart;
        ConstraintWidgetContainer mLayoutEnd;
        ConstraintSet mStart;
        ConstraintSet mEnd;
        int mStartId;
        int mEndId;
        
        Model() {
            this.mLayoutStart = new ConstraintWidgetContainer();
            this.mLayoutEnd = new ConstraintWidgetContainer();
            this.mStart = null;
            this.mEnd = null;
        }
        
        void copy(final ConstraintWidgetContainer src, final ConstraintWidgetContainer dest) {
            final ArrayList<ConstraintWidget> children = (ArrayList<ConstraintWidget>)src.getChildren();
            final HashMap<ConstraintWidget, ConstraintWidget> map = new HashMap<ConstraintWidget, ConstraintWidget>();
            map.put((ConstraintWidget)src, (ConstraintWidget)dest);
            dest.getChildren().clear();
            dest.copy((ConstraintWidget)src, (HashMap)map);
            for (final ConstraintWidget child_s : children) {
                ConstraintWidget child_d;
                if (child_s instanceof Barrier) {
                    child_d = (ConstraintWidget)new Barrier();
                }
                else if (child_s instanceof Guideline) {
                    child_d = (ConstraintWidget)new Guideline();
                }
                else if (child_s instanceof Flow) {
                    child_d = (ConstraintWidget)new Flow();
                }
                else if (child_s instanceof Helper) {
                    child_d = (ConstraintWidget)new HelperWidget();
                }
                else {
                    child_d = new ConstraintWidget();
                }
                dest.add(child_d);
                map.put(child_s, child_d);
            }
            for (final ConstraintWidget child_s : children) {
                map.get(child_s).copy(child_s, (HashMap)map);
            }
        }
        
        void initFrom(final ConstraintWidgetContainer baseLayout, final ConstraintSet start, final ConstraintSet end) {
            this.mStart = start;
            this.mEnd = end;
            this.mLayoutStart.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
            this.mLayoutEnd.setMeasurer(MotionLayout.this.mLayoutWidget.getMeasurer());
            this.mLayoutStart.removeAllChildren();
            this.mLayoutEnd.removeAllChildren();
            this.copy(MotionLayout.this.mLayoutWidget, this.mLayoutStart);
            this.copy(MotionLayout.this.mLayoutWidget, this.mLayoutEnd);
            if (start != null) {
                this.setupConstraintWidget(this.mLayoutStart, start);
            }
            this.setupConstraintWidget(this.mLayoutEnd, end);
            this.mLayoutStart.updateHierarchy();
            this.mLayoutEnd.updateHierarchy();
            final ViewGroup.LayoutParams layoutParams = MotionLayout.this.getLayoutParams();
            if (layoutParams != null) {
                if (layoutParams.width == -2) {
                    this.mLayoutStart.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                    this.mLayoutEnd.setHorizontalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
                if (layoutParams.height == -2) {
                    this.mLayoutStart.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                    this.mLayoutEnd.setVerticalDimensionBehaviour(ConstraintWidget.DimensionBehaviour.WRAP_CONTENT);
                }
            }
        }
        
        private void setupConstraintWidget(final ConstraintWidgetContainer base, final ConstraintSet cset) {
            final SparseArray<ConstraintWidget> mapIdToWidget = (SparseArray<ConstraintWidget>)new SparseArray();
            final Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(-2, -2);
            mapIdToWidget.clear();
            mapIdToWidget.put(0,base);
            mapIdToWidget.put(MotionLayout.this.getId(),base);
            for (final ConstraintWidget child : base.getChildren()) {
                final View view = (View)child.getCompanionWidget();
                mapIdToWidget.put(view.getId(),child);
            }
            for (final ConstraintWidget child : base.getChildren()) {
                final View view = (View)child.getCompanionWidget();
                cset.applyToLayoutParams(view.getId(), layoutParams);
                child.setWidth(cset.getWidth(view.getId()));
                child.setHeight(cset.getHeight(view.getId()));
                if (view instanceof ConstraintHelper) {
                    cset.applyToHelper((ConstraintHelper)view, child, layoutParams, mapIdToWidget);
                    if (view instanceof com.smart.library.support.constraint.Barrier) {
                        ((com.smart.library.support.constraint.Barrier)view).validateParams();
                    }
                }
                if (Build.VERSION.SDK_INT >= 17) {
                    layoutParams.resolveLayoutDirection(MotionLayout.this.getLayoutDirection());
                }
                else {
                    layoutParams.resolveLayoutDirection(0);
                }
                applyConstraintsFromLayoutParams(false, view, child, layoutParams, mapIdToWidget);
                if (cset.getVisibilityMode(view.getId()) == 1) {
                    child.setVisibility(view.getVisibility());
                }
                else {
                    child.setVisibility(cset.getVisibility(view.getId()));
                }
            }
            for (final ConstraintWidget child : base.getChildren()) {
                if (child instanceof Helper) {
                    final Helper helper = (Helper)child;
                    helper.removeAllIds();
                    final ConstraintHelper view2 = (ConstraintHelper)child.getCompanionWidget();
                    view2.updatePreLayout(base, helper, mapIdToWidget);
                    if (!(helper instanceof VirtualLayout)) {
                        continue;
                    }
                    final VirtualLayout virtualLayout = (VirtualLayout)helper;
                    virtualLayout.captureWidgets();
                }
            }
        }
        
        ConstraintWidget getWidget(final ConstraintWidgetContainer container, final View view) {
            if (container.getCompanionWidget() == view) {
                return (ConstraintWidget)container;
            }
            final ArrayList<ConstraintWidget> children = (ArrayList<ConstraintWidget>)container.getChildren();
            for (int count = children.size(), i = 0; i < count; ++i) {
                final ConstraintWidget widget = children.get(i);
                if (widget.getCompanionWidget() == view) {
                    return widget;
                }
            }
            return null;
        }
        
        private void debugLayoutParam(final String str, final LayoutParams params) {
            String a = " ";
            a += ((params.startToStart != -1) ? "SS" : "__");
            a += ((params.startToEnd != -1) ? "|SE" : "|__");
            a += ((params.endToStart != -1) ? "|ES" : "|__");
            a += ((params.endToEnd != -1) ? "|EE" : "|__");
            a += ((params.leftToLeft != -1) ? "|LL" : "|__");
            a += ((params.leftToRight != -1) ? "|LR" : "|__");
            a += ((params.rightToLeft != -1) ? "|RL" : "|__");
            a += ((params.rightToRight != -1) ? "|RR" : "|__");
            a += ((params.topToTop != -1) ? "|TT" : "|__");
            a += ((params.topToBottom != -1) ? "|TB" : "|__");
            a += ((params.bottomToTop != -1) ? "|BT" : "|__");
            a += ((params.bottomToBottom != -1) ? "|BB" : "|__");
            Log.v("MotionLayout", str + a);
        }
        
        private void debugWidget(final String str, final ConstraintWidget child) {
            String a = " ";
            a += ((child.mTop.mTarget != null) ? ("T" + ((child.mTop.mTarget.mType == ConstraintAnchor.Type.TOP) ? "T" : "B")) : "__");
            a += ((child.mBottom.mTarget != null) ? ("B" + ((child.mBottom.mTarget.mType == ConstraintAnchor.Type.TOP) ? "T" : "B")) : "__");
            a += ((child.mLeft.mTarget != null) ? ("L" + ((child.mLeft.mTarget.mType == ConstraintAnchor.Type.LEFT) ? "L" : "R")) : "__");
            a += ((child.mRight.mTarget != null) ? ("R" + ((child.mRight.mTarget.mType == ConstraintAnchor.Type.LEFT) ? "L" : "R")) : "__");
            Log.v("MotionLayout", str + a + " ---  " + child);
        }
        
        private void debugLayout(final String title, final ConstraintWidgetContainer c) {
            View v = (View)c.getCompanionWidget();
            final String cName = title + " " + com.smart.library.support.constraint.motion.Debug.getName(v);
            Log.v("MotionLayout", cName + "  ========= " + c);
            for (int count = c.getChildren().size(), i = 0; i < count; ++i) {
                final String str = cName + "[" + i + "] ";
                final ConstraintWidget child = c.getChildren().get(i);
                String a = "";
                a += ((child.mTop.mTarget != null) ? "T" : "_");
                a += ((child.mBottom.mTarget != null) ? "B" : "_");
                a += ((child.mLeft.mTarget != null) ? "L" : "_");
                a += ((child.mRight.mTarget != null) ? "R" : "_");
                v = (View)child.getCompanionWidget();
                String name = com.smart.library.support.constraint.motion.Debug.getName(v);
                if (v instanceof TextView) {
                    name = name + "(" +((TextView)v).getText() + ")";
                }
                Log.v("MotionLayout", str + "  " + name + " " + child + " " + a);
            }
            Log.v("MotionLayout", cName + " done. ");
        }
        
        public void reEvaluateState() {
            this.measure(MotionLayout.this.mLastWidthMeasureSpec, MotionLayout.this.mLastHeightMeasureSpec);
            MotionLayout.this.setupMotionViews();
        }
        
        public void measure(final int widthMeasureSpec, final int heightMeasureSpec) {
            final int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
            final int heightMode = View.MeasureSpec.getMode(heightMeasureSpec);
            MotionLayout.this.mWidthMeasureMode = widthMode;
            MotionLayout.this.mHeightMeasureMode = heightMode;
            final int optimisationLevel = MotionLayout.this.getOptimizationLevel();
            if (MotionLayout.this.mCurrentState == MotionLayout.this.getStartState()) {
                resolveSystem(this.mLayoutEnd, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                if (this.mStart != null) {
                    resolveSystem(this.mLayoutStart, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                }
            }
            else {
                if (this.mStart != null) {
                    resolveSystem(this.mLayoutStart, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
                }
                resolveSystem(this.mLayoutEnd, optimisationLevel, widthMeasureSpec, heightMeasureSpec);
            }
            MotionLayout.this.mStartWrapWidth = this.mLayoutStart.getWidth();
            MotionLayout.this.mStartWrapHeight = this.mLayoutStart.getHeight();
            MotionLayout.this.mEndWrapWidth = this.mLayoutEnd.getWidth();
            MotionLayout.this.mEndWrapHeight = this.mLayoutEnd.getHeight();
            int width = MotionLayout.this.mStartWrapWidth;
            int height = MotionLayout.this.mStartWrapHeight;
            if (MotionLayout.this.mWidthMeasureMode == Integer.MIN_VALUE) {
                width = (int)(MotionLayout.this.mStartWrapWidth + MotionLayout.this.mPostInterpolationPosition * (MotionLayout.this.mEndWrapWidth - MotionLayout.this.mStartWrapWidth));
            }
            if (MotionLayout.this.mHeightMeasureMode == Integer.MIN_VALUE) {
                height = (int)(MotionLayout.this.mStartWrapHeight + MotionLayout.this.mPostInterpolationPosition * (MotionLayout.this.mEndWrapHeight - MotionLayout.this.mStartWrapHeight));
            }
            final boolean isWidthMeasuredTooSmall = this.mLayoutStart.isWidthMeasuredTooSmall() || this.mLayoutEnd.isWidthMeasuredTooSmall();
            final boolean isHeightMeasuredTooSmall = this.mLayoutStart.isHeightMeasuredTooSmall() || this.mLayoutEnd.isHeightMeasuredTooSmall();
            resolveMeasuredDimension(widthMeasureSpec, heightMeasureSpec, width, height, isWidthMeasuredTooSmall, isHeightMeasuredTooSmall);
        }
        
        public void build() {
            final int n = MotionLayout.this.getChildCount();
            MotionLayout.this.mFrameArrayList.clear();
            for (int i = 0; i < n; ++i) {
                final View v = MotionLayout.this.getChildAt(i);
                final MotionController motionController = new MotionController(v);
                MotionLayout.this.mFrameArrayList.put(v, motionController);
            }
            for (int i = 0; i < n; ++i) {
                final View v = MotionLayout.this.getChildAt(i);
                final MotionController motionController = MotionLayout.this.mFrameArrayList.get(v);
                if (motionController != null) {
                    if (this.mStart != null) {
                        final ConstraintWidget startWidget = this.getWidget(this.mLayoutStart, v);
                        if (startWidget != null) {
                            motionController.setStartState(startWidget, this.mStart);
                        }
                        else {
                            Log.e("MotionLayout", com.smart.library.support.constraint.motion.Debug.getLocation() + "no widget for  " + com.smart.library.support.constraint.motion.Debug.getName(v) + " (" + v.getClass().getName() + ")");
                        }
                    }
                    if (this.mEnd != null) {
                        final ConstraintWidget endWidget = this.getWidget(this.mLayoutEnd, v);
                        if (endWidget != null) {
                            motionController.setEndState(endWidget, this.mEnd);
                        }
                        else {
                            Log.e("MotionLayout", com.smart.library.support.constraint.motion.Debug.getLocation() + "no widget for  " + Debug.getName(v) + " (" + v.getClass().getName() + ")");
                        }
                    }
                }
            }
        }
        
        public void setMeasuredId(final int startId, final int endId) {
            this.mStartId = startId;
            this.mEndId = endId;
        }
        
        public boolean isNotConfiguredWith(final int startId, final int endId) {
            return startId != this.mStartId || endId != this.mEndId;
        }
    }
    
    private class DevModeDraw
    {
        private static final int DEBUG_PATH_TICKS_PER_MS = 16;
        float[] mPoints;
        int[] mPathMode;
        float[] mKeyFramePoints;
        Path mPath;
        Paint mPaint;
        Paint mPaintKeyframes;
        Paint mPaintGraph;
        Paint mTextPaint;
        Paint mFillPaint;
        private float[] mRectangle;
        final int RED_COLOR = -21965;
        final int KEYFRAME_COLOR = -2067046;
        final int GRAPH_COLOR = -13391360;
        final int SHADOW_COLOR = 1996488704;
        final int DIAMOND_SIZE = 10;
        DashPathEffect mDashPathEffect;
        int mKeyFrameCount;
        Rect mBounds;
        boolean mPresentationMode;
        int mShadowTranslate;
        
        public DevModeDraw() {
            this.mBounds = new Rect();
            this.mPresentationMode = false;
            this.mShadowTranslate = 1;
            (this.mPaint = new Paint()).setAntiAlias(true);
            this.mPaint.setColor(-21965);
            this.mPaint.setStrokeWidth(2.0f);
            this.mPaint.setStyle(Paint.Style.STROKE);
            (this.mPaintKeyframes = new Paint()).setAntiAlias(true);
            this.mPaintKeyframes.setColor(-2067046);
            this.mPaintKeyframes.setStrokeWidth(2.0f);
            this.mPaintKeyframes.setStyle(Paint.Style.STROKE);
            (this.mPaintGraph = new Paint()).setAntiAlias(true);
            this.mPaintGraph.setColor(-13391360);
            this.mPaintGraph.setStrokeWidth(2.0f);
            this.mPaintGraph.setStyle(Paint.Style.STROKE);
            (this.mTextPaint = new Paint()).setAntiAlias(true);
            this.mTextPaint.setColor(-13391360);
            this.mTextPaint.setTextSize(12.0f * MotionLayout.this.getContext().getResources().getDisplayMetrics().density);
            this.mRectangle = new float[8];
            (this.mFillPaint = new Paint()).setAntiAlias(true);
            this.mDashPathEffect = new DashPathEffect(new float[] { 4.0f, 8.0f }, 0.0f);
            this.mPaintGraph.setPathEffect((PathEffect)this.mDashPathEffect);
            this.mKeyFramePoints = new float[100];
            this.mPathMode = new int[50];
            if (this.mPresentationMode) {
                this.mPaint.setStrokeWidth(8.0f);
                this.mFillPaint.setStrokeWidth(8.0f);
                this.mPaintKeyframes.setStrokeWidth(8.0f);
                this.mShadowTranslate = 4;
            }
        }
        
        public void draw(final Canvas canvas, final HashMap<View, MotionController> frameArrayList, final int duration, final int debugPath) {
            if (frameArrayList == null || frameArrayList.size() == 0) {
                return;
            }
            canvas.save();
            if (!MotionLayout.this.isInEditMode() && (0x1 & debugPath) == 0x2) {
                final String str = MotionLayout.this.getContext().getResources().getResourceName(MotionLayout.this.mEndState) + ":" + MotionLayout.this.getProgress();
                canvas.drawText(str, 10.0f, (float)(MotionLayout.this.getHeight() - 30), this.mTextPaint);
                canvas.drawText(str, 11.0f, (float)(MotionLayout.this.getHeight() - 29), this.mPaint);
            }
            for (final MotionController motionController : frameArrayList.values()) {
                int mode = motionController.getDrawPath();
                if (debugPath > 0 && mode == 0) {
                    mode = 1;
                }
                if (mode == 0) {
                    continue;
                }
                this.mKeyFrameCount = motionController.buildKeyFrames(this.mKeyFramePoints, this.mPathMode);
                if (mode < 1) {
                    continue;
                }
                final int frames = duration / 16;
                if (this.mPoints == null || this.mPoints.length != frames * 2) {
                    this.mPoints = new float[frames * 2];
                    this.mPath = new Path();
                }
                canvas.translate((float)this.mShadowTranslate, (float)this.mShadowTranslate);
                this.mPaint.setColor(1996488704);
                this.mFillPaint.setColor(1996488704);
                this.mPaintKeyframes.setColor(1996488704);
                this.mPaintGraph.setColor(1996488704);
                motionController.buildPath(this.mPoints, frames);
                this.drawAll(canvas, mode, this.mKeyFrameCount, motionController);
                this.mPaint.setColor(-21965);
                this.mPaintKeyframes.setColor(-2067046);
                this.mFillPaint.setColor(-2067046);
                this.mPaintGraph.setColor(-13391360);
                canvas.translate((float)(-this.mShadowTranslate), (float)(-this.mShadowTranslate));
                this.drawAll(canvas, mode, this.mKeyFrameCount, motionController);
                if (mode != 5) {
                    continue;
                }
                this.drawRectangle(canvas, motionController);
            }
            canvas.restore();
        }
        
        public void drawAll(final Canvas canvas, final int mode, final int keyFrames, final MotionController motionController) {
            if (mode == 4) {
                this.drawPathAsConfigured(canvas);
            }
            if (mode == 2) {
                this.drawPathRelative(canvas);
            }
            if (mode == 3) {
                this.drawPathCartesian(canvas);
            }
            this.drawBasicPath(canvas);
            this.drawTicks(canvas, mode, keyFrames, motionController);
        }
        
        private void drawBasicPath(final Canvas canvas) {
            canvas.drawLines(this.mPoints, this.mPaint);
        }
        
        private void drawTicks(final Canvas canvas, final int mode, final int keyFrames, final MotionController motionController) {
            int viewWidth = 0;
            int viewHeight = 0;
            if (motionController.mView != null) {
                viewWidth = motionController.mView.getWidth();
                viewHeight = motionController.mView.getHeight();
            }
            for (int i = 1; i < keyFrames - 1; ++i) {
                if (mode != 4 || this.mPathMode[i - 1] != 0) {
                    final float x = this.mKeyFramePoints[i * 2];
                    final float y = this.mKeyFramePoints[i * 2 + 1];
                    this.mPath.reset();
                    this.mPath.moveTo(x, y + 10.0f);
                    this.mPath.lineTo(x + 10.0f, y);
                    this.mPath.lineTo(x, y - 10.0f);
                    this.mPath.lineTo(x - 10.0f, y);
                    this.mPath.close();
                    final MotionPaths framePoint = motionController.getKeyFrame(i - 1);
                    final float dx = 0.0f;
                    final float dy = 0.0f;
                    if (mode == 4) {
                        if (this.mPathMode[i - 1] == 1) {
                            this.drawPathRelativeTicks(canvas, x - dx, y - dy);
                        }
                        else if (this.mPathMode[i - 1] == 2) {
                            this.drawPathCartesianTicks(canvas, x - dx, y - dy);
                        }
                        else if (this.mPathMode[i - 1] == 3) {
                            this.drawPathScreenTicks(canvas, x - dx, y - dy, viewWidth, viewHeight);
                        }
                        canvas.drawPath(this.mPath, this.mFillPaint);
                    }
                    if (mode == 2) {
                        this.drawPathRelativeTicks(canvas, x - dx, y - dy);
                    }
                    if (mode == 3) {
                        this.drawPathCartesianTicks(canvas, x - dx, y - dy);
                    }
                    if (mode == 6) {
                        this.drawPathScreenTicks(canvas, x - dx, y - dy, viewWidth, viewHeight);
                    }
                    if (dx != 0.0f || dy != 0.0f) {
                        this.drawTranslation(canvas, x - dx, y - dy, x, y);
                    }
                    else {
                        canvas.drawPath(this.mPath, this.mFillPaint);
                    }
                }
            }
            if (this.mPoints.length > 1) {
                canvas.drawCircle(this.mPoints[0], this.mPoints[1], 8.0f, this.mPaintKeyframes);
                canvas.drawCircle(this.mPoints[this.mPoints.length - 2], this.mPoints[this.mPoints.length - 1], 8.0f, this.mPaintKeyframes);
            }
        }
        
        private void drawTranslation(final Canvas canvas, final float x1, final float y1, final float x2, final float y2) {
            canvas.drawRect(x1, y1, x2, y2, this.mPaintGraph);
            canvas.drawLine(x1, y1, x2, y2, this.mPaintGraph);
        }
        
        private void drawPathRelative(final Canvas canvas) {
            canvas.drawLine(this.mPoints[0], this.mPoints[1], this.mPoints[this.mPoints.length - 2], this.mPoints[this.mPoints.length - 1], this.mPaintGraph);
        }
        
        private void drawPathAsConfigured(final Canvas canvas) {
            boolean path = false;
            boolean cart = false;
            for (int i = 0; i < this.mKeyFrameCount; ++i) {
                if (this.mPathMode[i] == 1) {
                    path = true;
                }
                if (this.mPathMode[i] == 2) {
                    cart = true;
                }
            }
            if (path) {
                this.drawPathRelative(canvas);
            }
            if (cart) {
                this.drawPathCartesian(canvas);
            }
        }
        
        private void drawPathRelativeTicks(final Canvas canvas, final float x, final float y) {
            final float x2 = this.mPoints[0];
            final float y2 = this.mPoints[1];
            final float x3 = this.mPoints[this.mPoints.length - 2];
            final float y3 = this.mPoints[this.mPoints.length - 1];
            final float dist = (float)Math.hypot(x2 - x3, y2 - y3);
            final float t = ((x - x2) * (x3 - x2) + (y - y2) * (y3 - y2)) / (dist * dist);
            final float xp = x2 + t * (x3 - x2);
            final float yp = y2 + t * (y3 - y2);
            final Path path = new Path();
            path.moveTo(x, y);
            path.lineTo(xp, yp);
            final float len = (float)Math.hypot(xp - x, yp - y);
            final String text = "" + (int)(100.0f * len / dist) / 100.0f;
            this.getTextBounds(text, this.mTextPaint);
            final float off = len / 2.0f - this.mBounds.width() / 2;
            canvas.drawTextOnPath(text, path, off, -20.0f, this.mTextPaint);
            canvas.drawLine(x, y, xp, yp, this.mPaintGraph);
        }
        
        void getTextBounds(final String text, final Paint paint) {
            paint.getTextBounds(text, 0, text.length(), this.mBounds);
        }
        
        private void drawPathCartesian(final Canvas canvas) {
            final float x1 = this.mPoints[0];
            final float y1 = this.mPoints[1];
            final float x2 = this.mPoints[this.mPoints.length - 2];
            final float y2 = this.mPoints[this.mPoints.length - 1];
            canvas.drawLine(Math.min(x1, x2), Math.max(y1, y2), Math.max(x1, x2), Math.max(y1, y2), this.mPaintGraph);
            canvas.drawLine(Math.min(x1, x2), Math.min(y1, y2), Math.min(x1, x2), Math.max(y1, y2), this.mPaintGraph);
        }
        
        private void drawPathCartesianTicks(final Canvas canvas, final float x, final float y) {
            final float x2 = this.mPoints[0];
            final float y2 = this.mPoints[1];
            final float x3 = this.mPoints[this.mPoints.length - 2];
            final float y3 = this.mPoints[this.mPoints.length - 1];
            final float minx = Math.min(x2, x3);
            final float maxy = Math.max(y2, y3);
            final float xgap = x - Math.min(x2, x3);
            final float ygap = Math.max(y2, y3) - y;
            String text = "" + (int)(0.5 + 100.0f * xgap / Math.abs(x3 - x2)) / 100.0f;
            this.getTextBounds(text, this.mTextPaint);
            float off = xgap / 2.0f - this.mBounds.width() / 2;
            canvas.drawText(text, off + minx, y - 20.0f, this.mTextPaint);
            canvas.drawLine(x, y, Math.min(x2, x3), y, this.mPaintGraph);
            text = "" + (int)(0.5 + 100.0f * ygap / Math.abs(y3 - y2)) / 100.0f;
            this.getTextBounds(text, this.mTextPaint);
            off = ygap / 2.0f - this.mBounds.height() / 2;
            canvas.drawText(text, x + 5.0f, maxy - off, this.mTextPaint);
            canvas.drawLine(x, y, x, Math.max(y2, y3), this.mPaintGraph);
        }
        
        private void drawPathScreenTicks(final Canvas canvas, final float x, final float y, final int viewWidth, final int viewHeight) {
            final float x2 = 0.0f;
            final float y2 = 0.0f;
            final float x3 = 1.0f;
            final float y3 = 1.0f;
            final float minx = 0.0f;
            final float maxy = 0.0f;
            String text = "" + (int)(0.5 + 100.0f * (x - viewWidth / 2) / (MotionLayout.this.getWidth() - viewWidth)) / 100.0f;
            this.getTextBounds(text, this.mTextPaint);
            float off = x / 2.0f - this.mBounds.width() / 2;
            canvas.drawText(text, off + minx, y - 20.0f, this.mTextPaint);
            canvas.drawLine(x, y, Math.min(x2, x3), y, this.mPaintGraph);
            text = "" + (int)(0.5 + 100.0f * (y - viewHeight / 2) / (MotionLayout.this.getHeight() - viewHeight)) / 100.0f;
            this.getTextBounds(text, this.mTextPaint);
            off = y / 2.0f - this.mBounds.height() / 2;
            canvas.drawText(text, x + 5.0f, maxy - off, this.mTextPaint);
            canvas.drawLine(x, y, x, Math.max(y2, y3), this.mPaintGraph);
        }
        
        private void drawRectangle(final Canvas canvas, final MotionController motionController) {
            this.mPath.reset();
            for (int rectFrames = 50, i = 0; i <= rectFrames; ++i) {
                final float p = i / rectFrames;
                motionController.buildRect(p, this.mRectangle, 0);
                this.mPath.moveTo(this.mRectangle[0], this.mRectangle[1]);
                this.mPath.lineTo(this.mRectangle[2], this.mRectangle[3]);
                this.mPath.lineTo(this.mRectangle[4], this.mRectangle[5]);
                this.mPath.lineTo(this.mRectangle[6], this.mRectangle[7]);
                this.mPath.close();
            }
            this.mPaint.setColor(1140850688);
            canvas.translate(2.0f, 2.0f);
            canvas.drawPath(this.mPath, this.mPaint);
            canvas.translate(-2.0f, -2.0f);
            this.mPaint.setColor(-65536);
            canvas.drawPath(this.mPath, this.mPaint);
        }
    }
    
    public interface TransitionListener
    {
        void onTransitionStarted(final MotionLayout p0, final int p1, final int p2);
        
        void onTransitionChange(final MotionLayout p0, final int p1, final int p2, final float p3);
        
        void onTransitionCompleted(final MotionLayout p0, final int p1);
        
        void onTransitionTrigger(final MotionLayout p0, final int p1, final boolean p2, final float p3);
    }
    
    protected interface MotionTracker
    {
        void recycle();
        
        void clear();
        
        void addMovement(final MotionEvent p0);
        
        void computeCurrentVelocity(final int p0);
        
        void computeCurrentVelocity(final int p0, final float p1);
        
        float getXVelocity();
        
        float getYVelocity();
        
        float getXVelocity(final int p0);
        
        float getYVelocity(final int p0);
    }
}
