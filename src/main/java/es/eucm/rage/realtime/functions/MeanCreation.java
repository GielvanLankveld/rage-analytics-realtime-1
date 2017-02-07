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
package es.eucm.rage.realtime.functions;

import es.eucm.rage.realtime.utils.Distribution;
import es.eucm.rage.realtime.utils.DistributionAssumptionChecker;
import es.eucm.rage.realtime.utils.DistributionUpdater;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import org.apache.storm.trident.operation.Function;
import org.apache.storm.trident.operation.TridentCollector;
import org.apache.storm.trident.operation.TridentOperationContext;
import org.apache.storm.trident.tuple.TridentTuple;
import java.util.Map;
import org.elasticsearch.client.transport.TransportClient;

public class MeanCreation implements Function {

	private String sessionId;
	private TransportClient client;
	private String myIndex;

	public MeanCreation(TransportClient client, String sessionId) {
		this.client = client;
		this.sessionId = sessionId;
		this.myIndex = "meanData-" + sessionId;
	}

	@Override
	public void execute(TridentTuple tuple, TridentCollector collector) {
		// Connect the flient to "myIndex" and query the value
		Double score = Double.parseDouble(tuple
				.getStringByField("PerfStatsScore"));

		// Query any preexistinng distributions from the elasticsearch client
                

		// Perform the mean algorithm, for the sake of this example it's
		// "result" double variable
		/*
		 * Distribution myDist = new Distribution(); DistributionUpdater
		 * myAnalysis = new DistributionUpdater(); DistributionAssumptionChecker
		 * myChecker = new DistributionAssumptionChecker();
		 */

		double result = score;
                
                Map<String, Object> results = new HashMap<String, Object>();
                results.put("meanScore", result);
                results.put("timestamp", new Date());

		// Emit the current result result to Trident system
		collector.emit(Arrays.asList(results));

		// Store the new compunded? mean value to "myIndex"
	}

	@Override
	public void prepare(Map conf, TridentOperationContext context) {

	}

	@Override
	public void cleanup() {

	}
}
