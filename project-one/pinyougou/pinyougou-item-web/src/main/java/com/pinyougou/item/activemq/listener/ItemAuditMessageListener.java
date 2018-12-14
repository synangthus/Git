package com.pinyougou.item.activemq.listener;

import com.pinyougou.pojo.TbItemCat;
import com.pinyougou.sellergoods.service.GoodsService;
import com.pinyougou.sellergoods.service.ItemCatService;
import com.pinyougou.vo.Goods;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.listener.adapter.AbstractAdaptableMessageListener;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ItemAuditMessageListener extends AbstractAdaptableMessageListener {

    @Value("${ITEM_HTML_PATH}")
    private String ITEM_HTML_PATH ;

    @Reference
    private GoodsService goodsService;

    @Reference
    private ItemCatService itemCatService;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    @Override
    public void onMessage(Message message, Session session) throws JMSException {

        ObjectMessage objectMessage = (ObjectMessage) message;

        Long[] goodsIds = (Long[]) objectMessage.getObject();

        for (Long goodsId : goodsIds) {
            genItemHtml(goodsId);
        }
        System.out.println(" 同步生成商品静态页面完成。");
    }

    private void genItemHtml(Long goodsId) {
        try {
            //获取模板
            Configuration configuration = freeMarkerConfigurer.getConfiguration();

            Template template = configuration.getTemplate("item.ftl");

            //获取模板需要的数据
            Map<String, Object> dataModel = new HashMap<>();

            //根据商品id查询商品基本. 描述.启用的SKU列表
            Goods goods = goodsService.findGoodsByIdAndStatus(goodsId, "1");

            //商品基本信息
            dataModel.put("goods",goods.getGoods());

            //商品描述信息
            dataModel.put("goodsDesc",goods.getGoodsDesc());

            //查询三级商品分类
            TbItemCat itemCat1 = itemCatService.findOne(goods.getGoods().getCategory1Id());

            TbItemCat itemCat2 = itemCatService.findOne(goods.getGoods().getCategory2Id());

            TbItemCat itemCat3 = itemCatService.findOne(goods.getGoods().getCategory3Id());

            //查询SKU商品列表
            dataModel.put("itemList",goods.getItemList());

            //输出到指定路径
            String filename = ITEM_HTML_PATH + goodsId + ".html";
            FileWriter fileWriter = new FileWriter(filename);

            template.process(dataModel , fileWriter);
            fileWriter.close();

        } catch (Exception e) {
            e.printStackTrace();
    }
}
}
