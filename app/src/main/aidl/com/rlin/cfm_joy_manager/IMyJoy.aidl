// IMyJoy.aidl
package com.rlin.cfm_joy_manager;

// Declare any non-default types here with import statements

interface IMyJoy {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    List<String> getJoyFiles() = 1;
    String readJoyFile(String fileName) = 2;
    int writeJoyFile(String fileName, String content) = 3;
    void destory() = 16777114;
}