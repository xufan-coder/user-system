package com.zerody.user.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.enums.StaffStatusEnum;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author DaBai
 * @date 2021/1/5 11:26
 */

@Data
public class  BosStaffInfoVo {

    /**
     * ID
     */
    private String id;

    /**
     * 员工ID
     */
    private String staffId;

    /**
     * 角色ID
     */
    private String roleId;

    /**
     * 企业ID
     */
    private String compId;

    /**
     * 企业名称
     */
    private String companyName;

    /**
     * 员工姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 岗位名称
     */
    private String positionName;

    /**
     * 部门名称
     */
    private String departName;

    /**
     * 角色
     */
    private String roleName;

    /**
     * 是否钻石会员（0-否 1-是）
     */
    private Integer isDiamondMember;

    /**
     * 员工状态 0在职、1离职、3合作
     */
    private Integer staffStatus;

    /**
     * 头像(相对路径)
     **/
    private String avatar;

    /**
     * 是否查看完整的手机号
     */
    private Integer isShowMobile;

    private Boolean isAdmin;

    /**
     * 是否为黑名单
     */
    private Boolean isBlock;

    /**
     * 员工头像
     */
    private String staffAvatar;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 评价
     */
    private String evaluate;

    /**
     * 判断是否有简历
     */
    private String resumeUrl;

    /**
     * 账号状态
     */
    private Integer useState;

    /**
     * 企业id 集合
     */
    @TableField(exist = false)
    private List<String> companys;

    public String getStaffStatusString() {
        if (DataUtil.isEmpty(this.staffStatus)) {
            return "";
        }
        StatusEnum statusEnum = StatusEnum.getByValue(this.staffStatus);
        if (DataUtil.isEmpty(statusEnum)) {
            return "";
        }
        return statusEnum.getDesc();
    }

    public String getPhone() {
        if (StringUtils.isEmpty(this.phone)) {
            return null;
        }
        return this.phone.replaceAll("(\\d{3})\\d{4}(\\w{4})", "$1****$2");
    }

    public String getPhoneFull() {
        return this.phone;
    }

}
