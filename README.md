项目为mybatis插件，通过此插件可监听对数据库的insert/delete/update操作。

**使用：**
**1. 新建一个监听类**
public class DataChangeMonitor extends AbstractMonitor {
	@Override
	public void listen(List<ChangeData> changeTable) {
		//TODO ...
	}
}

**2. 初始化mybatis时注册监听类**
@Bean
public DataChangeMonitor dataChangeMonitor(){
	return  new DataChangeMonitor();
}


@Bean
public ChangeMonitorInterceptor examplePlugin(@Autowired DataChangeMonitor dataChangeMonitor){
	ChangeMonitorInterceptor changeMonitorInterceptor =  new ChangeMonitorInterceptor();
	changeMonitorInterceptor.setMonitor(dataChangeMonitor);
	return changeMonitorInterceptor;
}


@Bean(name = "sqlSessionFactory")
public SqlSessionFactory sqlSessionFactoryBean(
	@Autowired DataSource dataSource, @Autowired ChangeMonitorInterceptor changeMonitorInterceptor) {
	try {
	SqlSessionFactoryBean sessionFactoryBean = new SqlSessionFactoryBean();
	sessionFactoryBean.setDataSource(dataSource);
	// 加载全局的配置文件
	sessionFactoryBean.setConfigLocation(new DefaultResourceLoader().getResource("classpath:mybatis-config.xml"));
	// 配置mapper的扫描，找到所有的mapper.xml映射文件
	Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml");
				sessionFactoryBean.setMapperLocations(resources);		
	// 添加changemonitor插件
	sessionFactoryBean.setPlugins(new Interceptor[]{changeMonitorInterceptor});
	return sessionFactoryBean.getObject();
	} catch (IOException e) {
		LOGGER.error("mybatis resolver mapper*xml is error", e);
		throw new RuntimeException(e);
	} catch (Exception e) {
        LOGGER.error("mybatis sqlSessionFactoryBean create error", e);
		throw new RuntimeException(e);
	}
}