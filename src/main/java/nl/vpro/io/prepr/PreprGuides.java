package nl.vpro.io.prepr;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;

import nl.vpro.io.prepr.domain.*;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprGuides {


    MCSchedule getSchedule(@Nonnull LocalDate from, @Nonnull LocalDate until, boolean exceptions, UUID showId);

    /**
     * Performs {@link #getSchedule(LocalDateTime, LocalDateTime)}, and wraps the result into a list of {@link MCEventWithDay}
     */

    default List<MCEventWithDay> getSchedule(@Nonnull  LocalDateTime from, @Nonnull  LocalDateTime until) {

        // broadcasts may span 0 o'clock, so we need the day before and after too, to make sure that we get only _complete_ broadcasts
        LocalDate fromDate = from.toLocalDate().minusDays(1);
        LocalDate untilDate = until.toLocalDate().plusDays(1);
        if (Duration.between(untilDate.atStartOfDay(), until).toMinutes() == 0) {
            // Well if the until day is exactly at the start of day, so actually belonging to the next day, we don't need any data for it.
            untilDate = untilDate.minusDays(1);
        }
        MCSchedule unfilteredResult = getSchedule(fromDate, untilDate, true, null);
        return MCEventWithDay.fromSchedule(unfilteredResult, from, until);
    }

    default MCSchedule getSchedule(@Nonnull LocalDate from, @Nonnull LocalDate until) {
        return getSchedule(from, until, true, null);
    }

    default MCSchedule getSchedule(@Nonnull LocalDate from) {
        return getSchedule(from, from);
    }

     MCItems<MCGuide> getGuides(@Nonnull String q);


}
