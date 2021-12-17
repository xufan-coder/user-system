package com.zerody.user.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerody.user.enums.ImportStateEnum;
import lombok.Data;

import java.util.Date;

/**
 * @author PengQiang
 * @ClassName ImportInfoQueryVo
 * @DateTime 2021/12/14_16:51
 * @Deacription TODO
 */
@Data
public class ImportInfoQueryVo {


    private String id;

    /**
     * 文件名称
     */
    private String importFileName;

    /**
     * 导入人id
     */
    private String userId;

    /**
     * 导入人名称
     */
    private String userName;

    /**
     * 导入时间
     */
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * excel 行数
     */
    private Integer excelRows;

    /**
     * 成功条数
     */
    private Integer successNum;

    /**
     * 失败条数
     */
    private Integer errorNum;

    /**
     * 所属企业id
     */
    private String companyId;

    /**
     * 所属企业
     */
    private String companyName;

    /**
     * 导入类型(1.外部内控名单导入)
     */
    private Integer importType;

    /**
     * 所属企业
     */
    private String importState;

    public String getImportStateString() {
        return ImportStateEnum.getDesc(this.importState);
    }

}
