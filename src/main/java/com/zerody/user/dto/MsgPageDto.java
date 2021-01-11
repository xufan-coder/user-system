/**
 * 
 */
package com.zerody.user.dto;

import java.util.Date;
import com.zerody.user.domain.*;
import com.zerody.common.api.bean.PageQueryDto;
/**
 * MsgPageDto 
 * @author 黄华盛 
 * @date 2021-01-11
 */
public class MsgPageDto extends PageQueryDto{
		private Date createTimeStart;
		private Date createTimeEnd;
		private String userId;
		public Date getCreateTimeStart(){
			return this.createTimeStart;
		}
	
		public void setCreateTimeStart(Date createTimeStart) {
			this.createTimeStart = createTimeStart;
		}
		public Date getCreateTimeEnd(){
			return this.createTimeEnd;
		}
	
		public void setCreateTimeEnd(Date createTimeEnd) {
			this.createTimeEnd = createTimeEnd;
		}
	
		public String getUserId(){
			return this.userId;
		}
	
		public void setUserId(String userId) {
			this.userId = userId;
		}
	

}
