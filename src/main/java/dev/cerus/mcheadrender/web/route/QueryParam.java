package dev.cerus.mcheadrender.web.route;

import java.util.List;

/**
 * Represents a query parameter
 *
 * @param name          The parameter name
 * @param description   The parameter description
 * @param type          The type of the parameter
 * @param optional      If it's optional or not
 * @param allowedValues A collection of allowed values
 * @param examples      A collection of examples
 * @param def           A default value
 */
public record QueryParam(String name, String description, Class<?> type, boolean optional, List<String> allowedValues, List<String> examples,
                         String def) {

}
