package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.dto.SysJobPositionDto;
import com.zerody.user.domain.SysJobPosition;
import com.zerody.user.vo.SysJobPositionVo;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:54
 * @param
 * @return
 */
@Mapper
public interface SysJobPositionMapper extends BaseMapper<SysJobPosition> {

    /**
     *
     * 分页查询部门
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:54
     * @param                sysJobPositionDto
     * @param                iPage
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.SysJobPositionVo>
     */
    IPage<SysJobPositionVo> getPageJob(@Param("job") SysJobPositionDto sysJobPositionDto, IPage iPage);

    /**
     *
     * 查询企业下的所有岗位
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/1/19 14:55
     * @param                companuId
     * @return               java.util.List<com.zerody.user.vo.SysJobPositionVo>
     */
    List<SysJobPositionVo> getAllJobByCompanyId(String companuId);

    List<Map<String, String>> getJobtEditInfo();

    void updateJobEditInfo(@Param("jobs") List<Map<String, String>> jobMap);
}