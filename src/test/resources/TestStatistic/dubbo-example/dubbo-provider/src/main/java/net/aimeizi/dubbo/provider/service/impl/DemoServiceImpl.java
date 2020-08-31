package net.aimeizi.dubbo.provider.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import net.aimeizi.dubbo.service.service.DemoService;

@Service
public class DemoServiceImpl implements DemoService {

	@Override
	public int getLength(String words) {
		if (words == null || words.isEmpty()) {
			return -1;
		} else {
			return words.length();
		}
	}
}
