package com.edenred.android.apps.avenesg.bean;

import java.io.Serializable;

/**
 * Created by zhaoxin on 2015/8/13.
 * 产品列表
 */
public class ProductBean implements Serializable{
    private static final long serialVersionUID = 1L;
    public String productId;//产品唯一标识
    public String productCode;//产品编码
    public String productName;//产品名称
    public String productDesc;//产品描述
    public String productImageURL;//产品图片地址，图片放到APP后台，CRM存放图片路径
    public String productPrice;//产品价格
    public String orderBy;//界面排序字段
    public String productCategoryId;//产品分类唯一号
}