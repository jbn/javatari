// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package pc;

import general.av.video.VideoStandard;

import java.io.File;
import java.io.FileInputStream;

import javax.swing.JFileChooser;

import atari.cartridge.Cartridge;
import atari.cartridge.Cartridge12K;
import atari.cartridge.Cartridge16K;
import atari.cartridge.Cartridge4K;
import atari.cartridge.Cartridge8K;
import atari.cartridge.Cartridge8KSliced;
import atari.cartridge.CartridgeDisconnected;

public class FileCartridgeReader {

	// DASM Format 3, no header. Common 2600 cartrige rom format (.bin)
	
	public static Cartridge chooseFile() {
		if (chooser == null)
			chooser = new JFileChooser();
		else 
			chooser.setCurrentDirectory(lastFileOpened);
		int res = chooser.showOpenDialog(null);
		if (res != 0) return null;
		lastFileOpened = chooser.getSelectedFile();
		return read(lastFileOpened);
	}
	
	public static Cartridge read(String fileName) {
		File file = new File(fileName);
		return read(file);
	}

	public static Cartridge read(File file) {
		try {
			FileInputStream stream;
			stream = new FileInputStream(file);
			int len = (int) file.length();
			byte[] buffer = new byte[len];
			System.out.println("Fetching Cartridge file: " + file.getName());
			stream.read(buffer);
			stream.close();
			return create(buffer, file.getName());
		} catch (Exception e) {
			System.out.println("Unable to load Cartridge file: " + file.getName());
			return null;
		}
	}

	// TODO Find a better way to identify the type of Bankswitching and the VideoStandard of Cartridges
	private static Cartridge create(byte[] content, String fileName) {
		Cartridge cart = null; 
		if (fileName.toUpperCase().indexOf("SLICED") >= 0) {
			switch (content.length) {
				case Cartridge8KSliced.SIZE:
					cart = new Cartridge8KSliced(content); break;
				default:
					throw new UnsupportedOperationException("Cartridge size not supported: " + content.length);
			}
		} else {
			switch (content.length) {
				case CartridgeDisconnected.SIZE:
					cart = new CartridgeDisconnected(); break;
				case Cartridge4K.HALF_SIZE:
				case Cartridge4K.SIZE:
					cart = new Cartridge4K(content); break;
				case Cartridge8K.SIZE:
					cart = new Cartridge8K(content); break;
				case Cartridge12K.SIZE:
					cart = new Cartridge12K(content); break;
				case Cartridge16K.SIZE:
					cart = new Cartridge16K(content); break;
				default:
					throw new UnsupportedOperationException("Cartridge size not supported: " + content.length);
			}
		}
		// Set the Video Standard based on the filename. Default is NTSC
		if (fileName.toUpperCase().indexOf("PAL") >= 0)
			cart.forceVideoStandard(VideoStandard.PAL);
		return cart;
	}
	
	private static JFileChooser chooser;
	private static File lastFileOpened = null;

}
