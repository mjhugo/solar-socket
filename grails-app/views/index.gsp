<!doctype html>
<html>
<head>
    <meta name="layout" content="main"/>
    <title>Solar Sockets</title>

    <asset:link rel="icon" href="favicon.ico" type="image/x-ico"/>

    <asset:javascript src="application.js"/>
    <asset:javascript src="jquery"/>
    <asset:javascript src="spring-websocket"/>

    <style>
    body {
        padding-top: 0;
    }

    .generation {
        color: #73c03a;
        font-size: 3em;
    }

    .usage {
        color: #cb513a;
        font-size: 3em;
    }

    #chart {
        margin-bottom: 30px;
    }

    .btn {
        margin-top: 10px;
    }
    </style>
</head>

<body>

<div class="container-fluid">
    <div class="row">

        <div class="col-sm-9 col-md-12 main">
            <h1 class="page-header">Solar Sockets</h1>

            <div class="row">
                <div class="col-xs-12 col-md-7">
                    <div id="chart" style="height:300px;">&nbsp;</div>
                </div>

                <div class="col-xs-12 col-md-5">
                    <div class="row">
                        <div class="col-xs-12 usage"><i class="fa fa-plug"></i> <span id="usage"></span> watts</div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12 generation"><i class="fa fa-sun-o"></i> <span
                                id="generation"></span> watts</div>
                    </div>

                    <div class="row">
                        <div class="col-xs-12">
                            <p>Data for this dashboard is based on a stream of data from the <g:link target="_blank"
                                                                                                     url="${System.properties['egauge.url'] ?: 'http://10.0.1.9'}">Egauge energy monitor</g:link> at the Hugo household in S. Minneapolis.
                            </p>

                            <p>
                                This was built as an experiment to explore the use of WebSockets and Rickshaw/D3.  There is a server side component to the
                                application that polls an XML API endpoint from the Egauge monitor every second and publishes the data as JSON on a websocket, which is then consumed by this page and rendered on the graph.
                            </p>
                            <p>Code can be found on <a href="https://github.com/mjhugo/solar-socket"><i class="fa fa-github"></i> GitHub</a></p>
                        </div>
                    </div>

                </div>

            </div>

            <div class="row">
                <div class="col-xs-12">Last Updated: <span id="timestamp"></span></div>
            </div>

            <div class="row">
                <div class="col-xs-12">
                    <a href="#" id="stop" class="btn btn-danger btn-lg"><i class="fa fa-stop"></i> Stop</a>
                    <a href="#" id="start" class="btn btn-success btn-lg" style="display: none;"><i
                            class="fa fa-play"></i> Start</a>
                </div>
            </div>

        </div>
    </div>
</div>
<script>
    var graph;
    $(function () {
        // based on WS example from https://objectpartners.com/2015/06/10/websockets-in-grails-3-0/
        var socket = new SockJS("${createLink(uri: '/stomp')}");
        var client = Stomp.over(socket);

        var websocketSubscription;

        var subscribe = function subscribe() {
            websocketSubscription = client.subscribe("/topic/usageAndGeneration", function (message) {
                var data = JSON.parse(message.body).content;
                $("#usage").text(numeral(data.usage).format('0,0'));
                $("#generation").text(numeral(data.generation).format('0,0'));
                $("#timestamp").text(new Date(data.timestamp).toLocaleString());

                graph.series.addData({one: data.usage, two: data.generation});
                graph.max = data.usage + (data.usage * .10);
                graph.render();
            });
        };

        $("#start").click(function () {
            $("#start").hide();
            $("#stop").show();
            subscribe();
        });

        $("#stop").click(function () {
            $("#stop").hide();
            $("#start").show();
            websocketSubscription.unsubscribe();
        });

        client.connect({}, subscribe);

        var oneSecond = 1000;

        // based on http://code.shutterstock.com/rickshaw/examples/fixed.html
        graph = new Rickshaw.Graph({
            element: document.getElementById("chart"),
            renderer: 'line',
            series: new Rickshaw.Series.FixedDuration([{name: 'one'}], undefined, {
                timeInterval: oneSecond,
                maxDataPoints: 25,
                timeBase: new Date().getTime() / 1000
            })
        });

        graph.render();

    });

</script>

</body>

</html>
