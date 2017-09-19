package com.github.changemonitor.parse;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

import com.github.changemonitor.dialect.AbstractDialect;


public class MybatisInvocation {
	private Object[] args ;
	private MappedStatement mappedStatement;
	private Object parameter;
	private Executor executor;
	private AbstractDialect dbDialect;
	private int maxRowMonitor;
	private CopyOnWriteArrayList<String> whiteCopyList;
	
	public MybatisInvocation(Object[] args, 
			MappedStatement mappedStatement, 
			Object parameter, Executor executor,
			AbstractDialect dbDialect,
			int maxRowMonitor,
			CopyOnWriteArrayList<String> whiteCopyList) {
		super();
		this.args = args;
		this.mappedStatement = mappedStatement;
		this.parameter = parameter;
		this.executor = executor;
		this.dbDialect = dbDialect;
		this.maxRowMonitor = maxRowMonitor;
		this.setWhiteCopyList(whiteCopyList);
	}
	
	public Object[] getArgs() {
		return args;
	}
	public void setArgs(Object[] args) {
		this.args = args;
	}
	public MappedStatement getMappedStatement() {
		return mappedStatement;
	}
	public void setMappedStatement(MappedStatement mappedStatement) {
		this.mappedStatement = mappedStatement;
	}
	public Object getParameter() {
		return parameter;
	}
	public void setParameter(Object parameter) {
		this.parameter = parameter;
	}
	public Executor getExecutor() {
		return executor;
	}
	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
	public AbstractDialect getDbDialect() {
		return dbDialect;
	}

	public void setDbDialect(AbstractDialect dbDialect) {
		this.dbDialect = dbDialect;
	}

	public int getMaxRowMonitor() {
		return maxRowMonitor;
	}

	public void setMaxRowMonitor(int maxRowMonitor) {
		this.maxRowMonitor = maxRowMonitor;
	}

	public CopyOnWriteArrayList<String> getWhiteCopyList() {
		return whiteCopyList;
	}

	public void setWhiteCopyList(CopyOnWriteArrayList<String> whiteCopyList) {
		this.whiteCopyList = whiteCopyList;
	}

}

