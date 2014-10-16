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

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

import org.azkfw.business.task.Task;

/**
 * @since 1.0.0
 * @version 1.0.0 2014/10/10
 * @author Kawakicchi
 */
public class ToolBoxTaskTableModel extends DefaultTableModel {

	/** serialVersionUID */
	private static final long serialVersionUID = 6969238829444199862L;

	public ToolBoxTaskTableModel() {
		addColumn("タスク");
		addColumn("メッセージ");
		addColumn("プログレス");
		addColumn("コントロール");
	}
	
	public void addTask(final Task aTask) {
		JProgressBar bar = new JProgressBar(0, 100);
		bar.setStringPainted(true);
		bar.setValue(0);

		Object[] datas = { aTask, new JLabel(""), bar, new JButton("button") };
		addRow(datas);
	}
	
	public boolean updateTask(final Task aTask, final double aPercent, final String aMessage) {
		for (int row = 0; row < getRowCount(); row++) {
			Task task = (Task) getValueAt(row, 0);
			if (task.equals(aTask)) {
				JLabel lbl = (JLabel) getValueAt(row, 1);
				JProgressBar bar = (JProgressBar) getValueAt(row, 2);
				lbl.setText(aMessage);
				bar.setValue((int) aPercent);				
				return true;
			}
		}
		return false;
	}
}
