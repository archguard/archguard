# dubbo-example

dubbo 分布式服务配置案例

升级到dubbox 2.8.4

dubbox 2.8.4编译安装

```
https://github.com/dangdangdotcom/dubbox/archive/dubbox-2.8.4.zip
修改根pom中curator_version版本为<curator_version>2.6.0</curator_version>
mvn install -Dmaven.test.skip=true
```

### 1. 项目结构介绍

dubbo-service 公共接口服务

dubbo-provider 公共接口服务实现(dubbo provider) 服务提供者

dubbo-consumer (dubbo consumer) dubbo服务消费者

### 2. 具体描述

* dubbo-service 为公共服务接口，在该模块中只声明对外提供的接口,在dubbo provider和 dubbo consumer均有引用

* dubbo-provider 公共接口服务实现,服务提供者.为dubbo consumer提供服务。

* 示例代码如下

```java
import User;
import UserService;

import com.alibaba.dubbo.config.annotation.Service;

@Service
public class UserServiceImpl implements UserService {

	@Override
	public User save(User user) {
		user.setUserId(++UserIdGenerator.id);
		return user;
	}

}
```
* dubbo provider 核心配置

```xml
<!-- 提供方应用信息，用于计算依赖关系 -->
<dubbo:application name="dubbo-provider" />
<!-- 使用zookeeper注册中心暴露服务地址 -->
<dubbo:registry address="zookeeper://127.0.0.1:2181" />
<!-- 用dubbo协议在20880端口暴露服务 -->
<dubbo:protocol name="dubbo" port="20880" />
<!-- 扫描注解包路径，多个包用逗号分隔，不填pacakge表示扫描当前ApplicationContext中所有的类 -->
<dubbo:annotation package="net.aimeizi.dubbo.service"/>
```
注:`<dubbo:annotation package="net.aimeizi.dubbo.service"/>`配置会扫描该包下的@Service(com.alibaba.dubbo.config.annotation.Service)注解. 这里的服务注入使用dubbo @Service注解

* maven依赖

```xml
<dependency>
    <groupId>net.aimeizi</groupId>
    <artifactId>dubbo-service</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>${spring.version}</version>
</dependency>

<dependency>
    <groupId>com.alibaba</groupId>
    <artifactId>dubbo</artifactId>
    <version>2.8.4</version>
    <exclusions>
	<exclusion>
	    <artifactId>spring</artifactId>
	    <groupId>org.springframework</groupId>
	</exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.apache.zookeeper</groupId>
    <artifactId>zookeeper</artifactId>
    <version>3.4.6</version>
    <exclusions>
	<exclusion>
	    <groupId>com.sun.jmx</groupId>
	    <artifactId>jmxri</artifactId>
	</exclusion>
	<exclusion>
	    <groupId>com.sun.jdmk</groupId>
	    <artifactId>jmxtools</artifactId>
	</exclusion>
	<exclusion>
	    <groupId>javax.jms</groupId>
	    <artifactId>jms</artifactId>
	</exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>com.github.sgroschupf</groupId>
    <artifactId>zkclient</artifactId>
    <version>0.1</version>
</dependency>
```

* dubbo-consumer 消费者.这里只依赖公共服务接口，不需要直接依赖dubbo provider

* 示例代码如下

```java
import com.alibaba.dubbo.config.annotation.Reference;
import DemoService;
import UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

/**
 *
 * dubbo 消费者
 *
 * @Reference 注解需要在 dubbo consumer中做如下配置
 *
 * <dubbo:annotation/>
 *	<context:component-scan base-package="net.aimeizi.dubbo.controller">
 *	<context:include-filter type="annotation" expression="com.alibaba.dubbo.config.annotation.Reference"/>
 * </context:component-scan>
 *
 * 若要使用@Autowired或@Resource注解需要显式声明bean
 *
 * 使用@Autowired或@Resource注解时需要使用dubbo:reference来声明
 * <dubbo:reference interface="UserService" id="userService"/>
 * <dubbo:reference interface="DemoService" id="demoService"/>
 *
 * 以上的配置均需要在spring mvc的DispatcherServlet配置中显式配置dubbo consumer的配置.如/WEB-INF/applicationContext-dubbo-consumer.xml 否则在Controller中服务报NullPointException
 * <servlet>
 *	<servlet-name>mvc-dispatcher</servlet-name>
 *		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
 *	<init-param>
 *	<param-name>contextConfigLocation</param-name>
 *		<param-value>/WEB-INF/applicationContext*.xml,/WEB-INF/mvc-dispatcher-servlet.xml</param-value>
 *	</init-param>
 *	<load-on-startup>1</load-on-startup>
 * </servlet>
 *
 */
@Controller
public class HelloController {

	@Reference
	//@Autowired
	//@Resource
	private DemoService demoService;

	@Reference
	//@Autowired
	//@Resource
	private UserService userService;

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String printWelcome(ModelMap model) {
		model.addAttribute("message", "Hello world!");
		return "hello";
	}
}
```

注意:

① @Reference 注解需要在 dubbo consumer配置文件中做如下配置
```xml
<dubbo:annotation/>
<context:component-scan base-package="net.aimeizi.dubbo.controller">
	<context:include-filter type="annotation" expression="com.alibaba.dubbo.config.annotation.Reference"/>
</context:component-scan>
```

② 若要使用@Autowired或@Resource注解需要显式声明bean

```xml
<!-- 使用@Resource注解时需要使用dubbo:reference来声明 -->
<dubbo:reference interface="UserService" id="userService"/>
<dubbo:reference interface="DemoService" id="demoService"/>
```

③ 以上的配置均需要在spring mvc的DispatcherServlet配置中显式配置dubbo consumer的配置.如/WEB-INF/applicationContext-dubbo-consumer.xml 否则在Controller中服务报NullPointException

```xml
<servlet>
	<servlet-name>mvc-dispatcher</servlet-name>
	<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
	<init-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>/WEB-INF/applicationContext*.xml,/WEB-INF/mvc-dispatcher-servlet.xml</param-value>
	</init-param>
	<load-on-startup>1</load-on-startup>
</servlet>
```

### 3. 演示

完整演示

![](Screenshots/1.gif)

错误演示Controller中服务报NullPointException

![](Screenshots/2.gif)

使用@Autowired或@Resource注解

![](Screenshots/3.gif)

dubbo管理控制台演示

![](Screenshots/dubbo.gif)


# 与我联系

* QQ:*184675420*

* Email:*sxyx2008#gmail.com*(#替换为@)

* HomePage:*[aimeizi.net](http://aimeizi.net)*

* Weibo:*[http://weibo.com/qq184675420](http://weibo.com/qq184675420)*(荧星诉语)

* Twitter:*[https://twitter.com/sxyx2008](https://twitter.com/sxyx2008)*


# License

MIT

Copyright (c) 2015 雪山飞鹄
