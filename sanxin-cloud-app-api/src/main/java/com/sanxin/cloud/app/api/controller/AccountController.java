package com.sanxin.cloud.app.api.controller;

import com.sanxin.cloud.app.api.common.AccountMapping;
import com.sanxin.cloud.app.api.service.AccountService;
import com.sanxin.cloud.common.BaseUtil;
import com.sanxin.cloud.common.rest.RestResult;
import com.sanxin.cloud.config.pages.SPage;
import com.sanxin.cloud.entity.CMarginDetail;
import com.sanxin.cloud.entity.CMoneyDetail;
import com.sanxin.cloud.entity.CTimeDetail;
import com.sanxin.cloud.service.system.login.LoginTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private LoginTokenService loginTokenService;

    /**
     * 我的押金明细
     * @return
     */
    @RequestMapping(value = AccountMapping.MY_DEPOSIT)
    public RestResult queryMyDeposit(SPage<CMarginDetail> page) {
        String token = BaseUtil.getUserToken();
        Integer cid = loginTokenService.validLoginCid(token);
        return accountService.queryMyDeposit(page,cid);
    }

    /**
     * 我的钱包
     * @return
     */
    @RequestMapping(value = AccountMapping.MY_PURSE)
    public RestResult queryMyPurse() {
        String token = BaseUtil.getUserToken();
        Integer cid = loginTokenService.validLoginCid(token);
        return accountService.queryMyPurse(cid);
    }

    /**
     * 余额明细
     * @return
     */
    @RequestMapping(value = AccountMapping.BALANCE_DETAIL)
    public RestResult queryBalanceDetail(SPage<CMoneyDetail> page) {
        String token = BaseUtil.getUserToken();
        Integer cid = loginTokenService.validLoginCid(token);
        return accountService.queryBalanceDetail(page,cid);
    }

    /**
     * 我要充值显示余额
     * @return
     */
    @RequestMapping(value = AccountMapping.GET_BALANCE)
    public RestResult getBalance() {
        String token = BaseUtil.getUserToken();
        Integer cid = loginTokenService.validLoginCid(token);
        return accountService.getBalance(cid);
    }

    /**
     * 用户时长明细
     * @return
     */
    @RequestMapping(value = AccountMapping.TIME_DETAIL)
    public RestResult queryTimeDetail(SPage<CTimeDetail> page) {
        String token = BaseUtil.getUserToken();
        Integer cid = loginTokenService.validLoginCid(token);
        return accountService.queryTimeDetail(page,cid);
    }

    /**
     * 购买时长礼包
     * @return
     */
    @RequestMapping(value = AccountMapping.GET_BUY_GIFT)
    public RestResult getBuyGift() {
        String token = BaseUtil.getUserToken();
        Integer cid = loginTokenService.validLoginCid(token);
        return accountService.getBuyGift(cid);
    }

    /**
     * 支付购买时长
     * @param id 礼包id
     * @param type 支付类型1余额
     * @param payPass 支付密码
     * @return
     */
    @RequestMapping(value = AccountMapping.PAY_TIME_GIFT)
    public RestResult payTimeGift(Integer id,Integer type,String payPass) {
        String token = BaseUtil.getUserToken();
        Integer cid = loginTokenService.validLoginCid(token);
        return accountService.payTimeGift(cid,id,type,payPass);
    }


    /**
     * 充值押金
     * @param payWord 支付密码-选择余额支付需要
     * @param payType 支付方式
     * @param payChannel 支付渠道
     * @param freeSecret 是否免密支付
     * @return
     */
    @RequestMapping(value = AccountMapping.RECHARGE_DEPOSIT)
    public RestResult rechargeDeposit(String payWord, Integer payType, Integer payChannel, Integer freeSecret) {
        String token = BaseUtil.getUserToken();
        Integer cid = loginTokenService.validLoginCid(token);
        return accountService.rechargeDeposit(cid, payWord, payType, payChannel, freeSecret);
    }

    /**
     * 支付方式
     * @param type 支付渠道类型
     * @return
     */
    @RequestMapping(value = AccountMapping.PAY_METHOD_LIST)
    public RestResult payMethodList(Integer type) {
        String token = BaseUtil.getUserToken();
        Integer cid = loginTokenService.validLoginCid(token);
        return accountService.payMethodList(type,cid);
    }


}
