package com.github.changemonitor.dialect;

/**
 *
 * @author sclu
 * @date 2017年8月15日 下午5:56:36
 */
public abstract class AbstractDialect {
	public abstract String getLimitSql(String sql, Integer limitSize);
}

