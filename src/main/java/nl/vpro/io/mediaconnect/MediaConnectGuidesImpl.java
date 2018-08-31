package nl.vpro.io.mediaconnect;

import java.time.LocalDate;

import com.google.api.client.http.GenericUrl;

import nl.vpro.io.mediaconnect.domain.MCGuide;
import nl.vpro.io.mediaconnect.domain.MCItems;
import nl.vpro.io.mediaconnect.domain.MCSchedule;

import static nl.vpro.io.mediaconnect.Fields.ASSETS;
import static nl.vpro.io.mediaconnect.Fields.COVER;
import static nl.vpro.io.mediaconnect.Fields.SOURCEFILE_FIELD;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public class MediaConnectGuidesImpl implements MediaConnectGuides {

    private  final MediaConnectRepositoryImpl impl;


    private final Fields SCHEDULE_FIELDS = Fields.builder()
        .field(Field.builder("timelines")
            .field(ASSETS)
            .build())
        .f("guide")
        .field(Field.builder("show")
            .fs("slug", "name", "body", "tags", "status")
            .field(COVER)
            .build()
        )
        .f("users")
        .build();



    public MediaConnectGuidesImpl(MediaConnectRepositoryImpl impl) {
        this.impl = impl;
    }

    @Override
    public MCSchedule getSchedule(LocalDate from, LocalDate until) {
        if (impl.getGuideId() == null) {
            throw new IllegalStateException("No guide id defined for " + impl);
        }
        GenericUrl url = impl.createUrl("guides", impl.getGuideId());
        //GenericUrl url = impl.createUrl("prepr", "schedules", channel,  "guide");
        if (from != null) {
            url.set("from", from.toString());
        }
        if (until != null) {
            url.set("until", until.toString());
        }
        //uri.addParameter("environment_id", "45ed5691-8bc1-4018-9d67-242150cff944");
        url.set("fields", SCHEDULE_FIELDS);

        return impl.get(url, MCSchedule.class);
    }

    @Override
    public MCItems<MCGuide> getGuides(String q) {
         GenericUrl url = impl.createUrl("guides");
        if (q!= null) {
            url.set("q", q);
        }
        url.set("fields", "timelines,guide,show{slug,name,body,tags,status,cover{" + SOURCEFILE_FIELD + "}},users");

        return impl.get(url, MCItems.class);


    }


}
