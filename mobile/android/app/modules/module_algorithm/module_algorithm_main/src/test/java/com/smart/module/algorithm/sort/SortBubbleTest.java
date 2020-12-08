package com.smart.module.algorithm.sort;

public class SortBubbleTest {

    @org.junit.Test
    public void main() {
        SortBubble bubble = new SortBubble();
        int[] originArray = ArrayUtil.ARRAY_TEST;

        System.out.println("array before sort ->");
        for (int element : originArray) System.out.print(element + " ");

        long timeStart = System.currentTimeMillis();
        int[] sortedArray = bubble.sortArray(originArray);
        long timeDuration = System.currentTimeMillis() - timeStart;
        System.out.println("\narray after sort (" + timeDuration + "ms) ->");

        for (int element : sortedArray) System.out.print(element + " ");
    }

}