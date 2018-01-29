package util.math;

import java.math.BigDecimal;

/**
 * @author André Schmer xxx
 *
 */
public class FloatComparator {

	private static final float epsilon = 0.0001f;

	private static float precision = 100;

	/**
	 * Compares if the first arg {@code smaller} is smaller than the second arg {@code greater} within a acceptable difference or equal than the second.
	 * 
	 * Uses the prcision value of {@value #epsilon}
	 * 
	 * @param smaller
	 * @param greater
	 * @return true | false
	 */
	public static boolean smallerOrEqual(final float smaller, final float greater) {
		final float diff = (smaller - greater);
		if (diff == 0) {
			return true;
		}
		return diff < epsilon;
	}

	/**
	 * Returns a float value which is closest to given parameter and under a precision of 100.
	 * 
	 * @param f
	 * @return
	 */
	public static float rr(final float f) {
		return (float) Math.rint(f * precision) / precision;
	}

	/**
	 * Compares if the param {@code smaller} is smaller or equal the second arg {@code greater}. Uses the default precision of two decimal points.
	 * 
	 * @param smaller
	 * @param greater
	 * @return true | false
	 */
	public static boolean smallerOrEqualWithPrecision(final float smaller, final float greater) {
		final float diff = (rr(smaller) - rr(greater));
		if (diff == 0) {
			return true;
		}
		return diff < epsilon;
	}

	/**
	 * Compares if the first arg {@code greater} is greater than the second arg {@code smaller}.
	 * 
	 * @param greater
	 * @param smaller
	 * @return true | false
	 */
	public static boolean greaterThan(final float greater, final float smaller) {
		final float diff = (greater - smaller);
		if (diff == 0) {
			return false;
		}
		return diff > epsilon;
	}

	/**
	 * Compares if the first arg {@code greater} is greater than the second arg {@code smaller}. Uses the default precision of two decimal points.
	 * 
	 * @param greater
	 * @param smaller
	 * @return true | false
	 */
	public static boolean greaterThanWithPrecision(final float greater, final float smaller) {
		final float diff = (rr(greater) - rr(smaller));
		if (diff == 0) {
			return false;
		}
		return diff > epsilon;
	}

	/**
	 * Compares if {@code greater} arg is greater or equal than {@code smaller} arg within a acceptable difference or equal than the second.
	 * 
	 * @param greater
	 * @param smaller
	 * @return true | false
	 */
	public static boolean greaterOrEqual(final float greater, final float smaller) {
		final int result = new Float(greater).compareTo(new Float(smaller));
		return result >= 0;
	}

	/**
	 * Uses {@link BigDecimal} to wrap the float values and operate a compare to it.
	 * 
	 * @param greater
	 * @param than
	 * @return true | false
	 */
	public static boolean greaterThanBD(final float greater, final float than) {
		return BigDecimal.valueOf(greater).compareTo(BigDecimal.valueOf(than)) > 0;
	}

	/**
	 * Uses {@link BigDecimal} to wrap the float values and operate a compare to it.
	 * 
	 * @param greater
	 * @param than
	 * @return true | false
	 */
	public static boolean greaterOrEqualBD(final float greater, final float than) {
		final int result = BigDecimal.valueOf(greater).compareTo(BigDecimal.valueOf(than));
		return result >= 0;
	}

	/**
	 * Uses {@link BigDecimal} to wrap the float values and operate a compare to it.
	 * 
	 * @param smaller
	 * @param than
	 * @return true | false
	 */
	public static boolean smallerThanBD(final float smaller, final float than) {
		return BigDecimal.valueOf(smaller).compareTo(BigDecimal.valueOf(than)) < 0;
	}

	/**
	 * Uses {@link BigDecimal} to wrap the float values and operate a compare to it.
	 * 
	 * @param smaller
	 * @param than
	 * @return true | false
	 */
	public static boolean smallerOrEqualBD(final float smaller, final float than) {
		final int result = BigDecimal.valueOf(smaller).compareTo(BigDecimal.valueOf(than));
		return result <= 0;
	}

	/**
	 * Uses {@link BigDecimal} to wrap the float values and operate a compare to it.
	 * 
	 * @param equal
	 * @param to
	 * @return true | false
	 */
	public static boolean equalsBD(final float equal, final float to) {
		return BigDecimal.valueOf(equal).compareTo(BigDecimal.valueOf(to)) == 0;
	}

	/**
	 * Compares two float values if there are equal.
	 * 
	 * @param a
	 * @param b
	 * @return true | false
	 */
	public static boolean isEqual(final float a, final float b) {
		final float diff = (a - b);
		if (diff == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Compares two float values {@code a} and {@code b} if there difference is smaller than a given epsilon.
	 * 
	 * @param a
	 * @param b
	 * @param epsilon
	 * @return true | false
	 */
	public static boolean isDiffSmallerThanEpsilon(final float a, final float b, final float epsilon) {
		final float diff = (a - b);
		if (diff == 0) {
			return false;
		}
		return diff <= epsilon;
	}

	/**
	 * Compares if {@code a} is equal than {@code b}. It considers a scale of 2 with a {@code round half up} strategy.
	 * 
	 * @param a
	 * @param b
	 * @return true if {@code a} is equal than {@code b}, false otherwise.
	 */
	public static boolean isDiffEqual(final float a, final float b) {
		final BigDecimal bdA = BigDecimal.valueOf(a).setScale(2, BigDecimal.ROUND_HALF_UP);
		final BigDecimal bdB = BigDecimal.valueOf(b).setScale(2, BigDecimal.ROUND_HALF_UP);
		return bdA.compareTo(bdB) == 0;
	}

	/**
	 * Compares if {@code a} is smaller than {@code b}. It considers a scale of 2 with a {@code round half up} strategy.
	 * 
	 * @param a
	 * @param b
	 * @return true if {@code a} is smaller than {@code b}, false otherwise.
	 */
	public static boolean isDiffSmaller(final float a, final float b) {
		final BigDecimal bdA = BigDecimal.valueOf(a).setScale(2, BigDecimal.ROUND_HALF_UP);
		final BigDecimal bdB = BigDecimal.valueOf(b).setScale(2, BigDecimal.ROUND_HALF_UP);
		return bdA.compareTo(bdB) < 0;
	}

	/**
	 * Compares if {@code a} is greater than {@code b}. It considers a scale of 2 with a {@code round half up} strategy.
	 * 
	 * @param a
	 * @param b
	 * @return true if {@code a} is greater than {@code b}, false otherwise.
	 */
	public static boolean isDiffGreater(final float a, final float b) {
		final BigDecimal bdA = BigDecimal.valueOf(a).setScale(2, BigDecimal.ROUND_HALF_UP);
		final BigDecimal bdB = BigDecimal.valueOf(b).setScale(2, BigDecimal.ROUND_HALF_UP);
		return bdA.compareTo(bdB) > 0;
	}

}
