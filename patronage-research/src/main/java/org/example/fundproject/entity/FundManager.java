package org.example.fundproject.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "fund_managers")
public class FundManager {
    @Id
    private String managerId;
    private String managerName;
    private String gender;
    private LocalDate employmentDate;
    private String education;
    private Integer yearsOfExperience;
    private String companyName;
    private String managementScale;
    private String bestReturn;
    private String workingCompany;
    private String managedFunds;

    @ElementCollection
    @CollectionTable(name = "manager_tags", joinColumns = @JoinColumn(name = "manager_id"))
    @Column(name = "tag")
    private Set<String> tags;

    // getters and setters


    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }
    public String getManagerName() {
        return managerName;
    }
    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }
    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public LocalDate getEmploymentDate() {
        return employmentDate;
    }
    public void setEmploymentDate(LocalDate employmentDate) {
        this.employmentDate = employmentDate;
    }
    public String getEducation() {
        return education;
    }
    public void setEducation(String education) {
        this.education = education;
    }
    public Integer getYearsOfExperience() {
        return yearsOfExperience;
    }
    public void setYearsOfExperience(Integer yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }
    public String getCompanyName() {
        return companyName;

    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getManagementScale() {
        return managementScale;
    }
    public void setManagementScale(String managementScale) {
        this.managementScale = managementScale;
    }
    public String getBestReturn() {
        return bestReturn;
    }
    public void setBestReturn(String bestReturn) {
        this.bestReturn = bestReturn;
    }
    public String getWorkingCompany() {
        return workingCompany;
    }
    public void setWorkingCompany(String workingCompany) {
        this.workingCompany = workingCompany;
    }
    public String getManagedFunds() {
        return managedFunds;
    }
    public void setManagedFunds(String managedFunds) {
        this.managedFunds = managedFunds;
    }
    public Set<String> getTags() {
        return tags;
    }
    public void setTags(Set<String> tags) {
        this.tags = tags;
    }


}