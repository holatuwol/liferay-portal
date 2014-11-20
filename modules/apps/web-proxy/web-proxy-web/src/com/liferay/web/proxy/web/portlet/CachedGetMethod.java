package com.liferay.web.proxy.web.portlet;

import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import org.apache.commons.httpclient.methods.GetMethod;

public class CachedGetMethod extends GetMethod {

	public CachedGetMethod(String uriString, byte[] bytes) {
		super(uriString);

		setResponseStream(new UnsyncByteArrayInputStream(bytes));
	}

}
