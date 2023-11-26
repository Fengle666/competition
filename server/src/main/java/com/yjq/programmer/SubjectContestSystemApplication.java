package com.yjq.programmer;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 项目启动类
 */
@SpringBootApplication
@MapperScan("com.yjq.programmer.dao")
public class SubjectContestSystemApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(SubjectContestSystemApplication.class, args);
    }
}
