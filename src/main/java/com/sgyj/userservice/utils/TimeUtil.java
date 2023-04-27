package com.sgyj.userservice.utils;

import java.time.LocalTime;
import lombok.Getter;

@Getter
public final class TimeUtil {

    private final LocalTime basicStartTime;

    private final LocalTime basicEndTime;

    public static TimeUtil of(LocalTime basicStartTime, LocalTime basicEndTime) {
        return new TimeUtil(basicStartTime, basicEndTime);
    }

    /**
     * 설정 된 시간을 포함하는지 확인
     * @param startTime : 시작 시간
     * @param endTime : 종료 시간
     */
    public boolean isContains(LocalTime startTime, LocalTime endTime) {
        return (startTime.isAfter(basicStartTime) && startTime.isBefore(basicEndTime)) ||
                (endTime.isAfter(basicStartTime) && endTime.isBefore(basicEndTime));
    }

    private TimeUtil() { throw new AssertionError();}

    private TimeUtil(LocalTime basicStartTime, LocalTime basicEndTime) {
        this.basicStartTime = basicStartTime;
        this.basicEndTime = basicEndTime;
    }
}
