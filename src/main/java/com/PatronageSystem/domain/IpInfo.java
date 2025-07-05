package com.PatronageSystem.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "ip_info")
public class IpInfo implements Serializable {

    /**
     * 序列化id
     */
    private static final long serialVersionUID = 4125096758372084309L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ip")
    private String ip;

    @Column(name = "address")
    private String address;

    @Column(name = "ua")
    private String ua;

    @Column(name = "time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date time;

    @Column(name = "axis")
    private String axis;

    // 页面点击数
    @Column(name = "count")
    private Long count;

    // 在线状态
    @Column(name = "status")
    private Integer status;


    public IpInfo() {
    }

    public IpInfo(String ip, String address, String ua, String axis, Long count, int status) {
        this.ip = ip;
        this.address = address;
        this.ua = ua;
        this.axis = axis;
        this.count = count;
        this.status = status;
    }

    public IpInfo(String ip, String address, String ua, Date time, String axis, Long count, Integer status) {
        this.ip = ip;
        this.address = address;
        this.ua = ua;
        this.time = time;
        this.axis = axis;
        this.count = count;
        this.status = status;
    }
}
