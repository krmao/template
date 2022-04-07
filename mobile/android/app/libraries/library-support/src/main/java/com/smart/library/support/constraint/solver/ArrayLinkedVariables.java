package com.smart.library.support.constraint.solver;

import com.smart.library.support.constraint.solver.ArrayRow;
import com.smart.library.support.constraint.solver.Cache;
import com.smart.library.support.constraint.solver.LinearSystem;
import com.smart.library.support.constraint.solver.SolverVariable;

import java.util.*;

public class ArrayLinkedVariables
{
    private static final boolean DEBUG = false;
    private static final int NONE = -1;
    private static final boolean FULL_NEW_CHECK = false;
    int currentSize;
    private final com.smart.library.support.constraint.solver.ArrayRow mRow;
    private final com.smart.library.support.constraint.solver.Cache mCache;
    private int ROW_SIZE;
    private com.smart.library.support.constraint.solver.SolverVariable candidate;
    private int[] mArrayIndices;
    private int[] mArrayNextIndices;
    private float[] mArrayValues;
    private int mHead;
    private int mLast;
    private boolean mDidFillOnce;
    
    ArrayLinkedVariables(final com.smart.library.support.constraint.solver.ArrayRow arrayRow, final Cache cache) {
        this.currentSize = 0;
        this.ROW_SIZE = 8;
        this.candidate = null;
        this.mArrayIndices = new int[this.ROW_SIZE];
        this.mArrayNextIndices = new int[this.ROW_SIZE];
        this.mArrayValues = new float[this.ROW_SIZE];
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.mRow = arrayRow;
        this.mCache = cache;
    }
    
    public final void put(final com.smart.library.support.constraint.solver.SolverVariable variable, final float value) {
        if (value == 0.0f) {
            this.remove(variable, true);
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[this.mHead] = value;
            this.mArrayIndices[this.mHead] = variable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++variable.usageInRowCount;
            variable.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
                if (this.mLast >= this.mArrayIndices.length) {
                    this.mDidFillOnce = true;
                    this.mLast = this.mArrayIndices.length - 1;
                }
            }
            return;
        }
        int current = this.mHead;
        int previous = -1;
        for (int counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            if (this.mArrayIndices[current] == variable.id) {
                this.mArrayValues[current] = value;
                return;
            }
            if (this.mArrayIndices[current] < variable.id) {
                previous = current;
            }
        }
        int availableIndice = this.mLast + 1;
        if (this.mDidFillOnce) {
            if (this.mArrayIndices[this.mLast] == -1) {
                availableIndice = this.mLast;
            }
            else {
                availableIndice = this.mArrayIndices.length;
            }
        }
        if (availableIndice >= this.mArrayIndices.length && this.currentSize < this.mArrayIndices.length) {
            for (int i = 0; i < this.mArrayIndices.length; ++i) {
                if (this.mArrayIndices[i] == -1) {
                    availableIndice = i;
                    break;
                }
            }
        }
        if (availableIndice >= this.mArrayIndices.length) {
            availableIndice = this.mArrayIndices.length;
            this.ROW_SIZE *= 2;
            this.mDidFillOnce = false;
            this.mLast = availableIndice - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[availableIndice] = variable.id;
        this.mArrayValues[availableIndice] = value;
        if (previous != -1) {
            this.mArrayNextIndices[availableIndice] = this.mArrayNextIndices[previous];
            this.mArrayNextIndices[previous] = availableIndice;
        }
        else {
            this.mArrayNextIndices[availableIndice] = this.mHead;
            this.mHead = availableIndice;
        }
        ++variable.usageInRowCount;
        variable.addToRow(this.mRow);
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if (this.currentSize >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
        }
        if (this.mLast >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
            this.mLast = this.mArrayIndices.length - 1;
        }
    }
    
    final void add(final com.smart.library.support.constraint.solver.SolverVariable variable, final float value, final boolean removeFromDefinition) {
        if (value == 0.0f) {
            return;
        }
        if (this.mHead == -1) {
            this.mHead = 0;
            this.mArrayValues[this.mHead] = value;
            this.mArrayIndices[this.mHead] = variable.id;
            this.mArrayNextIndices[this.mHead] = -1;
            ++variable.usageInRowCount;
            variable.addToRow(this.mRow);
            ++this.currentSize;
            if (!this.mDidFillOnce) {
                ++this.mLast;
                if (this.mLast >= this.mArrayIndices.length) {
                    this.mDidFillOnce = true;
                    this.mLast = this.mArrayIndices.length - 1;
                }
            }
            return;
        }
        int current = this.mHead;
        int previous = -1;
        for (int counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            final int idx = this.mArrayIndices[current];
            if (idx == variable.id) {
                final float[] mArrayValues = this.mArrayValues;
                final int n = current;
                mArrayValues[n] += value;
                if (this.mArrayValues[current] == 0.0f) {
                    if (current == this.mHead) {
                        this.mHead = this.mArrayNextIndices[current];
                    }
                    else {
                        this.mArrayNextIndices[previous] = this.mArrayNextIndices[current];
                    }
                    if (removeFromDefinition) {
                        variable.removeFromRow(this.mRow);
                    }
                    if (this.mDidFillOnce) {
                        this.mLast = current;
                    }
                    --variable.usageInRowCount;
                    --this.currentSize;
                }
                return;
            }
            if (this.mArrayIndices[current] < variable.id) {
                previous = current;
            }
        }
        int availableIndice = this.mLast + 1;
        if (this.mDidFillOnce) {
            if (this.mArrayIndices[this.mLast] == -1) {
                availableIndice = this.mLast;
            }
            else {
                availableIndice = this.mArrayIndices.length;
            }
        }
        if (availableIndice >= this.mArrayIndices.length && this.currentSize < this.mArrayIndices.length) {
            for (int i = 0; i < this.mArrayIndices.length; ++i) {
                if (this.mArrayIndices[i] == -1) {
                    availableIndice = i;
                    break;
                }
            }
        }
        if (availableIndice >= this.mArrayIndices.length) {
            availableIndice = this.mArrayIndices.length;
            this.ROW_SIZE *= 2;
            this.mDidFillOnce = false;
            this.mLast = availableIndice - 1;
            this.mArrayValues = Arrays.copyOf(this.mArrayValues, this.ROW_SIZE);
            this.mArrayIndices = Arrays.copyOf(this.mArrayIndices, this.ROW_SIZE);
            this.mArrayNextIndices = Arrays.copyOf(this.mArrayNextIndices, this.ROW_SIZE);
        }
        this.mArrayIndices[availableIndice] = variable.id;
        this.mArrayValues[availableIndice] = value;
        if (previous != -1) {
            this.mArrayNextIndices[availableIndice] = this.mArrayNextIndices[previous];
            this.mArrayNextIndices[previous] = availableIndice;
        }
        else {
            this.mArrayNextIndices[availableIndice] = this.mHead;
            this.mHead = availableIndice;
        }
        ++variable.usageInRowCount;
        variable.addToRow(this.mRow);
        ++this.currentSize;
        if (!this.mDidFillOnce) {
            ++this.mLast;
        }
        if (this.mLast >= this.mArrayIndices.length) {
            this.mDidFillOnce = true;
            this.mLast = this.mArrayIndices.length - 1;
        }
    }
    
    public final float remove(final com.smart.library.support.constraint.solver.SolverVariable variable, final boolean removeFromDefinition) {
        if (this.candidate == variable) {
            this.candidate = null;
        }
        if (this.mHead == -1) {
            return 0.0f;
        }
        int current = this.mHead;
        int previous = -1;
        for (int counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            final int idx = this.mArrayIndices[current];
            if (idx == variable.id) {
                if (current == this.mHead) {
                    this.mHead = this.mArrayNextIndices[current];
                }
                else {
                    this.mArrayNextIndices[previous] = this.mArrayNextIndices[current];
                }
                if (removeFromDefinition) {
                    variable.removeFromRow(this.mRow);
                }
                --variable.usageInRowCount;
                --this.currentSize;
                this.mArrayIndices[current] = -1;
                if (this.mDidFillOnce) {
                    this.mLast = current;
                }
                return this.mArrayValues[current];
            }
            previous = current;
        }
        return 0.0f;
    }
    
    public final void clear() {
        for (int current = this.mHead, counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            final com.smart.library.support.constraint.solver.SolverVariable variable = this.mCache.mIndexedVariables[this.mArrayIndices[current]];
            if (variable != null) {
                variable.removeFromRow(this.mRow);
            }
        }
        this.mHead = -1;
        this.mLast = -1;
        this.mDidFillOnce = false;
        this.currentSize = 0;
    }
    
    final boolean containsKey(final com.smart.library.support.constraint.solver.SolverVariable variable) {
        if (this.mHead == -1) {
            return false;
        }
        for (int current = this.mHead, counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            if (this.mArrayIndices[current] == variable.id) {
                return true;
            }
        }
        return false;
    }
    
    boolean hasAtLeastOnePositiveVariable() {
        for (int current = this.mHead, counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            if (this.mArrayValues[current] > 0.0f) {
                return true;
            }
        }
        return false;
    }
    
    void invert() {
        for (int current = this.mHead, counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            final float[] mArrayValues = this.mArrayValues;
            final int n = current;
            mArrayValues[n] *= -1.0f;
        }
    }
    
    void divideByAmount(final float amount) {
        for (int current = this.mHead, counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            final float[] mArrayValues = this.mArrayValues;
            final int n = current;
            mArrayValues[n] /= amount;
        }
    }
    
    private boolean isNew(final com.smart.library.support.constraint.solver.SolverVariable variable, final com.smart.library.support.constraint.solver.LinearSystem system) {
        return variable.usageInRowCount <= 1;
    }
    
    com.smart.library.support.constraint.solver.SolverVariable chooseSubject(final LinearSystem system) {
        com.smart.library.support.constraint.solver.SolverVariable restrictedCandidate = null;
        com.smart.library.support.constraint.solver.SolverVariable unrestrictedCandidate = null;
        float unrestrictedCandidateAmount = 0.0f;
        float restrictedCandidateAmount = 0.0f;
        boolean unrestrictedCandidateIsNew = false;
        boolean restrictedCandidateIsNew = false;
        int current = this.mHead;
        int counter = 0;
        final float candidateAmount = 0.0f;
        while (current != -1 && counter < this.currentSize) {
            float amount = this.mArrayValues[current];
            final float epsilon = 0.001f;
            final com.smart.library.support.constraint.solver.SolverVariable variable = this.mCache.mIndexedVariables[this.mArrayIndices[current]];
            if (amount < 0.0f) {
                if (amount > -epsilon) {
                    this.mArrayValues[current] = 0.0f;
                    amount = 0.0f;
                    variable.removeFromRow(this.mRow);
                }
            }
            else if (amount < epsilon) {
                this.mArrayValues[current] = 0.0f;
                amount = 0.0f;
                variable.removeFromRow(this.mRow);
            }
            if (amount != 0.0f) {
                if (variable.mType == com.smart.library.support.constraint.solver.SolverVariable.Type.UNRESTRICTED) {
                    if (unrestrictedCandidate == null) {
                        unrestrictedCandidate = variable;
                        unrestrictedCandidateAmount = amount;
                        unrestrictedCandidateIsNew = this.isNew(variable, system);
                    }
                    else if (unrestrictedCandidateAmount > amount) {
                        unrestrictedCandidate = variable;
                        unrestrictedCandidateAmount = amount;
                        unrestrictedCandidateIsNew = this.isNew(variable, system);
                    }
                    else if (!unrestrictedCandidateIsNew && this.isNew(variable, system)) {
                        unrestrictedCandidate = variable;
                        unrestrictedCandidateAmount = amount;
                        unrestrictedCandidateIsNew = true;
                    }
                }
                else if (unrestrictedCandidate == null && amount < 0.0f) {
                    if (restrictedCandidate == null) {
                        restrictedCandidate = variable;
                        restrictedCandidateAmount = amount;
                        restrictedCandidateIsNew = this.isNew(variable, system);
                    }
                    else if (restrictedCandidateAmount > amount) {
                        restrictedCandidate = variable;
                        restrictedCandidateAmount = amount;
                        restrictedCandidateIsNew = this.isNew(variable, system);
                    }
                    else if (!restrictedCandidateIsNew && this.isNew(variable, system)) {
                        restrictedCandidate = variable;
                        restrictedCandidateAmount = amount;
                        restrictedCandidateIsNew = true;
                    }
                }
            }
            current = this.mArrayNextIndices[current];
            ++counter;
        }
        if (unrestrictedCandidate != null) {
            return unrestrictedCandidate;
        }
        return restrictedCandidate;
    }
    
    final void updateFromRow(final com.smart.library.support.constraint.solver.ArrayRow self, final com.smart.library.support.constraint.solver.ArrayRow definition, final boolean removeFromDefinition) {
        int current = this.mHead;
        int counter = 0;
        while (current != -1 && counter < this.currentSize) {
            if (this.mArrayIndices[current] == definition.variable.id) {
                final float value = this.mArrayValues[current];
                this.remove(definition.variable, removeFromDefinition);
                final ArrayLinkedVariables definitionVariables = definition.variables;
                for (int definitionCurrent = definitionVariables.mHead, definitionCounter = 0; definitionCurrent != -1 && definitionCounter < definitionVariables.currentSize; definitionCurrent = definitionVariables.mArrayNextIndices[definitionCurrent], ++definitionCounter) {
                    final com.smart.library.support.constraint.solver.SolverVariable definitionVariable = this.mCache.mIndexedVariables[definitionVariables.mArrayIndices[definitionCurrent]];
                    final float definitionValue = definitionVariables.mArrayValues[definitionCurrent];
                    this.add(definitionVariable, definitionValue * value, removeFromDefinition);
                }
                self.constantValue += definition.constantValue * value;
                if (removeFromDefinition) {
                    definition.variable.removeFromRow(self);
                }
                current = this.mHead;
                counter = 0;
            }
            else {
                current = this.mArrayNextIndices[current];
                ++counter;
            }
        }
    }
    
    void updateFromSystem(final com.smart.library.support.constraint.solver.ArrayRow self, final com.smart.library.support.constraint.solver.ArrayRow[] rows) {
        int current = this.mHead;
        int counter = 0;
        while (current != -1 && counter < this.currentSize) {
            final com.smart.library.support.constraint.solver.SolverVariable variable = this.mCache.mIndexedVariables[this.mArrayIndices[current]];
            if (variable.definitionId != -1) {
                final float value = this.mArrayValues[current];
                this.remove(variable, true);
                final ArrayRow definition = rows[variable.definitionId];
                if (!definition.isSimpleDefinition) {
                    final ArrayLinkedVariables definitionVariables = definition.variables;
                    for (int definitionCurrent = definitionVariables.mHead, definitionCounter = 0; definitionCurrent != -1 && definitionCounter < definitionVariables.currentSize; definitionCurrent = definitionVariables.mArrayNextIndices[definitionCurrent], ++definitionCounter) {
                        final com.smart.library.support.constraint.solver.SolverVariable definitionVariable = this.mCache.mIndexedVariables[definitionVariables.mArrayIndices[definitionCurrent]];
                        final float definitionValue = definitionVariables.mArrayValues[definitionCurrent];
                        this.add(definitionVariable, definitionValue * value, true);
                    }
                }
                self.constantValue += definition.constantValue * value;
                definition.variable.removeFromRow(self);
                current = this.mHead;
                counter = 0;
            }
            else {
                current = this.mArrayNextIndices[current];
                ++counter;
            }
        }
    }
    
    com.smart.library.support.constraint.solver.SolverVariable getPivotCandidate() {
        if (this.candidate == null) {
            int current = this.mHead;
            int counter = 0;
            com.smart.library.support.constraint.solver.SolverVariable pivot = null;
            while (current != -1 && counter < this.currentSize) {
                if (this.mArrayValues[current] < 0.0f) {
                    final com.smart.library.support.constraint.solver.SolverVariable v = this.mCache.mIndexedVariables[this.mArrayIndices[current]];
                    if (pivot == null || pivot.strength < v.strength) {
                        pivot = v;
                    }
                }
                current = this.mArrayNextIndices[current];
                ++counter;
            }
            return pivot;
        }
        return this.candidate;
    }
    
    com.smart.library.support.constraint.solver.SolverVariable getPivotCandidate(final boolean[] avoid, final com.smart.library.support.constraint.solver.SolverVariable exclude) {
        int current = this.mHead;
        int counter = 0;
        com.smart.library.support.constraint.solver.SolverVariable pivot = null;
        float value = 0.0f;
        while (current != -1 && counter < this.currentSize) {
            if (this.mArrayValues[current] < 0.0f) {
                final com.smart.library.support.constraint.solver.SolverVariable v = this.mCache.mIndexedVariables[this.mArrayIndices[current]];
                if ((avoid == null || !avoid[v.id]) && v != exclude && (v.mType == com.smart.library.support.constraint.solver.SolverVariable.Type.SLACK || v.mType == com.smart.library.support.constraint.solver.SolverVariable.Type.ERROR)) {
                    final float currentValue = this.mArrayValues[current];
                    if (currentValue < value) {
                        value = currentValue;
                        pivot = v;
                    }
                }
            }
            current = this.mArrayNextIndices[current];
            ++counter;
        }
        return pivot;
    }
    
    final com.smart.library.support.constraint.solver.SolverVariable getVariable(final int index) {
        for (int current = this.mHead, counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            if (counter == index) {
                return this.mCache.mIndexedVariables[this.mArrayIndices[current]];
            }
        }
        return null;
    }
    
    final float getVariableValue(final int index) {
        for (int current = this.mHead, counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            if (counter == index) {
                return this.mArrayValues[current];
            }
        }
        return 0.0f;
    }
    
    public final float get(final com.smart.library.support.constraint.solver.SolverVariable v) {
        for (int current = this.mHead, counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            if (this.mArrayIndices[current] == v.id) {
                return this.mArrayValues[current];
            }
        }
        return 0.0f;
    }
    
    int sizeInBytes() {
        int size = 0;
        size += 3 * (this.mArrayIndices.length * 4);
        size += 36;
        return size;
    }
    
    public void display() {
        final int count = this.currentSize;
        System.out.print("{ ");
        for (int i = 0; i < count; ++i) {
            final SolverVariable v = this.getVariable(i);
            if (v != null) {
                System.out.print(v + " = " + this.getVariableValue(i) + " ");
            }
        }
        System.out.println(" }");
    }
    
    @Override
    public String toString() {
        String result = "";
        for (int current = this.mHead, counter = 0; current != -1 && counter < this.currentSize; current = this.mArrayNextIndices[current], ++counter) {
            result += " -> ";
            result = result + this.mArrayValues[current] + " : ";
            result += this.mCache.mIndexedVariables[this.mArrayIndices[current]];
        }
        return result;
    }
}
