package com.io.hhplus.concert.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
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
            log.debug("[utils-DateUtils-createTemporalDateByIntParameters]: year: {}, month: {}, day: {}, hour: {}, minute: {}, second: {}", year, month, day, hour, minute, second);
            return getSysDate();
        }
    }

    /**
     *
     * @param source 일시
     * @param target 비교일시
     * @return 클 시 1 작을 시 -1 같을 시 0 에러시 -9999
     */
    public static int compareTo(Date source, Date target) {
        try {
            return source.compareTo(target);
        } catch (Exception e) {
            log.debug("[utils-DateUtils-compareTo]: source: {}, target: {}", source, target);
            return -9999;
        }
    }

    /**
     * 기간 내 일시 포함여부
     * @param source 일시
     * @param targetFrom 시작일시
     * @param targetTo 종료일시
     * @return 기간 내 일시 포함 시 1 아닐 시 0 에러 시 -9999
     */
    public static int betweenFromTo(Date source, Date targetFrom, Date targetTo) {
        try {
            return source.compareTo(targetFrom) >= 0 && source.compareTo(targetTo) <= 0 ? 1 : 0;
        } catch (Exception e) {
            log.debug("[utils-DateUtils-betweenFromTo]: source: {}, targetFrom: {}, targetTo: {}", source, targetFrom, targetTo);
            return -9999;
        }
    }

    /**
     * 기간 계산
     * @param fromDate 시작일시
     * @param toDate 종료일시
     * @return 기간 (에러일 시 -9999)
     */
    public static long calculateDuration(Date fromDate, Date toDate) {
        try {
            return TimeUnit.MILLISECONDS.toSeconds(fromDate.getTime() - toDate.getTime());
        } catch (Exception e) {
            log.debug("[utils-DateUtils-calculateDuration]: fromDate: {}, toDate: {}", fromDate, toDate);
            return 0;
        }
    }

    /**
     * 날짜에 초를 더한다.
     * @param source 더할 날짜
     * @param target 초 (s)
     * @return 날짜
     */
    public static Date addSeconds(Date source, long target) {
        source.setTime(source.getTime() + (target * 1000));
        return source;
    }

    public static Date subtractSeconds(Date source, long target) {
        return addSeconds(source, target * -1);
    }
}
