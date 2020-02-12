package nl.vpro.io.prepr.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.checkerframework.checker.nullness.qual.NonNull;

import com.google.common.collect.Range;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

/**
 * The guides call returns data in format which is often unsuitable for processing. It will group by day, but broadcasts may span days.
 * Also, the contained event objects are not self contained, and need the day to calculate the actual time.
 *
 * I don't know yet how it works with time zones.
 *
 * This class wraps the {@link PreprEvent} with {@link LocalDate} object to get a complete small bundle of information which can be used as an entry in lists which represent a schedule.
 *
 * The utility {@link #asRange()} returns the actual range the event is representing.
 *
 * The {@link #append(PreprEventWithDay)} utility targets to be able to 'glue' events together.
 *
 * @author Michiel Meeuwissen
 * @since 0.9
 */
@Getter
@ToString
@Slf4j
public class PreprEventWithDay {
    private final LocalDate day;
    private final ZoneId zoneId;
    private final PreprEvent event;

    PreprEventWithDay next = null;

    PreprEventWithDay(LocalDate day, ZoneId zoneId, PreprEvent event) {
        this.day = day;
        this.event = event;
        this.zoneId = zoneId;
    }

    public Instant getFrom() {
        return asRange().lowerEndpoint().atZone(zoneId).toInstant();
    }
    public Instant getUntil() {
        return asRange().upperEndpoint().atZone(zoneId).toInstant();
    }

    public List<PreprTimeline> getTimelines() {
        List<PreprTimeline> result = new ArrayList<>();
        PreprEventWithDay c = this;
        while(c != null) {
            result.addAll(emptyIfNull(c.getEvent().getTimelines()));
            c = c.getNext();
        }
        return result;
    }
    public PreprShow getShow() {
        return event.getShow();
    }
    public Optional<PreprTimeline> getFirstTimeline() {
        if (event == null) {
            return Optional.empty();
        }
        if (event.getTimelines() == null) {
            return Optional.empty();
        }
        return event
            .getTimelines()
            .stream()
            .findFirst()
            ;

    }

    public PreprUsers getUsers() {
        return event.getUsers();
    }

    public Range<Instant> asRange() {
        Range<Instant> range = Range.closedOpen(
            event.getFrom().atDate(day).atZone(zoneId).toInstant(),
            event.getUntil().atDate(day).atZone(zoneId).toInstant());
        if (next != null) {
            return range.span(next.asRange());
        } else {
            return range;
        }
    }

    public void append(PreprEventWithDay next) {
        if (this.next == null) {
            this.next = next;
        } else {
            this.next.append(next);
        }
    }

    public String showId() {
        if (event.getTimelines() != null) {
            return event.getTimelines().stream().map(AbstractPreprContent::getReference_id).findFirst().orElse(null);
        } else {
            return null;
        }
    }


    public static List<PreprEventWithDay> fromSchedule(@NonNull PreprSchedule unfilteredResult, ZoneId zoneId) {
        List<PreprEventWithDay> result = new ArrayList<>();

        unfilteredResult.forEach((e) -> {
            for (PreprEvent mcEvent : e.getValue()) {
                PreprEventWithDay withDay = new PreprEventWithDay(e.getKey(), zoneId, mcEvent);
                String showId = withDay.showId();
                if (result.size() > 0) {
                    PreprEventWithDay previous = result.get(result.size() - 1);
                    String previousShowId = previous.showId();
                    if (showId != null && Objects.equals(showId, previousShowId)) {
                        log.debug("Appending {} to {}", withDay, previous);
                        previous.append(withDay);
                        continue;
                    }
                }
                result.add(withDay);
            }
        });
        return result;
    }

    public static List<PreprEventWithDay> fromSchedule(@NonNull PreprSchedule unfilteredResult, @NonNull ZoneId zoneId, @NonNull LocalDateTime from, @NonNull LocalDateTime until) {
        Range<Instant> range = Range.closedOpen(from.atZone(zoneId).toInstant(), until.atZone(zoneId).toInstant());
        List<PreprEventWithDay> result = fromSchedule(unfilteredResult, zoneId);

        result.removeIf(event -> {
            Range<Instant> erange = event.asRange();
            boolean startInRange = range.contains(erange.lowerEndpoint());
            if (!startInRange) {
                log.debug("{} not in {}: Removing {})", erange.lowerEndpoint(), range, event);
            }
            return !startInRange;
        });
        return result;

    }

}
