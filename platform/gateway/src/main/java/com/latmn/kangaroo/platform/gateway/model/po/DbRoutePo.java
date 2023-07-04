package com.latmn.kangaroo.platform.gateway.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(value = "route")
public class DbRoutePo {

    @Id
    @Column(value = "id")
    private String id;

    @Column(value = "uri")
    private String uri;

    @Column(value = "path")
    private String path;

    @Column(value = "del_flag")
    private Integer delFlag;

    @Column(value = "create_time")
    private LocalDateTime createTime;

    @Column(value = "update_time")
    private LocalDateTime updateTime;

    @Column(value = "create_by")
    private String createBy;

    @Column(value = "update_by")
    private String updateBy;
}
