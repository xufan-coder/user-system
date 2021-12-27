package com.zerody.user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.ImportInfo;
import com.zerody.user.dto.ImportInfoQueryDto;
import com.zerody.user.vo.ImportInfoQueryVo;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *@ClassName ImportInfoService
 *@author    PengQiang
 *@DateTime  2021/12/14_15:50
 *@Deacription TODO
 */
public interface ImportInfoService extends IService<ImportInfo> {
    /**
     *
     *  分页查询导入记录
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/12/14 17:05
     * @param                param
     * @return               com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.ImportInfoQueryVo>
     */
    IPage<ImportInfoQueryVo> getPageImportInfo(ImportInfoQueryDto param);

    /**
     *
     *  导入历史记录导出
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/12/14 17:20
     * @param                response
     * @param                id
     * @return               void
     */
    void doExportImportInfo(HttpServletResponse response, String id) throws IOException;
}
