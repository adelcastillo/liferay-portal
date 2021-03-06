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

package com.liferay.dynamic.data.mapping.util;

import com.liferay.dynamic.data.mapping.model.DDMForm;
import com.liferay.dynamic.data.mapping.model.LocalizedValue;
import com.liferay.dynamic.data.mapping.model.Value;
import com.liferay.dynamic.data.mapping.storage.DDMFormFieldValue;
import com.liferay.dynamic.data.mapping.storage.DDMFormValues;
import com.liferay.dynamic.data.mapping.test.util.DDMFormTestUtil;
import com.liferay.dynamic.data.mapping.test.util.DDMFormValuesTestUtil;
import com.liferay.dynamic.data.mapping.util.impl.DDMFormValuesMergerImpl;
import com.liferay.portal.kernel.test.util.RandomTestUtil;
import com.liferay.portal.kernel.util.LocaleUtil;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Inácio Nery
 */
public class DDMFormValuesMergerTest {

	@Test
	public void testAddMissingDDMFormFieldValue() {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createTextDDMFormField(
				"text1", false, false, true));
		ddmForm.addDDMFormField(
			DDMFormTestUtil.createTextDDMFormField(
				"text2", false, false, true));

		// Existing dynamic data mapping form values

		String text1StringValue = RandomTestUtil.randomString();

		LocalizedValue text1LocalizedValue =
			DDMFormValuesTestUtil.createLocalizedValue(
				text1StringValue, LocaleUtil.US);

		DDMFormFieldValue text1DDMFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"text1", text1LocalizedValue);

		DDMFormValues existingDDMFormValues = createDDMFormValues(
			ddmForm, text1DDMFormFieldValue);

		// New dynamic data mapping form values

		String text2StringValue = RandomTestUtil.randomString();

		LocalizedValue text2LocalizedValue =
			DDMFormValuesTestUtil.createLocalizedValue(
				text2StringValue, LocaleUtil.US);

		DDMFormFieldValue text2DDMFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"text2", text2LocalizedValue);

		DDMFormValues newDDMFormValues = createDDMFormValues(
			ddmForm, text2DDMFormFieldValue);

		DDMFormValues mergedDDMFormValues = _ddmFormValuesMerger.merge(
			newDDMFormValues, existingDDMFormValues);

		List<DDMFormFieldValue> mergedDDMFormFieldValues =
			mergedDDMFormValues.getDDMFormFieldValues();

		Assert.assertEquals(2, mergedDDMFormFieldValues.size());

		DDMFormFieldValue mergedText1DDMFormFieldValue =
			mergedDDMFormFieldValues.get(0);

		Value mergedText1Value = mergedText1DDMFormFieldValue.getValue();

		Assert.assertEquals(
			text1StringValue, mergedText1Value.getString(LocaleUtil.US));

		DDMFormFieldValue mergedText2DDMFormFieldValue =
			mergedDDMFormFieldValues.get(1);

		Value mergedText2Value = mergedText2DDMFormFieldValue.getValue();

		Assert.assertEquals(
			text2StringValue, mergedText2Value.getString(LocaleUtil.US));
	}

	@Test
	public void testAddMissingLocaleToExistingDDMFormFieldValue() {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createTextDDMFormField("text", false, false, true));

		// Existing dynamic data mapping form values

		String enStringValue = RandomTestUtil.randomString();

		LocalizedValue existingLocalizedValue =
			DDMFormValuesTestUtil.createLocalizedValue(
				enStringValue, LocaleUtil.US);

		DDMFormFieldValue textDDMFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"text", existingLocalizedValue);

		DDMFormValues existingDDMFormValues = createDDMFormValues(
			ddmForm, textDDMFormFieldValue);

		// New dynamic data mapping form values

		String ptStringValue = RandomTestUtil.randomString();

		LocalizedValue newLocalizedValue =
			DDMFormValuesTestUtil.createLocalizedValue(
				enStringValue, ptStringValue, LocaleUtil.US);

		textDDMFormFieldValue = DDMFormValuesTestUtil.createDDMFormFieldValue(
			"text", newLocalizedValue);

		DDMFormValues newDDMFormValues = createDDMFormValues(
			ddmForm, textDDMFormFieldValue);

		DDMFormValues mergedFormValues = _ddmFormValuesMerger.merge(
			newDDMFormValues, existingDDMFormValues);

		List<DDMFormFieldValue> mergedFormFieldValues =
			mergedFormValues.getDDMFormFieldValues();

		Assert.assertEquals(1, mergedFormFieldValues.size());

		DDMFormFieldValue mergedDDMFormFieldValue = mergedFormFieldValues.get(
			0);

		Value mergedValue = mergedDDMFormFieldValue.getValue();

		Assert.assertEquals(
			enStringValue, mergedValue.getString(LocaleUtil.US));
		Assert.assertEquals(
			ptStringValue, mergedValue.getString(LocaleUtil.BRAZIL));
	}

	@Test
	public void testReplaceAndAddMissingLocaleToExistingDDMFormFieldValue() {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createTextDDMFormField("text", false, false, true));

		// Existing dynamic data mapping form values

		String existingEnStringValue = RandomTestUtil.randomString();

		LocalizedValue existingLocalizedValue =
			DDMFormValuesTestUtil.createLocalizedValue(
				existingEnStringValue, LocaleUtil.US);

		DDMFormFieldValue textDDMFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"text", existingLocalizedValue);

		DDMFormValues existingDDMFormValues = createDDMFormValues(
			ddmForm, textDDMFormFieldValue);

		// New dynamic data mapping form values

		String newEnStringValue = RandomTestUtil.randomString();
		String newPtStringValue = RandomTestUtil.randomString();

		LocalizedValue newLocalizedValue =
			DDMFormValuesTestUtil.createLocalizedValue(
				newEnStringValue, newPtStringValue, LocaleUtil.US);

		textDDMFormFieldValue = DDMFormValuesTestUtil.createDDMFormFieldValue(
			"text", newLocalizedValue);

		DDMFormValues newDDMFormValues = createDDMFormValues(
			ddmForm, textDDMFormFieldValue);

		DDMFormValues mergedFormValues = _ddmFormValuesMerger.merge(
			newDDMFormValues, existingDDMFormValues);

		List<DDMFormFieldValue> mergedFormFieldValues =
			mergedFormValues.getDDMFormFieldValues();

		Assert.assertEquals(1, mergedFormFieldValues.size());

		DDMFormFieldValue mergedDDMFormFieldValue = mergedFormFieldValues.get(
			0);

		Value mergedValue = mergedDDMFormFieldValue.getValue();

		Assert.assertEquals(
			newEnStringValue, mergedValue.getString(LocaleUtil.US));
		Assert.assertEquals(
			newPtStringValue, mergedValue.getString(LocaleUtil.BRAZIL));
	}

	@Test
	public void testReplaceLocaleToExistingDDMFormFieldValue() {
		DDMForm ddmForm = DDMFormTestUtil.createDDMForm();

		ddmForm.addDDMFormField(
			DDMFormTestUtil.createTextDDMFormField("text", false, false, true));

		// Existing dynamic data mapping form values

		String existingEnStringValue = RandomTestUtil.randomString();

		LocalizedValue existingLocalizedValue =
			DDMFormValuesTestUtil.createLocalizedValue(
				existingEnStringValue, LocaleUtil.US);

		DDMFormFieldValue textDDMFormFieldValue =
			DDMFormValuesTestUtil.createDDMFormFieldValue(
				"text", existingLocalizedValue);

		DDMFormValues existingDDMFormValues = createDDMFormValues(
			ddmForm, textDDMFormFieldValue);

		// New dynamic data mapping form values

		String newEnStringValue = RandomTestUtil.randomString();

		LocalizedValue newLocalizedValue =
			DDMFormValuesTestUtil.createLocalizedValue(
				newEnStringValue, LocaleUtil.US);

		textDDMFormFieldValue = DDMFormValuesTestUtil.createDDMFormFieldValue(
			"text", newLocalizedValue);

		DDMFormValues newDDMFormValues = createDDMFormValues(
			ddmForm, textDDMFormFieldValue);

		DDMFormValues mergedFormValues = _ddmFormValuesMerger.merge(
			newDDMFormValues, existingDDMFormValues);

		List<DDMFormFieldValue> mergedFormFieldValues =
			mergedFormValues.getDDMFormFieldValues();

		Assert.assertEquals(1, mergedFormFieldValues.size());

		DDMFormFieldValue mergedDDMFormFieldValue = mergedFormFieldValues.get(
			0);

		Value mergedValue = mergedDDMFormFieldValue.getValue();

		Assert.assertEquals(
			newEnStringValue, mergedValue.getString(LocaleUtil.US));
	}

	protected DDMFormValues createDDMFormValues(
		DDMForm ddmForm, DDMFormFieldValue... ddmFormFieldValues) {

		DDMFormValues ddmFormValues = DDMFormValuesTestUtil.createDDMFormValues(
			ddmForm);

		for (DDMFormFieldValue ddmFormFieldValue : ddmFormFieldValues) {
			ddmFormValues.addDDMFormFieldValue(ddmFormFieldValue);
		}

		return ddmFormValues;
	}

	private final DDMFormValuesMerger _ddmFormValuesMerger =
		new DDMFormValuesMergerImpl();

}