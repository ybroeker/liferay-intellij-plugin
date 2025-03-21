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

package com.liferay.ide.idea.bnd.psi.impl;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.projectRoots.ProjectJdkTable;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.LanguageLevelModuleExtension;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess;
import com.intellij.pom.java.LanguageLevel;
import com.intellij.testFramework.LightProjectDescriptor;
import com.intellij.testFramework.fixtures.DefaultLightProjectDescriptor;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import com.intellij.util.PathUtil;

import com.liferay.ide.idea.util.SdkUtil;

import java.io.File;

import org.jetbrains.annotations.NotNull;

/**
 * @author Dominik Marks
 */
public class BndHeaderValuePartManipulatorTest extends LightJavaCodeInsightFixtureTestCase {

	public void testRenameClassInsideBnd() {
		myFixture.configureByFiles("testRenameClassInsideBnd/bnd.bnd", "com/liferay/test/Foo.java");

		myFixture.renameElementAtCaret("Bar");

		myFixture.checkResultByFile("com/liferay/test/Bar.java", "com/liferay/test/Bar_Renamed.java", false);
	}

	public void testRenameClassInsideClass() {
		myFixture.configureByFiles("com/liferay/test/Baz.java", "testRenameClassInsideClass/bnd.bnd");

		myFixture.renameElementAtCaret("Foo");

		myFixture.checkResultByFile(
			"testRenameClassInsideClass/bnd.bnd", "testRenameClassInsideClass/bnd_renamed.bnd", false);
	}

	public void testRenamePackageInsideBnd() {
		myFixture.configureByFiles("testRenamePackageInsideBnd/bnd.bnd", "com/liferay/test/Foo.java");

		myFixture.renameElementAtCaret("renamed");

		myFixture.checkResultByFile("com/liferay/renamed/Foo.java", "com/liferay/test/Foo_Renamed.java", false);
	}

	public void testRenamePackageInsideClass() {
		myFixture.configureByFiles("com/liferay/test/Bar.java", "testRenamePackageInsideClass/bnd.bnd");

		myFixture.renameElementAtCaret("renamed");

		myFixture.checkResultByFile(
			"testRenamePackageInsideClass/bnd.bnd", "testRenamePackageInsideClass/bnd_renamed.bnd", false);
	}

	@NotNull
	@Override
	protected LightProjectDescriptor getProjectDescriptor() {
		return _JAVA_DESCRIPTOR;
	}

	@Override
	protected String getTestDataPath() {
		return _TEST_DATA_PATH;
	}

	private static final LightProjectDescriptor _JAVA_DESCRIPTOR = new DefaultLightProjectDescriptor() {

		@Override
		public void configureModule(
			@NotNull Module module, @NotNull ModifiableRootModel modifiableRootModel,
			@NotNull ContentEntry contentEntry) {

			LanguageLevelModuleExtension extension = modifiableRootModel.getModuleExtension(
				LanguageLevelModuleExtension.class);

			if (extension != null) {
				extension.setLanguageLevel(LanguageLevel.JDK_1_8);
			}

			ProjectJdkTable.getInstance(
			).addJdk(
				SdkUtil.getTestJdk()
			);

			modifiableRootModel.setSdk(SdkUtil.getTestJdk());

			File testDataDir = new File(_TEST_DATA_PATH);

			final String testDataPath = PathUtil.toSystemIndependentName(testDataDir.getAbsolutePath());

			VfsRootAccess.allowRootAccess(Disposer.newDisposable(), testDataPath);
		}

	};

	private static final String _TEST_DATA_PATH =
		"testdata/com/liferay/ide/idea/bnd/psi/impl/BndHeaderValuePartManipulatorTest";

}