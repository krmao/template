package com.smart.library.support.constraint.solver;

final class Pools
{
    private static final boolean DEBUG = false;
    
    static class SimplePool<T> implements Pool<T>
    {
        private final Object[] mPool;
        private int mPoolSize;
        
        SimplePool(final int maxPoolSize) {
            if (maxPoolSize <= 0) {
                throw new IllegalArgumentException("The max pool size must be > 0");
            }
            this.mPool = new Object[maxPoolSize];
        }
        
        @Override
        public T acquire() {
            if (this.mPoolSize > 0) {
                final int lastPooledIndex = this.mPoolSize - 1;
                final T instance = (T)this.mPool[lastPooledIndex];
                this.mPool[lastPooledIndex] = null;
                --this.mPoolSize;
                return instance;
            }
            return null;
        }
        
        @Override
        public boolean release(final T instance) {
            if (this.mPoolSize < this.mPool.length) {
                this.mPool[this.mPoolSize] = instance;
                ++this.mPoolSize;
                return true;
            }
            return false;
        }
        
        @Override
        public void releaseAll(final T[] variables, int count) {
            if (count > variables.length) {
                count = variables.length;
            }
            for (final T instance : variables) {
                if (this.mPoolSize < this.mPool.length) {
                    this.mPool[this.mPoolSize] = instance;
                    ++this.mPoolSize;
                }
            }
        }
        
        private boolean isInPool(final T instance) {
            for (int i = 0; i < this.mPoolSize; ++i) {
                if (this.mPool[i] == instance) {
                    return true;
                }
            }
            return false;
        }
    }
    
    interface Pool<T>
    {
        T acquire();
        
        boolean release(final T p0);
        
        void releaseAll(final T[] p0, final int p1);
    }
}
