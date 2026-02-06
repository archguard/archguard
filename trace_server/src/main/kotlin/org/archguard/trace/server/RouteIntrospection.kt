package org.archguard.trace.server

import io.ktor.server.routing.*
import io.ktor.http.HttpMethod

/**
 * Data class representing a registered HTTP route
 */
data class RegisteredRoute(
    val method: String,
    val path: String,
    val description: String = ""
)

/**
 * Extract all registered routes from a Ktor Routing object
 */
fun Routing.getAllRoutes(): List<RegisteredRoute> {
    val routes = mutableListOf<RegisteredRoute>()
    collectRoutes(this, "", routes)
    return routes.sortedWith(compareBy({ it.path }, { it.method }))
}

/**
 * Recursively collect routes from the routing tree
 */
private fun collectRoutes(route: Route, parentPath: String, routes: MutableList<RegisteredRoute>) {
    val currentPath = buildPath(route, parentPath)
    
    // Check if this route has an HTTP method selector
    val httpMethod = route.selector as? HttpMethodRouteSelector
    if (httpMethod != null) {
        routes.add(RegisteredRoute(
            method = httpMethod.method.value,
            path = currentPath
        ))
    }
    
    // Recursively process child routes
    route.children.forEach { child ->
        collectRoutes(child, currentPath, routes)
    }
}

/**
 * Build the full path for a route by combining parent path and current selector
 */
private fun buildPath(route: Route, parentPath: String): String {
    val selector = route.selector
    
    return when (selector) {
        is PathSegmentConstantRouteSelector -> {
            "$parentPath/${selector.value}"
        }
        is PathSegmentParameterRouteSelector -> {
            "$parentPath/{${selector.name}}"
        }
        is PathSegmentOptionalParameterRouteSelector -> {
            "$parentPath/{${selector.name}?}"
        }
        is PathSegmentWildcardRouteSelector -> {
            "$parentPath/*"
        }
        is PathSegmentTailcardRouteSelector -> {
            "$parentPath/{...}"
        }
        is HttpMethodRouteSelector -> {
            // HTTP method selector doesn't add to path
            parentPath
        }
        is RootRouteSelector -> {
            // Root selector
            ""
        }
        else -> {
            // For other selectors, keep the parent path
            parentPath
        }
    }
}

/**
 * Format routes for logging output
 */
fun List<RegisteredRoute>.formatForLogging(): List<String> {
    // Group routes by path for better readability
    val grouped = this.groupBy { it.path }
    
    return grouped.flatMap { (path, routesForPath) ->
        routesForPath.map { route ->
            val methodPadded = route.method.padEnd(6)
            "  $methodPadded $path"
        }
    }
}

