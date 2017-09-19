package com.github.changemonitor.parse;
import java.util.List;

import com.github.changemonitor.domain.ChangeData;


public class ParseContext implements ParseData {
	@Override
	public List<ChangeData> parse(String commandName, MybatisInvocation mybatisInvocation)throws Throwable {
		ParseData parseData =  ParseFactory.getInstance().creator(commandName);
		return parseData.parse(commandName, mybatisInvocation);
	}
}

