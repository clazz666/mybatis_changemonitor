package com.github.changemonitor.parse;
import java.util.List;

import com.github.changemonitor.domain.ChangeData;


public interface ParseData {
	List<ChangeData> parse(String commandName,MybatisInvocation mybatisInvocation) throws Throwable;
}

