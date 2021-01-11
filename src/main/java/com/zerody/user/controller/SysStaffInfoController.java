package com.zerody.user.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.api.bean.R;
import com.zerody.common.exception.DefaultException;
import com.zerody.user.dto.AdminsPageDto;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.SysStaffInfoPageDto;
import com.zerody.user.enums.TemplateTypeEnum;
import com.zerody.user.service.SysStaffInfoService;
import com.zerody.user.vo.BosStaffInfoVo;
import com.zerody.user.vo.SysUserInfoVo;
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
    private SysStaffInfoService sysStaffInfoService;

    /**
    *   分页查询员工信息
    */
    @RequestMapping(value = "/page/get", method = RequestMethod.GET)
    public DataResult<IPage<BosStaffInfoVo>> getPageAllStaff(SysStaffInfoPageDto sysStaffInfoPageDto){
        return R.success(sysStaffInfoService.getPageAllStaff(sysStaffInfoPageDto));
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
            log.error("添加员工错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("添加员工错误:{}",e.getMessage());
            return R.error("添加员工失败,请求异常");
        }
    }

    //修改员工状态
    @RequestMapping(value = "/loginStatus/{id}/{status}", method =  RequestMethod.PUT)
    public DataResult<Object> updateStaffStatus(@PathVariable(name = "id") String staffId, @PathVariable(name = "status") Integer status){
        try {
            sysStaffInfoService.updateStaffStatus(staffId, status);
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
            sysStaffInfoService.updateStaff(setSysUserInfoDto);
            return R.success();
        } catch (DefaultException e){
            log.error("修改员工信息错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("修改员工信息错误:{}",e.getMessage());
            return R.error("修改员工信息失败,请求异常");
        }
    }

    /**
    *    根据员工id查询员工信息
    */
    @GetMapping("/{id}")
    public DataResult<SysUserInfoVo> selectStaffById(@PathVariable(name = "id") String staffId){
        return R.success(sysStaffInfoService.selectStaffById(staffId));
    }

    /**
     *    根据用户id查询员工信息
     */
    @GetMapping("/get-by-user")
    public DataResult<SysUserInfoVo> getInfoByUserId(@RequestParam(value = "userId")String userId){
        return R.success(sysStaffInfoService.selectStaffByUserId(userId));
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
            response.addHeader("content-disposition", encodeName);

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
    *    批量导入用户excel
    */
    @PostMapping("/import")
    public DataResult<Object> batchImportUser(MultipartFile file){
        try {
            return R.success(sysStaffInfoService.batchImportStaff(file));
        } catch (DefaultException e){
            log.error("批量导入员工错误:{}",e.getMessage());
            return R.error(e.getMessage());
        }  catch (Exception e) {
            log.error("批量导入员工错误:{}",e.getMessage());
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
    *   分页获取平台管理员列表
    */
    @RequestMapping(value = "/get-admins", method = RequestMethod.GET)
    public DataResult<IPage<BosStaffInfoVo>> getAdmins(AdminsPageDto dto){
        return R.success(sysStaffInfoService.getAdmins(dto));
    }

}
