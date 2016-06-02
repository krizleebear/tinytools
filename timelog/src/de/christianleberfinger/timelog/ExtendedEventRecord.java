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

import java.lang.reflect.Field;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.sun.jna.platform.win32.Advapi32Util.EventLogRecord;
import com.sun.jna.platform.win32.WinDef.DWORD;

/**
 * Wrapper around {@link EventLogRecord} offering convenience methods, like
 * accessing creation time.
 */
public class ExtendedEventRecord {

	private EventLogRecord record;
	private long timestampCreated;

	public ExtendedEventRecord(EventLogRecord record) {
		this.record = record;
		this.timestampCreated = getTimestampCreated();
	}

	public EventLogRecord getRecord() {
		return record;
	}
	
	public int getEventId()
	{
		return record.getEventId();
	}
	
	public long getTimestampCreated() {
		Object o = getPrivateField(record, "_record");
		DWORD t = (DWORD) getPrivateField(o, "TimeGenerated");
		long unixTime = t.longValue();
		return unixTime;
	}

	public ZonedDateTime getTimeCreated(ZoneId timeZone) {
		return fromUnixtime(timeZone, timestampCreated);
	}

	public static ZonedDateTime fromUnixtime(ZoneId timeZone, long unixTime) {
		Instant timeInstant = Instant.ofEpochSecond(unixTime);
		ZonedDateTime dateTime = timeInstant.atZone(timeZone);
		return dateTime;
	}

	public static Object getPrivateField(Object obj, String fieldname) {
		Field f;
		try {
			f = obj.getClass().getDeclaredField(fieldname);
			f.setAccessible(true);
			return f.get(obj);
		} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
			throw new RuntimeException(e);
		}
	}

	public boolean isSameDay(ZonedDateTime other) {
		ZonedDateTime timeCreated = getTimeCreated(other.getZone());
		if (other.getYear() != timeCreated.getYear()) {
			return false;
		}

		return other.getDayOfYear() == timeCreated.getDayOfYear();
	}
}
