package com.xyc.userc.service;

import com.xyc.userc.entity.Violation;

import java.util.List;

/**
 * Created by 1 on 2020/10/22.
 */
public interface ViolationService
{
    List<Violation> getAllViolation() throws Exception;
}