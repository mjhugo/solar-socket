Websocket Example
--------------------

A sample application to demonstrate the use of WebSockets to render data in a Rickshaw Graph.

Includes a server side component to poll an Egauge Monitor XML endpoint and then stream that data via WebSocket to an HTML client.

The interesting bits are:

- Client: [index.gsp](https://github.com/mjhugo/solar-socket/blob/master/grails-app/views/index.gsp#L95)
- Server: [EgaugeClientService](https://github.com/mjhugo/solar-socket/blob/master/grails-app/services/com/piragua/EgaugeClientService.groovy) and [PollEgaugeJob](https://github.com/mjhugo/solar-socket/blob/master/grails-app/jobs/com/piragua/PollEgaugeJob.groovy)

And of course, there are [tests](https://github.com/mjhugo/solar-socket/tree/master/src/test/groovy/com/piragua) for the server side components as well. 

Hat tip to:
- http://code.shutterstock.com/rickshaw/examples/
- https://objectpartners.com/2015/06/10/websockets-in-grails-3-0/
- http://getbootstrap.com/examples/dashboard/
- https://fortawesome.github.io/Font-Awesome/
- http://numeraljs.com/