package com.sqx.domain.repository.service.sys;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.core.domain.Result;
import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.entity.sys.MessageInfo;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;

public interface MessageService extends IService<MessageInfo> {

    IPage<MessageInfo> selectMessageList(Integer page, Integer limit,MessageInfo messageInfo);

    int saveBody(MessageInfo messageInfo);

    int update(MessageInfo messageInfo);

    int delete(Long id);

    MessageInfo selectMessageById(Long id);

    int updateSendState(Long userId);

    Result auditMessage(Long messageId, Integer status, String auditContent);

    int sendMessage(String content, String title, UserEntity userEntity);

    HashMap<String, Integer> getMessageSign(Long userId);

    MessageInfo getUserMessageInfoListLimit1(MessageInfo messageInfo);
    /**
     * 条件查询
     * <p>
     * 分页
     */
    Result find(String state,Pageable pageable);
}
