package com.sqx.domain.repository.entity.sys;

import com.sqx.domain.repository.entity.app.TbFootprint;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2025/1/8 11:00
 */
@Data
public class DailyFootprint {

    // 日期
    private LocalDate date;

    // 足迹列表
    private List<TbFootprint> footprints;

    // 已查看的商品id集合
    private Set<Long> seenGoodsIds;

    // 构造函数、getter 和 setter 方法
    public DailyFootprint(LocalDate date) {
        this.date = date;
        this.footprints = new ArrayList<>();
        this.seenGoodsIds = new HashSet<>();
    }

    public DailyFootprint(LocalDate date,List<TbFootprint> data){
        this.date = date;
        this.footprints = data;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<TbFootprint> getFootprints() {
        return footprints;
    }

    public void setFootprints(List<TbFootprint> footprints) {
        this.footprints = footprints;
    }

    public void addFootprint(TbFootprint footprint) {
        this.footprints.add(footprint);
    }

    public Set<Long> getSeenGoodsIds() {
        return seenGoodsIds;
    }

    public void setSeenGoodsIds(Set<Long> seenGoodsIds) {
        this.seenGoodsIds = seenGoodsIds;
    }
}
