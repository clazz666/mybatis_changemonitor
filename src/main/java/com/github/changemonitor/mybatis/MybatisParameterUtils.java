package com.github.changemonitor.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.mapping.StatementType;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;


public class MybatisParameterUtils {
	public static Map<String, Object> getParameter(MappedStatement mappedStatement, BoundSql boundSql, Object updateParameterObject){
		Configuration configuration = mappedStatement.getConfiguration();
		List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
		Map<String, Object> paramMap = new HashMap<>();
		if(mappedStatement.getStatementType() == StatementType.PREPARED){
			if (parameterMappings != null) {
			      for (int i = 0; i < parameterMappings.size(); i++) {
			    	  ParameterMapping parameterMapping = parameterMappings.get(i);
			    	  if (parameterMapping.getMode() != ParameterMode.OUT) {
			    		  Object value;
			              String propertyName = parameterMapping.getProperty();
			              
			              if (boundSql.hasAdditionalParameter(propertyName)) { 
			                  value = boundSql.getAdditionalParameter(propertyName);
			                } else if (updateParameterObject == null) {
			                  value = null;
			                } else if (mappedStatement.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(updateParameterObject.getClass())) {
			                  value = updateParameterObject;
			                } else {
			                  MetaObject metaObject = configuration.newMetaObject(updateParameterObject);
			                  value = metaObject.getValue(propertyName);
			                }
			              
			              paramMap.put(propertyName.toLowerCase(), value);
			    	  }
			      }
			}
		}
		return paramMap;
	}
}

