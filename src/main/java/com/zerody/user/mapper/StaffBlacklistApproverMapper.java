package com.zerody.user.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.domain.StaffBlacklistApprover;
import com.zerody.user.dto.StaffBlacklistApproverPageDto;
import com.zerody.user.vo.StaffBlacklistApproverVo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface StaffBlacklistApproverMapper extends BaseMapper<StaffBlacklistApprover> {

    IPage<StaffBlacklistApproverVo> getBlacklistApproverPage(StaffBlacklistApproverPageDto param, IPage<StaffBlacklistApproverVo> iPage);
}
