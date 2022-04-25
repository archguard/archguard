package org.archguard.linter.rule.sql

import mu.KotlinLogging
import net.sf.jsqlparser.parser.CCJSqlParserUtil
import net.sf.jsqlparser.statement.Statement
import net.sf.jsqlparser.util.TablesNamesFinder
import org.archguard.linter.rule.sql.model.SimpleRelation

private val logger = KotlinLogging.logger {}

