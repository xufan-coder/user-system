package com.zerody.user.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zerody.user.domain.base.BaseModel;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class SysCompanyInfo extends BaseModel {


    /**
     *
     * 所属集团企业
     */
    private String blocId;

    /**
     * 企业名称
     */
    @NotEmpty(message = "企业名称不能为空")
    private String companyName;

    /**
     *
     * 企业编号，系统定义一套规则 自动生成
     */
    private String companyCode;

    /**
     *
     *      企业简称
     */
    private String companyShortName;

    /**
     *
     *      注册资本
     */
    private String registeredCapital;

    /**
     *
     *企业性质：1.国有企业、2.集体企业、3.私人企业、4.外资企业、5.人体企业
     */
    private String enterpriseNature;

    /**
     *
     *是否一般纳税人
     */
    private Integer isTaxPayer;

    /**
     *
     *公司地址省编码
     */
    @NotEmpty(message = "请选择省份")
    private String companyAddrProvinceCode;

    /**
     *
     *公司地址市编码
     */
    @NotEmpty(message = "请输入市")
    private String companyAddressCityCode;

    /**
     *
     *公司地址区编码
     */
    @NotEmpty(message = "请选择区/县")
    private String companyAddressAreaCode;

    /**
     *
     *公司地址
     */
    private String companyAddress;

    /**
     *
     *企业邮箱
     */
    private String enterpriseEmail;

    /**
     *
     *企业座机号码
     */
    private String businessLandlineNumber;

    /**
     *
     *联系人姓名
     */
    @NotEmpty(message = "请输入管理员")
    private String contactName;

    /**
     *
     *联系人手机
     */
    @NotEmpty(message = "请输入管理员手机号")
    @Length(message = "手机号错误", min = 11, max = 11)
    private String contactPhone;

    /**
     *
     *联系人邮箱
     */
    private String contactMail;

    /**
     *
     *纳税人识别号
     */
    private String taxpayerIdentificationNumber;

    /**
     *
     *营业执照所在地
     */
    private String businessLicenseAddress;

    /**
     *
     *统一社会信用代码
     */
    private String businessLicenseCode;

    /**
     *
     *营业执照图片
     */
    private String businessLicenseUrl;

    /**
     *
     *营业执照有效期
     */
    private String businessLicenseValid;

    /**
     *
     *法定代表人
     */
    private String legalRepresentative;

    /**
     *
     *法人证件证号
     */
    private String legalPersoncertNo;

    /**
     *
     *企业类型(1.客户企业,2.运营企业,3.合作企业, 4.管理员企业,5.客户运营企业)
     */
    private Integer companyType;

    /**
     *
     *上级集团ID
     */
    private String parentCompanyInfoId;

    /**
     *
     *企业管理员账号
     */
    private String adminAccount;

    /**
     *
     *企业简介
     */
    private String companyIntroduction;

    /**
     *
     *备注
     */
    @TableField("remark_")
    private String remark;


    /** 企业logo */
    private String companyLogo;

    private Integer isUpdateName;

    /** 经度 */
    private Float lon;

    /** 纬度 */
    private Float lat;

}