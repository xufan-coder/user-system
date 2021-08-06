package com.zerody.user.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.Image;
import com.zerody.user.domain.ResignationApplication;
import com.zerody.user.dto.ResignationPageDto;

import java.util.List;

/**
 * @author  DaBai
 * @date  2021/8/5 15:09
 */

public interface ResignationApplicationService extends IService<ResignationApplication> {

    ResignationApplication addOrUpdateResignationApplication(ResignationApplication data);

    IPage<ResignationApplication> selectPage(ResignationPageDto dto);
}
