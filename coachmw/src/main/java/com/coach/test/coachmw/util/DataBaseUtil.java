package com.coach.test.coachmw.util;

/**
 * Created by Administrator on 2017-02-17.
 */

public final class DataBaseUtil {
    public static int SEX_MALE = 1;
    public static int SEX_FEMALE = 2;

    public static int toSexInteger(String sex) {
        if(sex.equals("남자"))
            return SEX_MALE;
        else
            return SEX_FEMALE;
    }
}