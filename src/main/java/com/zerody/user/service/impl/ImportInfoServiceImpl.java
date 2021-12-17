package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.ExcelToolUtil;
import com.zerody.user.constant.ImportResultInfoType;
import com.zerody.user.domain.ImportResultInfo;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.dto.ImportInfoQueryDto;
import com.zerody.user.service.ImportResultInfoService;
import com.zerody.user.vo.ImportInfoQueryVo;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.zerody.user.domain.ImportInfo;
import com.zerody.user.mapper.ImportInfoMapper;
import com.zerody.user.service.ImportInfoService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *@ClassName ImportInfoServiceImpl
 *@author    PengQiang
 *@DateTime  2021/12/14_15:50
 *@Deacription TODO
 */
@Service
public class ImportInfoServiceImpl extends ServiceImpl<ImportInfoMapper, ImportInfo> implements ImportInfoService{

    @Autowired
    private ImportResultInfoService importResultInfoService;

    @Override
    public IPage<ImportInfoQueryVo> getPageImportInfo(ImportInfoQueryDto param) {
        IPage<ImportInfoQueryVo> iPage = new Page<>(param.getCurrent(), param.getPageSize());
        iPage = this.baseMapper.getPageImportInfo(param, iPage);
        return iPage;
    }

    @Override
    public void doExportImportInfo(HttpServletResponse response, String id) throws IOException {
        ImportInfo info = this.getById(id);
        String[] header;
        Integer[] requiredNum ;
        List<String[]> data ;
        String fileName;
        if (info.getErrorNum().intValue() == 0) {
            throw new DefaultException("没有错误条数无法导出");
        }
        switch (info.getImportType()) {
            default:
                return;
            case ImportResultInfoType.STAFF_BLACK_EXTERNAL_VICE:
                header = new String[]{"*姓名", "*手机号码", "*身份证号码", "*加入原因", "失败原因"};
                requiredNum = new Integer[]{0, 1, 2, 3, 4};
                fileName = "导入外部内控名单失败记录_" + System.currentTimeMillis();
                data = this.getStaffBlacklistLoseRecord(id);
                break;
        }

        HSSFWorkbook workbook = ExcelToolUtil.createExcel(header, data, requiredNum);
//        response.setContentType("octets/stream");
        response.addHeader("Content-Disposition", "attachment;filename="+new String( fileName.getBytes("gb2312"), "ISO8859-1" )+".xls");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
    }

    private List<String[]> getStaffBlacklistLoseRecord(String importId) {
        List<String[]> data = new ArrayList<>();
        QueryWrapper<ImportResultInfo> importInfoQw = new QueryWrapper<>();
        importInfoQw.lambda().eq(ImportResultInfo::getImportId, importId);
        List<ImportResultInfo> importResults = this.importResultInfoService.list(importInfoQw);
        String[] linData;
        for (ImportResultInfo result : importResults) {
            StaffBlacklist black = JSONObject.parseObject(result.getImportContent(), StaffBlacklist.class);
            linData = new String[5];
            linData[0] = black.getUserName();
            linData[1] = black.getMobile();
            linData[2] = black.getIdentityCard();
            linData[3] = black.getReason();
            linData[4] = result.getErrorCause();
            data.add(linData);
        }
        return data;
    }
}
