package com.fundy.FundyBE.domain.project;

import com.fundy.FundyBE.domain.project.repository.ProjectPeriod;
import com.fundy.FundyBE.global.exception.customexception.InvalidPeriodException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@DisplayName("Period 객체 테스트")
public class ProjectPeriodTest {
    @DisplayName("[성공] Period 객체 생성")
    @Test
    public void createPeriodSuccess() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.plusDays(1);
        LocalDateTime endDateTime = startDate.plusDays(15);

        ProjectPeriod projectPeriod = ProjectPeriod.of(startDate, endDateTime);
        Assertions.assertThat(projectPeriod.getStartDate()).isEqualTo(startDate);
        Assertions.assertThat(projectPeriod.getEndDate()).isEqualTo(endDateTime);
    }

    @DisplayName("[실패] Period 객체 생성: 2주 미만의 기간")
    @Test
    public void createPeriodFailCase1() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.plusDays(1);
        LocalDateTime endDate = startDate.plusDays(3);

        Assertions.assertThatThrownBy(()-> ProjectPeriod.of(startDate,endDate)).isInstanceOf(InvalidPeriodException.class);
    }

    @DisplayName("[실패] Period 객체 생성: 역순")
    @Test
    public void createPeriodFailCase2() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.plusDays(1);
        LocalDateTime endDate = startDate.plusDays(14);

        Assertions.assertThatThrownBy(()-> ProjectPeriod.of(endDate,startDate)).isInstanceOf(InvalidPeriodException.class);
    }

    @DisplayName("[실패] Period 객체 생성: 현재보다 이전 상태로 설정")
    @Test
    public void createPeriodFailCase3() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(3);
        LocalDateTime endDate = startDate.plusDays(15);

        Assertions.assertThatThrownBy(()-> ProjectPeriod.of(startDate,endDate)).isInstanceOf(InvalidPeriodException.class);
    }
}
