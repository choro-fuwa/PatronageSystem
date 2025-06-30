package org.example.fundproject.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "funds")
public class Fund {
    @Id
    private String fundCode;
    private String fundName;
    private String fundType;
    private LocalDate establishDate;
    private BigDecimal initialNetValue;
    private BigDecimal currentNetValue;
    private String managerId;
    private String companyId;
    private String companyName;
    private String managerName;

    @ElementCollection
    @CollectionTable(name = "fund_tags", joinColumns = @JoinColumn(name = "fund_code"))
    @Column(name = "tag")
    private Set<String> tags;

    // getters and setters

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public LocalDate getEstablishDate() {
        return establishDate;
    }

    public void setEstablishDate(LocalDate establishDate) {
        this.establishDate = establishDate;
    }

    public BigDecimal getInitialNetValue() {
        return initialNetValue;
    }

    public void setInitialNetValue(BigDecimal initialNetValue) {
        this.initialNetValue = initialNetValue;
    }

    public BigDecimal getCurrentNetValue() {
        return currentNetValue;
    }

    public void setCurrentNetValue(BigDecimal currentNetValue) {
        this.currentNetValue = currentNetValue;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    public String getManagerName() {
        return managerName;
    }
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
