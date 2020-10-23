package com.example.transit;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.espresso.Espresso;
import androidx.test.rule.ActivityTestRule;

import com.example.transit.Common.ForgetPassword;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

public class ForgetPwdTest {

    @Rule
    public ActivityTestRule<Login> loginActivityTestRule = new ActivityTestRule<Login>(Login.class);

    private Login loginActivity = null;

    Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(ForgetPassword.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        loginActivity = loginActivityTestRule.getActivity();
    }

    @Test
    public void testLaunchForgetPasswordActivity(){
        assertNotNull(loginActivity.findViewById(R.id.forget_pwd));

        Espresso.onView(withId(R.id.forget_pwd)).perform(click());

        Activity forget_pwd = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);

        assertNotNull(forget_pwd);
    }

    @After
    public void tearDown() throws Exception {
        loginActivity = null;
    }
}