/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.maintenance.entity;

import com.jeeplus.modules.tp.material.entity.TpMaterialPart;
import com.jeeplus.modules.tp.road.entity.SysArea;
import javax.validation.constraints.NotNull;
import com.jeeplus.modules.tp.roadcross.entity.TpRoadCrossing;
import com.jeeplus.modules.tp.road.entity.TpRoad;
import com.jeeplus.modules.sys.entity.User;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.jeeplus.modules.sys.entity.Office;
import java.util.List;
import com.google.common.collect.Lists;

import com.jeeplus.core.persistence.DataEntity;
import com.jeeplus.common.utils.excel.annotation.ExcelField;

/**
 * 施工Entity
 * @author 尹彬
 * @version 2018-12-22
 */
public class TpMaintenance extends DataEntity<TpMaintenance> {

	private static final long serialVersionUID = 1L;
	private String num;		// 任务编号
	private String jobType;		// 任务类型
	private String malfunctionType; 	// 故障类型
	private String source;		// 任务来源
    private String jobDescription; //任务描述
	private String location;		// 经纬度
	private SysArea area;		// 所属区域
	private TpRoadCrossing roadcross;		// 所属路口
	private String nearestJunction;		// 所属路口相对位置
	private TpRoad road;		// 所属道路
	private String address;		// 搜索用地址
	private String nearestPoi;		// 搜索地址相对位置
	private User sendBy;		// 派单人
	private Date sendDate;		// 派单时间
	private String whatDay; 	// 星期几
	private Office office;		// 施工单位
	private User leaderBy;		// 施工负责人
	private Date jobBeginDate;		// 施工开始时间
	private Date jobEndDate;		// 施工结束时间
	private Double money = 0.0D;		// 总费用
	private String process;		// 施工过程
	private String prePic;		// 施工前照片
	private String middlePic;		// 施工中照片
	private String afterPic;		// 施工后照片
	private String approve;		// 审批意见
	private String status;		// 任务状态
	private Date beginSendDate;		// 开始 派单时间
	private Date endSendDate;		// 结束 派单时间
	private Date beginJobBeginDate;		// 开始 施工开始时间
	private Date endJobBeginDate;		// 结束 施工开始时间
	private Date beginJobEndDate;		// 开始 施工结束时间
	private Date endJobEndDate;		// 结束 施工结束时间
	private List<TpMaintenanceItem> tpMaintenanceItemList = Lists.newArrayList();		// 子表列表

    // ---- 导出报表用图片
	private byte[] image1;
	private byte[] image2;
	private byte[] image3;
	private byte[] image4;
    // 导出的物料list
    private List<TpMaterialPart> materialParts = Lists.newArrayList();

    // 物料统计 排序
	private String orderBy;
	// 查询分组方式
	private String group;

	public TpMaintenance() {
		super();
	}

	public TpMaintenance(String id){
		super(id);
	}

	public String getMalfunctionType() {
		return malfunctionType;
	}

	public void setMalfunctionType(String malfunctionType) {
		this.malfunctionType = malfunctionType;
	}

	@ExcelField(title="任务编号", align=2, sort=7)
	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	@ExcelField(title="任务类型", dictType="job_type", align=2, sort=8)
	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	@ExcelField(title="任务来源", dictType="job_source", align=2, sort=9)
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@NotNull(message="问题描述不能为空")
	@ExcelField(title="问题描述",align=2, sort=29)
	public String getJobDescription() {
		return jobDescription;
	}

	public void setJobDescription(String jobDescription) {
		this.jobDescription = jobDescription;
	}

	@ExcelField(title="经纬度", align=2, sort=10)
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@NotNull(message="所属区域不能为空")
	@ExcelField(title="所属区域", fieldType=SysArea.class, value="area.name", align=2, sort=11)
	public SysArea getArea() {
		return area;
	}

	public void setArea(SysArea area) {
		this.area = area;
	}

	@NotNull(message="所属路口不能为空")
	@ExcelField(title="所属路口", fieldType=TpRoadCrossing.class, value="roadcross.name", align=2, sort=12)
	public TpRoadCrossing getRoadcross() {
		return roadcross;
	}

	public void setRoadcross(TpRoadCrossing roadcross) {
		this.roadcross = roadcross;
	}

	@ExcelField(title="所属路口相对位置", align=2, sort=13)
	public String getNearestJunction() {
		return nearestJunction;
	}

	public void setNearestJunction(String nearestJunction) {
		this.nearestJunction = nearestJunction;
	}

	@ExcelField(title="所属道路", fieldType=TpRoad.class, value="road.name", align=2, sort=14)
	public TpRoad getRoad() {
		return road;
	}

	public void setRoad(TpRoad road) {
		this.road = road;
	}

	@ExcelField(title="搜索用地址", align=2, sort=15)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@ExcelField(title="搜索地址相对位置", align=2, sort=16)
	public String getNearestPoi() {
		return nearestPoi;
	}

	public void setNearestPoi(String nearestPoi) {
		this.nearestPoi = nearestPoi;
	}

	@ExcelField(title="派单人", fieldType=User.class, value="sendBy.name", align=2, sort=17)
	public User getSendBy() {
		return sendBy;
	}

	public void setSendBy(User sendBy) {
		this.sendBy = sendBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@ExcelField(title="派单时间", align=2, sort=18)
	public Date getSendDate() {
		return sendDate;
	}

	public void setSendDate(Date sendDate) {
		this.sendDate = sendDate;
	}

//	@NotNull(message="施工单位不能为空")
	@ExcelField(title="施工单位", fieldType=Office.class, value="office.name", align=2, sort=19)
	public Office getOffice() {
		return office;
	}

	public void setOffice(Office office)
	{
		this.office = office;
	}

//	@NotNull(message="施工负责人不能为空")
	@ExcelField(title="施工负责人", fieldType=User.class, value="leaderBy.name", align=2, sort=20)
	public User getLeaderBy() {
		return leaderBy;
	}

	public void setLeaderBy(User leaderBy) {
		this.leaderBy = leaderBy;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@NotNull(message="施工开始时间不能为空")
	@ExcelField(title="施工开始时间", align=2, sort=20)
	public Date getJobBeginDate() {
		return jobBeginDate;
	}

	public void setJobBeginDate(Date jobBeginDate) {
		this.jobBeginDate = jobBeginDate;
	}

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//	@NotNull(message="施工结束时间不能为空")
	@ExcelField(title="施工结束时间", align=2, sort=21)
	public Date getJobEndDate() {
		return jobEndDate;
	}

	public void setJobEndDate(Date jobEndDate) {
		this.jobEndDate = jobEndDate;
	}

	@ExcelField(title="物料总成本", align=2, sort=22)
	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	@ExcelField(title="施工过程", align=2, sort=23)
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	@ExcelField(title="施工前照片", align=2, sort=24)
	public String getPrePic() {
		return prePic;
	}

	public void setPrePic(String prePic) {
		this.prePic = prePic;
	}

	@ExcelField(title="施工中照片", align=2, sort=25)
	public String getMiddlePic() {
		return middlePic;
	}

	public void setMiddlePic(String middlePic) {
		this.middlePic = middlePic;
	}

	@ExcelField(title="施工后照片", align=2, sort=26)
	public String getAfterPic() {
		return afterPic;
	}

	public void setAfterPic(String afterPic) {
		this.afterPic = afterPic;
	}

	@ExcelField(title="审批意见", align=2, sort=27)
	public String getApprove() {
		return approve;
	}

	public void setApprove(String approve) {
		this.approve = approve;
	}

	@ExcelField(title="任务状态", dictType="job_status", align=2, sort=28)
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getBeginSendDate() {
		return beginSendDate;
	}

	public void setBeginSendDate(Date beginSendDate) {
		this.beginSendDate = beginSendDate;
	}

	public Date getEndSendDate() {
		return endSendDate;
	}

	public void setEndSendDate(Date endSendDate) {
		this.endSendDate = endSendDate;
	}

	public Date getBeginJobBeginDate() {
		return beginJobBeginDate;
	}

	public void setBeginJobBeginDate(Date beginJobBeginDate) {
		this.beginJobBeginDate = beginJobBeginDate;
	}

	public Date getEndJobBeginDate() {
		return endJobBeginDate;
	}

	public void setEndJobBeginDate(Date endJobBeginDate) {
		this.endJobBeginDate = endJobBeginDate;
	}

	public Date getBeginJobEndDate() {
		return beginJobEndDate;
	}

	public void setBeginJobEndDate(Date beginJobEndDate) {
		this.beginJobEndDate = beginJobEndDate;
	}

	public Date getEndJobEndDate() {
		return endJobEndDate;
	}

	public void setEndJobEndDate(Date endJobEndDate) {
		this.endJobEndDate = endJobEndDate;
	}

	public List<TpMaintenanceItem> getTpMaintenanceItemList() {
		return tpMaintenanceItemList;
	}

	public void setTpMaintenanceItemList(List<TpMaintenanceItem> tpMaintenanceItemList) {
		this.tpMaintenanceItemList = tpMaintenanceItemList;
	}


	public String getWhatDay() {
		return whatDay;
	}

	public void setWhatDay(String whatDay) {
		this.whatDay = whatDay;
	}

	public byte[] getImage1() {
		return image1;
	}

	public void setImage1(byte[] image1) {
		this.image1 = image1;
	}

	public byte[] getImage2() {
		return image2;
	}

	public void setImage2(byte[] image2) {
		this.image2 = image2;
	}

	public byte[] getImage3() {
		return image3;
	}

	public void setImage3(byte[] image3) {
		this.image3 = image3;
	}

	public byte[] getImage4() {
		return image4;
	}

	public void setImage4(byte[] image4) {
		this.image4 = image4;
	}

    public List<TpMaterialPart> getMaterialParts() {
        return materialParts;
    }

    public void setMaterialParts(List<TpMaterialPart> materialParts) {
        this.materialParts = materialParts;
    }

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}