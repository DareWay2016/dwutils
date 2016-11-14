package cn.com.dareway.dwlibrary.datastoreutils;

import java.sql.Clob;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



public class DataStore extends ArrayList<DataObject> {

	private static final long serialVersionUID = 1L;
	public static final String TYPE_STRING = "string";
	public static final String TYPE_NUMBER = "number";
	public static final String TYPE_DATE = "date";
	public static final String TYPE_BOOLEAN = "boolean";
	public static final String TYPE_NULL = "null";
	public static final String TYPE_STRING_AB = "s"; // 缩写形式, ab作为后缀
	public static final String TYPE_NUMBER_AB = "n";
	public static final String TYPE_DATE_AB = "d";
	public static final String TYPE_BOOLEAN_AB = "b";
	public static final String TYPE_NULL_AB = "l";

	private LinkedHashMap<String, String> columnTypeMap;

	public DataStore() {
		if (columnTypeMap == null)
			columnTypeMap = new LinkedHashMap<String, String>();
	}

	public DataStore(Vector<DataObject> vector) {
		// super(vector);
		if (null == vector)
			return;
		for (Object object : vector) {
			this.addRow((DataObject) object);
		}
	}

	public DataStore(int rowInit) {
		super(rowInit);
		if (columnTypeMap == null)
			columnTypeMap = new LinkedHashMap<String, String>();
	}

	public DataStore(List<DataObject> nds) {
		super(nds);
		if (columnTypeMap == null)
			columnTypeMap = new LinkedHashMap<String, String>();
	}

	@Override
	public boolean contains(Object o) {
		if (!(o instanceof DataObject)) {
			return false;
		}
		return super.contains(o);
	}

	@Override
	@Deprecated
	public DataObject set(int index, DataObject o) {
		if (o != null) {
			try {
				checkDataObjectKeys(o);
			} catch (Exception e) {
				return null;
			}
		}
		return super.set(index, o);
	}

	@Override
	@Deprecated
	public boolean add(DataObject o) {
		if (o != null) {
			try {
				checkDataObjectKeys(o);
			} catch (Exception e) {
				return false;
			}
		}
		return super.add(o);
	}

	@Override
	@Deprecated
	public void add(int index, DataObject o) {
		if (o != null) {
			try {
				checkDataObjectKeys(o);
			} catch (Exception e) {
				return;
			}
		}
		super.add(index, o);
	}

	@Override
	@Deprecated
	public boolean addAll(Collection<? extends DataObject> c) {
		for (DataObject o : c) {
			try {
				checkDataObjectKeys(o);
			} catch (Exception e) {
				return false;
			}
		}
		return super.addAll(c);
	}

	@Override
	@Deprecated
	public boolean addAll(int index, Collection<? extends DataObject> c) {
		for (DataObject o : c) {
			try {
				checkDataObjectKeys(o);
			} catch (Exception e) {
				return false;
			}
		}
		return super.addAll(index, c);
	}

	public final int rowCount() {
		return super.size();
	}

	public Object delRow(int row) throws Exception {
		checkRow(row);
		return super.remove(row);
	}

	public boolean containsItem(int row, String column) throws Exception {
		return getRow(row).containsKey(column);
	}

	public DataObject getRow(int row) throws Exception {
		checkRow(row);
		Object o = super.get(row);
		if (o instanceof DataObject) {
			return (DataObject) o;
		} else {
			throw new Exception("第[" + row + "]行取出的数据不能转换成DataObject");
		}
	}

	public void clear() {
		super.clear();
		this.columnTypeMap = null;
	}

	public void addRow() throws Exception {
		this.add(new DataObject());
	}

	public void addRow(DataObject o) {
		this.add(o);
	}

	public void insertRow(int row, DataObject o) throws Exception {
		if (row != this.rowCount()) {
			this.checkRow(row);
		}
		super.add(row, o);
		if (o != null) {
			checkDataObjectKeys(o);
		}
	}

	public void insertRow(int row) throws Exception {
		this.insertRow(row, new DataObject());
	}

	public void insertRowWithDefaultColumns(int row) throws Exception {
		this.insertRow(row, getRowWithDefaultColumns());
	}

	public void addRowWithDefaultColumns() throws Exception {
		addRow(getRowWithDefaultColumns());
	}

	public Object put(int row, String column, Object value) throws Exception {
		if (row == rowCount()) {
			addRow();
		} else {
			checkRow(row);
		}
		DataObject dbo = getRow(row);
		Object tmp = dbo.put(column, value);
		// super.set(row, dbo); //当用vector作为构造函数时，需要加上。
		this.checkAndSaveColumnType(column, value);
		return tmp;
	}

	public Object put(int row, String column, double value) throws Exception {
		return put(row, column, new Double(value));
	}

	public Object put(int row, String column, int value) throws Exception {
		return put(row, column, new Integer(value));
	}

	public Object put(int row, String column, boolean value)
			throws Exception {
		return put(row, column, new Boolean(value));
	}

	public Object getObject(int row, String column) throws Exception {
		return getRow(row).getObject(column);
	}

	public String getString(int row, String column) throws Exception {
		return getRow(row).getString(column);
	}

	public double getDouble(int row, String column) throws Exception {
		return getRow(row).getDouble(column);
	}

	public Double getDoubleClass(int row, String column) throws Exception {
		return getRow(row).getDoubleClass(column);
	}

	public int getInt(int row, String column) throws Exception {
		return getRow(row).getInt(column);
	}

	public Integer getIntClass(int row, String column) throws Exception {
		return getRow(row).getIntClass(column);
	}

	public boolean getBoolean(int row, String column) throws Exception {
		return getRow(row).getBoolean(column);
	}

	public String getBlob(int row, String column) throws Exception {
		return getRow(row).getBlob(column);
	}

	public Date getDate(int row, String column) throws Exception {
		return getRow(row).getDate(column);
	}

	public Clob getClob(int rowId, String columnName) throws Exception {
		return this.getRow(rowId).getClob(columnName);
	}

	public Date getStringDate(int rowId, String columnName, String format)
			throws Exception {
		return this.getRow(rowId).getDatePara(columnName, format);
	}

	public String getDateToString(int rowId, String columnName,
								  String targetFormat) throws Exception {
		return this.getRow(rowId).getDateToString(columnName, targetFormat);
	}

	public DataStore subDataStore(int beginRow, int endRow) throws Exception {
		if (this.rowCount() == 0)
			return null;
		checkRow(beginRow);
		if (endRow != rowCount()) {
			checkRow(endRow);
		}
		DataStore newDataStore = new DataStore(endRow - beginRow + 1);
		for (int i = beginRow; i < endRow; i++) {
			newDataStore.addRow(getRow(i));
		}
		newDataStore.setTypeList(getTypeList());
		return newDataStore;
	}

	public DataStore combineDatastore(DataStore otherds) throws Exception {
		DataObject row = null;
		for (int i = 0; i < otherds.rowCount(); i++) {
			row = (DataObject) otherds.getRow(i).clone();
			this.addRow(row);
		}
		return this;

	}

	// public Collection ds2Collection() throws Exception {
	// return ds2Collection(this);
	// }
	//
	// public Collection ds2Collection(DataStore ds) throws Exception {
	//
	// }
	public void setTypeList(HashMap<String, String> typeList) {
		LinkedHashMap<String, String> typelist = new LinkedHashMap<String, String>();
		for (String keyword : typeList.keySet()) {
			typelist.put(keyword, typeList.get(keyword));
		}
		this.columnTypeMap = typelist;
	}

	public void setTypeList(LinkedHashMap<String, String> typeList) {
		this.columnTypeMap = typeList;
	}

	public void setTypeList(String typeList) throws Exception {
		if (typeList == null || "".equals(typeList)) {
			throw new Exception("输入的typelist为空");
		}
		String[] tmpList = typeList.split(",");
		String vcolName, vcolType;
		for (int i = 0; i < tmpList.length; i++) {
			if (tmpList[i] == null || "".equals(tmpList[i])) {
				continue;
			}
			if (tmpList[i].split(":") == null
					|| tmpList[i].split(":").length != 2) {
				throw new Exception("typeList的结构不对，正确的结构应该是:colName:coltype,colName:coltype");
			}
			vcolName = tmpList[i].split(":")[0];
			vcolType = tmpList[i].split(":")[1];

			if (columnTypeMap == null) {
				columnTypeMap = new LinkedHashMap<String, String>();
			}
			this.columnTypeMap.put(vcolName.toLowerCase(),
					convertTypeAbbreviation(vcolType));
		}
	}

	public String getTypeList() throws Exception {
		if (this.columnTypeMap == null)
			return null;
		StringBuffer sb = new StringBuffer();
		for (String colName : columnTypeMap.keySet()) {
			String colType = convertTypeAbbreviation((String) columnTypeMap
					.get(colName));
			sb.append(colName).append(":").append(colType).append(",");
		}
		// col_boolean:s,col1:n
		String typeList = sb.toString();

		if (typeList != null && typeList.endsWith(",")) {
			typeList = typeList.substring(0, typeList.length() - 1);
		}
		return typeList;
	}

	public String[] getColumnName() throws Exception {
		if (this.columnTypeMap == null) {
			return null;
		}
		String[] colNames = new String[columnTypeMap.keySet().size()];
		int i = 0;
		for (String tmp : columnTypeMap.keySet()) {
			colNames[i] = tmp;
			i++;
		}
		return colNames;
	}

	// 没看有用的
	// public Object[] getNotNoneColumnValue() throws Exception {
	// if(this.rowCount() == 0)
	// return null;
	//
	// return null;
	// }

	public String getColumnType(String colName) throws Exception {
		String columnType = TYPE_NULL;
		columnType = (String) columnTypeMap.get(colName.toLowerCase());
		// 转换成类名
		if (TYPE_DATE.equals(columnType) || TYPE_DATE_AB.equals(columnType)) {
			columnType = TYPE_DATE;
		} else if (TYPE_NUMBER.equals(columnType)
				|| TYPE_NUMBER_AB.equals(columnType)) {
			columnType = TYPE_NUMBER;
		} else if (TYPE_BOOLEAN.equals(columnType)
				|| TYPE_BOOLEAN_AB.equals(columnType)) {
			columnType = TYPE_BOOLEAN;
		} else if (TYPE_STRING.equals(columnType)
				|| TYPE_STRING_AB.equals(columnType)) {
			columnType = TYPE_STRING;
		} else {
			// 20130116 modi by www 当找不到数据类型时默认使用String
			columnType = TYPE_STRING;
		}
		return columnType;
	}

	public LinkedHashMap<String, String> getTypeMap() throws Exception {
		return this.columnTypeMap;
	}

	public DataStore sort(String colName) throws Exception {
		if (this.rowCount() == 0)
			return this;
		Collections.sort(this, new DataObjectComparator(colName));
		return this;
	}

	public DataStore sortdesc(String column) throws Exception {
		if (this.rowCount() == 0)
			return this;
		Collections.sort(this,
				Collections.reverseOrder(new DataObjectComparator(column)));
		return this;
	}

	public DataStore multiSort(String conditionString) throws Exception {
		if (this.rowCount() == 0)
			return this;
		if (conditionString == null || conditionString.equals(""))
			return this;
		String[] conditions = conditionString.split(",");
		String[] cols = new String[conditions.length];
		String[] colOrders = new String[conditions.length];
		for (int i = 0; i < conditions.length; i++) {
			String[] tmp = conditions[i].split(":");
			cols[i] = tmp[0];
			colOrders[i] = tmp[1];
		}
		Collections.sort(this, new DataObjectMultiComparator(cols, colOrders));
		return this;
	}


	public DataStore clone() {
		DataStore vd = new DataStore();
		try {
			vd.setTypeList(this.getTypeList());
			if (this.rowCount() == 0)
				return vd;
			for (int i = 0; i < this.size(); i++) {
				vd.add(this.getRow(i).clone());
			}
		} catch (Exception e1) {
			// 2012.11.19 钱进 为适应原接口需求，当clone发生意外时，返回空DataStore
			return vd;
		}
		return vd;
	}

	/**
	 * 复杂度O(n2)
	 *
	 * @param condition
	 * @param pBeginRow
	 * @param pEndRow
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public int find(String condition, int pBeginRow, int pEndRow)
			throws Exception {
		if (this.rowCount() == 0)
			return -1;
		Condition[] conditions = getConditions(condition);
		Comparator comparator = new HasNullComparator();
		for (int k = 0; k < conditions.length; k++) {
			String colType = this.getColumnType(conditions[k].columnName);
			if (colType.equals(TYPE_DATE)) {
				conditions[k].setObjValue(DateUtil
						.stringToDate(conditions[k].value));
			}
			if (colType.equals(TYPE_NUMBER)) {
				conditions[k].setObjValue(MathUtil.round(
						Double.parseDouble(conditions[k].value), 10));
			}
			if (colType.equals(TYPE_BOOLEAN)) {
				conditions[k].setObjValue(Boolean
						.parseBoolean(conditions[k].value));
			}
			if (colType.equals(TYPE_STRING)) {
				conditions[k].setObjValue(conditions[k].value);
			}
		}

		for (int i = pBeginRow; i < pEndRow; i++) {
			for (int k = 0; k < conditions.length; k++) {
				Object o = this.getObject(i, conditions[k].columnName);
				if ("==".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) == 0
						|| "!=".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) != 0
						|| ">".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) > 0
						|| "<".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) < 0
						|| ">=".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) >= 0
						|| "<=".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) <= 0) {
					if (k == conditions.length - 1) {
						return i;
					}
				} else {
					break;
				}
			}
		}
		return -1;
	}

	public int find(String condition) throws Exception {
		return this.find(condition, 0, this.rowCount());
	}

	public int find(String condition, int pBeginRow) throws Exception {
		return this.find(condition, pBeginRow, this.rowCount());
	}

	/**
	 * 复杂度O(n2)
	 *
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public DataStore findAll(String condition) throws Exception {
		DataStore result = new DataStore();
		if (this.rowCount() == 0)
			return result;
		Condition[] conditions = getConditions(condition);
		Comparator comparator = new HasNullComparator();
		for (int k = 0; k < conditions.length; k++) {
			String colType = this.getColumnType(conditions[k].columnName);
			if (colType.equals(TYPE_DATE)) {
				conditions[k].setObjValue(DateUtil
						.stringToDate(conditions[k].value));
			}
			if (colType.equals(TYPE_NUMBER)) {
				conditions[k].setObjValue(MathUtil.round(
						Double.parseDouble(conditions[k].value), 10));
			}
			if (colType.equals(TYPE_BOOLEAN)) {
				conditions[k].setObjValue(Boolean
						.parseBoolean(conditions[k].value));
			}
			if (colType.equals(TYPE_STRING)) {
				conditions[k].setObjValue(conditions[k].value);
			}
		}
		result.setTypeList(this.columnTypeMap);
		for (int i = 0; i < this.rowCount(); i++) {
			for (int k = 0; k < conditions.length; k++) {
				Object o = this.getObject(i, conditions[k].columnName);
				if ("==".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) == 0
						|| "!=".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) != 0
						|| ">".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) > 0
						|| "<".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) < 0
						|| ">=".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) >= 0
						|| "<=".equals(conditions[k].operator)
						&& comparator.compare(o, conditions[k].objValue) <= 0) {
					if (k == conditions.length - 1) {
						result.addRow(this.get(i));
					}
				} else {
					break;
				}
			}
		}
		return result;
	}

	public DataStore filter(String conditions) throws Exception {
		/**
		 * 前置条件检验
		 */
		if (this.rowCount() == 0) {
			return new DataStore();
		}

		if (conditions == null) {
			return this;
		}
		conditions = conditions.trim();
		if ("".equals(conditions)) {
			return this;
		}


		/**
		 * 表达式分析
		 */
		String regx4LogicalOperator = "(?<=(\\w|[\\u4e00-\\u9fa5])\\s{0,10})(\\band\\b|\\bor\\b)(?=\\s+(\\w|[\\u4e00-\\u9fa5]))";
		Pattern pattern4LogicalOperator = Pattern.compile(regx4LogicalOperator);
		Matcher matcher4LogicalOperator = pattern4LogicalOperator.matcher(conditions);
		String[] expStrArr = conditions.split(regx4LogicalOperator); 		// 表达式数组["a==b", "c>=d", ...]
		ArrayList<DataObject> expInfoObjArr = new ArrayList<DataObject>(); 	// 解析后的表达式数组[用DataObject表示的"a==n", ...];
		ArrayList<String> logicOps = new ArrayList<String>();				// 逻辑运算符数组["and", "or", ...]
		while (matcher4LogicalOperator.find()) {
			logicOps.add(matcher4LogicalOperator.group());
		}

		for(int i=0; i<expStrArr.length; i++){
			String currentExp = expStrArr[i].trim();
			String regx4ComparisonOperator = "(?<=\\s)(==|<|>|<=|>=|!=)(?=\\s)";
			Pattern pattern4ComparisonOperator = Pattern.compile(regx4ComparisonOperator);
			Matcher matcher4ComparisonOperator = pattern4ComparisonOperator.matcher(currentExp);
			if(!matcher4ComparisonOperator.find()){
				throw new Exception("在表达式【"+currentExp+"】中未找到比较运算符【==、<、>、<=、>=、!=】!");
			}

			// 找到表达式中的key、operator、value
			String operator = matcher4ComparisonOperator.group().trim();
			String key = currentExp.split(regx4ComparisonOperator)[0].trim();
			String value = currentExp.split(regx4ComparisonOperator)[1].trim();

			DataObject o = new DataObject();
			o.put("cmpKey", key);
			o.put("cmpOperator", operator);
			o.put("cmpValue", value);
			expInfoObjArr.add(o);
		}


		/**
		 * 转换原始字符串类型【待比较数据】到特定类型的【待比较数据】
		 */
		for (int i = 0; i < expInfoObjArr.size(); i++) {
			DataObject exp = expInfoObjArr.get(i);
			String colName = exp.getString("cmpKey");
			String cmpValue = exp.getString("cmpValue");
			String colType = this.getColumnType(colName);
			if (colType.equals(TYPE_DATE)) {
				exp.put("cmpRealValue", DateUtil.stringToDate(cmpValue));
			}else if (colType.equals(TYPE_NUMBER)) {
				exp.put("cmpRealValue", MathUtil.round(Double.parseDouble(cmpValue), 10));
			}else if (colType.equals(TYPE_BOOLEAN)) {
				exp.put("cmpRealValue", Boolean.parseBoolean(cmpValue));
			}else if (colType.equals(TYPE_STRING)) {
				exp.put("cmpRealValue", cmpValue);
			}else{
				throw new Exception("数据集中的列【"+colName+"】的数据类型【"+colType+"】不正确!");
			}
		}

//		/**
//		 * 数据筛选，选择出待删除的行
//		 */
//		JexlEngine jexl = new JexlBuilder().cache(512).strict(true).silent(false).create();
//		for (int i = this.rowCount() -1; i >= 0; i--) {
//			JexlContext context = new MapContext();
//			StringBuffer sb = new StringBuffer();
//			for (int k = 0; k < expInfoObjArr.size(); k++) {
//				DataObject exp = expInfoObjArr.get(k);
//				String columnName = exp.getString("cmpKey");
//				String operator = exp.getString("cmpOperator"); // >、>=、 <、 <=、 ==、 !=
//				Object cmpLVal = this.getObject(i, columnName); // a >= b 中的 a
//				Object cmpRVal = exp.getObject("cmpRealValue"); // a >= b 中的 b
//				String cmpLValName = "__CL__"+columnName+"_"+k;
//				String cmpRValName = "__CR__"+columnName+"_"+k;
//
//				if("==".equals(operator)){
//					sb.append(cmpLValName+"._eq("+cmpRValName+")");
//				}else if("!=".equals(operator)){
//					sb.append(cmpLValName+"._neq("+cmpRValName+")");
//				}else if(">".equals(operator)){
//					sb.append(cmpLValName+"._gt("+cmpRValName+")");
//				}else if(">=".equals(operator)){
//					sb.append(cmpLValName+"._gte("+cmpRValName+")");
//				}else if("<".equals(operator)){
//					sb.append(cmpLValName+"._lt("+cmpRValName+")");
//				}else if("<=".equals(operator)){
//					sb.append(cmpLValName+"._lte("+cmpRValName+")");
//				}else{
//					throw new Exception("表达式【"+conditions+"】中存在不正确的运算符【"+operator+"】!");
//				}
//
//				if(k<expInfoObjArr.size() - 1){
//					if(logicOps.get(k).trim().equalsIgnoreCase("and")){
//						sb.append(" && ");
//					}else if(logicOps.get(k).trim().equalsIgnoreCase("or")){
//						sb.append(" || ");
//					}else{
//						throw new Exception("表达式【"+conditions+"】中存在不正确的逻辑运算符【"+logicOps.get(k).trim()+"】，请检查!");
//					}
//				}
//
//
//				context.set(cmpLValName, new DSFilterComparator(cmpLVal));
//				context.set(cmpRValName, cmpRVal);
//			}
//
//			// 表达式计算，删除不匹配的行
//			JexlExpression e = jexl.createExpression(sb.toString());
//			Object o = e.evaluate(context);
//			if (o instanceof Boolean) {
//				if((Boolean) o == false){
//					this.remove(i);
//				}
//			}else{
//				throw new Exception("表达式【"+sb.toString()+"】的计算结果不是boolean类型值！");
//			}
//		}
		return this;
	}

	@Deprecated
	// 可以通过put(int row, String column,null)实现，效率高
	public Object delItem(int row, String column) throws Exception {
		return getRow(row).remove(column);
	}

	public void delColumn(String columnName) throws Exception {
		for (int i = 0, count = this.size(); i < count; i++) {
			if (this.containsItem(i, columnName)) {
				this.delItem(i, columnName);
			}
		}
		if (this.columnTypeMap != null
				&& this.columnTypeMap.containsKey(columnName)) {
			this.columnTypeMap.remove(columnName);
		}
	}

	public Collection<Object[]> ds2Collection() throws Exception {
		return ds2Collection(this);
	}

	public Collection<Object[]> ds2Collection(DataStore ds) throws Exception {
		if (ds == null)
			return null;
		Collection<Object[]> collection = new ArrayList<Object[]>();

		String[] colnames = ds.getColumnName();
		for (DataObject dataObject : ds) {
			Object[] objects = new Object[colnames.length];
			for (int j = 0; j < colnames.length; j++) {
				if (!dataObject.containsKey(colnames[j])) {
					objects[j] = null;
					continue;
				} else {
					objects[j] = dataObject.get(colnames[j]);
				}
			}
			collection.add(objects);
		}
		return collection;
	}

	public Collection<Object> getColumn(String column) throws Exception {
		ArrayList<Object> l = new ArrayList<Object>();
		for (int i = 0; i < rowCount(); i++) {
			l.add(i, getObject(i, column));
		}
		return l;
	}

	public String expToString() throws Exception {
		StringBuffer vstr = new StringBuffer("");
		StringBuffer vline = null;
		for (DataObject vdo : this) {
			String[] colNames = this.getColumnName();
			vline = new StringBuffer("");
			for (int i = 0; i < colNames.length; i++) {
				String vcolumnvalue = null;
				if (this.getColumnType(colNames[i]).equals(TYPE_STRING)) {
					vcolumnvalue = vdo.getString(colNames[i]);
				} else if (this.getColumnType(colNames[i]).equals(TYPE_DATE)) {
					Date vdate = vdo.getDate(colNames[i]);
					if (vdate == null) {
						vcolumnvalue = "";
					} else {
						vcolumnvalue = DateUtil.FormatDate(vdate);
					}
				} else if (this.getColumnType(colNames[i]).equals(TYPE_NUMBER)) {
					double vdouble = vdo.getDouble(colNames[i]);
					vcolumnvalue = DataFormat.formatDouble(vdouble);
				} else {
					vcolumnvalue = vdo.get(colNames[i]).toString();
					if (vcolumnvalue == null) {
						vcolumnvalue = "";
					}
				}

				vcolumnvalue = vcolumnvalue.replaceAll("\n", "`n");
				vcolumnvalue = vcolumnvalue.replaceAll("\t", "`t");
				vcolumnvalue = vcolumnvalue.replaceAll("\r", "`r");
				if (i < colNames.length) {
					vline = vline.append(vcolumnvalue + "\t");
				} else {
					vline = vline.append(vcolumnvalue);
				}
			}
			vstr = vstr.append(vline.append("\n"));
		}
		return vstr.toString();
	}

	public void impFromString(String pStr) throws Exception {
		String vtypelist = "", vcol[] = new String[100], vtype[] = new String[200];
		String vline = null;
		String vstr = null, vvalue = null;
		int vcount = 0, vpos = 0;
		vtypelist = this.getTypeList();
		if ("".equals(vtypelist) || vtypelist == null) {
			throw new Exception("导入datastore 时,datastore 没有属性列表");
		}
		while (vtypelist.length() > 0) {
			vpos = vtypelist.indexOf(":");
			if (vpos < 0) {
				break;
			}
			vcol[vcount] = vtypelist.substring(0, vpos);
			vtype[vcount] = vtypelist.substring(vpos + 1, vpos + 2);
			if (vtypelist.length() > vpos + 3) {
				vtypelist = vtypelist.substring(vpos + 3);
			} else {
				break;
			}
			vcount++;
		}
		vstr = pStr;
		if (vstr == null) {
			vstr = "";
		}
		while (vstr.length() > 0) {
			this.addRowWithDefaultColumns();
			vpos = vstr.indexOf("\n");
			if (vpos < 0) {
				vline = vstr;
				vstr = "";
			} else {
				vline = vstr.substring(0, vpos);
				vstr = vstr.substring(vpos + 1);
			}
			if (vline == null) {
				vline = "";
			}
			for (int vi = 0; vi <= vcount; vi++) {
				vpos = vline.indexOf("\t");
				if (vpos < 0) {
					vvalue = vline;
					vline = "";
				} else {
					vvalue = vline.substring(0, vpos);
					vline = vline.substring(vpos + 1);
				}
				vvalue = vvalue.replaceAll("`n", "\n");
				vvalue = vvalue.replaceAll("`t", "\t");
				vvalue = vvalue.replaceAll("`r", "\r");
				if ("s".equals(vtype[vi])) {
					this.put(this.rowCount() - 1, vcol[vi], vvalue);
				} else if ("d".equals(vtype[vi])) {
					if (vvalue == null || "".equals(vvalue)) {
						this.put(this.rowCount() - 1, vcol[vi], null);
					} else {
						this.put(this.rowCount() - 1, vcol[vi],
								DateUtil.stringToDate(vvalue));
					}
				} else if ("n".equals(vtype[vi])) {
					if (vvalue == null || "".equals(vvalue)) {
						this.put(this.rowCount() - 1, vcol[vi], 0);
					} else {
						if (MathUtil.isNumber(vvalue)) {
							this.put(this.rowCount() - 1, vcol[vi],
									StringUtil.stringToDouble(vvalue));
						} else {
							this.put(this.rowCount() - 1, vcol[vi], 0);
						}
					}
				} else {
					this.put(this.rowCount() - 1, vcol[vi], null);
				}
			}
		}
	}

	private Condition[] getConditions(String conditions) throws Exception {
		int pos, posBlank;
		String subCondition;
		conditions = conditions + " and ";
		ArrayList<Condition> resultList = new ArrayList<Condition>();
		while (true) {
			pos = conditions.indexOf(" and");
			if (pos < 0) {
				break;
			}
			subCondition = conditions.substring(0, pos).trim();
			posBlank = subCondition.indexOf(" ");
			if (posBlank < 0) {
				throw new Exception("DataStore Find 条件出错，正确的格式为\n<列名 比较符 值> and !!");
			}
			String columnName = subCondition.substring(0, posBlank).trim();
			// String columnType = null;
			String operator, value;
			// if (this.columnTypeMap == null
			// || !this.columnTypeMap.containsKey(columnName)) {
			// columnType = TYPE_STRING_AB;// "s"
			// } else {
			// columnType = convertTypeAbbreviation((String) this.columnTypeMap
			// .get(columnName));
			// }

			subCondition = subCondition.substring(posBlank + 1);
			posBlank = subCondition.indexOf(" ");
			if (posBlank < 0) {
				throw new Exception("DataStore Find 条件出错，正确的格式为\n<列名 比较符 值> and !!");
			}
			operator = subCondition.substring(0, posBlank).trim();
			value = subCondition.substring(posBlank + 1).trim();

			Condition condition = new Condition();
			condition.setColumnName(columnName);
			condition.setOperator(operator);
			condition.setValue(value);
			resultList.add(condition);
			conditions = conditions.substring(pos + 4);
		}
		// conditions = conditions + " and ";
		// String[] strConditions = conditions.split(" and ");
		// Condition[] results = new Condition[strConditions.length];
		// for (int i = 0; i < strConditions.length; i++) {
		// String strCon = strConditions[i];
		// String[] cons = strCon.trim().split(" +");
		// if (cons.length != 3) {
		// Alert.FormatError("DataStore Find 条件出错，正确的格式为\n<列名 比较符 值> and !!");
		// }
		// Condition condition = new Condition();
		// condition.setColumnName(cons[0]);
		// condition.setOperator(cons[1]);
		// condition.setValue(cons[2]);
		// results[i] = condition;
		// }
		Condition[] result = new Condition[resultList.size()];
		resultList.toArray(result);
		return result;
	}

	private void checkDataObjectKeys(DataObject o) throws Exception {
		if (o != null) {
			@SuppressWarnings("unchecked")
			Iterator<Object> it = o.keySet().iterator();
			while (it.hasNext()) {
				String colName = (String) it.next();
				checkAndSaveColumnType(colName, o.get(colName));
			}
		}
	}

	private void checkAndSaveColumnType(String colName, Object colData) {
		if (columnTypeMap == null) {
			columnTypeMap = new LinkedHashMap<String, String>();
		}
		colName = colName.toLowerCase();
		// 该列未在map中定义,或已有定义但type为NULL时,更新typeMap
		if (!columnTypeMap.containsKey(colName)
				|| columnTypeMap.get(colName).equals(TYPE_NULL)) {
			columnTypeMap.put(colName, getObjectType(colData));
		}
	}

	private void checkRow(int row) throws Exception {
		if (row < 0 || row >= rowCount()) {
			throw new Exception("无效行号:" + row + ",当前DataStore共有" + rowCount() + "行");
		}
	}

	private static String getObjectType(Object o) {
		String type = TYPE_NULL; // 缺省
		String cname = null;
		if (o == null) {
			return type;
		}
		cname = o.getClass().getName();
		if (cname.equals("java.lang.String")) {
			type = TYPE_STRING;
		} else if (cname.equals("java.lang.Double")
				|| cname.equals("java.lang.Integer")) {
			type = TYPE_NUMBER;
		} else if (cname.equals("java.lang.Boolean")) {
			type = TYPE_BOOLEAN;
		} else if (cname.equals("java.lang.Long")
				|| cname.equals("java.math.BigDecimal")) {
			type = TYPE_NUMBER;
		} else if (cname.equals("java.util.Date")
				|| cname.equals("java.sql.Date")
				|| cname.equals("java.sql.Timestamp")) {
			type = TYPE_DATE;
		}
		return type;
	}

	private static String convertTypeAbbreviation(String colType) {
		if (colType == null) {
			return null;
		}
		if (colType.equals(TYPE_STRING)) {
			return TYPE_STRING_AB;
		} else if (colType.equals(TYPE_NUMBER)) {
			return TYPE_NUMBER_AB;
		} else if (colType.equals(TYPE_DATE)) {
			return TYPE_DATE_AB;
		} else if (colType.equals(TYPE_BOOLEAN)) {
			return TYPE_BOOLEAN_AB;
		} else if (colType.equals(TYPE_NULL)) {
			return TYPE_NULL_AB;
		} else if (colType.equals(TYPE_STRING_AB)) {
			return TYPE_STRING;
		} else if (colType.equals(TYPE_NUMBER_AB)) {
			return TYPE_NUMBER;
		} else if (colType.equals(TYPE_DATE_AB)) {
			return TYPE_DATE;
		} else if (colType.equals(TYPE_BOOLEAN_AB)) {
			return TYPE_BOOLEAN;
		} else if (colType.equals(TYPE_NULL_AB)) {
			return TYPE_NULL;
		}
		return null;
	}

	private DataObject getRowWithDefaultColumns() throws Exception {
		DataObject dob = new DataObject();
		// 自动将typelist中各列都置为空值
		String cols[] = this.getColumnName();
		for (int i = 0; i < cols.length; i++) {
			dob.put(cols[i], null);
		}
		return dob;
	}

	public String toJSON() throws Exception {
		try {
			JSONArray jArrData = new JSONArray();
			for (int i = 0; i < this.size(); i++) {
				DataObject row = this.getRow(i);
				jArrData.put(new JSONObject(row.toJSON()));
			}
			return jArrData.toString();
		} catch (JSONException ex) {
			throw new Exception(ex);
		}
	}

}
