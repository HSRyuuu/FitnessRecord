package com.example.fitnessrecord.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    //USER 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 이용자 입니다."),
    USER_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 이용자 입니다."),
    PASSWORD_CHECK_INCORRECT(HttpStatus.BAD_REQUEST.value(), "비밀번호 확인이 일치하지 않습니다."),
    EMAIL_AUTH_REQUIRED(HttpStatus.UNAUTHORIZED.value(), "이메일 인증이 완료되지 않았습니다."),
    EMAIL_AUTH_KEY_ERROR(HttpStatus.NOT_FOUND.value(), "이메일 인증 키에 문제가 있습니다."),

    //BodyInfo 관련
    BODY_INFO_DATA_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "바디 데이터가 존재하지 않습니다."),

    //Training 관련
    TRAINING_NAME_ALREADY_EXIST(HttpStatus.BAD_REQUEST.value(), "이미 존재 하는 Training Name 입니다."),
    TRAINING_NOT_FOUND_BY_NAME(HttpStatus.NOT_FOUND.value(), "해당 Training을 찾을 수 없습니다."),
    TRAINING_NOT_FOUND_BY_ID(HttpStatus.NOT_FOUND.value(), "해당 Training을 찾을 수 없습니다."),

    //Record 관련
    TRAINING_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "운동 기록을 찾을 수 없습니다."),
    SET_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "세트 기록을 찾을 수 없습니다."),
    VOLUME_RECORD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "볼륨 기록을 찾을 수 없습니다."),

    //Routine 관련
    ROUTINE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "루틴을 찾을 수 없습니다."),
    ROUTINE_ELEMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "루틴 요소를 찾을 수 없습니다."),
    ROUTINE_ELEMENT_NOT_MATCH_ROUTINE(HttpStatus.NOT_FOUND.value(), "루틴 요소가 루틴에 포함되지 않습니다."),


    //Redisson
    LOCK_NOT_AVAILABLE(HttpStatus.LOCKED.value(), "락을 획득할 수 없습니다."),
    LOCK_INTERRUPTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "스레드 인터럽트 에러 발생"),
    UNLOCKING_A_LOCK_WHICH_IS_NOT_LOCKED(HttpStatus.INTERNAL_SERVER_ERROR.value(), "이미 종료된 락을 unlocking 하려고 시도하였습니다."),


    //Security
    LOGIN_FAILED_USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "계정이 존재하지 않습니다."),
    LOGIN_FAILED_PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED.value(), "비밀번호가 틀립니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN.value(), "접근 권한이 없습니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED.value(), "로그인이 되지 않았습니다."),

    JWT_REFRESH_TOKEN_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 RefreshToken 입니다. 다시 로그인 해주세요."),
    JWT_TOKEN_ALREADY_LOGGED_OUT(HttpStatus.UNAUTHORIZED.value(), "로그아웃된 인증 정보입니다."),
    TOKEN_TIME_OUT(HttpStatus.CONFLICT.value(), "토큰이 만료되었습니다."),
    JWT_TOKEN_WRONG_TYPE(HttpStatus.UNAUTHORIZED.value(), "JWT 토큰 형식에 문제가 있습니다."),
    JWT_TOKEN_MALFORMED(HttpStatus.UNAUTHORIZED.value(), "JWT 토큰 형식에 문제가 있습니다."),

    //BASIC
    NO_AUTHORITY_ERROR(HttpStatus.FORBIDDEN.value(), "권한이 없습니다."),
    NOT_FOUND_ERROR(HttpStatus.NOT_FOUND.value(), "404 NOT FOUND"),
    BAD_REQUEST_ERROR(HttpStatus.BAD_REQUEST.value(), "BAD_REQUEST"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 서버 오류가 발생 했습니다.");

    private final int statusCode;
    private final String description;

}
