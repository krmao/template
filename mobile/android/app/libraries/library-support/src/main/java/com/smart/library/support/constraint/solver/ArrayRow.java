package com.smart.library.support.constraint.solver;

import com.smart.library.support.constraint.solver.ArrayLinkedVariables;
import com.smart.library.support.constraint.solver.Cache;
import com.smart.library.support.constraint.solver.LinearSystem;
import com.smart.library.support.constraint.solver.SolverVariable;

public class ArrayRow implements LinearSystem.Row
{
    private static final boolean DEBUG = false;
    SolverVariable variable;
    float constantValue;
    boolean used;
    private static final float epsilon = 0.001f;
    public final ArrayLinkedVariables variables;
    boolean isSimpleDefinition;
    
    public ArrayRow(final Cache cache) {
        this.variable = null;
        this.constantValue = 0.0f;
        this.used = false;
        this.isSimpleDefinition = false;
        this.variables = new ArrayLinkedVariables(this, cache);
    }
    
    boolean hasKeyVariable() {
        return this.variable != null && (this.variable.mType == SolverVariable.Type.UNRESTRICTED || this.constantValue >= 0.0f);
    }
    
    @Override
    public String toString() {
        return this.toReadableString();
    }
    
    String toReadableString() {
        String s = "";
        if (this.variable == null) {
            s += "0";
        }
        else {
            s += this.variable;
        }
        s += " = ";
        boolean addedVariable = false;
        if (this.constantValue != 0.0f) {
            s += this.constantValue;
            addedVariable = true;
        }
        for (int count = this.variables.currentSize, i = 0; i < count; ++i) {
            final SolverVariable v = this.variables.getVariable(i);
            if (v != null) {
                float amount = this.variables.getVariableValue(i);
                if (amount != 0.0f) {
                    final String name = v.toString();
                    if (!addedVariable) {
                        if (amount < 0.0f) {
                            s += "- ";
                            amount *= -1.0f;
                        }
                    }
                    else if (amount > 0.0f) {
                        s += " + ";
                    }
                    else {
                        s += " - ";
                        amount *= -1.0f;
                    }
                    if (amount == 1.0f) {
                        s += name;
                    }
                    else {
                        s = s + amount + " " + name;
                    }
                    addedVariable = true;
                }
            }
        }
        if (!addedVariable) {
            s += "0.0";
        }
        return s;
    }
    
    public void reset() {
        this.variable = null;
        this.variables.clear();
        this.constantValue = 0.0f;
        this.isSimpleDefinition = false;
    }
    
    boolean hasVariable(final SolverVariable v) {
        return this.variables.containsKey(v);
    }
    
    ArrayRow createRowDefinition(final SolverVariable variable, final int value) {
        this.variable = variable;
        variable.computedValue = value;
        this.constantValue = value;
        this.isSimpleDefinition = true;
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable variable, final int value) {
        if (value < 0) {
            this.constantValue = -1 * value;
            this.variables.put(variable, 1.0f);
        }
        else {
            this.constantValue = value;
            this.variables.put(variable, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowEquals(final SolverVariable variableA, final SolverVariable variableB, final int margin) {
        boolean inverse = false;
        if (margin != 0) {
            int m = margin;
            if (m < 0) {
                m *= -1;
                inverse = true;
            }
            this.constantValue = m;
        }
        if (!inverse) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
        }
        else {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
        }
        return this;
    }
    
    ArrayRow addSingleError(final SolverVariable error, final int sign) {
        this.variables.put(error, sign);
        return this;
    }
    
    public ArrayRow createRowGreaterThan(final SolverVariable variableA, final SolverVariable variableB, final SolverVariable slack, final int margin) {
        boolean inverse = false;
        if (margin != 0) {
            int m = margin;
            if (m < 0) {
                m *= -1;
                inverse = true;
            }
            this.constantValue = m;
        }
        if (!inverse) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
            this.variables.put(slack, 1.0f);
        }
        else {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
            this.variables.put(slack, -1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowGreaterThan(final SolverVariable a, final int b, final SolverVariable slack) {
        this.constantValue = b;
        this.variables.put(a, -1.0f);
        return this;
    }
    
    public ArrayRow createRowLowerThan(final SolverVariable variableA, final SolverVariable variableB, final SolverVariable slack, final int margin) {
        boolean inverse = false;
        if (margin != 0) {
            int m = margin;
            if (m < 0) {
                m *= -1;
                inverse = true;
            }
            this.constantValue = m;
        }
        if (!inverse) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
            this.variables.put(slack, -1.0f);
        }
        else {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
            this.variables.put(slack, 1.0f);
        }
        return this;
    }
    
    public ArrayRow createRowEqualMatchDimensions(final float currentWeight, final float totalWeights, final float nextWeight, final SolverVariable variableStartA, final SolverVariable variableEndA, final SolverVariable variableStartB, final SolverVariable variableEndB) {
        this.constantValue = 0.0f;
        if (totalWeights == 0.0f || currentWeight == nextWeight) {
            this.variables.put(variableStartA, 1.0f);
            this.variables.put(variableEndA, -1.0f);
            this.variables.put(variableEndB, 1.0f);
            this.variables.put(variableStartB, -1.0f);
        }
        else if (currentWeight == 0.0f) {
            this.variables.put(variableStartA, 1.0f);
            this.variables.put(variableEndA, -1.0f);
        }
        else if (nextWeight == 0.0f) {
            this.variables.put(variableStartB, 1.0f);
            this.variables.put(variableEndB, -1.0f);
        }
        else {
            final float cw = currentWeight / totalWeights;
            final float nw = nextWeight / totalWeights;
            final float w = cw / nw;
            this.variables.put(variableStartA, 1.0f);
            this.variables.put(variableEndA, -1.0f);
            this.variables.put(variableEndB, w);
            this.variables.put(variableStartB, -w);
        }
        return this;
    }
    
    public ArrayRow createRowEqualDimension(final float currentWeight, final float totalWeights, final float nextWeight, final SolverVariable variableStartA, final int marginStartA, final SolverVariable variableEndA, final int marginEndA, final SolverVariable variableStartB, final int marginStartB, final SolverVariable variableEndB, final int marginEndB) {
        if (totalWeights == 0.0f || currentWeight == nextWeight) {
            this.constantValue = -marginStartA - marginEndA + marginStartB + marginEndB;
            this.variables.put(variableStartA, 1.0f);
            this.variables.put(variableEndA, -1.0f);
            this.variables.put(variableEndB, 1.0f);
            this.variables.put(variableStartB, -1.0f);
        }
        else {
            final float cw = currentWeight / totalWeights;
            final float nw = nextWeight / totalWeights;
            final float w = cw / nw;
            this.constantValue = -marginStartA - marginEndA + w * marginStartB + w * marginEndB;
            this.variables.put(variableStartA, 1.0f);
            this.variables.put(variableEndA, -1.0f);
            this.variables.put(variableEndB, w);
            this.variables.put(variableStartB, -w);
        }
        return this;
    }
    
    ArrayRow createRowCentering(final SolverVariable variableA, final SolverVariable variableB, final int marginA, final float bias, final SolverVariable variableC, final SolverVariable variableD, final int marginB) {
        if (variableB == variableC) {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableD, 1.0f);
            this.variables.put(variableB, -2.0f);
            return this;
        }
        if (bias == 0.5f) {
            this.variables.put(variableA, 1.0f);
            this.variables.put(variableB, -1.0f);
            this.variables.put(variableC, -1.0f);
            this.variables.put(variableD, 1.0f);
            if (marginA > 0 || marginB > 0) {
                this.constantValue = -marginA + marginB;
            }
        }
        else if (bias <= 0.0f) {
            this.variables.put(variableA, -1.0f);
            this.variables.put(variableB, 1.0f);
            this.constantValue = marginA;
        }
        else if (bias >= 1.0f) {
            this.variables.put(variableC, -1.0f);
            this.variables.put(variableD, 1.0f);
            this.constantValue = marginB;
        }
        else {
            this.variables.put(variableA, 1.0f * (1.0f - bias));
            this.variables.put(variableB, -1.0f * (1.0f - bias));
            this.variables.put(variableC, -1.0f * bias);
            this.variables.put(variableD, 1.0f * bias);
            if (marginA > 0 || marginB > 0) {
                this.constantValue = -marginA * (1.0f - bias) + marginB * bias;
            }
        }
        return this;
    }
    
    public ArrayRow addError(final LinearSystem system, final int strength) {
        this.variables.put(system.createErrorVariable(strength, "ep"), 1.0f);
        this.variables.put(system.createErrorVariable(strength, "em"), -1.0f);
        return this;
    }
    
    ArrayRow createRowDimensionPercent(final SolverVariable variableA, final SolverVariable variableB, final SolverVariable variableC, final float percent) {
        this.variables.put(variableA, -1.0f);
        this.variables.put(variableB, 1.0f - percent);
        this.variables.put(variableC, percent);
        return this;
    }
    
    public ArrayRow createRowDimensionRatio(final SolverVariable variableA, final SolverVariable variableB, final SolverVariable variableC, final SolverVariable variableD, final float ratio) {
        this.variables.put(variableA, -1.0f);
        this.variables.put(variableB, 1.0f);
        this.variables.put(variableC, ratio);
        this.variables.put(variableD, -ratio);
        return this;
    }
    
    public ArrayRow createRowWithAngle(final SolverVariable at, final SolverVariable ab, final SolverVariable bt, final SolverVariable bb, final float angleComponent) {
        this.variables.put(bt, 0.5f);
        this.variables.put(bb, 0.5f);
        this.variables.put(at, -0.5f);
        this.variables.put(ab, -0.5f);
        this.constantValue = -angleComponent;
        return this;
    }
    
    int sizeInBytes() {
        int size = 0;
        if (this.variable != null) {
            size += 4;
        }
        size += 4;
        size += 4;
        size += this.variables.sizeInBytes();
        return size;
    }
    
    void ensurePositiveConstant() {
        if (this.constantValue < 0.0f) {
            this.constantValue *= -1.0f;
            this.variables.invert();
        }
    }
    
    boolean chooseSubject(final LinearSystem system) {
        boolean addedExtra = false;
        final SolverVariable pivotCandidate = this.variables.chooseSubject(system);
        if (pivotCandidate == null) {
            addedExtra = true;
        }
        else {
            this.pivot(pivotCandidate);
        }
        if (this.variables.currentSize == 0) {
            this.isSimpleDefinition = true;
        }
        return addedExtra;
    }
    
    SolverVariable pickPivot(final SolverVariable exclude) {
        return this.variables.getPivotCandidate(null, exclude);
    }
    
    void pivot(final SolverVariable v) {
        if (this.variable != null) {
            this.variables.put(this.variable, -1.0f);
            this.variable = null;
        }
        final float amount = this.variables.remove(v, true) * -1.0f;
        this.variable = v;
        if (amount == 1.0f) {
            return;
        }
        this.constantValue /= amount;
        this.variables.divideByAmount(amount);
    }
    
    @Override
    public boolean isEmpty() {
        return this.variable == null && this.constantValue == 0.0f && this.variables.currentSize == 0;
    }
    
    @Override
    public SolverVariable getPivotCandidate(final LinearSystem system, final boolean[] avoid) {
        return this.variables.getPivotCandidate(avoid, null);
    }
    
    @Override
    public void clear() {
        this.variables.clear();
        this.variable = null;
        this.constantValue = 0.0f;
    }
    
    @Override
    public void initFromRow(final LinearSystem.Row row) {
        if (row instanceof ArrayRow) {
            final ArrayRow copiedRow = (ArrayRow)row;
            this.variable = null;
            this.variables.clear();
            for (int i = 0; i < copiedRow.variables.currentSize; ++i) {
                final SolverVariable var = copiedRow.variables.getVariable(i);
                final float val = copiedRow.variables.getVariableValue(i);
                this.variables.add(var, val, true);
            }
        }
    }
    
    @Override
    public void addError(final SolverVariable error) {
        float weight = 1.0f;
        if (error.strength == 1) {
            weight = 1.0f;
        }
        else if (error.strength == 2) {
            weight = 1000.0f;
        }
        else if (error.strength == 3) {
            weight = 1000000.0f;
        }
        else if (error.strength == 4) {
            weight = 1.0E9f;
        }
        else if (error.strength == 5) {
            weight = 1.0E12f;
        }
        this.variables.put(error, weight);
    }
    
    @Override
    public SolverVariable getKey() {
        return this.variable;
    }
}
