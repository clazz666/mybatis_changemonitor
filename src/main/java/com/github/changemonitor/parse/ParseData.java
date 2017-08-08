package com.github.changemonitor.parse;
import java.util.List;

import com.github.changemonitor.domain.ChangeData;

/**
 * @author AlexLu
 * @version CreateDate：2017年4月21日 上午9:43:39
 * Class Description 
 */
public interface ParseData {
	List<ChangeData> parse(String commandName,MybatisInvocation mybatisInvocation) throws Throwable;
}

