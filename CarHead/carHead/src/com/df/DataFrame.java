package com.df;

import java.io.Serializable;

public class DataFrame implements Serializable{
    private static final long serialVersionUID = 1L;
    private String ip;	// 목적지 IP Address
    private String sender;	// 발신 위치
    private String contents;	// Data: SensorID + SensorData

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public DataFrame() {
    }

    public DataFrame(String ip, String contents) {
        this.ip = ip;
        this.contents = contents;
    }


    public DataFrame(String ip, String sender, String contents) {
        this.ip = ip;
        this.sender = sender;
        this.contents = contents;
    }
}
