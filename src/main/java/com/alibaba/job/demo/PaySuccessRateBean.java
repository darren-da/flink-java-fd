package com.alibaba.job.demo;

/**
 * @author fada.yu
 * @version 1.0
 * @date 2022/3/7 12:28
 * @Desc:
 */
public class PaySuccessRateBean{
    //支付Id
    private String payId;
    //用户Id
    private int userId;
    //下单时间
    private Long payStart;
    //付款时间（此订单若失败，payEnd=0）
    private Long payEnd;
    //终端渠道
    private String channel;
    //页面打开code
    private int pageOpen;
    //页面来源
    private int srcCode;
    //影响源code
    private int referCode;
    //eventTime
    private Long ts;
    //事件版本
    private String vc;

    public PaySuccessRateBean(String payId, int userId, Long payStart, Long payEnd, String channel, int pageOpen, int srcCode, int referCode, Long ts, String vc) {
        this.payId = payId;
        this.userId = userId;
        this.payStart = payStart;
        this.payEnd = payEnd;
        this.channel = channel;
        this.pageOpen = pageOpen;
        this.srcCode = srcCode;
        this.referCode = referCode;
        this.ts = ts;
        this.vc = vc;
    }



    @Override
    public String toString() {
        return "PaySuccessRate{" +
                "payId='" + payId + '\'' +
                ", userId=" + userId +
                ", payStart=" + payStart +
                ", payEnd=" + payEnd +
                ", channel='" + channel + '\'' +
                ", pageOpen=" + pageOpen +
                ", srcCode=" + srcCode +
                ", referCode=" + referCode +
                ", ts=" + ts +
                ", vc=" + vc +
                '}';
    }

    public PaySuccessRateBean() {
    }

    public String getPayId() {
        return payId;
    }

    public void setPayId(String payId) {
        this.payId = payId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Long getPayStart() {
        return payStart;
    }

    public void setPayStart(Long payStart) {
        this.payStart = payStart;
    }

    public Long getPayEnd() {
        return payEnd;
    }

    public void setPayEnd(Long payEnd) {
        this.payEnd = payEnd;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getPageOpen() {
        return pageOpen;
    }

    public void setPageOpen(int pageOpen) {
        this.pageOpen = pageOpen;
    }

    public int getSrcCode() {
        return srcCode;
    }

    public void setSrcCode(int srcCode) {
        this.srcCode = srcCode;
    }

    public int getReferCode() {
        return referCode;
    }

    public void setReferCode(int referCode) {
        this.referCode = referCode;
    }

    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    public String getVc() {
        return vc;
    }

    public void setVc(String vc) {
        this.vc = vc;
    }
}