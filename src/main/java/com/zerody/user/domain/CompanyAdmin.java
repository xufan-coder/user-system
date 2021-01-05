
package com.zerody.user.domain;


import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * @author 黄华盛
 * @date 2021-01-04
 */
@TableName("company_admin")
public class CompanyAdmin implements java.io.Serializable {
  		/**id**/
		@TableId(type = IdType.UUID)
		private String id;
  		/**员工ID**/
		private String staffId;
  		/**状态:1在职、2离职、3合作**/
		private Integer state;
  		/**角色**/
		private String roleId;
  		/**更新人id**/
		private String updateBy;
  		/**更新人名称**/
		private String updateUsername;
  		/**更新时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
		private Date updateTime;
  		/**创建人名称**/
		private String createUsername;
  		/**创建人id**/
		private String createBy;
  		/**创建时间**/
		@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
		private Date createTime;
  		/**删除**/
		private Integer deleted;
  		/**企业id**/
		private String companyId;
		public CompanyAdmin() {} 
		public CompanyAdmin(String id,String userId,Integer state,String roleId,
				String updateBy,String updateUsername,Date updateTime,
				String createUsername,String createBy,Date createTime,
				Integer deleted,String companyId){		
				this.id=id;
				this.staffId=userId;
				this.state=state;
				this.roleId=roleId;
				this.updateBy=updateBy;
				this.updateUsername=updateUsername;
				this.updateTime=updateTime;
				this.createUsername=createUsername;
				this.createBy=createBy;
				this.createTime=createTime;
				this.deleted=deleted;
				this.companyId=companyId;		} 
	
		public String getId() {
			return this.id;
		}
	
		public void setId(String id) {
			this.id = id;
		}

	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public Integer getState() {
			return this.state;
		}
	
		public void setState(Integer state) {
			this.state = state;
		}
	
		public String getRoleId() {
			return this.roleId;
		}
	
		public void setRoleId(String roleId) {
			this.roleId = roleId;
		}
	
		public String getUpdateBy() {
			return this.updateBy;
		}
	
		public void setUpdateBy(String updateBy) {
			this.updateBy = updateBy;
		}
	
		public String getUpdateUsername() {
			return this.updateUsername;
		}
	
		public void setUpdateUsername(String updateUsername) {
			this.updateUsername = updateUsername;
		}
	
		public Date getUpdateTime() {
			return this.updateTime;
		}
	
		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}
	
		public String getCreateUsername() {
			return this.createUsername;
		}
	
		public void setCreateUsername(String createUsername) {
			this.createUsername = createUsername;
		}
	
		public String getCreateBy() {
			return this.createBy;
		}
	
		public void setCreateBy(String createBy) {
			this.createBy = createBy;
		}
	
		public Date getCreateTime() {
			return this.createTime;
		}
	
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
	
		public Integer getDeleted() {
			return this.deleted;
		}
	
		public void setDeleted(Integer deleted) {
			this.deleted = deleted;
		}
	
		public String getCompanyId() {
			return this.companyId;
		}
	
		public void setCompanyId(String companyId) {
			this.companyId = companyId;
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
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CompanyAdmin other = (CompanyAdmin) obj;
			if (getId()== null) {
				if (other.getId()!= null)
					return false;
			} else if (!getId().equals(other.getId()))
				return false;
			return true;
		}

	
}