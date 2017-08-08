package com.github.changemonitor;

import java.util.List;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;

import com.github.changemonitor.domain.ChangeData;
import com.github.changemonitor.parse.MybatisInvocation;
import com.github.changemonitor.parse.ParseContext;



@Intercepts(value = { @Signature(type = Executor.class, method = "update", args = { MappedStatement.class, Object.class }) })
public class ChangeMonitorInterceptor implements Interceptor {
	private AbstractMonitor monitor;

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
            
            MybatisInvocation mybatisInvocation = new MybatisInvocation(args,ms,parameter,(Executor)target);
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

	}

	public AbstractMonitor getMonitor() {
		return monitor;
	}

	public void setMonitor(AbstractMonitor monitor) {
		this.monitor = monitor;
	}


	
	
}
