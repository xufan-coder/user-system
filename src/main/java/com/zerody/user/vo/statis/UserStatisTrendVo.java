package com.zerody.user.vo.statis;

import lombok.Data;

import java.util.List;

/**
 * @author : chenKeFeng
 * @date : 2023/5/8 11:34
 */
@Data
public class UserStatisTrendVo {

    /**
     * 年龄
     */
    List<UserAgeStatisQueryVo> ageStatisQuery;

    /**
     * 性別
     */
    List<UserSexStatisQueryVo> userSexStatisQueryVoList;

    /**
     * 学历
     */
    List<DegreeVo> degreeVoList;

}
