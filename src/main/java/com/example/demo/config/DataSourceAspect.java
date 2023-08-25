package com.example.demo.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Aspect
@Component
public class DataSourceAspect {

    @Around("@annotation(transactional)")
    public Object routeDataSource(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        if (transactional.readOnly()) {
            DataSourceContextHolder.setDataSourceKey("readDataSource");
        } else {
            DataSourceContextHolder.setDataSourceKey("writeDataSource");
        }

        try {
            return joinPoint.proceed();
        } finally {
            DataSourceContextHolder.clearDataSourceKey();
        }
    }
}
