# MyTomcat
	主要工作
		Servlet接口定义Servlet的核心功能
		Servlet不同的实现类处理动态的请求
		tomcat服务器类解析请求，响应数据
		静态请求直接通过路径拿静态资源，响应到客户端
		conf.properties配置文件
			把配置文件加载为一个properties对象，转化成一个hashmap
			解析的动态的请求作为hashmap的键去取相应处理请求的servlet类名
			拿到类名之后就通过反射创建servlet实例
			调用相应的servlet方法，完成响应
	使用的技术
		HTTP协议
		socket网络编程
		反射
    	IO
