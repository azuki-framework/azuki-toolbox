package org.azkfw.toolbox.support;

import java.util.List;

import org.azkfw.gui.dialog.PreferenceClientPanel;

public interface ToolBoxPreferenceSupport {

	public static class ToolBoxPreferenceData {
		private String path;
		private String title;
		private PreferenceClientPanel panel;

		public ToolBoxPreferenceData(final String aPath, final String aTitle, final PreferenceClientPanel aPanel) {
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

		public PreferenceClientPanel getPanel() {
			return panel;
		}
	}
	
	public List<ToolBoxPreferenceData> getPreferenceDataList();
}
