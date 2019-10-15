/**
 * Copyright &copy; 2018-2020 <a href="http://www.yinbin.ink/">青岛前途软件技术</a> All rights reserved.
 */
package com.jeeplus.modules.tp.material.service;

import java.util.List;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.common.utils.StringUtils;
import com.jeeplus.modules.tp.material.entity.TpMaterial;
import com.jeeplus.modules.tp.material.mapper.TpMaterialMapper;
import com.jeeplus.modules.tp.material.entity.TpMaterialPart;
import com.jeeplus.modules.tp.material.mapper.TpMaterialPartMapper;

/**
 * 物料Service
 * @author 尹彬
 * @version 2018-12-23
 */
@Service
@Transactional(readOnly = true)
public class TpMaterialService extends CrudService<TpMaterialMapper, TpMaterial> {

	@Autowired
	private TpMaterialPartMapper tpMaterialPartMapper;
	
	public TpMaterial get(String id) {
		TpMaterial tpMaterial = super.get(id);
		tpMaterial.setTpMaterialPartList(tpMaterialPartMapper.findList(new TpMaterialPart(tpMaterial)));
		return tpMaterial;
	}
	
	public List<TpMaterial> findList(TpMaterial tpMaterial) {
		return super.findList(tpMaterial);
	}
	
	public Page<TpMaterial> findPage(Page<TpMaterial> page, TpMaterial tpMaterial) {
		return super.findPage(page, tpMaterial);
	}
	
	@Transactional(readOnly = false)
	public void save(TpMaterial tpMaterial) {
		try {
			HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
			format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
			tpMaterial.setName_py(PinyinHelper.toHanYuPinyinString(tpMaterial.getName(), format, "", false));
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.save(tpMaterial);
		for (TpMaterialPart tpMaterialPart : tpMaterial.getTpMaterialPartList()){
			if (tpMaterialPart.getId() == null){
				continue;
			}
			if (TpMaterialPart.DEL_FLAG_NORMAL.equals(tpMaterialPart.getDelFlag())){
				try {
					HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
					format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
					tpMaterialPart.setName_py(PinyinHelper.toHanYuPinyinString(tpMaterialPart.getName(), format, "", false));
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (StringUtils.isBlank(tpMaterialPart.getId())){
					tpMaterialPart.setMaterial(tpMaterial);
					tpMaterialPart.preInsert();
					tpMaterialPartMapper.insert(tpMaterialPart);
				}else{
					tpMaterialPart.preUpdate();
					tpMaterialPartMapper.update(tpMaterialPart);
				}
			}else{
				tpMaterialPartMapper.delete(tpMaterialPart);
			}
		}
	}
	
	@Transactional(readOnly = false)
	public void delete(TpMaterial tpMaterial) {
		super.delete(tpMaterial);
		tpMaterialPartMapper.delete(new TpMaterialPart(tpMaterial));
	}
	
}