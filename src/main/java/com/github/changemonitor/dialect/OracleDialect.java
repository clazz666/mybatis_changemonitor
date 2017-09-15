package com.github.changemonitor.dialect;

/**
 *
 * @author sclu
 * @date 2017年8月15日 下午6:20:04
 */
public class OracleDialect extends AbstractDialect {
	@Override
	public String getLimitSql(String sql, Integer limitSize) {
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
		sqlBuilder.append(" SELECT TMP_PAGE.*, ROWNUM ROW_ID FROM ( ");
		sqlBuilder.append(sql);
		sqlBuilder.append(" ) TMP_PAGE WHERE ROWNUM <= ");
		sqlBuilder.append(limitSize);
		return sqlBuilder.toString();
	}
}
