package com.sanxin.cloud.app.api.service;

import com.sanxin.cloud.common.rest.RestResult;
import com.sanxin.cloud.dto.ProgramBindVo;
import com.sanxin.cloud.entity.CCustomer;

/**
 * 注册
 * @author xiaoky
 * @date 2019-09-16
 */
public interface RegistService {
    /**
     * 发送短信验证码
     * @param phone
     * @return
     */
    RestResult sendVerCode(String phone, String areaCode);


    /**
     * 注册
     * @param customer
     * @return
     */
    RestResult doRegister(CCustomer customer) throws Exception;

    /**
     * 处理小程序绑定手机号
     * @param vo
     * @return
     */
    RestResult handleProgramBindPhone(ProgramBindVo vo);
}
