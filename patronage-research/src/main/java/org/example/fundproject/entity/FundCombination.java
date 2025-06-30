package org.example.fundproject.entity;



import java.util.Date;
import java.util.List;

public class FundCombination {
    private String id;
    private String combinationName;
    private List<String> fundCodes;
    private Date createTime;
    private Date updateTime;
    private String creator;

    // 组合分析指标
    private Double averageReturn;
    private Double volatility;
    private Double sharpeRatio;

    // getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCombinationName() {
        return combinationName;
    }

    public void setCombinationName(String name) {
        this.combinationName = name;
    }

    public List<String> getFundCodes() {
        return fundCodes;
    }

    public void setFundCodes(List<String> fundCodes) {
        this.fundCodes = fundCodes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Double getAverageReturn() {
        return averageReturn;
    }

    public void setAverageReturn(Double averageReturn) {
        this.averageReturn = averageReturn;
    }

    public Double getVolatility() {
        return volatility;
    }

    public void setVolatility(Double volatility) {
        this.volatility = volatility;
    }

    public Double getSharpeRatio() {
        return sharpeRatio;
    }

    public void setSharpeRatio(Double sharpeRatio) {
        this.sharpeRatio = sharpeRatio;
    }
}