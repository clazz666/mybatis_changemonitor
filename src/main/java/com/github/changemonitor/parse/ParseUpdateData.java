package com.github.changemonitor.parse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.Select;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.RowBounds;

import com.github.changemonitor.domain.ChangeData;
import com.github.changemonitor.domain.ChangeDataUtil;
import com.github.changemonitor.enumerate.DBActionTypeEnum;
import com.github.changemonitor.mybatis.MSUtils;
import com.github.changemonitor.mybatis.MybatisParameterUtils;
import com.github.changemonitor.sql.JsqlParserHelper;
import com.github.changemonitor.sql.SqlParserInfo;

/**
 * @author AlexLu
 * @version CreateDate：2017年4月21日 上午9:44:07 Class Description
 */
public class ParseUpdateData implements ParseData {
	
	@Override
	public List<ChangeData> parse(String commandName, MybatisInvocation mybatisInvocation) throws Throwable {
		List<ChangeData> results = null;
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		Object updateParameterObject = mybatisInvocation.getParameter();
		BoundSql boundSql = mappedStatement.getBoundSql(mybatisInvocation.getParameter());
		String sql = boundSql.getSql();
		
		SqlParserInfo sqlParserInfo = new SqlParserInfo(sql, DBActionTypeEnum.UPDATE);
		
		//获取要更新数据
		ArrayList<HashMap<String, Object>> queryResults = queryWillUpdateDatas(mybatisInvocation,boundSql, sqlParserInfo);
		
		//获取更新字段列表
		Map<String, Object> updateDataMap = MybatisParameterUtils.getParameter(mappedStatement, boundSql, updateParameterObject);
		//组装变更列表
		results =  buildChangeDatas(updateDataMap, queryResults,sqlParserInfo);
		return results;
	}
	
	private ArrayList<HashMap<String, Object>> queryWillUpdateDatas(MybatisInvocation mybatisInvocation, 
			BoundSql boundSql, SqlParserInfo sqlParserInfo) throws SQLException{

		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		MappedStatement selectMappedStatement = MSUtils.newHashMapMappedStatement(mappedStatement);
		List<Column> updateColumns = new ArrayList<>();
		Column column = new Column();
		column.setColumnName("*");
		updateColumns.add(column);
		Expression whereExpression = sqlParserInfo.getWhereExpression();

		Select select = JsqlParserHelper.getSelect(sqlParserInfo.getTable(), updateColumns, whereExpression);
		String selectSqlString = select.toString();
		
		List<ParameterMapping> selectParamMap = new ArrayList<>();
		List<String> whereIdList =JsqlParserHelper.getWhereColumn(whereExpression);
		for (ParameterMapping paramMap : boundSql.getParameterMappings()) {
			if(paramMap.getProperty()!=null &&
					whereIdList.contains(paramMap.getProperty().toLowerCase())){
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
	
	private List<ChangeData> buildChangeDatas(final Map<String, Object> updateDataMap, 
			final ArrayList<HashMap<String, Object>> queryResults,SqlParserInfo sqlParserInfo){
		List<ChangeData> changeDatas = new ArrayList<>();
		if(queryResults != null && !queryResults.isEmpty()){
			for(HashMap<String, Object> queryDataMap : queryResults){
				ChangeData changeData = ChangeDataUtil.buildChangeDataForUpdate(updateDataMap,queryDataMap);
				changeData.setTableName(sqlParserInfo.getTableName());
				changeData.setSchemaName(sqlParserInfo.getSchemaName());
				changeData.setEventType(DBActionTypeEnum.UPDATE.getKey());
				changeDatas.add(changeData);
			}
		}
		return changeDatas;
	}

}
