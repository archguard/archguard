package com.thoughtworks.archguard.v2.backyard.persistent.infrastructure.mongo

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration

@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {
    @field:Value("\${mongo.username}")
    var username: String = ""

    @field:Value("\${mongo.password}")
    var password: String = ""

    override fun getDatabaseName(): String {
        return "archguard"
    }

    override fun configureClientSettings(builder: MongoClientSettings.Builder) {
        builder.credential(MongoCredential.createCredential(username, "archguard", password.toCharArray()))
            .applyToClusterSettings {
                // TODO move server address to properties as well
                it.hosts(listOf(ServerAddress("localhost", 27017)))
            }
    }
}