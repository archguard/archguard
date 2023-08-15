package org.archguard.scanner.analyser.api.openapi.render

import org.archguard.scanner.analyser.api.openapi.OpenApiV3Processor
import org.archguard.scanner.analyser.api.render.SimpleApiRender
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class SimpleApiRenderTest {
    @Test
    fun should_render_api_to_string() {
        val file2 = File("src/test/resources/testsets/swagger-3.yaml")
        val apiDetails = OpenApiV3Processor(OpenApiV3Processor.fromFile(file2)!!, file2).convertApi()

        val render = SimpleApiRender()
        val result = render.render(apiDetails)

        val expected =
            """
Cashback campaign
GET /cashback-campaigns/{campaignId}  getCashbackCampaign(X-App-Token: string, campaignId: string) : [200: {campaignId: string, campaignName: string, startDate: string, endDate: string, status: string, remainingAmountInCents: integer}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 404: {errors: array}, 500: {errors: array}] 

Cashback
GET /cashback-campaigns/{campaignId}/cashbacks  getCashbackList(X-App-Token: string, campaignId: string, pageNumber: integer, pageSize: integer, fromDateTime: string, toDateTime: string, status: string) : [200: {cashbacks: array, totalElementCount: integer}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 404: {errors: array}, 500: {errors: array}] 
POST /cashback-campaigns/{campaignId}/cashbacks  createCashback(X-App-Token: string, campaignId: string) : [201: {cashbackId: string, url: string, amountInCents: integer, createdDateTime: string, expiryDateTime: string, redeemedDateTime: string, status: string, referenceId: string, locationId: string, locationAddress: string}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 404: {errors: array}, 500: {errors: array}] 
GET /cashback-campaigns/{campaignId}/cashbacks/{cashbackId}  getCashback(X-App-Token: string, campaignId: string, cashbackId: string) : [200: {cashbackId: string, url: string, amountInCents: integer, createdDateTime: string, expiryDateTime: string, redeemedDateTime: string, status: string, referenceId: string, locationId: string, locationAddress: string}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 404: {errors: array}, 500: {errors: array}] 

Cashback notification
POST /cashback-subscriptions  subscribeCashbackNotifications(X-App-Token: string) : [201: {subscriptionId: string}, 400: {errors: array}, 401: {errors: array}, 403: {errors: array}, 500: {errors: array}] 
DELETE /cashback-subscriptions  deleteCashbackNotifications(X-App-Token: string) : [204: {}, 401: {errors: array}, 403: {errors: array}, 500: {errors: array}] 
""".trimIndent()

        assertEquals(expected, result)
    }
}
