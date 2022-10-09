package com.code.wlu.namrata.androidassignments;

import junit.framework.TestCase;

public class LoginActivityTest extends TestCase {

    public void testOnCreate() {
        assertTrue((LoginActivity.ACTIVITY_NAME == "LoginActivity"));
    }

    public void testIsEmailValid() {
            Boolean data = LoginActivity.isEmailValid("namfrgthy");
            Boolean Data2 = LoginActivity.isEmailValid("namrata@gmail.com");
            assertFalse(data);
            assertTrue(Data2);
    }

    public void testIsValidPassword() {
        Boolean data = LoginActivity.isValidPassword("nam");
        Boolean data2 = LoginActivity.isValidPassword("");
        assertFalse(data2);
        assertTrue(data);
    }

    public void testPrint() {
    }

    public void testOnResume() {
        assertTrue((LoginActivity.ACTIVITY_NAME == "LoginActivity"));
    }

    public void testOnStart() {
        assertTrue((LoginActivity.ACTIVITY_NAME == "LoginActivity"));
    }

    public void testOnPause() {
        assertTrue((LoginActivity.ACTIVITY_NAME == "LoginActivity"));
    }

    public void testOnStop() {
    }

    public void testOnDestroy() {
    }
}