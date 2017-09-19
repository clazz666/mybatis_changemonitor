package com.github.changemonitor.dialect;


public class MySqlDialect extends AbstractDialect {
	@Override
	public String getLimitSql(String sql, Integer limitSize) {
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
		sqlBuilder.append(sql);
		sqlBuilder.append(" LIMIT ");
		sqlBuilder.append(limitSize);
		return sqlBuilder.toString();
	}

}
