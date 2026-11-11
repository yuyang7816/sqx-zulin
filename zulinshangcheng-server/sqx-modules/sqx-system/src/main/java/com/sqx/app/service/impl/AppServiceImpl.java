package com.sqx.app.service.impl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.app.service.AppService;
import com.sqx.domain.repository.entity.app.App;
import com.sqx.domain.repository.mapper.app.AppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("AppService")
public class AppServiceImpl extends ServiceImpl<AppDao, App> implements AppService {

	@Autowired
	private AppDao appDao;


	@Override
	public App selectAppById(Long id) {
		return appDao.selectById(id);
	}

	@Override
	public int insertApp(App app) {
		return appDao.insert(app);
	}

	@Override
	public int updateAppById(App app) {
		return appDao.updateById(app);
	}

	@Override
	public int deleteAppById(Long id) {
		return appDao.deleteById(id);
	}

	@Override
	public List<App> selectNewApp() {
		return appDao.selectNewApp();
	}
}
