package service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class MapAPIService {

    public static String getLongLat(String input) throws ClientProtocolException, IOException {
        try {
            String address= URLEncoder.encode(input, StandardCharsets.UTF_8.toString());
            String url= config.Config.GoongMapAPIConfig.API_GEO_CODE+"api_key="+config.Config.GoongMapAPIConfig.API_KEY+"&address="+address;
            service.Logging.logger.info("Geocoding request URL: {}", url);
            String geoCodeJson= Request.Get(url).addHeader("Accept", "application/json").execute().handleResponse(responseHandler);
            service.Logging.logger.info("Geocoding response: {}", geoCodeJson);
            JsonObject jobj = new Gson().fromJson(geoCodeJson, JsonObject.class);
            Map<String,JsonElement> codeMap = jobj.getAsJsonArray("results").get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject().asMap();
            return codeMap.get("lat")+","+codeMap.get("lng");
        } catch (Exception e) {
            service.Logging.logger.error("Error in getLongLat for input: " + input, e);
            throw e;
        }
        
    } 

    private static final ResponseHandler<String> responseHandler= response -> {
        int status = response.getStatusLine().getStatusCode();
        if(status>=200 && status<300)
		{
			HttpEntity entity= response.getEntity();
			return entity!=null ? EntityUtils.toString(entity):null;
		}
		else
		{
			throw new ClientProtocolException("Unexpected status code: "+status);
		}
    };

    public static String getDistance(String originCode, String destinationCode) throws ClientProtocolException, IOException {
        try {
            String encodedOriginCode = URLEncoder.encode(originCode, StandardCharsets.UTF_8.toString());
            String encodedDestinationCode = URLEncoder.encode(destinationCode, StandardCharsets.UTF_8.toString());
            String url = config.Config.GoongMapAPIConfig.API_GEO_DIRECTION+"origin="+encodedOriginCode+"&destination="+encodedDestinationCode+"&vehicle=car&api_key="+config.Config.GoongMapAPIConfig.API_KEY;
            String directionJson = Request.Get(url).addHeader("Accept", "application/json").execute().handleResponse(responseHandler);
            JsonObject jobj = new Gson().fromJson(directionJson, JsonObject.class);
            String distance = jobj.getAsJsonArray("routes").get(0).getAsJsonObject().getAsJsonArray("legs").get(0).getAsJsonObject().get("distance").getAsJsonObject().get("text").getAsString();
            return distance;
        } catch (Exception e) {
            service.Logging.logger.error("Error in getDistance for originCode: " + originCode + " and destinationCode: " + destinationCode, e);
            throw e;
        }
    }

    public static String getAddressAuto(String query) throws ClientProtocolException, IOException {
        String input= URLEncoder.encode(query, StandardCharsets.UTF_8.toString());
        String url= config.Config.GoongMapAPIConfig.API_AUTOCOMPLETE_LINK+"api_key="+config.Config.GoongMapAPIConfig.API_KEY+"&input="+input;
        String address= Request.Get(url).addHeader("Accept", "application/json").execute().handleResponse(responseHandler);
        return address;
    }

    public static Double getShippingFee(String distance) {
        Double distanceNum = Double.parseDouble( distance.split(" ")[0]);
        String unit = distance.split(" ")[1];
        switch (unit) {
            // if < 1km
            case "m":
                return 5.0;
            case "km":
                if (distanceNum <= 10) 
                    return 10.0;
                if(distanceNum <= 25)
                    return 15.0;
                if (distanceNum <= 50) 
                    return 20.0;
                if(distanceNum <= 100)
                    return 25.0;
                else
                {
                    //max 200
                    double shippingfee = 25.0 + ((distanceNum-100) * 2.0);
                    return shippingfee > 200.0 ? 200.0:shippingfee ;
                }
        }
        return null;
    }
}
