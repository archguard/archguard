package net.aimeizi.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import net.aimeizi.dubbo.service.entity.User;
import net.aimeizi.dubbo.service.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User save(User user) {
		user.setUserId(++UserIdGenerator.id);
		return user;
	}

}
