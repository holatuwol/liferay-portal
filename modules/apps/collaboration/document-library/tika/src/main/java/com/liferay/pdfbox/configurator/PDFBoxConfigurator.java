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

package com.liferay.pdfbox.configurator;

import com.liferay.document.library.kernel.util.DLProcessor;
import com.liferay.document.library.kernel.util.DLProcessorRegistryUtil;
import com.liferay.document.library.kernel.util.PDFProcessor;
import com.liferay.document.library.kernel.util.PDFProcessorUtil;
import com.liferay.pdfbox.util.UpgradedPDFProcessorImpl;
import com.liferay.portal.kernel.util.InstanceFactory;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * @author Jonathan McCann
 */
@Component
public class PDFBoxConfigurator {

	@Activate
	public void activate() throws Exception {
		_originalPdfProcessor = PDFProcessorUtil.getPDFProcessor();

		DLProcessor dlProcessor = (DLProcessor) InstanceFactory.newInstance(
			UpgradedPDFProcessorImpl.class.getName());

		dlProcessor.afterPropertiesSet();

		DLProcessorRegistryUtil.register(dlProcessor);
	}

	@Deactivate
	public void deactivate() throws Exception {
		DLProcessorRegistryUtil.register((DLProcessor)_originalPdfProcessor);
	}

	private PDFProcessor _originalPdfProcessor;

}