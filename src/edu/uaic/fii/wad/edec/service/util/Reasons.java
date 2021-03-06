package edu.uaic.fii.wad.edec.service.util;

public final class Reasons {

    public static String getReasonURLFromId(int ruleType, int reasonId) {
        String reasonURL = "/filter_reasons/";

        switch (ruleType) {
            case 0: {
                if (reasonId == 0) {
                    reasonURL += "1";
                } else if (reasonId == 1) {
                    reasonURL += "3";
                } else if (reasonId == 2) {
                    reasonURL += "2";
                } else if (reasonId == 3) {
                    reasonURL += "10";
                }
                break;
            }

            case 1: {
                if (reasonId == 0) {
                    reasonURL += "7";
                } else if (reasonId == 1) {
                    reasonURL += "8";
                } else if (reasonId == 2) {
                    reasonURL += "9";
                } else if (reasonId == 3) {
                    reasonURL += "12";
                }
                break;
            }

            case 2: {
                if (reasonId == 0) {
                    reasonURL += "4";
                } else if (reasonId == 1) {
                    reasonURL += "5";
                } else if (reasonId == 2) {
                    reasonURL += "6";
                } else if (reasonId == 3) {
                    reasonURL += "11";
                }
                break;
            }
        }

        return reasonURL;
    }

    public static int getReasonIdFromURL(int ruleType, String reasonURL) {
        int reasonIdToken = Integer.parseInt(reasonURL.substring(reasonURL.length() - 1));
        int reasonId = -1;

        switch (ruleType) {
            case 0: {
                if (reasonIdToken == 1) {
                    reasonId = 0;
                } else if (reasonIdToken == 3) {
                    reasonId = 1;
                } else if (reasonIdToken == 2) {
                    reasonId = 2;
                } else if (reasonIdToken == 10) {
                    reasonId = 3;
                }
                break;
            }

            case 1: {
                if (reasonIdToken == 7) {
                    reasonId = 0;
                } else if (reasonIdToken == 8) {
                    reasonId = 1;
                } else if (reasonIdToken == 9) {
                    reasonId = 2;
                } else if (reasonIdToken == 12) {
                    reasonId = 3;
                }
                break;
            }

            case 2: {
                if (reasonIdToken == 4) {
                    reasonId = 0;
                } else if (reasonIdToken == 5) {
                    reasonId = 1;
                } else if (reasonIdToken == 6) {
                    reasonId = 2;
                } else if (reasonIdToken == 11) {
                    reasonId = 3;
                }
                break;
            }
        }

        return reasonId;
    }
}
