/**
 * Copyright &copy; 2015-2020 <a href="http://www.jeeplus.org/">JeePlus</a> All rights reserved.
 */
package com.jeeplus.modules.tp.material.service;

import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jeeplus.core.persistence.Page;
import com.jeeplus.core.service.CrudService;
import com.jeeplus.modules.tp.material.entity.TpMaterialPart;
import com.jeeplus.modules.tp.material.mapper.TpMaterialPartMapper;

/**
 * 零件Service
 *
 * @author 尹彬
 * @version 2018-12-21
 */
@Service
@Transactional(readOnly = true)
public class TpMaterialPartService extends CrudService<TpMaterialPartMapper, TpMaterialPart> {

    @Autowired
    private TpMaterialPartMapper tpMaterialPartMapper;

    public TpMaterialPart get(String id) {
        return super.get(id);
    }

    public List<TpMaterialPart> findList(TpMaterialPart tpMaterialPart) {
        return super.findList(tpMaterialPart);
    }

    public Page<TpMaterialPart> findPage(Page<TpMaterialPart> page, TpMaterialPart tpMaterialPart) {
        return super.findPage(page, tpMaterialPart);
    }

    @Transactional(readOnly = false)
    public void save(TpMaterialPart tpMaterialPart) {
        try {
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            tpMaterialPart.setName_py(PinyinHelper.toHanYuPinyinString(tpMaterialPart.getName(), format, " ", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.save(tpMaterialPart);
    }

    @Transactional(readOnly = false)
    public void delete(TpMaterialPart tpMaterialPart) {
        super.delete(tpMaterialPart);
    }

    public List<Map<String, String>> autocomplete(String query) {
        return tpMaterialPartMapper.autocomplete(query);
    }
}