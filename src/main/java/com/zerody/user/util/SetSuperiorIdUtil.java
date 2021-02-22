package com.zerody.user.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author PengQiang
 * @ClassName SetSuperiorIdUtil
 * @DateTime 2021/2/22_14:37
 * @Deacription TODO
 */
public class SetSuperiorIdUtil {

    private final static String SUPERIOR_SYMBOL  = "_";

    /**
     *
     *
     * @author               PengQiang
     * @description          获取上级id
     * @date                 2021/2/22 14:57
     * @param                [id]
     * @return               java.util.List<java.lang.String>
     */
    public static List<String> getSuperiorIds(String id){
        return getSuperiorIds(id, new ArrayList<>());
    }

    /**
     *
     *
     * @author               PengQiang
     * @description          获取上级id ( 第一个为自身id 如果不要自身的id 可remove(0)
     * @date                 2021/2/22 14:54
     * @param                [id, superiorIds]
     * @return               java.util.List<java.lang.String>
     */
    private static List<String> getSuperiorIds(String id, List<String> superiorIds){
        superiorIds.add(id);
        //如果没有下划线 表示已是顶级id
        if(id.lastIndexOf(SUPERIOR_SYMBOL) == -1){
            return superiorIds;
        }
        //从最后一个_ 进行截取 最后一个_ 前面的为上级部门id
        id = id.substring(0, id.lastIndexOf(SUPERIOR_SYMBOL));
        return getSuperiorIds(id, superiorIds);
    }

}
