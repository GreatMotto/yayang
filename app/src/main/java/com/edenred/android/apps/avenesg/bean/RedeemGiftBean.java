package com.edenred.android.apps.avenesg.bean;

import java.io.Serializable;

/**
 * Created by wangwm on 2015/8/12.
 * 会员信息
 */
public class RedeemGiftBean implements Serializable{

    private static final long serialVersionUID = 1L;
    public String productId;//产品唯一标识
    public String articleName;//礼品名称
    public String articleDesc;//礼品描述
    public String productImageURL;//产品图片地址，图片放到APP后台，CRM存放图片路径
    public String articlePoint;//礼品所需积分
    public String articleId;//礼品唯一标识
    public String productCategroyId;//产品分类唯一标识
    public String rewardCatalogueFlag;//是否在reward catalogue界面中显示  1-显示
    public String rewardCatalogueOrderBy;//在reward catalogue界面中显示的排序

}
