package ru.mechtatell.Collections;

import java.util.Comparator;

public class TimSort<T> {

    private final Comparator<T> comparator;

    public TimSort(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    /*
    Получение минимальной длинны для упорядоченных подмассивов
    Используются побитовые операции для получения числа близкому к оптимальному при работе алгоритма
    Число дожно быть больше 8 и меньше 256, в идеале быть степенью двойки
    Берутся старшие 6 бит (size >= 32) от размера массива и если в оставшихся младших подбитах есть
    хотя бы один ненулевой элемент, к 6 старшим битам прибавляется единица.
     */
    public int minRunLength(int size) {
        int r = 0;
        while (size >= 32) {
            r |= (size & 1);
            size >>= 1;
        }
        return size + r;
    }

    /*
    Подмассивы упорядочиваются обычной сортировкой вставками
    Для упорядочивания конкретных частей массива в метод были добавлены аргументы left и right,
    которые представляет собой начальный и конечный индекс сортируемого подмассива
     */
    public void insertionSort(Object[] array, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            T temp = (T) array[i];
            int j = i - 1;
            while (j >= left && comparator.compare((T) array[j], temp) > 0) {
                array[j + 1] = array[j];
                j--;
            }
            array[j + 1] = temp;
        }
    }

    /*
    После упорядоченные подмассивы сортируются сортировкой слияния
    Вначале алгоритм берет два подмассива (уже отсортированных), после чего начинает сравнивать элементы
    подмассивов между собой и заносить их в массив в порядке возрастания
     */
    public void mergeSort(Object[] array, int leftBorder, int middle, int rightBorder) {
        int leftArraySize = middle - leftBorder + 1;
        int rightArraySize = rightBorder - middle;
        if (rightArraySize < 0) rightArraySize = 0;

        Object[] leftArray = new Object[leftArraySize];
        Object[] rightArray = new Object[rightArraySize];

        for (int i = 0; i < leftArraySize; i++) {
            leftArray[i] = array[leftBorder + i];
        }

        for (int i = 0; i < rightArraySize; i++) {
            rightArray[i] = array[middle + i + 1];
        }

        int leftArrayIndex = 0;
        int rightArrayIndex = 0;
        int arrayIndex = leftBorder;

        while (leftArrayIndex < leftArraySize && rightArrayIndex < rightArraySize) {
            if (comparator.compare((T) leftArray[leftArrayIndex], (T) rightArray[rightArrayIndex]) <= 0) {
                array[arrayIndex] = leftArray[leftArrayIndex];
                leftArrayIndex++;
            } else {
                array[arrayIndex] = rightArray[rightArrayIndex];
                rightArrayIndex++;
            }

            arrayIndex++;
        }

        while (leftArrayIndex < leftArraySize) {
            array[arrayIndex] = leftArray[leftArrayIndex];
            leftArrayIndex++;

            arrayIndex++;
        }

        while (rightArrayIndex < rightArraySize) {
            array[arrayIndex] = rightArray[rightArrayIndex];
            rightArrayIndex++;

            arrayIndex++;
        }
    }


    public void timSort(Object[] arr, int size) {
        //Получаем минимальную длинну подмассива
        int minRunLength = minRunLength(size);

        //Сортируем подмассивы сортировкой вставками
        for (int i = 0; i < size; i += minRunLength) {
            insertionSort(arr, i, Math.min(i + 31, size - 1));
        }

        //Разбиваем массив на подмассивы
        for (int runSize = minRunLength; runSize < size; runSize *= 2) {

            //Определеяем границы подмассивов
            for (int left = 0; left < size; left += runSize * 2) {
                int mid = left + runSize - 1;
                int right = Math.min(left + runSize * 2 - 1, size - 1);

                //Сортируем подмассивы сортировкой слияния
                mergeSort(arr, left, mid, right);
            }
        }
    }
}
