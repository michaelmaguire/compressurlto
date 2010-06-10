package ca.michaelmaguire.server.compressurlto.server;

public class Base36 {

	private static String BASE36DIGITS = "0123456789abcdefghijklmnopqrstuvwxyz";

	public static String convertToBase36(long longDecimal) {
		String response = (longDecimal == 0 ? "0" : "");

		int mod = 0;

		while (longDecimal != 0L) {
			mod = (int) (longDecimal % 36L);
			response = BASE36DIGITS.substring(mod, mod + 1) + response;
			longDecimal = longDecimal / 36;
		}

		return response;
	}

	public static long convertFromBase36(String aBase36) {
		int iterator = aBase36.length();
		long returnValue = 0L;
		long multiplier = 1L;

		while (iterator > 0) {
			returnValue = returnValue
					+ (BASE36DIGITS.indexOf(aBase36.substring(iterator - 1,
							iterator)) * multiplier);
			multiplier = multiplier * 36;
			--iterator;
		}
		return returnValue;
	}
}
