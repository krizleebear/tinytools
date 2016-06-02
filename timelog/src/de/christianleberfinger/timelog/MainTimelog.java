/*
 * Copyright 2016 Christian Leberfinger
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.christianleberfinger.timelog;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.time.ZoneId;
import java.util.List;

public class MainTimelog {

	public static void main(String[] args) throws IOException {

		ZoneId timeZone = ZoneId.systemDefault();

		if (args.length > 0) {
			// e.g. "Europe/Berlin"
			timeZone = ZoneId.of(args[0]);
		}

		Timelog log = new Timelog(timeZone);

		List<TimedEvent> powerEvents = log.collect();

		EventPrinter printer = new EventPrinter(timeZone, System.out);
		printer.printEvents(powerEvents);
		
		File outFile = new File("events.txt");
		printToFile(outFile, timeZone, powerEvents);
	}

	public static void printToFile(File f, ZoneId timeZone, List<TimedEvent> events) throws IOException {
		PrintStream ps = null;
		try {
			ps = new PrintStream(f, "UTF-8");
			EventPrinter p = new EventPrinter(timeZone, ps);
			p.printEvents(events);
		} finally {
			if (ps != null) {
				ps.close();
			}
		}
	}
}
