package com.sqx.domain.repository.service.app;


import com.sqx.common.core.domain.Result;

public interface IAppleService {
     Result getAppleUserInfo(String identityToken) throws Exception;

}
