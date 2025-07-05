package org.example.fundproject.entity;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "fund_companies")
public class FundCompany {
    @Id
    private String companyId;
    private String companyName;
    private String establishedDate;
    private String companyType;
    private String registeredAddress;
    private String managementScale;
    private String registeredCapital;

    @ElementCollection
    @CollectionTable(name = "company_tags", joinColumns = @JoinColumn(name = "company_id"))
    @Column(name = "tag")
    private Set<String> tags;

    // getters and setters

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEstablishedDate() {
        return establishedDate;
    }

    public void setEstablishedDate(String establishedDate) {
        this.establishedDate = establishedDate;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getRegisteredAddress() {
        return registeredAddress;
    }

    public void setRegisteredAddress(String registeredAddress) {
        this.registeredAddress = registeredAddress;
    }

    public String getManagementScale() {
        return managementScale;
    }

    public void setManagementScale(String managementScale) {
        this.managementScale = managementScale;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }
    public String getRegisteredCapital() {
        return registeredCapital;
    }
    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital;
    }
}