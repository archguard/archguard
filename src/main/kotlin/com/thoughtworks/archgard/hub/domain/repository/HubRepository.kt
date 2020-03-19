package com.thoughtworks.archgard.hub.domain.repository

import com.thoughtworks.archgard.hub.domain.model.ProjectInfo

interface HubRepository {

    fun getProjectInfo(): ProjectInfo
}