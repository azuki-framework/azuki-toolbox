package org.azkfw.toolbox.support;

import java.util.List;
import java.util.Properties;

import org.azkfw.gui.dialog.PreferencePanel;

public interface ToolBoxPreferenceSupport {

	public static class ToolBoxPreferenceData {
		private String path;
		private String title;
		private PreferencePanel panel;

		public ToolBoxPreferenceData(final String aPath, final String aTitle, final PreferencePanel aPanel) {
			path = aPath;
			title = aTitle;
			panel = aPanel;
		}

		public String getPath() {
			return path;
		}

		public String getTitle() {
			return title;
		}

		public PreferencePanel getPanel() {
			return panel;
		}
	}

	public List<ToolBoxPreferenceData> getPreferenceDataList();

	public void load(final Properties properties);

	public void store(final Properties properties);
}
