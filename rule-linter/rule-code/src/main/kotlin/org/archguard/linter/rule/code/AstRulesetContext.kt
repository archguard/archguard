package org.archguard.linter.rule.code

/**
 * Represents a path in the AST (Abstract Syntax Tree).
 *
 * An `AstPath` object contains information about a specific path in the AST, including the path item and the operation object.
 * The path item represents the path of the API endpoint, while the operation object represents the HTTP operation associated with the endpoint.
 *
 * @property pathItem The path of the API endpoint.
 * @property operationObject The HTTP operation associated with the endpoint.
 */
class AstPath(
    // like
    val pathItem: String,
    // http operation sample: ^(get|put|post|delete|options|head|patch|trace)$
    val operationObject: String,
) {
    fun resolveAlias() {

    }
}

class AstRulesetContext(
    path: AstPath
) {

}