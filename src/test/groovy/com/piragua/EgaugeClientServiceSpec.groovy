package com.piragua

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(EgaugeClientService)
class EgaugeClientServiceSpec extends Specification {

    void "test parsing of XML response"() {
        setup:
        def xml = new XmlSlurper().parseText(exampleResponse)

        when:
        def data = service.buildInstantaneousData(xml)

        then:
        data.generation == 401
        data.usage == 2477
    }

    void "return min value of 0"() {
        setup:
        def xml = new XmlSlurper().parseText(exampleResponseWithNegativeNumbers)

        when:
        def data = service.buildInstantaneousData(xml)

        then:
        data.generation == 0
        data.usage == 1136
    }

    void "when solar producing more than grid, calculate proper values"() {
        setup:
        def xml = new XmlSlurper().parseText(exampleResponseSolarGreaterThanGrid)

        when:
        def data = service.buildInstantaneousData(xml)

        then:
        data.generation == 1788
        data.usage == 1407
    }

    def exampleResponse = '''<?xml version="1.0" encoding="UTF-8" ?>
<data serial="0x7d70779e">
 <ts>1454554834</ts>
 <r t="P" n="Grid"><v>13854521935</v><i>2076</i></r>
 <r t="P" n="Solar"><v>4642705319</v><i>401</i></r>
 <r t="P" n="Solar+"><v>4703990316</v><i>0</i></r>
 <r t="P" n="CT1"><v>4597371876</v><i>295</i></r>
 <r t="P" n="CT2"><v>9257150059</v><i>841</i></r>
</data>
'''

    def exampleResponseWithNegativeNumbers = '''<?xml version="1.0" encoding="UTF-8" ?>
<data serial="0x7d70779e">
 <ts>1454554834</ts>
 <r t="P" n="Grid"><v>13854521935</v><i>1136</i></r>
 <r t="P" n="Solar"><v>4642705319</v><i>-9</i></r>
 <r t="P" n="Solar+"><v>4703990316</v><i>0</i></r>
 <r t="P" n="CT1"><v>4597371876</v><i>295</i></r>
 <r t="P" n="CT2"><v>9257150059</v><i>841</i></r>
</data>
'''

    def exampleResponseSolarGreaterThanGrid = '''<?xml version="1.0" encoding="UTF-8" ?>
<data serial="0x7d70779e">
 <ts>1454867453</ts>
 <r t="P" n="Grid"><v>13854521935</v><i>-381</i></r>
 <r t="P" n="Solar"><v>4642705319</v><i>1788</i></r>
 <r t="P" n="Solar+"><v>4703990316</v><i>1788</i></r>
 <r t="P" n="CT1"><v>4597371876</v><i>494</i></r>
 <r t="P" n="CT2"><v>9257150059</v><i>113</i></r>
</data>
'''

}
