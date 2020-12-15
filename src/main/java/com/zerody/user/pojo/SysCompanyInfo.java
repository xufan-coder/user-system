package com.zerody.user.pojo;

import com.zerody.user.pojo.base.BaseModel;

import java.util.Date;

public class SysCompanyInfo extends BaseModel {


    //所属集团企业
    private String blocId;

    //企业名称
    private String companyName;

    //企业编号，系统定义一套规则 自动生成
    private String companyCode;

    //企业简称
    private String companyShortName;

    //注册资本
    private String registeredCapital;

    //企业性质：1.国有企业、2.集体企业、3.私人企业、4.外资企业、5.人体企业
    private String enterpriseNature;

    //是否一般纳税人
    private Byte isTaxPayer;

    //公司地址省编码
    private String companyAddrProvinceCode;

    //公司地址市编码
    private String companyAddressCityCode;

    //公司地址区编码
    private String companyAddressAreaCode;

    //公司地址
    private String companyAddress;

    //企业邮箱
    private String enterpriseEmail;

    //企业座机号码
    private String businessLandlineNumber;

    //联系人姓名
    private String contactName;

    //联系人手机
    private String contactPhone;

    //联系人邮箱
    private String contactMail;

    //纳税人识别号
    private String taxpayerIdentificationNumber;

    //
    private String businessLicenseAddress;

    private String businessLicenseCode;

    private String businessLicenseUrl;

    private String businessLicenseValid;

    private String legalRepresentative;

    private String legalPersoncertNo;

    private Byte companyType;

    private String parentCompanyInfoId;

    private String adminAccount;


    private String companyIntroduction;

    public String getBlocId() {
        return blocId;
    }

    public void setBlocId(String blocId) {
        this.blocId = blocId == null ? null : blocId.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode == null ? null : companyCode.trim();
    }

    public String getCompanyShortName() {
        return companyShortName;
    }

    public void setCompanyShortName(String companyShortName) {
        this.companyShortName = companyShortName == null ? null : companyShortName.trim();
    }

    public String getRegisteredCapital() {
        return registeredCapital;
    }

    public void setRegisteredCapital(String registeredCapital) {
        this.registeredCapital = registeredCapital == null ? null : registeredCapital.trim();
    }

    public String getEnterpriseNature() {
        return enterpriseNature;
    }

    public void setEnterpriseNature(String enterpriseNature) {
        this.enterpriseNature = enterpriseNature == null ? null : enterpriseNature.trim();
    }

    public Byte getIsTaxPayer() {
        return isTaxPayer;
    }

    public void setIsTaxPayer(Byte isTaxPayer) {
        this.isTaxPayer = isTaxPayer;
    }

    public String getCompanyAddrProvinceCode() {
        return companyAddrProvinceCode;
    }

    public void setCompanyAddrProvinceCode(String companyAddrProvinceCode) {
        this.companyAddrProvinceCode = companyAddrProvinceCode == null ? null : companyAddrProvinceCode.trim();
    }

    public String getCompanyAddressCityCode() {
        return companyAddressCityCode;
    }

    public void setCompanyAddressCityCode(String companyAddressCityCode) {
        this.companyAddressCityCode = companyAddressCityCode == null ? null : companyAddressCityCode.trim();
    }

    public String getCompanyAddressAreaCode() {
        return companyAddressAreaCode;
    }

    public void setCompanyAddressAreaCode(String companyAddressAreaCode) {
        this.companyAddressAreaCode = companyAddressAreaCode == null ? null : companyAddressAreaCode.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getEnterpriseEmail() {
        return enterpriseEmail;
    }

    public void setEnterpriseEmail(String enterpriseEmail) {
        this.enterpriseEmail = enterpriseEmail == null ? null : enterpriseEmail.trim();
    }

    public String getBusinessLandlineNumber() {
        return businessLandlineNumber;
    }

    public void setBusinessLandlineNumber(String businessLandlineNumber) {
        this.businessLandlineNumber = businessLandlineNumber == null ? null : businessLandlineNumber.trim();
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName == null ? null : contactName.trim();
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone == null ? null : contactPhone.trim();
    }

    public String getContactMail() {
        return contactMail;
    }

    public void setContactMail(String contactMail) {
        this.contactMail = contactMail == null ? null : contactMail.trim();
    }

    public String getTaxpayerIdentificationNumber() {
        return taxpayerIdentificationNumber;
    }

    public void setTaxpayerIdentificationNumber(String taxpayerIdentificationNumber) {
        this.taxpayerIdentificationNumber = taxpayerIdentificationNumber == null ? null : taxpayerIdentificationNumber.trim();
    }

    public String getBusinessLicenseAddress() {
        return businessLicenseAddress;
    }

    public void setBusinessLicenseAddress(String businessLicenseAddress) {
        this.businessLicenseAddress = businessLicenseAddress == null ? null : businessLicenseAddress.trim();
    }

    public String getBusinessLicenseCode() {
        return businessLicenseCode;
    }

    public void setBusinessLicenseCode(String businessLicenseCode) {
        this.businessLicenseCode = businessLicenseCode == null ? null : businessLicenseCode.trim();
    }

    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl == null ? null : businessLicenseUrl.trim();
    }

    public String getBusinessLicenseValid() {
        return businessLicenseValid;
    }

    public void setBusinessLicenseValid(String businessLicenseValid) {
        this.businessLicenseValid = businessLicenseValid == null ? null : businessLicenseValid.trim();
    }

    public String getLegalRepresentative() {
        return legalRepresentative;
    }

    public void setLegalRepresentative(String legalRepresentative) {
        this.legalRepresentative = legalRepresentative == null ? null : legalRepresentative.trim();
    }

    public String getLegalPersoncertNo() {
        return legalPersoncertNo;
    }

    public void setLegalPersoncertNo(String legalPersoncertNo) {
        this.legalPersoncertNo = legalPersoncertNo == null ? null : legalPersoncertNo.trim();
    }

    public Byte getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Byte companyType) {
        this.companyType = companyType;
    }

    public String getParentCompanyInfoId() {
        return parentCompanyInfoId;
    }

    public void setParentCompanyInfoId(String parentCompanyInfoId) {
        this.parentCompanyInfoId = parentCompanyInfoId == null ? null : parentCompanyInfoId.trim();
    }

    public String getAdminAccount() {
        return adminAccount;
    }

    public void setAdminAccount(String adminAccount) {
        this.adminAccount = adminAccount == null ? null : adminAccount.trim();
    }

    public String getCompanyIntroduction() {
        return companyIntroduction;
    }

    public void setCompanyIntroduction(String companyIntroduction) {
        this.companyIntroduction = companyIntroduction == null ? null : companyIntroduction.trim();
    }
}