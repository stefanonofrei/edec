package edu.uaic.fii.wad.edec.service.util;

public final class Reasons {

    public static String getReasonURL(int ruleType, int reasonId) {
        String reasonURL = "/filter_reasons/";

        switch (ruleType) {
            case 0: {
                if (reasonId == 0) {
                    reasonURL += "1";
                } else if (reasonId == 1) {
                    reasonURL += "3";
                } else {
                    reasonURL += "2";
                }
                break;
            }

            case 1: {
                if (reasonId == 0) {
                    reasonURL += "7";
                } else if (reasonId == 1) {
                    reasonURL += "8";
                } else {
                    reasonURL += "9";
                }
                break;
            }

            case 2: {
                if (reasonId == 0) {
                    reasonURL += "4";
                } else if (reasonId == 1) {
                    reasonURL += "5";
                } else {
                    reasonURL += "6";
                }
                break;
            }
        }

        return reasonURL;
    }
}
