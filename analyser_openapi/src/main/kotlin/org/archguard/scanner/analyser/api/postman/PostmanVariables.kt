package org.archguard.scanner.analyser.api.postman

import java.util.*
import java.util.regex.Pattern

class PostmanVariables(val env: PostmanEnvironment) {
    private val r = Random(1000)

    private fun getConstantVal(exp: String): String {
        return if (exp.equals(GUID, ignoreCase = true)) {
            UUID.randomUUID().toString()
        } else if (exp.equals(TIMESTAMP, ignoreCase = true)) {
            java.lang.Long.toString(System.currentTimeMillis() / 1000)
        } else if (exp.equals(RANDOMINT, ignoreCase = true)) {
            Integer.toString(r.nextInt(1000))
        } else {
            throw IllegalArgumentException("Invalid POSTMAN dynamic variable $exp")
        }
    }

    private fun getVal(name: String): String? {
        if (name.startsWith("{{$")) {
            try {
                return getConstantVal(name)
            } catch (e: IllegalArgumentException) {
                // ignore
            }
        }
        val key = name.substring(2, name.length - 2).trim { it <= ' ' }
        val `val` = env.lookup[key]
            ?: // throw new IllegalArgumentException("Invalid dynamic variable: " + name);
            return "UNDEFINED"
        return `val`.value
    }

    /**
     * Replace all {{dynamic variable}} in orig string with values found in the
     * environment. If variable is not found, replace it with constant string
     * "UNDEFINED".
     *
     * @param orig - the original value
     * @return The new string with all dynamic variables replaced
     */
    fun replace(orig: String?): String? {
        if (orig.isNullOrEmpty()) return orig
        // Get all the dynamic variables
        val allMatches: MutableList<String> = ArrayList()
        val m = Pattern.compile(POSTMAN_EXP).matcher(orig)
        while (m.find()) {
            allMatches.add(m.group().trim { it <= ' ' })
        }

        // TODO: this is not the most efficient way to do it
        // but it is the simplest in term of code and this is not
        // production code anyway.
        var result: String = orig
        for (`var` in allMatches) {
            val varVal = getVal(`var`)
            // System.out.println(var + " ==> " + varVal);
//            result = result.replace(`var` as CharSequence, varVal as CharSequence?)
            result = result.replace(`var`, varVal!!)
        }
        return result
    }

    companion object {
        const val POSTMAN_EXP = "\\{\\{[^\\}]+\\}\\}"
        const val GUID = "{{\$guid}}"
        const val TIMESTAMP = "{{\$timestamp}}"
        const val RANDOMINT = "{{\$randomInt}}"
    }
}
