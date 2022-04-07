package com.smart.library.support.constraint.motion;

import android.util.*;
import android.content.*;
import java.lang.reflect.*;
import android.view.*;

public class Debug
{
    public static void logStack(final String tag, final String msg, int n) {
        final StackTraceElement[] st = new Throwable().getStackTrace();
        String s = " ";
        n = Math.min(n, st.length - 1);
        for (int i = 1; i <= n; ++i) {
            final StackTraceElement ste = st[i];
            final String stack = ".(" + st[i].getFileName() + ":" + st[i].getLineNumber() + ") ";
            s += " ";
            Log.v(tag, msg + s + stack + s);
        }
    }
    
    public static void printStack(final String msg, int n) {
        final StackTraceElement[] st = new Throwable().getStackTrace();
        String s = " ";
        n = Math.min(n, st.length - 1);
        for (int i = 1; i <= n; ++i) {
            final StackTraceElement ste = st[i];
            final String stack = ".(" + st[i].getFileName() + ":" + st[i].getLineNumber() + ") ";
            s += " ";
            System.out.println(msg + s + stack + s);
        }
    }
    
    public static String getName(final View view) {
        try {
            final Context context = view.getContext();
            return context.getResources().getResourceEntryName(view.getId());
        }
        catch (Exception ex) {
            return "UNKNOWN";
        }
    }
    
    public static void dumpPoc(final Object obj) {
        final StackTraceElement s = new Throwable().getStackTrace()[1];
        final String loc = ".(" + s.getFileName() + ":" + s.getLineNumber() + ")";
        final Class c = obj.getClass();
        System.out.println(loc + "------------- " + c.getName() + " --------------------");
        final Field[] declaredFields = c.getFields();
        for (int i = 0; i < declaredFields.length; ++i) {
            final Field declaredField = declaredFields[i];
            try {
                final Object value = declaredField.get(obj);
                if (declaredField.getName().startsWith("layout_constraint")) {
                    if (!(value instanceof Integer) || !value.toString().equals("-1")) {
                        if (!(value instanceof Integer) || !value.toString().equals("0")) {
                            if (!(value instanceof Float) || !value.toString().equals("1.0")) {
                                if (!(value instanceof Float) || !value.toString().equals("0.5")) {
                                    System.out.println(loc + "    " + declaredField.getName() + " " + value);
                                }
                            }
                        }
                    }
                }
            }
            catch (IllegalAccessException ex) {}
        }
        System.out.println(loc + "------------- " + c.getSimpleName() + " --------------------");
    }
    
    public static String getName(final Context context, final int id) {
        try {
            if (id != -1) {
                return context.getResources().getResourceEntryName(id);
            }
            return "UNKNOWN";
        }
        catch (Exception ex) {
            return "UNKNOWN";
        }
    }
    
    public static String getName(final Context context, final int[] id) {
        try {
            String str = "";
            for (int i = 0; i < id.length; ++i) {
                str += ((i == 0) ? "" : " ");
                str += context.getResources().getResourceEntryName(id[i]);
            }
            return str;
        }
        catch (Exception ex) {
            return "UNKNOWN";
        }
    }
    
    public static String getState(final MotionLayout layout, final int stateId) {
        if (stateId == -1) {
            return "UNDEFINED";
        }
        final Context context = layout.getContext();
        return context.getResources().getResourceEntryName(stateId);
    }
    
    public static String getActionType(final MotionEvent event) {
        final int type = event.getAction();
        final Field[] fields = MotionEvent.class.getFields();
        for (int i = 0; i < fields.length; ++i) {
            final Field field = fields[i];
            try {
                if (Modifier.isStatic(field.getModifiers()) && field.getType().equals(Integer.TYPE) && field.getInt(null) == type) {
                    return field.getName();
                }
            }
            catch (IllegalAccessException ex) {}
        }
        return "---";
    }
    
    public static String getLocation() {
        final StackTraceElement s = new Throwable().getStackTrace()[1];
        return ".(" + s.getFileName() + ":" + s.getLineNumber() + ")";
    }
    
    public static String getLocation2() {
        final StackTraceElement s = new Throwable().getStackTrace()[2];
        return ".(" + s.getFileName() + ":" + s.getLineNumber() + ")";
    }
    
    public static String getCallFrom(final int n) {
        final StackTraceElement s = new Throwable().getStackTrace()[2 + n];
        return ".(" + s.getFileName() + ":" + s.getLineNumber() + ")";
    }
    
    public static void dumpLayoutParams(final ViewGroup layout, final String str) {
        final StackTraceElement s = new Throwable().getStackTrace()[1];
        final String loc = ".(" + s.getFileName() + ":" + s.getLineNumber() + ") " + str + "  ";
        final int n = layout.getChildCount();
        System.out.println(str + " children " + n);
        for (int i = 0; i < n; ++i) {
            final View v = layout.getChildAt(i);
            System.out.println(loc + "     " + getName(v));
            final ViewGroup.LayoutParams param = v.getLayoutParams();
            final Field[] declaredFields = param.getClass().getFields();
            for (int k = 0; k < declaredFields.length; ++k) {
                final Field declaredField = declaredFields[k];
                try {
                    final Object value = declaredField.get(param);
                    final String name = declaredField.getName();
                    if (name.contains("To")) {
                        if (!value.toString().equals("-1")) {
                            System.out.println(loc + "       " + declaredField.getName() + " " + value);
                        }
                    }
                }
                catch (IllegalAccessException ex) {}
            }
        }
    }
    
    public static void dumpLayoutParams(final ViewGroup.LayoutParams param, final String str) {
        final StackTraceElement s = new Throwable().getStackTrace()[1];
        final String loc = ".(" + s.getFileName() + ":" + s.getLineNumber() + ") " + str + "  ";
        System.out.println(" >>>>>>>>>>>>>>>>>>. dump " + loc + "  " + param.getClass().getName());
        final Field[] declaredFields = param.getClass().getFields();
        for (int k = 0; k < declaredFields.length; ++k) {
            final Field declaredField = declaredFields[k];
            try {
                final Object value = declaredField.get(param);
                final String name = declaredField.getName();
                if (name.contains("To")) {
                    if (!value.toString().equals("-1")) {
                        System.out.println(loc + "       " + name + " " + value);
                    }
                }
            }
            catch (IllegalAccessException ex) {}
        }
        System.out.println(" <<<<<<<<<<<<<<<<< dump " + loc);
    }
}
