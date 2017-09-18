package com.github.changemonitor.parse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;

import com.github.changemonitor.domain.ChangeData;
import com.github.changemonitor.domain.ChangeDataUtil;
import com.github.changemonitor.enumerate.DBActionTypeEnum;
import com.github.changemonitor.filter.FilterContext;
import com.github.changemonitor.mybatis.MybatisParameterUtils;
import com.github.changemonitor.sql.SqlParserInfo;

/**
 * @author AlexLu
 * @version CreateDate：2017年4月21日 上午9:44:26 Class Description
 */
public class ParseInsertData implements ParseData {
	@Override
	public List<ChangeData> parse(String commandName, MybatisInvocation mybatisInvocation) throws Throwable {
		MappedStatement mappedStatement = mybatisInvocation.getMappedStatement();
		Object updateParameterObject = mybatisInvocation.getParameter();
		BoundSql boundSql = mappedStatement.getBoundSql(mybatisInvocation.getParameter());
		String sql = boundSql.getSql();
		SqlParserInfo sqlParserInfo = new SqlParserInfo(sql, DBActionTypeEnum.INSERT);

		//校验过滤
		Boolean validateBoolean =  FilterContext.validate(sqlParserInfo.getTableName(), mybatisInvocation);
		if(!validateBoolean){
			return null;
		}
				
		// 获取更新字段列表
		Map<String, Object> updateDataMap = MybatisParameterUtils.getParameter(mappedStatement, boundSql, updateParameterObject);
		
		List<ChangeData> changeDatas = new ArrayList<>();
		ChangeData changeDate = ChangeDataUtil.buildChangeDataForInsert(updateDataMap);
		changeDate.setTableName(sqlParserInfo.getTableName());
		changeDate.setSchemaName(sqlParserInfo.getSchemaName());
		
		changeDatas.add(changeDate);

		return changeDatas;
	}
}
