package org.archguard.scanner.analyser.layered

import org.archguard.architecture.base.*

/**
 * The `DddLayeredChecker` class is an implementation of the `LayeredChecker` interface in the Kotlin language.
 * It is used to check if a given string belongs to the different layers of a Domain-Driven Design (DDD) architecture.
 *
 * The class has four private Boolean properties: `hasDomain`, `hasInfra`, `hasInterfaces`, and `hasApp`.
 * These properties are initially set to `false`.
 *
 * The class implements two methods from the `LayeredChecker` interface: `addToIdentify` and `canMarked`.
 *
 * The `addToIdentify` method takes a string as a parameter and splits it using the dot (.) separator.
 * It then checks if the last element of the split string is present in the predefined lists: `DOMAIN`, `CONTROLLERS`,
 * `INFRA`, and `APPLICATION`. If the corresponding property is `false`, it updates the property to `true`.
 *
 * The `canMarked` method checks if all four properties (`hasDomain`, `hasInfra`, `hasInterfaces`, and `hasApp`) are `true`.
 * If all properties are `true`, it returns `true`; otherwise, it returns `false`.
 *
 * Example usage:
 * ```
 * val checker = DddLayeredChecker()
 * checker.addToIdentify("com.example.domain.User")
 * checker.addToIdentify("com.example.infra.UserRepository")
 * checker.addToIdentify("com.example.controllers.UserController")
 * checker.addToIdentify("com.example.application.UserService")
 *
 * val canMarked = checker.canMarked() // true
 * ```
 */
class DddLayeredChecker() : LayeredChecker {
    private var hasDomain: Boolean = false
    private var hasInfra: Boolean = false
    private var hasInterfaces: Boolean = false
    private var hasApp: Boolean = false

    override fun addToIdentify(str: String) {
        val split = str.split(".")
        if (!this.hasDomain) {
            this.hasDomain = DOMAIN.contains(split.last())
        }

        if (!this.hasInterfaces) {
            this.hasInterfaces = CONTROLLERS.contains(split.last())
        }

        if (!this.hasInfra) {
            this.hasInfra = INFRA.contains(split.last())
        }

        if (!this.hasApp) {
            this.hasApp = APPLICATION.contains(split.last())
        }
    }

    override fun canMarked(): Boolean {
        return this.hasInterfaces && this.hasDomain && this.hasApp && this.hasInfra
    }
}