package com.android.zwrobot.test;

import com.android.zwrobot.utils.HttpUtils;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestHttpUtiles extends AndroidTestCase {

	public void testSendInfo(){
		String res = HttpUtils.doGet("给我讲个笑话");
		Log.e("1111", res);
		res = HttpUtils.doGet("真的吗");
		Log.e("1111", res);
		res = HttpUtils.doGet("不会吧");
		Log.e("1111", res);
		res = HttpUtils.doGet("黄志鸿大神好帅");
		Log.e("1111", res);
	}
}
