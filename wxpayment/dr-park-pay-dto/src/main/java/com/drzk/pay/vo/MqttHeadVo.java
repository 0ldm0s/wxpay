package com.drzk.pay.vo;

import java.io.Serializable;

/**
 * 2018/6/23 cx
 */
public class MqttHeadVo implements Serializable {

    //版本号
    private String version;
    //业务方法
    private String method;
    //数据主键
    private String subId;
    //状态 1  成功  0 失败
    private String status;
    //消息描述
    private String message;
    //车场编号
    private String parkingNo;
    //执行操作 1新增 2修改 3 删除
    private String executeType;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParkingNo() {
        return parkingNo;
    }

    public void setParkingNo(String parkingNo) {
        this.parkingNo = parkingNo;
    }

    public String getExecuteType() {
        return executeType;
    }

    public void setExecuteType(String executeType) {
        this.executeType = executeType;
    }

    public void clearSubId() {
        if(getSubId()!=null||"".equals( getSubId() )){
            setSubId( null );
        }
    }

    @Override
    public String toString() {
        return "MqttHeadVo{" +
                "version='" + version + '\'' +
                ", method='" + method + '\'' +
                ", subId='" + subId + '\'' +
                ", status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", parkingNo='" + parkingNo + '\'' +
                ", executeType='" + executeType + '\'' +
                '}';
    }
}
