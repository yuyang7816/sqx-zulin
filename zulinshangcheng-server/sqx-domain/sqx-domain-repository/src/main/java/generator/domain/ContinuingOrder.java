package generator.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName continuing_order
 */
@TableName(value ="continuing_order")
@Data
public class ContinuingOrder implements Serializable {
    /**
     * 继续租赁表id
     */
    @TableId
    private Integer id;

    /**
     * 订单id
     */
    private Integer orderId;

    /**
     * 起租日期
     */
    private Date startDate;

    /**
     * 结束租期
     */
    private Date endDate;

    /**
     * 租金
     */
    private BigDecimal leasePrice;

    /**
     * 租赁天数
     */
    private String leaseDay;

    /**
     * 租赁描述
     */
    private String remark;

    /**
     * 优惠金额
     */
    private BigDecimal discountMoney;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}