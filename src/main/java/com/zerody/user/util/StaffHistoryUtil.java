package com.zerody.user.util;

import com.zerody.user.domain.FamilyMember;
import com.zerody.user.domain.UserResume;
import com.zerody.user.dto.StaffHistoryDto;
import com.zerody.user.dto.SysStaffRelationDto;
import com.zerody.user.vo.StaffHistoryVo;
import com.zerody.user.vo.SysStaffRelationVo;
import com.zerody.user.vo.UserCompar;
import io.micrometer.core.instrument.util.StringUtils;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class StaffHistoryUtil {


    public static String updateHistory(List<StaffHistoryVo> historyVos,List<StaffHistoryDto> newHistoryList, String name) throws ParseException, IllegalAccessException {

        StringBuilder honor = new StringBuilder();
        for (StaffHistoryDto history : newHistoryList) {
            if (StringUtils.isEmpty(history.getId())) {
                honor.append("新增").append(name).append("记录:").append(history.getTime()).append("    ").append(history.getDescribe()).append("    ");
            }
        }
        List<String> newStr = newHistoryList.stream().map(StaffHistoryDto::getId).collect(Collectors.toList());
        for(StaffHistoryVo oldHistory: historyVos){
            if(!newStr.contains(oldHistory.getId())){
                honor.append("删除了惩罚记录:[").append(oldHistory.getTime()).append("    ").append(oldHistory.getDescribe()).append("]    ");
            }
        }
        return honor.toString();
    }

    public static String updateRelation(List<SysStaffRelationVo> relationVos, List<SysStaffRelationDto> newRelationVos) throws ParseException, IllegalAccessException {

        StringBuilder honor = new StringBuilder();
        for(int i=0;i<newRelationVos.size();i++){
            SysStaffRelationDto history = newRelationVos.get(i);
            if(StringUtils.isEmpty(history.getId())){
                honor.append("新增关系记录:").append(history.getDesc()).append("    ");
            }else {
                for(SysStaffRelationVo oldHistory: relationVos){
                    if(oldHistory.getId().equals(history.getId())){
                        List<UserCompar> comparList = UserCompareUtil.compareTwoClass(oldHistory,history);
                        String content = UserCompareUtil.convertCompars(comparList);
                        if(StringUtils.isNotEmpty(content)){
                            honor.append("更新关系记录").append(i).append(":").append(content);
                        }
                        break;
                    }
                }
            }
        }
        List<String> newStr = newRelationVos.stream().map(SysStaffRelationDto::getId).collect(Collectors.toList());
        for(SysStaffRelationVo oldHistory: relationVos){
            if(!newStr.contains(oldHistory.getId())){
                honor.append("删除了关系记录:[").append(oldHistory.getCompanyName()).append("    ").append(oldHistory.getUserName()).append("]   ");
            }
        }
        return honor.toString();
    }


    public static String getDiplomas(List<String> oldDiplomas,List<String> diplomas,String name){
        StringBuilder honor = new StringBuilder();
        for(String old : oldDiplomas) {
            if(!diplomas.contains(old)){
                honor.append("删除了").append(name).append(":[").append(old).append("]   ");
            }
        }
        for(String d : diplomas) {
            if(!oldDiplomas.contains(d)){
                honor.append("新增了").append(name).append(":[").append(d).append("]   ");
            }
        }
        return honor.toString();
    }


    public static String updateResume(List<UserResume> oldResumeList, List<UserResume> userResumes) throws ParseException, IllegalAccessException {
        StringBuilder honor = new StringBuilder();
        UserResume resume = new UserResume();

        // 比对个人履历的修改项
        for(int i=0;i<userResumes.size();i++){
            UserResume history = userResumes.get(i);
            if(i < oldResumeList.size()) {
                resume = oldResumeList.get(i);
            }else {
                resume = new UserResume();
            }
            List<UserCompar> comparList = UserCompareUtil.compareTwoClass(resume,history);
            String content = UserCompareUtil.convertCompars(comparList);
            if(StringUtils.isNotEmpty(content)) {
                honor.append("更新了个人履历:").append(i + 1).append(":{").append(content).append(" }   ");
            }
        }

        if(oldResumeList.size() > userResumes.size()) {
            for(int i= userResumes.size()-1; i <oldResumeList.size();i++){
                UserResume oldHistory = oldResumeList.get(i);
                List<UserCompar> comparList = UserCompareUtil.compareTwoClass(oldHistory,resume);
                String content = UserCompareUtil.convertCompars(comparList);
                if(StringUtils.isNotEmpty(content)){
                    honor.append("删除了个人履历").append(i).append(": {").append(content).append(" }   ");
                }

            }
        }
        return honor.toString();
    }

    public static String updateFamily(List<FamilyMember> oldFamilyList, List<FamilyMember> familyMembers) throws ParseException, IllegalAccessException {
        StringBuilder honor = new StringBuilder();
        FamilyMember family = new FamilyMember();

        for(int i=0;i<familyMembers.size();i++){
            FamilyMember history = familyMembers.get(i);
            if(StringUtils.isEmpty(history.getId())){
                List<UserCompar> comparList = UserCompareUtil.compareTwoClass(family,history);
                String content = UserCompareUtil.convertCompars(comparList);
                if(StringUtils.isNotEmpty(content)){
                    honor.append("新增了家庭成员:").append(i).append(":").append(content).append("    ");
                }
            }else {
                // 比对个人履历的修改项
                for(FamilyMember oldHistory: oldFamilyList){
                    if(oldHistory.getId().equals(history.getId())){
                        List<UserCompar> comparList = UserCompareUtil.compareTwoClass(oldHistory,history);
                        String content = UserCompareUtil.convertCompars(comparList);
                        if(StringUtils.isNotEmpty(content)){
                            honor.append("更新了家庭成员:").append(i).append(":").append(content).append("    ");;
                        }
                        break;
                    }
                }
            }
        }
        List<String> newStr = familyMembers.stream().map(FamilyMember::getId).collect(Collectors.toList());
        for(FamilyMember oldHistory: oldFamilyList){
            if(!newStr.contains(oldHistory.getId())){
                List<UserCompar> comparList = UserCompareUtil.compareTwoClass(oldHistory,family);
                String content = UserCompareUtil.convertCompars(comparList);
                if(StringUtils.isNotEmpty(content)){
                    honor.append("新增了家庭成员:").append(content).append("    ");
                }
            }
        }
        return honor.toString();
    }
}
