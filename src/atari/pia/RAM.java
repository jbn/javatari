// Copyright 2011-2012 Paulo Augusto Peccin. See licence.txt distributed with this file.

package atari.pia;

import general.board.BUS16Bits;

import java.io.Serializable;

import utils.Randomizer;

public final class RAM implements BUS16Bits {

	public RAM() {
		init();
	}
	
	public byte readByte(int address) {		
		return bytes[(address & ADDRESS_MASK)];	
	}

	public int unsignedByte(int address) {  
		return bytes[(address & ADDRESS_MASK)] & 0xff;
	}
	
	public void writeByte(int address, byte b) {	
		bytes[(address & ADDRESS_MASK)] = b;		
	}

	private void init() {
		// RAMs come totally random at power on!
		Randomizer.instance.nextBytes(bytes);
	}

	public void dump() {
		System.out.println("RAM DUMP:");
		for(int i = 0; i < bytes.length; i++)
			System.out.printf("%02x ", unsignedByte(i));
		System.out.println();
	}
	
	public RAMState saveState() {
		RAMState state = new RAMState();
		state.bytes = bytes.clone();
		return state;
	}
	
	public void loadState(RAMState state) {
		System.arraycopy(state.bytes, 0, bytes, 0, bytes.length);
	}
	

	// State Variables --------------------------------------
	private final byte[] bytes = new byte[128];
	
	
	// Constants -------------------------------------------
	private static final int ADDRESS_MASK = 0x007f;


	// Used to save/load states
	public static class RAMState implements Serializable {
		byte[] bytes;
		private static final long serialVersionUID = 1L;
	}

}
