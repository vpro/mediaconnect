package nl.vpro.io.prepr.domain;

import lombok.*;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@JsonTypeName(PreprTagGroup.LABEL)
public class PreprTagGroup extends PreprAbstractObject {

    public static final String LABEL = "TagGroup";

    boolean visible;

    String name;

    List<String> visible_in;

    String type;

    List<PreprTag> tags;
}
