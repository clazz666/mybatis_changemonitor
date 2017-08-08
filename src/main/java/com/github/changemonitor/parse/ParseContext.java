package com.github.changemonitor.parse;
import java.util.List;

import com.github.changemonitor.domain.ChangeData;

/**
 * @author AlexLu
 * @version CreateDate：2017年4月21日 上午9:46:06
 * Class Description 
 */
public class ParseContext implements ParseData {
	@Override
	public List<ChangeData> parse(String commandName, MybatisInvocation mybatisInvocation)throws Throwable {
		ParseData parseData =  ParseFactory.getInstance().creator(commandName);
		return parseData.parse(commandName, mybatisInvocation);
	}
}

