package cn.com.dareway.dwlibrary.datastoreutils;

import org.json.JSONArray;
import org.json.JSONException;


public class DataStoreUtil {
	
	public static DataStore parseJSON(String json) throws Exception {
	DataStore dso = new DataStore();
	try {
		JSONArray array = new JSONArray(json);
		for (int i = 0; i < array.length(); i++) {
			DataObject dto = DataObject.parseJSON(array.get(i).toString());
			dso.addRow(dto);
		}
	} catch (JSONException e) {
		throw new Exception(e);
	}
	return dso;
}

}
