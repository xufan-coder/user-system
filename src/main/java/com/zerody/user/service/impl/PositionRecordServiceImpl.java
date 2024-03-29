package com.zerody.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.constant.YesNo;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.domain.BlacklistOperationRecord;
import com.zerody.user.domain.Dict;
import com.zerody.user.domain.PositionRecord;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.BlacklistOperationRecordAddDto;
import com.zerody.user.mapper.PositionRecordMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.service.BlacklistOperationRecordService;
import com.zerody.user.service.DictService;
import com.zerody.user.service.PositionRecordService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.vo.PositionRecordListVo;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PositionRecordServiceImpl extends ServiceImpl<PositionRecordMapper, PositionRecord> implements PositionRecordService {

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private DictService dictService;

    @Override
    public List<PositionRecordListVo> queryPositionRecord(String certificateCard) {
        QueryWrapper<PositionRecord> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(PositionRecord::getCertificateCard,certificateCard);
        List<PositionRecord> list = this.list(wrapper);
        ArrayList<PositionRecordListVo> vos = new ArrayList<>();
        if(ObjectUtils.isNotEmpty(list) && list.size()!=0){
            for (PositionRecord positionRecord : list) {
                PositionRecordListVo vo = new PositionRecordListVo();
                BeanUtils.copyProperties(positionRecord,vo);
                if(StringUtils.isNotEmpty(positionRecord.getLeaveType())){
                    Dict dict = this.dictService.getById(positionRecord.getLeaveType());
                    if(DataUtil.isNotEmpty(dict)){
                        vo.setLeaveTypeText(dict.getDictName());
                    }
                }
                vos.add(vo);
            }
        }
        return vos;
    }

    @Override
    public List<PositionRecordListVo> getPositionRecord(String userId) {
        SysUserInfo userInfo = sysUserInfoMapper.selectById(userId);
        if(DataUtil.isNotEmpty(userInfo)) {
            return queryPositionRecord(userInfo.getCertificateCard());
        }
        return new ArrayList<>();
    }
}
