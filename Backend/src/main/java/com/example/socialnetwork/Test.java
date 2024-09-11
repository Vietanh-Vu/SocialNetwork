package com.example.socialnetwork;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String url = "https://ghtk-socialnetwork.s3.ap-southeast-2.amazonaws.com/images/02282ff2-7a79-4e60-92c4-38fd148c711a.png";
//        String regex = ".*/([^/]+)\\.png$";
        String regex = ".*(images/[^/]+\\.png)$";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            String result = matcher.group(1);
            System.out.println("Result: " + result);
        } else {
            System.out.println("No match found");
        }
    }
}
