package service;

import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LoginService {
 
	public static String getGoogleToken(String code) throws ClientProtocolException, IOException {
		String response= Request.Post(config.Config.GGLoginConfig.GOOGLE_LINK_GET_TOKEN).bodyForm(
			Form.form().add("client_id", config.Config.GGLoginConfig.GOOGLE_CLIENT_ID)
						.add("client_secret", config.Config.GGLoginConfig.GOOGLE_CLIENT_SECRET)
						.add("redirect_uri", config.Config.GGLoginConfig.GOOGLE_REDIRECT_URI)
						.add("code", code)
						.add("grant_type", config.Config.GGLoginConfig.GOOGLE_GRANT_TYPE)
						.build()).execute().returnContent().asString();
		JsonObject jobj= new Gson().fromJson(response, JsonObject.class);
		String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
		return accessToken;
	}

    public static String getGGToken(String code) throws ClientProtocolException,IOException  {
		Request request = Request.Post(config.Config.GGLoginConfig.GOOGLE_LINK_GET_TOKEN).bodyForm
		(Form.form().add("client_id", config.Config.GGLoginConfig.GOOGLE_CLIENT_ID)
					.add("client_secret", config.Config.GGLoginConfig.GOOGLE_CLIENT_SECRET)
					.add("redirect_uri", config.Config.GGLoginConfig.GOOGLE_REDIRECT_URI)
					.add("code", code)
					.add("grant_type", config.Config.GGLoginConfig.GOOGLE_GRANT_TYPE)
					.build());

		request.setHeader("Content-type", "application/x-www-form-urlencoded");

		service.Logging.logger.info("Request: {}, client_id {}, client_secret {}, redirect_uri {}, code {}, grant_type {}", request,
		 config.Config.GGLoginConfig.GOOGLE_CLIENT_ID,
		 config.Config.GGLoginConfig.GOOGLE_CLIENT_SECRET,
		 config.Config.GGLoginConfig.GOOGLE_REDIRECT_URI,
		 code,
		 config.Config.GGLoginConfig.GOOGLE_GRANT_TYPE);
		
		String response = request.execute().returnContent().asString();
        
		if(response==null)
        {
            service.Logging.logger.error("access token from google is null");
            throw new ClientProtocolException("google access token null");
        }
		JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
		String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
		return accessToken;
	}
	
	
	private static final ResponseHandler<String> responseHandler= response -> {
		int status= response.getStatusLine().getStatusCode();
		if(status>=200 && status<300)
		{
			HttpEntity entity= response.getEntity();
			return entity!=null ? EntityUtils.toString(entity):null;
		}
		else
		{
			service.Logging.logger.error("response handler error");
			throw new ClientProtocolException("Unexpected status code: "+status);
		}
	};

	public static Map<String,JsonElement> getGGUserInfoJson(final String accessToken) throws ClientProtocolException, IOException {
		String link = config.Config.GGLoginConfig.GOOGLE_LINK_GET_USER_INFO + accessToken;
		//String response = Request.Get(link).execute().handleResponse(responseHandler);
		String response = Request.Get(link).execute().returnContent().asString();
		JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
		service.Logging.logger.info("id = "+jobj.asMap().get("id"));
		service.Logging.logger.info("mail = "+jobj.asMap().get("email"));
		return jobj.asMap();
	}

	//FB Login
	//if not work, check https://developers.facebook.com/apps/1292754858725836/settings/basic/ for changing domain name, url
	public static String getFBToken(String code) throws ClientProtocolException,IOException  {
		String response = Request.Post(config.Config.FBLoginConfig.FACEBOOK_LINK_GET_TOKEN).bodyForm
                        (Form.form().add("client_id", config.Config.FBLoginConfig.FACEBOOK_CLIENT_ID)
                                    .add("client_secret", config.Config.FBLoginConfig.FACEBOOK_CLIENT_SECRET)
                                    .add("redirect_uri", config.Config.FBLoginConfig.FACEBOOK_REDIRECT_URI)
                                    .add("code", code)
						            .build()).execute().handleResponse(responseHandler);
        if(response==null)
        {
            service.Logging.logger.error("Database connection established.");
            throw new ClientProtocolException("response null");
        }
		JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
		String accessToken = jobj.get("access_token").toString().replaceAll("\"", "");
		return accessToken;
	}

	public static Map<String,JsonElement> getFBUserInfoJson(final String accessToken) throws ClientProtocolException, IOException {
		String link = config.Config.FBLoginConfig.FACEBOOK_LINK_GET_USER_INFO + accessToken;
		String response = Request.Get(link).execute().handleResponse(responseHandler);
		Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);
        return jsonObject.asMap();
	}

	/* 
	public static String getFBPermission(final String userID, String accessToken) throws ClientProtocolException, IOException {
		String link = "https://graph.facebook.com/" + userID + "/permissions?access_token="+accessToken;
		String response = Request.Get(link).execute().returnContent().asString();
        return response;
	}
	*/
}
