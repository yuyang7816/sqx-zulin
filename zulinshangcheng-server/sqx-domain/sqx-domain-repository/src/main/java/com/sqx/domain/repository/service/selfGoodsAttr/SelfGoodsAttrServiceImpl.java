package com.sqx.domain.repository.service.selfGoodsAttr;

import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.domain.repository.entity.sys.SelfGoodsAttr;
import com.sqx.domain.repository.entity.sys.SelfGoodsAttrValue;
import com.sqx.domain.repository.mapper.selfGoodsAttr.SelfGoodsAttrJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsAttrValue.SelfGoodsAttrValueJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SelfGoodsAttrServiceImpl implements SelfGoodsAttrService {
    @Autowired
    private SelfGoodsAttrJpaRepository attrJpaRepository;
    @Autowired
    private SelfGoodsAttrValueJpaRepository attrValueJpaRepository;

    @Override
    public Result saveBody(SelfGoodsAttr e) {
        SelfGoodsAttr save = attrJpaRepository.save(e);
        List<SelfGoodsAttrValue> list = e.getAttrValue();
        for (SelfGoodsAttrValue v : list) {
            v.setGoodsId(e.getGoodsId());
            v.setAttrId(save.getId());
            attrValueJpaRepository.save(v);
        }
        return ResultUtil.success(save);
    }

    /**
     * 修改商品attr
     * @param e
     * @return
     */
    @Override
    public Result updateBody(SelfGoodsAttr e) {
        //查询所有值删掉
        List<SelfGoodsAttrValue> allRuleValue = attrValueJpaRepository.findAllByGoodsId(e.getGoodsId());
        attrValueJpaRepository.deleteAll(allRuleValue);
        //放入新值
        List<SelfGoodsAttrValue> list = e.getAttrValue();
        for (SelfGoodsAttrValue v : list) {
            v.setGoodsId(e.getGoodsId());
            v.setAttrId(e.getId());
            attrValueJpaRepository.save(v);
        }
        return ResultUtil.success(attrJpaRepository.save(e));
    }

    @Override
    public List<SelfGoodsAttr> findByGoodsId(Long goodsId) {
        List<SelfGoodsAttr> list = attrJpaRepository.findAllByGoodsId(goodsId);
        for (SelfGoodsAttr s : list) {
            List<SelfGoodsAttrValue> valueList = attrValueJpaRepository.findAllByGoodsId(s.getGoodsId());
            s.setAttrValue(valueList);
        }
        return list;
    }

    @Override
    public void deleteAttrAndValue(Long goodsId) {
        //删掉所有attr
        List<SelfGoodsAttr> list = attrJpaRepository.findAllByGoodsId(goodsId);
        attrJpaRepository.deleteAll(list);
        //删掉所有value
        List<SelfGoodsAttrValue> allRuleValue = attrValueJpaRepository.findAllByGoodsId(goodsId);
        attrValueJpaRepository.deleteAll(allRuleValue);
    }
}
