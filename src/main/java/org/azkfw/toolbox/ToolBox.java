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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JMenuItem;

import org.azkfw.business.task.server.MultiTaskServer;
import org.azkfw.gui.dialog.PreferenceDialog;
import org.azkfw.toolbox.plugin.ImageViewerPlugin;
import org.azkfw.toolbox.plugin.ToolBoxPlugin;
import org.azkfw.toolbox.support.ToolBoxFileOpen;
import org.azkfw.toolbox.support.ToolBoxFileOpenSupport;
import org.azkfw.toolbox.support.ToolBoxFilePopupMenuSupport;
import org.azkfw.toolbox.support.ToolBoxPreferenceSupport;

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
	private List<Class<? extends ToolBoxPlugin>> pluginList;
	private Map<Class<? extends ToolBoxPlugin>, ToolBoxPlugin> plugins;
	private Map<Class<? extends ToolBoxPlugin>, Properties> pluginProperties;

	private ToolBox() {
		pluginList = new ArrayList<Class<? extends ToolBoxPlugin>>();
		plugins = new HashMap<Class<? extends ToolBoxPlugin>, ToolBoxPlugin>();
		pluginProperties = new HashMap<Class<? extends ToolBoxPlugin>, Properties>();

		registerPlugin(ImageViewerPlugin.class);
	}

	public static ToolBox getInstance() {
		return INSTANCE;
	}

	public synchronized boolean setup() {
		boolean result = false;

		loadData();

		server = new MultiTaskServer();
		server.start();

		if (null == frame) {
			frame = new ToolBoxFrame();
			frame.setTitle(title);
			frame.setVisible(true);
			result = true;
		}
		return result;
	}

	public synchronized void terminate() {
		storeData();
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
				pluginList.add(aClass);
				plugins.put(aClass, (ToolBoxPlugin) obj);
				pluginProperties.put(aClass, new Properties());
				// System.out.println("Add plugin.[" + aClass.getName() + "]");
			} else {
				// System.out.println("Unsupported ToolBoxPlugin.[" +
				// aClass.getName() + "]");
			}
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
		}
		return this;
	}

	public void putPreferenceSupport(final PreferenceDialog aDialog) {
		for (Class<? extends ToolBoxPlugin> clazz : pluginList) {
			ToolBoxPlugin plugin = plugins.get(clazz);
			if (plugin instanceof ToolBoxPreferenceSupport) {
				ToolBoxPreferenceSupport support = (ToolBoxPreferenceSupport) plugin;

				List<ToolBoxPreferenceSupport.ToolBoxPreferenceData> datas = support.getPreferenceDataList();
				if (null != datas) {
					for (ToolBoxPreferenceSupport.ToolBoxPreferenceData data : datas) {
						aDialog.addPreference(data.getPath(), data.getTitle(), data.getPanel());
					}
				}
			}
		}
	}

	public void loadData() {
		for (Class<? extends ToolBoxPlugin> clazz : pluginList) {
			ToolBoxPlugin plugin = plugins.get(clazz);
			if (plugin instanceof ToolBoxPreferenceSupport) {
				ToolBoxPreferenceSupport support = (ToolBoxPreferenceSupport) plugin;

				Properties p = pluginProperties.get(plugin.getClass());

				try {
					File proFile = Paths.get(".", "plugin", plugin.getClass().getName(), "preference.properties").toFile();
					if (proFile.isFile()) {
						p.load(new FileInputStream(proFile));
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				support.load(p);
			}
		}
	}

	public void storeData() {
		for (Class<? extends ToolBoxPlugin> clazz : pluginList) {
			ToolBoxPlugin plugin = plugins.get(clazz);
			if (plugin instanceof ToolBoxPreferenceSupport) {
				ToolBoxPreferenceSupport support = (ToolBoxPreferenceSupport) plugin;

				Properties p = pluginProperties.get(plugin.getClass());

				support.store(p);

				try {
					File proFile = Paths.get(".", "plugin", plugin.getClass().getName(), "preference.properties").toFile();
					File parentDir = proFile.getParentFile();
					parentDir.mkdirs();

					p.store(new FileOutputStream(proFile), null);
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public ToolBoxFileOpen getFileSupport(final File aFile) {
		ToolBoxFileOpen execute = null;

		for (Class<? extends ToolBoxPlugin> clazz : pluginList) {
			ToolBoxPlugin plugin = plugins.get(clazz);
			if (plugin instanceof ToolBoxFileOpenSupport) {
				// XXX:微妙
				plugin.setToolBoxFrame(frame);

				ToolBoxFileOpenSupport support = (ToolBoxFileOpenSupport) plugin;

				if (support.isSupportFileOpen(aFile)) {
					execute = support;
					break;
				}
			} else {
				// System.out.println("Unsupported file support.");
			}
		}

		return execute;
	}

	public List<JMenuItem> getPopupMenu(final File aFile) {
		List<JMenuItem> menus = new ArrayList<>();

		for (Class<? extends ToolBoxPlugin> clazz : pluginList) {
			ToolBoxPlugin plugin = plugins.get(clazz);
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
				// System.out.println("Unsupported file support.");
			}
		}
		return menus;
	}

	public ToolBoxPlugin getPlugin(final Class<? extends ToolBoxPlugin> aClass) {
		return plugins.get(aClass);
	}

	public MultiTaskServer getServer() {
		return server;
	}
}
