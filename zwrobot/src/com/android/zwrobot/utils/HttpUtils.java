package com.android.zwrobot.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import org.apache.http.client.utils.URLEncodedUtils;

import com.android.zwrobot.bean.ChatMessage;
import com.android.zwrobot.bean.ChatMessage.Type;
import com.android.zwrobot.bean.Result;
import com.google.gson.Gson;

public class HttpUtils {

	private static final String ROBOT_URL = "http://www.tuling123.com/openapi/api";
	private static final String API_KEY = "79b2af3923b5bf4605b3b739ce738119";
	
	public static ChatMessage sendMessage(String msg) {
		ChatMessage chatMessage = new ChatMessage();
		
		String jsonRes = doGet(msg);
		
		Gson gson = new Gson();
		Result result = null;
		try {
			result = gson.fromJson(jsonRes, Result.class);
			chatMessage.setMsg(result.getText());
		} catch (Exception e) {
			// TODO: handle exception
			chatMessage.setMsg("服务器繁忙，请稍后再试");
		}
		chatMessage.setDate(new Date());
		chatMessage.setType(Type.INCOMING);
		
		return chatMessage;
	}
	
	
	public static String doGet(String msg) {
		String result = "";
		String url = setParam(msg);
		InputStream inputStream = null;
		ByteArrayOutputStream stream = null;
		
		try {
			URL urlNet = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlNet.openConnection();
			connection.setReadTimeout(5*1000);
			connection.setConnectTimeout(5*1000);
			connection.setRequestMethod("GET");
			
			int len = -1;
			inputStream = connection.getInputStream();
			stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[128];
			while ((len=inputStream.read(buffer)) != -1) {
				stream.write(buffer, 0, len);
			}
			
			result = new String(stream.toByteArray());
			stream.flush();//清除缓冲区
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return result;
	}

	private static String setParam(String msg) {
		// TODO Auto-generated method stub
		String url;
		try {
			url = ROBOT_URL + "?key="+API_KEY+"&info="+URLEncoder.encode(msg, "UTF-8");
			return url;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
}
