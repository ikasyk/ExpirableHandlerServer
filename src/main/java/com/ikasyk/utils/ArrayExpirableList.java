package com.ikasyk.utils;

import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * Project ≈
 * Created by igor, 26.07.16 12:16
 */

public class ArrayExpirableList<T> implements ExpirableList<T> {

    // Life time
    private long lifetime;

    // Counter of elements
    private int size;

    // Array of elements to store
    private ExpirableInspector.Element<T>[] elementData;

    /**
     * Constructor sets the time of life
     *
     * @param l the time of life
     */
    public ArrayExpirableList(long l) {
        this.lifetime = l;
        elementData = new ExpirableInspector.Element[10];
        size = 0;
    }

    /**
     * Constructor sets the time of life = 300 default
     */
    public ArrayExpirableList() {
        this(60000);
    }

    /**
     * @return the number of elements in this list
     */
    public int size() {
        return size;
    }

    /**
     * @return true if this list contains no elements
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * @return an iterator over the elements in this list
     */
    public ArrayExpirableList.Iterator<T> iterator() {
        return new Itr();
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element element to be appended to this list
     */
    public void add(T element) {
        rebuild();
        addElement(new ExpirableInspector.Element<T>(element, ExpirableInspector.getTime() + lifetime));
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position(if any) and any subsequent
     * elements to the right.
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public void add(int index, T element) {
        rebuild();
        addElement(index, new ExpirableInspector.Element<T>(element, ExpirableInspector.getTime() + lifetime));
    }


    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left.
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public T remove(int index) {
        rebuild();
        return removeElement(index).get();
    }

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present. If this list does not contain the element, it is
     * unchanged.
     *
     * @param o - element to be removed if present
     */
    public void remove(Object o) {
        rebuild();
        removeElement(o);
    }

    /**
     * Returns true if this list contains the specified element.
     *
     * @param o - element whose presence in this list is to be tested
     */
    public boolean contains(Object o) {
        rebuild();
        return containsElement(o);
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index - index of the element to return
     */
    public T get(int index) {
        rebuild();
        return getElement(index).get();
    }

    class Itr implements Iterator<T> {
        // Index of the next element to return
        private int cursor;

        // Index of the last returned element; -1 if no such
        private int lastRet = -1;

        /**
         * Reset start cursor to 0
         */
        public Itr() { this(0); }

        /**
         * Reset start cursor to index
         * @param index - position of first element
         */
        public Itr(int index) { this.cursor = index; }

        /**
         * Returns true if the iteration has more elements.
         */
        public boolean hasNext() {
            return cursor != size;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        public T next() {
            rebuild();
            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            cursor = i + 1;
            return (T) elementData[lastRet = i].get();
        }
    }

    /**
     * Delete all expired elements and rebuild list
     */
    private void rebuild() {
        int i = 0;
        ExpirableInspector.Element<T> element;
        while (i < size) {
            element = elementData[i];
            if (!ExpirableInspector.isExists(element)) {
                removeElement(i);
            } else
                i++;
        }
    }

    /**
     * Appends the specified element to the end of this list.
     *
     * @param e - element to be appended to this list
     */
    private void addElement(ExpirableInspector.Element<T> e) {
        ensureCapacity(size + 1);
        elementData[size++] = e;
    }

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position(if any) and any subsequent
     * elements to the right.
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void addElement(int index, ExpirableInspector.Element<T> element) {
        rangeCheckForAdd(index);
        ensureCapacity(size + 1);
        System.arraycopy(elementData, index, elementData, index + 1, size - index);
        elementData[index] = element;
        size++;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left.
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private ExpirableInspector.Element<T> removeElement(int index) {
        rangeCheck(index);
        ExpirableInspector.Element<T> oldValue = elementData[index];
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index, numMoved);
        elementData[--size] = null;
        return oldValue;
    }

    /**
     * Removes the first occurrence of the specified element from this list.
     * If this list does not contain the element, it is
     * unchanged.
     *
     * @param o the current element
     */
    private void removeElement(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index].get() == null) {
                    fastRemoveElement(index);
                    return;
                }
        } else {
            for (int index = 0; index < size; index++)
                if (o.equals(elementData[index].get())) { // CHECCKKKKKKKKKK
                    fastRemoveElement(index);
                    return;
                }
        }
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left.
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    private void fastRemoveElement(int index) {
        rangeCheck(index);
        int numMoved = size - index - 1;
        if (numMoved > 0)
            System.arraycopy(elementData, index+1, elementData, index, numMoved);
        elementData[--size] = null;
    }

    /**
     * Returns true if this list contains the specified element.
     *
     * @param o - element whose presence in this list is to be tested
     */
    boolean containsElement(Object o) {
        if (o == null) {
            for (int index = 0; index < size; index++)
                if (elementData[index].get() == null) {
                    return true;
                }
        } else {
            for (int index = 0; index < size; index++) {//System.out.println("Equals = " + o.equals(elementData[index].get()));
                if (o.equals(elementData[index].get())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index - index of the element to return
     */
    private ExpirableInspector.Element<T> getElement(int index) {
        return elementData[index];
    }

    /**
     * Reserve memory for elementData
     *
     * @param minCapacity - the length of elements
     */
    private void ensureCapacity(int minCapacity) {
        int oldCapacity = elementData.length;
        if (minCapacity > oldCapacity) {
            Object oldData[] = elementData;
            int newCapacity = (oldCapacity * 3) / 2 + 1;
            if (newCapacity < minCapacity)
                newCapacity = minCapacity;
            elementData = Arrays.copyOf(elementData, newCapacity);
        }
    }

    /**
     * Throws exception if position index is not in array
     * @param index
     */
    private void rangeCheck(int index) {
        if (index >= size)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }

    private void rangeCheckForAdd(int index) {
        if (index > size || index < 0)
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
    }
}