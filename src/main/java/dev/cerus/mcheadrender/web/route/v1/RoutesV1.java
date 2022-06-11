package dev.cerus.mcheadrender.web.route.v1;

import dev.cerus.mcheadrender.web.WebServer;
import dev.cerus.mcheadrender.web.route.Route;
import io.javalin.Javalin;
import java.util.List;

/**
 * A collection of v1 routes. I don't like this implementation, needs a rework
 */
public class RoutesV1 {

    public static void register(final WebServer webServer) {
        register(webServer.getApp(), new InfoRouteV1(webServer));
        register(webServer.getApp(), new RenderRouteV1(webServer));
        register(webServer.getApp(), new SkinsRouteV1(webServer));
    }

    private static void register(final Javalin app, final Route route) {
        app.addHandler(route.type(), "/" + route.apiVersion() + route.path(), route);
    }

    public static List<Route> all(final WebServer webServer) {
        return List.of(
                new InfoRouteV1(webServer),
                new RenderRouteV1(webServer),
                new SkinsRouteV1(webServer)
        );
    }

}
