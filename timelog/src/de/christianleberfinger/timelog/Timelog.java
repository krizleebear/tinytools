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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.platform.win32.Advapi32Util.EventLogIterator;
import com.sun.jna.platform.win32.Advapi32Util.EventLogRecord;

/**
 * Very simple tool that lets you know when your computer was switched on or
 * off. (or went to sleep and woke up). You might find that useful to track your
 * work hours. It uses JNA to parse Windows Event Log.
 */
public class Timelog {

	private ZoneId timezone;

	public enum EventTypes {
		SLEEP(42), WAKE(1), POWERUP(12), SHUTDOWN(13);

		private int eventID;

		EventTypes(int eventID) {
			this.eventID = eventID;
		}

		public static boolean contains(int id) {
			for (EventTypes type : values()) {
				if (id == type.eventID) {
					return true;
				}
			}
			return false;
		}

		public static EventTypes fromInt(int id) {
			for (EventTypes type : values()) {
				if (id == type.eventID) {
					return type;
				}
			}
			throw new RuntimeException("invalid argument: " + id);
		}
	}

	public Timelog(ZoneId timezone) {
		this.timezone = timezone;
	}

	public List<TimedEvent> collect() {

		List<TimedEvent> events = new ArrayList<>();
		EventLogIterator iter = new EventLogIterator("System");

		while (iter.hasNext()) {
			EventLogRecord record = iter.next();
			ExtendedEventRecord ext = new ExtendedEventRecord(record);

			// ignore irrelevant events
			int eventID = record.getEventId();
			if (!EventTypes.contains(eventID)) {
				continue;
			}

			ZonedDateTime timeCreated = ext.getTimeCreated(timezone);
			TimedEvent event = new TimedEvent(timeCreated, ext);
			events.add(event);
		}

		return events;
	}
}
