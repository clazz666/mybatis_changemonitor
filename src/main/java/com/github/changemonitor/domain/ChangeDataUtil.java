package com.github.changemonitor.domain;

import java.util.*;
import java.util.Map.Entry;


public class ChangeDataUtil {

	public static ChangeData buildChangeDataForInsert(final Map<String, Object> afterDataMap) {
		ChangeData changeData = new ChangeData();
		List<ChangeObject> afterColumnList = new ArrayList<>();
		List<ChangeObject> beforeColumnList = new ArrayList<>();
		changeData.setAfterColumnList(afterColumnList);
		changeData.setBeforeColumnList(beforeColumnList);

		ChangeObject changeObject;
		for (Entry<String, Object> queryDataItem : afterDataMap.entrySet()) {
			String queryDataItemKey = queryDataItem.getKey();
			String queryDataItemValue = queryDataItem.getValue() != null ? queryDataItem.getValue().toString() : null;

			// set after change object
			changeObject = new ChangeObject();
			changeObject.setName(queryDataItemKey);
			changeObject.setValue(queryDataItemValue);
			afterColumnList.add(changeObject);
		}

		return changeData;
	}
	
	
	public static ChangeData buildChangeDataForDelete(final Map<String, Object> beforeDataMap) {
		ChangeData changeData = new ChangeData();
		List<ChangeObject> afterColumnList = new ArrayList<>();
		List<ChangeObject> beforeColumnList = new ArrayList<>();
		changeData.setAfterColumnList(afterColumnList);
		changeData.setBeforeColumnList(beforeColumnList);

		ChangeObject changeObject;
		for (Entry<String, Object> queryDataItem : beforeDataMap.entrySet()) {
			String queryDataItemKey = queryDataItem.getKey();
			String queryDataItemValue = queryDataItem.getValue() != null ? queryDataItem.getValue().toString() : null;

			// set after change object
			changeObject = new ChangeObject();
			changeObject.setName(queryDataItemKey);
			changeObject.setValue(queryDataItemValue);
			beforeColumnList.add(changeObject);
		}
		return changeData;
	}

	public static ChangeData buildChangeDataForUpdate(final Map<String, Object> changeDataMap, HashMap<String, Object> queryDataMap) {
		ChangeData changeData = new ChangeData();
		List<ChangeObject> afterColumnList = new ArrayList<>();
		List<ChangeObject> beforeColumnList = new ArrayList<>();
		changeData.setAfterColumnList(afterColumnList);
		changeData.setBeforeColumnList(beforeColumnList);
		if (queryDataMap == null) {
			return changeData;
		}
		ChangeObject changeObject;
		for (Entry<String, Object> queryDataItem : queryDataMap.entrySet()) {
			String queryDataItemKey = queryDataItem.getKey();
			String queryDataItemValue = queryDataItem.getValue() != null ? queryDataItem.getValue().toString() : null;

			// set before change object
			changeObject = new ChangeObject();
			changeObject.setName(queryDataItemKey);
			changeObject.setValue(queryDataItemValue);
			beforeColumnList.add(changeObject);

			// set after change object
			changeObject = new ChangeObject();
			changeObject.setName(queryDataItemKey);
			if (changeDataMap != null && changeDataMap.containsKey(queryDataItemKey.toLowerCase())) {
				String resultValueString = changeDataMap.get(
						queryDataItemKey.toLowerCase()) != null ? changeDataMap.get(queryDataItemKey.toLowerCase()).toString() : null;
				changeObject.setValue(resultValueString);
			} else {
				changeObject.setValue(queryDataItemValue);
			}
			afterColumnList.add(changeObject);
		}

		return changeData;
	}
}
