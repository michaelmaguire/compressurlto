package ca.michaelmaguire.server.compressurlto.server;

import junit.framework.TestCase;

public class Base36Test extends TestCase {

	public void testConvertToBase36() {
		
		assertEquals( "0", Base36.convertToBase36(0L) );
		assertEquals( "1", Base36.convertToBase36(1L) );
		assertEquals( "a", Base36.convertToBase36(10L) );
		assertEquals( "2s", Base36.convertToBase36(100L) );
		assertEquals( "rs", Base36.convertToBase36(1000L) );
		assertEquals( "7ps", Base36.convertToBase36(10000L) );
		assertEquals( "255s", Base36.convertToBase36(100000L) );
		assertEquals( "lfls", Base36.convertToBase36(1000000L) );
		assertEquals( "gjdgxs", Base36.convertToBase36(1000000000L) );
		assertEquals( "cre66i9s", Base36.convertToBase36(1000000000000L) );
	}

	public void testConvertFromBase36() {
		assertEquals( 0L, Base36.convertFromBase36("0") );
		assertEquals( 1L, Base36.convertFromBase36("1") );
		assertEquals( 36L, Base36.convertFromBase36("10") );
		assertEquals( 1296L, Base36.convertFromBase36("100") );
		assertEquals( 46656L, Base36.convertFromBase36("1000") );
		assertEquals( 1679616L, Base36.convertFromBase36("10000") );
		assertEquals( 60466176L, Base36.convertFromBase36("100000") );
		assertEquals( 2176782336L, Base36.convertFromBase36("1000000") );
		assertEquals( 78364164096L, Base36.convertFromBase36("10000000") );
		assertEquals( 2821109907456L, Base36.convertFromBase36("100000000") );
	}

}
