package cn.com.dareway.dwlibrary.datastoreutils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.sql.Clob;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



/**
 * 说明：DataObjet类，主要是用来存放一维数据的数据结构。
 * <p>
 * 客户端取值 要用getXXpara方法 Server端取值需要使用 getXX系列方法
 * </p>
 *
 * @author 郑其荣 May 12, 2009
 * @update wf 2009-12-1 10:00:35
 */

@SuppressWarnings({ "rawtypes", "unchecked" })
public class DataObject extends HashMap implements Serializable {

    private static final long serialVersionUID = 1466923519690153347L;

    public DataObject() {
    }

    public DataObject(HashMap map) {
        super(map);
    }

    public String[] getValueTypes() {
        if (this.size() == 0)
            return null;
        Collection values = this.values();
        String[] types = new String[values.size()];
        Object[] valuesArray = values.toArray();
        for (int i = 0; i < valuesArray.length; i++) {
            types[i] = valuesArray[i].getClass().getName();
        }
        return types;
    }

    // 20121029 modi by www 因为putAll方法是Map提供的，
    // 但因为其使用方式跟框架的DataObject使用方式不一致，所以不建议使用该方法
    @Deprecated
    public void putAll(Map map) {
        super.putAll(map);
    }

    /**
     * 说明：验证是否有关键字
     *
     * @author:郑其荣 May 12, 2009
     * @param key
     * @return
     * @throws Exception
     */
    public boolean containsKey(String key) throws Exception {
        if (key == null || "".equalsIgnoreCase(key)) {
            throw  new Exception("关键字为空");
        }
        key = key.toLowerCase();
        return super.containsKey(key);
    }

    /**
     * 返回所有的关键字
     */
    public Set keySet() {
        return super.keySet();
    }

    /**
     * 清空所有的关键字及其对应的值
     */
    public void clear() {
        super.clear();
    }

    public DataObject clone() {
        return (DataObject) super.clone();
    }

    /**
     * 说明：存放键值对
     *
     * @author:郑其荣 May 12, 2009
     * @param name
     * @param value
     * @return
     * @throws Exception
     */
    public Object put(String name, Object value) throws Exception {
        if (name == null || name.equals("")) {
            throw  new Exception("关键字为空");
        }
        name = name.toLowerCase();
        if (value instanceof java.sql.Date) {
            value = new Date(((java.sql.Date) value).getTime());
        }
        if (value instanceof java.sql.Timestamp) {
            value = new Date(((java.sql.Timestamp) value).getTime());
        }
        return super.put(name, value);
    }

    /**
     * 说明：服务器端往客户端发送数据，供客户端打印时用。cs客户端专用
     *
     * @author:郑其荣 May 12, 2009
     * @param name
     * @param value
     * @return
     * @throws Exception
     */
    public Object putReport(String name, DataStore value) throws Exception {
        if (name == null || name.equals("")) {
            throw  new Exception("关键字为空");
        }
        return this.put(name + "$r", value);
    }

    /**
     * 说明：存放int类型的键值对
     *
     * @author:郑其荣 May 12, 2009
     * @param name
     * @param value
     * @return
     * @throws Exception
     */
    public Object put(String name, int value) throws Exception {
        return put(name, new Integer(value));
    }

    /**
     * 说明：存放double类型的键值对
     *
     * @author:郑其荣 May 12, 2009
     * @param name
     * @param value
     * @return
     * @throws Exception
     */
    public Object put(String name, double value) throws Exception {
        return put(name, new Double(value));
    }

    /**
     * 说明：存放boolean类型的键值对
     *
     * @author:郑其荣 May 12, 2009
     * @param name
     * @param value
     * @return
     * @throws Exception
     */

    public Object put(String name, boolean value) throws Exception {
        return put(name, new Boolean(value));
    }

    /**
     * 说明：根据关键字获取相应的值
     *
     * @author:郑其荣 May 12, 2009
     * @param name
     * @return
     * @throws Exception
     */
    public Object get(String name) throws Exception {
        if (name == null || name.equals("")) {
            throw  new Exception("关键字为空");
        }
        if (!super.containsKey(name.toLowerCase())) {

            if (!super.containsKey((name + "$result").toLowerCase())) {
                throw  new Exception("关键字队列中不存在关键字'" + name + "'");
            }
            name = name + "$result";
        }
        return super.get(name.toLowerCase());
    }

    /**
     * 说明：根据关键字获取相应的值
     *
     * @author:郑其荣 May 12, 2009 modified 2010-5-25 15:16:32 wf
     * @param name
     * @return
     * @throws Exception
     */
    public Object getObject(String name) throws Exception {
        return this.get(name);
    }

    /**
     * 说明：根据关键字获取相应的值,如果不存在返回默认的pdefault。
     *
     * @author:郑其荣 May 12, 2009
     * @modified 2010-5-25 15:16:20
     * @param name
     * @param pdefault
     *            ：当用关键字取不到值时，用pdefault代替。
     * @return
     * @throws Exception
     */
    public Object getObject(String name, Object pdefault) throws Exception {
        if (name == null || name.equals("")) {
            throw  new Exception("关键字为空");
        }
        if (!super.containsKey(name.toLowerCase())) {
            // 当关键字不存在时，查找name+$result,因为Cs客户端有封装$result的用法。
            if (!super.containsKey((name + "$result").toLowerCase())) {
                return pdefault;
            }
            name = name + "$result";
        }
        return super.get(name.toLowerCase());
    }

    @Deprecated
    public Object getObjectPara(String name) throws Exception {
        return this.getObject(name, null);
    }

    /**
     * 说明：取关键字对应的double值
     *
     * @author:郑其荣 May 12, 2009
     * @param name
     * @return
     * @throws Exception
     */
    public double getDouble(String name) throws Exception {
        Object o = getObject(name);
        if (o == null || o.equals("")) {
            return 0.0;
        }
        if (o instanceof Double) {
            return ((Double) o).doubleValue();
        } else {
            return StringUtil.stringToDouble(o.toString());
        }
    }

    /**
     * 取关键字所对应的Double值
     *
     * @param name
     *            关键字名
     * @return 关键字所对应的double值
     */
    public Double getDoubleClass(String name) throws Exception {
        Object o = getObject(name);
        if (o == null) {
            return null;
        }
        if (o.equals("")) {
            return Double.valueOf("0.0");
        }
        if (o instanceof Double) {
            return (Double) o;
        } else {
            return Double.valueOf(o.toString());//
        }
    }

    /**
     * 取关键字所对应的double值 当关键字为空，或者内容为空时取 default的值。
     *
     * @param name
     *            关键字名 pdefault 默认值
     * @return 关键字所对应的Int值
     */
    public double getDouble(String name, double pdefault) throws Exception {
        Object o = getObject(name, Double.valueOf(String.valueOf(pdefault)));
        if (o == null || o.equals("")) {
            return pdefault;
        }
        if (o instanceof Double) {
            return ((Double) o).doubleValue();
        } else {
            return StringUtil.stringToDouble(o.toString());
        }
    }

    @Deprecated
    // 统一废除这种类型的方法，多余
    public double getDoublePara(String name) throws Exception {
        return this.getDouble(name, 0.0);
    }

    /**
     * 取关键字所对应的Int值
     *
     * @param name
     *            关键字名
     * @return 关键字所对应的Int值
     */

    public int getInt(String name) throws Exception {
        Object o = getObject(name);
        if (o == null || o.equals("")) {
            return 0;
        }
        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        } else {
            return StringUtil.stringToInt(o.toString());
        }
    }

    /**
     * 取关键字所对应的Int值
     *
     * @param name
     *            关键字名
     * @return 关键字所对应的Int值
     */

    public Integer getIntClass(String name) throws Exception {
        Object o = getObject(name);
        if (o == null) {
            return null;
        }
        if (o.equals("")) {
            return Integer.valueOf("0");
        }
        if (o instanceof Integer) {
            return (Integer) o;
        } else {
            return Integer.valueOf(o.toString());
        }
    }

    /**
     * 取关键字所对应的Int值
     *
     * @param name
     *            关键字名 pdefault 默认值
     * @return 关键字所对应的Int值
     */
    public int getInt(String name, int pdefault) throws Exception {
        Object o = getObject(name, Double.valueOf(String.valueOf(pdefault)));
        if (o == null || o.equals("")) {
            return pdefault;
        }
        if (o instanceof Integer) {
            return ((Integer) o).intValue();
        } else {
            return StringUtil.stringToInt(o.toString());
        }
    }

    @Deprecated
    public int getIntPara(String name) throws Exception {
        return this.getInt(name, 0);
    }

    /**
     * 取关键字所对应的boolean值
     *
     * @param name
     *            关键字名
     * @return 关键字所对应的boolean值
     */
    public boolean getBoolean(String name) throws Exception {
        Object o = getObject(name);
        if (o == null) {
            return false;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        } else {
            if ("true".equals(o.toString().toLowerCase())) {
                return true;
            } else if ("false".equals(o.toString().toLowerCase())) {
                return false;
            } else {
                throw new Exception("关键字['" + name + "']对应的值不是一个boolean类型的值！");
            }
        }
    }

    /**
     * 取关键字所对应的boolean值
     *
     * @param name
     *            关键字名 boolean pdefault 默认值
     * @return 关键字所对应的boolean值
     */
    public boolean getBoolean(String name, boolean pdefault)
            throws Exception {
        Object o = getObject(name, Boolean.valueOf(pdefault));
        if (o == null || o.equals("")) {
            return pdefault;
        }
        if (o instanceof Boolean) {
            return ((Boolean) o).booleanValue();
        } else {
            if ("true".equals(o.toString().toLowerCase())) {
                return true;
            } else if ("false".equals(o.toString().toLowerCase())) {
                return false;
            } else {
                throw new Exception("关键字['" + name + "']对应的值不是一个boolean类型的值！");
            }
        }
    }

    @Deprecated
    // 统一废除这种类型的方法，多余
    public boolean getBooleanPara(String name) throws Exception {
        return getBoolean(name, false);
    }

    /**
     * 取关键字所对应的Blob值
     *
     * @param name
     *            关键字名 boolean pdefault 默认值
     * @return 关键字所对应的boolean值
     */
    public String getBlob(String name, String pdefault) throws Exception {
        try {
            Object o = getObject(name, pdefault.getBytes("GBK"));
            if (o == null || o.toString().equals("")) {
                return null;
            }
            if (o instanceof byte[]) {
                return (new String((byte[]) o, "GBK"));
            } else {
                if (o instanceof String) {
                    return o.toString();
                } else {
                    throw new Exception("关键字['" + name + "']对应的值不是一个Blob类型的值！");
                }
            }
        } catch (UnsupportedEncodingException e) {
            throw  new Exception("字符集出错");
        }
    }

    /**
     * 取关键字所对应的Blob值
     *
     * @param name
     *            关键字名 boolean pdefault 默认值
     * @return 关键字所对应的boolean值
     */
    public String getBlob(String name) throws Exception {
        Object o = getObject(name);
        if (o == null || o.toString().equals("")) {
            return null;
        }
        if (o instanceof byte[]) {
            try {
                return (new String((byte[]) o, "GBK"));
            } catch (UnsupportedEncodingException e) {
                throw new Exception("字符集出错");
            }
        } else {
            if (o instanceof String) {
                return o.toString();
            } else {
                throw new Exception("关键字['" + name + "']对应的值不是一个Blob类型的值！");
            }
        }
    }

    /**
     *
     * 取关键字所对应的Date值.
     *
     * @param name
     *            关键字
     * @return Date 返回日期
     * @author zqr
     * @date 创建时间 Apr 7, 2010
     * @since V1.0
     */
    public Date getDate(String name) throws Exception {
        Object o = getObject(name);
        if (o == null || o.toString().equals("")) {
            return null;
        }
        if (o instanceof Date) {
            return (Date) o;
        } else {
            return DateUtil.stringToDate(o.toString());
        }
    }

    /**
     *
     * 取关键字所对应的Date值.
     *
     * @param name
     *            关键字
     * @param format
     *            格式转换的格式
     * @return Date 返回日期
     * @author zqr
     * @date 创建时间 Apr 7, 2010
     * @since V3.7
     */
    public Date getDate(String name, String format) throws Exception {
        Object o = getObject(name);
        if (o == null || o.toString().equals("")) {
            return null;
        }
        if (o instanceof Date) {
            return (Date) o;
        } else {
            return DateUtil.stringToDate(o.toString(), format);
        }
    }

    /**
     *
     * 取关键字所对应的Date值.
     *
     * @param name
     *            关键字
     * @param pdefault
     *            默认值
     * @return Date 返回日期
     * @author zqr
     * @date 创建时间 Apr 7, 2010
     * @since V3.7
     */
    public Date getDate(String name, Date pdefault) throws Exception {
        Object o = getObject(name, pdefault);
        if (o == null || o.toString().equals("")) {
            return null;
        }
        if (o instanceof Date) {
            return (Date) o;
        } else {
            return DateUtil.stringToDate(o.toString());
        }
    }

    /**
     * 获取String格式的日期类型.
     * <p>
     * 原始值是String的；本方法是为了String->String的格式转换
     * </p>
     *
     * @param key
     *            关键字
     * @param tagetFormat
     *            期望转成的串的格式
     * @return String 返回的结果集
     * @author zqr
     * @date 创建时间 Apr 7, 2010
     * @since V1.0
     */
    public String getDateToString(String key, String tagetFormat)
            throws Exception {
        Date vdate = getDate(key);
        return DateUtil.FormatDate(vdate, tagetFormat);
    }

    /**
     * 获取String格式的日期类型.
     * <p>
     * 原始值是String的；本方法是为了String->String的格式转换
     * </p>
     *
     * @param key
     *            关键字
     * @param sourceFormat
     *            原数据的格式
     * @param tagetFormat
     *            期望转成的串的格式
     * @return String 返回的结果集
     * @author zqr
     * @date 创建时间 Apr 7, 2010
     * @since V1.0
     */
    public String getDateToString(String key, String sourceFormat,
                                  String tagetFormat) throws Exception {
        Date vdate = getDate(key, sourceFormat);
        return DateUtil.FormatDate(vdate, tagetFormat);
    }

    /**
     * 获取String格式的日期类型.
     * <p>
     * 原始值是String的；本方法是为了String->String的格式转换
     * </p>
     *
     * @param key
     *            关键字
     * @param tagetFormat
     *            期望转成的串的格式
     * @param pdefault
     *            默认值
     * @return String 返回的结果集
     * @author zqr
     * @date 创建时间 Apr 7, 2010
     * @since V1.0
     */
    public String getDateToString(String key, String tagetFormat, Date pdefault)
            throws Exception {
        Date vdate = getDate(key, pdefault);
        return DateUtil.FormatDate(vdate, tagetFormat);
    }

    @Deprecated
    // 统一废除这种类型的方法，多余
    public Date getDatePara(String name) throws Exception {
        return this.getDate(name, (Date) null);
    }

    public Date getStringDate(String name, String format) throws Exception {
        return this.getDatePara(name, format);
    }

    public Date getStringDate(String name, String format, String pdefault)
            throws Exception {
        return this.getDatePara(name, format, pdefault);
    }

    public String getDateParaToString(String key, String sourceFormat,
                                      String tagetFormat) throws Exception {
        Date vdate = getDatePara(key, sourceFormat);
        return DateUtil.FormatDate(vdate, tagetFormat);
    }

    public Date getDatePara(String name, String format) throws Exception {
        Object o = getObject(name);
        if (o == null || o.toString().equals("")) {// 如果当前值为"" 则返回null
            return null;
        } else if (o instanceof String) {
            return DateUtil.stringToDate(o.toString(), format);
        } else {
            throw new Exception("关键字['" + name + "']对应的值不是一个String类型的值！");
        }
    }

    public Date getDatePara(String name, String format, String pdefault)
            throws Exception {
        Object o = getObject(name, pdefault);
        if (o == null || "".equalsIgnoreCase(o.toString())) {
            return null;
        }
        if (o instanceof String) {
            return DateUtil.stringToDate(o.toString(), format);
        } else {
            throw new Exception("关键字['" + name + "']对应的值不是一个String类型的值！");
        }
    }

    /**
     * 取关键字所对应的String值
     *
     * @param name
     *            关键字名
     * @return 关键字所对应的String值
     * @throws Exception
     *             如果关键字所对应值不是String型抛出Exception("Item '" + name + "' is not a
     *             String")
     */
    public String getString(String name) throws Exception {
        Object o = getObject(name);
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String) o;
        } else {
            throw new Exception("关键字['" + name + "']对应的值不是一个String类型的值！");
        }
    }

    /**
     * 取关键字所对应的String值
     *
     * @param name
     *            关键字名 pdefault 默认值
     * @return 关键字所对应的String值
     * @throws Exception
     *             如果关键字所对应值不是String型抛出Exception("Item '" + name + "' is not a
     *             String")
     */
    public String getString(String name, String pdefault) throws Exception {
        Object o = getObject(name, pdefault);
        if (o == null) {
            return null;
        }
        if (o instanceof String) {
            return (String) o;
        } else {
            throw new Exception("关键字['" + name + "']对应的值不是一个String类型的值！");
        }
    }

    @Deprecated
    // 统一废除这种类型的方法，多余
    public String getStringPara(String name) throws Exception {
        return this.getString(name, null);
    }

    /**
     * 取关键字所对应的DataStore值
     *
     * @param name
     *            关键字名
     * @return 关键字所对应的DataStore值
     * @throws Exception
     *             如果关键字所对应值不是DataStore型抛出Exception("Item '" + name + "' is not
     *             a DataStore")
     */

    public DataStore getDataStore(String name) throws Exception {
        Object o = getObject(name);
        if (o == null || o.toString().equals("")) {
            return null;
        }
        if (o instanceof DataStore) {
            return (DataStore) o;
        } else {
            throw new Exception("关键字['" + name + "']对应的值不是一个DataStore类型的值！");
        }
    }

    /**
     * 说明：取关键字所对应的DataStore值
     *
     * @author:郑其荣 May 12, 2009
     * @param name
     * @return
     * @throws Exception
     */
    public DataStore getDataStore(String name, DataStore pdefault)
            throws Exception {
        Object o = getObject(name, pdefault);
        if (o == null || o.toString().equals("")) {
            return null;
        }
        if (o instanceof DataStore) {
            return (DataStore) o;
        } else {
            throw new Exception("关键字['" + name + "']对应的值不是一个DataStore类型的值！");
        }
    }

    @Deprecated
    // 统一废除这种类型的方法，多余
    public DataStore getDataStorePara(String name) throws Exception {
        return this.getDataStore(name, null);
    }

    /**
     * 取关键字所对应的DataObject值
     *
     * @param name
     *            关键字名
     * @return 关键字所对应的DataObject值
     * @throws Exception
     *             如果关键字所对应值不是DataObject型抛出Exception("Item '" + name + "' is not
     *             a DataObject")
     */

    public DataObject getDataObject(String name) throws Exception {
        Object o = getObject(name);
        if (o == null || o.toString().equals("")) {
            return null;
        }
        if (o instanceof DataObject) {
            return (DataObject) o;
        } else {
            throw new Exception("关键字['" + name + "']对应的值不是一个DataObject类型的值！");
        }
    }

    /**
     * 说明：取关键字所对应的DataObject值
     *
     * @author:郑其荣 May 12, 2009
     * @param name
     * @return
     * @throws Exception
     */
    public DataObject getDataObject(String name, DataObject pdefault)
            throws Exception {
        Object o = getObject(name, pdefault);
        if (o == null || o.toString().equals("")) {
            return null;
        }
        if (o instanceof DataObject) {
            return (DataObject) o;
        } else {
            throw new Exception("关键字['" + name + "']对应的值不是一个DataObject类型的值！");
        }
    }

    public DataObject getDataObjectPara(String name) throws Exception {
        return this.getDataObject(name, null);
    }

    /**
     * 删除给定的关键字
     *
     * @param name
     *            关键字名
     * @return 如果关键字有对应的值返回关键字所对应的值，反之返回空。
     * @throws Exception
     */
    public Object remove(String name) throws Exception {
        if (name == null || name.equals("")) {
            throw  new Exception("关键字为空");
        }
        if (!super.containsKey(name.toLowerCase())) {
            // 当关键字不存在时，查找name+$result,因为Cs客户端有封装$result的用法。
            if (!super.containsKey((name + "$result").toLowerCase())) {
                throw new Exception("关键字队列中不存在关键字'" + name + "'");
            }
            name = name + "$result";
        }
        return super.remove(name.toLowerCase());
    }

    // /**
    // * 替换有特使意义的字符
    // *
    // * @param o 要替换的对象
    // * @return 替换后的对象
    // */
    // private Object replaceSpecialChr(Object o) {
    // if (o == null) {
    // return "`k";
    // }
    // return o;
    // }
    //
    // /**
    // * 说明：恢复有特殊意义的字符
    // *
    // * @author:郑其荣 May 12, 2009
    // * @param o
    // * @return
    // */
    //
    // private Object recoverSpecialChr(Object o) {
    // String vtemp;
    // if (o == null) {
    // return null;
    // }
    // vtemp = o.toString();
    // if ("`k".equals(vtemp)) {
    // return null;
    // }
    // return o;
    // }

//	/**
//	 * 说明：为了压缩DataStore，供CS客户端使用。
//	 *
//	 * @deprecated
//	 * @author:郑其荣 May 12, 2009
//	 * @param name
//	 * @param value
//	 * @return
//	 * @throws Exception
//	 *
//	 */
//	public Object putCompressedDataStore(String name, DataStore value)
//			throws Exception {
//		HashMap vTypeMap = new HashMap();
//		EncodeClass code = new EncodeClass();
//		if (name == null || name.equals("")) {
//			Alert.isNull("关键字为空");
//		}
//		if (value == null) {
//			return null;
//		}
//		name = name.toLowerCase();
//		if (value.rowCount() == 0) {
//			return put("loop_count", 0);
//		}
//		String[] vcol = value.getColumnName();
//		String[] vtype = new String[vcol.length];
//		vTypeMap = value.getTypeMap();
//		for (int i = 0; i < vTypeMap.size(); i++) {
//			vtype[i] = (String) vTypeMap.get(vcol[i]);
//		}
//		StringBuffer vstr = new StringBuffer("");
//		StringBuffer vline = null;
//		String vcolumnvalue = "";
//		Date vdate;
//		double vdouble;
//		byte buffer[] = new byte[512];
//		byte buffer2[] = new byte[512];
//
//		for (int vi = 0; vi < value.rowCount(); vi++) {
//			vline = new StringBuffer("");
//			for (int vj = 0; vj < vcol.length; vj++) {
//				if (vtype[vj].equalsIgnoreCase("string")) {
//					vcolumnvalue = value.getString(vi, vcol[vj]);
//					if (vcolumnvalue == null) {
//						vcolumnvalue = "";
//					}
//				} else if (vtype[vj].equalsIgnoreCase("date")) {
//					vdate = value.getDate(vi, vcol[vj]);
//					if (vdate == null) {
//						vcolumnvalue = "";
//					} else {
//						SimpleDateFormat v = new SimpleDateFormat(
//								"yyyy-MM-dd HH:mm:ss");
//						v.setLenient(false);
//						vcolumnvalue = v.format(vdate);
//					}
//				} else if (vtype[vj].equalsIgnoreCase("number")) {
//					vdouble = value.getDouble(vi, vcol[vj]);
//					vcolumnvalue = DataFormat.formatNumber(vdouble);
//				} else {
//					vcolumnvalue = value.getObject(vi, vcol[vj]).toString();
//					if (vcolumnvalue == null) {
//						vcolumnvalue = "";
//					}
//				}
//				// vcolumnvalue = (String) recoverSpecialChr(vcolumnvalue);
//				if (vj < vcol.length) {
//					vline = vline.append(vcolumnvalue + "\t");
//				} else {
//					vline = vline.append(vcolumnvalue);
//				}
//			}
//			vstr = vstr.append(vline.append("\n"));
//		}
//		try {
//			buffer = vstr.toString().getBytes("GBK");
//		} catch (Exception e) {
//			Alert.FormatError("数据错误：字符串 " + vstr + " String转化为GBK byte[]报错!");
//		}
//		buffer2 = code.encodePEMBlock(buffer);
//		long vloop = 0;
//		if (buffer2.length % 10240 == 0) {
//			vloop = buffer2.length / 10240;
//		} else {
//			vloop = buffer2.length / 10240 + 1;
//		}
//		for (int vi = 0; vi < vloop; vi++) {
//			if (buffer2.length > vi * 10240 + 10240) {
//				put(name + vi, new String((byte[]) buffer2, vi * 10240, 10240));
//			} else {
//				put(name + vi, new String((byte[]) buffer2, vi * 10240,
//						buffer2.length - vi * 10240));
//			}
//		}
//		return put("loop_count", vloop);
//	}

    /**
     * 将原有格式的DataObject转换为字符串stringParm
     *
     * @param
     *            parm
     * @return String
     * @throws Exception
     * @throws Exception
     * @throws Exception
     * @deprecated
     */
    public static String dataObjectTostring(DataObject parm)
            throws Exception {
        String name = "";
        String value = "";
        String result = "";
        Object array[] = parm.keySet().toArray();
        for (int i = 0; i < array.length; i++) {
            name = array[i].toString().toLowerCase();
            if (parm.getObject(name) != null) {
                value = parm.getObject(name).toString();
            } else {
                value = "`k";
            }
            result = result + "\n" + name + "\t" + value;
        }
        return result;
    }

    /**
     * @deprecated
     */
    public static DataObject stringToDataObject(String stringParm)
            throws Exception {
        int i = 0;
        String tmp = "";
        String str = stringParm + "\n";
        DataObject parm = new DataObject();
        try {
            do {
                i = str.indexOf("\n");
                if (i < 0) {
                    break;
                } else {
                    if (i == 0) {
                        str = str.substring(1);
                        continue;
                    }
                }
                tmp = str.substring(0, i);
                str = str.substring(i + 1);
                i = tmp.indexOf("\t");
                if (i < 0) {
                    break;
                }
                parm.put(tmp.substring(0, i), tmp.substring(i + 1));
            } while (true);
            return parm;
        } catch (Exception e) {
            throw new Exception("将原有格式的字符串参数转换为DataObject时出错，字符串参数格式错误\n正确地格式是：\\nname\\tvalue\\n...");
        }
    }

    /**
     * 获取当前DataObject中关键字columnName对应的Clob类型值
     *
     * @param columnName
     * @return Clob
     * @throws Exception
     * @since 2.0
     * @author wf 2009-12-2 10:42:02
     * @throws Exception
     * @see  {@link DataStore}
     */
    public Clob getClob(String columnName) throws Exception {
        //
        Object o = getObject(columnName);
        if (o == null) {
            return null;
        }
        if (o instanceof Clob) {
            return (Clob) o;
        } else {
            throw new Exception("关键字['" + columnName + "']对应的值不是一个Clob类型的值！");
        }
    }

    public String toJSON() throws Exception {
        try {
            JSONObject jobj = new JSONObject();
            Set<?> keySet = this.keySet();
            Iterator<?> iterator = keySet.iterator();
            while(iterator.hasNext()){
                String varName = (String)iterator.next();
                Object varValue = this.get(varName);
                if(varValue == null){
                    jobj.put(varName, "");
                }else if(varValue instanceof DataStore){
                    jobj.put(varName, new JSONArray(((DataStore) varValue).toJSON()));
                }else if(varValue instanceof Double){
                    jobj.put(varName, ((Double) varValue).doubleValue());
                }else if(varValue instanceof Date){
                    jobj.put(varName, DateUtil.FormatDate((Date)varValue, "yyyyMMddHHmmss"));
                }else if(varValue instanceof Number){
                    jobj.put(varName, (Number)varValue);
                }else if(varValue instanceof Boolean){
                    jobj.put(varName, (Boolean)varValue);
                }else{
                    jobj.put(varName, varValue.toString());
                }
            }
            return jobj.toString();
        } catch (JSONException e) {
            throw new Exception(e);
        }
    }

    public static DataObject parseJSON(String json) throws Exception {
        DataObject dto = new DataObject();
        try {
            JSONObject jObject = new JSONObject(json);
            Iterator<String> iterator = jObject.keys();
            while (iterator.hasNext()) {
                String varName = (String) iterator.next();
                Object varValue = jObject.get(varName);
                if (varValue == null)
                    dto.put(varName, "");
                else if ((varValue instanceof JSONArray))
                    dto.put(varName, DataStoreUtil.parseJSON(varValue.toString()));
                else if ((varValue instanceof Double))
                    dto.put(varName, ((Double) varValue).doubleValue());
                else if ((varValue instanceof Date))
                    dto.put(varName, DateUtil.FormatDate((Date) varValue, "yyyyMMddHHmmss"));
                else if ((varValue instanceof Number))
                    dto.put(varName, (Number) varValue);
                else if ((varValue instanceof Boolean))
                    dto.put(varName, (Boolean) varValue);
                else {
                    dto.put(varName, varValue.toString());
                }
            }
        } catch (JSONException e) {
            throw new Exception(e);
        }
        return dto;
    }
}
