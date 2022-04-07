package com.smart.library.support.constraint.motion;

import com.smart.library.support.constraint.*;

public class TransitionBuilder
{
    private static final String TAG = "TransitionBuilder";
    
    public static void validate(final MotionLayout layout) {
        if (layout.mScene == null) {
            throw new RuntimeException("Invalid motion layout. Layout missing Motion Scene.");
        }
        final MotionScene scene = layout.mScene;
        if (!scene.validateLayout(layout)) {
            throw new RuntimeException("MotionLayout doesn't have the right motion scene.");
        }
        if (scene.mCurrentTransition == null || scene.getDefinedTransitions().isEmpty()) {
            throw new RuntimeException("Invalid motion layout. Motion Scene doesn't have any transition.");
        }
    }
    
    public static MotionScene.Transition buildTransition(final MotionScene scene, final int transitionId, final int startConstraintSetId, final ConstraintSet startConstraintSet, final int endConstraintSetId, final ConstraintSet endConstraintSet) {
        final MotionScene.Transition transition = new MotionScene.Transition(transitionId, scene, startConstraintSetId, endConstraintSetId);
        updateConstraintSetInMotionScene(scene, transition, startConstraintSet, endConstraintSet);
        return transition;
    }
    
    private static void updateConstraintSetInMotionScene(final MotionScene scene, final MotionScene.Transition transition, final ConstraintSet startConstraintSet, final ConstraintSet endConstraintSet) {
        final int startId = transition.getStartConstraintSetId();
        final int endId = transition.getEndConstraintSetId();
        scene.setConstraintSet(startId, startConstraintSet);
        scene.setConstraintSet(endId, endConstraintSet);
    }
}
