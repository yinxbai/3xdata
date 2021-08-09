package com.txdata.common.utils;

import java.util.Random;

public class GenerateRandomUtil {

	/**
	 * 
	 * @Description: 生成指定位数随机数
	 *
	 * @param: row 要生成随机数的位数
	 * @return: 返回结果描述
	 * @throws: 异常描述
	 *
	 * @author: mark
	 * @date: 2019年1月2日 下午2:14:09
	 */
	public static String getRandNum(int row) {
		if (row < 1) {
			return "";
		}
		double pow = Math.pow(10, row);
		String randNum = new Random().nextInt((int)pow) + "";
		System.out.println("生成6位随机数：" + randNum);
		if (randNum.length() != row) { // 如果生成的不是6位数随机数则返回该方法继续生成
			return getRandNum(row);
		}
		return randNum;
	}
}
