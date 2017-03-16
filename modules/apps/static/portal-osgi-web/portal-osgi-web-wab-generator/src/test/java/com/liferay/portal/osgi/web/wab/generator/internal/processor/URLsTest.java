/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.osgi.web.wab.generator.internal.processor;

import com.liferay.portal.osgi.web.wab.generator.internal.util.URLs;

import java.net.URL;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Raymond Augé
 */
public class URLsTest {

	@Test
	public void testEmoty2() throws Exception {
		URL url = new URL("file:/this?");

		Map<String, String[]> parameterMap = URLs.parameterMap(url);

		Assert.assertTrue(parameterMap.isEmpty());
	}

	@Test
	public void testEmpty() throws Exception {
		URL url = new URL("file:/this");

		Map<String, String[]> parameterMap = URLs.parameterMap(url);

		Assert.assertTrue(parameterMap.isEmpty());
	}

	@Test
	public void testMultiEntry() throws Exception {
		URL url = new URL("file:/this?foo=bar&fee=baz");

		Map<String, String[]> parameterMap = URLs.parameterMap(url);

		Assert.assertEquals(parameterMap.toString(), 2, parameterMap.size());
		Assert.assertTrue(parameterMap.containsKey("foo"));
		Assert.assertEquals(1, parameterMap.get("foo").length);
		Assert.assertEquals("bar", parameterMap.get("foo")[0]);
		Assert.assertTrue(parameterMap.containsKey("fee"));
		Assert.assertEquals(1, parameterMap.get("fee").length);
		Assert.assertEquals("baz", parameterMap.get("fee")[0]);
	}

	@Test
	public void testOneEntry() throws Exception {
		URL url = new URL("file:/this?foo=bar");

		Map<String, String[]> parameterMap = URLs.parameterMap(url);

		Assert.assertEquals(parameterMap.toString(), 1, parameterMap.size());
		Assert.assertTrue(parameterMap.containsKey("foo"));
		Assert.assertEquals(1, parameterMap.get("foo").length);
		Assert.assertEquals("bar", parameterMap.get("foo")[0]);
	}

	@Test
	public void testOneEntryMultiValue() throws Exception {
		URL url = new URL("file:/this?foo=bar&foo=baz");

		Map<String, String[]> parameterMap = URLs.parameterMap(url);

		Assert.assertEquals(parameterMap.toString(), 1, parameterMap.size());
		Assert.assertTrue(parameterMap.containsKey("foo"));
		Assert.assertEquals(2, parameterMap.get("foo").length);
		Assert.assertEquals("bar", parameterMap.get("foo")[0]);
		Assert.assertEquals("baz", parameterMap.get("foo")[1]);
	}

	@Test
	public void testOneEntryNoValue() throws Exception {
		URL url = new URL("file:/this?foo");

		Map<String, String[]> parameterMap = URLs.parameterMap(url);

		Assert.assertEquals(parameterMap.toString(), 1, parameterMap.size());
		Assert.assertTrue(parameterMap.containsKey("foo"));
		Assert.assertEquals(1, parameterMap.get("foo").length);
		Assert.assertEquals(null, parameterMap.get("foo")[0]);
	}

	@Test
	public void testUrlEncodedString() throws Exception {
		URL url = new URL(
			"file:/this?foo=%2Fthis%2Fis%2Fa%2Fpath&fee=%2Fanother%2Dpath");

		Map<String, String[]> parameterMap = URLs.parameterMap(url);

		Assert.assertEquals(parameterMap.toString(), 2, parameterMap.size());
		Assert.assertTrue(parameterMap.containsKey("foo"));
		Assert.assertEquals(1, parameterMap.get("foo").length);
		Assert.assertEquals("/this/is/a/path", parameterMap.get("foo")[0]);
		Assert.assertTrue(parameterMap.containsKey("fee"));
		Assert.assertEquals(1, parameterMap.get("fee").length);
		Assert.assertEquals("/another-path", parameterMap.get("fee")[0]);
	}

}