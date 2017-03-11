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

package com.liferay.portal.osgi.web.wab.generator.internal.connection;

import com.liferay.portal.osgi.web.wab.generator.WabGenerator;
import com.liferay.portal.osgi.web.wab.generator.internal.util.URLs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URL;
import java.net.URLConnection;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import java.util.Comparator;
import java.util.Map;

/**
 * @author Raymond Augé
 * @author Miguel Pastor
 * @author Gregory Amerson
 */
public class WabURLConnection extends URLConnection {

	public WabURLConnection(
		ClassLoader classLoader, WabGenerator wabGenerator, URL url) {

		super(url);

		_classLoader = classLoader;
		_wabGenerator = wabGenerator;
	}

	@Override
	public void connect() throws IOException {
	}

	@Override
	public InputStream getInputStream() throws IOException {
		URL url = getURL();

		Map<String, String[]> parameters = URLs.parameterMap(url);

		if (!parameters.containsKey("Web-ContextPath")) {
			throw new IllegalArgumentException(
				"The parameter map does not contain the required parameter " +
					"Web-ContextPath");
		}

		File file = transferToTempFile(new URL(url.getPath()));

		try {
			File processedFile = _wabGenerator.generate(
				_classLoader, file, parameters);

			return new FileInputStream(processedFile);
		}
		finally {
			File rootPath = file.getParentFile();

			Files.walk(
				rootPath.toPath()).sorted(Comparator.reverseOrder()).map(
				Path::toFile).forEach(File::delete);
		}
	}

	protected File transferToTempFile(URL url) throws IOException {
		String path = url.getPath();

		String fileName = path.substring(path.lastIndexOf('/') + 1);

		Path file = Files.createTempFile(fileName, ".tmp");

		try (InputStream inputStream = url.openStream()) {
			Files.copy(inputStream, file, StandardCopyOption.COPY_ATTRIBUTES);
		}

		return file.toFile();
	}

	private final ClassLoader _classLoader;
	private final WabGenerator _wabGenerator;

}