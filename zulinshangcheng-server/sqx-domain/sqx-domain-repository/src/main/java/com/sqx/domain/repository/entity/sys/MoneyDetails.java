package com.sqx.domain.repository.entity.sys;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 *  money_details
 * @author fang
 * @date 2020/7/23
 */

@Data
@Entity
public class MoneyDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 钱包明细id
     */
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户id
     */
    @Column
    private Long userId;

    /**
     * 类型 1增加 2减少
     */
    @Column
    private Integer type;

    /**
     * 分类 1结算 2积分 3任务 4提现
     */
    @Column
    private Integer classify;

    /**
     * 标题
     */
    @Column
    private String title;

    /**
     * 内容
     */
    @Column
    private String content;

    /**
     * 金额
     */
    @Column
    private Double money;

    /**
     * 时间
     */
    @Column
    private String createTime;


}
