package com.brainflow.utils;

/**
 * Created by IntelliJ IDEA.
 * User: buchs
 * Date: Aug 8, 2006
 * Time: 10:26:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class Junk {


    public static String getHandler(String[] config, String requestUri) {

        int matchIdx = 0;
        int matchLen = 0;

        boolean inconclusive = true;
        boolean matchWrong = false;

        for (int i = 0; i < config.length; i += 2) {
            String matchStr = config[i];

            int count = 0;
            int maxCount = Math.min(matchStr.length(), requestUri.length());
            boolean wrong = false;
            while (count < maxCount) {
                if (requestUri.charAt(count) == matchStr.charAt(count)) {
                    count++;
                } else {
                    wrong = true;
                    break;
                }
            }
            if (count == requestUri.length()) {
                return config[i + 1];
            } else if (count > matchLen) {
                matchWrong = wrong;
                matchLen = count;
                matchIdx = i;
                inconclusive = false;
            } else if (count == matchLen) {
                if (matchWrong)
                    inconclusive = true;
            }

            System.out.println("matchlen " + matchLen);
        }

        if (inconclusive) {
            return "TEXwMv";
        } else {
            return config[matchIdx + 1];
        }


    }


    public static void main(String[] args) {

        String[] config = new String[]{"/", "TestServlet", "/test", "TestServlet2"};
        String request = "/servlet/TestServlet";

        System.out.println(Junk.getHandler(config, request));
    }
}
