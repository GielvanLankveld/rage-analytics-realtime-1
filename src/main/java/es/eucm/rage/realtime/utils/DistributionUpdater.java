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
public class DistributionUpdater {
	public Distribution updateDistribution(Distribution distribution,
			double newScore) {

		// New N, code guarantees N of 1 or higher
		Long tmpNL = distribution.n;

		if (tmpNL <= 0) {
			tmpNL = 1L;

			// First max and min
			distribution.max = newScore;
			distribution.min = newScore;
		} else {
			tmpNL++;

			// New max
			if (newScore > distribution.max)
				distribution.max = newScore;

			// New min
			if (newScore < distribution.min)
				distribution.min = newScore;
		}
		distribution.n = tmpNL;
		Double tmpN = (double) tmpNL;

		// New sum
		distribution.sum = newScore + distribution.sum;

		// New mean, variance, & stdDev >> based on:
		// http://www.johndcook.com/blog/standard_deviation/
		Double oldMean = distribution.mean;
		Double newMean;
		Double oldS = distribution.variance;
		Double newS;

		if (tmpN == 1) {
			newMean = newScore;
			newS = 0D;
		} else {
			// New means formula suitable for big data
			// Previous code guarantees tmpN > 0
			newMean = oldMean + (newScore - oldMean) / tmpN;
			newS = oldS + (newScore - oldMean) * (newScore - newMean);
		}
		distribution.mean = newMean;
		distribution.variance = ((tmpN > 1) ? (newS / (tmpN - 1)) : 0D);
		distribution.standardDeviation = (Math.sqrt(distribution.variance));

		// New skewness & kurtosis >> based on:
		// http://www.johndcook.com/blog/skewness_kurtosis/
		double delta, delta_n, delta_n2, term1;

		delta = newScore - newMean;
		delta_n = delta / tmpN;
		delta_n2 = delta_n * delta_n;
		term1 = delta * delta_n * tmpN;
		distribution.helper3 = ((term1 * delta_n2 * (tmpN * tmpN - 3 * tmpN + 3))
				+ (6 * delta_n2 * distribution.helper1) - (4 * delta_n * distribution.helper2));
		distribution.helper2 = ((term1 * delta_n * (tmpN - 2)) - (3 * tmpN
				* delta_n * distribution.helper1));
		distribution.helper1 = term1;

		distribution.skewness = (Math.sqrt((double) tmpN)
				* distribution.helper2 / Math.pow(distribution.helper1, 1.5));
		distribution.kurtosis = (((double) tmpN) * distribution.helper3
				/ (distribution.helper1 * distribution.helper1) - 3.0);

		return distribution;
	}
}
