package com.fundy.FundyBE.domain.project.repository;

import com.fundy.FundyBE.global.exception.customexception.InvalidPeriodException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProjectPeriod {
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "START_DATETIME", nullable = false)
    private LocalDateTime startDate;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATETIME", nullable = false)
    private LocalDateTime endDate;

    public static ProjectPeriod of(LocalDateTime startDate, LocalDateTime endDate) {
        if (!isAvailablePeriod(startDate, endDate)) {
            throw InvalidPeriodException.createBasic();
        }

        return new ProjectPeriod(startDate, endDate);
    }

    private static boolean isAvailablePeriod(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isBefore(LocalDateTime.now())) {
            return false;
        }

        int MINIMUM_PERIOD = 14;
        if (ChronoUnit.DAYS.between(startDate, endDate) < MINIMUM_PERIOD) {
            return false;
        }

        return true;
    }

}
