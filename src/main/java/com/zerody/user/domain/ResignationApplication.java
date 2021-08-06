
package com.zerody.user.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author  DaBai
 * @date  2021/8/5 15:05
 */

@Data
public class ResignationApplication implements java.io.Serializable {
  		/**id**/
		@TableId(type = IdType.UUID)
		private String id;
  		/**user_id**/
		private String userId;
		private String name;

		private String staffId;
		/** 审批状态*/
		private String approvalState;
		/**创建时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date approvalTime;
		/**离职时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date resignationTime;

		private String companyId;
		private String companyName;
		private String departId;
		private String departName;
		private String positionId;
		private String positionName;
		private String reason;
		private String remark;
		/**创建时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;


}
