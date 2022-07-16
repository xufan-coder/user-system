package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.user.domain.UserOpinionType;
import com.zerody.user.mapper.UserOpinionTypeMapper;
import com.zerody.user.service.UserOpinionTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author DELL
 */
@Service
public class UserOpinionTypeServiceImpl extends ServiceImpl<UserOpinionTypeMapper, UserOpinionType>  implements UserOpinionTypeService{


    @Override
    public void addOpinionType(UserOpinionType opinionType) {
        opinionType.setId(UUIDutils.getUUID32());
        checkName(null,opinionType.getName());
        this.save(opinionType);
    }

    @Override
    public void updateOpinionType(UserOpinionType opinionType) {
        checkName(opinionType.getId(),opinionType.getName());
        this.updateById(opinionType);
    }

    @Override
    public List<UserOpinionType> getTypeAll() {
        QueryWrapper<UserOpinionType> qw = new QueryWrapper<>();
        qw.lambda().eq(UserOpinionType::getDeleted, YesNo.NO);
        return this.list(qw);
    }

    /**检查分类名称是否已存在*/
    private void checkName(String id,String name){
        QueryWrapper<UserOpinionType> qw = new QueryWrapper<>();
        qw.lambda().eq(UserOpinionType::getName,name);
        qw.lambda().eq(UserOpinionType::getDeleted, YesNo.NO);
        qw.lambda().notIn(StringUtils.isNotEmpty(id),UserOpinionType::getId,id);
        int cont =this.count(qw);
        if(cont >0) {
            throw new DefaultException("该分类名称已存在");
        }
    }
}
