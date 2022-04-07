package com.smart.library.support.constraint.motion;

import java.util.*;

public class KeyCache
{
    HashMap<Object, HashMap<String, float[]>> map;
    
    public KeyCache() {
        this.map = new HashMap<Object, HashMap<String, float[]>>();
    }
    
    void setFloatValue(final Object view, final String type, final int element, final float value) {
        if (!this.map.containsKey(view)) {
            final HashMap<String, float[]> array = new HashMap<String, float[]>();
            final float[] vArray = new float[element + 1];
            vArray[element] = value;
            array.put(type, vArray);
            this.map.put(view, array);
        }
        else {
            final HashMap<String, float[]> array = this.map.get(view);
            if (!array.containsKey(type)) {
                final float[] vArray = new float[element + 1];
                vArray[element] = value;
                array.put(type, vArray);
                this.map.put(view, array);
            }
            else {
                float[] vArray = array.get(type);
                if (vArray.length <= element) {
                    vArray = Arrays.copyOf(vArray, element + 1);
                }
                vArray[element] = value;
                array.put(type, vArray);
            }
        }
    }
    
    float getFloatValue(final Object view, final String type, final int element) {
        if (!this.map.containsKey(view)) {
            return Float.NaN;
        }
        final HashMap<String, float[]> array = this.map.get(view);
        if (!array.containsKey(type)) {
            return Float.NaN;
        }
        final float[] vArray = array.get(type);
        if (vArray.length > element) {
            return vArray[element];
        }
        return Float.NaN;
    }
}
