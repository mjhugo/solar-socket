package com.piragua

import groovy.json.JsonBuilder
import org.springframework.messaging.simp.SimpMessagingTemplate

class PollEgaugeJob {

    static triggers = {
        simple repeatInterval: 1000L
    }

    SimpMessagingTemplate brokerMessagingTemplate
    EgaugeClientService egaugeClientService

    def execute() {
        def data = egaugeClientService.getInstantaneousData()
        
        def builder = new JsonBuilder()
        builder {
            usage(data.usage)
            generation(data.generation)
            timestamp(new Date())
        }

        brokerMessagingTemplate.convertAndSend "/topic/usageAndGeneration", builder
    }


}
