package com.example.chaoshan.utils;


import android.text.format.Time;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class DateUtils {

    /**
     * 判断当前系统时间是否在指定时间的范围内
     * <p>
     * beginHour 开始小时,例如22
     * beginMin  开始小时的分钟数,例如30
     * endHour   结束小时,例如 8
     * endMin    结束小时的分钟数,例如0
     * true表示在范围内, 否则false
     */
    public static boolean isCurrentInTimeScope(int beginHour, int beginMin, int endHour, int endMin) {
        boolean result = false;
        final long aDayInMillis = 1000 * 60 * 60 * 24;
        final long currentTimeMillis = System.currentTimeMillis();
        Time now = new Time();
        now.set(currentTimeMillis);
        Time startTime = new Time();
        startTime.set(currentTimeMillis);
        startTime.hour = beginHour;
        startTime.minute = beginMin;
        Time endTime = new Time();
        endTime.set(currentTimeMillis);
        endTime.hour = endHour;
        endTime.minute = endMin;
        // 跨天的特殊情况(比如22:00-8:00)
        if (!startTime.before(endTime)) {
            startTime.set(startTime.toMillis(true) - aDayInMillis);
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
            Time startTimeInThisDay = new Time();
            startTimeInThisDay.set(startTime.toMillis(true) + aDayInMillis);
            if (!now.before(startTimeInThisDay)) {
                result = true;
            }
        } else {
            //普通情况(比如 8:00 - 14:00)
            result = !now.before(startTime) && !now.after(endTime); // startTime <= now <= endTime
        }
        return result;
    }



    /*
     * @desc 字符串转时间戳
     * @example time="2019-04-19 00:00:00"
     **/

    public static long getTimestamp(String time) {
        long timestamp = 0;
        try {
            timestamp = Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                    Locale.CHINA).parse(time)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public static long getTimestamp() {
        long timestamp = 0;
        try {
            timestamp = Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd",
                    Locale.CHINA).parse(getStringDate())).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("TAG", "getTimestamp: "+timestamp);
        return timestamp;
    }


    /*
     * @desc 字符串转时间戳
     * @example time="2019-04-19 00:00:00"
     **/

    public static Long getTimestampNotSecond(String time) {
        Long timestamp = null;
        try {
            timestamp = Objects.requireNonNull(new SimpleDateFormat("yyyy-MM-dd HH:mm",
                    Locale.CHINA).parse(time)).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timestamp;
    }


    /*
     * @desc  时间戳转字符串
     * @param Long timestamp
     * @example timestamp=1558322327000
     **/
   /* @RequiresApi(api = Build.VERSION_CODES.N)
    public String getStringTime(Long timestamp) {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(timestamp.getTime()));  // 获取只有年月日的时间
        String datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(timestamp.getTime()));  // 获取年月日时分秒
        return datetime;
    }*/

    /**
     * 获取现在时间
     *
     * @return String返回字符串格式 yyyy-MM-dd
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return formatter.format(currentTime);
    }

    /**
     * 获取现在时间
     *
     * @return String返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateToSecond(LocalDate date) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return formatter.format(date);
    }

    /**
     * 获取现在时间
     *
     * @return String 返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateToSecond() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return formatter.format(currentTime);
    }


    /**
     * 获取现在时间
     *
     * @return String 返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateUnderLine() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.CHINA);
        return formatter.format(currentTime);
    }


    public static boolean getTwoNotSecondEquel(String startDate, String endDate,boolean allowEquality) {
        long startTime;
        long endTime;

        startTime = getTimestampNotSecond(startDate);
        endTime = getTimestampNotSecond(endDate);
        if (allowEquality){
            if (endTime == startTime)
                return true;
        }
        return endTime > startTime;

    }


    /**
     * 获取现在时间
     *
     * @return String 返回字符串格式 yyyy年MM月dd
     */
    public static String dataToYear() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        return sdf.format(date);
    }

    /**
     * 判断当前时间是星期几
     *
     * @return String
     */
    public static String toWeek() {

        Calendar c = Calendar.getInstance();
        int week = c.get(Calendar.DAY_OF_WEEK);
        String[] arr = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        return arr[week - 1];

    }

    /**
     * 6      * 时间戳转换成日期格式字符串
     * 7      * @param seconds 精确到秒的字符串
     * 8      * @param formatStr
     * 9      * @return
     * 10
     */
    public static String timeStamp2Date(long time2) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(time2);
    }

    /**
     * 6      * 时间戳转换成日期格式字符串
     * 7      * @param seconds 精确到秒的字符串
     * 8      * @param formatStr
     * 9      * @return
     * 10
     */
    public static String timeStamp3Date(long time2) {
        return new SimpleDateFormat("yyyy-MM-dd HH", Locale.CHINA).format(time2);
    }

    /**
     * 得到昨天的日期
     *
     * @return String
     */
    public static String getYestoryDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        return sdf.format(calendar.getTime());
    }

    /**
     * 得到今天的日期
     *
     * @return String
     */
    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(new Date());
    }

    /*
     * 将时间转换为时间戳
     */
    public static String dateToStamp(String s) throws ParseException {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        Date date = simpleDateFormat.parse(s);
        long ts = 0;
        if (date != null) {
            ts = date.getTime();
        }
        res = String.valueOf(ts);
        return res;
    }

    /**
     * 时间戳转化为时间格式
     *
     * @return String
     */
    public static String timeStampToStrMin(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        return sdf.format(timeStamp * 1000);
    }

    /**
     * 得到日期   yyyy-MM-dd
     *
     * @param timeStamp 时间戳
     * @return String
     */
    public static String formatDate(long timeStamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(timeStamp * 1000);
    }

    /**
     * 得到时间  HH:mm:ss
     *
     * @param timeStamp 时间戳
     * @return String
     */
    public static String getTime(long timeStamp) {
        String time = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        String date = sdf.format(timeStamp * 1000);
        String[] split = date.split("\\s");
        if (split.length > 1) {
            time = split[1];
        }
        return time;
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，如刚刚，1秒前
     *
     * @return String
     */
    public static String convertTimeToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;

        if (time < 60 && time >= 0) {
            return "刚刚";
        } else if (time >= 60 && time < 3600) {
            return time / 60 + "分钟前";
        } else if (time >= 3600 && time < 3600 * 24) {
            return time / 3600 + "小时前";
        } else if (time >= 3600 * 24 && time < 3600 * 24 * 30) {
            return time / 3600 / 24 + "天前";
        } else if (time >= 3600 * 24 * 30 && time < 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 + "个月前";
        } else if (time >= 3600 * 24 * 30 * 12) {
            return time / 3600 / 24 / 30 / 12 + "年前";
        } else {
            return "刚刚";
        }
    }

    /**
     * 将一个时间戳转换成提示性时间字符串，(多少分钟)
     *
     * @return String
     */
    public static String timeStampToFormat(long timeStamp) {
        long curTime = System.currentTimeMillis() / (long) 1000;
        long time = curTime - timeStamp;
        return time / 60 + "";
    }

    /**
     * SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
     * Date date =df.parse("2017-09-18 23:59:58");
     * date.setTime(date.getTime() + 1000);
     * System.out.println("当前时间      ："+df.format(date));
     * <p>
     * <p>
     * 注：如果是加N分钟则：
     * date.setTime(date.getTime() + N*60*1000);
     * <p>
     * 日期比较大小，获得相差天数
     */
    public static long twotimeToday(String startDateStr, String endDateStr) {
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        //开始时间
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //结束时间
        long betweenDate = 0;
        //得到相差的天数 betweenDate
        if (startDate != null && endDate != null) {
            betweenDate = (endDate.getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
        }
        //打印控制台相差的天数
        return betweenDate;

    }


    public static boolean equalsHMS(String startDateStr, String endDateStr) {
        boolean flag = false;
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);

        //开始时间
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf.parse(startDateStr);
            endDate = sdf.parse(endDateStr);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //得到
        if (startDate != null && endDate != null) {
            Log.e("TAG", "equalsHMS: "+startDate.getTime()+"   "+endDate.getTime());
            if (startDate.getTime() >= endDate.getTime()){
                flag = true;
            }else {
                flag = false;
            }
        }
        return flag;

    }

    public static long getTime(String startDateStr) {
        long millionSeconds = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        try {
            millionSeconds = sdf.parse(startDateStr).getTime();//毫秒
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return millionSeconds;
    }



    public static long getDateTime(String startDateStr) {
        startDateStr = startDateStr+" 00:00:00";
        Log.e("TAG", "getDateTime: "+startDateStr);
        long millionSeconds = 0;
     /* SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            millionSeconds = sdf.parse(startDateStr).getTime();//毫秒
        } catch (ParseException e) {
            e.printStackTrace();
        }
       */

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.CHINA);
            millionSeconds = Objects.requireNonNull(sdf.parse(startDateStr)).getTime()/1000;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("TAG", "getDateTime: "+millionSeconds);
        return millionSeconds;
    }


    public static String secToTime(int time) {
        String timeStr;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 时间戳转换成日期格式字符串
     * @param seconds 精确到秒的字符串
     * @return
     */
    public static String timeStamp2Date(long seconds,String format) {
        Log.e("TAG", "timeStamp2Date: "+seconds);
        if(format == null || format.isEmpty()){
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format,Locale.CHINA);
        Log.e("TAG", "timeStamp2Date: "+seconds+"  "+sdf.format(new Date(seconds*1000)));
        return sdf.format(new Date(seconds*1000));
    }
}

