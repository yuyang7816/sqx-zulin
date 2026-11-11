package com.sqx.domain.repository.service.selfGoodsType;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.core.domain.Result;
import com.sqx.common.core.domain.ResultUtil;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.Goods;
import com.sqx.domain.repository.entity.sys.GoodsType;
import com.sqx.domain.repository.entity.sys.GoodsTypeResult;
import com.sqx.domain.repository.mapper.selfGoodsType.GoodsTypeJpaRepository;
import com.sqx.domain.repository.mapper.selfGoodsType.GoodsTypeRepository;
import com.sqx.domain.repository.mapper.sys.GoodsTypeDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GoodsTypeServiceImpl implements GoodsTypeService {
    @Autowired
    private GoodsTypeJpaRepository jpaRepository;
    @Autowired
    private GoodsTypeRepository repository;
    @Autowired
    private GoodsTypeDao goodsTypeDao;

    @Override
    public Result findAll(String name) {
        List<GoodsType> type2 = goodsTypeDao.selectList(new QueryWrapper<GoodsType>().ne("parent_id", 0));
        if (name == null || name.isEmpty()) {
            // 如果没有名字过滤条件，则直接返回所有顶级以及子集的递归构建
            List<GoodsType> type1 = goodsTypeDao.selectList(new QueryWrapper<GoodsType>().eq("parent_id", 0));
            List<GoodsType> resultList = new ArrayList<>();
            for (GoodsType g : type1) {
                GoodsType goodsType = new GoodsType();
                goodsType.setId(g.getId());
                goodsType.setName(g.getName());
                goodsType.setImg(g.getImg());
                goodsType.setSort(g.getSort());
                goodsType.setCreateAt(g.getCreateAt());
                goodsType.setParentId(g.getParentId());
                goodsType.setChildren(builderAllChildren(type2,g.getId()));
                resultList.add(goodsType);
            }
            return ResultUtil.success(resultList);
        } else {
            List<GoodsType> typeList = new ArrayList<>();
            List<GoodsType> allTypeList = goodsTypeDao.findAllTypes(name);
            // 查找匹配的名字，如果是顶级就将子集递归构建
            for (GoodsType allType : allTypeList) {
                if (allType.getName().contains(name)) {
                    if (allType.getParentId() == 0) {
                        GoodsType goodsType = new GoodsType();
                        goodsType.setId(allType.getId());
                        goodsType.setName(allType.getName());
                        goodsType.setImg(allType.getImg());
                        goodsType.setSort(allType.getSort());
                        goodsType.setCreateAt(allType.getCreateAt());
                        goodsType.setParentId(allType.getParentId());
                        // 如果是顶级类目，构建完整的层级结构
                        goodsType.setChildren(builderAllChildren(type2, goodsType.getId()));
                        typeList.add(goodsType);
                        return ResultUtil.success(typeList);
                    } else {
                        // 如果不是顶级类目
                        List<GoodsType> allTypes = goodsTypeDao.findAllTypes(name);
                        typeList.addAll(allTypes);
                        return ResultUtil.success(typeList);
                    }
                }
            }
            return ResultUtil.success(typeList);
        }
    }

    private List<GoodsType> builderAllChildren(List<GoodsType> type1, Long parentId) {
        List<GoodsType> resultList = new ArrayList<>();
        if (type1 != null) {
            for (GoodsType type : type1) {
                if (type.getParentId() == null) {
                    continue;
                }
                if (type.getParentId().equals(parentId)) {
                    GoodsType child = new GoodsType();
                    child.setId(type.getId());
                    child.setName(type.getName());
                    child.setImg(type.getImg());
                    child.setSort(type.getSort());
                    child.setCreateAt(type.getCreateAt());
                    child.setParentId(type.getParentId());
                    // 构建递归查询子集
                    child.setChildren(builderAllChildren(type1, type.getId()));
                    resultList.add(child);
                }
            }
        }
        return resultList;
    }

    private List<GoodsType> builderChildrens(List<GoodsType> allTypes, Long parentId) {
        List<GoodsType> resultList = new ArrayList<>();
        if (allTypes != null) {
            for (GoodsType type : allTypes) {
                if (type.getParentId().equals(parentId)) {
                    GoodsType result = new GoodsType();
                    result.setValue(type.getId());
                    result.setLabel(type.getName());
                    // 构建递归查询子集
                    result.setChildren(builderChildrens(allTypes, type.getId()));
                    resultList.add(result);
                }
            }
        }
        return resultList;
    }

    @Override
    public Result findAllResult() {
        List<GoodsType> type1 = repository.findAllStatus0();
        List<GoodsType> type2 = repository.findAllTypes();
        List<GoodsType> resultList = new ArrayList<>();
        for (GoodsType g : type1) {
            GoodsType result = new GoodsType();
            result.setValue(g.getId());
            result.setLabel(g.getName());
            List<GoodsType> children = builderChildrens(type2, g.getId());
            if (children.size() == 0){
                result.setChildren(null);
            }else {
                result.setChildren(children);
            }
//            result.setChildren(builderChildrens(type2, g.getId()));
            resultList.add(result);
        }
        return ResultUtil.success(resultList);
    }

    @Override
    public Result findAllResult1() {
        List<GoodsType> type1 = goodsTypeDao.selectList(new QueryWrapper<GoodsType>().eq("parent_id", 0));
        List<GoodsTypeResult> resultList = new ArrayList<>();
        for (GoodsType g : type1) {
            GoodsTypeResult result = new GoodsTypeResult();
            if (g.getParentId().toString().equals("0")) {
                result.setValue(g.getId());
                result.setLabel(g.getName());
            }
            resultList.add(result);
        }
        return ResultUtil.success(resultList);
    }

    @Override
    public Result findAllResult2(Long typeId) {
        List<GoodsType> type2 = repository.findAllTypesByParentId(typeId);
        List<GoodsTypeResult> resultList = new ArrayList<>();
        for (GoodsType g : type2) {
            GoodsTypeResult result = new GoodsTypeResult();
            result.setValue(g.getId());
            result.setLabel(g.getName());
            resultList.add(result);
        }
        return ResultUtil.success(resultList);
    }

    @Override
    public Result saveBody(GoodsType entity) {
        if (entity.getParentId() == null){
        entity.setParentId(0L);
        }
        entity.setCreateAt(DateUtils.createTime());
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(GoodsType entity) {
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

}
