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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import org.azkfw.business.task.server.MultiTaskServer;
import org.azkfw.toolbox.plugin.ImageViewerPlugin;
import org.azkfw.toolbox.plugin.ToolBoxPlugin;
import org.azkfw.toolbox.support.ToolBoxFileOpen;
import org.azkfw.toolbox.support.ToolBoxFileOpenSupport;
import org.azkfw.toolbox.support.ToolBoxFilePopupMenuSupport;

/**
 * このクラスは、ツールボックスのメインクラスです。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/14
 * @author Kawakicchi
 */
public class ToolBox {

	/**
	 * メイン関数
	 * 
	 * @param args 引数
	 */
	public static void main(final String[] args) {
		ToolBox.getInstance().setup();
	}

	private static final ToolBox INSTANCE = new ToolBox();

	private String title;
	private MultiTaskServer server;
	private ToolBoxFrame frame;
	private List<Class<? extends ToolBoxPlugin>> pluginClasses;
	private List<ToolBoxPlugin> plugins;

	private ToolBox() {
		server = new MultiTaskServer();
		server.start();

		pluginClasses = new ArrayList<Class<? extends ToolBoxPlugin>>();
		plugins = new ArrayList<ToolBoxPlugin>();

		registerPlugin(ImageViewerPlugin.class);
	}

	public ToolBox setTitle(final String aTitle) {
		title = aTitle;
		return this;
	}

	public ToolBox registerPlugins(final List<Class<? extends ToolBoxPlugin>> classes) {
		for (Class<? extends ToolBoxPlugin> clazz : classes) {
			registerPlugin(clazz);
		}
		return this;
	}

	public ToolBox registerPlugins(final Class<? extends ToolBoxPlugin>[] classes) {
		for (Class<? extends ToolBoxPlugin> clazz : classes) {
			registerPlugin(clazz);
		}
		return this;
	}

	public ToolBox registerPlugin(final Class<? extends ToolBoxPlugin> aClass) {
		try {
			Object obj = aClass.newInstance();
			if (obj instanceof ToolBoxPlugin) {
				pluginClasses.add(aClass);
				plugins.add((ToolBoxPlugin) obj);
				System.out.println("Add plugin.[" + aClass.getName() + "]");
			} else {
				System.out.println("Unsupported ToolBoxPlugin.[" + aClass.getName() + "]");
			}
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		}
		return this;
	}

	public ToolBoxFileOpen getFileSupport(final File aFile) {
		ToolBoxFileOpen execute = null;

		for (ToolBoxPlugin plugin : plugins) {
			if (plugin instanceof ToolBoxFileOpenSupport) {
				// XXX:微妙
				plugin.setToolBoxFrame(frame);

				ToolBoxFileOpenSupport support = (ToolBoxFileOpenSupport) plugin;

				if (support.isSupportFileOpen(aFile)) {
					execute = support;
					break;
				}
			} else {
				System.out.println("Unsupported file support.");
			}
		}

		return execute;
	}

	public List<JMenuItem> getPopupMenu(final File aFile) {
		List<JMenuItem> menus = new ArrayList<>();

		for (ToolBoxPlugin plugin : plugins) {
			if (plugin instanceof ToolBoxFilePopupMenuSupport) {
				// XXX:微妙
				plugin.setToolBoxFrame(frame);

				ToolBoxFilePopupMenuSupport support = (ToolBoxFilePopupMenuSupport) plugin;

				if (support.isSupportFilePopupMenu(aFile)) {

					List<JMenuItem> lst = support.pupupMenuFile(aFile);
					if (null != lst) {
						menus.addAll(lst);
					}
				}
			} else {
				System.out.println("Unsupported file support.");
			}
		}
		return menus;
	}

	public synchronized boolean setup() {
		boolean result = false;
		if (null == frame) {
			frame = new ToolBoxFrame();
			frame.setTitle(title);
			frame.setVisible(true);
			result = true;
		}
		return result;
	}

	public static ToolBox getInstance() {
		return INSTANCE;
	}

	public MultiTaskServer getServer() {
		return server;
	}
}
