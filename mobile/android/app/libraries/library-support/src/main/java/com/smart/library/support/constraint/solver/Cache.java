package com.smart.library.support.constraint.solver;

import com.smart.library.support.constraint.solver.ArrayRow;
import com.smart.library.support.constraint.solver.Pools;
import com.smart.library.support.constraint.solver.SolverVariable;

public class Cache
{
    Pools.Pool<ArrayRow> arrayRowPool;
    Pools.Pool<SolverVariable> solverVariablePool;
    SolverVariable[] mIndexedVariables;
    
    public Cache() {
        this.arrayRowPool = new Pools.SimplePool<ArrayRow>(256);
        this.solverVariablePool = new Pools.SimplePool<SolverVariable>(256);
        this.mIndexedVariables = new SolverVariable[32];
    }
}
