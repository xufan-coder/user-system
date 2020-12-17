package com.zerody.user.service.impl;

import com.zerody.common.bean.DataResult;
import com.zerody.common.util.ResultCodeEnum;
import com.zerody.common.utils.FileUtil;
import com.zerody.user.check.CheckUser;
import com.zerody.user.dto.SysUserInfoPageDto;
import com.zerody.user.enums.DataRecordStatusEnum;
import com.zerody.user.mapper.SysUserInfoMapper;
import com.zerody.user.mapper.UnionRoleStaffMapper;
import com.zerody.user.pojo.SysLoginInfo;
import com.zerody.user.pojo.SysStaffInfo;
import com.zerody.user.pojo.SysUserInfo;
import com.zerody.user.service.SysLoginInfoService;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.SysUserInfoService;
import com.zerody.user.service.base.BaseService;
import com.zerody.user.util.IdCardUtil;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author PengQiang
 * @ClassName SysUserInfoServiceImpl
 * @DateTime 2020/12/16_17:12
 * @Deacription TODO
 */
@Slf4j
@Service
public class SysUserInfoServiceImpl extends BaseService<SysUserInfoMapper, SysUserInfo> implements SysUserInfoService {

    @Autowired
    private SysUserInfoMapper sysUserInfoMapper;

    @Autowired
    private SysLoginInfoService sysLoginInfoService;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    @Autowired
    private UnionRoleStaffMapper unionRoleStaffMapper;


    private boolean checkPhone(String phone){
        if(StringUtils.isBlank(phone)){
            return false;
        }
        String regex = "^(1[3-9]\\d{9}$)";
        if (phone.trim().length() == 11) {
            return false;
        }
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            return false;
        }

        return true;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataResult addUser(SysUserInfo userInfo) {
        DataResult  dataResult = CheckUser.checkParam(userInfo);
        //如果校验不通过提示前端
        if(!dataResult.isIsSuccess()){
            return dataResult;
        }
        //通过校验 把状态设为正常使用状态
        userInfo.setStatus(DataRecordStatusEnum.INVALID.getCode());
        //查看手机号或登录名是否被占用
        List<SysUserInfo> users = sysUserInfoMapper.selectUserByPhoneOrLogName(userInfo);
        dataResult = new DataResult(ResultCodeEnum.RESULT_ERROR,true,"操作成功",null);
        if(users != null && users.size() > 0){
            dataResult.setMessage("该手机号或用户名称被占用");
            dataResult.setIsSuccess(!dataResult.isIsSuccess());
            return dataResult;
        }

        //效验通过保存用户信息
        userInfo.setRegisterTime(new Date());
        this.saveOrUpdate(userInfo);
        //用户信息保存添加登录信息
        SysLoginInfo logInfo = new SysLoginInfo();
        logInfo.setUserId(userInfo.getId());
        logInfo.setMobileNumber(userInfo.getPhoneNumber());
        logInfo.setNickname(userInfo.getNickname());
        logInfo.setAvatar(userInfo.getAvatar());
        logInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
        sysLoginInfoService.addLogin(logInfo);
        return dataResult;
    }

    @Override
    public DataResult updateUser(SysUserInfo userInfo) {
        this.saveOrUpdate(userInfo);
        return new DataResult();
    }

    @Override
    public DataResult deleteUserById(String userId) {
        this.removeById(userId);
        return new DataResult();
    }

    @Override
    public DataResult deleteUserBatchByIds(List<Integer> ids) {
        this.removeByIds(ids);
        return new DataResult();
    }

    @Override
    public DataResult selectUserPage(SysUserInfoPageDto sysUserInfoPageDto) {
        return null;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public DataResult batchImportUser(MultipartFile file) {
        //excel标题 用于导入失败提醒生成新的excel
        String[] titles = {"姓名","手机号","身份证号","入职日期（格式：yyyy-MM-dd）"};
        //必填项
        Integer[] indexs = {0,1,2,3};
        List<String[]> errors = new ArrayList<>();
        //结果返回
        Map<String, Object> result = new HashMap<>();
        String[] errorData = new String[titles.length];
        Integer headSize = 1; //从第几行开始读取数据
        Integer successCount = 0; //成功数量
        Integer total = 0; //数据总数
        try {
            List<String[]> dataList = FileUtil.fileImport(file);
            //判断当前导入excel是否有数据
            if(dataList.size()<=headSize){
                return new DataResult(ResultCodeEnum.RESULT_ERROR,false, "文件没有数据", result);
            }
            total = dataList.size() - headSize; //获得用户数
            result.put("total",total);
            String[] row = null;
            SysUserInfo userInfo ; //保存用户所用实体
            StringBuilder errorInfo = new StringBuilder("");
            //循环行
            for (int rowIndex = headSize,rowlength = dataList.size() ; rowIndex < rowlength; rowIndex++) {
                //这一行的数据
                row = dataList.get(rowIndex);
                userInfo = new SysUserInfo();
                for (int lineIndex = 0,lineLength = row.length; lineIndex<lineLength; lineIndex++){
                    if(row[lineIndex] == null || "".equals(row[lineIndex])){
                        continue;
                    }
                    row[lineIndex] = row[lineIndex].trim();//去掉空格
                }
                userInfo.setUserName(row[0]);
                userInfo.setPhoneNumber(row[1]);
                userInfo.setCertificateCard(row[2]);
                userInfo.setRegisterTime(new Date(row[3]));
                userInfo.setStatus(DataRecordStatusEnum.VALID.getCode());
                //验证手机
                if(checkPhone(userInfo.getPhoneNumber())){
                     errorInfo.append("手机号码错误,");
                }
                //验证身份证
                if(!IdCardUtil.isValidatedAllIdcard(userInfo.getCertificateCard())){
                    errorInfo.append("身份证错误,");
                }
                sysUserInfoMapper.selectUserByPhoneOrLogName(userInfo);
                if(errorInfo.length() > 0){ //如果有错误信息就下一次循环 不保存  并记录错误信息
                    System.arraycopy(row,0,errorData,0,row.length);
                    errorData[errorData.length - 1] = errorInfo.toString();
                    errors.add(errorData);
                    errorInfo = new StringBuilder("");//循环最后清空错误信息以方便记录下一次循环的错误信息
                    errorData = new String[errorData.length];
                    continue;
                }
                this.saveOrUpdate(userInfo); //因为员工表跟登录表都要用到用户所有先保存用户
                SysStaffInfo staff = new SysStaffInfo();
//                staff.setCompId(UserUtils.get); //获取当前登录用户的当前公司id
                staff.setUserName(userInfo.getUserName());
                staff.setUserId(userInfo.getId());
                staff.setStatus(DataRecordStatusEnum.VALID.getCode());
                sysStaffInfoService.addStaff(staff); //保存到员工表

                SysLoginInfo loginInfo = new SysLoginInfo();
                loginInfo.setMobileNumber(userInfo.getPhoneNumber());
                loginInfo.setUserId(userInfo.getId());
                sysLoginInfoService.addLogin(loginInfo);
            }

            if(errors.size()>0){

            }
        } catch (IOException e) {
            log.error("导入失败---:{}",e);
            e.printStackTrace();
        }
        return new DataResult(result);
    }

    @Override
    public DataResult deleteUserRole(String staffId, String roleId) {
        return null;
    }


}
