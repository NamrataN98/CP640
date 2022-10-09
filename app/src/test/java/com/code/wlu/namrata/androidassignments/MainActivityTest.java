package com.code.wlu.namrata.androidassignments;

import static org.junit.Assert.assertFalse;

import android.util.Log;
import android.widget.Button;

import junit.framework.TestCase;

public class MainActivityTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void tearDown() throws Exception {
    }

    public void testOnActivityResult() {
    }

    public void testOnCreate() {
        assertTrue((MainActivity.ACTIVITY_NAME == "MainActivity"));
    }


    public void testOnResume() {
        assertTrue((MainActivity.ACTIVITY_NAME == "MainActivity"));
        LogTest.i(MainActivity.ACTIVITY_NAME, "In onResume()");
    }

    public void testOnStart() {
        assertTrue((MainActivity.ACTIVITY_NAME == "MainActivity"));
    }

    public void testOnPause() {
        assertTrue((MainActivity.ACTIVITY_NAME == "MainActivity"));
    }

    public void testOnStop() {
    }

    public void testOnDestroy() {
    }
}