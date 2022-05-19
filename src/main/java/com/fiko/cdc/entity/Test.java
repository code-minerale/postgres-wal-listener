package com.fiko.cdc.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Test {
    private Integer id;

    private Date createTime;

    private Date updateTime;

    private String content;
}
