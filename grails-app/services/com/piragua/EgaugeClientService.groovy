package com.piragua

import com.pirgaua.InstantaneousData

class EgaugeClientService {

    def InstantaneousData getInstantaneousData() {
        XmlSlurper slurper = new XmlSlurper()
        String serverBaseUrl = System.properties['egauge.url'] ?: 'http://10.0.1.9'
        return buildInstantaneousData(slurper.parseText(new URL("${serverBaseUrl}/cgi-bin/egauge?v1&inst").text))
    }

    public InstantaneousData buildInstantaneousData(def xml) {
        def gridIncomingOrOutgoing = Long.valueOf(xml.r.find { it.@n.text() == 'Grid' }.i.text())
        def generationRawValue = Long.valueOf(xml.r.find { it.@n.text() == 'Solar' }.i.text())
        def generation = generationRawValue > 0 ? generationRawValue : 0

        def usage = gridIncomingOrOutgoing + generation

        return new InstantaneousData(
                usage: usage,
                generation: generation
        )
    }
}
