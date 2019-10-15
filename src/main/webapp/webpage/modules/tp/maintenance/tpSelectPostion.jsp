<%@ page import="com.jeeplus.common.config.Global" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/webpage/include/taglib.jsp" %>
<html>
<head>
    <title>选择地点</title>
    <meta name="decorator" content="ani"/>
    <!-- SUMMERNOTE -->
    <%@include file="/webpage/include/summernote.jsp" %>
    <link rel="stylesheet" href="https://a.amap.com/jsapi_demos/static/demo-center/css/demo-center.css"/>

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
            flex: 6;
            padding-right: 10px;
        }

        .my-right {
            flex: 11;
        }

        .amap-zoomcontrol {
            display: none !important;
        }
    </style>
</head>
<body>
<div class="wrapper wrapper-content">
    <div class="my-row">
        <div class="my-left">
            <div class="panel panel-primary" style="height: 20%;min-height: 120px;">
                <div class="panel-heading">
                    <h3 class="panel-title"> 第一步：快速搜索定位大概区域 </h3>
                </div>
                <div class="panel-body">
                    <div class="input-item">
                        <input id='tipinput' type="text" autocomplete="off" placeholder="输入任意地址定位大概范围" value="${roadcrossName}">
                    </div>
                    <%--<div class="title" style="color: darkred;margin-top: 5px;">注意：快速定位后，请拖拽右边的地图进行精确定位，定位后务必检查详细位置</div>--%>
                </div>
            </div>
            <div class="panel panel-primary" style="height: 78%">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        第三步：检查并修改详细位置
                    </h3>
                </div>
                <div id="my-detail" class="panel-body" style="overflow-y: scroll!important;height: 75%">
                    <div class='c'>经纬度:</div>
                    <div id='lnglat'></div>
                    <div class='c'>地址:</div>
                    <div id='address'></div>
                    <div class='c'>最近的路口:</div>
<%--                    <div id='nearestJunction'></div>--%>
                    <input type="text" id="nearestJunction">
                    <div class='c'>最近的道路:</div>
<%--                    <div id='nearestRoad'></div>--%>
                    <input type="text" id="nearestRoad">
                    <%--<div class='c'>最近的POI:</div>--%>
                    <%--<div id='nearestPOI'></div>--%>
                </div>
            </div>
        </div>
        <div class="my-right">
            <div class="panel panel-primary" style="height: 100%;">
                <div class="panel-heading">
                    <h3 class="panel-title">
                        第二步：拖拽地图进行精确定位
                    </h3>
                </div>
                <div class="panel-body"
                     style="padding: 0!important;height: 90%;overflow: hidden!important;">
                    <div id="container" class="map" style="height: 80%" tabindex="0"></div>
                </div>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript"
        src='//webapi.amap.com/maps?v=1.4.11&key=044a68bca642bd52ae17b08c3fa21c88&plugin=AMap.ToolBar,AMap.Autocomplete'></script>
<!-- UI组件库 1.0 -->
<script src="//webapi.amap.com/ui/1.0/main.js?v=1.0.11"></script>
<%--<script type="text/javascript" src="https://cache.amap.com/lbs/static/addToolbar.js"></script>--%>

<script type="text/javascript">
    // $('#container').height($(window).height() - 95);
    // $('#my-detail').height($(window).height() - 250);
    var _positionResult;

    function save(callbackFun) {
        delete _positionResult.regeocode.addressComponent.businessAreas;
        delete _positionResult.regeocode.aois;
        delete _positionResult.regeocode.pois;
        // console.log(_positionResult);
        if (!_positionResult) {
            jp.warning('请等待"详细位置"计算完毕.');
            return false;
        } else {
            jp.loading();
            _positionResult.nearestJunction = document.getElementById('nearestJunction').value;
            _positionResult.nearestRoad = document.getElementById('nearestRoad').value;
            jp.post("${ctx}/tp/maintenance/tpMaintenance/savePosition", {json: encodeURIComponent(JSON.stringify(_positionResult))}, function (data) {
                if (data.success) {
                    var dialogIndex = parent.layer.getFrameIndex(window.name); // 获取窗口索引
                    parent.layer.close(dialogIndex);
                    jp.success(data.msg);
                    callbackFun(data.body);
                } else {
                    jp.error(data.msg);
                }
            })
        }
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
            // 'bg',
            'road',
            // 'point',
            // 'building'
        ],// 多个种类要素显示
        // resizeEnable: true,
        zoom: 17,
        scrollWheel: true,
        city: city
    });

    var auto = new AMap.Autocomplete({
        input: "tipinput",
        citylimit: true,
        city: city // 兴趣点城市
    });

    if (location2) {
        var list = location2.split(',');
        var lng = list[0];
        var lat = list[1];
        // 传入经纬度，设置地图中心点
        var position = new AMap.LngLat(lng, lat);
        // 简写 var position = [116, 39];
        map.setCenter(position);
    } else {
        console.log(city);
        map.setCity(city);
        auto.setCity(city);
        auto.setCityLimit(city);
    }


    AMapUI.loadUI(['misc/PositionPicker'], function (PositionPicker) {
        // ----------------------------------- 拖拽定位后，显示数据
        var positionPicker = new PositionPicker({
            mode: 'dragMap',
            map: map,
            iconStyle: { //自定义外观
                url: '//webapi.amap.com/ui/1.0/assets/position-picker2.png',
                ancher: [24, 40],
                size: [48, 48]
            }
        });

        // ----------------------------------- 选中自动完成行，地图定位
        AMap.event.addListener(auto, "select", function (e) {
            // console.log(e);
            // 传入经纬度，设置地图中心点
            var position = new AMap.LngLat(e.poi.location.lng, e.poi.location.lat);  // 标准写法
            // 简写 var position = [116, 39];
            map.setCenter(position);
            positionPicker.start();
        });//注册监听，当选中某条记录时会触发

        positionPicker.on('success', function (positionResult) {
            _positionResult = positionResult;
            document.getElementById('lnglat').innerHTML = positionResult.position;
            document.getElementById('address').innerHTML = positionResult.address;
            // document.getElementById('nearestJunction').innerHTML = positionResult.nearestJunction;
            // document.getElementById('nearestRoad').innerHTML = positionResult.nearestRoad;
            document.getElementById('nearestJunction').value = positionResult.nearestJunction;
            document.getElementById('nearestRoad').value = positionResult.nearestRoad;
            // document.getElementById('nearestPOI').innerHTML = positionResult.nearestPOI;
        });
        positionPicker.on('fail', function (positionResult) {
            document.getElementById('lnglat').innerHTML = ' ';
            document.getElementById('address').innerHTML = ' ';
            // document.getElementById('nearestJunction').innerHTML = ' ';
            // document.getElementById('nearestRoad').innerHTML = ' ';
            document.getElementById('nearestJunction').value = ' ';
            document.getElementById('nearestRoad').value = ' ';
            // document.getElementById('nearestPOI').innerHTML = ' ';
        });

        positionPicker.start();

        map.addControl(new AMap.ToolBar({
            liteStyle: true
        }))
    });
</script>
</body>
</html>