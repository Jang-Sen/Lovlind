package com.project.lovlind.domain.chat.exception;

import com.project.lovlind.conmon.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatExceptionCode implements ExceptionCode {
  NOT_FOUND_CHATROOM(HttpStatus.FOUND, "채팅방을 찾을 수 없습니다."),
  NOT_FOUND_PARTICIPANT(HttpStatus.NOT_FOUND, "참가자를 찾을 수 없습니다."),
  NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  FULL_PARTICIPANTS(HttpStatus.NOT_FOUND, "인원이 가득 차 있습니다."),
  IS_ALREADY_MEMBER(HttpStatus.NOT_FOUND, "이미 채팅방에 참가되어 있습니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
