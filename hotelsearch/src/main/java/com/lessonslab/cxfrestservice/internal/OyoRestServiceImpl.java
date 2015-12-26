package com.lessonslab.cxfrestservice.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response;

import org.apache.http.client.ClientProtocolException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import com.lessonslab.cxfrestservice.OyoRestService;
import com.oyo.util.OyoApiCaller;

public class OyoRestServiceImpl implements OyoRestService {

	public Response getHotelDetails(String id) {
		// TODO Auto-generated method stub
		Map<String, Double> map = null;
		Map<String, Object> output = new HashMap<String, Object>();

		try {
			map = OyoApiCaller.call();
			Iterator<Map.Entry<String, Double>> iterator = map.entrySet()
					.iterator();
			int i = 1;
			List<String> size = new ArrayList<String>();
			size.add(Integer.toString(map.size()));
			output.put("Number of Hotels", size);
			output.put("hotel size", map.size());
			Map<String, Object> outputest = new HashMap<String, Object>();
			outputest.put("Test", map.size());
			output.put("MapTEst",outputest);
			List<String> out = null;
			out = new ArrayList<String>();
			while (iterator.hasNext()) {
				Map.Entry<String, Double> roomEntry = iterator.next();
				/*
				 * String s = "name: " + roomEntry.getKey() + "," + "value: " +
				 * roomEntry.getValue();
				 */
			
				out.add("name : " + roomEntry.getKey() + "," + "value "
						+ roomEntry.getValue());

				/*
				 * output.put("name ", roomEntry.getKey()); output.put("value ",
				 * roomEntry.getValue());
				 */
				i++;
			}
			output.put("Hotel List", out);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObjectMapper obj = new ObjectMapper();
		String res = null;
		try {
			res = obj.writeValueAsString(output);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Response.ok(res).build();
	}
}
