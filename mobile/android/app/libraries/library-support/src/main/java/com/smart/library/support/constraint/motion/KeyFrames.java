package com.smart.library.support.constraint.motion;

import java.lang.reflect.*;
import android.content.*;
import android.util.*;
import org.xmlpull.v1.*;
import java.io.*;
import com.smart.library.support.constraint.*;
import java.util.*;

public class KeyFrames
{
    public static final int UNSET = -1;
    private HashMap<Integer, ArrayList<Key>> mFramesMap;
    static HashMap<String, Constructor<? extends Key>> sKeyMakers;
    private static final String TAG = "KeyFrames";
    
    private void addKey(final Key key) {
        if (!this.mFramesMap.containsKey(key.mTargetId)) {
            this.mFramesMap.put(key.mTargetId, new ArrayList<Key>());
        }
        this.mFramesMap.get(key.mTargetId).add(key);
    }
    
    public KeyFrames(final Context context, final XmlPullParser parser) {
        this.mFramesMap = new HashMap<Integer, ArrayList<Key>>();
        String tagName = null;
        try {
            Key key = null;
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 2: {
                        tagName = parser.getName();
                        if (KeyFrames.sKeyMakers.containsKey(tagName)) {
                            try {
                                key = (Key)KeyFrames.sKeyMakers.get(tagName).newInstance(new Object[0]);
                                key.load(context, Xml.asAttributeSet(parser));
                                this.addKey(key);
                            }
                            catch (Exception e) {
                                Log.e("KeyFrames", "unable to create ", (Throwable)e);
                            }
                            break;
                        }
                        if (tagName.equalsIgnoreCase("CustomAttribute") && key != null && key.mCustomConstraints != null) {
                            ConstraintAttribute.parse(context, parser, key.mCustomConstraints);
                            break;
                        }
                        break;
                    }
                    case 3: {
                        if ("KeyFrameSet".equals(parser.getName())) {
                            return;
                        }
                        break;
                    }
                }
            }
        }
        catch (XmlPullParserException e2) {
            e2.printStackTrace();
        }
        catch (IOException e3) {
            e3.printStackTrace();
        }
    }
    
    public void addFrames(final MotionController motionController) {
        ArrayList<Key> list = this.mFramesMap.get(motionController.mId);
        if (list != null) {
            motionController.addKeys(list);
        }
        list = this.mFramesMap.get(-1);
        if (list != null) {
            for (final Key key : list) {
                final String tag = ((ConstraintLayout.LayoutParams)motionController.mView.getLayoutParams()).constraintTag;
                if (key.matches(tag)) {
                    motionController.addKey(key);
                }
            }
        }
    }
    
    static String name(final int viewId, final Context context) {
        return context.getResources().getResourceEntryName(viewId);
    }
    
    public Set<Integer> getKeys() {
        return this.mFramesMap.keySet();
    }
    
    public ArrayList<Key> getKeyFramesForView(final int id) {
        return this.mFramesMap.get(id);
    }
    
    static {
        KeyFrames.sKeyMakers = new HashMap<String, Constructor<? extends Key>>();
        try {
            KeyFrames.sKeyMakers.put("KeyAttribute", KeyAttributes.class.getConstructor((Class<?>[])new Class[0]));
            KeyFrames.sKeyMakers.put("KeyPosition", KeyPosition.class.getConstructor((Class<?>[])new Class[0]));
            KeyFrames.sKeyMakers.put("KeyCycle", KeyCycle.class.getConstructor((Class<?>[])new Class[0]));
            KeyFrames.sKeyMakers.put("KeyTimeCycle", KeyTimeCycle.class.getConstructor((Class<?>[])new Class[0]));
            KeyFrames.sKeyMakers.put("KeyTrigger", KeyTrigger.class.getConstructor((Class<?>[])new Class[0]));
        }
        catch (NoSuchMethodException e) {
            Log.e("KeyFrames", "unable to load", (Throwable)e);
        }
    }
}
