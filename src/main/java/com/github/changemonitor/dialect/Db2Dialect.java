package com.github.changemonitor.dialect;

/**
 *
 * @author sclu
 * @date 2017年8月15日 下午6:25:10
 */
public class Db2Dialect  extends AbstractDialect {
	@Override
	public String getLimitSql(String sql, Integer limitSize) {
		StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
        sqlBuilder.append("SELECT * FROM (SELECT TMP_PAGE.*,ROWNUMBER() OVER() AS ROW_ID FROM ( ");
        sqlBuilder.append(sql);
        sqlBuilder.append(" ) AS TMP_PAGE) WHERE ROW_ID BETWEEN ");
        sqlBuilder.append(1);
        sqlBuilder.append(" AND ");
        sqlBuilder.append(limitSize);
        return sqlBuilder.toString();
	}
}

