package nl.vpro.io.prepr;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.slf4j.LoggerFactory;
import com.google.common.collect.Range;

import nl.vpro.io.prepr.domain.MCGuide;
import nl.vpro.io.prepr.domain.MCItems;
import nl.vpro.io.prepr.domain.MCSchedule;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprGuides {


    MCSchedule getSchedule(LocalDate from, LocalDate until, boolean exceptions, UUID showId);

    default MCSchedule getSchedule(@Nonnull  LocalDateTime from, @Nonnull  LocalDateTime until) {
        Range<LocalDateTime> range = Range.closedOpen(from, until);
        LocalDate fromDate = from.toLocalDate();
        LocalDate untilDate = until.toLocalDate();
        if (Duration.between(untilDate.atStartOfDay(), until).toMinutes() == 0) {
            untilDate = untilDate.minusDays(1);
        }
        MCSchedule result = getSchedule(fromDate, untilDate, true, null);
        result.forEach((e) -> {
            e.setValue(new ArrayList<>(e.getValue()));
            e.getValue().removeIf(event -> {
                Range<LocalDateTime> erange = event.getRange(e.getKey());
                boolean startInRange = range.contains(erange.lowerEndpoint());
                if (! startInRange) {
                    LoggerFactory.getLogger(PreprGuides.class).info("{} not in {}: Removing {})", erange.lowerEndpoint(), range, event);
                }
                return ! startInRange;
            });
        });
        return result;
    }

    default MCSchedule getSchedule(LocalDate from, LocalDate until) {
        return getSchedule(from, until, true, null);
    }

    default MCSchedule getSchedule(LocalDate from) {
        return getSchedule(from, from);
    }

     MCItems<MCGuide> getGuides(String q);



}