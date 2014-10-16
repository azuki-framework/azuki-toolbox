package org.azkfw.toolbox.plugin;

import javax.swing.Icon;
import javax.swing.JPanel;

import org.azkfw.business.task.Task;
import org.azkfw.toolbox.ToolBoxFrame;

public abstract class AbstractToolBoxPlugin implements ToolBoxPlugin {

	private ToolBoxFrame frame;

	public void setToolBoxFrame(final ToolBoxFrame aFrame) {
		frame = aFrame;
	}

	protected final void queueTask(final Task aTask) {
		frame.queueTask(aTask);
	}

	protected final void addTab(final String aTitle, final JPanel aPanel) {
		frame.addTab(aTitle, aPanel);
	}

	protected final void addTab(final String aTitle, final Icon aIcon, final JPanel aPanel, final String aToolTip) {
		frame.addTab(aTitle, aIcon, aPanel, aToolTip);
	}

}
