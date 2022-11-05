package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.ExcelToolUtil;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.user.constant.ImportResultInfoType;
import com.zerody.user.domain.ImportResultInfo;
import com.zerody.user.domain.StaffBlacklist;
import com.zerody.user.dto.ImportInfoQueryDto;
import com.zerody.user.dto.UserImportErrorDataDto;
import com.zerody.user.service.ImportResultInfoService;
import com.zerody.user.vo.ImportInfoQueryVo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.zerody.user.domain.ImportInfo;
import com.zerody.user.mapper.ImportInfoMapper;
import com.zerody.user.service.ImportInfoService;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *@ClassName ImportInfoServiceImpl
 *@author    PengQiang
 *@DateTime  2021/12/14_15:50
 *@Deacription TODO
 */
@Service
public class ImportInfoServiceImpl extends ServiceImpl<ImportInfoMapper, ImportInfo> implements ImportInfoService{



    @Value("${import.deleted.day}")
    private Long deletedDay;


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
        List<String[]> data = null;
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
            case ImportResultInfoType.STAFF_EXTERNAL_VICE:
                if (DataUtil.isNotEmpty(info.getCompanyId())) {
                    header = Arrays.copyOf(SysStaffInfoServiceImpl.STAFF_EXCEL_TITTLE, SysStaffInfoServiceImpl.STAFF_EXCEL_TITTLE.length + 1);
                    requiredNum = new Integer[]{0, 1, 4, 5, 13};
                } else {
                    header = Arrays.copyOf(SysStaffInfoServiceImpl.COMPANY_STAFF_EXCEL_TITTLE, SysStaffInfoServiceImpl.COMPANY_STAFF_EXCEL_TITTLE.length + 1);
                    requiredNum = new Integer[]{0, 1, 2, 5, 6, 14};
                }
                header[header.length - 1] = "导入失败原因";
                data = this.getStaffoseRecord(id, info.getCompanyId());
                fileName = "导入员工失败记录_" + System.currentTimeMillis();
                break;
        }

        HSSFWorkbook workbook = ExcelToolUtil.createExcel(header, data, requiredNum);
//        response.getContentType("octets/stream");
        response.addHeader("Content-Disposition", "attachment;filename="+new String( fileName.getBytes("gb2312"), "ISO8859-1" )+".xls");
        OutputStream os = response.getOutputStream();
        workbook.write(os);
        os.close();
    }

    @Override
    public void deletedImportTiming() {
        LocalDate now = LocalDate.now();
        Date delTime = Date.from(now.atStartOfDay().minusDays(this.deletedDay).toInstant(ZoneOffset.of("+8")));
        List<String> importIds = this.baseMapper.getImportIdByTime(delTime);
        QueryWrapper<ImportInfo> delQw = new QueryWrapper<>();
        delQw.lambda().lt(ImportInfo::getCreateTime, delTime);
        this.baseMapper.delete(delQw);
        QueryWrapper<ImportResultInfo> resultDelQw = new QueryWrapper<>();
        resultDelQw.lambda().in(ImportResultInfo::getImportId, importIds);
        this.importResultInfoService.remove(resultDelQw);
    }

    private List<String[]> getStaffBlacklistLoseRecord(String importId) {
        List<String[]> data = new ArrayList<>();
        QueryWrapper<ImportResultInfo> importInfoQw = new QueryWrapper<>();
        importInfoQw.lambda().eq(ImportResultInfo::getImportId, importId);
        importInfoQw.lambda().orderByDesc(ImportResultInfo::getCreateTime);
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

    private List<String[]> getStaffoseRecord(String importId, String companyId) {
        List<String[]> result = new ArrayList<>();
        QueryWrapper<ImportResultInfo> importInfoQw = new QueryWrapper<>();
        importInfoQw.lambda().eq(ImportResultInfo::getImportId, importId);
        importInfoQw.lambda().orderByDesc(ImportResultInfo::getCreateTime);
        List<ImportResultInfo> importResults = this.importResultInfoService.list(importInfoQw);
        String[] data;
        for (ImportResultInfo ir : importResults) {
            UserImportErrorDataDto bean = JSONObject.parseObject(ir.getImportContent(), UserImportErrorDataDto.class);
            int index = 0;
            if (StringUtils.isEmpty(companyId)) {
                data = new String[30];
            } else {
                data = new String[29];
            }
            data[index++] = bean.getName();
            data[index++] = bean.getMobile();
            if (StringUtils.isEmpty(companyId)) {
                data[index++] =   bean.getCompanyName();
            }
            data[index++] = bean.getDepartName();
            data[index++] = bean.getJobName();
            data[index++] =  bean.getRoleName();
            data[index++] =  bean.getDateJoin();
            data[index++] = bean.getRecommendMobile();
            data[index++] = bean.getStatus();
            data[index++] =  bean.getGender();
            data[index++] = bean.getAncestral();
            data[index++] =  bean.getNation();
            data[index++] =  bean.getMarital();
            data[index++] =  bean.getBirthday();
            data[index++] = bean.getIdCard();
            data[index++] =  bean.getCertificateCardAddress();
            data[index++] =  bean.getContactAddress();
            data[index++] = bean.getEmail();
            data[index++] = bean.getHighestEducation();
            data[index++] = bean.getGraduatedFrom();
            data[index++] = bean.getMajor();
            data[index++] = bean.getUrgentName();
            data[index++] = bean.getUrgentPhone();
            data[index++] = bean.getUrgentRelation();
            data[index++] = bean.getFamilyName();
            data[index++] = bean.getFamilyMobile();
            data[index++] = bean.getRelationship();
            data[index++] = bean.getProfession();
            data[index++] = bean.getFamilyContactAddress();
            data[index++] = ir.getErrorCause();
            result.add(data);
        }
        return result;
    }
}
