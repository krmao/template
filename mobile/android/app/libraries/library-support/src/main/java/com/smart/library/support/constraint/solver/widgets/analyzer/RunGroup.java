package com.smart.library.support.constraint.solver.widgets.analyzer;

import com.smart.library.support.constraint.solver.widgets.*;
import java.util.*;

class RunGroup
{
    public static final int START = 0;
    public static final int END = 1;
    public static final int BASELINE = 2;
    public static int index;
    public int position;
    public boolean dual;
    WidgetRun firstRun;
    WidgetRun lastRun;
    ArrayList<WidgetRun> runs;
    int groupIndex;
    int direction;
    
    public RunGroup(final WidgetRun run, final int dir) {
        this.position = 0;
        this.dual = false;
        this.firstRun = null;
        this.lastRun = null;
        this.runs = new ArrayList<WidgetRun>();
        this.groupIndex = 0;
        this.groupIndex = RunGroup.index;
        ++RunGroup.index;
        this.firstRun = run;
        this.lastRun = run;
        this.direction = dir;
    }
    
    public void add(final WidgetRun run) {
        this.runs.add(run);
        this.lastRun = run;
    }
    
    private long traverseStart(final DependencyNode node, final long startPosition) {
        final WidgetRun run = node.run;
        if (run instanceof HelperReferences) {
            return startPosition;
        }
        long position = startPosition;
        for (int count = node.dependencies.size(), i = 0; i < count; ++i) {
            final Dependency dependency = node.dependencies.get(i);
            if (dependency instanceof DependencyNode) {
                final DependencyNode nextNode = (DependencyNode)dependency;
                if (nextNode.run != run) {
                    position = Math.max(position, this.traverseStart(nextNode, startPosition + nextNode.margin));
                }
            }
        }
        if (node == run.start) {
            final long dimension = run.getWrapDimension();
            position = Math.max(position, this.traverseStart(run.end, startPosition + dimension));
            position = Math.max(position, startPosition + dimension - run.end.margin);
        }
        return position;
    }
    
    private long traverseEnd(final DependencyNode node, final long startPosition) {
        final WidgetRun run = node.run;
        if (run instanceof HelperReferences) {
            return startPosition;
        }
        long position = startPosition;
        for (int count = node.dependencies.size(), i = 0; i < count; ++i) {
            final Dependency dependency = node.dependencies.get(i);
            if (dependency instanceof DependencyNode) {
                final DependencyNode nextNode = (DependencyNode)dependency;
                if (nextNode.run != run) {
                    position = Math.min(position, this.traverseEnd(nextNode, startPosition + nextNode.margin));
                }
            }
        }
        if (node == run.end) {
            final long dimension = run.getWrapDimension();
            position = Math.min(position, this.traverseEnd(run.start, startPosition - dimension));
            position = Math.min(position, startPosition - dimension - run.start.margin);
        }
        return position;
    }
    
    public long computeWrapSize(final ConstraintWidgetContainer container, final int orientation) {
        if (this.firstRun instanceof ChainRun) {
            final ChainRun chainRun = (ChainRun)this.firstRun;
            if (chainRun.orientation != orientation) {
                return 0L;
            }
        }
        else if (orientation == 0) {
            if (!(this.firstRun instanceof HorizontalWidgetRun)) {
                return 0L;
            }
        }
        else if (!(this.firstRun instanceof VerticalWidgetRun)) {
            return 0L;
        }
        final DependencyNode containerStart = (orientation == 0) ? container.horizontalRun.start : container.verticalRun.start;
        final DependencyNode containerEnd = (orientation == 0) ? container.horizontalRun.end : container.verticalRun.end;
        final boolean runWithStartTarget = this.firstRun.start.targets.contains(containerStart);
        final boolean runWithEndTarget = this.firstRun.end.targets.contains(containerEnd);
        long dimension = this.firstRun.getWrapDimension();
        if (runWithStartTarget && runWithEndTarget) {
            final long maxPosition = this.traverseStart(this.firstRun.start, 0L);
            final long minPosition = this.traverseEnd(this.firstRun.end, 0L);
            long endGap = maxPosition - dimension;
            if (endGap >= -this.firstRun.end.margin) {
                endGap += this.firstRun.end.margin;
            }
            long startGap = -minPosition - dimension - this.firstRun.start.margin;
            if (startGap >= this.firstRun.start.margin) {
                startGap -= this.firstRun.start.margin;
            }
            final float bias = this.firstRun.widget.getBiasPercent(orientation);
            long gap = 0L;
            if (bias > 0.0f) {
                gap = (long)(startGap / bias + endGap / (1.0f - bias));
            }
            startGap = (long)(0.5f + gap * bias);
            endGap = (long)(0.5f + gap * (1.0f - bias));
            final long runDimension = startGap + dimension + endGap;
            dimension = this.firstRun.start.margin + runDimension - this.firstRun.end.margin;
        }
        else if (runWithStartTarget) {
            final long maxPosition = this.traverseStart(this.firstRun.start, this.firstRun.start.margin);
            final long runDimension2 = this.firstRun.start.margin + dimension;
            dimension = Math.max(maxPosition, runDimension2);
        }
        else if (runWithEndTarget) {
            final long minPosition2 = this.traverseEnd(this.firstRun.end, this.firstRun.end.margin);
            final long runDimension2 = -this.firstRun.end.margin + dimension;
            dimension = Math.max(-minPosition2, runDimension2);
        }
        else {
            dimension = this.firstRun.start.margin + this.firstRun.getWrapDimension() - this.firstRun.end.margin;
        }
        return dimension;
    }
    
    private boolean defineTerminalWidget(final WidgetRun run, final int orientation) {
        if (!run.widget.isTerminalWidget[orientation]) {
            return false;
        }
        for (final Dependency dependency : run.start.dependencies) {
            if (dependency instanceof DependencyNode) {
                final DependencyNode node = (DependencyNode)dependency;
                if (node.run == run) {
                    continue;
                }
                if (node != node.run.start) {
                    continue;
                }
                if (run instanceof ChainRun) {
                    final ChainRun chainRun = (ChainRun)run;
                    for (final WidgetRun widgetChainRun : chainRun.widgets) {
                        this.defineTerminalWidget(widgetChainRun, orientation);
                    }
                }
                else if (!(run instanceof HelperReferences)) {
                    run.widget.isTerminalWidget[orientation] = false;
                }
                this.defineTerminalWidget(node.run, orientation);
            }
        }
        for (final Dependency dependency : run.end.dependencies) {
            if (dependency instanceof DependencyNode) {
                final DependencyNode node = (DependencyNode)dependency;
                if (node.run == run) {
                    continue;
                }
                if (node != node.run.start) {
                    continue;
                }
                if (run instanceof ChainRun) {
                    final ChainRun chainRun = (ChainRun)run;
                    for (final WidgetRun widgetChainRun : chainRun.widgets) {
                        this.defineTerminalWidget(widgetChainRun, orientation);
                    }
                }
                else if (!(run instanceof HelperReferences)) {
                    run.widget.isTerminalWidget[orientation] = false;
                }
                this.defineTerminalWidget(node.run, orientation);
            }
        }
        return false;
    }
    
    public void defineTerminalWidgets(final boolean horizontalCheck, final boolean verticalCheck) {
        if (horizontalCheck && this.firstRun instanceof HorizontalWidgetRun) {
            this.defineTerminalWidget(this.firstRun, 0);
        }
        if (verticalCheck && this.firstRun instanceof VerticalWidgetRun) {
            this.defineTerminalWidget(this.firstRun, 1);
        }
    }
}
