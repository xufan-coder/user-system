
package com.zerody.user.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 伙伴账户推送app
 *
 * @author  DaBai
 * @date  2021/7/9 16:15
 */

@TableName("app_user_push")
@Data
public class AppUserPush implements java.io.Serializable {
  		/**id**/
		@TableId(type = IdType.UUID)
		private String id;
  		/**用户ID**/
		private String userId;
		/**企业Id**/
		private String compId;
  		/**创建时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
		/**发送时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date sendTime;
		/** 状态(1已推送，0未推送） */
		private Integer state;
		/**是否删除**/
		private Integer deleted;
		/** 是否已离职 */
		private Integer resigned;
		/**离职状态是否推送(1已推送，0未推送）**/
		private Integer resignedState;
		/**修改时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date updateTime;

}
