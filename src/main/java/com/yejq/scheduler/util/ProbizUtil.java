package com.yejq.scheduler.util;

import java.io.File;
import java.util.ResourceBundle;

/**
 * 
 * @author zhuxinhua
 *资源文件获取
 */
public class ProbizUtil {

	//配置文件路径：spring/placeholder.properties
	public static final ResourceBundle conf = ResourceBundle.getBundle("spring"+File.separator+"placeholder");
	
	public static String getString(String key){
		try {
			return  conf.getString(key);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "";
	}
}
