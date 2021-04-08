
package com.zerody.user.domain;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * @author 黄华盛
 * @date 2021-01-11
 */
@TableName("msg")
@Data
public class Msg implements java.io.Serializable {
  		/**id**/
		@TableId(type = IdType.UUID)
		private String id;
  		/**user_id**/
		private String userId;
  		/**创建时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
		private Date createTime;
  		/**消息内容**/
		private String messageContent;
  		/**消息标题**/
		private String messageTile;

		/** 消息类型 */
		private String msgType;

		/** 未联系天数 */
		private Integer notContactDays;
}