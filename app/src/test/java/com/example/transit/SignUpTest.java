package com.example.transit;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class SignUpTest {

    /**
     * Positive Test case to validate Phnoe No
     * @throws Exception
     */

    @Test
    public void validatePhoneNo_true() throws Exception{

        String phoneNo = "0768856084";

        SignupThird signUp = new SignupThird();
        boolean valid = signUp.validatePhoneNo(phoneNo);

        assertEquals(true,valid);
    }

    /**
     * Negative Test case to validate Phone No
     * @throws Exception
     */

    @Test
    public void validatePhoneNo_false() throws Exception{

        String phoneNo = "";

        SignupThird signUp = new SignupThird();
        boolean valid = signUp.validatePhoneNo(phoneNo);

        assertEquals(false,valid);
    }
}
