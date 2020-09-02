package com.maintenance.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtil {
    private static Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /**
     * 精确到秒数的时间格式
     */
    public static final String TIME_FORMAT_DEFAULT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒数的时间格式
     */
    public static final String TIME_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss:SSS";

    public static final String TIME_FORMAT_DAY_ONLY = "yyyy-MM-dd";

    private static final DateFormat DATEFORMAT = new SimpleDateFormat(TIME_FORMAT_DAY_ONLY);

    /**
     * 根据formatStr(例：yyyy-MM-dd HH:mm:ss:SSS)格式化时间
     * @param date
     * @param formatStr
     * @return
     */
    public static String formatDate(Date date, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    /**
     *
     * @param GMTTime "Thu Feb 07 2019 16:51:05 GMT 0800"
     * @param formatStr "yyyy-MM-dd"
     * @return
     */
    public static String formatGMTTime(String GMTTime, String formatStr) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.US);
        GMTTime = GMTTime.replace("GMT 0800", "+0800");
        try {
            Date parse = sdf.parse(GMTTime);
            sdf = new SimpleDateFormat(formatStr);
            GMTTime = sdf.format(parse);
        } catch (Exception e) {
            logger.error("格林威治时间转换错误，请求参数[ GMTTime:" + GMTTime + ", formatStr:" + formatStr + "]");
        }
        return GMTTime;
    }

    /**
     * 返回格式：yyyy-MM-dd
     * @param date
     * @return
     */
    public static synchronized String formatDateToIso8601(Date date) {
        return DATEFORMAT.format(date);
    }

    public static synchronized String getCurDateWithIso8601Format() {
        return formatDateToIso8601(new Date());
    }

    // 日期运算;按天向前计算多少天;
    public static Date forwardByDay(Date origin, int cnt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(origin);
        calendar.add(Calendar.DATE, cnt);
        return calendar.getTime();
    }

    public static Date forwardByMonth(Date origin, int cnt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(origin);
        calendar.add(Calendar.MONTH, cnt);
        return calendar.getTime();
    }

    public static Date forwardByYear(Date origin, int cnt) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(origin);
        calendar.add(Calendar.YEAR, cnt);
        return calendar.getTime();
    }

    /**
     * 将yyyy-MM-dd格式的时间，对齐到 当天开始的时间。<br>
     * 例如 2016-01-01 补全为 2016-01-01 00:00:00
     */
    public static String alignToDayStart(String shortDateFormat) {
        Date date = stringToDate(shortDateFormat, "yyyy-MM-dd");
        Assert.notNull(date, shortDateFormat + " should be in the format of yyyy-MM-dd");
        return timeMillisToString(date.getTime(), TIME_FORMAT_DEFAULT);
    }

    /**
     * 将Date格式的时间，对齐到 当天开始的时间。<br>
     * 例如 2016-01-01 08:09:00 对齐到 2016-01-01 00:00:00
     */
    public static String alignToDayStart(Date date) {
        Assert.notNull(date, "date should not be null");
        String day = timeMillisToString(date.getTime(), TIME_FORMAT_DAY_ONLY);
        //return timeMillisToString(date.getTime(), TIME_FORMAT_DEFAULT);
        return timeMillisToString(stringToTimeMillis(day, TIME_FORMAT_DAY_ONLY), TIME_FORMAT_DEFAULT);
    }

    /**
     * 将yyyy-MM-dd格式的时间，对齐到 当天结束的时间。<br>
     * 例如 2016-01-01 10:01:00 对齐到 2016-01-01 23:59:59
     */
    public static String alignToDayEnd(Date date) {
        Assert.notNull(date, "date should not be null");
        String day = timeMillisToString(date.getTime(), TIME_FORMAT_DAY_ONLY);
        return timeMillisToString(date.getTime() + (24 * 60 * 60 * 1000 - 1), TIME_FORMAT_DEFAULT);
    }

    /**
     * 将yyyy-MM-dd格式的时间，对齐到 当天结束的时间。<br>
     * 例如 2016-01-01 补全为 2016-01-01 23:59:59
     *
     * @param shortDateFormat
     * @return
     */
    public static String alignToDayEnd(String shortDateFormat) {
        Date date = stringToDate(shortDateFormat, "yyyy-MM-dd");
        Assert.notNull(date, shortDateFormat + " should be in the format of yyyy-MM-dd");
        return timeMillisToString(date.getTime() + (24 * 60 * 60 * 1000 - 1), TIME_FORMAT_DEFAULT);
    }


    /**
     * 获得 TIME_FORMAT_DEFAULT 定义的时间格式对应的Date对象
     *
     * @param dateString
     * @return
     */
    public static Date stringToDate(String dateString) {
        return stringToDate(dateString, TIME_FORMAT_DEFAULT);
    }

    public static Date stringToDate(String dateString, String dateFormat) {
        if (StringUtils.isEmpty(dateString)) {
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            simpleDateFormat.setLenient(false);
            return simpleDateFormat.parse(dateString);

        } catch (Exception e) {
            for (Iterator iterator = getSupportedTimeFormats().iterator(); iterator.hasNext(); ) {
                String pattern = (String) iterator.next();
                try {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                    simpleDateFormat.setLenient(false);
                    return simpleDateFormat.parse(dateString);
                } catch (Exception e1) {
                }
            }
            return null;
        }
    }

    /**
     * 字符串转换成日期
     * @param str
     * @return date
     */
    public static Date StrToDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            return null;
        }
        return date;
    }

    private static List<String> getSupportedTimeFormats() {
        List patterns = new ArrayList();
        patterns.add("yyyy-MM-dd HH:mm:ss:SSS");
        patterns.add("yyyy-MM-dd HH:mm:ss");
        patterns.add("yyyy-MM-dd HH:mm");
        patterns.add("yyyy-MM-dd HH");
        patterns.add("yyyy-MM-dd");
        patterns.add("yyyyMMdd");
        return patterns;
    }

    /**
     * 将 timeMillis 转成系统默认的时间格式（ yyyy-MM-dd HH:mm:ss）的方法
     *
     * @param timeMillis
     * @return
     */
    public static String timeMillisToString(long timeMillis) {
        return timeMillisToString(timeMillis, TIME_FORMAT_DEFAULT);
    }


    /**
     * 完成timeMillis到日期类型的字符串的转换
     *
     * @param timeMillis 以毫秒数记的long型的时间
     * @param dateFormat 日期格式
     * @return
     */
    public static String timeMillisToString(long timeMillis, String dateFormat) {
        if (timeMillis == 0)
            return "";

        if ("".equals(dateFormat) || dateFormat == null) {
            return "";
        }
        try {
            return new SimpleDateFormat(dateFormat).format(new Date(timeMillis));
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 完成将指定时间格式的时间串转化成毫秒数.<br>
     * <br>
     * 建议输入的时间串格式与输入的时间格式保持一致,如果不一致,会尝试使用预定义的时间格式进行转化)}
     * ,如果还是没有找到匹配的时间格式,那么将返回-1. <br>
     * <br>
     * <b>预定义的时间格式：</b> <br>
     * <br>
     * 1） yyyy-MM-dd HH:mm:ss:SSS <br>
     * 2） yyyy-MM-dd HH:mm:ss<br>
     * 3） yyyy-MM-dd HH:mm <br>
     * 4） yyyy-MM-dd HH <br>
     * 5） yyyy-MM-dd <br>
     * 6） yyyyMMdd
     * <p>
     * <br>
     * <br>
     * <b>Eg:</b> <br>
     * <br>
     * 将时间串"2011-03-08 10:15:20:621" 按照时间格式"yyyy-MM-dd HH:mm:ss:SSS"转成毫秒数(long型):1299549600000.<br>
     * 1299549600000 = stringToTimeMillis("2011-03-08 10:15:20:621","yyyy-MM-dd HH:mm:ss:SSS");
     *
     * @param dateStr    指定时间格式的时间串.
     * @param dateFormat 时间格式;如果没有输入该参数,那么将使用默认的时间格式：{@link #TIME_FORMAT_DEFAULT}
     * @return 1) 如果没有输入参数dateStr,那么返回-1; <br>
     * 2) 如果输入的时间串与输入的时间格式不匹配,并且在预定义的时间格式中也找不到匹配项,那么返回-1;
     */
    public static long stringToTimeMillis(String dateStr, String dateFormat) {
        if (dateStr == null || "".equals(dateStr)) {
            return -1;
        }

        if (StringUtils.isEmpty(dateFormat)) {
            dateFormat = TIME_FORMAT_DEFAULT;
        }

        Date date = DateUtil.stringToDate(dateStr, dateFormat);
        return date == null ? -1 : date.getTime();
    }

    public static long stringToTimeMillis(String dateStr) {
        return stringToTimeMillis(dateStr, false);
    }

    /**
     * 将字符串格式转换成Unix时间戳
     *
     * @param dateStr
     * @param smart   根据系统内支持的时间格式，逐个进行尝试是否可以转换
     * @return
     */
    public static long stringToTimeMillis(String dateStr, boolean smart) {
        if (smart) {
            List<String> allFormats = getSupportedTimeFormats();
            for (String format : allFormats) {
                long result = stringToTimeMillis(dateStr, format);
                if (result > 0) {
                    return result;
                }
            }
        }
        return stringToTimeMillis(dateStr, TIME_FORMAT_DEFAULT);
    }


    /**
     * 获取以系统默认的时间格式（ yyyy-MM-dd HH:mm:ss）展现的当前系统时间
     *
     * @return
     */
    public static String getCurrentDateTime() {
        return getCurrentDateTime(TIME_FORMAT_DEFAULT);
    }

    /**
     * 获得以String表示的当前系统时间
     *
     * @param dateFormat
     * @return
     */
    public static String getCurrentDateTime(String dateFormat) {
        return timeMillisToString(System.currentTimeMillis(), dateFormat);
    }
    // 将传入时间与当前时间进行对比，是否今天昨天
    public static String getTime(Date date) {
        String todySDF = "今天 HH:mm";
        String yesterDaySDF = "昨天 HH:mm";
        String otherSDF = "yy/M/d HH:mm";
        SimpleDateFormat sfd = null;
        String time = "";
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        Date now = new Date();
        Calendar targetCalendar = Calendar.getInstance();
        targetCalendar.setTime(now);
        targetCalendar.set(Calendar.HOUR_OF_DAY, 0);
        targetCalendar.set(Calendar.MINUTE, 0);
        if (dateCalendar.after(targetCalendar)) {
            sfd = new SimpleDateFormat(todySDF);
            time = sfd.format(date);
            return time;
        } else {
            targetCalendar.add(Calendar.DATE, -1);
            if (dateCalendar.after(targetCalendar)) {
                sfd = new SimpleDateFormat(yesterDaySDF);
                time = sfd.format(date);
                return time;
            }
        }
        sfd = new SimpleDateFormat(otherSDF);
        time = sfd.format(date);
        return time;
    }

    /**
     * 获取时间间隔，单位秒
     * @param beginTime     yyyy-MM-dd HH:mm:ss
     * @param endTime   yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static int getBetween(String beginTime, String endTime){
        logger.info("------getBetween------"+ beginTime + "-"+endTime);
        Date beginDate = stringToDate(beginTime, TIME_FORMAT_DEFAULT);
        Date endDate = stringToDate(endTime, TIME_FORMAT_DEFAULT);
        long begin = beginDate.getTime();
        long end = endDate.getTime();
        return (int)Math.ceil(((end-begin)/1000)/(double)60);
    }


    public static void main(String[] args) {
//        String date = "2016-01-01";
//        System.out.println(timeMillisToString(stringToTimeMillis(date, "yyyy-MM-dd")));
//        System.out.println(alignToDayStart(date));
//        System.out.println(alignToDayEnd(date));

        //System.out.println("stringToTimeMillis(\"2016-07-07 09:00:00\",\"yyyy-MM-dd HH:mm:ss\") = " + stringToTimeMillis("2016-07-07 09:00:00","yyyy-MM-dd HH:mm:ss"));
//        System.out.println(timeMillisToString(2459134*600000L));


        String recordTime1 = "2018-12-19 19:57:07";

        String recordTime2 = "2018-12-19 19:58:17";

//        String recordTime3 = "2018-12-25 13:57:07";
//        Date createDate1 = DateUtil.StrToDate(recordTime1);
//        String createTime1 = DateUtil.getTime(createDate1);
//        System.out.println("createTime1---"+ createTime1);
//
//        Date createDate2 = DateUtil.StrToDate(recordTime2);
//        String createTime2 = DateUtil.getTime(createDate2);
//        System.out.println("createTime2---"+ createTime2);
//
//        Date createDate3 = DateUtil.StrToDate(recordTime3);
//        String createTime3 = DateUtil.getTime(createDate3);
        System.out.println("createTime3---"+ DateUtil.getBetween(recordTime1, recordTime2));
    }


    public static String toDateString(Date date,String format){
        if(format == null || "".equals(format))format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String toDateString(Date date){
        return toDateString(date,null);
    }
}
