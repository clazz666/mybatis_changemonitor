package com.github.changemonitor.parse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Select;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.RowBounds;

import com.github.changemonitor.domain.ChangeData;
import com.github.changemonitor.domain.ChangeDataUtil;
import com.github.changemonitor.enumerate.DBActionTypeEnum;
import com.github.changemonitor.filter.FilterContext;
import com.github.changemonitor.mybatis.MSUtils;
import com.github.changemonitor.sql.JsqlParserHelper;
import com.github.changemonitor.sql.SqlParserInfo;

/**
 * @author AlexLu
 * @version CreateDate：2017年4月21日 上午10:08:17 Class Description
 */
public class ParseDeleteData implements ParseData {
	@Override
	public List<ChangeData> parse(String commandName, MybatisInvocation mybatisInvocation) throws Throwable {
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		BoundSql boundSql = mappedStatement.getBoundSql(mybatisInvocation.getParameter());
		String sql = boundSql.getSql();
		
		SqlParserInfo sqlParserInfo = new SqlParserInfo(sql, DBActionTypeEnum.DELETE);

		//校验过滤
		Boolean validateBoolean =  FilterContext.validate(sqlParserInfo.getTableName(), mybatisInvocation);
		if(!validateBoolean){
			return null;
		}
				
		// 获取要更新数据
		ArrayList<HashMap<String, Object>> beforeRsults = queryWillUpdateDatas(mybatisInvocation, boundSql, sqlParserInfo);

		List<ChangeData> results = buildChangeDatas(beforeRsults,sqlParserInfo);
		
		return results;
	}
	private List<ChangeData> buildChangeDatas(final ArrayList<HashMap<String, Object>> beforeResults, SqlParserInfo sqlParserInfo){
		List<ChangeData> changeDatas = new ArrayList<>();
		if(beforeResults != null && !beforeResults.isEmpty()){
			for(HashMap<String, Object> queryDataMap : beforeResults){
				ChangeData changeData = ChangeDataUtil.buildChangeDataForDelete(queryDataMap);
				changeData.setTableName(sqlParserInfo.getTableName());
				changeData.setSchemaName(sqlParserInfo.getSchemaName());
				changeDatas.add(changeData);
			}
		}
		return changeDatas;
	}
	
	private ArrayList<HashMap<String, Object>> queryWillUpdateDatas(MybatisInvocation mybatisInvocation, 
			BoundSql boundSql, SqlParserInfo sqlParserInfo) throws SQLException {
		Table table = sqlParserInfo.getTable();
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		MappedStatement selectMappedStatement = MSUtils.newHashMapMappedStatement(mappedStatement);
		List<Column> updateColumns = new ArrayList<>();
		Column column = new Column();
		column.setColumnName("*");
		updateColumns.add(column);
		Expression whereExpression = sqlParserInfo.getWhereExpression();

		Select select = JsqlParserHelper.getSelect(table, updateColumns, whereExpression);
		String selectSqlString = select.toString();
		
		// 查询数量限制
		selectSqlString = mybatisInvocation.getDbDialect().getLimitSql(selectSqlString, mybatisInvocation.getMaxRowMonitor());

		List<ParameterMapping> selectParamMap = new ArrayList<>();
		List<String> whereIdList = JsqlParserHelper.getWhereColumn(whereExpression);
		for (ParameterMapping paramMap : boundSql.getParameterMappings()) {
			if (paramMap.getProperty() != null && whereIdList.contains(paramMap.getProperty().toLowerCase())) {
				selectParamMap.add(paramMap);
				whereIdList.remove(paramMap.getProperty().toLowerCase());
			}
		}

		BoundSql queryBoundSql = new BoundSql(mybatisInvocation.getMappedStatement().getConfiguration(), selectSqlString, selectParamMap, mybatisInvocation.getParameter());
		Object queryResultList = mybatisInvocation.getExecutor().query(selectMappedStatement, mybatisInvocation.getParameter(), RowBounds.DEFAULT, null, null, queryBoundSql);
		@SuppressWarnings("unchecked")
		ArrayList<HashMap<String, Object>> queryResults = (ArrayList<HashMap<String, Object>>) queryResultList;
		return queryResults;
	}
}
