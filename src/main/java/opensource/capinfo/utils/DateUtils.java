package opensource.capinfo.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd）
     */
    public static String getDate() {
        return getDate("yyyy-MM-dd");
    }

    /**
     * 得到当前日期字符串 格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String getDate(String pattern) {
        return DateFormatUtils.format(new Date(), pattern);
    }

    /**
     * 得到日期字符串 默认格式（yyyy-MM-dd） pattern可以为："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public static String formatDate(Date date, Object... pattern) {
        String formatDate = null;
        if (pattern != null && pattern.length > 0) {
            formatDate = DateFormatUtils.format(date, pattern[0].toString());
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    public static void main(String[] args) {
        System.out.println(formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"));
    }
    /**
     * 得到日期时间字符串，转换格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String formatDateTime(Date date) {
        return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前时间字符串 格式（HH:mm:ss）
     */
    public static String getTime() {
        return formatDate(new Date(), "HH:mm:ss");
    }

    /**
     * 得到当前日期和时间字符串 格式（yyyy-MM-dd HH:mm:ss）
     */
    public static String getDateTime() {
        return formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        return formatDate(new Date(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        return formatDate(new Date(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        return formatDate(new Date(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        return formatDate(new Date(), "E");
    }

    /**
     * 日期型字符串转化为日期 格式
     * { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
     *   "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm",
     *   "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm" }
     */
    public static Date parseDate(Object str) {
        if (str == null){
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 获取过去的天数
     * @param date
     * @return
     */
    public static long pastDays(Date date) {
        long t = new Date().getTime()-date.getTime();
        return t/(24*60*60*1000);
    }

    /**
     * 获取过去的小时
     * @param date
     * @return
     */
    public static long pastHour(Date date) {
        long t = new Date().getTime()-date.getTime();
        return t/(60*60*1000);
    }

    /**
     * 获取过去的分钟
     * @param date
     * @return
     */
    public static long pastMinutes(Date date) {
        long t = new Date().getTime()-date.getTime();
        return t/(60*1000);
    }

    /**
     * 转换为时间（天,时:分:秒.毫秒）
     * @param timeMillis
     * @return
     */
    public static String formatDateTime(long timeMillis){
        long day = timeMillis/(24*60*60*1000);
        long hour = (timeMillis/(60*60*1000)-day*24);
        long min = ((timeMillis/(60*1000))-day*24*60-hour*60);
        long s = (timeMillis/1000-day*24*60*60-hour*60*60-min*60);
        long sss = (timeMillis-day*24*60*60*1000-hour*60*60*1000-min*60*1000-s*1000);
        return (day>0?day+",":"")+hour+":"+min+":"+s+"."+sss;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    /**
     * 获取两个日期之间的小时数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getHoursOfTwoDate(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60);
    }

    /**
     * 获取某个日期加特定天数后与另一个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static double getDistanceOfTwoChangedDate(Date before, Date after,int day) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24) + day;
    }

    /**
     * 一个指定日期加上指定天数得到新日期
     *
     * @param date
     * @param day
     * @return
     * @throws ParseException
     * @author hanwangdong
     * @date 2018-5-3
     */
    public static Date addDate(Date date, long day) throws ParseException {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time += day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    /**
     * 一个指定日期减去指定天数得到新日期
     *
     * @param date
     * @param day
     * @return
     * @throws ParseException
     * @author hanwangdong
     * @date 2018-5-3
     */
    public static Date reduceDate(Date date, long day) throws ParseException {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day * 24 * 60 * 60 * 1000; // 要加上的天数转换成毫秒数
        time -= day; // 相减得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    /**
     * 一个指定日期过去特定月后的日期
     *
     * @param date
     * @param month
     * @return
     * @throws ParseException
     * @author hanwangdong
     * @date 2018-5-3
     */
    public static Date deforeMonDate(Date date, int month) throws Exception {
        Calendar c = Calendar.getInstance();// 获得一个日历的实例
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        /*
         * Date date = null; try{ date = sdf.parse("2006-06-07");//初始日期 }catch(Exception
         * e){
         *
         * }
         */
        c.setTime(date);// 设置日历时间
        c.add(Calendar.MONTH, -month);// 在日历的月份上6个月
        // System.out.println(sdf.format(c.getTime()));//的到你想要得6个月后的日期
        return c.getTime();
    }

    /**
     * 由出生日期获得年龄
     *
     * @param birthDay
     * @return
     */
    public static int getAgeByBirthDay(Date birthDay) {
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH);
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(birthDay);

        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth)
                    age--;
            } else {
                age--;
            }
        }
        return age;
    }
    /**
     * 判断时间是否在时间段内
     *
     * @param date
     *            当前时间 yyyy-MM-dd HH:mm:ss
     * @param startDate
     *            开始时间 00:00:00
     * @param endDate
     *            结束时间 00:05:00
     * @return
     */
    public static boolean isInDate(Date date,Date startDate,Date endDate) {
        long dateLong = date.getTime();
        long startLong = startDate.getTime();
        long endLong = endDate.getTime();
        if(dateLong <=endLong && dateLong>=startLong) {
            return true;
        }
        return false;
    }

    /**
     * 判断 startDate 是否在endDate后面
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean isBigDate(Date startDate,Date endDate){
        long startLong = startDate.getTime();
        long endLong = endDate.getTime();
        if(startLong>endLong){
            return true;
        }
        return false;
    }
    /**
     * 当前时间加上过期的分钟数得到过期的时间
     * @param expiredMinute
     * @return
     */
    public static Date getExpireDate(int expiredMinute) {
        Long time = System.currentTimeMillis();
        time+=expiredMinute*60*1000;
        Date date = new Date(time);
//		long time2 = date.getTime();
//		System.out.println(DateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ss"));

        return date;


    }

    /**
     * 得到当前季度
     * @param now
     * @return
     */
    public static String getNowSeason(Date now){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = sdf.format(now);
        String nowMonth = nowDate.substring(5, 7);
        String season = "";
        if (nowMonth.equals("01") || nowMonth.equals("02") || nowMonth.equals("03")) {
            season = "1";
        } else if (nowMonth.equals("04") || nowMonth.equals("05") || nowMonth.equals("06")) {
            season = "2";
        } else if (nowMonth.equals("07") || nowMonth.equals("08") || nowMonth.equals("09")) {
            season = "3";
        } else if (nowMonth.equals("10") || nowMonth.equals("11") || nowMonth.equals("12")) {
            season = "4";
        }
        return season;
    }

    /**
     * 当前季度的开始时间
     */
    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 6);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = parseDate(formatDate(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

    /**
     * 当前季度的结束时间
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = parseDate(formatDate(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return now;
    }

}
