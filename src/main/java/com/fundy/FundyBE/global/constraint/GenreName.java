package com.fundy.FundyBE.global.constraint;

public enum GenreName {
    RACING("레이싱"),
    PUZZLE("퍼즐"),
    ROLE_PLAYING("롤플레잉"),
    BOARD("보드"),
    SPORT("스포츠"),
    SHOOTING("슈팅"),
    SIMULATION("시뮬레이션"),
    ACTION("액션");

    String value;
    GenreName(String value) {
        this.value = value;
    }

    public static GenreName ofKorean(String value) {
        for(GenreName enumValue : GenreName.values()) {
            if(enumValue.getValue().equals(value)) {
                return enumValue;
            }
        }

        throw new IllegalArgumentException("No enum constant with value " + value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
