package com.smart.library.support.constraint.motion;

import java.util.*;
import android.util.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.*;
import android.view.*;

public class DesignTool implements ProxyInterface
{
    private static final boolean DEBUG = false;
    private static final String TAG = "DesignTool";
    private final MotionLayout mMotionLayout;
    private MotionScene mSceneCache;
    private String mLastStartState;
    private String mLastEndState;
    private int mLastStartStateId;
    private int mLastEndStateId;
    static final HashMap<Pair<Integer, Integer>, String> allAttributes;
    static final HashMap<String, String> allMargins;
    
    public DesignTool(final MotionLayout motionLayout) {
        this.mLastStartState = null;
        this.mLastEndState = null;
        this.mLastStartStateId = -1;
        this.mLastEndStateId = -1;
        this.mMotionLayout = motionLayout;
    }
    
    private static int GetPxFromDp(final int dpi, final String value) {
        if (value == null) {
            return 0;
        }
        final int index = value.indexOf(100);
        if (index == -1) {
            return 0;
        }
        final String filteredValue = value.substring(0, index);
        final int dpValue = (int)(Integer.valueOf(filteredValue) * dpi / 160.0f);
        return dpValue;
    }
    
    private static void Connect(final int dpi, final ConstraintSet set, final View view, final HashMap<String, String> attributes, final int from, final int to) {
        final String connection = DesignTool.allAttributes.get(Pair.create((Object)from,to));
        final String connectionValue = attributes.get(connection);
        if (connectionValue != null) {
            int marginValue = 0;
            final String margin = DesignTool.allMargins.get(connection);
            if (margin != null) {
                marginValue = GetPxFromDp(dpi, attributes.get(margin));
            }
            final int id = Integer.parseInt(connectionValue);
            set.connect(view.getId(), from, id, to, marginValue);
        }
    }
    
    private static void SetBias(final ConstraintSet set, final View view, final HashMap<String, String> attributes, final int type) {
        String bias = "layout_constraintHorizontal_bias";
        if (type == 1) {
            bias = "layout_constraintVertical_bias";
        }
        final String biasValue = attributes.get(bias);
        if (biasValue != null) {
            if (type == 0) {
                set.setHorizontalBias(view.getId(), Float.parseFloat(biasValue));
            }
            else if (type == 1) {
                set.setVerticalBias(view.getId(), Float.parseFloat(biasValue));
            }
        }
    }
    
    private static void SetDimensions(final int dpi, final ConstraintSet set, final View view, final HashMap<String, String> attributes, final int type) {
        String dimension = "layout_width";
        if (type == 1) {
            dimension = "layout_height";
        }
        final String dimensionValue = attributes.get(dimension);
        if (dimensionValue != null) {
            int value = -2;
            if (!dimensionValue.equalsIgnoreCase("wrap_content")) {
                value = GetPxFromDp(dpi, dimensionValue);
            }
            if (type == 0) {
                set.constrainWidth(view.getId(), value);
            }
            else {
                set.constrainHeight(view.getId(), value);
            }
        }
    }
    
    private static void SetAbsolutePositions(final int dpi, final ConstraintSet set, final View view, final HashMap<String, String> attributes) {
        final String absoluteX = attributes.get("layout_editor_absoluteX");
        if (absoluteX != null) {
            set.setEditorAbsoluteX(view.getId(), GetPxFromDp(dpi, absoluteX));
        }
        final String absoluteY = attributes.get("layout_editor_absoluteY");
        if (absoluteY != null) {
            set.setEditorAbsoluteY(view.getId(), GetPxFromDp(dpi, absoluteY));
        }
    }
    
    public int getAnimationPath(final Object view, final float[] path, final int len) {
        if (this.mMotionLayout.mScene == null) {
            return -1;
        }
        final MotionController motionController = this.mMotionLayout.mFrameArrayList.get(view);
        if (motionController == null) {
            return 0;
        }
        motionController.buildPath(path, len);
        return len;
    }
    
    public void getAnimationRectangles(final Object view, final float[] path) {
        if (this.mMotionLayout.mScene == null) {
            return;
        }
        final int duration = this.mMotionLayout.mScene.getDuration();
        final int frames = duration / 16;
        final MotionController motionController = this.mMotionLayout.mFrameArrayList.get(view);
        if (motionController == null) {
            return;
        }
        motionController.buildRectangles(path, frames);
    }
    
    public int getAnimationKeyFrames(final Object view, final float[] key) {
        if (this.mMotionLayout.mScene == null) {
            return -1;
        }
        final int duration = this.mMotionLayout.mScene.getDuration();
        final int frames = duration / 16;
        final MotionController motionController = this.mMotionLayout.mFrameArrayList.get(view);
        if (motionController == null) {
            return 0;
        }
        motionController.buildKeyFrames(key, null);
        return frames;
    }
    
    @Override
    public void setToolPosition(final float position) {
        if (this.mMotionLayout.mScene == null) {
            this.mMotionLayout.mScene = this.mSceneCache;
        }
        this.mMotionLayout.setProgress(position);
        this.mMotionLayout.evaluate(true);
        this.mMotionLayout.requestLayout();
        this.mMotionLayout.invalidate();
    }
    
    public void setState(String id) {
        if (id == null) {
            id = "motion_base";
        }
        if (this.mLastStartState == id) {
            return;
        }
        this.mLastStartState = id;
        this.mLastEndState = null;
        if (id == null) {}
        if (this.mMotionLayout.mScene == null) {
            this.mMotionLayout.mScene = this.mSceneCache;
        }
        final int rscId = (id != null) ? this.mMotionLayout.lookUpConstraintId(id) : R.id.motion_base;
        if ((this.mLastStartStateId = rscId) != 0) {
            if (rscId == this.mMotionLayout.getStartState()) {
                this.mMotionLayout.setProgress(0.0f);
            }
            else if (rscId == this.mMotionLayout.getEndState()) {
                this.mMotionLayout.setProgress(1.0f);
            }
            else {
                this.mMotionLayout.transitionToState(rscId);
                this.mMotionLayout.setProgress(1.0f);
            }
        }
        this.mMotionLayout.requestLayout();
    }
    
    public String getStartState() {
        final int startid = this.mMotionLayout.getStartState();
        if (this.mLastStartStateId == startid) {
            return this.mLastStartState;
        }
        final String last = this.mMotionLayout.getConstraintSetNames(startid);
        if (last != null) {
            this.mLastStartState = last;
            this.mLastStartStateId = startid;
        }
        return this.mMotionLayout.getConstraintSetNames(startid);
    }
    
    public String getEndState() {
        final int endId = this.mMotionLayout.getEndState();
        if (this.mLastEndStateId == endId) {
            return this.mLastEndState;
        }
        final String last = this.mMotionLayout.getConstraintSetNames(endId);
        if (last != null) {
            this.mLastEndState = last;
            this.mLastEndStateId = endId;
        }
        return last;
    }
    
    public float getProgress() {
        return this.mMotionLayout.getProgress();
    }
    
    public String getState() {
        if (this.mLastStartState != null && this.mLastEndState != null) {
            final float progress = this.getProgress();
            final float epsilon = 0.01f;
            if (progress <= epsilon) {
                return this.mLastStartState;
            }
            if (progress >= 1.0f - epsilon) {
                return this.mLastEndState;
            }
        }
        return this.mLastStartState;
    }
    
    public boolean isInTransition() {
        return this.mLastStartState != null && this.mLastEndState != null;
    }
    
    public void setTransition(final String start, final String end) {
        if (this.mMotionLayout.mScene == null) {
            this.mMotionLayout.mScene = this.mSceneCache;
        }
        final int startId = this.mMotionLayout.lookUpConstraintId(start);
        final int endId = this.mMotionLayout.lookUpConstraintId(end);
        this.mMotionLayout.setTransition(startId, endId);
        this.mLastStartStateId = startId;
        this.mLastEndStateId = endId;
        this.mLastStartState = start;
        this.mLastEndState = end;
    }
    
    public void disableAutoTransition(final boolean disable) {
        this.mMotionLayout.disableAutoTransition(disable);
    }
    
    @Override
    public long getTransitionTimeMs() {
        return this.mMotionLayout.getTransitionTimeMs();
    }
    
    public int getKeyFramePositions(final Object view, final int[] type, final float[] pos) {
        final MotionController controller = this.mMotionLayout.mFrameArrayList.get(view);
        return controller.getkeyFramePositions(type, pos);
    }
    
    @Override
    public float getKeyFramePosition(final Object view, final int type, final float x, final float y) {
        return this.mMotionLayout.mFrameArrayList.get(view).getKeyFrameParameter(type, x, y);
    }
    
    @Override
    public void setKeyFrame(final Object view, final int position, final String name, final Object value) {
        if (this.mMotionLayout.mScene != null) {
            this.mMotionLayout.mScene.setKeyframe((View)view, position, name, value);
            this.mMotionLayout.mTransitionGoalPosition = position / 100.0f;
            this.mMotionLayout.mTransitionLastPosition = 0.0f;
            this.mMotionLayout.rebuildScene();
            this.mMotionLayout.evaluate(true);
        }
    }
    
    @Override
    public boolean setKeyFramePosition(final Object view, int position, final int type, final float x, final float y) {
        if (this.mMotionLayout.mScene != null) {
            final MotionController motionController = this.mMotionLayout.mFrameArrayList.get(view);
            position = (int)(this.mMotionLayout.mTransitionPosition * 100.0f);
            if (motionController != null && this.mMotionLayout.mScene.hasKeyFramePosition((View)view, position)) {
                final float fx = motionController.getKeyFrameParameter(2, x, y);
                final float fy = motionController.getKeyFrameParameter(5, x, y);
                this.mMotionLayout.mScene.setKeyframe((View)view, position, "motion:percentX", fx);
                this.mMotionLayout.mScene.setKeyframe((View)view, position, "motion:percentY", fy);
                this.mMotionLayout.rebuildScene();
                this.mMotionLayout.evaluate(true);
                this.mMotionLayout.invalidate();
                return true;
            }
        }
        return false;
    }
    
    public void setViewDebug(final Object view, final int debugMode) {
        final MotionController motionController = this.mMotionLayout.mFrameArrayList.get(view);
        if (motionController != null) {
            motionController.setDrawPath(debugMode);
            this.mMotionLayout.invalidate();
        }
    }
    
    @Override
    public int designAccess(final int cmd, final String type, final Object viewObject, final float[] in, final int inLength, final float[] out, final int outLength) {
        final View view = (View)viewObject;
        MotionController motionController = null;
        if (cmd != 0) {
            if (this.mMotionLayout.mScene == null) {
                return -1;
            }
            if (view == null) {
                return -1;
            }
            motionController = this.mMotionLayout.mFrameArrayList.get(view);
            if (motionController == null) {
                return -1;
            }
        }
        switch (cmd) {
            case 0: {
                return 1;
            }
            case 1: {
                final int duration = this.mMotionLayout.mScene.getDuration();
                final int frames = duration / 16;
                motionController.buildPath(out, frames);
                return frames;
            }
            case 2: {
                final int duration = this.mMotionLayout.mScene.getDuration();
                final int frames = duration / 16;
                motionController.buildKeyFrames(out, null);
                return frames;
            }
            case 3: {
                final int duration = this.mMotionLayout.mScene.getDuration();
                final int frames = duration / 16;
                return motionController.getAttributeValues(type, out, outLength);
            }
            default: {
                return -1;
            }
        }
    }
    
    public Object getKeyframe(final int type, final int target, final int position) {
        if (this.mMotionLayout.mScene == null) {
            return null;
        }
        return this.mMotionLayout.mScene.getKeyFrame(this.mMotionLayout.getContext(), type, target, position);
    }
    
    @Override
    public Object getKeyframeAtLocation(final Object viewObject, final float x, final float y) {
        final View view = (View)viewObject;
        MotionController motionController = null;
        if (this.mMotionLayout.mScene == null) {
            return -1;
        }
        if (view == null) {
            return null;
        }
        motionController = this.mMotionLayout.mFrameArrayList.get(view);
        if (motionController == null) {
            return null;
        }
        final ViewGroup viewGroup = (ViewGroup)view.getParent();
        final int layoutWidth = viewGroup.getWidth();
        final int layoutHeight = viewGroup.getHeight();
        return motionController.getPositionKeyframe(layoutWidth, layoutHeight, x, y);
    }
    
    @Override
    public Boolean getPositionKeyframe(final Object keyFrame, final Object view, final float x, final float y, final String[] attribute, final float[] value) {
        if (keyFrame instanceof KeyPositionBase) {
            final KeyPositionBase key = (KeyPositionBase)keyFrame;
            final MotionController motionController = this.mMotionLayout.mFrameArrayList.get(view);
            motionController.positionKeyframe((View)view, key, x, y, attribute, value);
            this.mMotionLayout.rebuildScene();
            this.mMotionLayout.mInTransition = true;
            return true;
        }
        return false;
    }
    
    public Object getKeyframe(final Object view, final int type, final int position) {
        if (this.mMotionLayout.mScene == null) {
            return null;
        }
        final int target = ((View)view).getId();
        return this.mMotionLayout.mScene.getKeyFrame(this.mMotionLayout.getContext(), type, target, position);
    }
    
    public void setKeyframe(final Object keyFrame, final String tag, final Object value) {
        if (keyFrame instanceof Key) {
            final Key key = (Key)keyFrame;
            key.setValue(tag, value);
            this.mMotionLayout.rebuildScene();
            this.mMotionLayout.mInTransition = true;
        }
    }
    
    @Override
    public void setAttributes(final int dpi, final String constraintSetId, final Object opaqueView, final Object opaqueAttributes) {
        final View view = (View)opaqueView;
        final HashMap<String, String> attributes = (HashMap<String, String>)opaqueAttributes;
        final int rscId = this.mMotionLayout.lookUpConstraintId(constraintSetId);
        final ConstraintSet set = this.mMotionLayout.mScene.getConstraintSet(rscId);
        if (set == null) {
            return;
        }
        set.clear(view.getId());
        SetDimensions(dpi, set, view, attributes, 0);
        SetDimensions(dpi, set, view, attributes, 1);
        Connect(dpi, set, view, attributes, 6, 6);
        Connect(dpi, set, view, attributes, 6, 7);
        Connect(dpi, set, view, attributes, 7, 7);
        Connect(dpi, set, view, attributes, 7, 6);
        Connect(dpi, set, view, attributes, 1, 1);
        Connect(dpi, set, view, attributes, 1, 2);
        Connect(dpi, set, view, attributes, 2, 2);
        Connect(dpi, set, view, attributes, 2, 1);
        Connect(dpi, set, view, attributes, 3, 3);
        Connect(dpi, set, view, attributes, 3, 4);
        Connect(dpi, set, view, attributes, 4, 3);
        Connect(dpi, set, view, attributes, 4, 4);
        Connect(dpi, set, view, attributes, 5, 5);
        SetBias(set, view, attributes, 0);
        SetBias(set, view, attributes, 1);
        SetAbsolutePositions(dpi, set, view, attributes);
        this.mMotionLayout.updateState(rscId, set);
        this.mMotionLayout.requestLayout();
    }
    
    public void dumpConstraintSet(final String set) {
        if (this.mMotionLayout.mScene == null) {
            this.mMotionLayout.mScene = this.mSceneCache;
        }
        final int setId = this.mMotionLayout.lookUpConstraintId(set);
        System.out.println(" dumping  " + set + " (" + setId + ")");
        try {
            this.mMotionLayout.mScene.getConstraintSet(setId).dump(this.mMotionLayout.mScene, new int[0]);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    static {
        allAttributes = new HashMap<Pair<Integer, Integer>, String>();
        allMargins = new HashMap<String, String>();
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(4,4), "layout_constraintBottom_toBottomOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(4,3), "layout_constraintBottom_toTopOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(3,4), "layout_constraintTop_toBottomOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(3,3), "layout_constraintTop_toTopOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(6,6), "layout_constraintStart_toStartOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(6,7), "layout_constraintStart_toEndOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(7,6), "layout_constraintEnd_toStartOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(7,7), "layout_constraintEnd_toEndOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(1,1), "layout_constraintLeft_toLeftOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(1,2), "layout_constraintLeft_toRightOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(2,2), "layout_constraintRight_toRightOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(2,1), "layout_constraintRight_toLeftOf");
        DesignTool.allAttributes.put((Pair<Integer, Integer>)Pair.create(5,5), "layout_constraintBaseline_toBaselineOf");
        DesignTool.allMargins.put("layout_constraintBottom_toBottomOf", "layout_marginBottom");
        DesignTool.allMargins.put("layout_constraintBottom_toTopOf", "layout_marginBottom");
        DesignTool.allMargins.put("layout_constraintTop_toBottomOf", "layout_marginTop");
        DesignTool.allMargins.put("layout_constraintTop_toTopOf", "layout_marginTop");
        DesignTool.allMargins.put("layout_constraintStart_toStartOf", "layout_marginStart");
        DesignTool.allMargins.put("layout_constraintStart_toEndOf", "layout_marginStart");
        DesignTool.allMargins.put("layout_constraintEnd_toStartOf", "layout_marginEnd");
        DesignTool.allMargins.put("layout_constraintEnd_toEndOf", "layout_marginEnd");
        DesignTool.allMargins.put("layout_constraintLeft_toLeftOf", "layout_marginLeft");
        DesignTool.allMargins.put("layout_constraintLeft_toRightOf", "layout_marginLeft");
        DesignTool.allMargins.put("layout_constraintRight_toRightOf", "layout_marginRight");
        DesignTool.allMargins.put("layout_constraintRight_toLeftOf", "layout_marginRight");
    }
}
