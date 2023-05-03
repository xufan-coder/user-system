package com.zerody.user.handler.user;

import com.zerody.user.vo.statis.UserAgeStatisQueryVo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName UserStatisHandle
 * @DateTime 2023/5/3 10:21
 */
@Component
public class UserStatisHandle {

    public static List<UserAgeStatisQueryVo> getStatisUserAge() {
        List<UserAgeStatisQueryVo> result = new ArrayList<>();
        result.add(new UserAgeStatisQueryVo(18, 25));
        result.add(new UserAgeStatisQueryVo(26, 35));
        result.add(new UserAgeStatisQueryVo(36, 15));
        result.add(new UserAgeStatisQueryVo(46, null));
        return result;
    }
}
