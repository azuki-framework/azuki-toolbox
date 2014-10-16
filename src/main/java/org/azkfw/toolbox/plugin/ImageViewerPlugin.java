package org.azkfw.toolbox.plugin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.azkfw.gui.component.ImagePanel;
import org.azkfw.toolbox.support.ToolBoxFileOpenSupport;

public class ImageViewerPlugin extends AbstractToolBoxPlugin implements ToolBoxFileOpenSupport {

	private Pattern pattern = Pattern.compile("^.*\\.(png|jpg|jpeg)$");

	@Override
	public boolean isSupportFileOpen(final File aFile) {
		String name = aFile.getName().toLowerCase();
		return pattern.matcher(name).matches();
	}

	@Override
	public void openFile(final File aFile) {
		try {
			Image image = ImageIO.read(aFile);
			addTab(aFile.getName(), null, new ImagePanel(image), aFile.getAbsolutePath());
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}
