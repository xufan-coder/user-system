package com.zerody.user.util;

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
        for(int i=0;i<newHistoryList.size();i++){
            StaffHistoryDto history = newHistoryList.get(i);
            if(StringUtils.isEmpty(history.getId())){
                honor.append("新增").append(name).append("记录:").append(history.getDescribe()).append("    ");
            }else {
                for(StaffHistoryVo oldHistory: historyVos){
                    if(oldHistory.getId().equals(history.getId())){
                        List<UserCompar> comparList = UserCompareUtil.compareTwoClass(oldHistory,history);
                        String content = UserCompareUtil.convertCompars(comparList);
                        if(StringUtils.isNotEmpty(content)){
                            honor.append("更新").append(name).append("记录").append(i).append(":").append(content).append("    ");;
                        }
                        break;
                    }
                }
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


}
