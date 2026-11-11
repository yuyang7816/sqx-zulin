package com.sqx.domain.repository.service.jifen;

import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.sys.JiFen;

public interface IJiFenService {

    Result findAll();

    Result saveBody(JiFen jiFen);

    Result findOne(long id);

    Result findList(long id);

    Result delete(long id);


    Result updateBody(JiFen jiFen);


}
