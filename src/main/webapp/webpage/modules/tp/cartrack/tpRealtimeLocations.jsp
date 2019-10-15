<%@ page import="com.jeeplus.common.config.Global" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>实时位置大屏</title>
    <meta name="decorator" content="ani"/>
    <!-- SUMMERNOTE -->
    <%@include file="/webpage/include/summernote.jsp" %>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>

    <style type="text/css">
        .map {
            height: 100%;
            width: 100%;
            float: left;
        }

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

        .amap-zoomcontrol {
            display: none !important;
        }
    </style>
</head>
<body>

<div id="container" class="map" tabindex="0"></div>

<script type="text/javascript" src="//webapi.amap.com/maps?v=1.4.11&key=044a68bca642bd52ae17b08c3fa21c88"></script>

<script type="text/javascript">
    jp.info('全屏，请点击地图，按F11键');

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

    var city = "<%= Global.getConfig("city")%>".trim();


    var map = new AMap.Map("container", {
        resizeEnable: true,
        // center: [116.397428, 39.90923],
        zoom: 12
    });

    map.setCity(city);


    var carList = {};
    setInterval(function () {

        jp.get("${ctx}/tp/car/tpCar/getRealtimeLocations", function (result) {
            // console.log(data);
            var data = result.sort(function () {
                return Math.random() - 0.5
            });
            for (var i = 0; i < data.length; i++) {
                var d = data[i];
                var name = d.name;
                // console.log(name)
                var oldMarker = carList[name];
                if (oldMarker) {
                    map.remove(oldMarker);
                }
                addMarker(d, map, name);
            }
        });

    }, 5000);

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
        // marker.setMap(map);
//                      移除已创建的 marker
//                     map.remove(marker);

        carList[name] = marker;
    }

    map.setZoom(12);

    map.setFitView();
</script>
</body>
</html>