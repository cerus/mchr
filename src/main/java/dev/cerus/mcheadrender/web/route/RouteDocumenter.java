package dev.cerus.mcheadrender.web.route;

import dev.cerus.mcheadrender.web.WebServer;
import dev.cerus.mcheadrender.web.route.v1.RoutesV1;

/**
 * I don't like the way this works
 */
public class RouteDocumenter {

    public static void register(final WebServer webServer) {
        webServer.getApp().get("/", context -> context.status(200).html(generateHtml(webServer)));
        webServer.getApp().error(404, ctx -> ctx.redirect("/"));
    }

    private static String generateHtml(final WebServer webServer) {
        final StringBuilder builder = new StringBuilder();
        builder.append("<html><head><title>MCHR Api</title></head><body><h2>MCHR api endpoints</h2><ul>");

        // V1
        builder.append("<li><a>v1</a><ul>");
        for (final Route route : RoutesV1.all(webServer)) {
            builder.append("<li><a href=\"").append(route.apiVersion()).append(route.path()).append("\">").append(route.type().name())
                    .append(" ").append(route.path()).append("</a>").append("<ul>");
            builder.append("<li><a>").append(route.description()).append("</a></li>");
            if (route.params() != null && !route.params().isEmpty()) {
                builder.append("<li><a>").append("Query parameters:").append("</a><ul>");
                for (final QueryParam param : route.params()) {
                    builder.append("<li><a>").append(param.optional() ? "(Optional) " : "").append(param.name()).append("</a><ul>");
                    builder.append("<li><a>").append(param.description()).append("</a></li>");
                    if (param.allowedValues() != null) {
                        builder.append("<li><a>").append("Allowed values:").append("</a><ul>");
                        for (final String allowedValue : param.allowedValues()) {
                            builder.append("<li><a>").append(allowedValue).append("</a></li>");
                        }
                        builder.append("</ul></li>");
                    }
                    if (param.examples() != null) {
                        builder.append("<li><a>").append("Examples:").append("</a><ul>");
                        for (final String example : param.examples()) {
                            builder.append("<li><a>").append(example).append("</a></li>");
                        }
                        builder.append("</ul></li>");
                    }
                    if (param.def() != null) {
                        builder.append("<li><a>").append("Default: ").append(param.def()).append("</a></li>");
                    }
                    builder.append("</ul></li>");
                }
                builder.append("</ul></li>");
            }
            builder.append("</ul></li>");
        }
        builder.append("</ul></li>");

        builder.append("</ul></body></html>");
        return builder.toString();
    }

}
