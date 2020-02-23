<%@ page import="com.jeeplus.common.config.Global" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>实时位置大屏</title>
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
    </style>
</head>

<body>

<div class="wrapper wrapper-content">
    <div id="container" class="map" tabindex="0"></div>
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
<div id="div"></div>

<script src="${ctxStatic}/plugin/awesome/4.4/src/3.2.1/assets/js/jquery-1.7.1.min.js"></script>
<!-- 引入layer插件,当做独立组件使用，不使用layui模块，该版本修复了chrome下花屏的bug -->
<script src="${ctxStatic}/plugin/layui/layer/layer.js"></script>
<script src="${ctxStatic}/plugin/layui/laytpl/laytpl.js"></script>
<!-- 引入toastr -->
<link rel="stylesheet" type="text/css" href="${ctxStatic}/plugin/toastr/toastr.css">
<script type="text/javascript" src="${ctxStatic}/plugin/toastr/toastr.min.js"></script>
<script src="${ctxStatic}/common/js/jeeplus.js"></script>
<script type="text/javascript" src="//webapi.amap.com/maps?v=1.4.11&key=044a68bca642bd52ae17b08c3fa21c88"></script>

<script type="text/javascript">

    //设置快捷键
    $('body').keyup(function () {
        // 先判断焦点是不是在文本框中或者下拉框
        if (document.activeElement.localName != "input" && document.activeElement.localName != "select") {
            var value = event.keyCode;
            //判断按键是不是F11
            if (value == 122) {
                // console.log("按下F11键...");
                toggleFullScreen();
            }
        }
    });


    var div = document.getElementById('div');
    // fixme 此端口需要修改
    var socket = new WebSocket('ws://152.136.144.79:8201/websocket');


    var city = "<%= Global.getConfig("city")%>".trim();


    var map = new AMap.Map("container", {
        resizeEnable: true,
        // center: [116.397428, 39.90923],
        zoom: 12
    });

    map.setCity(city);

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
    var dataStruct = {};

    // 1. 查询gps_realtime表所有数据
    $.get("${ctx}/tp/cartrack/tpCarTrack/getGpsList", function (result) {
        console.log(result);
        dataStruct = result;
        show();
    });

    /**
     * 2. 初始化实时轨迹初段或显示最后坐标
     */
    function show() {
        for (var key in dataStruct) {
            var carInfo = dataStruct[key];
            console.log(key);
            console.log(carInfo);
            if (carInfo['online']) { // 正在行驶车辆，需要绘制轨迹前段
                moveMarker(carInfo.gpsRealtimes);
            } else { // 已经断电车辆，需要绘制定位标记
                addMarker(carInfo, map, carInfo.name);
            }
        }
    }

    /**
     * 2.1 显示车辆行驶轨迹
     */
    function moveMarker(lineArr) {
        if (!lineArr || !lineArr.length) return;
        var marker = new AMap.Marker({
            map: map,
            position: lineArr[lineArr.length - 1],
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
        marker.moveAlong(lineArr, 200);
    }

    /**
     * 2.2 显示熄火停车位置
     */
    function addMarker(d, map, name) {
        // var ratLon = d.location.split(',');
        // // console.log(ratLon);
        // oldMarker.setPosition(new AMap.LngLat(ratLon[0], ratLon[1]));

        // } else {
        var ratLon = d.location.split(',');
        // console.log(ratLon);

        //构建信息窗体中显示的内容
        var info = [];
        info.push("<div><p>公司：" + (d.officeName ? d.officeName : '未知，请先设置') + "</p>");
        info.push("<p>车牌：" + (d.name ? d.name : '新装车辆，请先设置') + "</p>");
        info.push("<p>时间：" + jp.dateFormat(d.updateDate, 'yyyy-MM-dd hh:mm:ss') + "</p></div>");

        var online = d.online;
        var icon = "${ctxStatic}/common/images/car_offline.png";
        if (online) {
            icon = "${ctxStatic}/common/images/car_online.png";
        }
        var marker = new AMap.Marker({
            map: map,
            position: [ratLon[0], ratLon[1]],
            icon: icon,
            offset: new AMap.Pixel(-26, -13),
            label: {
                offset: new AMap.Pixel(-50, -55),//修改label相对于maker的位置
                content: info.join('')
            }
        });

        // 将创建的点标记添加到已有的地图实例：
        map.add(marker);
//                      移除已创建的 marker
//                     map.remove(marker);

    }


    /**
     * 3. 更新轨迹、更新定位标记
     */
    function update() {

    }

    var marker,
        <%--lineArr = eval('${gpsHistories}');--%>
        lineArr = [[116.478935, 39.997761], [116.478939, 39.997825], [116.478912, 39.998549],
            [116.478912, 39.998549], [116.478998, 39.998555], [116.478998, 39.998555], [116.479282, 39.99856],
            [116.479658, 39.998528], [116.480151, 39.998453], [116.480784, 39.998302], [116.480784, 39.998302],
            [116.481149, 39.998184], [116.481573, 39.997997], [116.481863, 39.997846], [116.482072, 39.997718],
            [116.482362, 39.997718], [116.483633, 39.998935], [116.48367, 39.998968], [116.484648, 39.999861]];


    // window.polyline = polyline;

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

    // map.setFitView();

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

    map.setZoom(12);

    map.setFitView();

</script>
</body>
</html>