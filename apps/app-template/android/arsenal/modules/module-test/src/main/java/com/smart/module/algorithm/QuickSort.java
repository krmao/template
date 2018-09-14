package com.smart.module.algorithm;

public class QuickSort {

    private static void quickSort(final int array[], final int originLeft, final int originRight) {

        if (originLeft < originRight) {

            int baseValue = array[originLeft];
            int tmpLeft = originLeft;
            int tmpRight = originRight;

            while (tmpLeft != tmpRight) {

                while (tmpLeft < tmpRight && array[tmpRight] >= baseValue) tmpRight--;
                array[tmpLeft] = array[tmpRight];

                while (tmpLeft < tmpRight && array[tmpLeft] <= baseValue) tmpLeft++;
                array[tmpRight] = array[tmpLeft];

            }

            array[tmpRight] = baseValue;
            quickSort(array, originLeft, tmpLeft - 1);
            quickSort(array, tmpRight + 1, originRight);

        }

    }

    /**
     * @return 查找第 k 个大的值
     */
    private static int findKTHBigValue(final int array[], final int k) {


        return 0;
    }

    public void main() {

        int array[] = {10, 5, 4, 1, 5, 2, 7, 3, 0, 4, 9, 6, 8};

        System.out.println("排序之前：");
        for (int element : array) System.out.print(element + " ");

        quickSort(array, 0, array.length - 1);

        System.out.println("\n排序之后：");
        for (int element : array) System.out.print(element + " ");

    }
}
