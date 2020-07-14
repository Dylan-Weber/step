// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public final class FindMeetingQuery {

  public static final int LONGEST_MEETING_DURATION = 24*60;

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    
    long duration = request.getDuration();
    if (duration > LONGEST_MEETING_DURATION) {
      return new ArrayList<>();
    }

    Set<String> requestAttendees = new HashSet<>();
    requestAttendees.addAll(request.getAttendees());
    List<StartEndTime> eventTimes = new ArrayList<>();

    for (Event event : events) {
      Set<String> eventAttendees = event.getAttendees();
      Set<String> attendeeIntersection = intersection(requestAttendees, eventAttendees);
      
      if (!attendeeIntersection.isEmpty()) {
        TimeRange eventTime = event.getWhen();
        eventTimes.add(new StartEndTime(TimeType.START, eventTime.start()));
        eventTimes.add(new StartEndTime(TimeType.END, eventTime.end()));
      }
    }

    Collections.sort(eventTimes);

    int conflicts = 0;
    int startOfOpenTime = TimeRange.START_OF_DAY;

    List<TimeRange> potentialTimes = new ArrayList<>();

    for (StartEndTime time : eventTimes) {
      if (time.type == TimeType.START) {
        conflicts++;
        
        if (conflicts == 1) {
          addTimeRangeIfValid(potentialTimes, duration, startOfOpenTime, time.time);
        }
      } else {
        conflicts--;

        if (conflicts == 0) {
          startOfOpenTime = time.time;
        }
      }
    }

    if (conflicts == 0) {
      addTimeRangeIfValid(potentialTimes, duration, startOfOpenTime, TimeRange.END_OF_DAY + 1);
    }

    return potentialTimes;
  }

  private <T> Set<T> intersection(Set<T> first, Set<T> second) {
    Set<T> result = new HashSet<>(first);
    result.retainAll(second);
    return result;
  }

  private void addTimeRangeIfValid(Collection<TimeRange> potentialTimes, long minimumDuration, int start, int end) {
    if (minimumDuration <= end - start) {
      TimeRange timeRangeToAdd = TimeRange.fromStartEnd(start, end, false);
      potentialTimes.add(timeRangeToAdd);
    }
  }

  private enum TimeType {
    START,
    END;
  }

  private static final class StartEndTime implements Comparable<StartEndTime> {
    
    TimeType type;
    int time;

    StartEndTime(TimeType type, int time) {
      this.type = type;
      this.time = time;
    }

    @Override
    public int compareTo(StartEndTime other) {
      return this.time - other.time;
    }
  }
}
