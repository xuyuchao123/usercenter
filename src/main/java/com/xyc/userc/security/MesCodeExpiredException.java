package com.xyc.userc.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by 1 on 2020/7/8.
 */
public class MesCodeExpiredException extends AuthenticationException
{
    public MesCodeExpiredException(String msg) {
        super(msg);
    }

    public MesCodeExpiredException(String msg, Throwable t) {
        super(msg, t);
    }
}
