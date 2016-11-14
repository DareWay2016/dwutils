package cn.com.dareway.dwlibrary.datastoreutils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;

/**
 *
 * 说明：将各种数据转换成String的工具类
 *
 * @author 郑其荣 May 12, 2009
 */

public class DataFormat {

	private static final String NumberFormatPatterns = "##0";
	private static final char CHINESE_NUMBER[] = { '零', '壹', '贰', '叁', '肆',
			'伍', '陆', '柒', '捌', '玖' };
	private static final char CHINESE_CODE[] = { '分', '角', '整' };
	private static final char CHINESE_CARRY[] = { '元', '拾', '佰', '仟', '万', '拾',
			'佰', '仟', '亿', '拾', '佰', '仟', '兆', '拾', '佰', '仟', '万', '拾', '佰',
			'仟' };
	private static int maxMoneyLength = 16;

	/**
	 * 格式化字符. 如果字符位 null 转换为空格.
	 */

	public static String formatString(final String vString) {
		if (vString == null || vString.length() == 0) {
			return "";
		} else {
			return vString;
		}
	}

	/**
	 * 格式化对象.
	 */

	public static String formatString(final Object vObject) {
		if (vObject == null) {
			return "";
		} else {
			return formatString(vObject.toString());
		}
	}

	/**
	 * 格式化整数.
	 */

	public static String formatInt(final int vInt) {
		final java.text.NumberFormat DF = new DecimalFormat(
				NumberFormatPatterns);
		return (DF.format(vInt));
	}

	/**
	 * 格式化整数.
	 *
	 * @throws Exception
	 */

	public static String formatInt(final Integer vInt) throws Exception {
		if(vInt==null){
			throw new Exception("转换参数为空");
		}
		final java.text.NumberFormat DF = new DecimalFormat(
				NumberFormatPatterns);
		return (DF.format(vInt));
	}

	/**
	 * 格式化整数.
	 */
	public static String formatInt(final int vInt, final String vFormat)
			throws Exception {
		if (vFormat == null || vFormat.length() == 0) {
			return formatInt(vInt);
		} else {
			return (new DecimalFormat(vFormat)).format(vInt);
		}
	}

	/**
	 * 格式化long类型整数
	 */
	public static String formatLong(final long vLong) {
		final java.text.NumberFormat DF = new DecimalFormat(
				NumberFormatPatterns);
		return (DF.format(vLong));
	}

	/**
	 * 格式化long类型整数
	 *
	 * @throws Exception
	 */
	public static String formatLong(final Long vLong) throws Exception {
		if(vLong==null){
			throw new Exception("转换参数为空");
		}
		final java.text.NumberFormat DF = new DecimalFormat(
				NumberFormatPatterns);
		return (DF.format(vLong));
	}

	/**
	 * 格式化long类型整数
	 */
	public static String formatLong(final long vLong, final String vFormat)
			throws Exception {
		if (vFormat == null || vFormat.length() == 0) {
			return formatLong(vLong);
		} else {
			return (new DecimalFormat(vFormat)).format(vLong);
		}
	}

	/**
	 * 格式化数值. 保留两位小数.
	 *
	 * @throws Exception
	 */

	public static String formatDouble(final double vDouble) throws Exception {
		return formatDouble(vDouble, 2);
	}

	/**
	 * 数值格式化. 将double类型数字格式化为字符.
	 *
	 * @param
	 *            vDouble 要处理的数字.
	 * @param
	 *            n 处理以后的保留n位小数.
	 * @throws Exception
	 */

	public static String formatDouble(final double vDouble, final int n)
			throws Exception {
		return formatDouble(new Double(vDouble), n);
	}

	/**
	 * 数值格式化. 将String类型数字格式化为字符,保留两位小数.如果转换错误按字符输出.
	 *
	 * @throws Exception
	 */

	public static String formatDouble(final String vDouble) throws Exception {
		return formatDouble(vDouble, 2);
	}

	/**
	 * 数值格式化. 将String类型数字格式化为字符.如果转换错误按字符输出.
	 *
	 * @param
	 *            vDouble 要处理的数字.
	 * @param
	 *            n 处理以后的保留n位小数.
	 * @throws Exception
	 */

	public static String formatDouble(final String vDouble, final int n)
			throws Exception {
		try {
			return formatDouble(new Double(vDouble), n);
		} catch (NumberFormatException e) {
			throw new Exception("值[" + vDouble + "]不是一个String类型的数字!");
		}
	}

	/**
	 * 格式化数值.format格式参见java.text.DecimalFormat.
	 *
	 * @throws Exception
	 */

	public static String formatDouble(final double vDouble, final String vFormat)
			throws Exception {
		if (vFormat == null || vFormat.length() == 0) {
			return formatDouble(vDouble);
		} else {
			return (new DecimalFormat(vFormat)).format(vDouble);
		}
	}

	/**
	 * 格式化数值.format格式参见java.text.DecimalFormat.
	 *
	 * @throws Exception
	 */

	public static String formatDouble(final Double vDouble, final String vFormat)
			throws Exception {
		return formatDouble(vDouble.doubleValue(), vFormat);
	}

	/**
	 * 格式化数值.format格式参见java.text.DecimalFormat.
	 *
	 * @throws Exception
	 */

	public static String formatDouble(final String vDouble, final String vFormat)
			throws Exception {
		try {
			return formatDouble(new Double(vDouble), vFormat);
		} catch (final NumberFormatException e) {
			throw new Exception("值[" + vDouble + "]不是一个double类型的数字!");
		}
	}

	/**
	 * 格式化数值. 保留两位小数.
	 *
	 * @throws Exception
	 */

	public static String formatDouble(final Double vDouble) throws Exception {
		return formatDouble(vDouble, 2);
	}

	/**
	 * 格式化数值.
	 *
	 * @throws Exception
	 */

	public static String formatDouble(final Double vDouble, final int n)
			throws Exception {
		if (Double.isNaN(vDouble.doubleValue())) {
			throw new Exception("值[" + vDouble + "]不是一个的数字");
		}
		if (Double.isInfinite(vDouble.doubleValue())) {
			throw new Exception("值[" + vDouble + "]是一个无穷大的数字");
		}
		String f = NumberFormatPatterns;
		int i = 0;
		while (i < n) {
			f += (i++ == 0) ? ".0" : "0";
		}
		final DecimalFormat DF = new DecimalFormat(f);
		return (DF.format(vDouble.doubleValue()));
	}

	/**
	 * 1真,其他假.
	 *
	 * @param vInt
	 *            description.
	 * @return 是,否.
	 */

	public static String formatBoolean(final int vInt) {
		if (vInt == 1) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 转换字符串为文本. 将字符串 "1","true","yes","t" 转换为是，其他为否.
	 */

	public static String formatBoolean(final String vString) {
		if ("1".equals(vString) || "true".equalsIgnoreCase(vString)
				|| "yes".equalsIgnoreCase(vString)
				|| "y".equalsIgnoreCase(vString)) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 将Boolean对象转换为字符.
	 *
	 * @param vBool .
	 * @return true:是，false：否.
	 */

	public static String formatBoolean(final Boolean vBool) {
		if (vBool.booleanValue()) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 将boolean转换为字符
	 */

	public static String formatBoolean(final boolean vBool) {
		if (vBool) {
			return "true";
		} else {
			return "false";
		}
	}

	/**
	 * 将数值型转换为字符串
	 *
	 * @param vDouble
	 * @param vFormat
	 * @return
	 * @throws Exception
	 */
	public static String formatNumber(final double vDouble, final String vFormat)
			throws Exception {
		return formatDouble(vDouble, vFormat);
	}

	/**
	 * 将数值型转换为字符串
	 *
	 * @param vDouble
	 * @return
	 * @throws Exception
	 */
	public static String formatNumber(final double vDouble) throws Exception {
		return formatDouble(vDouble);
	}

	/**
	 * 将数值型转换为字符串
	 *
	 * @param vInt
	 * @return
	 * @throws Exception
	 */
	public static String formatNumber(final int vInt) throws Exception {
		return formatInt(vInt);
	}

	/**
	 * 将数值型转换为字符串
	 *
	 * @param vInt
	 * @param vFormat
	 * @return
	 * @throws Exception
	 */
	public static String formatNumber(final int vInt, final String vFormat)
			throws Exception {
		return formatInt(vInt, vFormat);
	}

	/**
	 * 将数字表示为汉字的大写金额.
	 *
	 * <pre>
	 * 	  -00   -9999999999999999     9999999999999999   +00
	 * 	   |           |                    |             |
	 *
	 * </pre>
	 *
	 * 如果 money > 9999999999999999 || money < -9999999999999999 ,数字太大，以用数字表示. 如果
	 * money <= 9999999999999999 && money >= -9999999999999999 ,正常的转换为汉字大大写金额.
	 *
	 * @throws Exception
	 */

	public static String numberToChinese(final long vMoney) throws Exception {
		final int leng = (CHINESE_CARRY.length > maxMoneyLength) ? maxMoneyLength
				: CHINESE_CARRY.length;
		// 数字合适，转换
		if (Math.abs(vMoney) < Math.pow(10, leng)) {
			final DecimalFormat DF = new DecimalFormat(
					"#.00");
			final String numStr = DF.format(vMoney);
			return numberToChinese(numStr);
		} else {
			throw new Exception("值[" + vMoney + "]不是一个合法的表示钱的数字");
		}
	}

	/**
	 *
	 * 允许转换的数字的最大长度是16,即数字最大允许9999999999999999.
	 * double不丢失精度可以存储的long类型最大数是18014398509481980.
	 *
	 * 将数字表示为汉字的大写金额.
	 *
	 * <pre>
	 * 	  -00   -9999999999999999     9999999999999999   +00
	 * 	   |           |                    |             |
	 * </pre>
	 *
	 * 如果 money > 9999999999999999 || money < -9999999999999999 ,数字太大，以科学计数法表示.
	 * 如果 money <= 9999999999999999 && money >= -9999999999999999
	 * ,正常的转换为汉字大大写金额.
	 *
	 * @throws Exception
	 */

	public static String numberToChinese(final double vMoney)
			throws Exception {
		int leng = (CHINESE_CARRY.length > maxMoneyLength) ? maxMoneyLength
				: CHINESE_CARRY.length;
		// 数字合适，转换
		if (Math.abs(vMoney) < Math.pow(10, leng)) {
			DecimalFormat DF = new DecimalFormat("#.00");
			String numStr = DF.format(vMoney);
			return numberToChinese(numStr);
		} else {
			throw new Exception("值[" + vMoney + "]不是一个合法的表示钱的数字");
		}
	}

	private static String numberToChinese(final String vMoneyString)
			throws Exception {
		String intStr = vMoneyString.substring(0, vMoneyString.length() - 3);
		final String decStr = vMoneyString.substring(vMoneyString.length() - 2);
		final StringBuffer moneyStr = new StringBuffer();
		// 处理负数
		if (intStr.indexOf("-") != -1) {
			moneyStr.append("退：");
			intStr = intStr.substring(intStr.indexOf("-") + 1);
		}

		// 处理整数部分
		boolean hasZero = false;
		boolean hasNumber = true;
		char oneNum;
		final int intLen = intStr.length();
		int zeroCount = 0; // 处理多个连续0.
		for (int i = 0; i < intLen; i++) {
			oneNum = intStr.charAt(i);
			if (oneNum == '0') {
				zeroCount++;
			} else {
				zeroCount = 0;
			}
			if ((intLen - i - 1) % 4 == 0 && oneNum == '0') {
				if (hasNumber && zeroCount <= 4) {
					if (zeroCount != 4 || intLen == i + 1)
						moneyStr.append(CHINESE_CARRY[intLen - i - 1]);
					hasNumber = false;
				}
				hasZero = false;
				continue;
			}
			if (oneNum > '0' && oneNum <= '9') {
				hasNumber = true;
				if (hasZero) {
					moneyStr.append(CHINESE_NUMBER[0]);
				}
				moneyStr.append(CHINESE_NUMBER[oneNum - '0']);
				moneyStr.append(CHINESE_CARRY[intLen - i - 1]);
				hasZero = false;
			} else {
				hasZero = true;
			}
		}
		// 处理小数部分
		final int decLen = decStr.length();
		hasZero = false;
		if (decStr.equals("00")) {
			if (intLen == 0)
				moneyStr.append("" + CHINESE_NUMBER[0] + CHINESE_CARRY[0]);
			moneyStr.append(CHINESE_CODE[2]);
		} else {
			for (int i = 0; i < decLen; i++) {
				oneNum = decStr.charAt(i);
				if (oneNum > '0' && oneNum <= '9') {
					if (hasZero) {
						moneyStr.append(CHINESE_NUMBER[0]);
						hasZero = false;
					}
					moneyStr.append(CHINESE_NUMBER[oneNum - '0']);
					moneyStr.append(CHINESE_CODE[decLen - i - 1]);
				} else {
					hasZero = true;
				}
			}
		}
		return moneyStr.toString();
	}

	/**
	 * 根据格式化样式mask，格式化数字类型的变量
	 *
	 * @param value
	 * @param mask
	 * @return
	 * @throws Exception
	 */
	public static String formatValue(Object value, String mask) throws Exception {
		if (value == null) {
			return "";
		}
		if(value.toString().equalsIgnoreCase("")){
			return "";
		}
		if (mask == null || mask.equals("")) {
			return formatValueWithoutMask(value);
		}
		if ("date".equalsIgnoreCase(mask)&& value instanceof Date) {
			return DateUtil.FormatDate((Date) value,"yyyy-MM-dd");
		}else if (mask.indexOf("#")!=-1&&mask.indexOf(".")==-1) {// 判断是否是输入数字类型的掩码，判断依据是mask中是否有"#."
			if(mask.indexOf("+")>-1){
				mask=mask.replace("+", "");
			}
			if (value instanceof BigDecimal) { // 处理BigDecimal类型
				return DataFormat.formatInt(((BigDecimal)value).intValue(),mask);
			}
			if (value instanceof Long) {// 处理Long类型
				return DataFormat.formatLong(((Long)value).longValue(),mask);
			}
			if (value instanceof Integer) {// 处理Integer类型
				return DataFormat.formatInt(((Integer)value).intValue(),mask);
			}
			if (value instanceof Double) {// 处理Double类型
				return DataFormat.formatLong(new BigDecimal(((Double)value).doubleValue()).longValue(),mask);
			}
			if (value instanceof String) {// 处理String类型
				if (value.equals("")) {
					return "";
				}
				return value.toString();//zqr修改，当值为String时，不对数值进行设置
			}
		} else if(mask.indexOf("#")!=-1&&mask.indexOf(".")!=-1){
			if(mask.indexOf("+")>-1){
				mask=mask.replace("+", "");
			}
			if (value instanceof BigDecimal) { // 处理BigDecimal类型
				return DataFormat.formatDouble(((BigDecimal) value)
						.doubleValue(),mask);
			}
			if (value instanceof Long) {// 处理Long类型
				return DataFormat.formatDouble(
						((Long) value).doubleValue(),mask);
			}
			if (value instanceof Integer) {// 处理Integer类型
				return DataFormat.formatDouble(((Integer) value)
						.doubleValue(),mask);
			}
			if (value instanceof Double) {// 处理Double类型
				return DataFormat.formatDouble(((Double) value)
						.doubleValue(),mask);
			}
			if (value instanceof String) {// 处理String类型
				if (value.equals("")) {
					return "";
				}
//				return DataFormat.formatDouble((Double
//						.valueOf((String) value)).doubleValue(),mask);
				return value.toString();
			}
		} else if(value instanceof Date){
			return DateUtil.FormatDate((Date)value,mask);
		}else{
			return value.toString();
		}
		return value.toString();
	}

	/**
	 * 格式化没有掩码参数的变量 *
	 *
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static String formatValueWithoutMask(Object value) throws Exception {
		if (value instanceof BigDecimal || value instanceof Double) { // 处理BigDecimal类型Double类型
			return formatValue(value, NumberFormatPatterns+".00");
		}
		if (value instanceof Long || value instanceof Integer) {// 处理Long类型Integer类型
			return formatValue(value,NumberFormatPatterns);
		}
		if (value instanceof Date || value instanceof java.sql.Date) {// 处理Date类型
			return DateUtil.FormatDate((Date)value,"yyyy-MM-dd");
		}
		value=DataFormat.stripNonValidXMLCharacters(value.toString());
		return value.toString();
	}

	public static String filterSpecialChars(char in) {
		switch (in) {
			case '&':
				return "&amp;";
			case '<':
				return "&lt;";
			case '>':
				return "&gt;";
			case '\'':
				return "&#39;";
			case '\"':
				return "&quot;";
			case '\n':
				return "&#xA;";
			case '\r':
				return "&#xD;";
			case '\t':
				return "&#x9;";
			default:
				return String.valueOf(in);
		}
	}

	public static Object stripNonValidXMLCharacters(String in) {
		StringBuffer out = new StringBuffer();
		char current;

		if (in == null || ("".equals(in)))
			return "";
		for (int i = 0; i < in.length(); i++) {
			current = in.charAt(i);
			if ((current == 0x9) || (current == 0xA) || (current == 0xD)
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF))) {
				out.append(filterSpecialChars(current));
			}
		}
		return out.toString();
	}
}
