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
package org.azkfw.toolbox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.azkfw.util.UUIDUtility;

/**
 * @since 1.0.0
 * @version 1.0.0 2014/10/14
 * @author Kawakicchi
 */
public class ToolBoxTabbedPane extends JTabbedPane {

	/** serialVersionUID */
	private static final long serialVersionUID = -4322039788436100986L;

	private List<String> ids;

	public ToolBoxTabbedPane() {
		ids = new ArrayList<String>();

		setBorder(null);
	}

	public void addTab(final String aTitle, final Component aPanel) {
		addTab(aTitle, null, aPanel);
	}

	public void addTab(final String aTitle, final Icon aIcon, final Component aPanel) {
		addTab(aTitle, aIcon, aPanel, null);
	}

	public void addTab(final String aTitle, final Icon aIcon, final Component aPanel, final String aToolTip) {
		String id = UUIDUtility.generateToShortString();

		JPanel pnlTab = new JPanel();
		pnlTab.setOpaque(false);
		pnlTab.setLayout(new BorderLayout(5, 5));

		JLabel label = null;
		if (null != aIcon) {
			label = new JLabel(aTitle, aIcon, JLabel.LEFT);
		} else {
			label = new JLabel(aTitle);
		}
		pnlTab.add(label, BorderLayout.CENTER);

		ClassLoader cl = this.getClass().getClassLoader();
		TabCloseLabel closeLabel = new TabCloseLabel(id, new ImageIcon(cl.getResource("org/azkfw/toolbox/toolbox_close.png")));
		closeLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent event) {
				TabCloseLabel close = (TabCloseLabel) event.getSource();
				for (int i = 0; i < ids.size(); i++) {
					String id = ids.get(i);
					if (id.equals(close.getId())) {
						remove(i);
						ids.remove(i);
						break;
					}
				}
			}
		});
		pnlTab.add(closeLabel, BorderLayout.EAST);
		ids.add(id);

		super.addTab(null, aPanel);
		setTabComponentAt(getTabCount() - 1, pnlTab);
		if (null != aToolTip) {
			setToolTipTextAt(getTabCount() - 1, aToolTip);
		}

		setSelectedIndex(getTabCount() - 1);
	}

	private class TabCloseLabel extends JLabel {

		/** serialVersionUID */
		private static final long serialVersionUID = 3193755639540747912L;

		private String id;

		public TabCloseLabel(final String aId, final Icon aIcon) {
			super(aIcon);
			id = aId;
		}

		public String getId() {
			return id;
		}
	}
}
