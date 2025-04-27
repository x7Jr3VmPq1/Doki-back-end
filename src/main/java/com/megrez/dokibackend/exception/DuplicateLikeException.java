package com.megrez.dokibackend.exception;

/**
 * DuplicateLikeException 是一个自定义的运行时异常类，用于表示重复点赞操作的异常情况。
 * 该类继承自 RuntimeException，提供了两种构造方法来初始化异常信息。
 */
public class DuplicateLikeException extends RuntimeException {
    /**
     * 构造方法，用于创建一个带有指定错误信息的 DuplicateLikeException 实例。
     *
     * @param message 异常的描述信息，用于说明异常的具体原因。
     */
    public DuplicateLikeException(String message) {
        super(message);
    }

    /**
     * 构造方法，用于创建一个带有指定错误信息和原因的 DuplicateLikeException 实例。
     *
     * @param message 异常的描述信息，用于说明异常的具体原因。
     * @param cause   导致该异常的原始异常，通常用于异常链的传递。
     */
    public DuplicateLikeException(String message, Throwable cause) {
        super(message, cause);
    }
}
