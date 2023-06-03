package com.alexpera.pejikanbackend.service;

import java.util.Calendar;
import java.util.Date;

public class DateCalculator {
    /// https://stackoverflow.com/a/57437377
    public static Date getWeekStartDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }
    public static Date getWeekEndDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getWeekStartDate(date));
        cal.add(Calendar.DATE, 6);// last day of week
        return cal.getTime();
    }

    // todo add tests
}
