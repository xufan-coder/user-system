package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.FileUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.constant.ImportResultInfoType;
import com.zerody.user.domain.*;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.enums.StaffStatusEnum;
import com.zerody.user.mapper.StaffBlacklistMapper;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.service.*;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.FrameworkBlacListQueryPageVo;
import com.zerody.user.vo.MobileBlacklistQueryVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName StaffBlacklistServiceImpl
 * @DateTime 2021/8/4_9:26
 * @Deacription TODO
 */
@Slf4j
@Service
public class StaffBlacklistServiceImpl extends ServiceImpl<StaffBlacklistMapper, StaffBlacklist> implements StaffBlacklistService {

    private final static String[] BLACKLIST_IMOPRT_TITLE = {"*姓名", "*手机号", "*身份证号码", "*加入原因"};

    @Autowired
    private ImageService imageService;

    @Autowired
    private SysStaffInfoService staffInfoService;

    @Autowired
    private SysUserInfoService userInfoService;

    @Autowired
    private ImportResultInfoService importResultInfoService;

    @Autowired
    private CheckUtil checkUtil;

    @Override
    public void addStaffBlaklistJoin(StaffBlacklistAddDto param) {
        StaffBlacklist blac = param.getBlacklist();
        QueryWrapper<StaffBlacklist> blacQw = new QueryWrapper<>();
        blacQw.lambda().and(bl ->
                bl.eq(StaffBlacklist::getMobile, blac.getMobile())
                        .or()
                        .eq(StaffBlacklist::getIdentityCard, blac.getIdentityCard())
        );
        blacQw.lambda().eq(StaffBlacklist::getState, StaffBlacklistApproveState.BLOCK.name());
        blacQw.lambda().last("limit 0,1");
        StaffBlacklist oldBlac = this.getOne(blacQw);
        if (DataUtil.isNotEmpty(oldBlac)) {
            throw new DefaultException("该号码已被拉黑！无法不需重复添加");
        }
        blacQw.lambda().eq(StaffBlacklist::getType, 2);
        //外部内控名单验重 两者不并存
        if (DataUtil.isNotEmpty(oldBlac)) {
            throw new DefaultException("该员工已被拉黑！无法重复发起");
        }
        blac.setCreateTime(new Date());
        blac.setApprovalTime(new Date());
        blac.setState(StaffBlacklistApproveState.BLOCK.name());
        blac.setId(UUIDutils.getUUID32());
        this.save(blac);
        List<String> images = param.getImages();
        List<Image> imageAdds = new ArrayList<>();
        Image image;
        for (String s : images) {
            image = new Image();
            image.setConnectId(blac.getId());
            image.setId(UUIDutils.getUUID32());
            image.setImageType(ImageTypeInfo.STAFF_BLACKLIST);
            image.setImageUrl(s);
            image.setCreateTime(new Date());
            imageAdds.add(image);
        }
        QueryWrapper<Image> imageRemoveQw = new QueryWrapper<>();
        imageRemoveQw.lambda().eq(Image::getConnectId, blac.getId());
        imageRemoveQw.lambda().eq(Image::getImageType, ImageTypeInfo.STAFF_BLACKLIST);
        this.imageService.addImages(imageRemoveQw, imageAdds);
    }

    @Override
    public void doBlacklistExternalImport(MultipartFile file, UserVo user) throws IOException {
        List<String[]> dataList = FileUtil.fileImport(file);
        if (CollectionUtils.isEmpty(dataList) || Arrays.equals(dataList.get(0), BLACKLIST_IMOPRT_TITLE)) {
            throw new DefaultException("您上传的文件中表头不匹配系统最新要求的表头字段，请下载最新模板核对表头并按照要求填写！");
        }
        int titleRow = 1;
        if (dataList.size() == titleRow) {
            throw new DefaultException("导入的模板为空，没有数据！");
        }
        new Thread(() -> {
            int saveRow = 500;
            for (int i = titleRow, size = dataList.size(); i < size; i += saveRow) {
                if (saveRow + i >= size ) {
                    saveRow = size - i;
                }
                //校验以及保存伙伴内控名单
                this.doBlacklistImport(dataList.subList(i, saveRow), user);
            }
        }).start();
    }

    @Override
    public FrameworkBlacListQueryPageVo getInfoById(String id) {
        StaffBlacklist black = this.getById(id);
        FrameworkBlacListQueryPageVo result = new FrameworkBlacListQueryPageVo();
        if (DataUtil.isEmpty(black)) {
            return null;
        }
        BeanUtils.copyProperties(black, result);
        List<String> images = this.imageService.getListImages(id, ImageTypeInfo.STAFF_BLACKLIST);
        result.setImages(images);
        return result;
    }

    private void doBlacklistImport(List<String[]> dataList, UserVo user) {
        StringBuilder errStr = null;
        StaffBlacklist entity = null;
        List<StaffBlacklist> entitys = new ArrayList<>();
        List<ImportResultInfo> errors = new ArrayList<>();
        ImportResultInfo importInfo ;
        for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
            String[] rowData = dataList.get(rowIndex);
            boolean rowEmpty = true;
            for (int lineIndex = 0; lineIndex < rowData.length; lineIndex++) {
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(rowData[lineIndex])) {
                    rowEmpty = !rowEmpty;
                    rowData[lineIndex] = rowData[lineIndex].trim();//去掉空格
                    //  去掉逗号
                    rowData[lineIndex] = org.apache.commons.lang3.StringUtils.join(rowData[lineIndex].split(","));
                    if(rowEmpty){
                        rowEmpty = !rowEmpty;
                    }
                    continue;
                }
                if (rowEmpty) {
                    continue;
                }
                errStr = new StringBuilder();
                entity = new StaffBlacklist();
                importInfo = new ImportResultInfo();
                this.checkImportBlacklistParam(rowData, errStr, entity, user);
                if (errStr.length() > 0) {
                    importInfo.setCreateTime(entity.getCreateTime());
                    importInfo.setErrorCause(errStr.toString());
                    importInfo.setUserId(user.getUserId());
                    importInfo.setImportContent(JSON.toJSONString(entity));
                    importInfo.setType(ImportResultInfoType.STAFF_BLACK_EXTERNAL);
                    importInfo.setState(YesNo.YES);
                    errors.add(importInfo);
                    continue;
                }
                entitys.add(entity);
            }
        }
        this.saveBatch(entitys);
        this.importResultInfoService.saveBatch(errors);
    }

    private void checkImportBlacklistParam(String[] data, StringBuilder errStr, StaffBlacklist entity, UserVo user) {
        if (StringUtils.isEmpty(data[0])) {
            errStr.append("姓名必填,");
        }
        if (StringUtils.isEmpty(data[1])) {
            errStr.append("手机号码必填,");
        } else {
            QueryWrapper<StaffBlacklist> blacQw = new QueryWrapper<>();
            blacQw.lambda().eq(StaffBlacklist::getMobile, data[1]);
            blacQw.lambda().eq(StaffBlacklist::getState, StaffBlacklistApproveState.BLOCK.name());
            blacQw.lambda().last("limit 0,1");
            StaffBlacklist oldBlac = this.getOne(blacQw);
            if (DataUtil.isNotEmpty(oldBlac)) {
               errStr.append("该号码已被拉黑,");
            }
        }
        if (StringUtils.isEmpty(data[2])) {
            errStr.append("身份证号码必填,");
        } else {
            QueryWrapper<StaffBlacklist> blacQw = new QueryWrapper<>();
            blacQw.lambda().eq(StaffBlacklist::getIdentityCard, data[2]);
            blacQw.lambda().eq(StaffBlacklist::getState, StaffBlacklistApproveState.BLOCK.name());
            blacQw.lambda().last("limit 0,1");
            StaffBlacklist oldBlac = this.getOne(blacQw);
            if (DataUtil.isNotEmpty(oldBlac)) {
                errStr.append("该身份证号码已被拉黑,");
            }
        }
        if (StringUtils.isEmpty(data[3])) {
            errStr.append("加入原因必填,");
        }
        if (errStr.length() > 0) {
            errStr.substring(0, errStr.length() - 1);
        }
        int index = 0;
        entity.setId(UUIDutils.getUUID32());
        entity.setUserName(data[index++]);
        entity.setMobile(data[index++]);
        entity.setIdentityCard(data[index++]);
        entity.setReason(data[index++]);
        entity.setCreateTime(new Date());
        entity.setState(String.valueOf(YesNo.NO));
        entity.setSubmitUserId(user.getUserId());
        entity.setSubmitUserName(user.getUserName());
        entity.setType(2);
    }

    @Override
    public StaffBlacklistAddDto addStaffBlaklist(StaffBlacklistAddDto param) {
        StaffBlacklist blac = param.getBlacklist();
        if (StringUtils.isEmpty(blac.getId())) {
            QueryWrapper<StaffBlacklist> blacQw = new QueryWrapper<>();
            StaffInfoVo staff = this.staffInfoService.getStaffInfo(blac.getUserId());
            if(DataUtil.isNotEmpty(staff)){
                StaffInfoVo finalStaffInfo = staff;
                blacQw.lambda().and(bl ->
                        bl.eq(StaffBlacklist::getMobile, finalStaffInfo.getMobile())
                        .or()
                        .eq(StaffBlacklist::getIdentityCard, finalStaffInfo.getIdentityCard())
                        );
                blacQw.lambda().eq(StaffBlacklist::getCompanyId, staff.getCompanyId());
                blacQw.lambda().eq(StaffBlacklist::getState, StaffBlacklistApproveState.BLOCK.name());
                StaffBlacklist oldBlac = this.getOne(blacQw);
                //内部内控名单验重
                if (DataUtil.isNotEmpty(oldBlac)) {
                    throw new DefaultException("该员工已被拉黑！无法重复发起");
                }
                blacQw.lambda().eq(StaffBlacklist::getType, 2);
                //外部内控名单验重 两者不并存
                if (DataUtil.isNotEmpty(oldBlac)) {
                    throw new DefaultException("该员工已被拉黑！无法重复发起");
                }
            }
//            this.remove(blacQw);
            blac.setUserName(staff.getUserName());
            blac.setCompanyId(staff.getCompanyId());
            blac.setMobile(staff.getMobile());

            blac.setCreateTime(new Date());
            blac.setApprovalTime(new Date());
            blac.setState(StaffBlacklistApproveState.BLOCK.name());
            blac.setId(UUIDutils.getUUID32());
            this.save(blac);
        } else {
            this.updateById(blac);
        }
    //把员工设为离职
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getUserId, blac.getUserId());
        SysStaffInfo  staff = this.staffInfoService.getOne(staffQw);
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().set(SysUserInfo::getIsEdit, YesNo.YES);
        userUw.lambda().set(SysUserInfo::getStatus, StatusEnum.stop.getValue());
        userUw.lambda().eq(true, SysUserInfo::getId, blac.getUserId());
        this.userInfoService.update(userUw);
        staff.setStatus(StatusEnum.stop.getValue());
        staff.setLeaveReason(param.getBlacklist().getReason());
        this.staffInfoService.updateById(staff);
        this.checkUtil.removeUserToken(staff.getUserId());
        if (CollectionUtils.isEmpty(param.getImages())) {
            return param;
        }
        List<String> images = param.getImages();
        List<Image> imageAdds = new ArrayList<>();
        Image image;
        for (String s : images) {
            image = new Image();
            image.setConnectId(blac.getId());
            image.setId(UUIDutils.getUUID32());
            image.setImageType(ImageTypeInfo.STAFF_BLACKLIST);
            image.setImageUrl(s);
            image.setCreateTime(new Date());
            imageAdds.add(image);
        }
        QueryWrapper<Image> imageRemoveQw = new QueryWrapper<>();
        imageRemoveQw.lambda().eq(Image::getConnectId, blac.getId());
        imageRemoveQw.lambda().eq(Image::getImageType, ImageTypeInfo.STAFF_BLACKLIST);
        this.imageService.addImages(imageRemoveQw, imageAdds);
        return param;
    }

    @Override
    public IPage<FrameworkBlacListQueryPageVo> getPageBlackList(FrameworkBlacListQueryPageDto param) {
        IPage<FrameworkBlacListQueryPageVo> iPage = new Page<>(param.getCurrent(), param.getPageSize());
        iPage = this.baseMapper.getPageBlackList(param, iPage);
        return iPage;
    }

    @Override
    public void doRelieveByStaffId(String id) {
        UpdateWrapper<StaffBlacklist> relieveUw = new UpdateWrapper<>();
        relieveUw.lambda().eq(StaffBlacklist::getId, id);
        relieveUw.lambda().set(StaffBlacklist::getState, StaffBlacklistApproveState.RELIEVE.name());
        relieveUw.lambda().set(StaffBlacklist::getUpdateTime, new Date());
        this.update(relieveUw);
//        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
//        userUw.lambda().set(SysUserInfo::getIsEdit, YesNo.YES);
//        userUw.lambda().set(SysUserInfo::getStatus, StatusEnum.stop.getValue());
//        userUw.lambda().inSql(true, SysUserInfo::getId,
//                "SELECT ssi.user_id FROM sys_staff_info AS ssi  WHERE ssi.id = '".concat(staffId).concat("'")
//        );
//        this.userInfoService.update(userUw);
    }

    @Override
    public MobileBlacklistQueryVo getBlacklistByMobile(String mobile) {
        MobileBlacklistQueryVo  result = new MobileBlacklistQueryVo();
        List<String> companys = this.baseMapper.getBlacklistByMobile(mobile);
        result.setIsBlock(CollectionUtils.isNotEmpty(companys));
        result.setCompanyNames(companys);
        return result;
    }
}
