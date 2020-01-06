package com.fairy.common;


import org.springframework.stereotype.Component;

import com.fairy.utils.encrypt.Des;
import com.fairy.utils.encrypt.DesUtil;
import com.fairy.utils.encrypt.Encrypter;

@Component
public class MngSvrEncrypter implements Encrypter {
	public static String DEFAULT_KEY = "B4011F49527A00964CE3A9SDWEX61D82A9B40";

	protected String key = DEFAULT_KEY;

	public MngSvrEncrypter() {
	}

	public MngSvrEncrypter(String key) {
		this.key = key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getKey() {
		return this.key;
	}

	public String encrypt(String string) {
		int hisuLen = string.getBytes().length;
		StringBuilder encrypted = new StringBuilder();

		byte[] hisuBytes = new byte[hisuLen];
		try {
			byte[] hisuPasswdPlainBt = string.getBytes("utf-8");
			System.arraycopy(hisuPasswdPlainBt, 0, hisuBytes, 0, hisuLen);
		} catch (Exception e) {
			return "";
		}
		Des hisuDes = new Des(getKey());
		encrypted.append(hisuDes.enc(DesUtil.hisuLeftAddZero(new StringBuilder().append(hisuLen).toString(), 2)
				+ DesUtil.hisuBytes2HexString(hisuBytes)));
		return encrypted.toString();
	}
}
