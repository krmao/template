package com.smart.library.support.constraint.solver;

import com.smart.library.support.constraint.solver.ArrayRow;

import java.util.*;

public class SolverVariable
{
    private static final boolean INTERNAL_DEBUG = false;
    public static final int STRENGTH_NONE = 0;
    public static final int STRENGTH_LOW = 1;
    public static final int STRENGTH_MEDIUM = 2;
    public static final int STRENGTH_HIGH = 3;
    public static final int STRENGTH_HIGHEST = 4;
    public static final int STRENGTH_EQUALITY = 5;
    public static final int STRENGTH_FIXED = 6;
    public static final int STRENGTH_BARRIER = 7;
    private static int uniqueSlackId;
    private static int uniqueErrorId;
    private static int uniqueUnrestrictedId;
    private static int uniqueConstantId;
    private static int uniqueId;
    private String mName;
    public int id;
    int definitionId;
    public int strength;
    public float computedValue;
    static final int MAX_STRENGTH = 7;
    float[] strengthVector;
    Type mType;
    com.smart.library.support.constraint.solver.ArrayRow[] mClientEquations;
    int mClientEquationsCount;
    public int usageInRowCount;
    
    static void increaseErrorId() {
        ++SolverVariable.uniqueErrorId;
    }
    
    private static String getUniqueName(final Type type, final String prefix) {
        if (prefix != null) {
            return prefix + SolverVariable.uniqueErrorId;
        }
        switch (type) {
            case UNRESTRICTED: {
                return "U" + ++SolverVariable.uniqueUnrestrictedId;
            }
            case CONSTANT: {
                return "C" + ++SolverVariable.uniqueConstantId;
            }
            case SLACK: {
                return "S" + ++SolverVariable.uniqueSlackId;
            }
            case ERROR: {
                return "e" + ++SolverVariable.uniqueErrorId;
            }
            case UNKNOWN: {
                return "V" + ++SolverVariable.uniqueId;
            }
            default: {
                throw new AssertionError((Object)type.name());
            }
        }
    }
    
    public SolverVariable(final String name, final Type type) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[7];
        this.mClientEquations = new com.smart.library.support.constraint.solver.ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.mName = name;
        this.mType = type;
    }
    
    public SolverVariable(final Type type, final String prefix) {
        this.id = -1;
        this.definitionId = -1;
        this.strength = 0;
        this.strengthVector = new float[7];
        this.mClientEquations = new com.smart.library.support.constraint.solver.ArrayRow[8];
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
        this.mType = type;
    }
    
    void clearStrengths() {
        for (int i = 0; i < 7; ++i) {
            this.strengthVector[i] = 0.0f;
        }
    }
    
    String strengthsToString() {
        String representation = this + "[";
        boolean negative = false;
        boolean empty = true;
        for (int j = 0; j < this.strengthVector.length; ++j) {
            representation += this.strengthVector[j];
            if (this.strengthVector[j] > 0.0f) {
                negative = false;
            }
            else if (this.strengthVector[j] < 0.0f) {
                negative = true;
            }
            if (this.strengthVector[j] != 0.0f) {
                empty = false;
            }
            if (j < this.strengthVector.length - 1) {
                representation += ", ";
            }
            else {
                representation += "] ";
            }
        }
        if (negative) {
            representation += " (-)";
        }
        if (empty) {
            representation += " (*)";
        }
        return representation;
    }
    
    public final void addToRow(final com.smart.library.support.constraint.solver.ArrayRow row) {
        for (int i = 0; i < this.mClientEquationsCount; ++i) {
            if (this.mClientEquations[i] == row) {
                return;
            }
        }
        if (this.mClientEquationsCount >= this.mClientEquations.length) {
            this.mClientEquations = Arrays.copyOf(this.mClientEquations, this.mClientEquations.length * 2);
        }
        this.mClientEquations[this.mClientEquationsCount] = row;
        ++this.mClientEquationsCount;
    }
    
    public final void removeFromRow(final com.smart.library.support.constraint.solver.ArrayRow row) {
        for (int count = this.mClientEquationsCount, i = 0; i < count; ++i) {
            if (this.mClientEquations[i] == row) {
                for (int j = 0; j < count - i - 1; ++j) {
                    this.mClientEquations[i + j] = this.mClientEquations[i + j + 1];
                }
                --this.mClientEquationsCount;
                return;
            }
        }
    }
    
    public final void updateReferencesWithNewDefinition(final ArrayRow definition) {
        for (int count = this.mClientEquationsCount, i = 0; i < count; ++i) {
            this.mClientEquations[i].variables.updateFromRow(this.mClientEquations[i], definition, false);
        }
        this.mClientEquationsCount = 0;
    }
    
    public void reset() {
        this.mName = null;
        this.mType = Type.UNKNOWN;
        this.strength = 0;
        this.id = -1;
        this.definitionId = -1;
        this.computedValue = 0.0f;
        this.mClientEquationsCount = 0;
        this.usageInRowCount = 0;
    }
    
    public String getName() {
        return this.mName;
    }
    
    public void setName(final String name) {
        this.mName = name;
    }
    
    public void setType(final Type type, final String prefix) {
        this.mType = type;
    }
    
    @Override
    public String toString() {
        String result = "";
        result += this.mName;
        return result;
    }
    
    static {
        SolverVariable.uniqueSlackId = 1;
        SolverVariable.uniqueErrorId = 1;
        SolverVariable.uniqueUnrestrictedId = 1;
        SolverVariable.uniqueConstantId = 1;
        SolverVariable.uniqueId = 1;
    }
    
    public enum Type
    {
        UNRESTRICTED, 
        CONSTANT, 
        SLACK, 
        ERROR, 
        UNKNOWN;
    }
}
