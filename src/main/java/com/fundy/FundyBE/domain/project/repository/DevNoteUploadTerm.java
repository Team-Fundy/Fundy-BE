package com.fundy.FundyBE.domain.project.repository;

import com.fundy.FundyBE.domain.project.repository.converter.DayAttributeConverter;
import com.fundy.FundyBE.global.constraint.Day;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class DevNoteUploadTerm {
    // 개발 업로드 주기: 1주에 한 번씩 ~ 5주에 한 번씩
    @Column(name = "DEVNOTE_UPLOAD_CYCLE", nullable = false)
    @Min(1)
    @Max(5)
    private int weekCycle;

    // 업로드 요일: 월~일
    @Convert(converter = DayAttributeConverter.class)
    @Column(name = "DEVNOTE_UPLOAD_DAY", nullable = false)
    private Day day;

    public Day getDay() {
        return day;
    }

    public int getWeekCycle() {
        return weekCycle;
    }
}
