package com.sanxin.cloud.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sanxin.cloud.config.pages.SPage;
import com.sanxin.cloud.entity.CMoneyDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户商余额明细 Mapper 接口
 * </p>
 *
 * @author Arno
 * @since 2019-09-20
 */
public interface CMoneyDetailMapper extends BaseMapper<CMoneyDetail> {

    /**
     * 余额明细
     * @param page
     * @param cid
     * @return
     */
    Page<CMoneyDetail> queryBalanceDetail(@Param("cid") Integer cid);
}
