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

import javax.swing.JFrame;

import org.azkfw.business.task.Task;
import org.azkfw.gui.dialog.ConfigurationDialog;

/**
 * このクラスは、設定ダイアログ画面です。
 * 
 * @since 1.0.0
 * @version 1.0.0 2014/10/06
 * @author Kawakicchi
 */
public abstract class TaskConfigurationDialog extends ConfigurationDialog {

	/** serialVersionUID */
	private static final long serialVersionUID = -8908616024008040757L;

	private Task task;

	public TaskConfigurationDialog(final JFrame frame) {
		super(frame);
	}

	public void setTask(final Task aTask) {
		task = aTask;
	}

	public Task getTask() {
		return task;
	}
}
