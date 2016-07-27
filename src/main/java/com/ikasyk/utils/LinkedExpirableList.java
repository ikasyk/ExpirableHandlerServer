package com.ikasyk.utils;

import java.util.NoSuchElementException;

/**
 * Project ExpireHandler
 * Created by igor, 27.07.16 12:28
 */

public class LinkedExpirableList<T> implements ExpirableList<T> {

    // Life time
    private long lifetime;

    // Counter of elements
    private int size;

    private Entry<T> header = new Entry<T>(null, null, null);

    /**
     * Constructor sets the time of life
     *
     * @param l the time of life
     */
    public LinkedExpirableList(long l) {
        this.lifetime = l;
        size = 0;
        header.next = header.prev = header;
    }

    /**
     * Constructor sets the time of life = 300 default
     */
    public LinkedExpirableList() {
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
        addBeforeElement(new ExpirableInspector.Element<T>(element, ExpirableInspector.getTime() + lifetime), header);
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
        addBeforeElement(new ExpirableInspector.Element<T>(element, ExpirableInspector.getTime() + lifetime), (index == size ? header : entryElement(index)));
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
        return removeElement(entryElement(index)).get();
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
        private int cursor = 0;

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
            return cursor != size();
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         * @throws NoSuchElementException if the iteration has no more elements
         */
        public T next() {
            rebuild();
            try {
                int i = cursor;
                ExpirableInspector.Element<T> next = getElement(i);
                lastRet = i;
                cursor = i + 1;
                return (T) next.get();
            } catch (IndexOutOfBoundsException e) {
                throw new NoSuchElementException();
            }
        }
    }

    /**
     * Delete all expired elements and rebuild list
     */
    private void rebuild() {
        Entry e = header.next;
        if (e == null) return;
        while (e != header) {
            if (!ExpirableInspector.isExists(e.element)) {
                Entry next = e.next;
                removeElement(e);
                e = next;
            } else e = e.next;
        }
    }


    /**
     * Appends the specified element to the end of this list.
     *
     * @param e - element to be appended to this list
     */
    private void addBeforeElement(ExpirableInspector.Element<T> e, Entry<T> entry) {
        Entry<T> newEntry = new Entry<T>(e, entry, entry.prev);
        newEntry.prev.next = newEntry;
        newEntry.next.prev = newEntry;
        size++;
    }

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left.
     * Returns the element that was removed from the list.
     *
     * @param e the entry to be removed
     * @return the element previously at the specified position
     * @throws NoSuchElementException if the entry not found
     */
    private ExpirableInspector.Element<T> removeElement(Entry<T> e) {
        if (e == header)
            throw new NoSuchElementException();

        ExpirableInspector.Element<T> result = e.element;
        e.prev.next = e.next;
        e.next.prev = e.prev;
        e.next = e.prev = null;
        e.element = null;
        size--;
        return result;
    }

    /**
     * Removes the first occurrence of the specified element from this list.
     * If this list does not contain the element, it is
     * unchanged.
     *
     * @param o the current element
     */
    private void removeElement(Object o) {
        if (o==null) {
            for (Entry<T> e = header.next; e != header; e = e.next) {
                if (e.element.get() == null) {
                    remove(e);
                    return;
                }
            }
        } else {
            for (Entry<T> e = header.next; e != header; e = e.next) {
                if (o.equals(e.element.get())) {
                    remove(e);
                    return;
                }
            }
        }
    }

    /**
     * Returns true if this list contains the specified element.
     *
     * @param o - element whose presence in this list is to be tested
     */
    boolean containsElement(Object o) {
        int index = 0;
        if (o==null) {
            for (Entry e = header.next; e != header; e = e.next) {
                if (e.element.get()==null)
                    return true;
            }
        } else {
            for (Entry e = header.next; e != header; e = e.next) {
                if (o.equals(e.element.get()))
                    return true;
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
        return entryElement(index).element;
    }

    private static class Entry<T> {
        ExpirableInspector.Element<T> element;
        Entry<T> next;
        Entry<T> prev;

        Entry(ExpirableInspector.Element<T> element, Entry<T> next, Entry<T> prev) {
            this.element = element;
            this.next = next;
            this.prev = prev;
        }
    }

    private Entry<T> entryElement(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index: "+index+
                    ", Size: "+size);
        Entry<T> e = header;
        if (index < (size >> 1)) {
            for (int i = 0; i <= index; i++)
                e = e.next;
        } else {
            for (int i = size; i > index; i--)
                e = e.prev;
        }
        return e;
    }

    private void rangeCheckForAdd(int index) {
        if (index < 0 || index > size())
            throw new IndexOutOfBoundsException("Index: "+index+", Size: "+size());
    }

}
