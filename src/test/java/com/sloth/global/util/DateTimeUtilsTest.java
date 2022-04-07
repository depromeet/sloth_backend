package com.sloth.global.util;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilsTest {

    @Test
    @DisplayName("Date -> LocalDateTime 변환 테스트")
    void convertToLocalDateTimeTest() {

        // given
        Calendar calendar = Calendar.getInstance();
        calendar.set(2022,0,5);
        Date date = calendar.getTime();

        // when
        LocalDateTime localDateTime = DateTimeUtils.convertToLocalDateTime(date);

        // then
        Assertions.assertThat(localDateTime.getYear()).isEqualTo(2022);
        Assertions.assertThat(localDateTime.getMonthValue()).isEqualTo(1);
        Assertions.assertThat(localDateTime.getDayOfMonth()).isEqualTo(5);
    }

    @Test
    @DisplayName("LocalDate -> Date 변환 테스트")
    void convertToDateTest() {

        // given
        LocalDate localDate = LocalDate.of(2022,1,5);

        // when
        Date date = DateTimeUtils.convertToDate(localDate);

        // then
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        Assertions.assertThat(calendar.get(Calendar.YEAR)).isEqualTo(2022);
        Assertions.assertThat(calendar.get(Calendar.MONTH)).isEqualTo(0);
        Assertions.assertThat(calendar.get(Calendar.DATE)).isEqualTo(5);
    }

    @Test
    @DisplayName("LocalDate -> String 변환 테스트")
    void convertToStringTest() {

        // given
        LocalDate localDate = LocalDate.of(2022,1,5);

        // when
        String date = DateTimeUtils.convertToString(localDate);

        // then
        Assertions.assertThat(date).isEqualTo("2022-01-05");
    }

    @Test
    @DisplayName("date String -> LocalDate 변환 테스트")
    void convertToDateTest2() {

        // given
        String date = "2022-01-05";

        // when
        LocalDate localDate = DateTimeUtils.convertToDate(date);

        // then
        Assertions.assertThat(localDate.getYear()).isEqualTo(2022);
        Assertions.assertThat(localDate.getMonthValue()).isEqualTo(1);
        Assertions.assertThat(localDate.getDayOfMonth()).isEqualTo(5);
    }


}