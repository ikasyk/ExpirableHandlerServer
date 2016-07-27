package com.ikasyk.utils;

/**
 * Represents List of elements with a shelf time (an element has to be removed after specified time)
 * The time-to-life is specified in the list constructor. Element's countdown starts after adding it to the list.
 */
interface ExpirableList<T> {

    /**
     * @return the number of elements in this list
     */
    int size();

    /**
     * @return true if this list contains no elements
     */
    boolean isEmpty();

    /**
     * @return an iterator over the elements in this list
     */
    Iterator<T> iterator();

    /**
     * Appends the specified element to the end of this list.
     *
     * @param element element to be appended to this list
     */
    void add(T element);

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position(if any) and any subsequent
     * elements to the right.
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    void add(int index, T element);


    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left.
     * Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    T remove(int index);

    /**
     * Removes the first occurrence of the specified element from this list,
     * if it is present. If this list does not contain the element, it is
     * unchanged.
     *
     * @param o - element to be removed if present
     */
    void remove(Object o);

    /**
     * Returns true if this list contains the specified element. 
     *
     * @param o - element whose presence in this list is to be tested
     */
    boolean contains(Object o);

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index - index of the element to return
     */ 
    T get(int index);

    interface Iterator<T> {

        /**
         * Returns true if the iteration has more elements.
         */
        boolean hasNext();

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        T next();
    }
}
