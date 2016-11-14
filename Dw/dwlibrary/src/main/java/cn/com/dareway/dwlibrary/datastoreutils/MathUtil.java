package cn.com.dareway.dwlibrary.datastoreutils;

import java.math.BigDecimal;


public class MathUtil {

	/**
	 * 得到绝对值
	 *
	 * @param num
	 * @return
	 * @throws Exception
	 */
	public static double abs(double num) {
		if (num < 0) {
			return -1 * num;
		}
		return num;
	}
	/**
	 * 截取double指定位数函数
	 *
	 * @param
	 *            d 原double
	 * @param
	 *            i 小数位
	 * @return double
	 */
	public static double truncate(double d, int i) {
		double tmp = Math.pow(10, i);
		return Math.floor(d * tmp) / tmp;
	}

	/**
	 * 四舍五入函数
	 *
	 * @param
	 *            v 原double
	 * @param
	 *            scale 小数位
	 * @return double
	 * @throws Exception
	 */
	public static double round(double v, int scale) throws Exception {
		if (scale < 0) {
			throw new Exception(
					"The scale must be a positive integer or zero");
		}
		BigDecimal b = new BigDecimal(Double.toString(v));
		BigDecimal one = new BigDecimal("1");
		double i = b.divide(one, 10, BigDecimal.ROUND_HALF_UP).doubleValue();
		b = new BigDecimal(Double.toString(i));
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 判定一个字符串是不是数值，包括long ,double, 科学计数法
	 *
	 * @param numberString
	 * @return
	 */
	public static boolean isNumber(String numberString) {
		try {
			Double.parseDouble(numberString);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
}
