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

import java.time.ZonedDateTime;

public class TimedEvent {

	private ZonedDateTime timeCreated;
	private ExtendedEventRecord event;

	public TimedEvent(ZonedDateTime timeCreated, ExtendedEventRecord event) {
		this.timeCreated = timeCreated;
		this.event = event;
	}

	public boolean isSameDay(ZonedDateTime other) {
		return event.isSameDay(other);
	}

	public ZonedDateTime getTimeCreated() {
		return timeCreated;
	}

	public ExtendedEventRecord getEventRecord() {
		return event;
	}
}
