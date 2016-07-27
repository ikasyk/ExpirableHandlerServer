package com.ikasyk;

import com.ikasyk.utils.*;

/**
 * Project ExpireHandlerServer
 * Created by igor, 27.07.16 15:00
 */
public class ExpireHandlerServer {
    public static ArrayExpirableList<String> arrayList = new ArrayExpirableList<String>(10000);

    public static void main(String[] args) {
        System.out.print("Project starts");
    }
}
