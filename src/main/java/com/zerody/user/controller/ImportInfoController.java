package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.constant.ImportResultInfoType;
import com.zerody.user.domain.ImportInfo;
import com.zerody.user.domain.ImportResultInfo;
import com.zerody.user.dto.ExportDto;
import com.zerody.user.dto.ImportInfoQueryDto;
import com.zerody.user.service.ImportInfoService;
import com.zerody.user.vo.ImportInfoQueryVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName ImportInfoController
 * @DateTime 2021/12/14_16:46
 * @Deacription TODO
 */
@Slf4j
@RestController
@RequestMapping("/import-info")
public class ImportInfoController {

    @Autowired
    private ImportInfoService importInfoService;


    /**
     *
     *  分页查询历史记录(通用接口)
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/12/14 17:19
     * @param                param
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.ImportInfoQueryVo>>
     */
    @GetMapping("/page")
    public DataResult<IPage<ImportInfoQueryVo>> getPageImportInfo(ImportInfoQueryDto param) {

        try {
            if (UserUtils.getUser().isBackAdmin()) {
                param.setCompanyId(UserUtils.getUser().getCompanyId());
            }
            if (DataUtil.isEmpty(param.getType())) {
                param.setType(ImportResultInfoType.STAFF_BLACK_EXTERNAL);
            }
            IPage<ImportInfoQueryVo> iPage = this.importInfoService.getPageImportInfo(param);
            return R.success(iPage);
        } catch (DefaultException e) {
            log.error("分页查询导入记录异常：", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("分页查询导入记录异常：", e, e);
            return R.error(e.getMessage());
        }
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          根据id查询导入信息
     * @date                 2021/12/27 14:58
     * @param                id
     * @return               com.zerody.common.api.bean.DataResult<com.zerody.user.domain.ImportInfo>
     */
    @GetMapping("/get/{id}")
    public DataResult<ImportInfo> getByIdImportInfo(@PathVariable("id") String id) {

        try {
            ImportInfo result = this.importInfoService.getById(id);
            return R.success(result);
        } catch (DefaultException e) {
            log.error("根据id查询导入信息异常：", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据id查询导入信息异常：", e, e);
            return R.error(e.getMessage());
        }
    }


    /**
     *
     *  导出记录
     * @author               PengQiang
     * @description          DELL
     * @date                 2021/12/14 17:19
     * @param                response
     * @param                param
     * @return               com.zerody.common.api.bean.DataResult<java.lang.Void>
     */
    @PostMapping("/export")
    public DataResult<Void> doExportImportInfo(HttpServletResponse response,@Validated @RequestBody ExportDto param) {

        try {
            this.importInfoService.doExportImportInfo(response, param.getId());
            return R.success();
        } catch (DefaultException e) {
            log.error("导入历史记录导出异常：", e, e);
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("导入历史记录导出异常：", e, e);
            return R.error(e.getMessage());
        }
    }


}
