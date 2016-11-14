package cn.com.dareway.dwlibrary.datastoreutils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;


public class DateUtil {

	/**
	 * 将传入的Date型参数增加天数
	 *
	 * @param date
	 * @param  dayNumber 增加月数，可以是负数
	 * @return Date
	 * @throws Exception
	 */
	public static Date addDay(Date date, int dayNumber) throws Exception {
		if (date == null || dayNumber == 0)
			return date;
		Calendar vcal = Calendar.getInstance();
		vcal.setTime(date);
		vcal.add(Calendar.DATE, dayNumber);
		date = vcal.getTime();
		return date;
	}

	/**
	 * 将传入的String型参数增加天数，按照格式区分年月
	 *
	 * @param dateString
	 * @param format :可以是6位yyyyMM或者8位yyyyMMdd,
	 * @param  dayNumber 增加月数，可以是负数
	 * @return String
	 */
	public static String addDayToString(String dateString, String format,
										int dayNumber) throws Exception {
		Date vdate = stringToDate(dateString, format);
		vdate = addDay(vdate, dayNumber);
		String vdates = dateToString(vdate, format);
		return vdates;
	}

	/**
	 * 将传入的Date型参数增加月数
	 *
	 * @param date
	 * @param  monthNumber 增加月数，可以是负数
	 * @return Date
	 * @throws Exception
	 */
	public static Date addMonth(Date date, int monthNumber) throws Exception {
		if (date == null || monthNumber == 0)
			return date;
		Calendar vcal = Calendar.getInstance();
		vcal.setTime(date);
		vcal.add(Calendar.MONTH, monthNumber);
		date = vcal.getTime();
		return date;
	}

	/**
	 * 将传入的String型参数增加月数，按照格式区分年月
	 *
	 * @param dateString
	 *
	 * @param format:可以是6位yyyyMM或者8位yyyyMMdd,
	 * @param  monthNumber 增加月数，可以是负数
	 * @return String
	 */
	public static String addMonthToString(String dateString, String format,
										  int monthNumber) throws Exception {
		Date vdate = stringToDate(dateString, format);
		vdate = addMonth(vdate, monthNumber);
		String vdates = dateToString(vdate, format);
		return vdates;
	}

	/**
	 * 将日期转化为字符串. 返回 8 位长度 yyyyMMdd
	 *
	 * @param date
	 * @return String 8 位
	 * @throws Exception
	 */
	public static String dateToString(Date date) throws Exception {
		return dateToString(date, "yyyyMMdd");
	}

	/**
	 * 将日期转化为字符串. 返回 14 位长度 yyyyMMddHHmiss 具体到秒
	 *
	 * @param date
	 * @return String 14位
	 * @throws Exception
	 */
	// 3.9新增
	public static String dateTimeToString(Date date) throws Exception {
		return dateToString(date, "yyyyMMddHHmmss");
	}

	public static String FormatDate(Date date) throws Exception {
		return dateToString(date, "yyyyMMddHHmmss");
	}

	/**
	 * 说明：把时间格式成字符串
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */
	public static String FormatDate(Date date, String format)
			throws Exception {
		return dateToString(date, format);
	}

	public static String dateToString(Date date, String format)
			throws Exception {
		if (date == null)
			return null;

		if (format == null || format.equalsIgnoreCase("")) {
			throw new Exception("传入参数中的[时间格式]为空");
		}
		Hashtable<Integer, String> h = new Hashtable<Integer, String>();
		String javaFormat = new String();
		if (format.indexOf("yyyy") != -1)
			h.put(new Integer(format.indexOf("yyyy")), "yyyy");
		else if (format.indexOf("yy") != -1)
			h.put(new Integer(format.indexOf("yy")), "yy");
		if (format.indexOf("MM") != -1)
			h.put(new Integer(format.indexOf("MM")), "MM");
		else if (format.indexOf("mm") != -1)
			h.put(new Integer(format.indexOf("mm")), "MM");
		if (format.indexOf("dd") != -1)
			h.put(new Integer(format.indexOf("dd")), "dd");
		if (format.indexOf("hh24") != -1)
			h.put(new Integer(format.indexOf("hh24")), "HH");
		else if (format.indexOf("hh") != -1) {
			h.put(new Integer(format.indexOf("hh")), "HH");
		} else if (format.indexOf("HH") != -1) {
			h.put(new Integer(format.indexOf("HH")), "HH");
		}
		if (format.indexOf("mi") != -1)
			h.put(new Integer(format.indexOf("mi")), "mm");
		else if (format.indexOf("mm") != -1 && h.containsValue("HH"))
			h.put(new Integer(format.lastIndexOf("mm")), "mm");
		if (format.indexOf("ss") != -1)
			h.put(new Integer(format.indexOf("ss")), "ss");
		if (format.indexOf("SSS") != -1)
			h.put(new Integer(format.indexOf("SSS")), "SSS");

		for (int intStart = 0; format.indexOf("-", intStart) != -1; intStart++) {
			intStart = format.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}
		for (int intStart = 0; format.indexOf(".", intStart) != -1; intStart++) {
			intStart = format.indexOf(".", intStart);
			h.put(new Integer(intStart), ".");
		}
		for (int intStart = 0; format.indexOf("/", intStart) != -1; intStart++) {
			intStart = format.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}

		for (int intStart = 0; format.indexOf(" ", intStart) != -1; intStart++) {
			intStart = format.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}

		for (int intStart = 0; format.indexOf(":", intStart) != -1; intStart++) {
			intStart = format.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}

		if (format.indexOf("年") != -1)
			h.put(new Integer(format.indexOf("年")), "年");
		if (format.indexOf("月") != -1)
			h.put(new Integer(format.indexOf("月")), "月");
		if (format.indexOf("日") != -1)
			h.put(new Integer(format.indexOf("日")), "日");
		if (format.indexOf("时") != -1)
			h.put(new Integer(format.indexOf("时")), "时");
		if (format.indexOf("分") != -1)
			h.put(new Integer(format.indexOf("分")), "分");
		if (format.indexOf("秒") != -1)
			h.put(new Integer(format.indexOf("秒")), "秒");
		int i = 0;
		while (h.size() != 0) {
			Enumeration<Integer> e = h.keys();
			int n = 0;
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n)
					n = i;
			}
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = temp + javaFormat;
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat,
				new DateFormatSymbols());
		return df.format(date);
	}

	/**
	 * 说明：把时间减去一个月
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public static String descreaseYearMonth(String dateString)
			throws Exception {
		if (dateString == null)
			return null;
		if (dateString.length() != 6)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		int year = (new Integer(dateString.substring(0, 4))).intValue();
		int month = (new Integer(dateString.substring(4, 6))).intValue();
		if (--month >= 10)
			return dateString.substring(0, 4) + (new Integer(month)).toString();
		if (month > 0 && month < 10)
			return dateString.substring(0, 4) + "0"
					+ (new Integer(month)).toString();
		else
			return (new Integer(year - 1)).toString()
					+ (new Integer(month + 12)).toString();
	}

	public static String descreaseYearMonth(String dateString, int delMonth)
			throws Exception {
		if (dateString == null)
			return null;
		if (dateString.length() != 6)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		int year = (new Integer(dateString.substring(0, 4))).intValue();
		int month = (new Integer(dateString.substring(4, 6))).intValue();

		if (delMonth < 0) {
			return DateUtil.increaseYearMonth(dateString, (-1) * delMonth);
		}
		month = month - delMonth;
		if (month >= 10)
			return dateString.substring(0, 4) + (new Integer(month)).toString();
		if (month > 0 && month < 10) {
			return dateString.substring(0, 4) + "0"
					+ (new Integer(month)).toString();
		} else {
			int yearDec = (-1) * month / 12 + 1;
			int month2 = 12 - ((-1) * month % 12);
			if (month2 >= 10) {
				return (new Integer(year - yearDec)).toString()
						+ new Integer(month2).toString();
			} else {
				return (new Integer(year - yearDec)).toString() + "0"
						+ new Integer(month2).toString();
			}
		}
	}

	/**
	 * 说明：把时间转成格式化成年
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static int FormatDateToYear(Date date) throws Exception {
		if (date == null)
			throw new Exception("传入参数中的[时间]为空");
		return Integer.parseInt(FormatDate(date, "yyyy"));
	}

	/**
	 * 说明：把时间转成格式化成yyyyMM
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String FormatDateToYearMonth(Date date) throws Exception {
		if (date == null)
			return null;
		return FormatDate(date, "yyyyMM");
	}

	/**
	 * 说明：把时间转成格式化成yyyyMMdd
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String FormatDateToYearMonthDay(Date date)
			throws Exception {
		if (date == null)
			return null;
		return FormatDate(date, "yyyyMMdd");
	}

	/**
	 * 说明：得到汉字的日期
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static String getChineseDate(Date date) throws Exception {
		if (date == null)
			return null;
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd",
				new DateFormatSymbols());
		String dtrDate = df.format(date);
		return dtrDate.substring(0, 4) + "年"
				+ Integer.parseInt(dtrDate.substring(4, 6)) + "月"
				+ Integer.parseInt(dtrDate.substring(6, 8)) + "日";

	}

	/**
	 * 说明：根据时间得到中文年月
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public static String getChineseYearAndMonth(String dateString)
			throws Exception {

		if (dateString == null)
			return null;
		if (dateString.length() != 6)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		String year = dateString.substring(0, 4);
		String month = dateString.substring(4, 6);
		return year + "年" + month + "月";
	}

	/**
	 * 说明：得到当前的时间
	 *
	 * @author:郑其荣 May 13, 2009
	 * @return
	 */
	public static Date getCurrentDate() {
		Calendar cal = Calendar.getInstance();
		return cal.getTime();
	}

	/**
	 * 说明：得到当前的时间
	 *
	 * @author:郑其荣 May 13, 2009
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentDateToString() throws Exception {
		return getCurrentDateToString("yyyyMMddHHmiss");
	}

	/**
	 * 说明：得到当前的时间
	 *
	 * @author:郑其荣 May 13, 2009
	 * @return
	 * @throws Exception
	 */
	public static String getCurrentDateToString(String strFormat)
			throws Exception {
		return dateToString(getCurrentDate(), strFormat);
	}

	/**
	 * 说明：获取当前是那一年
	 *
	 * @author:郑其荣 May 13, 2009
	 * @return
	 */
	public static int getCurrentYear() {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.YEAR);
	}

	/**
	 * 说明：得到当前时间的年月值yyyyMM
	 *
	 * @author:郑其荣 May 13, 2009
	 * @return
	 */
	public static String getCurrentYearMonthToString() {
		Calendar cal = Calendar.getInstance();
		String currentYear = (new Integer(cal.get(Calendar.YEAR))).toString();
		String currentMonth = null;
		if (cal.get(Calendar.MONTH) < 9)
			currentMonth = "0" + (new Integer(cal.get(2) + 1)).toString();
		else
			currentMonth = (new Integer(cal.get(2) + 1)).toString();
		return currentYear + currentMonth;
	}

	/**
	 * 说明：获取当前时间
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @param format
	 * @return
	 * @throws Exception
	 */

	public static String getDate(Date date, String format) throws Exception {
		if (date == null)
			return null;
		if (format == null || format.equalsIgnoreCase(""))
			throw new Exception("传入参数中的[时间格式]为空");
		Hashtable<Integer, String> h = new Hashtable<Integer, String>();
		String javaFormat = new String();
		if (format.indexOf("yyyy") != -1)
			h.put(new Integer(format.indexOf("yyyy")), "yyyy");
		else if (format.indexOf("yy") != -1)
			h.put(new Integer(format.indexOf("yy")), "yy");
		if (format.indexOf("MM") != -1)
			h.put(new Integer(format.indexOf("MM")), "MM");
		else if (format.indexOf("mm") != -1)
			h.put(new Integer(format.indexOf("mm")), "MM");
		if (format.indexOf("dd") != -1)
			h.put(new Integer(format.indexOf("dd")), "dd");
		if (format.indexOf("hh24") != -1)
			h.put(new Integer(format.indexOf("hh24")), "HH");
		else if (format.indexOf("hh") != -1) {
			h.put(new Integer(format.indexOf("hh")), "HH");
		} else if (format.indexOf("HH") != -1) {
			h.put(new Integer(format.indexOf("HH")), "HH");
		}
		if (format.indexOf("mi") != -1)
			h.put(new Integer(format.indexOf("mi")), "mm");
		else if (format.indexOf("mm") != -1)
			h.put(new Integer(format.indexOf("mm")), "MM");
		if (format.indexOf("ss") != -1)
			h.put(new Integer(format.indexOf("ss")), "ss");
		if (format.indexOf("SSS") != -1)
			h.put(new Integer(format.indexOf("SSS")), "SSS");
		for (int intStart = 0; format.indexOf("-", intStart) != -1; intStart++) {
			intStart = format.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}
		for (int intStart = 0; format.indexOf(".", intStart) != -1; intStart++) {
			intStart = format.indexOf(".", intStart);
			h.put(new Integer(intStart), ".");
		}
		for (int intStart = 0; format.indexOf("/", intStart) != -1; intStart++) {
			intStart = format.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}

		for (int intStart = 0; format.indexOf(" ", intStart) != -1; intStart++) {
			intStart = format.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}

		for (int intStart = 0; format.indexOf(":", intStart) != -1; intStart++) {
			intStart = format.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}

		if (format.indexOf("年") != -1)
			h.put(new Integer(format.indexOf("年")), "年");
		if (format.indexOf("月") != -1)
			h.put(new Integer(format.indexOf("月")), "月");
		if (format.indexOf("日") != -1)
			h.put(new Integer(format.indexOf("日")), "日");
		if (format.indexOf("时") != -1)
			h.put(new Integer(format.indexOf("时")), "时");
		if (format.indexOf("分") != -1)
			h.put(new Integer(format.indexOf("分")), "分");
		if (format.indexOf("秒") != -1)
			h.put(new Integer(format.indexOf("秒")), "秒");
		int i = 0;
		while (h.size() != 0) {
			Enumeration<Integer> e = h.keys();
			int n = 0;
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n)
					n = i;
			}
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = temp + javaFormat;
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat,
				new DateFormatSymbols());
		return df.format(date);
	}

	/**
	 * 说明：计算记录date有i天是那一天
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @param i
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static Date getDateBetween(Date date, int i) throws Exception {
		if (date == null || i == 0)
			return date;
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.DATE, i);
		return calo.getTime();
	}

	/**
	 * 说明：计算记录date有i天是那一天
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @param i
	 * @return
	 */
	@Deprecated
	public static String getDateBetweenToString(Date date, int i,
												String strFromat) throws Exception {
		Date dateOld = getDateBetween(date, i);
		return getDate(dateOld, strFromat);
	}

	/**
	 * 求两个日期之间相差的天数
	 *
	 * @param beginDate
	 * @param endDate
	 * @return long
	 * @throws Exception
	 */
	public static long getDayDifferenceBetweenTwoDate(Date beginDate,
													  Date endDate) throws Exception {
		if (beginDate == null)
			throw new Exception("传入参数[开始时间]为空");
		if (endDate == null)
			throw new Exception("传入参数[结束时间]为空");
		long ld1 = beginDate.getTime();
		long ld2 = endDate.getTime();
		long days = (long) ((ld2 - ld1) / 86400000);
		return days;
	}

//	/**
//	 * 说明：获取数据库时间
//	 *
//	 * @author:郑其荣 May 13, 2009
//	 * @return
//	 * @throws Exception
//	 */
//	public static Date getDBDate() throws Exception {
//		String s = null;
//		Sql sql = new Sql();
//		sql.setSql("select to_char(sysdate,'yyyy-MM-dd') dbdate from dual");
//		DataStore vds = sql.executeQuery();
//		if (vds.rowCount() > 0)
//			s = vds.getString(0, "dbdate");
//		return new Date(stringToDate(s, "yyyy-MM-dd").getTime());
//	}

//	/**
//	 * 获取数据时间：格式为：yyyy-MM-dd hh:mm:ss
//	 *
//	 * @author zqr
//	 * @return Date
//	 * @return
//	 * @throws Exception
//	 * @date 创建时间 Mar 23, 2010
//	 * @since V1.0
//	 */
//	public static Date getDBTime() throws Exception {
//		String s = null;
//		Sql sql = new Sql();
//		sql.setSql("select to_char(sysdate,'yyyy-mm-dd hh24:mi:ss') dbdate from dual");
//		DataStore vds = sql.executeQuery();
//		if (vds.rowCount() > 0)
//			s = vds.getString(0, "dbdate");
//		return new Date(stringToDate(s, "yyyy-MM-dd hh:mm:ss").getTime());
//	}

	/**
	 * 说明：得到下个月的第一天。
	 *
	 * @author:郑其荣 May 13, 2009
	 * @return
	 * @throws Exception
	 */
	public static String getFirstDayOfNextMonth() throws Exception {
		String strToday = getCurrentDateToString();
		return increaseYearMonth(strToday.substring(0, 6)) + "01";
	}

	/**
	 * 取某年月最后一天
	 *
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public static String getLastDayOfMonth(String dateString)
			throws Exception {
		if (dateString == null)
			return null;
		if (dateString.length() != 6)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		int vnf = StringUtil.stringToInt(dateString.substring(0, 4));
		int vyf = StringUtil.stringToInt(dateString.substring(4, 6));
		if (vyf == 2) {
			if ((vnf % 4 == 0 && vnf % 100 != 0) || vnf % 400 == 0) {
				return "29";
			} else {
				return "28";
			}
		}
		switch (vyf) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				return "31";
			case 4:
			case 6:
			case 9:
			case 11:
				return "30";
			default:
				return null;
		}
	}

	/**
	 * 得到本月的第一天
	 *
	 * @return
	 */
	public static int getMonthFirstDay(Date pdate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pdate);
		return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	}

	public static int getMonthLastDay(Date pdate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(pdate);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

//	/**
//	 * 说明： 求两个日期之间相差的月数
//	 *
//	 * @author:郑其荣 May 13, 2009
//	 * @param beginDate
//	 * @param endDate
//	 * @return
//	 * @throws Exception
//	 */
//	public static int getMonthDifferenceBetweenTwoDate(Date beginDate,
//			Date endDate) throws Exception {
//		if (beginDate == null)
//			throw new Exception("传入参数[开始时间]为空");
//		if (endDate == null)
//			throw new Exception("传入参数[结束时间]为空");
//		int year1 = StringUtil.stringToInt(dateToString(beginDate, "yyyy"));
//		int year2 = StringUtil.stringToInt(dateToString(endDate, "yyyy"));
//		int month1 = StringUtil.stringToInt(dateToString(beginDate, "MM"));
//		int month2 = StringUtil.stringToInt(dateToString(endDate, "MM"));
//		int day1 = StringUtil.stringToInt(dateToString(beginDate, "dd"));
//		int day2 = StringUtil.stringToInt(dateToString(endDate, "dd"));
//
//		double months = (year2 - year1) * 12 + month2 - month1;
//
//		if (day1 == day2) {
//		} else if (day1 == getLastDayOfMonth(beginDate)
//				&& day2 == getLastDayOfMonth(endDate)) {
//		} else {
//			months += (day2 - day1) / 31.00;
//		}
//		return (int) MathUtil.truncate(months, 0);
//
//	}
//
//	/**
//	 * 说明：得到两个字符日期的之间的月数
//	 *
//	 * @author:郑其荣 May 13, 2009
//	 * @param beginDate
//	 * @param endDate
//	 * @return
//	 * @throws Exception
//	 */
//	public static int getMonthDifferenceBetweenTwoStringDate(String beginDate,
//			String endDate) throws Exception {
//		if (beginDate == null
//				|| (beginDate.length() != 6 && beginDate.length() != 8))
//			Alert.FormatError("起始时间输入格式错误,请输入形如\"yyyyMMdd\"或者\"yyyyMM\"的日期格式!");
//		if (endDate == null || (endDate.length() != 6 && endDate.length() != 8))
//			Alert.FormatError("终止时间输入格式错误,请输入形如\"yyyyMMdd\"或者\"yyyyMM\"的日期格式!");
//		Date vQsny_date = stringToDate(beginDate);
//		Date vZzny_date = stringToDate(endDate);
//		return getMonthDifferenceBetweenTwoDate(vQsny_date, vZzny_date);
//	}

	/**
	 * 说明：得到oracle格式的数据。
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param d
	 * @return
	 * @throws Exception
	 */
	public static String getOracleFormatDateStr(Date d) throws Exception {
		return getDate(d, "yyyy-MM-dd hh24:mi:ss");
	}

	/**
	 * 说明：将yyyyMMdd格式的时间转换成yyyy-MM-dd格式的时间
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	public static String getStrHaveAcross(String dateString)
			throws Exception {
		if (dateString == null)
			return null;
		if (dateString.length() != 8)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyyMMdd\"的日期格式!");
		return dateString.substring(0, 4) + "-" + dateString.substring(4, 6)
				+ "-" + dateString.substring(6, 8);

	}

	/**
	 * 说明：计算某个时间后的几个月
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @param i
	 * @return
	 */
	@Deprecated
	public static Date increaseMonth(Date date, int i) {
		if (date == null || i == 0)
			return date;
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.MONTH, i);
		return calo.getTime();
	}

	/**
	 * 说明：计算某个时间后的几年
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param date
	 * @param i
	 * @return
	 */
	public static Date increaseYear(Date date, int i) {
		if (date == null || i == 0)
			return date;
		Calendar calo = Calendar.getInstance();
		calo.setTime(date);
		calo.add(Calendar.YEAR, i);
		return calo.getTime();
	}

	/**
	 * 说明：获取参数时间yearMonth的下一个月
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static String increaseYearMonth(String dateString)
			throws Exception {
		if (dateString == null)
			return null;
		if (dateString.length() != 6)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyymm\"的日期格式!");
		int year = (new Integer(dateString.substring(0, 4))).intValue();
		int month = (new Integer(dateString.substring(4, 6))).intValue();
		if (++month <= 12 && month >= 10)
			return dateString.substring(0, 4) + (new Integer(month)).toString();
		if (month < 10)
			return dateString.substring(0, 4) + "0"
					+ (new Integer(month)).toString();
		else
			return (new Integer(year + 1)).toString() + "0"
					+ (new Integer(month - 12)).toString();
	}

	/**
	 * 说明：获取参数时间yearMonth的下addMonth个月
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param dateString
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static String increaseYearMonth(String dateString, int addMonth)
			throws Exception {
		if (dateString == null)
			return null;
		if (dateString.length() != 6)
			throw new Exception("[时间串]输入格式错误,请输入形如\"yyyymm\"的日期格式!");
		int year = (new Integer(dateString.substring(0, 4))).intValue();
		int month = (new Integer(dateString.substring(4, 6))).intValue();

		if (addMonth < 0) {
			return DateUtil.descreaseYearMonth(dateString, (-1) * addMonth);
		} else {
			month += addMonth;
			year += month / 12;
			month %= 12;
			if (month == 0) {
				month = 12;
				year--;
			}
			if (month <= 12 && month >= 10)
				return year + (new Integer(month)).toString();
			else
				return year + "0" + (new Integer(month)).toString();
		}
	}

	/**
	 * 判断p_str是否是Date类型的字符串,格式为"yyyy.mm.dd"或者"yyyyMMdd"或者yyyy-MM-dd
	 *
	 * @param dateString
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isDate(String dateString) {
		String s = null;
		if (dateString == null)
			return false;
		if (dateString.length() != 10 && dateString.length() != 8) {
			return false;
		}
		if (dateString.length() == 10) {
			s = dateString.substring(0, 4) + dateString.substring(5, 7)
					+ dateString.substring(8, 10);
		} else {
			s = dateString;
		}
		try {
			stringToDate(s, "yyyyMMdd");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 判断p_str是否是Date类型的字符串,格式为自定义格式。
	 *
	 * @param dateString
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean isDate(String dateString, String format) {
		if (dateString == null)
			return false;
		if (dateString.length() != format.length()) {
			return false;
		}
		try {
			stringToDate(dateString, format);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 将字符串转化为日期. 要求传入6位(yyyyMM)或者8位(yyyyMMdd)的参数
	 *
	 * @param dateString
	 * @return Date
	 */
	public static Date stringToDate(String dateString) throws Exception {
		Date vdate = null;
		String vformat = null;
		if (dateString == null)
			return null;
		if (dateString.length() != 4 && dateString.length() != 6
				&& dateString.length() != 7 && dateString.length() != 8
				&& dateString.length() != 10 && dateString.length() != 14
				&& dateString.length() != 19) {
			throw new Exception("时间串【" + dateString + "】输入格式错误,请输入合法的日期格式!");
		}
		if (dateString.length() == 4) {
			vformat = "yyyy";
		} else if (dateString.length() == 6) {
			vformat = "yyyyMM";
		} else if (dateString.length() == 7) {
			dateString = dateString.substring(0, 4)
					+ dateString.substring(5, 7);
			vformat = "yyyyMM";
		} else if (dateString.length() == 8) {
			vformat = "yyyyMMdd";
		} else if (dateString.length() == 10) {
			dateString = dateString.substring(0, 4)
					+ dateString.substring(5, 7) + dateString.substring(8, 10);
			vformat = "yyyyMMdd";
		} else if (dateString.length() == 14) {
			vformat = "yyyyMMddHHmmss";
		} else if (dateString.length() == 19) {
			vformat = "yyyy-MM-dd HH:mm:ss";
		}
		vdate = stringToDate(dateString, vformat);
		return vdate;
	}

	/**
	 * 说明：将字符串转成时间
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param dateString
	 * @param format
	 * @return
	 * @throws Exception
	 */

	public static Date stringToDate(String dateString, String format)
			throws Exception {
		if (dateString == null) {
			return null;
		}
		if (dateString.equalsIgnoreCase("")) {
			throw new Exception("传入参数中的[时间串]为空");
		}
		if (format == null || format.equalsIgnoreCase("")) {
			throw new Exception("传入参数中的[时间格式]为空");
		}

		Hashtable<Integer, String> h = new Hashtable<Integer, String>();
		if (format.indexOf("yyyy") != -1)
			h.put(new Integer(format.indexOf("yyyy")), "yyyy");
		else if (format.indexOf("yy") != -1)
			h.put(new Integer(format.indexOf("yy")), "yy");
		if (format.indexOf("MM") != -1)
			h.put(new Integer(format.indexOf("MM")), "MM");
		else if (format.indexOf("mm") != -1)
			h.put(new Integer(format.indexOf("mm")), "MM");
		if (format.indexOf("dd") != -1)
			h.put(new Integer(format.indexOf("dd")), "dd");
		if (format.indexOf("hh24") != -1)
			h.put(new Integer(format.indexOf("hh24")), "HH");
		else if (format.indexOf("hh") != -1) {
			h.put(new Integer(format.indexOf("hh")), "HH");
		} else if (format.indexOf("HH") != -1) {
			h.put(new Integer(format.indexOf("HH")), "HH");
		}
		if (format.indexOf("mi") != -1)
			h.put(new Integer(format.indexOf("mi")), "mm");
		else if (format.indexOf("mm") != -1 && h.containsValue("HH"))
			h.put(new Integer(format.lastIndexOf("mm")), "mm");
		if (format.indexOf("ss") != -1)
			h.put(new Integer(format.indexOf("ss")), "ss");
		if (format.indexOf("SSS") != -1)
			h.put(new Integer(format.indexOf("SSS")), "SSS");

		for (int intStart = 0; format.indexOf("-", intStart) != -1; intStart++) {
			intStart = format.indexOf("-", intStart);
			h.put(new Integer(intStart), "-");
		}
		for (int intStart = 0; format.indexOf(".", intStart) != -1; intStart++) {
			intStart = format.indexOf(".", intStart);
			h.put(new Integer(intStart), ".");
		}
		for (int intStart = 0; format.indexOf("/", intStart) != -1; intStart++) {
			intStart = format.indexOf("/", intStart);
			h.put(new Integer(intStart), "/");
		}

		for (int intStart = 0; format.indexOf(" ", intStart) != -1; intStart++) {
			intStart = format.indexOf(" ", intStart);
			h.put(new Integer(intStart), " ");
		}

		for (int intStart = 0; format.indexOf(":", intStart) != -1; intStart++) {
			intStart = format.indexOf(":", intStart);
			h.put(new Integer(intStart), ":");
		}

		if (format.indexOf("年") != -1)
			h.put(new Integer(format.indexOf("年")), "年");
		if (format.indexOf("月") != -1)
			h.put(new Integer(format.indexOf("月")), "月");
		if (format.indexOf("日") != -1)
			h.put(new Integer(format.indexOf("日")), "日");
		if (format.indexOf("时") != -1)
			h.put(new Integer(format.indexOf("时")), "时");
		if (format.indexOf("分") != -1)
			h.put(new Integer(format.indexOf("分")), "分");
		if (format.indexOf("秒") != -1)
			h.put(new Integer(format.indexOf("秒")), "秒");

		String javaFormat = new String();
		int i = 0;
		while (h.size() != 0) {
			Enumeration<Integer> e = h.keys();
			int n = 0;
			while (e.hasMoreElements()) {
				i = ((Integer) e.nextElement()).intValue();
				if (i >= n)
					n = i;
			}
			String temp = (String) h.get(new Integer(n));
			h.remove(new Integer(n));
			javaFormat = temp + javaFormat;
		}
		SimpleDateFormat df = new SimpleDateFormat(javaFormat);
		df.setLenient(false);// 这个的功能是不把1996-13-3 转换为1997-1-3
		Date myDate = new Date();
		try {
			myDate = df.parse(dateString);
		} catch (ParseException e) {
			/**
			 * 解析抛出异常后， 如果判定此异常为"夏时制"起始日期导致， 则使用"松散模式"再次解析日期，并返回结果。
			 *
			 *   1940-06-03 01:00:00 ~ 1940-09-30 23:00:00
			 *	 1941-03-16 01:00:00 ~ 1941-09-30 23:00:00
			 *   1986-05-04 01:00:00 ~ 1986-09-13 23:00:00
			 *   1987-04-12 01:00:00 ~ 1987-09-12 23:00:00
			 *   1988-04-10 01:00:00 ~ 1988-09-10 23:00:00
			 *	 1989-04-16 01:00:00 ~ 1989-09-16 23:00:00
			 *   1990-04-15 01:00:00 ~ 1990-09-15 23:00:00
			 *   1991-04-14 01:00:00 ~ 1991-09-14 23:00:00
			 * 20131015 lzx
			 */
			try {
				df.setLenient(true);

				Calendar c = Calendar.getInstance();
				c.setTime(df.parse(dateString));

				if ((c.get(Calendar.YEAR) == 1991 && c.get(Calendar.MONTH) == 3
						&& c.get(Calendar.DAY_OF_MONTH) == 14 && c
						.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1990
						&& c.get(Calendar.MONTH) == 3
						&& c.get(Calendar.DAY_OF_MONTH) == 15 && c
						.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1989
						&& c.get(Calendar.MONTH) == 3
						&& c.get(Calendar.DAY_OF_MONTH) == 16 && c
						.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1988
						&& c.get(Calendar.MONTH) == 3
						&& c.get(Calendar.DAY_OF_MONTH) == 10 && c
						.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1987
						&& c.get(Calendar.MONTH) == 3
						&& c.get(Calendar.DAY_OF_MONTH) == 12 && c
						.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1986
						&& c.get(Calendar.MONTH) == 4
						&& c.get(Calendar.DAY_OF_MONTH) == 4 && c
						.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1941
						&& c.get(Calendar.MONTH) == 2
						&& c.get(Calendar.DAY_OF_MONTH) == 16 && c
						.get(Calendar.HOUR_OF_DAY) == 1)
						|| (c.get(Calendar.YEAR) == 1940
						&& c.get(Calendar.MONTH) == 5
						&& c.get(Calendar.DAY_OF_MONTH) == 3 && c
						.get(Calendar.HOUR_OF_DAY) == 1)) {
					myDate = c.getTime();
				} else {
					throw new Exception("日期格式转换错误!将【" + dateString + "】按照格式【"
							+ javaFormat + "】转换成时间时出错");
				}
			} catch (ParseException e1) {
				throw new Exception("日期格式转换错误!将【" + dateString + "】按照格式【"
						+ javaFormat + "】转换成时间时出错");
			}
		}
		return myDate;
	}

	/**
	 * 说明：判断s1是不是大于等于s2，如果是返回true，否则返回false。
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public static boolean yearMonthGreatEqual(String beginDate, String endDate)
			throws Exception {
		if (beginDate == null || beginDate.length() != 6)
			throw new Exception("起始时间输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		if (endDate == null || endDate.length() != 6)
			throw new Exception("终止时间输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		String temp1 = beginDate.substring(0, 4);
		String temp2 = endDate.substring(0, 4);
		String temp3 = beginDate.substring(4, 6);
		String temp4 = endDate.substring(4, 6);
		if (Integer.parseInt(temp1) > Integer.parseInt(temp2))
			return true;
		if (Integer.parseInt(temp1) == Integer.parseInt(temp2))
			return Integer.parseInt(temp3) >= Integer.parseInt(temp4);
		else
			return false;
	}

	/**
	 * 说明：判断s1是不是大于s2，如果是返回true，否则返回false。
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param beginDate
	 * @param endDate
	 * @return
	 * @throws Exception
	 */
	public static boolean yearMonthGreater(String beginDate, String endDate)
			throws Exception {
		if (beginDate == null || beginDate.length() != 6)
			throw new Exception("起始时间输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		if (endDate == null || endDate.length() != 6)
			throw new Exception("终止时间输入格式错误,请输入形如\"yyyyMM\"的日期格式!");
		String temp1 = beginDate.substring(0, 4);
		String temp2 = endDate.substring(0, 4);
		String temp3 = beginDate.substring(4, 6);
		String temp4 = endDate.substring(4, 6);
		if (Integer.parseInt(temp1) > Integer.parseInt(temp2))
			return true;
		if (Integer.parseInt(temp1) == Integer.parseInt(temp2))
			return Integer.parseInt(temp3) > Integer.parseInt(temp4);
		else
			return false;
	}

	/**
	 * 说明：将month数转成几年几个月，如果13个月表示一年零一个月
	 *
	 * @author:郑其荣 May 13, 2009
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public static String monthToYearMonth(String month) throws Exception {
		if (month == null)
			return null;
		if (month.equalsIgnoreCase(""))
			throw new Exception("传入参数中的[月数]为空");
		String yearMonth = "";
		int smonth = 0;
		int year = 0;
		int rmonth = 0;
		if ("0".equals(month))
			return "0月";
		smonth = Integer.parseInt(month);
		year = smonth / 12;
		rmonth = smonth % 12;
		if (year > 0)
			yearMonth = year + "年";
		if (rmonth > 0)
			yearMonth = yearMonth + rmonth + "个月";
		return yearMonth;
	}

	/**
	 * 说明：得到月份的第一天
	 *
	 * @author:郑其荣 May 14, 2009
	 * @param date
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static int getFirstDayOfMonth(Date date) throws Exception {
		if (date == null) {
			throw new Exception("传入参数中的[时间]为空");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 说明：得到月份的最后一天
	 *
	 * @author:郑其荣 May 14, 2009
	 * @param date
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	public static int getLastDayOfMonth(Date date) throws Exception {
		if (date == null) {
			throw new Exception("传入参数中的[时间]为空");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

//	/**
//	 * 说明：根据出生日期和时间获取年龄
//	 *
//	 * @param pcsrq
//	 *            单位编号
//	 * @param pny
//	 *            时间 Date 型
//	 * @return long 年龄
//	 * @throws Exception
//	 */
//	public static double getAgeByBirthDay(Date pcsrq, Date pny)
//			throws Exception {
//		return getAgeByBirthDay(pcsrq, DateUtil.FormatDate(pny, "yyyyMMdd"));
//	}

//	/**
//	 * 说明：根据出生日期和时间获取年龄
//	 *
//	 * @author:郑其荣 May 14, 2009
//	 * @param pcsrq
//	 * @param pny
//	 * @return
//	 * @throws Exception
//	 */
//	public static double getAgeByBirthDay(Date pcsrq, String pny)
//			throws Exception {
//		double vnl = 0.00;
//		if (pcsrq == null) {
//			throw new Exception("计算年龄时传入的出生日期为空!");
//		}
//		if (pny == null || "".equals(pny)) {
//			throw new Exception("计算年龄时传入的年月为空!");
//		}
//		int vlen = pny.length();
//		if (vlen == 6) {
//			vnl = (DateUtil.getMonthDifferenceBetweenTwoDate(
//					DateUtil.stringToDate(
//							DateUtil.dateToString(pcsrq, "yyyyMM"), "yyyyMM"),
//					DateUtil.stringToDate(pny, "yyyyMM"))) / 12.00;
//		} else if (vlen == 8) {
//			vnl = (DateUtil
//					.getMonthDifferenceBetweenTwoDate(DateUtil.stringToDate(
//							DateUtil.dateToString(pcsrq, "yyyyMMdd"),
//							"yyyyMMdd"), DateUtil.stringToDate(pny, "yyyyMMdd"))) / 12.00;
//		} else {
//			Alert.FormatError("计算年龄时传入的年月应为[yyyymm]或[yyyymmdd]格式!");
//		}
//
//		return vnl;
//	}

	/**
	 * 获取星期数
	 *
	 * @author zqr
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 获取星期数
	 *
	 * @author zqr
	 * @param date
	 * @return
	 */
	public static String getChineseWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int i = cal.get(Calendar.DAY_OF_WEEK);
		if (i == 1) {
			return "星期天";
		} else if (i == 2) {
			return "星期一";
		} else if (i == 3) {
			return "星期二";
		} else if (i == 4) {
			return "星期三";
		} else if (i == 5) {
			return "星期四";
		} else if (i == 6) {
			return "星期五";
		} else if (i == 7) {
			return "星期六";
		} else {
			return "";
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.println(DateUtil.stringToDate("1941.03.1600:00:00", "yyyy.MM.ddHH:mm:ss"));
	}

}
