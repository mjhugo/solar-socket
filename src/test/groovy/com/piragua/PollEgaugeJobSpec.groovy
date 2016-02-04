package com.piragua

import com.pirgaua.InstantaneousData
import grails.converters.JSON
import org.springframework.messaging.simp.SimpMessagingTemplate
import spock.lang.Specification

class PollEgaugeJobSpec extends Specification {

    def "execute method polls for data and publishes response"() {
        setup:
        def expectedData = new InstantaneousData(usage: 1, generation: 100)
        PollEgaugeJob job = new PollEgaugeJob()

        def egaugeClientService = Mock(EgaugeClientService)
        job.egaugeClientService = egaugeClientService

        def simpMessagingTemplate = Mock(SimpMessagingTemplate)
        job.brokerMessagingTemplate = simpMessagingTemplate

        when:
        job.execute()

        then:
        1 * egaugeClientService.getInstantaneousData() >> expectedData
        1 * simpMessagingTemplate.convertAndSend("/topic/usageAndGeneration", { jsonBuilder ->
            JSON.parse(jsonBuilder.toString()).usage == expectedData.usage
            JSON.parse(jsonBuilder.toString()).generation == expectedData.generation
        })
    }
}
