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
        .amap-logo,.amap-copyright{
            display: none!important;
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
    var city = "<%= Global.getConfig("city")%>".trim();


    // var map = new AMap.Map('container', {
    //     mapStyle: 'amap://styles/light', //设置地图的显示样式
    //     /*
    //     bg 区域面
    //     point 兴趣点
    //     road 道路及道路标注
    //     building 建筑物
    //      */
    //     features: [
    //         'bg',
    //         'road',
    //         'point',
    //         'building'
    //     ],// 多个种类要素显示
    //     // resizeEnable: true,
    //     zoom: 17,
    //     scrollWheel: true,
    //     city: city
    // });

    var map = new AMap.Map("container", {
        resizeEnable: true,
        // center: [116.397428, 39.90923],
        zoom: 17
    });

    map.setCity(city);

    var carList = {};
    setInterval(function () {

        jp.get("${ctx}/tp/car/tpCar/getRealtimeLocations", function (data) {
            console.log(data);
            for (var i = 0; i < data.length; i++) {
                var d = data[i];
                var name = d.name;
                var oldMarker = carList[name];
                if (oldMarker) {
                    var ratLon = d.location.split(',');
                    console.log(ratLon);
                    oldMarker.setPosition(new AMap.LngLat(ratLon[0], ratLon[1]));
                } else {
                    var ratLon = d.location.split(',');
                    console.log(ratLon);
                    var marker = new AMap.Marker({
                        map: map,
                        position:  [ratLon[0], ratLon[1]],
                        icon: "https://webapi.amap.com/images/car.png",
                        offset: new AMap.Pixel(-26, -13)
                    });
                    // 将创建的点标记添加到已有的地图实例：
                    // map.add(marker);
                    // marker.setMap(map);
//                      移除已创建的 marker
//                     map.remove(marker);
                    carList[name] = marker;
                }
            }
        });

    }, 3000);


    map.setFitView();
</script>
</body>
</html>