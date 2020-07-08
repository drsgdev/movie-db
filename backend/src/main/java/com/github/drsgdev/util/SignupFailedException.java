package com.github.drsgdev.util;

public class SignupFailedException extends RuntimeException {

  private static final long serialVersionUID = 1619395151932840205L;

  public SignupFailedException(String msg) {
    super(msg);
  }
}
