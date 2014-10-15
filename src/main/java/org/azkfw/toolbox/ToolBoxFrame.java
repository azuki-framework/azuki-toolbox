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

import java.awt.Insets;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;

import org.azkfw.business.task.server.MultiTaskServerAdapter;
import org.azkfw.business.task.server.MultiTaskServerEvent;
import org.azkfw.gui.tree.FileExplorerTree;
import org.azkfw.gui.tree.FileExplorerTreeAdapter;
import org.azkfw.gui.tree.FileExplorerTreeEvent;

/**
 * このクラスは、ツールボックスのメインフレームクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/14
 * @author Kawakicchi
 */
public class ToolBoxFrame extends JFrame {

	/** serialVersionUID */
	private static final long serialVersionUID = -5630583340485053295L;

	/** メニューバー */
	private ToolBoxMenuBar menuBar;
	/** ステータスバー */
	private ToolBoxStatusBar statusBar;

	private JSplitPane splitMain;
	private JSplitPane splitSub;

	private ToolBoxTabbedPane tabMain;

	private FileExplorerTree treeFile;

	/**
	 * コンストラクタ
	 */
	public ToolBoxFrame() {
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		setLayout(null);

		menuBar = new ToolBoxMenuBar();
		setJMenuBar(menuBar);

		statusBar = new ToolBoxStatusBar();
		getContentPane().add(statusBar);

		splitMain = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		splitMain.setDividerLocation(200);
		splitMain.setBorder(null);
		getContentPane().add(splitMain);
		splitSub = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitSub.setDividerLocation(420);
		splitSub.setBorder(null);

		treeFile = new FileExplorerTree();
		treeFile.addFileExplorerTreeListener(new FileExplorerTreeAdapter() {
			@Override
			public void fileExplorerTreeClickedFile(final FileExplorerTreeEvent event, final File aFile) {
				if (aFile.isFile()) {
					tabMain.addTab("Test1", new JPanel());
				}
			}
		});

		JScrollPane s = new JScrollPane(treeFile);
		s.setBorder(null);
		splitMain.setLeftComponent(s);
		splitMain.setRightComponent(splitSub);

		tabMain = new ToolBoxTabbedPane();
		tabMain.addTab("Test1", new JPanel());
		tabMain.addTab("Test2", UIManager.getIcon("FileView.fileIcon"), new JPanel());
		tabMain.addTab("Test3", new JPanel());
		tabMain.addTab("Title4", UIManager.getIcon("FileView.fileIcon"), new JPanel());
		tabMain.addTab("Title4", UIManager.getIcon("FileView.fileIcon"), new JPanel(), "hint");

		splitSub.setTopComponent(tabMain);
		splitSub.setBottomComponent(new JPanel());

		ToolBox.getInstance().getServer().addMultiTaskServerListener(new MultiTaskServerAdapter() {
			@Override
			public void multiTaskServerStopped(final MultiTaskServerEvent event) {
				doExit();
			}
		});
		
		// listener
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(final WindowEvent event) {
				ToolBox.getInstance().getServer().stop();
			}
		});
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(final ComponentEvent event) {
				doResize();
			}
		});

		setSize(800, 600);
	}

	private void doResize() {
		Insets insets = getInsets();
		int width = getWidth() - (insets.left + insets.right);
		int height = getHeight() - (insets.top + menuBar.getHeight() + insets.bottom);

		splitMain.setBounds(0, 0, width, height - statusBar.getHeight());
		statusBar.setBounds(0, height - statusBar.getHeight(), width, statusBar.getHeight());
	}

	private void doExit() {
		setVisible(false);
		dispose();
	}
}
