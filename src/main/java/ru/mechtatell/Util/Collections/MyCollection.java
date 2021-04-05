package ru.mechtatell.Util.Collections;

import java.util.Comparator;
import java.util.EmptyStackException;
import java.util.Optional;

public class MyCollection<T> implements AdvancedList<T>, AuthorHolder {

    private int maxCount = 100;
    private Object[] array = new Object[maxCount];
    private int count = 0;


    @Override
    public void add(T item) {
        array[count] = item;
        count++;

        if (count > maxCount * 0.8) {
            resize();
        }
    }

    @Override
    public void insert(int index, T item) throws Exception {
        if (index < 0 || index > count) {
            throw new IndexOutOfBoundsException();
        }

        if (array[index] != null) {
            for (int i = count; i > index; i--) {
                array[i] = array[i - 1];
            }
        }
        array[index] = item;

        count++;

        if (count > maxCount * 0.8) {
            resize();
        }
    }

    @Override
    public void remove(int index) throws Exception {
        if (index < 0 || index >= count) {
            throw new IndexOutOfBoundsException();
        }
        if (isEmpty()) {
            throw new EmptyStackException();
        }

        for (int i = index; i < count - 1; i++) {
            array[i] = array[i + 1];
        }
        count--;
    }

    @Override
    public Optional<T> get(int index) {
        if (index < 0 || index >= count) {
            return Optional.empty();
        } else {
            return Optional.ofNullable((T) array[index]);
        }
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public void addAll(SimpleList<T> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isPresent()) {
                this.add(list.get(i).get());
            }
        }
    }

    @Override
    public int first(T item) {
        for (int i = 0; i < count; i++) {
            if (item == array[i]) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public int last(T item) {
        for (int i = count - 1; i >= 0; i--) {
            if (item == array[i]) {
                return i;
            }
        }

        return -1;
    }

    @Override
    public boolean contains(T item) {
        for (int i = 0; i < count; i++) {
            if (item == array[i]) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isEmpty() {
        return count == 0;
    }

    @Override
    public AdvancedList<T> shuffle() {
        MyCollection<T> list = new MyCollection<>();
        list.array = this.array.clone();
        list.count = this.count;
        for (int i = 0; i < count; i++) {
            Object temp = list.array[i];
            int newIndex = (int) (Math.random() * count);
            list.array[i] = list.array[newIndex];
            list.array[newIndex] = temp;
        }

        return list;
    }

    @Override
    public AdvancedList<T> sort(Comparator<T> comparator) {
        MyCollection<T> list = new MyCollection<>();
        TimSort<T> timSort = new TimSort<>(comparator);
        list.array = this.array.clone();
        list.count = this.count;
        timSort.timSort(list.array, size());
        return list;
    }

    private void resize() {
        maxCount *= 2;
        Object[] array = new Object[maxCount];
        for (int i = 0; i < count; i++) {
            array[i] = this.array[i];
        }
        this.array = array;
    }

    @Override
    public String author() {
        return "Ivan Kalachikov";
    }
}
