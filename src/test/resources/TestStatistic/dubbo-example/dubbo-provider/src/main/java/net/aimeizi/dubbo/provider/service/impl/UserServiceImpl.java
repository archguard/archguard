package net.aimeizi.dubbo.provider.service.impl;

import net.aimeizi.dubbo.service.entity.User;
import net.aimeizi.dubbo.service.service.UserService;

import com.alibaba.dubbo.config.annotation.Service;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User save(User user) {
		user.setUserId(++UserIdGenerator.id);
		return user;
	}

}
