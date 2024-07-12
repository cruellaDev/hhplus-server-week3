package com.io.hhplus.concert.common.utils;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {

    // 공통 부분 함수로 나눔
    public static Date convertLocaleDatetimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * 현재 시간 구하기
     * @return 현재시간
     */
    public static Date getSysDate() {
        LocalDateTime now = LocalDateTime.now();
        return convertLocaleDatetimeToDate(now);
    }

    /**
     * 숫자(int)로 임의 일시 만들기
     * @param year 연도
     * @param month 월
     * @param day 일
     * @param hour 시
     * @param minute 분
     * @param second 초
     * @return 임의 일시
     */
    public static Date createTemporalDateByIntParameters(int year, int month, int day, int hour, int minute, int second) {
        try {
            LocalDateTime localDateTime = LocalDateTime.of(
                    Math.max(year, 1),
                    Month.of(Math.max(Math.min(month, 12), 1)),
                    Math.max(day, 1),
                    Math.max(Math.min(hour, 23), 0),
                    Math.max(Math.min(minute, 59), 0),
                    Math.max(Math.min(second, 59), 0));
            return convertLocaleDatetimeToDate(localDateTime);
        } catch (DateTimeException e) {
            return null;
        }
    }

    public static int compareTo(Date source, Date target) {
        try {
            return source.compareTo(target);
        } catch (Exception e) {
            return -9999;
        }
    }

    public static int betweenFromTo(Date source, Date targetFrom, Date targetTo) {
        try {
            return source.compareTo(targetFrom) >= 0 && source.compareTo(targetTo) <= 0 ? 1 : 0;
        } catch (Exception e) {
            return -9999;
        }
    }
}
