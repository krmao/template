package com.smart.library.support.constraint.solver;

import java.util.*;

public class Metrics
{
    public long measures;
    public long additionalMeasures;
    public long resolutions;
    public long tableSizeIncrease;
    public long minimize;
    public long constraints;
    public long simpleconstraints;
    public long optimize;
    public long iterations;
    public long pivots;
    public long bfs;
    public long variables;
    public long errors;
    public long slackvariables;
    public long extravariables;
    public long maxTableSize;
    public long fullySolved;
    public long graphOptimizer;
    public long graphSolved;
    public long linearSolved;
    public long resolvedWidgets;
    public long minimizeGoal;
    public long maxVariables;
    public long maxRows;
    public long centerConnectionResolved;
    public long matchConnectionResolved;
    public long chainConnectionResolved;
    public long barrierConnectionResolved;
    public long oldresolvedWidgets;
    public long nonresolvedWidgets;
    public ArrayList<String> problematicLayouts;
    public long lastTableSize;
    public long widgets;
    public long measuresWrap;
    public long measuresWrapInfeasible;
    public long infeasibleDetermineGroups;
    public long determineGroups;
    
    public Metrics() {
        this.problematicLayouts = new ArrayList<String>();
    }
    
    @Override
    public String toString() {
        return "\n*** Metrics ***\nmeasures: " + this.measures + "\nmeasuresWrap: " + this.measuresWrap + "\nmeasuresWrapInfeasible: " + this.measuresWrapInfeasible + "\ndetermineGroups: " + this.determineGroups + "\ninfeasibleDetermineGroups: " + this.infeasibleDetermineGroups + "\ngraphOptimizer: " + this.graphOptimizer + "\nwidgets: " + this.widgets + "\ngraphSolved: " + this.graphSolved + "\nlinearSolved: " + this.linearSolved + "\n";
    }
    
    public void reset() {
        this.measures = 0L;
        this.widgets = 0L;
        this.additionalMeasures = 0L;
        this.resolutions = 0L;
        this.tableSizeIncrease = 0L;
        this.maxTableSize = 0L;
        this.lastTableSize = 0L;
        this.maxVariables = 0L;
        this.maxRows = 0L;
        this.minimize = 0L;
        this.minimizeGoal = 0L;
        this.constraints = 0L;
        this.simpleconstraints = 0L;
        this.optimize = 0L;
        this.iterations = 0L;
        this.pivots = 0L;
        this.bfs = 0L;
        this.variables = 0L;
        this.errors = 0L;
        this.slackvariables = 0L;
        this.extravariables = 0L;
        this.fullySolved = 0L;
        this.graphOptimizer = 0L;
        this.graphSolved = 0L;
        this.resolvedWidgets = 0L;
        this.oldresolvedWidgets = 0L;
        this.nonresolvedWidgets = 0L;
        this.centerConnectionResolved = 0L;
        this.matchConnectionResolved = 0L;
        this.chainConnectionResolved = 0L;
        this.barrierConnectionResolved = 0L;
        this.problematicLayouts.clear();
    }
}
