/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.azkfw.toolbox.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import org.azkfw.util.StringUtility;

/**
 * このクラスは、ツールボックスのメニューバークラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/14
 * @author Kawakicchi
 */
public class ToolBoxMenuBar extends JMenuBar {

	/** serialVersionUID */
	private static final long serialVersionUID = -4802142665042291722L;

	private List<ToolBoxMenuBarListener> listeners;

	private ToolBoxMenuData root;

	public ToolBoxMenuBar() {
		listeners = new ArrayList<ToolBoxMenuBarListener>();
		root = new ToolBoxMenuData("ROOT");
	}

	public void addToolBoxMenuBarListener(final ToolBoxMenuBarListener listener) {
		listeners.add(listener);
	}

	/**
	 * メニューを追加
	 * 
	 * @param aPath
	 * @param aId
	 * @param aText
	 */
	public void add(final String aPath, final String aText) {
		if (StringUtility.isNotEmpty(aPath)) {
			String[] paths = aPath.split("/");

			ToolBoxMenuData data = root;
			for (int i = 0; i < paths.length; i++) {
				String id = paths[i];
				ToolBoxMenuData child = data.get(id);
				if (i + 1 == paths.length) {
					if (null == child) {
						child = new ToolBoxMenuData(id, aText);
						data.add(child);
					} else {
						child.setText(aText);
					}
				} else {
					if (null == child) {
						child = new ToolBoxMenuData(id);
						data.add(child);
					}
					data = child;
				}
			}
		}
	}

	public synchronized void generate() {
		for (ToolBoxMenuData data : root.getChildren()) {
			String id = data.getId();
			String text = data.getText();
			List<ToolBoxMenuData> childList = data.getChildren();

			if (0 == childList.size()) {
				ToolBoxMenuItem menu = new ToolBoxMenuItem(id, StringUtility.isNotEmpty(text) ? text : id);
				menu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent event) {
						ToolBoxMenuItem menu = (ToolBoxMenuItem) event.getSource();

						for (ToolBoxMenuBarListener listener : listeners) {
							listener.toolBoxMenuBarActionMenuItem(menu.getPath());
						}
					}
				});
				add(menu);
			} else {
				JMenu menu = new JMenu(StringUtility.isNotEmpty(text) ? text : id);
				generate(menu, id + "/", data.getChildren());
				add(menu);
			}
		}
	}

	private void generate(final JMenu aParentMenu, final String aParentPath, List<ToolBoxMenuData> aChildDatas) {
		for (ToolBoxMenuData data : aChildDatas) {
			String id = data.getId();
			String text = data.getText();
			List<ToolBoxMenuData> childList = data.getChildren();

			if (0 == childList.size()) {
				ToolBoxMenuItem menu = new ToolBoxMenuItem(aParentPath + id, StringUtility.isNotEmpty(text) ? text : id);
				menu.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent event) {
						ToolBoxMenuItem menu = (ToolBoxMenuItem) event.getSource();

						for (ToolBoxMenuBarListener listener : listeners) {
							listener.toolBoxMenuBarActionMenuItem(menu.getPath());
						}
					}
				});
				aParentMenu.add(menu);
			} else {
				JMenu menu = new JMenu(StringUtility.isNotEmpty(text) ? text : id);
				generate(menu, aParentPath + id + "/", data.getChildren());
				aParentMenu.add(menu);
			}
		}
	}

	private class ToolBoxMenuItem extends JMenuItem {
		/** serialVersionUID */
		private static final long serialVersionUID = 4548313707478573715L;

		private String path;

		public ToolBoxMenuItem(final String aPath, final String aText) {
			super(aText);
			path = aPath;
		}

		public String getPath() {
			return path;
		}
	}

	private class ToolBoxMenuData {
		private String id;
		private String text;
		private List<ToolBoxMenuData> children;

		public ToolBoxMenuData(final String aId) {
			id = aId;
			text = null;
			children = new ArrayList<ToolBoxMenuData>();
		}

		public ToolBoxMenuData(final String aId, final String aText) {
			id = aId;
			text = aText;
			children = new ArrayList<ToolBoxMenuData>();
		}

		public String getId() {
			return id;
		}

		public void setText(final String aText) {
			text = aText;
		}

		public String getText() {
			return text;
		}

		public void add(ToolBoxMenuData aData) {
			children.add(aData);
		}

		public ToolBoxMenuData get(final String aId) {
			ToolBoxMenuData data = null;
			for (ToolBoxMenuData child : children) {
				if (child.getId().equals(aId)) {
					data = child;
					break;
				}
			}
			return data;
		}

		public List<ToolBoxMenuData> getChildren() {
			return children;
		}
	}

}
