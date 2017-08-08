package com.github.changemonitor.parse;
import java.util.*;

import com.github.changemonitor.enumerate.DBActionTypeEnum;

/**
 * @author AlexLu
 * @version CreateDate：2017年4月21日 上午10:05:02
 * Class Description 
 */
public class ParseFactory {
	private ParseFactory() {}
	
	 private static ParseFactory factory = new ParseFactory();
	 private static Map<String, ParseData> parseMap = new HashMap<>();
	 

	 static{
		 parseMap.put(DBActionTypeEnum.UPDATE.getValue(), new ParseUpdateData());
		 parseMap.put(DBActionTypeEnum.INSERT.getValue(), new ParseInsertData());
		 parseMap.put(DBActionTypeEnum.DELETE.getValue(), new ParseDeleteData());
	 }
	 
	 public static ParseFactory getInstance(){
	       return factory;
	    }
	 
	 public ParseData creator(String commandName){
	       return parseMap.get(commandName);
	    }
}

