package com.smartspending.common.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class CustomException extends RuntimeException {
  private final CommonResponseCode code;
  private List<String> messages;
  private String responseBody;

    public CustomException(CommonResponseCode code) {
        this.code = code;
    }

    public CustomException(CommonResponseCode code, List<String> messages) {
      this.code = code;
      this.messages = messages;
    }

    public CustomException(CommonResponseCode code, List<String> messages, String responseBody) {
      this.code = code;
      this.messages = messages;
      this.responseBody = responseBody;
    }
}
