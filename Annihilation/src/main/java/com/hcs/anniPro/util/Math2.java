package com.hcs.anniPro.util;

import static com.google.common.base.Preconditions.checkNotNull;

public class Math2
{
	/**
	 * @param i
	 *            first number
	 * @param j
	 *            second number
	 * @return if any param is less than 1 returns 1. If not it returns the rounded number up how many times j fit in i
	 */
	public static final int dividedRoundedUp(final int i, final int j)
	{
		checkNotNull(i, "i");
		checkNotNull(j, "j");
		if (i <= 0 || j <= 0)
		{
			return 1;
		}

		int modular = i % j;
		int fit;
		if (modular == 0)
		{
			fit = i / j;
		} else
		{
			fit = 1 + ((i - modular) / j);
		}
		return fit;
	}
}
