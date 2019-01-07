<%@ page import="com.jeeplus.common.config.Global" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>选择地点</title>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>
    <style type="text/css">
        html, body, #container {
            height: 100%;
            width: 100%;
        }

        .input-card .btn {
            margin-right: 1.2rem;
            width: 9rem;
        }

        .input-card .btn:last-child {
            margin-right: 0;
        }

        .amap-logo, .amap-copyright {
            display: none !important;
        }

        #div {
            color: red;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div id="container"></div>
    <div class="input-card">
        <h4>轨迹回放控制</h4>
        <div class="input-item">
            <input type="button" class="btn" value="开始动画" id="start" onclick="startAnimation()"/>
            <input type="button" class="btn" value="暂停动画" id="pause" onclick="pauseAnimation()"/>
        </div>
        <div class="input-item">
            <input type="button" class="btn" value="继续动画" id="resume" onclick="resumeAnimation()"/>
            <input type="button" class="btn" value="停止动画" id="stop" onclick="stopAnimation()"/>
        </div>
    </div>
</div>
<script type="text/javascript" src="//webapi.amap.com/maps?v=1.4.11&key=044a68bca642bd52ae17b08c3fa21c88"></script>

<body>
<h1>WEBSOCKET----TEST</h1>
<div id="div">

</div>
<script type="text/javascript">
    var div = document.getElementById('div');
    var socket = new WebSocket('ws://localhost:8080/websocket');


    var map = new AMap.Map("container", {
        resizeEnable: true,
        // center: [116.397428, 39.90923],
        zoom: 17
    });

    socket.onopen = function (event) {
        console.log(event);
        socket.send('websocket client connect test');
    }

    socket.onclose = function (event) {
        console.log(event);
    }

    socket.onerror = function (event) {
        console.log(event);
    }

    socket.onmessage = function (event) {
        console.log(event)
        div.innerHTML += (' @_@ ' + event.data + ' ~_~ ');
    }

    // 数据结构
    /*
    {
        deviceId:[] // lineArr
    }
     */

    // 1. 查询gps_realtime表所有数据
    // 2. 查询
    var dataStruct= {

    };

    var marker,
        <%--lineArr = eval('${gpsHistories}');--%>
    lineArr = [[116.478935, 39.997761], [116.478939, 39.997825], [116.478912, 39.998549],
        [116.478912, 39.998549], [116.478998, 39.998555], [116.478998, 39.998555], [116.479282, 39.99856],
        [116.479658, 39.998528], [116.480151, 39.998453], [116.480784, 39.998302], [116.480784, 39.998302],
        [116.481149, 39.998184], [116.481573, 39.997997], [116.481863, 39.997846], [116.482072, 39.997718],
        [116.482362, 39.997718], [116.483633, 39.998935], [116.48367, 39.998968], [116.484648, 39.999861]];


    marker = new AMap.Marker({
        map: map,
        position: lineArr[0],
        icon: "https://webapi.amap.com/images/car.png",
        offset: new AMap.Pixel(-26, -13),
        autoRotation: true,
        angle: -90,
    });

    // 绘制轨迹
    var polyline = new AMap.Polyline({
        map: map,
        path: lineArr,
        showDir: true,
        strokeColor: "#28F",  //线颜色
        // strokeOpacity: 1,     //线透明度
        strokeWeight: 6,      //线宽
        // strokeStyle: "solid"  //线样式
    });

    window.polyline = polyline;

    // var passedPolyline = new AMap.Polyline({
    //     map: map,
    //     path: lineArr,
    //     strokeColor: "#AF5",  //线颜色
    //     // strokeOpacity: 1,     //线透明度
    //     strokeWeight: 6,      //线宽
    //     // strokeStyle: "solid"  //线样式
    // });
    //
    //
    // marker.on('moving', function (e) {
    //     passedPolyline.setPath(e.passedPath);
    // });

    map.setFitView();

    function startAnimation() {
        marker.moveAlong(lineArr, 200);
    }

    function pauseAnimation() {
        marker.pauseMove();
    }

    function resumeAnimation() {
        marker.resumeMove();
    }

    function stopAnimation() {
        marker.stopMove();
    }


</script>
</body>
</html>