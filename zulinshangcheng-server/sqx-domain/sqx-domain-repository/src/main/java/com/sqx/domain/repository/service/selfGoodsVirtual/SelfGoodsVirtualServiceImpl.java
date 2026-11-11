package com.sqx.domain.repository.service.selfGoodsVirtual;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.SelfGoodsVirtual;
import com.sqx.domain.repository.mapper.selfGoodsVirtual.SelfGoodsVirtualJpaRepository;
import com.sqx.domain.repository.mapper.selfOrder.OrdersJpaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SelfGoodsVirtualServiceImpl implements SelfGoodsVirtualService {
    @Autowired
    private SelfGoodsVirtualJpaRepository jpaRepository;
    @Autowired
    private OrdersJpaRepository ordersJpaRepository;

    @Override
    public Result findAll(Integer page, Integer size, Long goodsId, String content) {
        Pageable pageable = PageRequest.of(page, size);
        if (StringUtils.isNotEmpty(content)){
            Page<Map<String,Object>> all1 = jpaRepository.findAllContent(pageable, goodsId, content);
            return ResultUtil.success(all1);
        }else {
            Page<Map<String,Object>> all1 = jpaRepository.findAll(pageable, goodsId);
            return ResultUtil.success(all1);
        }
    }


    /**
     * 批量添加
     * @return
     */
    @Override
    public Result saveBody(SelfGoodsVirtual e) {
        List<SelfGoodsVirtual> all = jpaRepository.findAll();
        String msg = "";
        int success = 0;
        int failure = 0;
        //获取多个卡密，切割循环
        String[] list = e.getContent().split(",");
        List<SelfGoodsVirtual> saveList = new ArrayList<>();
        for (int i = 0; i < list.length; i++) {
            String content = list[i];
            //卡密查重
            List<SelfGoodsVirtual> checkList = jpaRepository.checkContent(content, e.getGoodsId());
            if (checkList.size() > 0){
                failure++;
                msg += "/"+content;
            }else {
                SelfGoodsVirtual s = new SelfGoodsVirtual();
                s.setStatus(1); //基础信息
                s.setCreateTime(DateUtils.createTime());
                s.setGoodsId(e.getGoodsId()); //商品信息
                s.setTitle(e.getTitle());
                s.setCoverImg(e.getCoverImg());
                s.setGoodsId(e.getGoodsId());
                s.setLinkUrl(e.getLinkUrl()); //链接
                s.setContent(content); //卡密
                saveList.add(s);
                success++; //添加成功计数
            }
        }
        jpaRepository.saveAll(saveList);
        return ResultUtil.success("本次补货"+list.length+"条，成功"+success+"条，失败"+failure+"条，重复数据为"+msg);
    }

    @Override
    public Result updateBody(SelfGoodsVirtual entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        return ResultUtil.success(jpaRepository.findById(id).orElse(null));
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public SelfGoodsVirtual sendGoods(Long goodsId) {
        List<SelfGoodsVirtual> goodsList = jpaRepository.findByGoodsId(goodsId);
        if (goodsList.size() > 0){
            SelfGoodsVirtual g = goodsList.get(0);
            g.setStatus(3); //已使用
            jpaRepository.save(g);
            return g;
        }
        return null;
    }
}
