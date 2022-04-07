package com.smart.library.support.constraint;

import android.graphics.*;
import android.view.*;
import java.util.*;
import java.lang.reflect.*;
import android.graphics.drawable.*;

import com.smart.library.support.R;
import com.smart.library.support.constraint.motion.*;
import android.content.*;
import org.xmlpull.v1.*;
import android.util.*;
import android.content.res.*;

public class ConstraintAttribute
{
    private static final String TAG = "TransitionLayout";
    String mName;
    private AttributeType mType;
    private int mIntegerValue;
    private float mFloatValue;
    private String mStringValue;
    boolean mBooleanValue;
    private int mColorValue;
    
    public AttributeType getType() {
        return this.mType;
    }
    
    public void setFloatValue(final float value) {
        this.mFloatValue = value;
    }
    
    public void setColorValue(final int value) {
        this.mColorValue = value;
    }
    
    public void setIntValue(final int value) {
        this.mIntegerValue = value;
    }
    
    public void setStringValue(final String value) {
        this.mStringValue = value;
    }
    
    public int noOfInterpValues() {
        switch (this.mType) {
            case COLOR_TYPE:
            case COLOR_DRAWABLE_TYPE: {
                return 4;
            }
            default: {
                return 1;
            }
        }
    }
    
    public float getValueToInterpolate() {
        switch (this.mType) {
            case INT_TYPE: {
                return this.mIntegerValue;
            }
            case FLOAT_TYPE: {
                return this.mFloatValue;
            }
            case COLOR_TYPE:
            case COLOR_DRAWABLE_TYPE: {
                throw new RuntimeException("Color does not have a single color to interpolate");
            }
            case STRING_TYPE: {
                throw new RuntimeException("Cannot interpolate String");
            }
            case BOOLEAN_TYPE: {
                return this.mBooleanValue ? 0.0f : 1.0f;
            }
            case DIMENSION_TYPE: {
                return this.mFloatValue;
            }
            default: {
                return Float.NaN;
            }
        }
    }
    
    public void getValuesToInterpolate(final float[] ret) {
        switch (this.mType) {
            case INT_TYPE: {
                ret[0] = this.mIntegerValue;
                break;
            }
            case FLOAT_TYPE: {
                ret[0] = this.mFloatValue;
                break;
            }
            case COLOR_TYPE:
            case COLOR_DRAWABLE_TYPE: {
                final int a = 0xFF & this.mColorValue >> 24;
                final int r = 0xFF & this.mColorValue >> 16;
                final int g = 0xFF & this.mColorValue >> 8;
                final int b = 0xFF & this.mColorValue;
                final float f_r = (float)Math.pow(r / 255.0f, 2.2);
                final float f_g = (float)Math.pow(g / 255.0f, 2.2);
                final float f_b = (float)Math.pow(b / 255.0f, 2.2);
                ret[0] = f_r;
                ret[1] = f_g;
                ret[2] = f_b;
                ret[3] = a / 255.0f;
                break;
            }
            case STRING_TYPE: {
                throw new RuntimeException("Color does not have a single color to interpolate");
            }
            case BOOLEAN_TYPE: {
                ret[0] = (this.mBooleanValue ? 0.0f : 1.0f);
                break;
            }
            case DIMENSION_TYPE: {
                ret[0] = this.mFloatValue;
                break;
            }
        }
    }
    
    public void setValue(final float[] value) {
        switch (this.mType) {
            case INT_TYPE: {
                this.mIntegerValue = (int)value[0];
                break;
            }
            case FLOAT_TYPE: {
                this.mFloatValue = value[0];
                break;
            }
            case COLOR_TYPE:
            case COLOR_DRAWABLE_TYPE: {
                this.mColorValue = Color.HSVToColor(value);
                this.mColorValue = ((this.mColorValue & 0xFFFFFF) | clamp((int)(255.0f * value[3])) << 24);
                break;
            }
            case STRING_TYPE: {
                throw new RuntimeException("Color does not have a single color to interpolate");
            }
            case BOOLEAN_TYPE: {
                this.mBooleanValue = (value[0] > 0.5);
                break;
            }
            case DIMENSION_TYPE: {
                this.mFloatValue = value[0];
                break;
            }
        }
    }
    
    public boolean diff(final ConstraintAttribute constraintAttribute) {
        if (constraintAttribute == null || this.mType != constraintAttribute.mType) {
            return false;
        }
        switch (this.mType) {
            case INT_TYPE: {
                return this.mIntegerValue == constraintAttribute.mIntegerValue;
            }
            case FLOAT_TYPE: {
                return this.mFloatValue == constraintAttribute.mFloatValue;
            }
            case COLOR_TYPE:
            case COLOR_DRAWABLE_TYPE: {
                return this.mColorValue == constraintAttribute.mColorValue;
            }
            case STRING_TYPE: {
                return this.mIntegerValue == constraintAttribute.mIntegerValue;
            }
            case BOOLEAN_TYPE: {
                return this.mBooleanValue == constraintAttribute.mBooleanValue;
            }
            case DIMENSION_TYPE: {
                return this.mFloatValue == constraintAttribute.mFloatValue;
            }
            default: {
                return false;
            }
        }
    }
    
    public ConstraintAttribute(final String name, final AttributeType attributeType) {
        this.mName = name;
        this.mType = attributeType;
    }
    
    public ConstraintAttribute(final String name, final AttributeType attributeType, final Object value) {
        this.mName = name;
        this.mType = attributeType;
        this.setValue(value);
    }
    
    public ConstraintAttribute(final ConstraintAttribute source, final Object value) {
        this.mName = source.mName;
        this.mType = source.mType;
        this.setValue(value);
    }
    
    public void setValue(final Object value) {
        switch (this.mType) {
            case INT_TYPE: {
                this.mIntegerValue = (int)value;
                break;
            }
            case FLOAT_TYPE: {
                this.mFloatValue = (float)value;
                break;
            }
            case COLOR_TYPE:
            case COLOR_DRAWABLE_TYPE: {
                this.mColorValue = (int)value;
                break;
            }
            case STRING_TYPE: {
                this.mStringValue = (String)value;
                break;
            }
            case BOOLEAN_TYPE: {
                this.mBooleanValue = (boolean)value;
                break;
            }
            case DIMENSION_TYPE: {
                this.mFloatValue = (float)value;
                break;
            }
        }
    }
    
    public static HashMap<String, ConstraintAttribute> extractAttributes(final HashMap<String, ConstraintAttribute> base, final View view) {
        final HashMap<String, ConstraintAttribute> ret = new HashMap<String, ConstraintAttribute>();
        final Class<? extends View> viewClass = view.getClass();
        for (final String name : base.keySet()) {
            final ConstraintAttribute constraintAttribute = base.get(name);
            try {
                if (name.equals("BackgroundColor")) {
                    final ColorDrawable viewColor = (ColorDrawable)view.getBackground();
                    final Object val = viewColor.getColor();
                    ret.put(name, new ConstraintAttribute(constraintAttribute, val));
                }
                else {
                    final Method method = viewClass.getMethod("getMap" + name, (Class<?>[])new Class[0]);
                    final Object val = method.invoke(view, new Object[0]);
                    ret.put(name, new ConstraintAttribute(constraintAttribute, val));
                }
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
            catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
        return ret;
    }
    
    public static void setAttributes(final View view, final HashMap<String, ConstraintAttribute> map) {
        final Class<? extends View> viewClass = view.getClass();
        for (final String name : map.keySet()) {
            final ConstraintAttribute constraintAttribute = map.get(name);
            final String methodName = "set" + name;
            try {
                switch (constraintAttribute.mType) {
                    case INT_TYPE: {
                        final Method method = viewClass.getMethod(methodName, Integer.TYPE);
                        method.invoke(view, constraintAttribute.mIntegerValue);
                        continue;
                    }
                    case FLOAT_TYPE: {
                        final Method method = viewClass.getMethod(methodName, Float.TYPE);
                        method.invoke(view, constraintAttribute.mFloatValue);
                        continue;
                    }
                    case COLOR_DRAWABLE_TYPE: {
                        final Method method = viewClass.getMethod(methodName, Drawable.class);
                        final ColorDrawable drawable = new ColorDrawable();
                        drawable.setColor(constraintAttribute.mColorValue);
                        method.invoke(view, drawable);
                        continue;
                    }
                    case COLOR_TYPE: {
                        final Method method = viewClass.getMethod(methodName, Integer.TYPE);
                        method.invoke(view, constraintAttribute.mColorValue);
                        continue;
                    }
                    case STRING_TYPE: {
                        final Method method = viewClass.getMethod(methodName, CharSequence.class);
                        method.invoke(view, constraintAttribute.mStringValue);
                        continue;
                    }
                    case BOOLEAN_TYPE: {
                        final Method method = viewClass.getMethod(methodName, Boolean.TYPE);
                        method.invoke(view, constraintAttribute.mBooleanValue);
                        continue;
                    }
                    case DIMENSION_TYPE: {
                        final Method method = viewClass.getMethod(methodName, Float.TYPE);
                        method.invoke(view, constraintAttribute.mFloatValue);
                        continue;
                    }
                }
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
            catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
    }
    
    private static int clamp(int c) {
        final int N = 255;
        c &= ~(c >> 31);
        c -= N;
        c &= c >> 31;
        c += N;
        return c;
    }
    
    public void setInterpolatedValue(final View view, final float[] value) {
        final Class<? extends View> viewClass = view.getClass();
        final String methodName = "set" + this.mName;
        try {
            switch (this.mType) {
                case INT_TYPE: {
                    final Method method = viewClass.getMethod(methodName, Integer.TYPE);
                    method.invoke(view, (int)value[0]);
                    break;
                }
                case FLOAT_TYPE: {
                    final Method method = viewClass.getMethod(methodName, Float.TYPE);
                    method.invoke(view, value[0]);
                    break;
                }
                case COLOR_DRAWABLE_TYPE: {
                    final Method method = viewClass.getMethod(methodName, Drawable.class);
                    final int r = clamp((int)((float)Math.pow(value[0], 0.45454545454545453) * 255.0f));
                    final int g = clamp((int)((float)Math.pow(value[1], 0.45454545454545453) * 255.0f));
                    final int b = clamp((int)((float)Math.pow(value[2], 0.45454545454545453) * 255.0f));
                    final int a = clamp((int)(value[3] * 255.0f));
                    final int color = a << 24 | r << 16 | g << 8 | b;
                    final ColorDrawable drawable = new ColorDrawable();
                    drawable.setColor(color);
                    method.invoke(view, drawable);
                    break;
                }
                case COLOR_TYPE: {
                    final Method method = viewClass.getMethod(methodName, Integer.TYPE);
                    final int r = clamp((int)((float)Math.pow(value[0], 0.45454545454545453) * 255.0f));
                    final int g = clamp((int)((float)Math.pow(value[1], 0.45454545454545453) * 255.0f));
                    final int b = clamp((int)((float)Math.pow(value[2], 0.45454545454545453) * 255.0f));
                    final int a = clamp((int)(value[3] * 255.0f));
                    final int color = a << 24 | r << 16 | g << 8 | b;
                    method.invoke(view, color);
                    break;
                }
                case STRING_TYPE: {
                    throw new RuntimeException("unable to interpolate strings " + this.mName);
                }
                case BOOLEAN_TYPE: {
                    final Method method = viewClass.getMethod(methodName, Boolean.TYPE);
                    method.invoke(view, value[0] > 0.5f);
                    break;
                }
                case DIMENSION_TYPE: {
                    final Method method = viewClass.getMethod(methodName, Float.TYPE);
                    method.invoke(view, value[0]);
                    break;
                }
            }
        }
        catch (NoSuchMethodException e) {
            Log.e("TransitionLayout", "no method " + methodName + "on View \"" + Debug.getName(view) + "\"");
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            Log.e("TransitionLayout", "cannot access method " + methodName + "on View \"" + Debug.getName(view) + "\"");
            e2.printStackTrace();
        }
        catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
    }
    
    public static void parse(final Context context, final XmlPullParser parser, final HashMap<String, ConstraintAttribute> custom) {
        final AttributeSet attributeSet = Xml.asAttributeSet(parser);
        final TypedArray a = context.obtainStyledAttributes(attributeSet, R.styleable.CustomAttribute);
        String name = null;
        Object value = null;
        AttributeType type = null;
        for (int N = a.getIndexCount(), i = 0; i < N; ++i) {
            final int attr = a.getIndex(i);
            if (attr == R.styleable.CustomAttribute_attributeName) {
                name = a.getString(attr);
                if (name != null && name.length() > 0) {
                    name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
                }
            }
            else if (attr == R.styleable.CustomAttribute_customBoolean) {
                value = a.getBoolean(attr, false);
                type = AttributeType.BOOLEAN_TYPE;
            }
            else if (attr == R.styleable.CustomAttribute_customColorValue) {
                type = AttributeType.COLOR_TYPE;
                value = a.getColor(attr, 0);
            }
            else if (attr == R.styleable.CustomAttribute_customColorDrawableValue) {
                type = AttributeType.COLOR_DRAWABLE_TYPE;
                value = a.getColor(attr, 0);
            }
            else if (attr == R.styleable.CustomAttribute_customPixelDimension) {
                type = AttributeType.DIMENSION_TYPE;
                value = TypedValue.applyDimension(1, a.getDimension(attr, 0.0f), context.getResources().getDisplayMetrics());
            }
            else if (attr == R.styleable.CustomAttribute_customDimension) {
                type = AttributeType.DIMENSION_TYPE;
                value = a.getDimension(attr, 0.0f);
            }
            else if (attr == R.styleable.CustomAttribute_customFloatValue) {
                type = AttributeType.FLOAT_TYPE;
                value = a.getFloat(attr, Float.NaN);
            }
            else if (attr == R.styleable.CustomAttribute_customIntegerValue) {
                type = AttributeType.INT_TYPE;
                value = a.getInteger(attr, -1);
            }
            else if (attr == R.styleable.CustomAttribute_customStringValue) {
                type = AttributeType.STRING_TYPE;
                value = a.getString(attr);
            }
        }
        if (name != null && value != null) {
            custom.put(name, new ConstraintAttribute(name, type, value));
        }
        a.recycle();
    }
    
    public enum AttributeType
    {
        INT_TYPE, 
        FLOAT_TYPE, 
        COLOR_TYPE, 
        COLOR_DRAWABLE_TYPE, 
        STRING_TYPE, 
        BOOLEAN_TYPE, 
        DIMENSION_TYPE;
    }
}
