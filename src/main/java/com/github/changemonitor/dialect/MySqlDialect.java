package com.github.changemonitor.dialect;

/**
 *
 * @author sclu
 * @date 2017年8月15日 下午5:38:17
 */
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
