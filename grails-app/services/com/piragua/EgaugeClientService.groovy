package com.piragua

import com.pirgaua.InstantaneousData

class EgaugeClientService {

    def InstantaneousData getInstantaneousData() {
        XmlSlurper slurper = new XmlSlurper()
        String serverBaseUrl = System.properties['egauge.url'] ?: 'http://10.0.1.9'
        return buildInstantaneousData(slurper.parseText(new URL("${serverBaseUrl}/cgi-bin/egauge?v1&inst").text))
    }

    public InstantaneousData buildInstantaneousData(def xml) {
        def usage = Long.valueOf(xml.r.find { it.@n.text() == 'Grid' }.i.text())
        def generation = Long.valueOf(xml.r.find { it.@n.text() == 'Solar' }.i.text())
        return new InstantaneousData(
                usage: usage > 0 ? usage : 0,
                generation: generation > 0 ? generation : 0,
        )
    }
}
