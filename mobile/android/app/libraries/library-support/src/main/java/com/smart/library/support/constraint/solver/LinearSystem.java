package com.smart.library.support.constraint.solver;

import java.util.*;

import com.smart.library.support.constraint.solver.widgets.ConstraintAnchor;
import com.smart.library.support.constraint.solver.widgets.ConstraintWidget;

public class LinearSystem
{
    public static final boolean FULL_DEBUG = false;
    private static final boolean DEBUG = false;
    private static int POOL_SIZE;
    int mVariablesID;
    private HashMap<String, SolverVariable> mVariables;
    private Row mGoal;
    private int TABLE_SIZE;
    private int mMaxColumns;
    com.smart.library.support.constraint.solver.ArrayRow[] mRows;
    public boolean graphOptimizer;
    public boolean newgraphOptimizer;
    private boolean[] mAlreadyTestedCandidates;
    int mNumColumns;
    int mNumRows;
    private int mMaxRows;
    final com.smart.library.support.constraint.solver.Cache mCache;
    private SolverVariable[] mPoolVariables;
    private int mPoolVariablesCount;
    private com.smart.library.support.constraint.solver.ArrayRow[] tempClientsCopy;
    public static com.smart.library.support.constraint.solver.Metrics sMetrics;
    private final Row mTempGoal;
    
    public LinearSystem() {
        this.mVariablesID = 0;
        this.mVariables = null;
        this.TABLE_SIZE = 32;
        this.mMaxColumns = this.TABLE_SIZE;
        this.mRows = null;
        this.graphOptimizer = false;
        this.newgraphOptimizer = false;
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mNumColumns = 1;
        this.mNumRows = 0;
        this.mMaxRows = this.TABLE_SIZE;
        this.mPoolVariables = new SolverVariable[LinearSystem.POOL_SIZE];
        this.mPoolVariablesCount = 0;
        this.tempClientsCopy = new com.smart.library.support.constraint.solver.ArrayRow[this.TABLE_SIZE];
        this.mRows = new com.smart.library.support.constraint.solver.ArrayRow[this.TABLE_SIZE];
        this.releaseRows();
        this.mCache = new com.smart.library.support.constraint.solver.Cache();
        this.mGoal = new GoalRow(this.mCache);
        this.mTempGoal = new ArrayRow(this.mCache);
    }
    
    public void fillMetrics(final com.smart.library.support.constraint.solver.Metrics metrics) {
        LinearSystem.sMetrics = metrics;
    }
    
    public static com.smart.library.support.constraint.solver.Metrics getMetrics() {
        return LinearSystem.sMetrics;
    }
    
    private void increaseTableSize() {
        this.TABLE_SIZE *= 2;
        this.mRows = Arrays.copyOf(this.mRows, this.TABLE_SIZE);
        this.mCache.mIndexedVariables = Arrays.copyOf(this.mCache.mIndexedVariables, this.TABLE_SIZE);
        this.mAlreadyTestedCandidates = new boolean[this.TABLE_SIZE];
        this.mMaxColumns = this.TABLE_SIZE;
        this.mMaxRows = this.TABLE_SIZE;
        if (LinearSystem.sMetrics != null) {
            final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.tableSizeIncrease;
            LinearSystem.sMetrics.maxTableSize = Math.max(LinearSystem.sMetrics.maxTableSize, this.TABLE_SIZE);
            LinearSystem.sMetrics.lastTableSize = LinearSystem.sMetrics.maxTableSize;
        }
    }
    
    private void releaseRows() {
        for (int i = 0; i < this.mRows.length; ++i) {
            final com.smart.library.support.constraint.solver.ArrayRow row = this.mRows[i];
            if (row != null) {
                this.mCache.arrayRowPool.release(row);
            }
            this.mRows[i] = null;
        }
    }
    
    public void reset() {
        for (int i = 0; i < this.mCache.mIndexedVariables.length; ++i) {
            final SolverVariable variable = this.mCache.mIndexedVariables[i];
            if (variable != null) {
                variable.reset();
            }
        }
        this.mCache.solverVariablePool.releaseAll(this.mPoolVariables, this.mPoolVariablesCount);
        this.mPoolVariablesCount = 0;
        Arrays.fill(this.mCache.mIndexedVariables, null);
        if (this.mVariables != null) {
            this.mVariables.clear();
        }
        this.mVariablesID = 0;
        this.mGoal.clear();
        this.mNumColumns = 1;
        for (int i = 0; i < this.mNumRows; ++i) {
            this.mRows[i].used = false;
        }
        this.releaseRows();
        this.mNumRows = 0;
    }
    
    public SolverVariable createObjectVariable(final Object anchor) {
        if (anchor == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        SolverVariable variable = null;
        if (anchor instanceof ConstraintAnchor) {
            variable = ((ConstraintAnchor)anchor).getSolverVariable();
            if (variable == null) {
                ((ConstraintAnchor)anchor).resetSolverVariable(this.mCache);
                variable = ((ConstraintAnchor)anchor).getSolverVariable();
            }
            if (variable.id == -1 || variable.id > this.mVariablesID || this.mCache.mIndexedVariables[variable.id] == null) {
                if (variable.id != -1) {
                    variable.reset();
                }
                ++this.mVariablesID;
                ++this.mNumColumns;
                variable.id = this.mVariablesID;
                variable.mType = SolverVariable.Type.UNRESTRICTED;
                this.mCache.mIndexedVariables[this.mVariablesID] = variable;
            }
        }
        return variable;
    }
    
    public com.smart.library.support.constraint.solver.ArrayRow createRow() {
        com.smart.library.support.constraint.solver.ArrayRow row = this.mCache.arrayRowPool.acquire();
        if (row == null) {
            row = new com.smart.library.support.constraint.solver.ArrayRow(this.mCache);
        }
        else {
            row.reset();
        }
        SolverVariable.increaseErrorId();
        return row;
    }
    
    public SolverVariable createSlackVariable() {
        if (LinearSystem.sMetrics != null) {
            final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.slackvariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable variable = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
        ++this.mVariablesID;
        ++this.mNumColumns;
        variable.id = this.mVariablesID;
        return this.mCache.mIndexedVariables[this.mVariablesID] = variable;
    }
    
    public SolverVariable createExtraVariable() {
        if (LinearSystem.sMetrics != null) {
            final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.extravariables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable variable = this.acquireSolverVariable(SolverVariable.Type.SLACK, null);
        ++this.mVariablesID;
        ++this.mNumColumns;
        variable.id = this.mVariablesID;
        return this.mCache.mIndexedVariables[this.mVariablesID] = variable;
    }
    
    private void addError(final com.smart.library.support.constraint.solver.ArrayRow row) {
        row.addError(this, 0);
    }
    
    private void addSingleError(final com.smart.library.support.constraint.solver.ArrayRow row, final int sign) {
        this.addSingleError(row, sign, 0);
    }
    
    void addSingleError(final com.smart.library.support.constraint.solver.ArrayRow row, final int sign, final int strength) {
        final String prefix = null;
        final SolverVariable error = this.createErrorVariable(strength, prefix);
        row.addSingleError(error, sign);
    }
    
    private SolverVariable createVariable(final String name, final SolverVariable.Type type) {
        if (LinearSystem.sMetrics != null) {
            final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.variables;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable variable = this.acquireSolverVariable(type, null);
        variable.setName(name);
        ++this.mVariablesID;
        ++this.mNumColumns;
        variable.id = this.mVariablesID;
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        this.mVariables.put(name, variable);
        return this.mCache.mIndexedVariables[this.mVariablesID] = variable;
    }
    
    public SolverVariable createErrorVariable(final int strength, final String prefix) {
        if (LinearSystem.sMetrics != null) {
            final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.errors;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        final SolverVariable variable = this.acquireSolverVariable(SolverVariable.Type.ERROR, prefix);
        ++this.mVariablesID;
        ++this.mNumColumns;
        variable.id = this.mVariablesID;
        variable.strength = strength;
        this.mCache.mIndexedVariables[this.mVariablesID] = variable;
        this.mGoal.addError(variable);
        return variable;
    }
    
    private SolverVariable acquireSolverVariable(final SolverVariable.Type type, final String prefix) {
        SolverVariable variable = this.mCache.solverVariablePool.acquire();
        if (variable == null) {
            variable = new SolverVariable(type, prefix);
            variable.setType(type, prefix);
        }
        else {
            variable.reset();
            variable.setType(type, prefix);
        }
        if (this.mPoolVariablesCount >= LinearSystem.POOL_SIZE) {
            LinearSystem.POOL_SIZE *= 2;
            this.mPoolVariables = Arrays.copyOf(this.mPoolVariables, LinearSystem.POOL_SIZE);
        }
        return this.mPoolVariables[this.mPoolVariablesCount++] = variable;
    }
    
    Row getGoal() {
        return this.mGoal;
    }
    
    com.smart.library.support.constraint.solver.ArrayRow getRow(final int n) {
        return this.mRows[n];
    }
    
    float getValueFor(final String name) {
        final SolverVariable v = this.getVariable(name, SolverVariable.Type.UNRESTRICTED);
        if (v == null) {
            return 0.0f;
        }
        return v.computedValue;
    }
    
    public int getObjectVariableValue(final Object anchor) {
        final SolverVariable variable = ((ConstraintAnchor)anchor).getSolverVariable();
        if (variable != null) {
            return (int)(variable.computedValue + 0.5f);
        }
        return 0;
    }
    
    SolverVariable getVariable(final String name, final SolverVariable.Type type) {
        if (this.mVariables == null) {
            this.mVariables = new HashMap<String, SolverVariable>();
        }
        SolverVariable variable = this.mVariables.get(name);
        if (variable == null) {
            variable = this.createVariable(name, type);
        }
        return variable;
    }
    
    public void minimize() throws Exception {
        if (LinearSystem.sMetrics != null) {
            final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.minimize;
        }
        if (this.graphOptimizer || this.newgraphOptimizer) {
            if (LinearSystem.sMetrics != null) {
                final com.smart.library.support.constraint.solver.Metrics sMetrics2 = LinearSystem.sMetrics;
                ++sMetrics2.graphOptimizer;
            }
            boolean fullySolved = true;
            for (int i = 0; i < this.mNumRows; ++i) {
                final com.smart.library.support.constraint.solver.ArrayRow r = this.mRows[i];
                if (!r.isSimpleDefinition) {
                    fullySolved = false;
                    break;
                }
            }
            if (!fullySolved) {
                this.minimizeGoal(this.mGoal);
            }
            else {
                if (LinearSystem.sMetrics != null) {
                    final com.smart.library.support.constraint.solver.Metrics sMetrics3 = LinearSystem.sMetrics;
                    ++sMetrics3.fullySolved;
                }
                this.computeValues();
            }
        }
        else {
            this.minimizeGoal(this.mGoal);
        }
    }
    
    void minimizeGoal(final Row goal) throws Exception {
        if (LinearSystem.sMetrics != null) {
            final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.minimizeGoal;
            LinearSystem.sMetrics.maxVariables = Math.max(LinearSystem.sMetrics.maxVariables, this.mNumColumns);
            LinearSystem.sMetrics.maxRows = Math.max(LinearSystem.sMetrics.maxRows, this.mNumRows);
        }
        this.updateRowFromVariables((com.smart.library.support.constraint.solver.ArrayRow)goal);
        this.enforceBFS(goal);
        this.optimize(goal, false);
        this.computeValues();
    }
    
    private final void updateRowFromVariables(final com.smart.library.support.constraint.solver.ArrayRow row) {
        if (this.mNumRows > 0) {
            row.variables.updateFromSystem(row, this.mRows);
            if (row.variables.currentSize == 0) {
                row.isSimpleDefinition = true;
            }
        }
    }
    
    public void addConstraint(final com.smart.library.support.constraint.solver.ArrayRow row) {
        if (row == null) {
            return;
        }
        if (LinearSystem.sMetrics != null) {
            final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.constraints;
            if (row.isSimpleDefinition) {
                final com.smart.library.support.constraint.solver.Metrics sMetrics2 = LinearSystem.sMetrics;
                ++sMetrics2.simpleconstraints;
            }
        }
        if (this.mNumRows + 1 >= this.mMaxRows || this.mNumColumns + 1 >= this.mMaxColumns) {
            this.increaseTableSize();
        }
        boolean added = false;
        if (!row.isSimpleDefinition) {
            this.updateRowFromVariables(row);
            if (row.isEmpty()) {
                return;
            }
            row.ensurePositiveConstant();
            if (row.chooseSubject(this)) {
                final SolverVariable extra = this.createExtraVariable();
                row.variable = extra;
                this.addRow(row);
                added = true;
                this.mTempGoal.initFromRow(row);
                this.optimize(this.mTempGoal, true);
                if (extra.definitionId == -1) {
                    if (row.variable == extra) {
                        final SolverVariable pivotCandidate = row.pickPivot(extra);
                        if (pivotCandidate != null) {
                            if (LinearSystem.sMetrics != null) {
                                final com.smart.library.support.constraint.solver.Metrics sMetrics3 = LinearSystem.sMetrics;
                                ++sMetrics3.pivots;
                            }
                            row.pivot(pivotCandidate);
                        }
                    }
                    if (!row.isSimpleDefinition) {
                        row.variable.updateReferencesWithNewDefinition(row);
                    }
                    --this.mNumRows;
                }
            }
            if (!row.hasKeyVariable()) {
                return;
            }
        }
        if (!added) {
            this.addRow(row);
        }
    }
    
    private final void addRow(final com.smart.library.support.constraint.solver.ArrayRow row) {
        if (this.mRows[this.mNumRows] != null) {
            this.mCache.arrayRowPool.release(this.mRows[this.mNumRows]);
        }
        this.mRows[this.mNumRows] = row;
        row.variable.definitionId = this.mNumRows;
        ++this.mNumRows;
        row.variable.updateReferencesWithNewDefinition(row);
    }
    
    private final int optimize(final Row goal, final boolean b) {
        if (LinearSystem.sMetrics != null) {
            final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
            ++sMetrics.optimize;
        }
        boolean done = false;
        int tries = 0;
        for (int i = 0; i < this.mNumColumns; ++i) {
            this.mAlreadyTestedCandidates[i] = false;
        }
        while (!done) {
            if (LinearSystem.sMetrics != null) {
                final com.smart.library.support.constraint.solver.Metrics sMetrics2 = LinearSystem.sMetrics;
                ++sMetrics2.iterations;
            }
            if (++tries >= 2 * this.mNumColumns) {
                return tries;
            }
            if (goal.getKey() != null) {
                this.mAlreadyTestedCandidates[goal.getKey().id] = true;
            }
            final SolverVariable pivotCandidate = goal.getPivotCandidate(this, this.mAlreadyTestedCandidates);
            if (pivotCandidate != null) {
                if (this.mAlreadyTestedCandidates[pivotCandidate.id]) {
                    return tries;
                }
                this.mAlreadyTestedCandidates[pivotCandidate.id] = true;
            }
            if (pivotCandidate != null) {
                float min = Float.MAX_VALUE;
                int pivotRowIndex = -1;
                for (int j = 0; j < this.mNumRows; ++j) {
                    final com.smart.library.support.constraint.solver.ArrayRow current = this.mRows[j];
                    final SolverVariable variable = current.variable;
                    if (variable.mType != SolverVariable.Type.UNRESTRICTED) {
                        if (!current.isSimpleDefinition) {
                            if (current.hasVariable(pivotCandidate)) {
                                final float a_j = current.variables.get(pivotCandidate);
                                if (a_j < 0.0f) {
                                    final float value = -current.constantValue / a_j;
                                    if (value < min) {
                                        min = value;
                                        pivotRowIndex = j;
                                    }
                                }
                            }
                        }
                    }
                }
                if (pivotRowIndex > -1) {
                    final com.smart.library.support.constraint.solver.ArrayRow pivotEquation = this.mRows[pivotRowIndex];
                    pivotEquation.variable.definitionId = -1;
                    if (LinearSystem.sMetrics != null) {
                        final com.smart.library.support.constraint.solver.Metrics sMetrics3 = LinearSystem.sMetrics;
                        ++sMetrics3.pivots;
                    }
                    pivotEquation.pivot(pivotCandidate);
                    pivotEquation.variable.definitionId = pivotRowIndex;
                    pivotEquation.variable.updateReferencesWithNewDefinition(pivotEquation);
                }
                else {
                    done = true;
                }
            }
            else {
                done = true;
            }
        }
        return tries;
    }
    
    private int enforceBFS(final Row goal) throws Exception {
        int tries = 0;
        boolean infeasibleSystem = false;
        for (int i = 0; i < this.mNumRows; ++i) {
            final SolverVariable variable = this.mRows[i].variable;
            if (variable.mType != SolverVariable.Type.UNRESTRICTED) {
                if (this.mRows[i].constantValue < 0.0f) {
                    infeasibleSystem = true;
                    break;
                }
            }
        }
        if (infeasibleSystem) {
            boolean done = false;
            tries = 0;
            while (!done) {
                if (LinearSystem.sMetrics != null) {
                    final com.smart.library.support.constraint.solver.Metrics sMetrics = LinearSystem.sMetrics;
                    ++sMetrics.bfs;
                }
                ++tries;
                float min = Float.MAX_VALUE;
                int strength = 0;
                int pivotRowIndex = -1;
                int pivotColumnIndex = -1;
                for (int j = 0; j < this.mNumRows; ++j) {
                    final com.smart.library.support.constraint.solver.ArrayRow current = this.mRows[j];
                    final SolverVariable variable2 = current.variable;
                    if (variable2.mType != SolverVariable.Type.UNRESTRICTED) {
                        if (!current.isSimpleDefinition) {
                            if (current.constantValue < 0.0f) {
                                for (int k = 1; k < this.mNumColumns; ++k) {
                                    final SolverVariable candidate = this.mCache.mIndexedVariables[k];
                                    final float a_j = current.variables.get(candidate);
                                    if (a_j > 0.0f) {
                                        for (int l = 0; l < 7; ++l) {
                                            final float value = candidate.strengthVector[l] / a_j;
                                            if ((value < min && l == strength) || l > strength) {
                                                min = value;
                                                pivotRowIndex = j;
                                                pivotColumnIndex = k;
                                                strength = l;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (pivotRowIndex != -1) {
                    final com.smart.library.support.constraint.solver.ArrayRow pivotEquation = this.mRows[pivotRowIndex];
                    pivotEquation.variable.definitionId = -1;
                    if (LinearSystem.sMetrics != null) {
                        final Metrics sMetrics2 = LinearSystem.sMetrics;
                        ++sMetrics2.pivots;
                    }
                    pivotEquation.pivot(this.mCache.mIndexedVariables[pivotColumnIndex]);
                    pivotEquation.variable.definitionId = pivotRowIndex;
                    pivotEquation.variable.updateReferencesWithNewDefinition(pivotEquation);
                }
                else {
                    done = true;
                }
                if (tries > this.mNumColumns / 2) {
                    done = true;
                }
            }
        }
        return tries;
    }
    
    private void computeValues() {
        for (int i = 0; i < this.mNumRows; ++i) {
            final com.smart.library.support.constraint.solver.ArrayRow row = this.mRows[i];
            row.variable.computedValue = row.constantValue;
        }
    }
    
    private void displayRows() {
        this.displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            s += this.mRows[i];
            s += "\n";
        }
        s = s + this.mGoal + "\n";
        System.out.println(s);
    }
    
    void displayReadableRows() {
        this.displaySolverVariables();
        String s = " #  ";
        for (int i = 0; i < this.mNumRows; ++i) {
            s += this.mRows[i].toReadableString();
            s += "\n #  ";
        }
        if (this.mGoal != null) {
            s = s + this.mGoal + "\n";
        }
        System.out.println(s);
    }
    
    public void displayVariablesReadableRows() {
        this.displaySolverVariables();
        String s = "";
        for (int i = 0; i < this.mNumRows; ++i) {
            if (this.mRows[i].variable.mType == SolverVariable.Type.UNRESTRICTED) {
                s += this.mRows[i].toReadableString();
                s += "\n";
            }
        }
        s = s + this.mGoal + "\n";
        System.out.println(s);
    }
    
    public int getMemoryUsed() {
        int actualRowSize = 0;
        for (int i = 0; i < this.mNumRows; ++i) {
            if (this.mRows[i] != null) {
                actualRowSize += this.mRows[i].sizeInBytes();
            }
        }
        return actualRowSize;
    }
    
    public int getNumEquations() {
        return this.mNumRows;
    }
    
    public int getNumVariables() {
        return this.mVariablesID;
    }
    
    void displaySystemInformations() {
        final int count = 0;
        int rowSize = 0;
        for (int i = 0; i < this.TABLE_SIZE; ++i) {
            if (this.mRows[i] != null) {
                rowSize += this.mRows[i].sizeInBytes();
            }
        }
        int actualRowSize = 0;
        for (int j = 0; j < this.mNumRows; ++j) {
            if (this.mRows[j] != null) {
                actualRowSize += this.mRows[j].sizeInBytes();
            }
        }
        System.out.println("Linear System -> Table size: " + this.TABLE_SIZE + " (" + this.getDisplaySize(this.TABLE_SIZE * this.TABLE_SIZE) + ") -- row sizes: " + this.getDisplaySize(rowSize) + ", actual size: " + this.getDisplaySize(actualRowSize) + " rows: " + this.mNumRows + "/" + this.mMaxRows + " cols: " + this.mNumColumns + "/" + this.mMaxColumns + " " + count + " occupied cells, " + this.getDisplaySize(count));
    }
    
    private void displaySolverVariables() {
        final String s = "Display Rows (" + this.mNumRows + "x" + this.mNumColumns + ")\n";
        System.out.println(s);
    }
    
    private String getDisplaySize(final int n) {
        final int mb = n * 4 / 1024 / 1024;
        if (mb > 0) {
            return "" + mb + " Mb";
        }
        final int kb = n * 4 / 1024;
        if (kb > 0) {
            return "" + kb + " Kb";
        }
        return "" + n * 4 + " bytes";
    }
    
    public Cache getCache() {
        return this.mCache;
    }
    
    private String getDisplayStrength(final int strength) {
        if (strength == 1) {
            return "LOW";
        }
        if (strength == 2) {
            return "MEDIUM";
        }
        if (strength == 3) {
            return "HIGH";
        }
        if (strength == 4) {
            return "HIGHEST";
        }
        if (strength == 5) {
            return "EQUALITY";
        }
        if (strength == 6) {
            return "FIXED";
        }
        return "NONE";
    }
    
    public void addGreaterThan(final SolverVariable a, final SolverVariable b, final int margin, final int strength) {
        final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
        final SolverVariable slack = this.createSlackVariable();
        slack.strength = 0;
        row.createRowGreaterThan(a, b, slack, margin);
        if (strength != 6) {
            final float slackValue = row.variables.get(slack);
            this.addSingleError(row, (int)(-1.0f * slackValue), strength);
        }
        this.addConstraint(row);
    }
    
    public void addGreaterThan(final SolverVariable a, final int b) {
        final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
        final SolverVariable slack = this.createSlackVariable();
        slack.strength = 0;
        row.createRowGreaterThan(a, b, slack);
        this.addConstraint(row);
    }
    
    public void addGreaterBarrier(final SolverVariable a, final SolverVariable b, final int margin, final boolean hasMatchConstraintWidgets) {
        final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
        final SolverVariable slack = this.createSlackVariable();
        slack.strength = 5;
        row.createRowGreaterThan(a, b, slack, margin);
        if (hasMatchConstraintWidgets) {
            final float slackValue = row.variables.get(slack);
            this.addSingleError(row, (int)(-1.0f * slackValue), 5);
        }
        this.addConstraint(row);
    }
    
    public void addLowerThan(final SolverVariable a, final SolverVariable b, final int margin, final int strength) {
        final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
        final SolverVariable slack = this.createSlackVariable();
        slack.strength = 0;
        row.createRowLowerThan(a, b, slack, margin);
        if (strength != 6) {
            final float slackValue = row.variables.get(slack);
            this.addSingleError(row, (int)(-1.0f * slackValue), strength);
        }
        this.addConstraint(row);
    }
    
    public void addLowerBarrier(final SolverVariable a, final SolverVariable b, final int margin, final boolean hasMatchConstraintWidgets) {
        final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
        final SolverVariable slack = this.createSlackVariable();
        slack.strength = 5;
        row.createRowLowerThan(a, b, slack, margin);
        if (hasMatchConstraintWidgets) {
            final float slackValue = row.variables.get(slack);
            this.addSingleError(row, (int)(-1.0f * slackValue), 5);
        }
        this.addConstraint(row);
    }
    
    public void addCentering(final SolverVariable a, final SolverVariable b, final int m1, final float bias, final SolverVariable c, final SolverVariable d, final int m2, final int strength) {
        final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
        row.createRowCentering(a, b, m1, bias, c, d, m2);
        if (strength != 6) {
            row.addError(this, strength);
        }
        this.addConstraint(row);
    }
    
    public void addRatio(final SolverVariable a, final SolverVariable b, final SolverVariable c, final SolverVariable d, final float ratio, final int strength) {
        final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
        row.createRowDimensionRatio(a, b, c, d, ratio);
        if (strength != 6) {
            row.addError(this, strength);
        }
        this.addConstraint(row);
    }
    
    public com.smart.library.support.constraint.solver.ArrayRow addEquality(final SolverVariable a, final SolverVariable b, final int margin, final int strength) {
        final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
        row.createRowEquals(a, b, margin);
        if (strength != 6) {
            row.addError(this, strength);
        }
        this.addConstraint(row);
        return row;
    }
    
    public void addEquality(final SolverVariable a, final int value) {
        final int idx = a.definitionId;
        if (a.definitionId != -1) {
            final com.smart.library.support.constraint.solver.ArrayRow row = this.mRows[idx];
            if (row.isSimpleDefinition) {
                row.constantValue = value;
            }
            else if (row.variables.currentSize == 0) {
                row.isSimpleDefinition = true;
                row.constantValue = value;
            }
            else {
                final com.smart.library.support.constraint.solver.ArrayRow newRow = this.createRow();
                newRow.createRowEquals(a, value);
                this.addConstraint(newRow);
            }
        }
        else {
            final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
            row.createRowDefinition(a, value);
            this.addConstraint(row);
        }
    }
    
    public void addEquality(final SolverVariable a, final int value, final int strength) {
        final int idx = a.definitionId;
        if (a.definitionId != -1) {
            final com.smart.library.support.constraint.solver.ArrayRow row = this.mRows[idx];
            if (row.isSimpleDefinition) {
                row.constantValue = value;
            }
            else {
                final com.smart.library.support.constraint.solver.ArrayRow newRow = this.createRow();
                newRow.createRowEquals(a, value);
                newRow.addError(this, strength);
                this.addConstraint(newRow);
            }
        }
        else {
            final com.smart.library.support.constraint.solver.ArrayRow row = this.createRow();
            row.createRowDefinition(a, value);
            row.addError(this, strength);
            this.addConstraint(row);
        }
    }
    
    public static com.smart.library.support.constraint.solver.ArrayRow createRowEquals(final LinearSystem linearSystem, final SolverVariable variableA, final SolverVariable variableB, final int margin, final boolean withError) {
        final com.smart.library.support.constraint.solver.ArrayRow row = linearSystem.createRow();
        row.createRowEquals(variableA, variableB, margin);
        if (withError) {
            linearSystem.addSingleError(row, 1);
        }
        return row;
    }
    
    public static com.smart.library.support.constraint.solver.ArrayRow createRowDimensionPercent(final LinearSystem linearSystem, final SolverVariable variableA, final SolverVariable variableB, final SolverVariable variableC, final float percent, final boolean withError) {
        final com.smart.library.support.constraint.solver.ArrayRow row = linearSystem.createRow();
        if (withError) {
            linearSystem.addError(row);
        }
        return row.createRowDimensionPercent(variableA, variableB, variableC, percent);
    }
    
    public static com.smart.library.support.constraint.solver.ArrayRow createRowGreaterThan(final LinearSystem linearSystem, final SolverVariable variableA, final SolverVariable variableB, final int margin, final boolean withError) {
        final SolverVariable slack = linearSystem.createSlackVariable();
        final com.smart.library.support.constraint.solver.ArrayRow row = linearSystem.createRow();
        row.createRowGreaterThan(variableA, variableB, slack, margin);
        if (withError) {
            final float slackValue = row.variables.get(slack);
            linearSystem.addSingleError(row, (int)(-1.0f * slackValue));
        }
        return row;
    }
    
    public static com.smart.library.support.constraint.solver.ArrayRow createRowLowerThan(final LinearSystem linearSystem, final SolverVariable variableA, final SolverVariable variableB, final int margin, final boolean withError) {
        final SolverVariable slack = linearSystem.createSlackVariable();
        final com.smart.library.support.constraint.solver.ArrayRow row = linearSystem.createRow();
        row.createRowLowerThan(variableA, variableB, slack, margin);
        if (withError) {
            final float slackValue = row.variables.get(slack);
            linearSystem.addSingleError(row, (int)(-1.0f * slackValue));
        }
        return row;
    }
    
    public static com.smart.library.support.constraint.solver.ArrayRow createRowCentering(final LinearSystem linearSystem, final SolverVariable variableA, final SolverVariable variableB, final int marginA, final float bias, final SolverVariable variableC, final SolverVariable variableD, final int marginB, final boolean withError) {
        final com.smart.library.support.constraint.solver.ArrayRow row = linearSystem.createRow();
        row.createRowCentering(variableA, variableB, marginA, bias, variableC, variableD, marginB);
        if (withError) {
            row.addError(linearSystem, 4);
        }
        return row;
    }
    
    public void addCenterPoint(final ConstraintWidget widget, final ConstraintWidget target, final float angle, final int radius) {
        final SolverVariable Al = this.createObjectVariable(widget.getAnchor(ConstraintAnchor.Type.LEFT));
        final SolverVariable At = this.createObjectVariable(widget.getAnchor(ConstraintAnchor.Type.TOP));
        final SolverVariable Ar = this.createObjectVariable(widget.getAnchor(ConstraintAnchor.Type.RIGHT));
        final SolverVariable Ab = this.createObjectVariable(widget.getAnchor(ConstraintAnchor.Type.BOTTOM));
        final SolverVariable Bl = this.createObjectVariable(target.getAnchor(ConstraintAnchor.Type.LEFT));
        final SolverVariable Bt = this.createObjectVariable(target.getAnchor(ConstraintAnchor.Type.TOP));
        final SolverVariable Br = this.createObjectVariable(target.getAnchor(ConstraintAnchor.Type.RIGHT));
        final SolverVariable Bb = this.createObjectVariable(target.getAnchor(ConstraintAnchor.Type.BOTTOM));
        ArrayRow row = this.createRow();
        float angleComponent = (float)(Math.sin(angle) * radius);
        row.createRowWithAngle(At, Ab, Bt, Bb, angleComponent);
        this.addConstraint(row);
        row = this.createRow();
        angleComponent = (float)(Math.cos(angle) * radius);
        row.createRowWithAngle(Al, Ar, Bl, Br, angleComponent);
        this.addConstraint(row);
    }
    
    static {
        LinearSystem.POOL_SIZE = 1000;
    }
    
    interface Row
    {
        SolverVariable getPivotCandidate(final LinearSystem p0, final boolean[] p1);
        
        void clear();
        
        void initFromRow(final Row p0);
        
        void addError(final SolverVariable p0);
        
        SolverVariable getKey();
        
        boolean isEmpty();
    }
}
