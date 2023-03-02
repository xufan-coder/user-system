package com.zerody.user.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.zerody.common.api.bean.DataResult;
import com.zerody.common.enums.customer.EducationBackgroundEnum;
import com.zerody.common.utils.DataUtil;
import com.zerody.common.utils.DateUtil;
import com.zerody.user.api.service.AddrRemoteService;
import com.zerody.user.constant.CheckCompare;
import com.zerody.user.domain.SysUserInfo;
import com.zerody.user.dto.SetSysUserInfoDto;
import com.zerody.user.dto.UserInfoComparDto;
import com.zerody.user.enums.FormatCheseEnum;
import com.zerody.user.vo.UserCompar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author  DaBai
 * @date  2021/4/28 14:32
 */
@Slf4j
public class UserCompareUtil {

    /**
     * 获取两个对象同名属性内容不相同的列表
     * @param class1 对象1
     * @param class2 对象2
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     */
    public static List<UserCompar> compareTwoClass(Object class1, Object class2) throws IllegalAccessException, ParseException {

        List<UserCompar> list = new ArrayList<>();
        Class<?> clazz1 = class1.getClass();
        Class<?> clazz2 = class2.getClass();
        //获得类中所有声明的字段，包括public、private和proteced，但是不包括父类的申明字段
        Field[] field1 = clazz1.getDeclaredFields();
        Field[] field2 = clazz2.getDeclaredFields();
        //遍历属性列表field1
        for (int i = 0; i < field1.length; i++) {
            //根据注解过滤掉不需要比较的属性
            CheckCompare annotation = getAnnotation(field1[i]);
            if(!DataUtil.isEmpty(annotation)) {

                //遍历属性列表field2
                for (int j = 0; j < field2.length; j++) {
                    //如果field1[i]属性名与field2[j]属性名内容相同
                    //只是通过名字做了比较，没有比较它们的类型
                    //需要人为的保证两个字段的类型是一致
                    if (field1[i].getName().equals(field2[j].getName())) {
                        System.out.println(annotation.name());
                        field1[i].setAccessible(true);
                        field2[j].setAccessible(true);
                        //如果field1[i]属性值与field2[j]属性值内容不相同
                        //过滤掉空值和相同的值
                        if (!compareTwo(field1[i].get(class1), field2[j].get(class2))) {
                            UserCompar custCompar = new UserCompar();
                            custCompar.setKey(field1[i].getName());
                            custCompar.setKeyName(annotation.name());
                            custCompar.setOldSubmitValue(setValue(field1[i].get(class1)));
                            custCompar.setNewSubmitValue(setValue(field2[j].get(class2)));

                            //
                            if(FormatCheseEnum.getByCode(custCompar.getKey()) != null){
                                buildSubmitVal(custCompar,custCompar.getKey(),field1[i].get(class1),field2[j].get(class2));
                            }else {
                                custCompar.setOldValue(setValue(field1[i].get(class1)));
                                custCompar.setNewValue(setValue(field2[j].get(class2)));
                            }
                            list.add(custCompar);
                        }

                        break;
                    }
                }
            }
        }
        log.info("比对结果"+ JSON.toJSONString(list));
        return list;
    }

    //对比两个数据是否相等
    public static boolean compareTwo(Object object1, Object object2) {

        if (object1 == null && object2 == null) {
            return true;
        }
        if ( "".equals(object1) && object2 == null) {
            return true;
        }
        if (object1 == null && "".equals(object2)) {
            return true;
         }
        if ("".equals(object1) && "".equals(object2)) {
            return true;
        }
        if(object1!=null){
            if (object1 instanceof BigDecimal) {
                BigDecimal bigDecimal = ((BigDecimal) object1).stripTrailingZeros();
                object1=bigDecimal;
            }
            if (object2 instanceof BigDecimal) {
                BigDecimal bigDecimal2 = ((BigDecimal) object2).stripTrailingZeros();
                object2=bigDecimal2;
            }
            if (object1.equals(object2)) {
                return true;
            }
        }
        return false;
    }

    public static Object setValue(Object object) {

        if (object == null){
            return null;
        }
        return object;
    }

    /**
    *    通过field获取注解值
    */
    private static CheckCompare getAnnotation(Field field){
        if(null==field){
            return null;
        }
        CheckCompare annotation = field.getAnnotation(CheckCompare.class);
        if(null != annotation){
            return annotation;
        }
        return null;
    }


    public static void buildSubmitVal(UserCompar custCompar,String f,Object val1,Object val2) throws ParseException {

        String name1 = null;
        String name2 = null;
        String s1 = val1 == null ? "" : val1.toString();
        String s2 = val2 == null ? "" : val2.toString();

        if (f.equals(FormatCheseEnum.status.name())) {
            name1="1".equals(s1)?"已解约":"0".equals(s1)?"合约中":"3".equals(s1) ? "合作":"";
            name2="1".equals(s2)?"已解约":"0".equals(s2)?"合约中":"3".equals(s2) ? "合作":"";
        }else if(f.equals(FormatCheseEnum.gender.name())){
            name1="1".equals(s1)?"女":"0".equals(s1)?"男":"";
            name2="1".equals(s2)?"女":"0".equals(s2)?"男":"";
        }else if(f.equals(FormatCheseEnum.maritalStatus.name())){
            name1="0".equals(s1)?"未婚":"1".equals(s1)?"已婚":"2".equals(s1)?"离婚":"";
            name2="0".equals(s2)?"未婚":"1".equals(s2)?"已婚":"2".equals(s1)?"离婚":"";
        }else if(f.equals(FormatCheseEnum.useState.name())){
            name1="0".equals(s1)?"正常":"1".equals(s1)?"冻结":"";
            name2="0".equals(s2)?"正常":"1".equals(s2)?"冻结":"";
        }else if(f.equals(FormatCheseEnum.isDiamondMember.name())){
            name1="0".equals(s1)?"否":"1".equals(s1)?"是":"";
            name2="0".equals(s2)?"否":"1".equals(s2)?"是":"";
        }else if(f.equals(FormatCheseEnum.recommendType.name())){
            name1="0".equals(s1)?"公司社招":"1".equals(s1)?"员工介绍":"";
            name2="0".equals(s2)?"公司社招":"1".equals(s2)?"员工介绍":"";
        }else if(f.equals(FormatCheseEnum.highestEducation.name())){
            name1 = EducationBackgroundEnum.getTextByCode(s1);
            name2 = EducationBackgroundEnum.getTextByCode(s2);
        }else if(f.equals(FormatCheseEnum.birthday.name()) || f.equals(FormatCheseEnum.dateLeft.name()) || f.equals(FormatCheseEnum.dateJoin.name()) ){
            name1 =val1 instanceof Date ?  DateUtil.formatyyyyMMddtoString((Date)val1) : s1;
            name2 =val2 instanceof Date ?  DateUtil.formatyyyyMMddtoString((Date)val2) : s2;
        }
        custCompar.setOldValue(name1);
        custCompar.setNewValue(name2);
    }

    public static String convertCompars(List<UserCompar> custCompars) {
        StringBuilder sb = new StringBuilder();
        for(UserCompar compar : custCompars){

            String oldValue = compar.getOldValue() == null ? "" : compar.getOldValue().toString();
            String newValue = compar.getNewValue() == null ? "" : compar.getNewValue().toString();
            sb.append("将").append(compar.getKeyName()).append("[")
                    .append(oldValue).append("]更改为[")
                    .append(newValue).append("]，");
        }
        if(sb.length() > 0) {
            sb.deleteCharAt(sb.length()-1);
        }
        return sb.toString();
    }

    public static void main(String[] args) throws ParseException, IllegalAccessException {
        SysUserInfo oldUserInfo = new SysUserInfo();
        oldUserInfo.setAncestral("银行");
        oldUserInfo.setIdCardFront("private/2ea37c49e4cda41202a3bcc82d42351a/picture:d0b8faee32974e4bbaa232fadb10e170.png");
        oldUserInfo.setIdCardReverse("private/2ea37c49e4cda41202a3bcc82d42351a/picture:036df1b6e82541f3a8e34e97bb6004ed.png");
        SetSysUserInfoDto setSysUserInfoDto = new SetSysUserInfoDto();

        setSysUserInfoDto.setAncestral("河边");
        setSysUserInfoDto.setAvatar("dddd");
        setSysUserInfoDto.setIdCardFront("private/2ea37c49e4cda41202a3bcc82d42351a/picture:5f088834cd5d4e129d39af26cac7804d.png");
        setSysUserInfoDto.setIdCardReverse("private/c64121a66c1a2553e08cfdf098fdc950/picture:cf51a1c6658a478887507aced9d68193.png");

        UserInfoComparDto userCompart = new UserInfoComparDto();
        BeanUtils.copyProperties(setSysUserInfoDto,userCompart);
        List<UserCompar> list = compareTwoClass(oldUserInfo,userCompart);

      //  System.out.println(JSON.toJSONString(list));

        System.out.println( DateUtil.formatyyyyMMddtoString(new Date()));
    }
}
