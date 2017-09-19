package com.github.changemonitor;
import java.util.List;

import com.github.changemonitor.domain.ChangeData;


public abstract class AbstractMonitor {
	public abstract void  listen(List<ChangeData> changeTable);
}

