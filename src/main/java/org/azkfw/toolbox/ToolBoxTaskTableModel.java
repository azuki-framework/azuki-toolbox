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

import java.util.Calendar;
import java.util.Date;

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
		addColumn("開始日時");
		addColumn("終了日時");
		addColumn("プログレス");
		addColumn("コントロール");		
	}

	public void addTask(final Task aTask) {
		JProgressBar bar = new JProgressBar(0, 100);
		bar.setStringPainted(true);
		bar.setValue(0);

		TaskInfo info = new TaskInfo(aTask);

		Object[] datas = { info, new JLabel(""), new JLabel("--/-- --:--"), new JLabel("--/-- --:--"), bar, new JButton("button") };
		addRow(datas);
	}

	public boolean startTask(final Task aTask) {
		int index = getIndex(aTask);
		if (-1 != index) {
			Calendar cln = Calendar.getInstance();
			cln.setTime(new Date());

			JLabel lbl = (JLabel) getValueAt(index, 2); // start date
			lbl.setText(String.format("%02d/%02d %02d:%02d", cln.get(Calendar.MONTH) + 1, cln.get(Calendar.DAY_OF_MONTH),
					cln.get(Calendar.HOUR_OF_DAY), cln.get(Calendar.MINUTE)));

			return true;
		}
		return false;
	}

	public boolean stopTask(final Task aTask) {
		int index = getIndex(aTask);
		if (-1 != index) {
			Calendar cln = Calendar.getInstance();
			cln.setTime(new Date());

			JLabel lbl = (JLabel) getValueAt(index, 3); // stop date
			lbl.setText(String.format("%02d/%02d %02d:%02d", cln.get(Calendar.MONTH) + 1, cln.get(Calendar.DAY_OF_MONTH),
					cln.get(Calendar.HOUR_OF_DAY), cln.get(Calendar.MINUTE)));

			return true;
		}
		return false;
	}

	public boolean updateTask(final Task aTask, final double aPercent, final String aMessage) {
		int index = getIndex(aTask);
		if (-1 != index) {
			JLabel lbl = (JLabel) getValueAt(index, 1); // label
			JProgressBar bar = (JProgressBar) getValueAt(index, 4); // progress

			lbl.setText(aMessage);
			bar.setValue((int) aPercent);
			return true;
		}
		return false;
	}

	private int getIndex(final Task aTask) {
		int index = -1;
		for (int row = 0; row < getRowCount(); row++) {
			TaskInfo info = (TaskInfo) getValueAt(row, 0);
			if (aTask.equals(info.getTask())) {
				index = row;
				break;
			}
		}
		return index;
	}

	private class TaskInfo {
		private String name;
		private Task task;

		public TaskInfo(final Task aTask) {
			task = aTask;
			name = task.getName();
		}

		public String toString() {
			return name;
		}

		public Task getTask() {
			return task;
		}
	}
}
