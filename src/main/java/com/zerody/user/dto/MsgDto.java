/**
 * 
 */
package com.zerody.user.dto;

import java.util.Date;
import lombok.Data;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * MsgForm 
 * @author 黄华盛 autocoder
 * @date 2021-01-11
 */
 @Data
public class MsgDto{
  		 @NotEmpty(message = "id不能为空")
  		/**id**/
		private String id;
  		/**user_id**/
		private String userId;
  		/**创建时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
		private Date createTime;
  		/**消息内容**/
		private String messageContent;
  		/**消息标题**/
		private String messageTile;

}
