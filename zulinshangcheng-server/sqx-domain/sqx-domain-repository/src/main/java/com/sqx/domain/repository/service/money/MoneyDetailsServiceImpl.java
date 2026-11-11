package com.sqx.domain.repository.service.money;

import com.sqx.domain.repository.entity.sys.MoneyDetails;
import com.sqx.domain.repository.mapper.money.MoneyDetailsJpaRepository;
import com.sqx.domain.repository.mapper.money.MoneyDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 钱包明细
 */
@Service
public class MoneyDetailsServiceImpl implements IMoneyDetailsService {

    private final MoneyDetailsJpaRepository moneyDetailsJpaRepository;
    private final MoneyDetailsRepository moneyDetailsRepository;


    @Autowired
    public MoneyDetailsServiceImpl(MoneyDetailsRepository moneyDetailsRepository, MoneyDetailsJpaRepository moneyDetailsJpaRepository) {
        this.moneyDetailsJpaRepository = moneyDetailsJpaRepository;
        this.moneyDetailsRepository = moneyDetailsRepository;
    }


    /**
     * 增加钱包明细
     * @param userId 用户id
     * @param type 分类 1增加 2减少
     * @param classify 类别 1结算 2积分 3任务 4提现
     * @param title 标题
     * @param content 内容
     * @param money 金额
     * @return 钱包明细
     */
    @Override
    public MoneyDetails saveBody(Long userId, Integer type, Integer classify, String title, String content, Double money){
        MoneyDetails moneyDetails=new MoneyDetails();
        moneyDetails.setUserId(userId);
        moneyDetails.setType(type);
        moneyDetails.setClassify(classify);
        moneyDetails.setTitle(title);
        moneyDetails.setContent(content);
        moneyDetails.setMoney(money);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        moneyDetails.setCreateTime(sdf.format(new Date()));
        return moneyDetailsJpaRepository.save(moneyDetails);
    }

    @Override
    public Page<MoneyDetails> selectMoneyDetailsByUserId(Pageable pageable,Long userId){
        return moneyDetailsRepository.selectMoneyDetailsByUserId(pageable,userId);
    }









}
