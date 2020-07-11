package com.xyc.userc.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by 1 on 2020/7/8.
 */
public class MobileNotFoundException extends AuthenticationException
{
    public MobileNotFoundException(String msg) {
        super(msg);
    }

    public MobileNotFoundException(String msg, Throwable t) {
        super(msg, t);
    }
}
