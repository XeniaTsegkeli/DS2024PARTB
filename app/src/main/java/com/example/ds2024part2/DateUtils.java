package com.example.ds2024part2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

class DateUtils {

    public static List<Date> parseDateRange(String dateRangeString) {
        List<Date> dates = new ArrayList<>();
        String[] parts = dateRangeString.split(" - ");
        if (parts.length == 2) {
            try {
                Date startDate = parseDate(parts[0]);
                Date endDate = parseDate(parts[1]);
                if (startDate != null && endDate != null) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(startDate);
                    while (!calendar.getTime().after(endDate)) {
                        dates.add(calendar.getTime());
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return dates;
    }

    private static Date parseDate(String dateString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Date date = sdf.parse(dateString);

        // Set time fields to zero
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Adjust for timezone offset
        calendar.setTimeZone(TimeZone.getDefault());

        return calendar.getTime();
    }
}
