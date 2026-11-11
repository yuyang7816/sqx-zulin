package com.sqx.app.service;


import com.sqx.common.core.domain.Result;

public interface IAppleService {

        Result getAppleUserInfo(String identityToken) throws Exception;

}
