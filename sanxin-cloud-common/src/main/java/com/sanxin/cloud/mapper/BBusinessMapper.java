package com.sanxin.cloud.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sanxin.cloud.dto.PowerBankListVo;
import com.sanxin.cloud.entity.BBusiness;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Arno
 * @since 2019-08-27
 */
public interface BBusinessMapper extends BaseMapper<BBusiness> {

    /**
     * 根据经纬度分页查询周边商铺
     * @param page
     * @param latVal
     * @param lonVal
     * @param search
     * @param radius
     * @return
     */
    List<PowerBankListVo> findByShops(IPage<PowerBankListVo> page, @Param(value = "latVal") String latVal, @Param(value = "lonVal") String lonVal, @Param(value = "search") String search, @Param(value = "radius") Integer radius);

    /**
     * 根据经纬度搜索所有商铺
     * @param latVal
     * @param lonVal
     * @return
     */
    List<PowerBankListVo> rangeShop(@Param(value = "latVal")String latVal,@Param(value = "lonVal") String lonVal, @Param("radius") Integer radius);


    /**
     * 大小机柜分类
     * @param id
     * @return
     */
    List<Integer> findByCabinet(@Param(value = "id") Integer id);

    PowerBankListVo findByCode(@Param(value = "id") Integer id,@Param(value = "latVal")String latVal,@Param(value = "lonVal") String lonVal,@Param(value = "radius") Integer radius);
}
