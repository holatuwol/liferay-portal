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

package com.liferay.portal.scripting.osgi.internal.osgi.commands;

import com.liferay.portal.kernel.scripting.Scripting;
import com.liferay.portal.kernel.util.AggregateClassLoader;
import com.liferay.portal.kernel.util.FileUtil;

import org.apache.felix.service.command.Descriptor;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Mariano Álvaro Sáiz
 */
@Component(
	immediate = true,
	property = {"osgi.command.function=execute", "osgi.command.scope=system"},
	service = ScriptingOSGiCommands.class
)
public class ScriptingOSGiCommands {

	@Descriptor("Executes a script file in the provided language")
	public String execute(String language, String scriptFile) throws Exception {
		if (!FileUtil.exists(scriptFile)) {
			return "Unable to find file " + scriptFile;
		}

		String script = FileUtil.read(scriptFile);

		_setContextClassLoader();

		_scripting.exec(null, null, language, script);

		return null;
	}

	private void _setContextClassLoader() {
		Class<?> clazz = getClass();

		ClassLoader classLoader = clazz.getClassLoader();

		Thread currentThread = Thread.currentThread();

		ClassLoader aggregateClassLoader =
			AggregateClassLoader.getAggregateClassLoader(
				classLoader, currentThread.getContextClassLoader());

		currentThread.setContextClassLoader(aggregateClassLoader);
	}

	@Reference
	private Scripting _scripting;

}