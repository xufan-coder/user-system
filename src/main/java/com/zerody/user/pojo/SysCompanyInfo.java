package com.zerody.user.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zerody.user.pojo.base.BaseModel;
import lombok.Data;

import java.util.Date;

@Data
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

    //营业执照所在地
    private String businessLicenseAddress;

    //统一社会信用代码
    private String businessLicenseCode;

    //营业执照图片
    private String businessLicenseUrl;

    //营业执照有效期
    private String businessLicenseValid;

    //法定代表人
    private String legalRepresentative;

    //法人证件证号
    private String legalPersoncertNo;

    //企业类型(1.客户企业,2.运营企业,3.合作企业, 4.管理员企业,5.客户运营企业)
    private Byte companyType;

    private String parentCompanyInfoId;

    //企业管理员账号
    private String adminAccount;

    //企业简介
    private String companyIntroduction;

    //备注
    private String remark;

}