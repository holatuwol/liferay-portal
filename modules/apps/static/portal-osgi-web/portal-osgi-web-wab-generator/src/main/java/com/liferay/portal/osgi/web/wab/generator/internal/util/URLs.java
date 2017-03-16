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

package com.liferay.portal.osgi.web.wab.generator.internal.util;

import java.io.UnsupportedEncodingException;

import java.net.URL;
import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Raymond Augé
 */
public class URLs {

	public static Map<String, String[]> parameterMap(URL url) {
		assert Objects.nonNull(url);

		String queryString = url.getQuery();

		if (Objects.isNull(queryString) || queryString.trim().length() == 0) {
			return Collections.emptyMap();
		}

		return Arrays.stream(
			queryString.split(_AMP)).map(URLs::_splitQueryParameter).collect(
				Collectors.groupingBy(
					SimpleImmutableEntry::getKey, LinkedHashMap::new,
					Collectors.mapping(
						Map.Entry::getValue,
						Collectors.collectingAndThen(
							Collectors.toList(),
							list -> list.toArray(new String[0])))));
	}

	private static SimpleImmutableEntry<String, String> _splitQueryParameter(
		String paramAndValue) {

		try {
			paramAndValue = URLDecoder.decode(
				paramAndValue, StandardCharsets.UTF_8.name());
		}
		catch (UnsupportedEncodingException uee) {

			// Ignore this case.

		}

		int index = paramAndValue.indexOf(_EQ);

		String key = paramAndValue;

		if (index > 0) {
			key = paramAndValue.substring(0, index);
		}

		String value = null;

		if ((index > 0) && (paramAndValue.length() > (index + 1))) {
			value = paramAndValue.substring(index + 1);
		}

		return new SimpleImmutableEntry<>(key, value);
	}

	private URLs() {

		// Not instantiable

	}

	private static final String _AMP = "&";

	private static final String _EQ = "=";

}