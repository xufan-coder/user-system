package com.zerody.user.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.enums.UserTypeEnum;
import com.zerody.common.exception.DefaultException;
import com.zerody.common.util.UserUtils;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.vo.UserVo;
import com.zerody.user.api.vo.AdminVo;
import com.zerody.user.api.vo.StaffInfoVo;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.IdCardUpdateDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.enums.TemplateTypeEnum;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.service.base.CheckUtil;
import com.zerody.user.vo.*;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.StaffInfoByCompanyVo;
import com.zerody.user.vo.SysUserInfoVo;
import io.micrometer.core.instrument.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * @author PengQiang
 * @ClassName SysStaffInfoController
 * @DateTime 2020/12/17_19:26
 * @Deacription TODO
 */
@Slf4j
@RestController
@RequestMapping("/staff-info")
public class SysStaffInfoController {


    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private CheckUtil checkUtil;

    @Autowired
    private SysStaffInfoService sysStaffInfoService;

    /**
    *   分页查询员工信息
    */
    @RequestMapping(value = "/page/get", method = RequestMethod.GET)
    public DataResult<IPage<BosStaffInfoVo>> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto){
        //增加后台账户过滤
        List<String> list = null;
        if (UserUtils.getUser().isBack()) {
            list = this.checkUtil.setBackCompany(UserUtils.getUserId());
        }
        sysStaffInfoPageDto.setCompanyIds(list);
        return R.success(sysStaffInfoService.getPageAllStaff(sysStaffInfoPageDto));
    }

    @RequestMapping(value = "/page/get/inner", method = RequestMethod.POST)
    public DataResult<IPage<BosStaffInfoVo>> getPageAllStaffInner(@RequestBody SysStaffInfoPageDto sysStaffInfoPageDto){
        return R.success(sysStaffInfoService.getPageAllStaff(sysStaffInfoPageDto));
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          查询在职员工
     * @date                 2021/7/21 9:41
     * @param                sysStaffInfoPageDto
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BosStaffInfoVo>>
     */
    @RequestMapping(value = "/page/get/active-duty", method = RequestMethod.GET)
    public DataResult<IPage<BosStaffInfoVo>> getPageAllActiveDutyStaff(SysStaffInfoPageDto sysStaffInfoPageDto){
        if ("lower".equals(sysStaffInfoPageDto.getQueryType())) {
            this.checkUtil.SetUserPositionInfo(sysStaffInfoPageDto);
        }else {
            if (UserUtils.getUser().isBack()){
                List<String> list =  this.checkUtil.setBackCompany(UserUtils.getUserId());
                sysStaffInfoPageDto.setCompanyIds(list);
            }
            if (UserUtils.getUser().getUserType().equals(UserTypeEnum.CRM_CEO.getValue())){
                List<String> list =  this.checkUtil.setCeoCompany(UserUtils.getUserId());
                sysStaffInfoPageDto.setCompanyIds(list);
            }
        }
        return R.success(sysStaffInfoService.getPageAllActiveDutyStaff(sysStaffInfoPageDto));
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          查询在职员工
     * @date                 2021/7/21 9:41/get/page/performance-reviews/collect
     * @param                sysStaffInfoPageDto
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BosStaffInfoVo>>
     */
    @RequestMapping(value = "/page/get/active-duty-depart", method = RequestMethod.GET)
    public DataResult<IPage<BosStaffInfoVo>> getPageAllActiveDutyDepartStaff(SysStaffInfoPageDto sysStaffInfoPageDto){
        if ("lower".equals(sysStaffInfoPageDto.getQueryType())) {
            this.checkUtil.SetUserPositionInfo(sysStaffInfoPageDto);
        }
        if(StringUtils.isNotEmpty(sysStaffInfoPageDto.getUserId())){
            sysStaffInfoPageDto.setUserId(null);
        }
        return R.success(sysStaffInfoService.getPageAllActiveDutyStaff(sysStaffInfoPageDto));
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          分页查询上级部门员工
     * @date                 2021/2/22 16:13
     * @param                [sysStaffInfoPageDto]
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BosStaffInfoVo>>
     */
    @RequestMapping(value = "/superior/page/get", method = RequestMethod.GET)
    public DataResult<IPage<BosStaffInfoVo>> getPageAllSuperiorStaff(SysStaffInfoPageDto sysStaffInfoPageDto){
        return R.success(sysStaffInfoService.getPageAllSuperiorStaff(sysStaffInfoPageDto));
    }


    /**
    *   添加员工
    */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public DataResult<Object> addStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){
        try {
            sysStaffInfoService.addStaff(setSysUserInfoDto);
            return R.success();
        } catch (DefaultException e){
            log.error("添加伙伴错误:{}" + JSON.toJSONString(setSysUserInfoDto), e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("添加伙伴错误:{} "+ JSON.toJSONString(setSysUserInfoDto), e);
            return R.error("添加合作伙伴失败,请求异常");
        }
    }


    //修改员工状态
    @RequestMapping(value = "/loginStatus/{id}/{status}", method =  RequestMethod.PUT)
    public DataResult<Object> updateStaffStatus(@PathVariable(name = "id") String userId, @PathVariable(name = "status") Integer status){
        try {
            sysStaffInfoService.updateStaffStatus(userId, status, null, UserUtils.getUser());
            return R.success();
        } catch (DefaultException e){
            log.error("修改员工状态错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("修改员工状态错误:{}",e.getMessage());
            return R.error("修改员工状态失败,请求异常");
        }
    }

    /**
    *    编辑员工
    */
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public DataResult<Object> updateStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){
        try {
            UserVo user = UserUtils.getUser();
            boolean isTraverse=true;
            sysStaffInfoService.updateStaff(setSysUserInfoDto,user,isTraverse);
            return R.success();
        } catch (DefaultException e){
            log.error("修改员工信息错误:{}", JSON.toJSONString(setSysUserInfoDto), e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("修改员工信息错误:{}", JSON.toJSONString(setSysUserInfoDto), e);
            return R.error("修改员工信息失败,请求异常");
        }
    }

    /**
    *   APP上传修改身份证照片
    */
    @PutMapping("/id-card/update")
    public DataResult<Object> updateIdCard(@Validated @RequestBody IdCardUpdateDto dto){
        try {
            sysStaffInfoService.updateIdCard(dto);
            return R.success();
        } catch (DefaultException e){
            log.error("app上传身份证错误:{}" , e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("app上传身份证错误:{} ", e);
            return R.error("上传身份证错误,请求异常");
        }
    }


    /**
     * @Author: chenKeFeng
     * @param
     * @Description: app添加伙伴
     * @Date: 2022/11/28 23:58
     */
    @PostMapping("/app-add")
    public DataResult<Object> addAppStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){
        try {
            sysStaffInfoService.addStaff(setSysUserInfoDto);
            return R.success();
        } catch (DefaultException e){
            log.error("app添加伙伴错误:{}" + JSON.toJSONString(setSysUserInfoDto), e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("app添加伙伴错误:{} "+ JSON.toJSONString(setSysUserInfoDto), e);
            return R.error("添加合作伙伴失败,请求异常");
        }
    }

    /**
    * @Author: chenKeFeng
    * @param
    * @Description: app编辑伙伴
    * @Date: 2022/11/28 23:59
    */
    @PutMapping("/app-update")
    public DataResult<Object> updateAppStaff(@Validated @RequestBody SetSysUserInfoDto setSysUserInfoDto){
        log.info("app编辑伙伴入参: {}", JSON.toJSONString(setSysUserInfoDto));
        try {
            UserVo user = UserUtils.getUser();
            boolean isTraverse=false;
            sysStaffInfoService.updateStaff(setSysUserInfoDto,user,isTraverse);
            return R.success();
        } catch (DefaultException e){
            log.error("修改员工信息错误:{}", JSON.toJSONString(setSysUserInfoDto), e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("修改员工信息错误:{}", JSON.toJSONString(setSysUserInfoDto), e);
            return R.error("修改员工信息失败,请求异常");
        }
    }


    /**
    *  根据员工id查询员工详情信息
    */
    @GetMapping("/get/{id}")
    public DataResult<SysUserInfoVo> selectStaffById(@PathVariable(name = "id") String staffId){
        try {
            UserVo userVo = UserUtils.getUser();
            return R.success(sysStaffInfoService.selectStaffById(staffId,true,userVo));
        } catch (DefaultException e){
            log.error("根据员工id查询员工信息:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("根据员工id查询员工信息:{}", e, e);
            return R.error("根据员工id查询员工信息,请求异常");
        }
    }


    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 获取app伙伴详情
    * @Date: 2022/11/26 11:08
    */
    @GetMapping("/get-app/{id}")
    public DataResult<SysUserInfoVo> queryStaffById(@PathVariable String id){
        try {
            return R.success(sysStaffInfoService.selectStaffById(id,true,UserUtils.getUser()));
        } catch (DefaultException e){
            log.error("获取app伙伴详情错误:{}", e.getMessage());
            return R.error(e.getMessage());
        } catch (Exception e) {
            log.error("获取app伙伴详情错误:{}", e, e);
            return R.error("获取app伙伴详情,请求异常");
        }
    }


    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 获取企业下的员工
    * @Date: 2022/11/28 9:40
    */
    @GetMapping("/get-app-staff")
    public DataResult<List<AppUserVo>> querySysStaffInfoList(String compId) {
        try {
            UserVo user = UserUtils.getUser();
            if (DataUtil.isEmpty(compId)) {
                compId = user.getCompanyId();
            }
            //获取该企业下的员工
            return R.success(this.sysStaffInfoService.queryCompStaff(compId));
        } catch (DefaultException e) {
            log.error("获取下属员工出错:{}", e.getMessage());
            return R.error( e.getMessage());
        } catch (Exception e) {
            log.error("获取下属员工出错:{}", e, e);
            return R.error("获取下属员工出错");
        }
    }


    /**
     * 根据用户id查询员工信息
     *
     * @param userId 用户id
     * @return
     */
    @GetMapping("/get-by-user")
    public DataResult<SysUserInfoVo> getInfoByUserId(@RequestParam(value = "userId")String userId){
        log.info("根据用户id查询员工信息入参 {}", userId);
        try {
            return R.success(sysStaffInfoService.selectStaffByUserId(userId,UserUtils.getUser(),true));
        } catch (DefaultException e) {
            log.error("查询员工信息出错:{}", e.getMessage());
            return R.error("查询员工信息信息");
        } catch (Exception e) {
            log.error("查询员工信息出错:{}", e, e);
            return R.error("查询员工信息信息");
        }
    }

    /**
     * 根据用户id查询员工信息(内控操作记录)
     *
     * @param userId 用户id
     * @return
     */
    @GetMapping("/get-by-user/operate")
    public DataResult<SysUserInfoVo> getOperateInfoByUserId(@RequestParam(value = "userId")String userId){
        log.info("根据用户id查询员工信息入参 {}", userId);
        try {
            return R.success(sysStaffInfoService.selectStaffByUserId(userId,UserUtils.getUser(),false));
        } catch (DefaultException e) {
            log.error("查询员工信息出错(内控操作记录):{}", e.getMessage());
            return R.error("查询员工信息信息(内控操作记录)");
        } catch (Exception e) {
            log.error("查询员工信息出错(内控操作记录):{}", e, e);
            return R.error("查询员工信息信息(内控操作记录)");
        }
    }

    /**
    *  删除员工
    */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public DataResult<Object> deleteStaffById(@PathVariable(name = "id") String staffId){
        try {
            sysStaffInfoService.deleteStaffById(staffId);
            return R.success();
        } catch (DefaultException e){
            log.error("删除员工错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("删除员工错误:{}",e.getMessage());
            return R.error("删除员工失败,请求异常");
        }

    }

    /**
    *   下载模板
    */
    @GetMapping("/get-template")
    public void getTemplateUrl(Integer type) throws Exception {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletResponse response = attributes.getResponse();

        InputStream inputStream = null;
        ServletOutputStream servletOutputStream = null;
        try {
            String fileName = TemplateTypeEnum.getByCode(type).getUrl();

            String chinaeseName = TemplateTypeEnum.getByCode(type).getText() + "模板.xlsx";

            String path = "static/template" + "/" + fileName;

            org.springframework.core.io.Resource resource = resourceLoader.getResource("classpath:" + path);

            response.setContentType("application/vnd.ms-excel");
            response.addHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.addHeader("charset", "utf-8");
            response.addHeader("Pragma", "no-cache");
            String encodeName = URLEncoder.encode(chinaeseName, StandardCharsets.UTF_8.toString());
//            response.addHeader("content-disposition", encodeName);
            response.addHeader("Content-Disposition","inline;filename=" + new String(encodeName.getBytes(),StandardCharsets.UTF_8.toString()));

            inputStream = resource.getInputStream();
            servletOutputStream = response.getOutputStream();
            IOUtils.copy(inputStream, servletOutputStream);
            response.flushBuffer();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (servletOutputStream != null) {
                    servletOutputStream.close();
                    servletOutputStream = null;
                }
                if (inputStream != null) {
                    inputStream.close();
                    inputStream = null;
                }
                // 召唤jvm的垃圾回收器
                System.gc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 企业内部导入
    *    批量导入用户excel
    */
    @PostMapping("/import")
    public DataResult<Object> doBatchImportUser(MultipartFile file){
        try {
            return R.success(sysStaffInfoService.doBatchImportUser(file, UserUtils.getUser()));
        } catch (DefaultException e){
            log.error("批量导入员工错误:{}",e, e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("批量导入员工错误:{}", e, e);
            return R.error("批量导入员工失败,请求异常");
        }
    }

    /**
     *  非固定企业导入
     *    批量导入用户excel
     */
    @PostMapping("/company/import")
    public DataResult<Object> doBatchImportCompanyUser(MultipartFile file){
        try {
            return R.success(sysStaffInfoService.doBatchImportCompanyUser(file, UserUtils.getUser()));
        } catch (DefaultException e){
            log.error("批量导入员工错误:{}",e, e);
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("批量导入员工错误:{}",e, e);
            return R.error("批量导入员工失败,请求异常");
        }
    }

   /**
   *   按企业获取员工
    *   按部门获取员工
    *   按岗位企业员工
   */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    public DataResult<List<BosStaffInfoVo>> getStaff(@RequestParam(value = "companyId",required = false) String companyId,
                                                     @RequestParam(value = "departId",required = false) String departId,
                                                     @RequestParam(value = "positionId",required = false) String positionId){
        return R.success(sysStaffInfoService.getStaff(companyId,departId,positionId));
    }

    /**
     *   按企业获取员工
     */
    @RequestMapping(value = "/get/by-company", method = RequestMethod.GET)
    public DataResult<List<StaffInfoByCompanyVo>> getStaffByCompany(@RequestParam(value = "companyId",required = true)String companyId,
                                                                    @RequestParam(value = "isShowLeave", required = false) Integer isShowLeave){
        return R.success(sysStaffInfoService.getStaffByCompany(companyId, isShowLeave));
    }



    /**
    *   分页获取平台管理员列表
    */
    @RequestMapping(value = "/get-admins", method = RequestMethod.GET)
    public DataResult<IPage<BosStaffInfoVo>> getAdmins(AdminsPageDto dto){
        return R.success(sysStaffInfoService.getAdmins(dto));
    }


    /**
     *
     *
     * @author               PengQiang
     * @description          微信分页获取员工
     * @date                 2021/1/13 17:16
     * @param                [dto]
     * @return               com.zerody.common.api.bean.DataResult<com.baomidou.mybatisplus.core.metadata.IPage<com.zerody.user.vo.BosStaffInfoVo>>
     */
    @RequestMapping(value = "/wx/page", method = RequestMethod.GET)
    public DataResult<IPage<BosStaffInfoVo>> getWxPageAllStaff(SysStaffInfoPageDto dto){
        try {
            dto.setCompanyId(UserUtils.getUser().getCompanyId());
            return R.success(sysStaffInfoService.getWxPageAllStaff(dto));
        } catch (DefaultException e){
            log.error("微信分页获取员工出错:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("微信分页获取员工出错:{}",e.getMessage());
            return R.error("微信分页获取员工出错,请求异常");
        }
    }

    @RequestMapping(value = "/get-is-admin", method = RequestMethod.GET)
    public DataResult<AdminVo> getIsAdmin(){
        try {
            UserVo user = UserUtils.getUser();
            AdminVo admin = sysStaffInfoService.getIsAdmin(user);
            return R.success(admin);
        } catch (DefaultException e){
            log.error("获取管理员信息:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取管理员信息:{}",e.getMessage());
            return R.error("获取管理员信息,请求异常");
        }
    }

    @RequestMapping(value = "/get-is-admin/by-user-id/{id}", method = RequestMethod.GET)
    public DataResult<AdminVo> getIsAdminByUserId(@PathVariable("id") String id){
        try {
            UserVo user = new UserVo();
            StaffInfoVo staff = this.sysStaffInfoService.getStaffInfo(id);
            user.setUserId(id);
            user.setCompanyId(staff.getCompanyId());
            AdminVo admin = sysStaffInfoService.getIsAdmin(user);
            return R.success(admin);
        } catch (DefaultException e){
            log.error("获取管理员信息:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("获取管理员信息:{}",e.getMessage());
            return R.error("获取管理员信息,请求异常");
        }
    }




    /**
     *
     *
     * @author               PengQiang
     * @description          获取客户查询维度
     * @date                 2021/3/10 10:40
     * @param                []
     * @return               com.zerody.common.api.bean.DataResult<java.util.List<com.zerody.user.vo.SysComapnyInfoVo>>
     */
    @GetMapping("/get/customer-query-dimensionality")
    public DataResult<List<CustomerQueryDimensionalityVo>> getCustomerQuerydimensionality(){
        try {
            List<CustomerQueryDimensionalityVo> result = this.sysStaffInfoService.getCustomerQuerydimensionality(UserUtils.getUser());
            return R.success(result);
        } catch (DefaultException e){
            log.error("获取客户查询维度错误!", e , e);
            return R.error(e.getMessage());
        } catch (Exception e){
            log.error("获取客户查询维度错误!", e , e);
            return R.error(e.getMessage());
        }
    }

    /***
     * @description  查看员工详情
     * @author zhangpingping
     * @date 2021/9/11
     * @param [id]
     * @return
     */
    @GetMapping(value = "/get/staff-details-count/{id}")
    public DataResult<SysStaffInfoDetailsVo> getStaffDetailsCount(@PathVariable("id") String id){

        SysStaffInfoDetailsVo sysStaffInfoDetailsVo=this.sysStaffInfoService.getStaffDetailsCount(id);
        return R.success(sysStaffInfoDetailsVo);
    }



    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 伙伴数据统计(工作台)
    * @Date: 2022/11/11 9:51
    */
    @GetMapping("/statistics-users")
    public DataResult<UserStatistics> statisticsUsers(SetSysUserInfoDto userInfoDto) {
        try {
            return R.success(this.sysStaffInfoService.statisticsUsers(userInfoDto));
        } catch (Exception e) {
            log.error("伙伴数据统计出错:{}", e.getMessage());
            return R.error("伙伴数据统计出错" + e.getMessage());
        }
    }


    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 查询伙伴概况
    * @Date: 2023/4/28 17:17
    */
    @GetMapping("/get/user/overview")
    public DataResult<UserStatistics> getUserOverview() {
        try {
            return R.success(this.sysStaffInfoService.getUserOverview());
        } catch (DefaultException e) {
            log.error("查询伙伴概况出错:{}", e.getMessage());
            return R.error("查询伙伴概况出错");
        } catch (Exception e) {
            log.error("查询伙伴概况出错:{}", e, e);
            return R.error("查询伙伴概况出错");
        }
    }


    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 统计伙伴签约与解约
    * @Date: 2023/4/28 19:55
    */
    @GetMapping("/statistics/partner")
    public DataResult<UserStatistics> statisticsContractAndRescind() {
        try {
            return R.success(this.sysStaffInfoService.statisticsContractAndRescind());
        } catch (DefaultException e) {
            log.error("统计伙伴签约与解约出错:{}", e.getMessage());
            return R.error("统计伙伴签约与解约出错");
        } catch (Exception e) {
            log.error("统计伙伴签约与解约出错:{}", e, e);
            return R.error("统计伙伴签约与解约出错");
        }
    }


    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 统计伙伴签约详情
    * @Date: 2023/4/28 20:36
    */
    @GetMapping("/statistics/partner/details")
    public DataResult<StatisticsDataDetailsVo> statisticsDetails() {
        try {
            return R.success(this.sysStaffInfoService.statisticsDetails());
        } catch (DefaultException e) {
            log.error("统计伙伴签约详情出错:{}", e.getMessage());
            return R.error("统计伙伴签约详情出错");
        } catch (Exception e) {
            log.error("统计伙伴签约详情出错:{}", e, e);
            return R.error("统计伙伴签约详情出错");
        }
    }


    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 获取解约原因分析
    * @Date: 2023/4/29 10:59
    */
    @GetMapping("/termination/analysis")
    public DataResult<List<TerminationAnalysisVo>> getTerminationAnalysis() {
        try {
            return R.success(this.sysStaffInfoService.getTerminationAnalysis());
        } catch (DefaultException e) {
            log.error("获取解约原因分析出错:{}", e.getMessage());
            return R.error("获取解约原因分析出错");
        } catch (Exception e) {
            log.error("获取解约原因分析出错:{}", e, e);
            return R.error("获取解约原因分析出错");
        }
    }


    /**
    * @Author: chenKeFeng
    * @param
    * @Description: 获取学历分析
    * @Date: 2023/4/29 11:29
    */
    @GetMapping("/degree/analysis")
    public DataResult<DegreeAnalysisVo> getDegreeAnalysis() {
        try {
            return R.success(this.sysStaffInfoService.getDegreeAnalysis());
        } catch (DefaultException e) {
            log.error("获取学历分析出错:{}", e.getMessage());
            return R.error("获取学历分析出错");
        } catch (Exception e) {
            log.error("获取学历分析出错:{}", e, e);
            return R.error("获取学历分析出错");
        }
    }

    /**************************************************************************************************
     **
     * 原子服务判断是否其他部门，是否相同副总
     *
     * @param null
     * @return {@link null }
     * @author DaBai
     * @date 2022/11/30  10:36
     */
    @GetMapping("/get/same-dept")
    public DataResult<Object> getSameDept(@RequestParam(name = "userId") String userId,@RequestParam(name = "chooseUserId") String chooseUserId){
        try {
            Map<String,Object> map =sysStaffInfoService.getSameDept(userId,chooseUserId);
            return R.success(map);
        } catch (DefaultException e){
            log.error("判断部门错误:{}", e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("根判断部门错误:{}", e, e);
            return R.error("判断部门错误");
        }
    }

    /**************************************************************************************************
     **
     * 原子服务获取部门领导 包含总经理
     *
     * @author kuang
     */
    @GetMapping("/get/dept-leader")
    public DataResult<List<String>> getDeptLeader(@RequestParam(name = "userId") String userId,@RequestParam(name = "signDeptId",required = false) String signDeptId){
        try {
            List<String> map =sysStaffInfoService.getDeptLeader(userId,signDeptId);
            return R.success(map);
        } catch (DefaultException e){
            log.error("判断部门错误:{}", e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("根判断部门错误:{}", e, e);
            return R.error("判断部门错误");
        }
    }

    /**************************************************************************************************
     **
     * 原子服务查询伙伴的所属团队长和副总id
     *
     * @param userId
     * @param sameDept 是否是相同副总 1相同 null或者其他值为不同
     * @return {@link null }
     * @author DaBai
     * @date 2022/11/30  10:36
     */
    @GetMapping("/get/leader-user-id")
    public DataResult<List<String>> getLeaderUserId(@RequestParam(name = "userId") String userId,@RequestParam(name = "sameDept",required = false) Integer sameDept){
        try {
            List<String> leadersId =sysStaffInfoService.getLeaderUserId(userId,sameDept);
            return R.success(leadersId);
        } catch (DefaultException e){
            log.error("查询伙伴的所属团队长和副总错误:{}", e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("查询伙伴的所属团队长和副总错误:{}", e, e);
            return R.error("查询伙伴的所属团队长和副总错误");
        }
    }

    /**************************************************************************************************
     **
     * 原子服务查询伙伴副总id
     *
     * @param userId
     * @return {@link null }
     * @author DaBai
     * @date 2022/11/30  10:36
     */
    @GetMapping("/get/deputy-user-id")
    public DataResult<String> getLeaderUserId(@RequestParam(name = "userId") String userId){
        try {
            String leadersId =sysStaffInfoService.getLeaderUserId(userId);
            return R.success(leadersId);
        } catch (DefaultException e){
            log.error("查询伙伴的所属副总错误:{}", e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("查询伙伴的所属副总错误:{}", e, e);
            return R.error("查询伙伴的副总错误");
        }
    }

    /**************************************************************************************************
     **
     * 原子服务判断原负责人与当前用户的上级是否一致
     *
     * @param userId 当前用户
     * @param chooseUserId  原负责人
     * @return {@link null }
     * @author kuang
     * @date 2023/01/10  10:36
     */
    @GetMapping("/get/check-user-id")
    public DataResult<Object> getCheckUserId(@RequestParam(name = "userId") String userId,@RequestParam(name = "chargeId") String chargeId){
        try {
            Integer isCharge =sysStaffInfoService.getCheckUserId(userId,chargeId);
            return R.success(isCharge);
        } catch (DefaultException e){
            log.info(":{判断原负责人与当前用户的上级是否一致入参：{}-----{}", userId, chargeId);
            log.error("判断原负责人与当前用户的上级是否一致错误:{}", e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("判断原负责人与当前用户的上级是否一致错误:{}", e, e);
            return R.error("判断原负责人与当前用户的上级是否一致错误");
        }
    }



    /**************************************************************************************************
     **
     *  原子服务查询负责人级别
     *
     * @param chargeId
     * @return {@link null }
     * @author DaBai
     * @date 2023/2/9  15:25
     */
    @GetMapping("/get/charge-level")
    public DataResult<Object> getChargeLevel(@RequestParam(name = "chargeId") String chargeId){
        try {
            Map<String,Object> map =sysStaffInfoService.getChargeLevel(chargeId);
            return R.success(map);
        } catch (DefaultException e){
            log.error("查询负责人级别错误:{}", e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("查询负责人级别错误:{}", e, e);
            return R.error("查询负责人级别错误");
        }
    }
}
