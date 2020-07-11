package com.xyc;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by 1 on 2020/6/18.
 */
public class PasswordEncodeTest
{
    public static void main(String[] args)
    {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        System.out.println(bCryptPasswordEncoder.encode("1"));
    }
}
