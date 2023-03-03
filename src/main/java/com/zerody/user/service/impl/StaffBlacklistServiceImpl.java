package com.zerody.user.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Maps;
import com.zerody.common.constant.MQ;
import com.zerody.common.constant.YesNo;
import com.zerody.common.enums.StatusEnum;
import com.zerody.common.enums.user.StaffBlacklistApproveState;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.mq.RabbitMqService;
import com.zerody.common.util.UUIDutils;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.CollectionUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.FileUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.log.api.constant.DataCodeType;
import com.zerody.user.api.dto.mq.StaffDimissionInfo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.constant.ImageTypeInfo;
import com.zerody.user.constant.ImportResultInfoType;
import com.zerody.user.domain.*;
import com.zerody.user.dto.FrameworkBlacListQueryPageDto;
import com.zerody.user.dto.InternalControlDto;
import com.zerody.user.dto.MobileAndIdentityCardDto;
import com.zerody.user.dto.StaffBlacklistAddDto;
import com.zerody.user.enums.ImportStateEnum;
import com.zerody.user.feign.OauthFeignService;
import com.zerody.user.handler.blacklist.BlacklistParamHandle;
import com.zerody.user.handler.user.SysUserDimissionHandle;
import com.zerody.user.mapper.CeoCompanyRefMapper;
import com.zerody.user.mapper.StaffBlacklistMapper;
import com.zerody.user.service.*;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.util.DistinctByProperty;
import com.zerody.user.util.IdCardUtil;
import com.zerody.user.util.UserLogUtil;
import com.zerody.user.vo.*;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author PengQiang
 * @ClassName StaffBlacklistServiceImpl
 * @DateTime 2021/8/4_9:26
 * @Deacription TODO
 */
@Slf4j
@Service
public class StaffBlacklistServiceImpl extends ServiceImpl<StaffBlacklistMapper, StaffBlacklist> implements StaffBlacklistService {

    private final static String[] BLACKLIST_IMOPRT_TITLE = {"*姓名", "*手机号", "*身份证号码", "*所属企业", "*加入原因"};

    @Autowired
    private ImageService imageService;

    @Autowired
    private SysStaffInfoService staffInfoService;

    @Autowired
    private SysUserInfoService userInfoService;

    @Autowired
    private ImportResultInfoService importResultInfoService;

    @Autowired
    private SysCompanyInfoService sysCompanyInfoService;

    @Autowired
    private CheckUtil checkUtil;

    @Autowired
    private RabbitMqService mqService;

    @Autowired
    private ImportInfoService importInfoService;

    @Autowired
    private CeoCompanyRefService ceoCompanyRefService;

    @Autowired
    private CeoCompanyRefMapper ceoCompanyRefMapper;

    @Autowired
    private OauthFeignService oauthFeignService;

    @Autowired
    private AppUserPushService appUserPushService;

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
        QueryWrapper<SysStaffInfo> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(SysStaffInfo::getUserId,param.getBlacklist().getUserId());

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
    public String doBlacklistExternalImport(MultipartFile file, UserVo user) throws IOException {
        List<String[]> dataList = null;
        try {
            dataList = FileUtil.fileImport(file);
        } catch (IOException e) {
            throw new DefaultException("导入内控用户出错，请按照要求的导入模板格式上传");
        }
        if (CollectionUtils.isEmpty(dataList) || Arrays.equals(dataList.get(0), BLACKLIST_IMOPRT_TITLE)) {
            throw new DefaultException("您上传的文件中表头不匹配系统最新要求的表头字段，请下载最新模板核对表头并按照要求填写！");
        }
        int titleRow = 1;
        if (dataList.size() == titleRow) {
            throw new DefaultException("导入的模板为空，没有数据！");
        }
        ImportInfo importInfo = new ImportInfo();
        importInfo.setImportState(ImportStateEnum.UNDERWAY.name());
        if (user.isBackAdmin()) {
            importInfo.setCompanyName("后台管理员");
        } else {
            StaffInfoVo staff = this.staffInfoService.getStaffInfo(user.getUserId());
            importInfo.setCompanyName(staff.getCompanyName());
            importInfo.setCompanyId(user.getCompanyId());
        }
        importInfo.setUserId(user.getUserId());
        importInfo.setUserName(user.getUserName());
        importInfo.setImportFileName(file.getOriginalFilename());
        importInfo.setImportType(ImportResultInfoType.STAFF_BLACK_EXTERNAL);
        importInfo.setCreateTime(new Date());
        importInfo.setExcelRows(0);
        importInfo.setSuccessNum(0);
        importInfo.setErrorNum(0);
        this.importInfoService.save(importInfo);
        List<String[]> finalDataList = dataList;
        new Thread(() -> {
            try {
                int saveRow = 500;

                for (int i = titleRow, size = finalDataList.size(); i < size; i += saveRow) {
                    if (saveRow + i >= size ) {
                        saveRow = size - i;
                    }
                    //校验以及保存伙伴内控名单
                    Map<String, Integer> result = this.doBlacklistImport(finalDataList.subList(i, i + saveRow), user, importInfo);
                    importInfo.setExcelRows(importInfo.getExcelRows() + result.get("excelRows"));
                    importInfo.setSuccessNum(importInfo.getSuccessNum() + result.get("successNum"));
                    importInfo.setErrorNum(importInfo.getErrorNum() + result.get("errorNum"));
                }
            } catch (Exception e) {
                log.error("内控名单导入出错#错误信息：{}", e, e);
            } finally {
                importInfo.setImportState(ImportStateEnum.ACCOMPLISH.name());
                this.importInfoService.updateById(importInfo);
            }
        }).start();
        return importInfo.getId();
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

    @Override
    public List<BlackListCount> getBlacklistCount() {
        List<BlackListCount> result=new ArrayList<>();
        List<SysComapnyInfoVo> sysCompanyAll = this.sysCompanyInfoService.getCompanyAll(null);
        QueryWrapper<StaffBlacklist> qw = new QueryWrapper<StaffBlacklist>();
        qw.select("company_id,count(1) as number").eq("state","BLOCK")
                .groupBy("company_id");
        List<Map<String, Object>> maps = this.listMaps(qw);
        maps = maps.stream().filter(l -> l.get("company_id")!=null).collect(Collectors.toList());

        for (SysComapnyInfoVo sysComapnyInfoVo : sysCompanyAll) {
            Map<String, Object> map = maps.stream().filter(l -> l.get("company_id").toString().equals(sysComapnyInfoVo.getId())).findFirst().orElse(null);
            int number=0;
            if(DataUtil.isNotEmpty(map)){
                number=map.get("number")==null?0:Integer.parseInt(map.get("number").toString());
            }
            result.add(new BlackListCount(sysComapnyInfoVo.getCompanyName(),
                        sysComapnyInfoVo.getId()
                        ,number));
        }
        //降序 去重
        return  result.stream().sorted(Comparator.comparingInt(BlackListCount::getNumber).reversed())
                .filter(DistinctByProperty.distinctByKey(s -> s.getCompanyName()))
                .collect(Collectors.toList());
    }


    @Override
    public void updateStaffBlacklist(StaffBlacklist param) {
        if(DataUtil.isEmpty(param.getId())){
            throw new DefaultException("参数不正确!");
        }
        UpdateWrapper<StaffBlacklist> uw=new UpdateWrapper();
        uw.lambda().eq(StaffBlacklist::getId,param.getId());
        if(DataUtil.isNotEmpty(param.getCompanyId())){
            uw.lambda().set(StaffBlacklist::getCompanyId,param.getCompanyId());
        }
        if(DataUtil.isNotEmpty(param.getReason())){
            uw.lambda().set(StaffBlacklist::getReason,param.getReason());
        }
        uw.lambda().set(StaffBlacklist::getUpdateTime,new Date());
        boolean update = this.update(uw);
        if(!update){
            throw new DefaultException("ID参数不正确");
        }
        if(DataUtil.isNotEmpty(param.getImages())){
            List<String> images = param.getImages();
            List<Image> imageAdds = new ArrayList<>();
            Image image;
            for (String s : images) {
                image = new Image();
                image.setConnectId(param.getId());
                image.setId(UUIDutils.getUUID32());
                image.setImageType(ImageTypeInfo.STAFF_BLACKLIST);
                image.setImageUrl(s);
                image.setCreateTime(new Date());
                imageAdds.add(image);
            }
            QueryWrapper<Image> imageRemoveQw = new QueryWrapper<>();
            imageRemoveQw.lambda().eq(Image::getConnectId, param.getId());
            imageRemoveQw.lambda().eq(Image::getImageType, ImageTypeInfo.STAFF_BLACKLIST);
            this.imageService.addImages(imageRemoveQw, imageAdds);
        }

    }


    private Map<String, Integer> doBlacklistImport(List<String[]> dataList, UserVo user, ImportInfo imp) {
        Map<String, Integer> importResult = Maps.newHashMapWithExpectedSize(dataList.size()  - 1);
        StringBuilder errStr = null;
        StaffBlacklist entity = null;
        List<StaffBlacklist> entitys = new ArrayList<>();
        List<ImportResultInfo> errors = new ArrayList<>();
        Integer excelRows = 0;
        ImportResultInfo importInfo ;
        Map<String, String> mobiles = Maps.newHashMapWithExpectedSize(dataList.size()  - 1);
        Map<String, String> idCards = Maps.newHashMapWithExpectedSize(dataList.size()  - 1);
        Map<String, String> reasons = Maps.newHashMapWithExpectedSize(dataList.size()  - 1);
        Map<String, String> reasonsIdCard = Maps.newHashMapWithExpectedSize(dataList.size()  - 1);
        List<SysUserInfo> users = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < dataList.size(); rowIndex++) {
            String[] rowData = dataList.get(rowIndex);
            boolean rowEmpty = true;
            for (int lineIndex = 0; lineIndex < rowData.length; lineIndex++) {
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(rowData[lineIndex])) {
                    rowData[lineIndex] = rowData[lineIndex].trim();//去掉空格
                    //  去掉逗号
                    rowData[lineIndex] = org.apache.commons.lang3.StringUtils.join(rowData[lineIndex].split(","));
                    if(rowEmpty){
                        rowEmpty = !rowEmpty;
                    }
                }
            }
            if (rowEmpty) {
                continue;
            }
            excelRows++;
            try {
                errStr = new StringBuilder();
                entity = new StaffBlacklist();
                importInfo = new ImportResultInfo();
                if (DataUtil.isNotEmpty(mobiles.get(rowData[1]))) {
                    errStr.append("表格中重复手机号,");
                }
                if (DataUtil.isNotEmpty(idCards.get(rowData[2]))) {
                    errStr.append("表格中重复身份证号,");
                }
                mobiles.put(rowData[1], rowData[1]);
                idCards.put(rowData[2], rowData[2]);
                this.checkImportBlacklistParam(rowData, errStr, entity, user);
                if (errStr.length() > 0) {
                    importInfo.setImportId(imp.getId());
                    importInfo.setCreateTime(entity.getCreateTime());
                    importInfo.setErrorCause(errStr.toString());
                    importInfo.setUserId(user.getUserId());
                    importInfo.setImportContent(JSON.toJSONString(entity));
                    importInfo.setType(ImportResultInfoType.STAFF_BLACK_EXTERNAL);
                    importInfo.setState(YesNo.YES);
                    errors.add(importInfo);
                    continue;
                }
                reasons.put(entity.getMobile(), entity.getReason());
                reasonsIdCard.put(entity.getIdentityCard(), entity.getReason());
                //查询出该手机号码或身份证的历史账号
                QueryWrapper<SysUserInfo> usersQw = new QueryWrapper<>();
                usersQw.lambda().and(qw -> qw.eq(SysUserInfo::getPhoneNumber, rowData[1]).or().eq(SysUserInfo::getCertificateCard, rowData[2]));
                List<SysUserInfo> datas = this.userInfoService.list(usersQw);
                if (DataUtil.isNotEmpty(datas)) {
                    users.addAll(datas);
                    continue;
                }
                entitys.add(entity);
            } catch (Exception e) {
                log.error("内控名单导入出错：{}",e ,e);
            }
        }
        List<String> userIds = new ArrayList<>();
        List<SysUserInfo> userUpdate = new ArrayList<>();
        List<SysStaffInfo> staffInfos = new ArrayList<>();
        Map<String, String> mobileMap = new HashMap<>();
        Map<String, String> idCradMap = new HashMap<>();
        if (DataUtil.isNotEmpty(users)) {
            users.forEach(u -> {
                //手机号码、身份证已添加加 就不在添加
                if (DataUtil.isEmpty(mobileMap.get(u.getPhoneNumber())) && DataUtil.isEmpty(idCradMap.get(u.getCertificateCard()))) {
                    StaffInfoVo userInfo = this.staffInfoService.getStaffInfo(u.getId());
                    //添加为内部内控名单
                    StaffBlacklist entity2 = BlacklistParamHandle.insideStaffBlacklistParam(userInfo, user);
                    entity2.setReason(reasons.get(entity2.getMobile()));
                    if (DataUtil.isEmpty(entity2.getReason())) {
                        entity2.setReason(reasonsIdCard.get(entity2.getIdentityCard()));
                    }
                    mobileMap.put(entity2.getMobile(), entity2.getMobile());
                    idCradMap.put(entity2.getIdentityCard(), entity2.getIdentityCard());
                    entitys.add(entity2);
                }
                // 如果是在职状态 设置为离职(已解约)
                if (u.getStatus() == StatusEnum.stop.getValue()) {
                    return;
                }
                //设置为离职
                u.setStatus(StatusEnum.stop.getValue());
                userUpdate.add(u);
                //查询员工信息
                QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
                staffQw.lambda().eq(SysStaffInfo::getUserId, u.getId());
                staffQw.lambda().last("limit 0,1");
                SysStaffInfo staff = this.staffInfoService.getOne(staffQw);
                if (DataUtil.isEmpty(staff)) {
                    return;
                }
                //设置离职原因
                staff.setDateLeft(new Date());
                staff.setStatus(StatusEnum.stop.getValue());
                staff.setLeaveReason(reasons.get(u.getPhoneNumber()));
                staffInfos.add(staff);
                userIds.add(u.getId());
                //发送消息 离职转移客户
                StaffDimissionInfo staffDimissionInfo = new StaffDimissionInfo();
                staffDimissionInfo.setUserId(u.getId());
                staffDimissionInfo.setOperationUserId(user.getUserId());
                staffDimissionInfo.setOperationUserName(user.getUserName());
                this.mqService.send(staffDimissionInfo, MQ.QUEUE_STAFF_DIMISSION);
                appUserPushService.updateById(SysUserDimissionHandle.staffDimissionPush(u.getId()));
            });
        }
        //修改用户状态
        if (DataUtil.isNotEmpty(userUpdate)) {
            this.userInfoService.updateBatchById(userUpdate);
        }
        if (DataUtil.isNotEmpty(staffInfos)) {
            this.staffInfoService.updateBatchById(staffInfos);
        }
        //删除token
        if (DataUtil.isNotEmpty(userIds)) {
            this.oauthFeignService.removeToken(userIds);
        }
        this.saveBatch(entitys);
        this.importResultInfoService.saveBatch(errors);
        importResult.put("excelRows", excelRows);
        importResult.put("successNum", entitys.size());
        importResult.put("errorNum", errors.size());
        return importResult;
    }

    private void checkImportBlacklistParam(String[] data, StringBuilder errStr, StaffBlacklist entity, UserVo user) {
        if (StringUtils.isEmpty(data[0])) {
            errStr.append("姓名必填,");
        }
        if (StringUtils.isEmpty(data[1])) {
            errStr.append("手机号码必填,");
        } else {
            String regex = "^(1[3-9]\\d{9}$)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(data[1]);
            if (m.matches()) {
                QueryWrapper<StaffBlacklist> blacQw = new QueryWrapper<>();
                blacQw.lambda().eq(StaffBlacklist::getMobile, data[1]);
                blacQw.lambda().eq(StaffBlacklist::getState, StaffBlacklistApproveState.BLOCK.name());
                blacQw.lambda().last("limit 0,1");
                StaffBlacklist oldBlac = this.getOne(blacQw);
                if (DataUtil.isNotEmpty(oldBlac)) {
                    errStr.append("该手机号码已被拉黑,");
                }
            } else {
                errStr.append("手机号码不合法,");
            }
        }
        if (StringUtils.isEmpty(data[2])) {
            errStr.append("身份证号码必填,");
        } else {
            if (IdCardUtil.isValidatedAllIdcard(data[2])) {
                QueryWrapper<StaffBlacklist> blacQw = new QueryWrapper<>();
                blacQw.lambda().eq(StaffBlacklist::getIdentityCard, data[2]);
                blacQw.lambda().eq(StaffBlacklist::getState, StaffBlacklistApproveState.BLOCK.name());
                blacQw.lambda().last("limit 0,1");
                StaffBlacklist oldBlac = this.getOne(blacQw);
                if (DataUtil.isNotEmpty(oldBlac)) {
                    errStr.append("该身份证号码已被拉黑,");
                }
            } else {
                errStr.append("身份证号码不合法,");
            }
        }
        if (StringUtils.isEmpty(data[3])) {
            errStr.append("所属企业必填,");
        } else {
            QueryWrapper<SysCompanyInfo> companyQw = new QueryWrapper<>();
            companyQw.lambda().eq(SysCompanyInfo::getCompanyName, data[3]);
            companyQw.lambda().eq(SysCompanyInfo::getStatus, 0);
            companyQw.lambda().last("limit 0,1");
            SysCompanyInfo company = this.sysCompanyInfoService.getOne(companyQw);
            if (DataUtil.isEmpty(company)) {
                errStr.append("所属企业不存在,");
            } else {
                entity.setCompanyId(company.getId());
            }
        }
        if (StringUtils.isEmpty(data[4])) {
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
        entity.setCompanyName(data[index++]);
        entity.setReason(data[index++]);
        entity.setCreateTime(new Date());
        entity.setApprovalTime(entity.getCreateTime());
        entity.setState(String.valueOf(YesNo.NO));
        entity.setSubmitUserId(user.getUserId());
        entity.setSubmitUserName(user.getUserName());
        entity.setState(StaffBlacklistApproveState.BLOCK.name());
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
                        .eq(StringUtils.isNotEmpty( finalStaffInfo.getIdentityCard()), StaffBlacklist::getIdentityCard,
                                finalStaffInfo.getIdentityCard())
                        );
                blacQw.lambda().eq(StaffBlacklist::getUserId,finalStaffInfo.getUserId());
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
            blac.setIdentityCard(staff.getIdentityCard());
            blac.setCreateTime(new Date());
            blac.setApprovalTime(new Date());
            blac.setState(StaffBlacklistApproveState.BLOCK.name());
            blac.setId(UUIDutils.getUUID32());
            this.save(blac);

    //把员工设为离职
        QueryWrapper<SysStaffInfo> staffQw = new QueryWrapper<>();
        staffQw.lambda().eq(SysStaffInfo::getUserId, blac.getUserId());
        SysStaffInfo  staff1 = this.staffInfoService.getOne(staffQw);
        UpdateWrapper<SysUserInfo> userUw = new UpdateWrapper<>();
        userUw.lambda().set(SysUserInfo::getIsEdit, YesNo.YES);
        userUw.lambda().set(SysUserInfo::getStatus, StatusEnum.stop.getValue());
        userUw.lambda().eq(true, SysUserInfo::getId, blac.getUserId());
        this.userInfoService.update(userUw);
        staff1.setStatus(StatusEnum.stop.getValue());
        staff1.setLeaveReason(param.getBlacklist().getReason());
        staff1.setDateLeft(new Date());
        this.staffInfoService.updateById(staff1);
        this.checkUtil.removeUserToken(staff1.getUserId());
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
        StaffDimissionInfo staffDimissionInfo = new StaffDimissionInfo();
        staffDimissionInfo.setUserId(blac.getUserId());
        staffDimissionInfo.setOperationUserId(blac.getSubmitUserId());
        staffDimissionInfo.setOperationUserName(blac.getSubmitUserName());
        this.mqService.send(staffDimissionInfo, MQ.QUEUE_STAFF_DIMISSION);

        QueryWrapper<Image> imageRemoveQw = new QueryWrapper<>();
        imageRemoveQw.lambda().eq(Image::getConnectId, blac.getId());
        imageRemoveQw.lambda().eq(Image::getImageType, ImageTypeInfo.STAFF_BLACKLIST);
        this.imageService.addImages(imageRemoveQw, imageAdds);
        } else {
            this.updateById(blac);
        }

        appUserPushService.updateById(SysUserDimissionHandle.staffDimissionPush(blac.getUserId()));
        SysUserInfo userInfo = userInfoService.getUserById(blac.getUserId());
        UserLogUtil.addUserLog(userInfo, UserUtils.getUser(),"加入伙伴内控名单:原因["+blac.getReason()+"]", DataCodeType.PARTNER_LOCK);
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
        StaffBlacklist staffBlack = this.getById(id);
        SysUserInfo userInfo = this.userInfoService.getById(staffBlack.getUserId());
        UserLogUtil.addUserLog(userInfo,UserUtils.getUser(),"解除伙伴内控名单",DataCodeType.PARTNER_LOCK);
    }
    @Override
    public void doRelieve(String id,Integer state) {
        UpdateWrapper<StaffBlacklist> relieveUw = new UpdateWrapper<>();
        relieveUw.lambda().eq(StaffBlacklist::getId, id);
        relieveUw.lambda().set(StaffBlacklist::getIsApprove, state);
        relieveUw.lambda().set(StaffBlacklist::getUpdateTime, new Date());
        this.update(relieveUw);
    }

    @Override
    public void doRelieveByMobile(String mobile,Integer state,String relieveId) {
        UpdateWrapper<StaffBlacklist> relieveUw = new UpdateWrapper<>();
        relieveUw.lambda().eq(StaffBlacklist::getMobile, mobile);
        relieveUw.lambda().eq(StaffBlacklist::getRelieveId, relieveId);
        relieveUw.lambda().set(StaffBlacklist::getIsApprove, state);
        relieveUw.lambda().set(StaffBlacklist::getUpdateTime, new Date());
        this.update(relieveUw);
    }

    @Override
    public List<StaffBlacklist> updateRelieveByMobile(StaffBlacklist param) {
        QueryWrapper<StaffBlacklist> qw = new QueryWrapper<>();
        qw.lambda().eq(StaffBlacklist::getMobile, param.getMobile());
        qw.lambda().eq(StaffBlacklist::getState, StaffBlacklistApproveState.BLOCK.name());
        List<StaffBlacklist> list = this.list(qw);

        for (StaffBlacklist staffBlacklist : list) {
            if(DataUtil.isEmpty(staffBlacklist.getRelieveId())){
                staffBlacklist.setRelieveId(param.getRelieveId());
                staffBlacklist.setRelieveKey(param.getRelieveKey());
                staffBlacklist.setIsApprove(param.getIsApprove());
                staffBlacklist.setUpdateTime(new Date());
            }
        }
        this.updateBatchById(list);
        return list;
    }

    @Override
    public InternalControlVo updateInternalControl(InternalControlDto internalControlDto) {
        InternalControlVo vo = this.baseMapper.updateInternalControl(internalControlDto);
        return vo;
    }

    @Override
    public MobileBlacklistQueryVo getBlacklistByMobile(MobileAndIdentityCardDto dto) {
        MobileBlacklistQueryVo  result = new MobileBlacklistQueryVo();
        List<String> companys = this.baseMapper.getBlacklistByMobile(dto);
        result.setIsBlock(CollectionUtils.isNotEmpty(companys));
        result.setCompanyNames(companys);
        return result;
    }
}
