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
@JsonTypeName(PreprTag.LABEL)
public class PreprTag extends PreprAbstractObject {

    public static final String LABEL = "Tag";

    String name;

    String slug;

    String color;

    List<PreprTagType> tag_types;

    List<PreprTagGroup> tag_groups;
}
