package com.xyc.userc.security;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by 1 on 2020/7/8.
 */
public class MesCodeErrorException extends AuthenticationException
{
    public MesCodeErrorException(String msg) {
        super(msg);
    }

    public MesCodeErrorException(String msg, Throwable t) {
        super(msg, t);
    }
}
