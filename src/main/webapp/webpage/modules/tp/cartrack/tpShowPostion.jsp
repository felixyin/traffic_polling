<%@ page import="com.jeeplus.common.config.Global" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>查看地点</title>
    <meta name="decorator" content="ani"/>
    <!-- SUMMERNOTE -->
    <%@include file="/webpage/include/summernote.jsp" %>
    <%--<link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>--%>

    <style type="text/css">
        html,
        body {
            margin: 0;
            padding: 0;
            overflow: hidden;
            font-size: 13px;
        }

        .wrapper, .my-row, .my-panel-body {
            height: 99%;
        }

        * {
            overflow: hidden;
        }

        .map {
            height: 90%;
            min-height: 420px;
            width: 100%;
            float: left;
        }

        .title {
            width: 100%;
            background-color: #dadada
        }

        button {
            border: solid 1px;
            margin-left: 15px;
            background-color: #dadafa;
        }

        .c {
            font-weight: 600;
            padding-left: 15px;
            padding-top: 4px;
        }

        #lnglat,
        #address,
        #nearestJunction,
        #nearestRoad,
        #nearestPOI,
        .title {
            padding-left: 15px;
        }

        .my-row {
            display: flex;
            flex-direction: row;
            flex-wrap: nowrap;
            justify-content: space-around;
            align-items: stretch;
        }

        .my-left {
            flex: 9;
            padding-right: 10px;
        }

        .my-right {
            flex: 6;
        }

        .amap-zoomcontrol {
            display: none !important;
        }

        .amap-markers{
            overflow: visible!important;
            /*z-index:11111;*/
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="my-row">
        <div class="my-left">
            <div class="panel panel-primary" style="height: 100%;">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        地图定位
                    </h3>
                </div>
                <div class="panel-body"
                     style="padding: 0!important;height: 90%;overflow: hidden!important;">
                    <div id="container" class="map" style="height: 80%" tabindex="0"></div>
                </div>
            </div>
        </div>
        <div class="my-right">
            <div class="panel panel-primary" style="height: 100%">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        位置信息
                    </h3>
                </div>
                <div id="my-detail" class="panel-body" style="overflow-y: scroll!important;height: 100%">
                    <div class='c'>经纬度:</div>
                    <div id='lnglat'></div>
                    <div class='c'>地址:</div>
                    <div id='address'></div>
                    <div class='c'>最近的路口:</div>
                    <div id='nearestJunction'></div>
                    <div class='c'>最近的道路:</div>
                    <div id='nearestRoad'></div>
                    <%--<div class='c'>最近的POI:</div>--%>
                    <%--<div id='nearestPOI'></div>--%>
                </div>
            </div>

        </div>
    </div>
</div>
<script type="text/javascript"
        src='//webapi.amap.com/maps?v=1.4.12&key=044a68bca642bd52ae17b08c3fa21c88'></script>
<!-- UI组件库 1.0 -->
<script src="//webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<%--<script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>--%>

<script type="text/javascript">
    // $('#container').height($(window).height() - 95);
    // $('#my-detail').height($(window).height() - 250);
    var _positionResult;

    function save(callbackFun) {
        var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
        parent.layer.close(dialogIndex);
    }

    var city = "<%= Global.getConfig("city")%>".trim();
    var location2 = '${location}'.trim();

    var map = new AMap.Map('container', {
        mapStyle: 'amap://styles/light', //设置地图的显示样式
        /*
        bg 区域面
        point 兴趣点
        road 道路及道路标注
        building 建筑物
         */
        features: [
            'bg',
            'road',
            'point',
            'building'
        ],// 多个种类要素显示
        resizeEnable: false,
        zoom: 16,
        scrollWheel: true
    });

    var list;
    if (location2) {
        list = location2.split(',');
        var lng = list[0];
        var lat = list[1];
        // 传入经纬度，设置地图中心点
        var position = new AMap.LngLat(lng, lat);
        // 简写 var position = [116, 39];
        map.setCenter(position);


    } else {
        map.setCity(city);
    }


    AMapUI.loadUI(['overlay/SimpleMarker'], function(SimpleMarker) {


        new SimpleMarker({
            // iconLabel: '',
            //自定义图标节点(img)的属性
            iconStyle: {

                src: '//webapi.amap.com/theme/v1.3/markers/b/mark_b.png',
                style: {
                    width: '20px',
                    height: '30px'
                }
            },

            //设置基点偏移
            offset: new AMap.Pixel(-10, -30),

            map: map,
            showPositionPoint: true,
            position: list,
            zIndex: 2000000
        });

    });


    AMapUI.loadUI(['misc/PositionPicker'], function (PositionPicker) {
        // ----------------------------------- 拖拽定位后，显示数据
        var positionPicker = new PositionPicker({
            mode:'dragMarker',//拖拽Marker模式
            map:map
        });

        positionPicker.on('success', function (positionResult) {
            document.getElementById('lnglat').innerHTML = positionResult.position;
            document.getElementById('address').innerHTML = positionResult.address;
            document.getElementById('nearestJunction').innerHTML = positionResult.nearestJunction;
            document.getElementById('nearestRoad').innerHTML = positionResult.nearestRoad;
            // document.getElementById('nearestPOI').innerHTML = positionResult.nearestPOI;
        });
        positionPicker.on('fail', function (positionResult) {
            document.getElementById('lnglat').innerHTML = ' ';
            document.getElementById('address').innerHTML = ' ';
            document.getElementById('nearestJunction').innerHTML = ' ';
            document.getElementById('nearestRoad').innerHTML = ' ';
            // document.getElementById('nearestPOI').innerHTML = ' ';
        });

        positionPicker.start();
        // positionPicker.stop();

    });

    // map.setFitView();
    // map.panBy(0, 1);

</script>
</body>
</html>