package com.PatronageSystem.utils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {


    /*
     * 获取昨天日期
     */
    public static Date getYesday() {
        Calendar c = Calendar.getInstance();
        Date today = initDateByDay();//获取今天的日期

        c.setTime(today);

        c.add(Calendar.DAY_OF_MONTH, 1);

        Date tomorrow = c.getTime();//这是明天

        c.setTime(today);

        c.add(Calendar.DAY_OF_MONTH, -1);

        return c.getTime();
    }

    /**
     * 获得当天零时零分零秒
     *
     * @return
     */
    public static Date initDateByDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }
}
