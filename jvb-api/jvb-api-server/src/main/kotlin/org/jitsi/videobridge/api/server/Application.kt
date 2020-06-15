/*
 * Copyright @ 2018 - present 8x8, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jitsi.videobridge.api.server

import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.websocket.WebSockets
import org.jitsi.videobridge.api.types.ApiVersion
import org.jitsi.videobridge.api.types.SupportedApiVersions
import org.jitsi.videobridge.api.server.v1.app as v1App
import org.jitsi.videobridge.api.types.v1.ConferenceManager as v1ConferenceManager

/**
 * The top level JVB API application.  It's responsible for inserting all
 * the correct versions of the application at the correct URLs and injecting
 * the [v1ConferenceManager] instance for calls.
 */
fun Application.module(conferenceManager: v1ConferenceManager) {
    install(ContentNegotiation) {
        jackson {}
    }
    install(WebSockets)
    routing {
//        trace { println(it.buildText()) }
        route("/v1") {
            v1App(conferenceManager)
        }
        get("/about/api_version") {
            call.respond(SUPPORTED_API_VERSIONS)
        }
    }
}

/**
 * What versions of the API are currently supported by jvb-api-server.  Whenever
 * support for a new version is added, the [ApiVersion] must be added to
 * this value.  If support for an older version is removed, it must be removed
 * from this value.
 */
@JvmField
val SUPPORTED_API_VERSIONS = SupportedApiVersions(ApiVersion.V1)
