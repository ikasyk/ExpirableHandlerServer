package com.ikasyk.utils;

import java.util.Date;

/**
 * Created by igor on 26.07.16.
 */
class ExpirableInspector {

    public static class Element<T> {

        // Time when the element has been added
        private long expired;

        // Element container
        private transient Object data;

        /**
         * Creates new element
         *
         * @param o - the current object
         * @param ex - the time when element died
         */
        Element(T o, long ex) {
            set(o);
            setExpired(ex);
        }

        /**
         * Sets the the time when the element is expired
         *
         * @param time the setted expire time
         */
        void setExpired(long time) {
            this.expired = time;
        }

        /**
         * @return the time when the element is expired
         */
        long getExpired() {
            return this.expired;
        }

        /**
         * Sets the current element
         *
         * @param data the current element
         */
        void set(T data) {
            this.data = data;
        }

        /**
         * @return the current element
         */
        T get() {
            return (T) this.data;
        }

        /**
         * @element the another element
         * @return true if the current data and element are equals
         */
        public boolean equals(Object element) {
            return get().equals(element);
        }
    }

    /**
     * @return the current timestamp
     */
    public static long getTime() {
        Date date = new Date();
        return date.getTime();
    }

    /**
     * @param element the element of list
     * @return is the element lived
     */
    public static boolean isExists(ExpirableInspector.Element element) {
        long time = ExpirableInspector.getTime();
        if (time <= element.getExpired()) {
            return true;
        } else {
            return false;
        }
    }
}
