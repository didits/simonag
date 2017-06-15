package com.simonag.simonag.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by diditsepiyanto on 11/7/16.
 */

public class RegexInput {

    public boolean EmailValidator(String input) {
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        return check(pattern, input);
    }

    boolean NamaValidator(String input) {
        Pattern pattern = Pattern.compile("[A-Za-z ]{5,20}$");
        return check(pattern, input);
    }

    public boolean NomerValidator(String input) {
        Pattern pattern = Pattern.compile("^[0-9]{0,50}$");
        return check(pattern, input);
    }

    boolean PasswordValidator(String input){
        Pattern pattern = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$");
        return check(pattern, input);
    }

    private boolean check(Pattern pattern, String input){
        Matcher matcher = pattern.matcher(input);
        return matcher.find();
    }

}
