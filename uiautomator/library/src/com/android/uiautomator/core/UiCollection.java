/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.uiautomator.core;

/**
 * Used to enumerate a container's UI elements for the purpose of verification
 * and/or targeting a sub container by a child's text or description. For example
 * if a list view contained many list items each in its own LinearLayout, and
 * the test desired to locate an On/Off switch next to text Wi-Fi so not to be
 * confused with a switch near text Bluetooth, the test use a UiCollection pointing
 * at the list view of the items then use {@link #getChildByText(By, String)} for
 * locating the LinearLayout element containing the text Wi-Fi. The returned UiObject
 * can further be used to retrieve a child by selector targeting the desired switch and
 * not other switches that may also be in the list.
 */
public class UiCollection extends UiObject {

    public UiCollection(By selector) {
        super(selector);
    }

    /**
     * Searches for child UI element within the constraints of this UiCollection {@link By}
     * selector. It looks for any child matching the <code>childPattern</code> argument that has
     * a child UI element anywhere within its sub hierarchy that has content-description text.
     * The returned UiObject will point at the <code>childPattern</code> instance that matched the
     * search and not at the identifying child element that matched the content description.</p>
     * @param childPattern {@link By} selector of the child pattern to match and return
     * @param text String of the identifying child contents of of the <code>childPattern</code>
     * @return {@link UiObject} pointing at and instance of <code>childPattern</code>
     * @throws UiObjectNotFoundException
     */
    public UiObject getChildByDescription(By childPattern, String text)
            throws UiObjectNotFoundException {
        if (text != null) {
            int count = getChildCount(childPattern);
            for (int x = 0; x < count; x++) {
                UiObject row = getChildByInstance(childPattern, x);
                String nodeDesc = row.getContentDescription();
                if(nodeDesc != null && nodeDesc.contains(text)) {
                    return row;
                }
                UiObject item = row.getChild(By.selector().descriptionContains(text));
                if (item.exists()) {
                    return row;
                }
            }
        }
        throw new UiObjectNotFoundException("for description= \"" + text + "\"");
    }

    /**
     * Searches for child UI element within the constraints of this UiCollection {@link By}
     * selector. It looks for any child matching the <code>childPattern</code> argument that has
     * a child UI element anywhere within its sub hierarchy that is at the <code>instance</code>
     * specified. The operation is performed only on the visible items and no scrolling is performed
     * in this case.
     * @param childPattern {@link By} selector of the child pattern to match and return
     * @param instance int the desired matched instance of this <code>childPattern</code>
     * @return {@link UiObject} pointing at and instance of <code>childPattern</code>
     */
    public UiObject getChildByInstance(By childPattern, int instance)
            throws UiObjectNotFoundException {
        By patternSelector = By.patternBuilder(getSelector(),
                By.patternBuilder(childPattern).instance(instance));
        return new UiObject(patternSelector);
    }

    /**
     * Searches for child UI element within the constraints of this UiCollection {@link By}
     * selector. It looks for any child matching the <code>childPattern</code> argument that has
     * a child UI element anywhere within its sub hierarchy that has text attribute =
     * <code>text</code>. The returned UiObject will point at the <code>childPattern</code>
     * instance that matched the search and not at the identifying child element that matched the
     * text attribute.</p>
     * @param childPattern {@link By} selector of the child pattern to match and return
     * @param text String of the identifying child contents of of the <code>childPattern</code>
     * @return {@link UiObject} pointing at and instance of <code>childPattern</code>
     * @throws UiObjectNotFoundException
     */
    public UiObject getChildByText(By childPattern, String text)
            throws UiObjectNotFoundException {

        if (text != null) {
            int count = getChildCount(childPattern);
            for (int x = 0; x < count; x++) {
                UiObject row = getChildByInstance(childPattern, x);
                String nodeText = row.getText();
                if(text.equals(nodeText)) {
                    return row;
                }
                UiObject item = row.getChild(By.selector().text(text));
                if (item.exists()) {
                    return row;
                }
            }
        }
        throw new UiObjectNotFoundException("for text= \"" + text + "\"");
    }

    /**
     * Count child UI element instances matching the <code>childPattern</code>
     * argument. The number of elements match returned represent those elements that are
     * currently visible on the display within the sub hierarchy of this UiCollection {@link By}
     * selector. Take note that more elements may be present but invisible and are not counted.
     * @param childPattern is a {@link By} selector that is a pattern to count
     * @return the number of matched childPattern under the current {@link UiCollection}
     */
    public int getChildCount(By childPattern) {
        By patternSelector = By.patternBuilder(getSelector(), By.patternBuilder(childPattern));
        return getQueryController().getPatternCount(patternSelector);
    }
}