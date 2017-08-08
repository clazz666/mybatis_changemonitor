package com.github.changemonitor;
import java.util.List;

import com.github.changemonitor.domain.ChangeData;

/**
 * @author AlexLu
 * @version CreateDate：2017年4月21日 上午10:39:47
 * Class Description 
 */
public abstract class AbstractMonitor {
	public abstract void  listen(List<ChangeData> changeTable);
}

