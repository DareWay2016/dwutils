package cn.com.dareway.dwlibrary.datastoreutils;



import net.sourceforge.pinyin4j.PinyinHelper;

import java.math.BigDecimal;
import java.util.Comparator;


public class DataObjectComparator implements Comparator<DataObject> {

	private String[] key = null;

	public DataObjectComparator(String compareKeys) {
		key = getColNames(compareKeys);
	}

	public int compare(DataObject o1, DataObject o2) {
		Object o1Value, o2Value;
		int result = 0;
		for (int i = 0; i < key.length; i++) {
			try {
				o1Value = o1.getObject(key[i], null);
			} catch (Exception e) {
				o1Value = null;
			}
			try {
				o2Value = o2.getObject(key[i], null);
			} catch (Exception e) {
				o2Value = null;
			}
			result = compareCell(o1Value, o2Value);
			if (result == 0)
				continue;
			else
				return result;
		}
		return result;
	}

	protected int compareCell(Object o1, Object o2) {
		// o1,o2均不为null，则正常比较
		if (o1 != null && o2 != null) {
			if (o1.getClass().getName().equals("java.lang.Integer")
					|| o1.getClass().getName().equals("java.lang.Double")
					|| o1.getClass().getName().equals("java.lang.Long")
					|| o1.getClass().getName().equals("java.math.BigDecimal")) {
				return (new BigDecimal(o1.toString()))
						.compareTo(new BigDecimal(o2.toString()));
			} else if (o1.getClass().getName().equals("java.util.Date")) {
				try {
					return (DateUtil.FormatDate((java.util.Date) o1))
							.compareTo(DateUtil.FormatDate((java.util.Date) o2));
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			} else if (o1.getClass().getName().equals("java.sql.Date")) {
				try {
					return (DateUtil.FormatDate((java.sql.Date) o1))
							.compareTo(DateUtil.FormatDate((java.sql.Date) o2));
				} catch (Exception e) {
					e.printStackTrace();
					return 0;
				}
			} else {
				// return Collator.getInstance(Locale.CHINESE).compare(o1,o2);
				return compareChinese(o1.toString(), o2.toString());
			}
		} else if (o1 == null && o2 != null) {// o1为null，o2不为null，那么将o1放到o2后面
			return 1;
		} else if (o1 != null && o2 == null) {// o1不为null，o2为null，那么将o1放到o2前面
			return -1;
		} else {// o1，o2同时为空，则默认二者相等，按原次序排列
			return 0;
		}
	}

	protected int compareChinese(String o1, String o2) {
		for (int i = 0; i < o1.length() && i < o2.length(); i++) {

			int codePoint1 = o1.charAt(i);
			int codePoint2 = o2.charAt(i);

			if (Character.isSupplementaryCodePoint(codePoint1)
					|| Character.isSupplementaryCodePoint(codePoint2)) {
				i++;
			}

			if (codePoint1 != codePoint2) {
				if (Character.isSupplementaryCodePoint(codePoint1)
						|| Character.isSupplementaryCodePoint(codePoint2)) {
					return codePoint1 - codePoint2;
				}

				String pinyin1 = pinyin((char) codePoint1);
				String pinyin2 = pinyin((char) codePoint2);

				if (pinyin1 != null && pinyin2 != null) { // 两个字符都是汉字
					if (!pinyin1.equals(pinyin2)) {
						return pinyin1.compareTo(pinyin2);
					}
				} else {
					return codePoint1 - codePoint2;
				}
			}
		}
		return o1.length() - o2.length();
	}

	/**
	 * 字符的拼音，多音字就得到第一个拼音。不是汉字，就return null。
	 */
	protected String pinyin(char c) {
		String[] pinyins = PinyinHelper.toHanyuPinyinStringArray(c);
		if (pinyins == null) {
			return null;
		}
		return pinyins[0];
	}

	protected String[] getColNames(String cols) {
		if (null == cols)
			return null;
		String[] result = cols.split(",");
		for (int i = 0; i < result.length; i++) {
			result[i] = result[i].trim();
		}
		return result;
	}
}
