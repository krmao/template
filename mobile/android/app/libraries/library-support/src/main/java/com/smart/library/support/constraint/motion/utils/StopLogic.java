package com.smart.library.support.constraint.motion.utils;

import com.smart.library.support.constraint.motion.*;
import android.util.*;

public class StopLogic extends MotionInterpolator
{
    private float mStage1Velocity;
    private float mStage2Velocity;
    private float mStage3Velocity;
    private float mStage1Duration;
    private float mStage2Duration;
    private float mStage3Duration;
    private float mStage1EndPosition;
    private float mStage2EndPosition;
    private float mStage3EndPosition;
    private int mNumberOfStages;
    private String mType;
    private boolean mBackwards;
    private float mStartPosition;
    private float mLastPosition;
    
    public StopLogic() {
        this.mBackwards = false;
    }
    
    public void debug(final String tag, final String desc, float time) {
        Log.v(tag, desc + " ===== " + this.mType);
        Log.v(tag, desc + (this.mBackwards ? "backwards" : "forward ") + " time = " + time + "  stages " + this.mNumberOfStages);
        Log.v(tag, desc + " dur " + this.mStage1Duration + " vel " + this.mStage1Velocity + " pos " + this.mStage1EndPosition);
        if (this.mNumberOfStages > 1) {
            Log.v(tag, desc + " dur " + this.mStage2Duration + " vel " + this.mStage2Velocity + " pos " + this.mStage2EndPosition);
        }
        if (this.mNumberOfStages > 2) {
            Log.v(tag, desc + " dur " + this.mStage3Duration + " vel " + this.mStage3Velocity + " pos " + this.mStage3EndPosition);
        }
        if (time <= this.mStage1Duration) {
            Log.v(tag, desc + "stage 0");
            return;
        }
        if (this.mNumberOfStages == 1) {
            Log.v(tag, desc + "end stage 0");
            return;
        }
        time -= this.mStage1Duration;
        if (time < this.mStage2Duration) {
            Log.v(tag, desc + " stage 1");
            return;
        }
        if (this.mNumberOfStages == 2) {
            Log.v(tag, desc + "end stage 1");
            return;
        }
        time -= this.mStage2Duration;
        if (time < this.mStage3Duration) {
            Log.v(tag, desc + " stage 2");
            return;
        }
        Log.v(tag, desc + " end stage 2");
    }
    
    public float getVelocity(float x) {
        if (x <= this.mStage1Duration) {
            return this.mStage1Velocity + (this.mStage2Velocity - this.mStage1Velocity) * x / this.mStage1Duration;
        }
        if (this.mNumberOfStages == 1) {
            return 0.0f;
        }
        x -= this.mStage1Duration;
        if (x < this.mStage2Duration) {
            return this.mStage2Velocity + (this.mStage3Velocity - this.mStage2Velocity) * x / this.mStage2Duration;
        }
        if (this.mNumberOfStages == 2) {
            return this.mStage2EndPosition;
        }
        x -= this.mStage2Duration;
        if (x < this.mStage3Duration) {
            return this.mStage3Velocity - this.mStage3Velocity * x / this.mStage3Duration;
        }
        return this.mStage3EndPosition;
    }
    
    private float calcY(float time) {
        if (time <= this.mStage1Duration) {
            return this.mStage1Velocity * time + (this.mStage2Velocity - this.mStage1Velocity) * time * time / (2.0f * this.mStage1Duration);
        }
        if (this.mNumberOfStages == 1) {
            return this.mStage1EndPosition;
        }
        time -= this.mStage1Duration;
        if (time < this.mStage2Duration) {
            return this.mStage1EndPosition + this.mStage2Velocity * time + (this.mStage3Velocity - this.mStage2Velocity) * time * time / (2.0f * this.mStage2Duration);
        }
        if (this.mNumberOfStages == 2) {
            return this.mStage2EndPosition;
        }
        time -= this.mStage2Duration;
        if (time < this.mStage3Duration) {
            return this.mStage2EndPosition + this.mStage3Velocity * time - this.mStage3Velocity * time * time / (2.0f * this.mStage3Duration);
        }
        return this.mStage3EndPosition;
    }
    
    public void config(final float currentPos, final float destination, final float currentVelocity, final float maxTime, final float maxAcceleration, final float maxVelocity) {
        this.mStartPosition = currentPos;
        this.mBackwards = (currentPos > destination);
        if (this.mBackwards) {
            this.setup(-currentVelocity, currentPos - destination, maxAcceleration, maxVelocity, maxTime);
        }
        else {
            this.setup(currentVelocity, destination - currentPos, maxAcceleration, maxVelocity, maxTime);
        }
    }
    
    @Override
    public float getInterpolation(final float v) {
        final float y = this.calcY(v);
        this.mLastPosition = v;
        return this.mBackwards ? (this.mStartPosition - y) : (this.mStartPosition + y);
    }
    
    @Override
    public float getVelocity() {
        return this.getVelocity(this.mLastPosition);
    }
    
    private void setup(float velocity, final float distance, final float maxAcceleration, final float maxVelocity, final float maxTime) {
        if (velocity == 0.0f) {
            velocity = 1.0E-4f;
        }
        this.mStage1Velocity = velocity;
        final float min_time_to_stop = velocity / maxAcceleration;
        final float stopDistance = min_time_to_stop * velocity / 2.0f;
        if (velocity < 0.0f) {
            final float timeToZeroVelocity = -velocity / maxAcceleration;
            final float reversDistanceTraveled = timeToZeroVelocity * velocity / 2.0f;
            final float totalDistance = distance - reversDistanceTraveled;
            final float peak_v = (float)Math.sqrt(maxAcceleration * totalDistance);
            if (peak_v < maxVelocity) {
                this.mType = "backward accelerate, decelerate";
                this.mNumberOfStages = 2;
                this.mStage1Velocity = velocity;
                this.mStage2Velocity = peak_v;
                this.mStage3Velocity = 0.0f;
                this.mStage1Duration = (peak_v - velocity) / maxAcceleration;
                this.mStage2Duration = peak_v / maxAcceleration;
                this.mStage1EndPosition = (velocity + peak_v) * this.mStage1Duration / 2.0f;
                this.mStage2EndPosition = distance;
                this.mStage3EndPosition = distance;
                return;
            }
            this.mType = "backward accelerate cruse decelerate";
            this.mNumberOfStages = 3;
            this.mStage1Velocity = velocity;
            this.mStage2Velocity = maxVelocity;
            this.mStage3Velocity = maxVelocity;
            this.mStage1Duration = (maxVelocity - velocity) / maxAcceleration;
            this.mStage3Duration = maxVelocity / maxAcceleration;
            final float accDist = (velocity + maxVelocity) * this.mStage1Duration / 2.0f;
            final float decDist = maxVelocity * this.mStage3Duration / 2.0f;
            this.mStage2Duration = (distance - accDist - decDist) / maxVelocity;
            this.mStage1EndPosition = accDist;
            this.mStage2EndPosition = distance - decDist;
            this.mStage3EndPosition = distance;
        }
        else {
            if (stopDistance >= distance) {
                this.mType = "hard stop";
                final float time = 2.0f * distance / velocity;
                this.mNumberOfStages = 1;
                this.mStage1Velocity = velocity;
                this.mStage2Velocity = 0.0f;
                this.mStage1EndPosition = distance;
                this.mStage1Duration = time;
                return;
            }
            final float distance_before_break = distance - stopDistance;
            final float cruseTime = distance_before_break / velocity;
            if (cruseTime + min_time_to_stop < maxTime) {
                this.mType = "cruse decelerate";
                this.mNumberOfStages = 2;
                this.mStage1Velocity = velocity;
                this.mStage2Velocity = velocity;
                this.mStage3Velocity = 0.0f;
                this.mStage1EndPosition = distance_before_break;
                this.mStage2EndPosition = distance;
                this.mStage1Duration = cruseTime;
                this.mStage2Duration = velocity / maxAcceleration;
                return;
            }
            final float peak_v2 = (float)Math.sqrt(maxAcceleration * distance + velocity * velocity / 2.0f);
            this.mStage1Duration = (peak_v2 - velocity) / maxAcceleration;
            this.mStage2Duration = peak_v2 / maxAcceleration;
            if (peak_v2 < maxVelocity) {
                this.mType = "accelerate decelerate";
                this.mNumberOfStages = 2;
                this.mStage1Velocity = velocity;
                this.mStage2Velocity = peak_v2;
                this.mStage3Velocity = 0.0f;
                this.mStage1Duration = (peak_v2 - velocity) / maxAcceleration;
                this.mStage2Duration = peak_v2 / maxAcceleration;
                this.mStage1EndPosition = (velocity + peak_v2) * this.mStage1Duration / 2.0f;
                this.mStage2EndPosition = distance;
                return;
            }
            this.mType = "accelerate cruse decelerate";
            this.mNumberOfStages = 3;
            this.mStage1Velocity = velocity;
            this.mStage2Velocity = maxVelocity;
            this.mStage3Velocity = maxVelocity;
            this.mStage1Duration = (maxVelocity - velocity) / maxAcceleration;
            this.mStage3Duration = maxVelocity / maxAcceleration;
            final float accDist2 = (velocity + maxVelocity) * this.mStage1Duration / 2.0f;
            final float decDist2 = maxVelocity * this.mStage3Duration / 2.0f;
            this.mStage2Duration = (distance - accDist2 - decDist2) / maxVelocity;
            this.mStage1EndPosition = accDist2;
            this.mStage2EndPosition = distance - decDist2;
            this.mStage3EndPosition = distance;
        }
    }
}
