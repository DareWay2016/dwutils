package cn.com.dareway.dwlibrary.datastoreutils;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;


public class StringUtil{
	/**
	 * 说明：将一个字符串trim，如果是null，返回null
	 *
	 * @author:郑其荣 May 14, 2009
	 * @param s
	 * @return
	 */
	public static String trim(String s) {
		if (s == null) {
			return null;
		}
		s = s.trim();
		return s;
	}

	/**
	 * 判定如果字符串a is null，返回b,否则是a
	 *
	 * @param a
	 * @param b
	 * @return
	 */
	public static String nvlStr(String a, String b) {
		if (a == null) {
			return b;
		}
		return a;
	}

	/**
	 * 去掉右空格
	 *
	 * @todo:算法待改进
	 * @param  str
	 * @return String
	 */
	public static String rTrim(String str) {
		for (int i = str.length() - 1; i > 0; i--) {
			if (str.charAt(i) != ' ') {
				return str;
			}
			str = str.substring(0, i);
		}
		return str.trim();
	}

	/**
	 * 去掉左空格
	 *
	 * @param  str
	 * @return String
	 */
	public static String lTrim(String str) {
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) != ' ') {
				return str;
			}
			str = str.substring(i + 1, str.length());
		}
		return str.trim();
	}

	/**
	 * 将字符串转化为int，不能转化抛异常。
	 *
	 * @throws Exception
	 */
	public static int stringToInt(String intString) throws Exception {
		int iRet = 0;

		if (intString == null || intString.equalsIgnoreCase("")) {
			throw new Exception("传入参数为空!");
		}

		intString = intString.trim();

		try {
			iRet = Integer.valueOf(intString);
		} catch (NumberFormatException e) {

			try {
				iRet = (int)Double.valueOf(intString).doubleValue();
			} catch (NumberFormatException e1) {
				throw new Exception("StringUtil.stringToDouble出错，传入的字符串[" + intString +
						"]不能被转换为整型!");
			}

		}

		return iRet;
	}

	/**
	 * 将字符串转化为int，不能转化抛异常。
	 *
	 * @throws Exception
	 */
	public static double stringToDouble(String s) throws Exception {
		double i = 0;
		if (s == null || s.equalsIgnoreCase("")) {
			throw new Exception("传入参数为空!");

		}
		s = s.trim();
		try {
			DecimalFormat df = new DecimalFormat("");
			i = df.parse(s).doubleValue();
		} catch (ParseException e) {
			throw new Exception("StringUtil.stringToDouble出错，传入的字符串[" + s +
					"]不是一个包含数字的字符串!");
		}
		return i;

	}

//	/**
//	 * 将字符串转化为拼音首字母。
//	 *
//	 * @param String str
//	 * @return String
//	 * @throws Exception
//	 */
//	public static String getPy(String str) throws Exception {
//		return GetPy.getGBKpy(str);
//	}

	/**
	 * 不能简单的使用：new String(string.getBytes("gbk"),"utf-8");
	 * @author zqr
	 * @date 创建时间 Jul 8, 2010
	 * @since V3.9
	 */
	public static byte[] gbk2utf8(String chenese) {
		char c[] = chenese.toCharArray();
		byte[] fullByte = new byte[3 * c.length];
		for (int i = 0; i < c.length; i++) {
			int m = (int) c[i];
			String word = Integer.toBinaryString(m);

			StringBuffer sb = new StringBuffer();
			int len = 16 - word.length();
			// 补零
			for (int j = 0; j < len; j++) {
				sb.append("0");
			}
			sb.append(word);
			sb.insert(0, "1110");
			sb.insert(8, "10");
			sb.insert(16, "10");

			String s1 = sb.substring(0, 8);
			String s2 = sb.substring(8, 16);
			String s3 = sb.substring(16);

			byte b0 = Integer.valueOf(s1, 2).byteValue();
			byte b1 = Integer.valueOf(s2, 2).byteValue();
			byte b2 = Integer.valueOf(s3, 2).byteValue();
			byte[] bf = new byte[3];
			bf[0] = b0;
			fullByte[i * 3] = bf[0];
			bf[1] = b1;
			fullByte[i * 3 + 1] = bf[1];
			bf[2] = b2;
			fullByte[i * 3 + 2] = bf[2];

		}
		return fullByte;
	}

	/**
	 * Description:String的length()方法计算字符串长度，英文一个字母算一个，汉语一个字也算一个。
	 * <p>
	 * 在填充表格时不能精确确定显示内容的长度。编写chnSubstring和chnStrLen方法，计算字符
	 * <p>
	 * 串长度和截取子串时，一个汉字长度算两个。
	 * <p>
	 * 下面是chnSubstring方法。
	 *
	 * @param chinaString
	 * @param start
	 * @param end
	 * @return
	 * @throws Exception
	 */
	public static String chnSubstring(String chinaString, int start, int end) throws Exception {
		if (null == chinaString)
			return null;
		int startIdx = 0, endIdx = chinaString.length();
		int currentPos = 0, currentCharLen = 0;
		boolean flag = false;

		if (end > chnStrLen(chinaString)) {
			end = chnStrLen(chinaString);
		}
		if (start >= end)
			return "";

		for (int i = 0; i < chinaString.length(); i++) {
			if ((int) chinaString.charAt(i) > 255)
				currentCharLen = 2;
			else
				currentCharLen = 1;

			if (flag == false && currentPos >= start) {
				if (currentPos > start)
					startIdx = i - 1;
				else
					startIdx = i;
				flag = true;
			}
			if (flag == true && currentPos + currentCharLen > end) {
				endIdx = i;
				break;
			}
			currentPos += currentCharLen;
		}
		if (flag == false) {
			startIdx = chinaString.length() - 1;
		}
		return chinaString.substring(startIdx, endIdx);
	}

	/**
	 * Description:String的length()方法计算字符串长度，英文一个字母算一个，汉语一个字也算一个。
	 * <p>
	 * 在填充表格时不能精确确定显示内容的长度。编写chnSubstring和chnStrLen方法，计算字符
	 * <p>
	 * 串长度和截取子串时，一个汉字长度算两个。
	 * <p>
	 * 下面是chnStrLen方法。
	 *
	 * @param chinaString
	 *
	 * @return
	 */
	public static int chnStrLen(String chinaString) {
		int len = 0;
		for (int i = 0; i < chinaString.length(); i++) {
			if (((int) chinaString.charAt(i)) > 255)
				len += 2;
			else
				len++;
		}
		return len;
	}

	/**
	 * 获取字符串中汉字的个数
	 *
	 * @param chinaString
	 * @return
	 */
	public static int getChnNumber(String chinaString) {
		int len = 0;
		for (int i = 0; i < chinaString.length(); i++) {
			if (((int) chinaString.charAt(i)) > 255)
				len++;
		}
		return len;
	}

	/**
	 * 获取字符串中字母和数字的个数
	 *
	 * @param chinaString
	 * @return
	 */
	public static int getchrNumber(String chinaString) {
		int len = 0;
		for (int i = 0; i < chinaString.length(); i++) {
			if (((int) chinaString.charAt(i)) > 255)
				continue;
			else
				len++;
		}
		return len;
	}

	/**
	 * 获取字符串的长度
	 *
	 * @param value 原始字符串
	 * @param chinaLen 每个汉字所占的宽度
	 * @param englishLen 每个非汉字所占的宽度
	 * @return 整个字符串所占的总宽度
	 */
	public final static int getCssLength(String value, int chinaLen,
										 int englishLen) {
		int clen = 0;
		int elen = 0;
		for (int i = 0, j = value.length(); i < j; i++) {
			if ((int) value.charAt(i) > 255) {
				clen += chinaLen;
			} else {
				elen += englishLen;
			}
		}
		return clen + elen;
	}

//	/**
//	 * 说明：从身份证中得到年龄
//	 *
//	 * @author:郑其荣 May 13, 2009
//	 * @param card
//	 * @return
//	 * @throws Exception
//	 */
//	public static int getAgeFromCard(String card) throws Exception {
//		int age = -1;
//		int length = card.length();
//		String birthday = "";
//		//20120929 modi by www 修改根据身份证计算年龄的机制
//		if (length == 15) {
//			birthday = card.substring(6, 8);
//			birthday = "19" + birthday;
//		} else if (length == 18)
//			birthday = card.substring(6, 14);
//		else
//			throw new Exception( "错误的身份证号!");
//		//获取当前时间
//		Date currentTime = new Date();
//		//计算出当前时间与出生年月的月数差
//		int diffMonths = DateUtil.getMonthDifferenceBetweenTwoDate(DateUtil.stringToDate(birthday),
//				currentTime);
//
//		//预防身份证信息有错
//		if(diffMonths<0){
//			throw new Exception(ExceptionCode.CardError, "错误的身份证号：出生日期大于当前日期!");
//		}
//
//		//计算年龄，不满12个月忽略
//		age = diffMonths/12;
//		//20120929 end modi by www 修改根据身份证计算年龄的机制
//		return age;
//	}

//	/**
//	 * 判断身份证号码是否合法
//	 */
//	public static String validateCard(String p_sfzhm) throws Exception {
//		int vsfz_sum, vsfz_vi, vsfz_si, vsfz_wi, vsfz_vj, vsfz_res;
//		String vsfz_temp, vsfz_str, vsfz_a, vsfz_jg;
//		String vsfz_err = "0";
//
//		if (p_sfzhm == null) {
//			Alert.isNull("传入的身份证号码为空!");
//		}
//		p_sfzhm = p_sfzhm.trim();
//		if (p_sfzhm.length() == 18) {
//			vsfz_temp = "10X98765432";
//			vsfz_sum = 0;
//			vsfz_vi = 0;
//			vsfz_str = p_sfzhm.substring(0, 17);
//			while ("0".equals(vsfz_err) && vsfz_vi <= 16) {
//				vsfz_a = vsfz_str.substring(vsfz_vi, vsfz_vi + 1);
//				if ("9".compareTo(vsfz_a) < 0 || "0".compareTo(vsfz_a) > 0) {
//					vsfz_err = "1";
//				}
//				vsfz_vi++;
//			}
//			if ("1".equals(vsfz_err)) {
//				return "身份证号码的第" + DataFormat.formatNumber(vsfz_vi)
//						+ "位是非法数字请修改!";
//			}
//
//			boolean vflag=DateUtil.isDate(p_sfzhm.substring(6, 14));
//			if(!vflag){
//				return "身份证号码的出生日期不对，请修改!";
//			}
//
//
//			vsfz_vi = 0;
//			while (vsfz_vi <= 16) {
//				vsfz_a = p_sfzhm.substring(vsfz_vi, vsfz_vi + 1);
//				vsfz_si = stringToInt(vsfz_a);
//				vsfz_vj = 0;
//				vsfz_wi = 1;
//				while (vsfz_vj < 17 - vsfz_vi) {
//					vsfz_wi = vsfz_wi * 2;
//					vsfz_vj = vsfz_vj + 1;
//				}
//				vsfz_sum = vsfz_sum + vsfz_si * vsfz_wi;
//				vsfz_vi++;
//			}
//			vsfz_res = vsfz_sum % 11;
//			vsfz_jg = vsfz_temp.substring(vsfz_res, vsfz_res + 1);
//			if (!vsfz_jg.equals(p_sfzhm.substring(17, 18))) {
//				//return "身份证号码最后一位不对，应该是" + vsfz_jg+"!";
//				return "身份证号码的校验位不对，请检查!";
//			}
//		}else{
//			return "身份证号码的长度不是18位!";
//		}
//		return "";
//	}

	/**
	 * 功能：从字符串最左边起，根据限制长度截取字符串（字符串中包括汉字，一个汉字等于两个字符） 如果最后一个字符是半个汉字则会舍去。
	 *
	 * @param strParameter 要截取的字符串
	 * @param limitLength 截取的长度
	 * @return 截取后的字符串
	 * @throws Exception
	 */
	public static String leftb(String strParameter, int limitLength) throws Exception {
		if (strParameter == null) {
			throw new Exception("输入的字符串参数为空!");

		}
		String return_str = strParameter; // 返回的字符串
		int temp_int = 0; // 将汉字转换成两个字符后的字符串长度
		int cut_int = 0; // 对原始字符串截取的长度
		byte[] b = strParameter.getBytes(); // 将字符串转换成字符数组
		int last_length;
		for (int i = 0; i < b.length; i++) {
			if (b[i] >= 0) {
				last_length = 1; // 英文字符
			} else {
				last_length = 2; // 一个汉字等于两个字符
				i++;
			}
			temp_int = temp_int + last_length;
			cut_int++;
			if (temp_int >= limitLength) {
				if (temp_int > limitLength) {
					temp_int -= last_length;
					cut_int--;
				}
				return_str = return_str.substring(0, cut_int);
				break;
			}
		}
		return return_str;
	}

//	/**
//	 * 将Clob类型的对象转换成字符串 如果Clob是一个空Clob对象或是本身为空 则转换成null
//	 *
//	 * @param clob
//	 * @return 如果clob为 null 返回 null
//	 * @throws Exception
//	 * @since 2.0
//	 * @author wf 2009-12-2 13:31:05
//	 */
//	public static String clob2string(Clob clob) throws Exception {
//		if (clob == null) {
//			return null;
//		}
//		Reader reader;
//		try {
//			if (clob instanceof ClobProxy) {
//				ClobProxy clobProxy = (ClobProxy) clob;
//				reader = clobProxy.getRawClob().getCharacterStream();
//			} else if(clob instanceof javax.sql.rowset.serial.SerialClob){
//				javax.sql.rowset.serial.SerialClob serialClob = (javax.sql.rowset.serial.SerialClob)clob;
//				reader = serialClob.getCharacterStream();
//			} else {
//				reader = clob.getCharacterStream();
//			}
//			if (reader != null) {
//				BufferedReader br = new BufferedReader(reader);
//				String lineString = br.readLine();
//				StringBuffer sb = new StringBuffer();
//				while (lineString != null) {
//					if(sb.length()==0){
//						sb.append(lineString);
//					}else{
//						sb.append("\n"+lineString);
//					}
//					lineString = br.readLine();
//				}
//				return sb.toString();
//			} else {
//				return null;
//			}
//		} catch (SQLException e) {
//			throw new Exception("从Clob对象中获取字符流输出对象时出现异常!");
//		} catch (IOException e) {
//			throw new Exception("从Clob对象中读取内容是出现 I/O 异常!");
//		}
//	}

//	/**
//	 * 将String类型的长文本内容写入到Clob类型的对象当中
//	 *
//	 * @param clobObj 从数据库中获取的Clob列的对象
//	 * @param clobString 要写入的长文本字符串
//	 * @throws Exception
//	 */
//	public static void writeString2Clob(Clob clobObj, String clobString) throws Exception {
//		if (clobObj == null) {// 如果没有获取到
//			Alert.isNull("在向Clob类型中写入数据时，传入的Clob类型为NULL，属于非法对象!");
//		}
//		try {
//			Writer out = null;
//			if (clobObj instanceof ClobProxy) {
//				ClobProxy clobProxy = (ClobProxy) clobObj;
//				out = clobProxy.getRawClob().setCharacterStream( 0L );
//			}else if(clobObj instanceof javax.sql.rowset.serial.SerialClob){
//				javax.sql.rowset.serial.SerialClob serialClob = (javax.sql.rowset.serial.SerialClob)clobObj;
//				out = serialClob.setCharacterStream(0L);
//			}else{
//				out = ((CLOB) clobObj).setCharacterStream( 0L );
//			}
//
//			out.write(clobString);
//			out.close();
//		} catch (IOException e) {
//			throw new Exception("在向Clob对象中写入数据时出现I/O异常!", e);
//		} catch (SQLException e) {
//			throw new Exception("在获取Clob文本输出流时出现SQLException!", e);
//		}
//	}

	/**
	 * 解码
	 *
	 * @param originalString
	 * @param encoding
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String decode(String originalString, String encoding) throws UnsupportedEncodingException {
		return java.net.URLDecoder.decode(originalString, encoding);
	}

	public static String encode(String originalString, String encoding) throws UnsupportedEncodingException {
		return java.net.URLEncoder.encode(originalString, encoding);
	}

//	public static String getUUID() {
//		UUIDGenerator generator = UUIDGenerator.getInstance();
//		UUID uuid = generator.generateRandomBasedUUID();
//		String randomString = uuid.toString();
//		randomString = randomString.replaceAll("-", "_");
//		return randomString;
//	}
//
	/**
	 * 获取一个字母（区分大小写）或者数字的ASCII值，其他字符的返回"-1"
	 * @author www
	 * @date 2012-11-2
	 */
	public static String getASCIIOfLetterAndNumber(String str){
		char a = str.charAt(0);
		int b = a;

		//数字的范围是48-57，大写字符的是65-90，小写字母的是97-122
		if((47<b&&b<58)||(64<b&&b<91)||(96<b&&b<123)){
			return String.valueOf(b);
		}else{
			return "-1";
		}
	}

	public static void main(String[] args) throws Exception{
		System.out.println(stringToInt("0.0000001"));
	}
}
