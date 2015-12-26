package com.oyo.util;

import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OyoApiCaller {

	public String getOyoApiData(Double longitude, Double latitude)
			throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(
				"http://utilities-oyorooms.herokuapp.com/api/v2/search/hotels?filters[coordinates][longitude]="
						+ longitude
						+ "&filters[coordinates][latitude]="
						+ latitude
						+ "&filters[coordinates][distance]=20&fields=name,longitude,latitude,oyo_id&access_token=MXB2cE44LWJGaTViWExHQ0xCOC06VUtucEhhVV9mclNNeWdrNFBveFY=&additional_fields=room_pricing");
		CloseableHttpResponse response = httpclient.execute(httpget);

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String json = EntityUtils.toString(entity);
		//	System.out.println(json);
			response.close();
			return json;

		}
		response.close();

		return null;
	}

	public static boolean getGoogleLocation(Double latitude, Double longitude,
			String type, Double radius) throws ClientProtocolException,
			IOException, JSONException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(
				"https://maps.googleapis.com/maps/api/place/search/json?location="
						+ latitude + "," + longitude + "&radius=" + radius
						+ "&sensor=true&type=" + type
						+ "&key=AIzaSyCiUL3FVScMAT9pXvETbzMQqNcuek2C2WQ");
		CloseableHttpResponse response = httpclient.execute(httpget);

		HttpEntity entity = response.getEntity();
		if (entity != null) {
			String json = EntityUtils.toString(entity);
			JSONObject res = new JSONObject(json);
			String status = (String) res.get("status");
			boolean weight = status.equalsIgnoreCase("OK");
	//		System.out.println(weight);
			response.close();
			return weight;

		}
		response.close();

		return false;
	}

	public HashMap<String, Double> formatOyoResponse(String json)
			throws JSONException, ClientProtocolException, IOException {
		JSONObject jsonObject = new JSONObject(json);
		HashMap<String, Double> oyoroomresponse = new HashMap<String, Double>();
		int counter = Integer.parseInt(jsonObject.get("count").toString());
//		System.out.println(counter);
		// oyoroomresponse.put("count", Integer.toString(counter));
		JSONArray hotelarray = jsonObject.getJSONArray("hotels");
		for (int i = 0; i < hotelarray.length(); i++) {
			double priceweight = 0;
			double distanceweight = 0;
			JSONObject obj = (JSONObject) hotelarray.get(i);
		//	System.out.println(obj.get("oyo_id"));
			String[] pricing = (obj.getJSONArray("pricing").toString())
					.replace("[", "").replace("]", "").split(",");
			for (String price : pricing) {
				if (Integer.parseInt(price) == 0) {
					priceweight = priceweight + 20;
				} else if (Integer.parseInt(price) < 4000) {
					priceweight = priceweight
							+ ((20 * (4000 - Integer.parseInt(price))) / 4000);
				} else {
					priceweight = priceweight + 0;
				}
	//			System.out.println(priceweight);

			}
			Double distance = (Double) obj.get("distance");
			if (distance == 0) {
				distanceweight = 20;
			} else if (distance < 3) {
				distanceweight = (20 * (3 - distance) / 3);
			} else {
				distanceweight = 0;
			}

			// System.out.println(obj.get("pricing"));
			double airportweightage = 0;
			double hospitalweightage = 0;

			boolean apiResultForAirport = OyoApiCaller.getGoogleLocation(
					(Double) obj.get("latitude"),
					(Double) obj.get("longitude"), "airport",
					Double.parseDouble("3000"));
			if (!apiResultForAirport) {
				airportweightage = 0;
			}

			else {
				apiResultForAirport = OyoApiCaller.getGoogleLocation(
						(Double) obj.get("latitude"),
						(Double) obj.get("longitude"), "airport",
						Double.parseDouble("0"));
				if (apiResultForAirport) {
					airportweightage = 30;
				} else {
					double end = 3000;
					double temp = 0;

					while (apiResultForAirport) {
						apiResultForAirport = OyoApiCaller.getGoogleLocation(
								(Double) obj.get("latitude"),
								(Double) obj.get("longitude"), "airport",
								Double.parseDouble(Double.toString(end - 500)));
						temp = end - 500;
						end = end - 500;
					}

					airportweightage = (30 * (3000 - temp)) / 3000;
				}
			}

			boolean apiResultForHospital = OyoApiCaller.getGoogleLocation(
					(Double) obj.get("latitude"),
					(Double) obj.get("longitude"), "hospital",
					Double.parseDouble("3000"));
			if (!apiResultForHospital) {
				hospitalweightage = 0;
			}

			else {
				apiResultForHospital = OyoApiCaller.getGoogleLocation(
						(Double) obj.get("latitude"),
						(Double) obj.get("longitude"), "hospital",
						Double.parseDouble("0"));
				if (apiResultForHospital) {
					hospitalweightage = 30;
				} else {
					double end = 3000;
					double temp = 0;

					while (apiResultForHospital) {
						apiResultForAirport = OyoApiCaller.getGoogleLocation(
								(Double) obj.get("latitude"),
								(Double) obj.get("longitude"), "hospital",
								Double.parseDouble(Double.toString(end - 500)));
						temp = end - 500;
						end = end - 500;
					}

					hospitalweightage = (30 * (3000 - temp)) / 3000;
				}
			}

			double finalWeight = hospitalweightage + airportweightage
					+ priceweight + distanceweight;

			/*
			 * oyoroomresponse.put((String) obj.get("oyo_id"),
			 * Double.toString(finalWeight) + "," + obj.get("name"));
			 */
			oyoroomresponse
					.put((String) obj.get("name") + ","
							+ (String) obj.get("oyo_id"),
							finalWeight);

		}
		return oyoroomresponse;

	}

	
	
	public static  Map<String, Double> call() throws ClientProtocolException,
			IOException, JSONException {
		OyoApiCaller oyo = new OyoApiCaller();
		Double longitude = 77.6416611;
		Double latitude = 12.9509468;
		HashMap<String, Double> list1 = oyo.formatOyoResponse(oyo.getOyoApiData(
				longitude, latitude));
		Map<String, Double> unsortedMap = new TreeMap<String, Double>(list1);
		/*Iterator<Map.Entry<String, Double>> iterator = unsortedMap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Double> roomEntry = iterator.next();
			System.out.println(roomEntry.getKey() + " :: "
					+ roomEntry.getValue());
		}*/
		ValueComparator valuecomp=new ValueComparator(unsortedMap);
		Map<String,Double> sortedMap=new TreeMap<String, Double>(valuecomp);
		sortedMap.putAll(unsortedMap);
		Iterator<Map.Entry<String, Double>> iterator1 = sortedMap.entrySet()
				.iterator();
		System.out.println("sorted Map");
		while (iterator1.hasNext()) {
			Map.Entry<String, Double> roomEntry = iterator1.next();
			System.out.println(roomEntry.getKey() + " :: "
					+ roomEntry.getValue());
		}

		
		return sortedMap;
	}
	
	
	
}

class ValueComparator implements Comparator
{
   Map<String,Double> map;
   public ValueComparator(Map map)
   {
	   this.map=map;
   }
	public int compare(Object o1, Object o2) {
		// TODO Auto-generated method stub
		
		if(map.get(o1)>map.get(o2))
		{
			return -1;
		}
		else
		{
			return 1;
		}

	}
	
}
