package com.sqx.domain.repository.utils;

import com.sqx.domain.repository.entity.app.UserEntity;
import com.sqx.domain.repository.service.app.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 邀请码生成解密工具类
 * @author fang
 * @date 2020/7/8
 */
@Component
public class InvitationCodeUtil {

    private static UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        InvitationCodeUtil.userService = userService;
    }

    public static String toSerialCode() {
        //元素

        int[] array = {0,1,2,3,4,5,6,7,8,9};
        Random rand = new Random();
        while (true){
            for (int i = 10; i > 1; i--) {
                int index = rand.nextInt(i);
                int tmp = array[index];
                array[index] = array[i - 1];
                array[i - 1] = tmp;
            }
            int result = 0;
            for(int i = 0; i < 6; i++){
                result = result * 10 + array[i];
            }

            String sixString = Integer.toString(result);
            if (sixString.length() == 5) {
                sixString = "0" + sixString;

            }
            String str = sixString;
            UserEntity userEntity = userService.queryByInvitationCode(str);
            if(userEntity==null){
                return str;
            }
        }
    }





}