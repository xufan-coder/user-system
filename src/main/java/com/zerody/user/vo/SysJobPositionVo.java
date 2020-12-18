package com.zerody.user.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author PengQiang
 * @ClassName SysJobPositionVo
 * @DateTime 2020/12/18_18:23
 * @Deacription TODO
 */
@Data
@TableName("sys_job_position")
public class SysJobPositionVo {

    //主键id
    @TableField("id")
    private String id;

    //企业id
    @TableField("comp_id")
    private String compId;

    //岗位名称
    @TableField("position_name")
    private String positionName;

    //职责范围
    @TableField("job_scope")
    private String jobScope;

    //父级岗位id
    @TableField("parent_id")
    private String parentId;

    //岗位级别
    @TableField("level")
    private Integer level;

    //部门id
    @TableField("depart_id")
    private String departId;
}
