package com.xyc.userc.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by 1 on 2020/7/8.
 */
public class VerifyCodeErrorException extends AuthenticationException
{
    public VerifyCodeErrorException(String msg) {
        super(msg);
    }

    public VerifyCodeErrorException(String msg, Throwable t) {
        super(msg, t);
    }
}
