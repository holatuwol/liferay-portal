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

package com.liferay.pdfbox.util;

import com.liferay.portal.image.ImageToolImpl;
import com.liferay.portal.kernel.image.ImageTool;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import java.io.File;

import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * @author Juan Gonzalez
 */
public class UpgradedLiferayPDFBoxConverter {

	public UpgradedLiferayPDFBoxConverter(
		File inputFile, File thumbnailFile, File[] previewFiles,
		String extension, String thumbnailExtension, int dpi, int height,
		int width, boolean generatePreview, boolean generateThumbnail) {

		_inputFile = inputFile;
		_thumbnailFile = thumbnailFile;
		_previewFiles = previewFiles;
		_extension = extension;
		_thumbnailExtension = thumbnailExtension;
		_dpi = dpi;
		_height = height;
		_width = width;
		_generatePreview = generatePreview;
		_generateThumbnail = generateThumbnail;
	}

	public void generateImagesPB() throws Exception {
		try (PDDocument pdDocument = PDDocument.load(_inputFile)) {
			if (_generateThumbnail) {
				_generateImagesPB(
					pdDocument, 0, _thumbnailFile, _thumbnailExtension);
			}

			if (_generatePreview) {
				for (int i = 0; i < pdDocument.getNumberOfPages(); i++) {
					_generateImagesPB(
						pdDocument, i, _previewFiles[i], _extension);
				}
			}
		}
	}

	private void _generateImagesPB(
			PDDocument pdDocument, int page, File outputFile, String extension)
		throws Exception {

		PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);

		RenderedImage renderedImage = pdfRenderer.renderImageWithDPI(page, _dpi,
			ImageType.RGB);

		ImageTool imageTool = ImageToolImpl.getInstance();

		if (_height != 0) {
			renderedImage = imageTool.scale(renderedImage, _width, _height);
		}
		else {
			renderedImage = imageTool.scale(renderedImage, _width);
		}

		outputFile.createNewFile();

		ImageIO.write(renderedImage, extension, outputFile);
	}

	private final int _dpi;
	private final String _extension;
	private final boolean _generatePreview;
	private final boolean _generateThumbnail;
	private final int _height;
	private final File _inputFile;
	private final File[] _previewFiles;
	private final String _thumbnailExtension;
	private final File _thumbnailFile;
	private final int _width;

}