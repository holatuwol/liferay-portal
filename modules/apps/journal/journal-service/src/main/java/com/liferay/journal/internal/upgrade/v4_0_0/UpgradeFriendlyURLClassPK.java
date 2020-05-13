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

package com.liferay.journal.internal.upgrade.v4_0_0;

import com.liferay.friendly.url.service.FriendlyURLEntryLocalService;
import com.liferay.journal.model.JournalArticle;
import com.liferay.portal.dao.orm.common.SQLTransformer;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;
import com.liferay.portal.kernel.util.PortalUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author To Trinh
 */
public class UpgradeFriendlyURLClassPK extends UpgradeProcess {

	public UpgradeFriendlyURLClassPK(
		FriendlyURLEntryLocalService friendlyURLEntryLocalService) {

		_friendlyURLEntryLocalService = friendlyURLEntryLocalService;
	}

	@Override
	protected void doUpgrade() throws Exception {
		long journalArticleClassNameId = PortalUtil.getClassNameId(
			JournalArticle.class);

		StringBuilder sb = new StringBuilder(13);

		sb.append("select JournalArticle.id_, ");
		sb.append("FriendlyURLEntry.friendlyURLEntryId ");
		sb.append("from JournalArticle inner join FriendlyURLEntry on ( ");
		sb.append("FriendlyURLEntry.classNameId = ? and ");
		sb.append(
			"JournalArticle.resourcePrimKey = FriendlyURLEntry.classPK) ");
		sb.append("inner join ( ");
		sb.append("SELECT resourcePrimKey, MAX(version) as maxVersion ");
		sb.append("FROM JournalArticle GROUP BY resourcePrimKey)");
		sb.append("LastVersionJournalArticle on ( ");
		sb.append("JournalArticle.resourcePrimKey = ");
		sb.append("LastVersionJournalArticle.resourcePrimKey and ");
		sb.append("JournalArticle.version = ");
		sb.append("LastVersionJournalArticle.maxVersion)");

		try (PreparedStatement ps1 = connection.prepareStatement(
				SQLTransformer.transform(sb.toString()))) {

			ps1.setLong(1, journalArticleClassNameId);

			ResultSet rs1 = ps1.executeQuery();

			while (rs1.next()) {
				long id = rs1.getLong("id_");
				long friendlyURLEntryId = rs1.getLong("friendlyURLEntryId");

				_friendlyURLEntryLocalService.updateClassPK(
					friendlyURLEntryId, id);
			}
		}
	}

	private final FriendlyURLEntryLocalService _friendlyURLEntryLocalService;

}