package com.ikasyk.utils;

import java.util.NoSuchElementException;

/**
 * Project ExpireHandlerServer
 * Created by igor, 27.07.16 15:54
 */
public class ListController {
    public static LinkedExpirableList<String> list = new LinkedExpirableList<String>(10000);

    public static void add(String line) {
        list.add(line);
    }
    public static String getAll() throws NoSuchElementException {
        LinkedExpirableList.Iterator<String> i = list.iterator();
        String result = "<h1><pre>";
        while (i.hasNext()) {
            result += i.next() + "\n";
        }
        result += "</pre></h1>";
        return result;
    }
}
