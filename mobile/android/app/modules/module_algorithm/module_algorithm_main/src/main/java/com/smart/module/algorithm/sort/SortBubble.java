package com.smart.module.algorithm.sort;

@SuppressWarnings("WeakerAccess")
public class SortBubble {

    public int[] sortArray(final int[] nums) {
        if (nums == null || nums.length <= 1) {
            return nums;
        }

        int length = nums.length;
        int tmp;

        for (int i = 0; i < length - 1; i++) {
            boolean didSwap = false;
            for (int j = 0; j < length - 1 - i; j++) {
                if (nums[j] > nums[j + 1]) {

                    tmp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = tmp;

                    didSwap = true;
                }
            }

            if (!didSwap) {
                return nums;
            }
        }
        return nums;
    }

}