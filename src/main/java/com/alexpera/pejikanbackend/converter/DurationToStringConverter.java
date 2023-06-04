package com.alexpera.pejikanbackend.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class DurationToStringConverter implements Converter<Duration, String> {

    @Override
    public String convert(Duration from) {
        String hoursString;
        long hours = from.toHours();
        if (hours < 10) {
            hoursString = "0" + hours;
        } else {
            hoursString = Long.toString(hours);
        }
        int minutes = from.toMinutesPart();
        if (minutes < 10) {
            return hoursString + ":0" + minutes;
        } else {
            return hoursString + ":" + minutes;
        }
    }
}
