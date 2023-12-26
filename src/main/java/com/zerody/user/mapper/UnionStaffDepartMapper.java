package com.zerody.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zerody.user.domain.UnionStaffDepart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 *
 * @author
 * @description          DELL
 * @date                 2021/1/19 14:59
 * @param
 * @return
 */
public interface UnionStaffDepartMapper extends BaseMapper<UnionStaffDepart> {

    List<UnionStaffDepart> getStaffByRole(String roleId);

    String getDeptIdByStaffId(@Param("staffId") String staffId);
}