
package com.zerody.user.domain;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author 黄华盛
 * @date 2021-01-11
 */
@TableName("msg")
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
		public Msg() {} 
		public Msg(String id,String userId,Date createTime,String messageContent,
				String messageTile){		
				this.id=id;
				this.userId=userId;
				this.createTime=createTime;
				this.messageContent=messageContent;
				this.messageTile=messageTile;		} 
	
		public String getId() {
			return this.id;
		}
	
		public void setId(String id) {
			this.id = id;
		}
	
		public String getUserId() {
			return this.userId;
		}
	
		public void setUserId(String userId) {
			this.userId = userId;
		}
	
		public Date getCreateTime() {
			return this.createTime;
		}
	
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
	
		public String getMessageContent() {
			return this.messageContent;
		}
	
		public void setMessageContent(String messageContent) {
			this.messageContent = messageContent;
		}
	
		public String getMessageTile() {
			return this.messageTile;
		}
	
		public void setMessageTile(String messageTile) {
			this.messageTile = messageTile;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
			return result;
		}
	
		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
                return true;
            }
			if (obj == null) {
                return false;
            }
			if (getClass() != obj.getClass()) {
                return false;
            }
			Msg other = (Msg) obj;
			if (getId()== null) {
				if (other.getId()!= null) {
                    return false;
                }
			} else if (!getId().equals(other.getId())) {
                return false;
            }
			return true;
		}
			@Override
			public String toString() {

				StringBuffer sb=new StringBuffer();
				sb.append("Msg").append(' ').append('[');
		  				sb.append("id=").append(id);
		  				sb.append(',');
		  				sb.append("userId=").append(userId);
		  				sb.append(',');
		  				sb.append("createTime=").append(createTime);
		  				sb.append(',');
		  				sb.append("messageContent=").append(messageContent);
		  				sb.append(',');
		  				sb.append("messageTile=").append(messageTile);
		  				sb.append(',');
					sb.append(']');
					return sb.toString();
			}
	
}