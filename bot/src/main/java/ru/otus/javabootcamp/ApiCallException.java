package ru.otus.javabootcamp;

public class ApiCallException extends Exception {

  public ApiCallException(String message) {
    super(message);
  }

  public ApiCallException(String message, Throwable reason) {
    super(message, reason);
  }

  public ApiCallException(Throwable reason) {
    super(reason);
  }

}
