package com.smart.library.support.constraint.solver.widgets.analyzer;

import java.util.*;

public class DependencyNode implements Dependency
{
    public Dependency updateDelegate;
    public boolean delegateToWidgetRun;
    public boolean readyToSolve;
    WidgetRun run;
    Type type;
    int margin;
    public int value;
    int marginFactor;
    DimensionDependency marginDependency;
    public boolean resolved;
    List<Dependency> dependencies;
    List<DependencyNode> targets;
    
    public DependencyNode(final WidgetRun run) {
        this.updateDelegate = null;
        this.delegateToWidgetRun = false;
        this.readyToSolve = false;
        this.type = Type.UNKNOWN;
        this.marginFactor = 1;
        this.marginDependency = null;
        this.resolved = false;
        this.dependencies = new ArrayList<Dependency>();
        this.targets = new ArrayList<DependencyNode>();
        this.run = run;
    }
    
    @Override
    public String toString() {
        return this.run.widget.getDebugName() + ":" + this.type + "(" + (this.resolved ? this.value : "unresolved") + ") <t=" + this.targets.size() + ":d=" + this.dependencies.size() + ">";
    }
    
    public void resolve(final int value) {
        if (this.resolved) {
            return;
        }
        this.resolved = true;
        this.value = value;
        for (final Dependency node : this.dependencies) {
            node.update(node);
        }
    }
    
    @Override
    public void update(final Dependency node) {
        for (final DependencyNode target : this.targets) {
            if (!target.resolved) {
                return;
            }
        }
        this.readyToSolve = true;
        if (this.updateDelegate != null) {
            this.updateDelegate.update(this);
        }
        if (this.delegateToWidgetRun) {
            this.run.update(this);
            return;
        }
        DependencyNode target2 = null;
        int numTargets = 0;
        for (final DependencyNode t : this.targets) {
            if (t instanceof DimensionDependency) {
                continue;
            }
            target2 = t;
            ++numTargets;
        }
        if (target2 != null && numTargets == 1 && target2.resolved) {
            if (this.marginDependency != null) {
                if (!this.marginDependency.resolved) {
                    return;
                }
                this.margin = this.marginFactor * this.marginDependency.value;
            }
            this.resolve(target2.value + this.margin);
        }
        if (this.updateDelegate != null) {
            this.updateDelegate.update(this);
        }
    }
    
    public void addDependency(final Dependency dependency) {
        this.dependencies.add(dependency);
        if (this.resolved) {
            dependency.update(dependency);
        }
    }
    
    public String name() {
        String definition = this.run.widget.getDebugName();
        if (this.type == Type.LEFT || this.type == Type.RIGHT) {
            definition += "_HORIZONTAL";
        }
        else {
            definition += "_VERTICAL";
        }
        definition = definition + ":" + this.type.name();
        return definition;
    }
    
    public void clear() {
        this.targets.clear();
        this.dependencies.clear();
        this.resolved = false;
        this.value = 0;
        this.readyToSolve = false;
        this.delegateToWidgetRun = false;
    }
    
    enum Type
    {
        UNKNOWN, 
        HORIZONTAL_DIMENSION, 
        VERTICAL_DIMENSION, 
        LEFT, 
        RIGHT, 
        TOP, 
        BOTTOM, 
        BASELINE;
    }
}
