//source: https://docs.oracle.com/javase/tutorial/uiswing/examples/components/CustomComboBoxDemoProject/src/components/CustomComboBoxDemo.java
package gameOfLife;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ComboImageText extends JPanel
{
	ImageIcon[] images;
	String[] languages =
	{ "Polski", "English", "Русский", "日本語" };

	// konstruktor
	public ComboImageText()
	{
		super(new BorderLayout());

		// wczytywanie obrazków i tablicy z liczbą elementów
		images = new ImageIcon[languages.length];
		Integer[] intArray = new Integer[languages.length];
		for (int i = 0; i < languages.length; i++)
		{
			intArray[i] = i;
			//Icons mad by: https://www.flaticon.com/authors/freepik
			images[i] = createImageIcon("graphics/flaga" + (i + 1) + ".png");
			if (images[i] != null)
			{
				images[i].setDescription(languages[i]);
			}
		}
		this.setBackground(GameFrame.basicColor);

		// Create the combo box.
		JComboBox languageList = new JComboBox(intArray);
		ComboBoxRenderer renderer = new ComboBoxRenderer();
		renderer.setPreferredSize(new Dimension(100, 50));
		languageList.setRenderer(renderer);
		// żeby się całe nie rozwijało
		// languageList.setMaximumRowCount(3);

		// Lay out the demo.
		add(languageList, BorderLayout.PAGE_START);
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected static ImageIcon createImageIcon(String path)
	{
		java.net.URL imgURL = ComboImageText.class.getResource(path);
		if (imgURL != null)
		{
			return new ImageIcon(imgURL);
		} else
		{
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	/**
	 * Create the GUI and show it. For thread safety, this method should be invoked
	 * from the event-dispatching thread.
	 */

	class ComboBoxRenderer extends JLabel implements ListCellRenderer
	{
		private Font uhOhFont;

		public ComboBoxRenderer()
		{
			setOpaque(true);
			setHorizontalAlignment(CENTER);
			setVerticalAlignment(CENTER);
		}

		/*
		 * This method finds the image and text corresponding to the selected value and
		 * returns the label, set up to display the text and image.
		 */
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus)
		{
			// Get the selected index. (The index parameter isn't
			// always valid, so just use the value.)
			int selectedIndex = ((Integer) value).intValue();

			if (isSelected)
			{
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else
			{
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			// Set the icon and text. If icon was null, say so.
			ImageIcon icon = images[selectedIndex];
			String name = languages[selectedIndex];
			setIcon(icon);
			if (icon != null)
			{
				setText(name);
				setFont(list.getFont());
			} else
			{
				setText(name + " (no image available)");
			}
			return this;
		}

	}
}