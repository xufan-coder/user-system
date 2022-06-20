package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.CeoCompanyRef;
import com.zerody.user.domain.CompanyAdmin;
import com.zerody.user.dto.CeoRefDto;
import com.zerody.user.vo.CeoRefVo;

/**
 * @author  DaBai
 * @date  2022/6/18 11:50
 */
public interface CeoCompanyRefService extends IService<CeoCompanyRef> {


	void saveCompanyRef(CeoRefDto data);

	CeoRefVo getCeoRef(String id);
}
