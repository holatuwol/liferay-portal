package com.liferay.web.proxy.web.portlet;

import com.liferay.portal.kernel.cache.PortalCache;
import com.liferay.portal.kernel.cache.SingleVMPoolUtil;
import org.apache.commons.httpclient.HttpMethodBase;

import org.portletbridge.ResourceException;
import org.portletbridge.portlet.DefaultHttpClientTemplate;
import org.portletbridge.portlet.HttpClientCallback;
import org.portletbridge.portlet.HttpClientState;
import org.portletbridge.portlet.PerPortletMemento;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

public class CachingHttpClientTemplate extends DefaultHttpClientTemplate {

	@Override
	public Object service(
			HttpMethodBase method, HttpClientState client,
			HttpClientCallback callback)
		throws ResourceException {

		try {
			PerPortletMemento memento = (PerPortletMemento)client;
			URI initURL = memento.getInitUrl();
			String uriString = initURL.toString();

			byte[] bytes = (byte[])_portalCache.get(uriString);

			if (bytes != null) {
				method = new CachedGetMethod(uriString, bytes);

				return callback.doInHttpClient(
					HttpServletResponse.SC_OK, method);
			}
			else {
				Object result = super.service(method, client, callback);

				if (method.getStatusCode() == HttpServletResponse.SC_OK) {
					bytes = method.getResponseBody();

					_portalCache.put(uriString, bytes, _TIME_TO_LIVE);
				}

				return result;
			}
		}
		catch (ResourceException re) {
			throw re;
		}
		catch (Throwable t) {
			throw new ResourceException("error.httpclient", t.getMessage(), t);
		}
	}

	private PortalCache _portalCache = SingleVMPoolUtil.getCache(
		CachingHttpClientTemplate.class.getName());

	private static final int _TIME_TO_LIVE = 600;

}
