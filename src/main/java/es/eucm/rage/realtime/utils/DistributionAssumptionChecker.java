/**
 * Copyright (C) 2016 e-UCM (http://www.e-ucm.es/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.eucm.rage.realtime.utils;

/**
 * 
 * @author Worker
 */
public class DistributionAssumptionChecker {
	public Distribution checkNormalityAssumption(Distribution distribution) {
		// This function evaluates the class fields to determine if the
		// distribution is normal.
		// The basic result is that a distribution is normal but the assessment
		// of
		// skewness and kurtosis my change this result.
		// Checks for normality are based on Chapter 5 of: Field (2009)
		// Discovering statistics
		// using SPSS. Pp 136-139. ISBN: 978-1-84787-906-6, ISBN:
		// 978-1-84787-907-3.
		// A strict interpretation of these checks is used (tresholds of 1.96
		// and 2.58).
		// Definitions:
		// A small sample is a sample with n < 30.
		// A large sample is a sample with n >= 30.

		final int SMALL_SAMPLE_MAX = 29;
		final Double SMALL_SAMPLE_THRESHOLD = 1.96;
		final Double LARGE_SAMPLE_THRESHOLD = 2.58;

		Boolean normal = true;

		Double skew = distribution.skewness;
		Double kurt = distribution.kurtosis;
		Double mean = distribution.mean;
		Double stdDev = distribution.standardDeviation;
		Long n = distribution.n;

		// This check can only be performed on distributions with a standard
		// deviation that is not zero
		if (stdDev != 0) {
			// Calculate the z-scores of skewness and kurtosis
			Double zSkew = (skew - mean) / stdDev;
			Double zKurt = (kurt - mean) / stdDev;

			// Assumption checks are based on the size of n (see explanation
			// above)
			if (n < SMALL_SAMPLE_MAX) {
				if ((zSkew >= SMALL_SAMPLE_THRESHOLD)
						&& (zSkew <= -SMALL_SAMPLE_THRESHOLD))
					normal = false;
			} else {
				if ((zSkew >= LARGE_SAMPLE_THRESHOLD)
						&& (zSkew <= -LARGE_SAMPLE_THRESHOLD))
					normal = false;
			}
		} else {
			// On a distribution with too few samples the assumptions cannot be
			// checked.
			// On these small distributions the normality is set to false.

			normal = false;
		}

		distribution.normal = normal;

		return distribution;
	}
}
