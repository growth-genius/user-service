package com.sgyj.userservice.common;

public enum CommonMessage {

    SUCCESS_REGISTER("등록되었습니다."),
    SUCCESS_UPDATE("변경되었습니다."),

    ;

    private final String message;

    CommonMessage(String message) {
        this.message = message;
    }

    public String getMessage () {
        return message;
    }

}
