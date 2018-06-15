package nl.vpro.io.mediaconnect.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * @author Michiel Meeuwissen
 * @since 0.1
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonTypeName("Post")
public class MCPost extends MCContent {

    String slug;

    String length;

    String tease;
}
