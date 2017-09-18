package com.github.changemonitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.github.changemonitor.dialect.AbstractDialect;
import com.github.changemonitor.dialect.Db2Dialect;
import com.github.changemonitor.dialect.MySqlDialect;
import com.github.changemonitor.dialect.OracleDialect;
import com.github.changemonitor.domain.ChangeData;
import com.github.changemonitor.enumerate.DBType;
import com.github.changemonitor.parse.MybatisInvocation;
import com.github.changemonitor.parse.ParseContext;


@Intercepts(value = { @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class ChangeMonitorInterceptor implements Interceptor {
	private AbstractMonitor monitor;
	private AbstractDialect dbDialect;

	private int maxRowMonitor = 1000;
	private final static String PROPERTY_DBNAME = "dbname";
	private final static String PROPERTY_MAX_ROW_MONITOR = "maxRowMonitor";
	private final static String PROPERTY_WHITE_LIST = "whiteList";
	
	CopyOnWriteArrayList<String> whiteCopyList = new CopyOnWriteArrayList<>();
	
	private static Map<String, AbstractDialect> dbDialectMap = new HashMap<>();
	static{
		dbDialectMap.put(DBType.MY_SQL.getValue(), new MySqlDialect());
		dbDialectMap.put(DBType.ORACLE.getValue(), new OracleDialect());
		dbDialectMap.put(DBType.DB2.getValue(), new Db2Dialect());
	}
	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		if(monitor == null){
			return null;
		}
		Object target = invocation.getTarget();

		Object result = null;
		if (target instanceof Executor) {
            final Object[] args = invocation.getArgs();  
            MappedStatement ms = (MappedStatement) args[0];  
            Object parameter = args[1];  
            String commandName = ms.getSqlCommandType().name();  

            ParseContext parseContent = new ParseContext();
            
            MybatisInvocation mybatisInvocation = new MybatisInvocation(args, ms, parameter, 
            		(Executor)target, dbDialect, maxRowMonitor,whiteCopyList);
            List<ChangeData> changeTable = parseContent.parse(commandName, mybatisInvocation);
            if(changeTable != null){
            	monitor.listen(changeTable);
            }
			 /**执行方法*/  
            result = invocation.proceed();
		}
		return result;
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}
	

	@Override
	public void setProperties(Properties properties) {
		if(!properties.containsKey(PROPERTY_DBNAME)){
			throw new RuntimeException("must set property dbname.");
		}
		String dbname = properties.getProperty(PROPERTY_DBNAME);
		String maxRowMonitorString = properties.getProperty(PROPERTY_MAX_ROW_MONITOR);
		if(isEmpty(dbname)){
			throw new RuntimeException("must set property dbname.");
		}
		
		if(!isEmpty(maxRowMonitorString)){
			try {
				maxRowMonitor = Integer.getInteger(maxRowMonitorString);
			} catch (Exception e) {
				throw new RuntimeException(String.format("Illegal maxRowMonitor : [%s] ", maxRowMonitorString));
			}
		}
		
		if(dbDialectMap.containsKey(dbname)){
			dbDialect = dbDialectMap.get(dbname);
		}else {
			throw new IllegalArgumentException(String.format("Illegal dbName[%s]" , dbname));
		}
		
		
		//set whiteList
		if(properties.containsKey(PROPERTY_WHITE_LIST)){
			String whiteList = properties.getProperty(PROPERTY_WHITE_LIST);
			if(!isEmpty(whiteList)){
				String[] whiteListArray= whiteList.split(",");
				whiteCopyList = new CopyOnWriteArrayList<>(whiteListArray);
			}
		}
	}
	
	
	public AbstractMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(AbstractMonitor monitor) {
		this.monitor = monitor;
	}
	private Boolean isEmpty(String str){
		if(str == null && str.length()<=0){
			return true;
		}else{
			return false;
		}
		
	}
}
