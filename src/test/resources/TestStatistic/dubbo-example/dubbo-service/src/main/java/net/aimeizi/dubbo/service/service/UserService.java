package net.aimeizi.dubbo.service.service;

import net.aimeizi.dubbo.service.entity.User;

public interface UserService {

	/**
	 * 保存User,计算出年龄
	 * @param user
	 * @return
	 */
	public User save(User user);
}
