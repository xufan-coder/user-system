package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.CeoCompanyRef;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.dto.BackRefDto;
import com.zerody.user.dto.CeoRefDto;
import com.zerody.user.vo.BackUserRefVo;
import com.zerody.user.vo.CeoRefVo;

import java.util.List;

/**
 * @author  DaBai
 * @date  2022/6/18 11:50
 */
public interface CeoCompanyRefService extends IService<CeoCompanyRef> {


	void saveCompanyRef(CeoRefDto data);

	CeoRefVo getCeoRef(String id);

	void saveBackCompanyRef(BackRefDto data);

	BackUserRefVo getBackRef(String id);

	/**
	* @Author: chenKeFeng
	* @param
	* @Description: 获取ceo关联的企业信息
	* @Date: 2022/10/20 15:22
	*/
	public List<CeoCompanyRef> getBackRefById(String ceoId);
}
