package org.tmatesoft.sqljet2.internal.system;

public interface VarInt {

	long getValue(Pointer ptr, int offset);

	byte getBytesCount(long value);

	void setValue(Pointer ptr, int offset, long value);
	
	VarInt varInt = new VarInt() {
		
		public byte getBytesCount(long v) {
			byte i = 0;
	        do {
	            i++;
	            v >>= 7;
	        } while (v != 0 && i < 9);
	        return i;
		}

		public long getValue(Pointer p, int offset) {
	        long l = 0;
	        for (byte i = 0; i < 8; i++) {
	            final int b = p.getUnsignedByte(i + offset);
	            l = (l << 7) | (b & 0x7f);
	            if ((b & 0x80) == 0) {
	                return l;
	            }
	        }
	        final int b = p.getUnsignedByte(8 + offset);
	        l = (l << 8) | b;
	        return l;
		}

		public void setValue(Pointer ptr, int offset, long value) {
			// TODO Auto-generated method stub
			
		}
	};
	
}
