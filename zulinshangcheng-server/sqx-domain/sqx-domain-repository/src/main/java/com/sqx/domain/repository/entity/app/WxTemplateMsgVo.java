package com.sqx.domain.repository.entity.app;

import com.sqx.common.core.utils.TemplateParam;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/11/15 10:52
 */
@Data
public class WxTemplateMsgVo implements Serializable {

    private String touser;

    private String template_id;

    private String url;

    private Map<String, TemplateParam> data;

}
