package com.github.changemonitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

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
	private int maxRowMonitor;
	
	private final static int DEFAULT_MAXROW_MONITOR = 1000;
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
            		(Executor)target, dbDialect, maxRowMonitor);
            List<ChangeData> changeTable = parseContent.parse(commandName, mybatisInvocation);
            monitor.listen(changeTable);
            
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
		if(!properties.containsKey("dbname")){
			throw new RuntimeException("must set property dbname.");
		}
		String dbname = properties.getProperty("dbname");
		String maxRowMonitorString = properties.getProperty("maxRowMonitor");
		if(dbname == null || dbname.trim().length()<=0){
			throw new RuntimeException("must set property dbname.");
		}
		
		if(maxRowMonitorString == null || maxRowMonitorString.trim().length()<=0){
			maxRowMonitor = DEFAULT_MAXROW_MONITOR;
		}
		try {
			maxRowMonitor = Integer.getInteger(maxRowMonitorString);
		} catch (Exception e) {
			maxRowMonitor = DEFAULT_MAXROW_MONITOR;
		}
		if(dbDialectMap.containsKey(dbname)){
			dbDialect = dbDialectMap.get(dbname);
		}else {
			throw new IllegalArgumentException(String.format("Illegal dbName[%s]" , dbname));
		}
	}

	public AbstractMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(AbstractMonitor monitor) {
		this.monitor = monitor;
	}
}
