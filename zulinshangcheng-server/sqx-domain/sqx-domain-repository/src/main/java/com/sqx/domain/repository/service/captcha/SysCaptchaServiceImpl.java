package com.sqx.domain.repository.service.captcha;

import com.google.code.kaptcha.Producer;
import com.sqx.common.core.utils.DateUtils;
import com.sqx.domain.repository.entity.sys.SysCaptcha;
import com.sqx.domain.repository.mapper.captcha.SysCaptchaRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Date;

@Service
public class SysCaptchaServiceImpl implements SysCaptchaService {

    @Autowired
    private Producer producer;
    @Autowired
    private SysCaptchaRepository sysCaptchaRepository;

    @Override
    public BufferedImage getCaptcha(String uuid) {
        if(StringUtils.isBlank(uuid)){
            return null;
        }
        //生成文字验证码
        String code = producer.createText();

        SysCaptcha captchaEntity = new SysCaptcha();
        captchaEntity.setUuid(uuid);
        captchaEntity.setCode(code);
        //5分钟后过期
        captchaEntity.setExpireTime(DateUtils.addMinutes(new Date(), 5));
        sysCaptchaRepository.save(captchaEntity);

        return producer.createImage(code);
    }

    @Override
    public boolean validate(String uuid, String code) {
        SysCaptcha captchaEntity = sysCaptchaRepository.findByUuid(uuid);
        if(captchaEntity == null){
            return false;
        }

        //删除验证码
        sysCaptchaRepository.deleteSysCaptchaByUuid(uuid);
        if(captchaEntity.getCode().equalsIgnoreCase(code) && captchaEntity.getExpireTime().getTime() >= System.currentTimeMillis()){
            return true;
        }

        return false;
    }

}
