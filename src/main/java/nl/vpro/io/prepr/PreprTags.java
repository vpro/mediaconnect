package nl.vpro.io.prepr;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import nl.vpro.io.prepr.domain.MCItems;
import nl.vpro.io.prepr.domain.MCTag;
import nl.vpro.io.prepr.domain.MCTagGroup;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
public interface PreprTags {


    MCItems<MCTagGroup> getGroups(Paging paging, String q);

    default MCItems<MCTagGroup> getGroups(Paging paging) {
        return getGroups(paging, null);
    }

    default Optional<List<MCTag>> getTagsInGroup(String name) {
        return getGroups(Paging.one(), name).getItems().stream().map(MCTagGroup::getTags).findFirst();
    }

    MCItems<MCTag> getTags(UUID tagGroup);

    MCTag getTag(UUID tag);




}