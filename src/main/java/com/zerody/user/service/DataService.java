package com.zerody.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zerody.user.domain.Data;
import com.zerody.user.dto.data.DataAddDto;

/**
 *@ClassName DataService
 *@author    PengQiang
 *@DateTime  2022/9/15_14:33
 *@Deacription  service
 */
public interface DataService extends IService<Data> {


    /**
     *
     * 添加键值对数据
     * @author               PengQiang
     * @description
     * @date                 2022/9/15 14:44
     * @param                param 添加参数
     * @return               void
     */
    void addData(DataAddDto param);

    /**
     *
     * 通过key 查询value
     * @author               PengQiang
     * @description          DELL
     * @date                 2022/9/15 14:54
     * @param                key 键值对key
     * @return               java.lang.String
     */
    String getValueByKey(String key);
}
