package com.sqx.domain.repository.service.money;

import com.sqx.domain.repository.entity.sys.MoneyDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IMoneyDetailsService {

    MoneyDetails saveBody(Long userId, Integer type, Integer classify, String title, String content, Double money);

    Page<MoneyDetails> selectMoneyDetailsByUserId(Pageable pageable, Long userId);

}
