package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.user.domain.ImportInfo;
import com.zerody.user.dto.ImportInfoQueryDto;
import com.zerody.user.vo.ImportInfoQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *@ClassName ImportInfoMapper
 *@author    PengQiang
 *@DateTime  2021/12/14_15:50
 *@Deacription TODO
 */
public interface ImportInfoMapper extends BaseMapper<ImportInfo> {

    /**
     *
     *  分页查询导入历史记录
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/12/14 17:09
     * @param                param
     * @param                iPage
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.ImportInfoQueryVo>
     */
    IPage<ImportInfoQueryVo> getPageImportInfo(@Param("param") ImportInfoQueryDto param, IPage<ImportInfoQueryVo> iPage);

    List<String> getImportIdByTime(@Param("time") Date delTime);
}