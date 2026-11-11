package com.sqx.domain.repository.service.captcha;

import java.awt.image.BufferedImage;

public interface SysCaptchaService {

    BufferedImage getCaptcha(String uuid);

    boolean validate(String uuid, String code);


}
