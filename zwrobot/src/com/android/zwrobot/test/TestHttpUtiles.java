package com.android.zwrobot.test;

import com.android.zwrobot.utils.HttpUtils;

import android.test.AndroidTestCase;
import android.util.Log;

public class TestHttpUtiles extends AndroidTestCase {

	public void testSendInfo(){
		String res = HttpUtils.doGet("���ҽ���Ц��");
		Log.e("1111", res);
		res = HttpUtils.doGet("�����");
		Log.e("1111", res);
		res = HttpUtils.doGet("�����");
		Log.e("1111", res);
		res = HttpUtils.doGet("��־������˧");
		Log.e("1111", res);
	}
}
