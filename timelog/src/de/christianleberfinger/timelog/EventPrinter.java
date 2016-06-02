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

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import de.christianleberfinger.timelog.Timelog.EventTypes;

public class EventPrinter {

	private ZoneId timeZone;
	private PrintStream ps;

	public EventPrinter(ZoneId timeZone, PrintStream out) {
		this.timeZone = timeZone;
		this.ps = out;
	}
	
	public void printEvents(List<TimedEvent> events) {
		DateTimeFormatter isoLocalDate = DateTimeFormatter.ISO_LOCAL_DATE;
		DateTimeFormatter isoLocalTime = DateTimeFormatter.ISO_LOCAL_TIME;

		ZonedDateTime lastEventTime = ZonedDateTime.of(LocalDateTime.MIN, timeZone);

		for (Iterator<TimedEvent> iterator = events.iterator(); iterator.hasNext();) {
			TimedEvent timedEvent = (TimedEvent) iterator.next();
			ZonedDateTime timeCreated = timedEvent.getTimeCreated();

			// print a marker for each new day
			if (!timedEvent.isSameDay(lastEventTime)) {
				ps.print("------------ ");
				ps.print(timeCreated.format(isoLocalDate));
				ps.println(" ------------ ");
			}
			lastEventTime = timeCreated;

			ExtendedEventRecord eventRecord = timedEvent.getEventRecord();
			EventTypes eventType = EventTypes.fromInt(eventRecord.getEventId());
			String formattedTime = timeCreated.format(isoLocalTime);

			ps.println(String.format("%s: %s", formattedTime, eventType));
		}
	}
}
