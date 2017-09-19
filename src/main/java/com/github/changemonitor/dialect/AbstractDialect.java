package com.github.changemonitor.dialect;


public abstract class AbstractDialect {
	public abstract String getLimitSql(String sql, Integer limitSize);
}

