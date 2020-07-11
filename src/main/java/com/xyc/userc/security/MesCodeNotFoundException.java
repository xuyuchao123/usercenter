package com.xyc.userc.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by 1 on 2020/7/8.
 */
public class MesCodeNotFoundException extends AuthenticationException
{
    public MesCodeNotFoundException(String msg) {
        super(msg);
    }

    public MesCodeNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
