package org.duckdb.call;

import org.duckdb.DuckDBTimestamp;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * jni call java 的辅助方法
 *
 * @author Harrison
 * @since 11.0
 * Created on 2024/7/29
 */
public class DuckDBJavaCall {

    public static Date toDate(long daysSinceEpoch) {
        LocalDate localDate = LocalDate.ofEpochDay(daysSinceEpoch);
        return Date.valueOf(localDate);
    }

    public static Time toTime(long microseconds) {
        long nanoseconds = TimeUnit.MICROSECONDS.toNanos(microseconds);
        return Time.valueOf(LocalTime.ofNanoOfDay(nanoseconds));
    }

    public static Timestamp toTimestamp(long microseconds) {
        return DuckDBTimestamp.toSqlTimestamp(microseconds);
    }
}
