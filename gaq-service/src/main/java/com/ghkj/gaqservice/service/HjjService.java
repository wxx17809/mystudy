package com.ghkj.gaqservice.service;
import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @version 1.0
 * @ClassName : HjjService
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/9/4 15:35
 */
public interface HjjService {
    /**
     * 新增或者修改目标
     * @param hjj
     * @return
     */
    Map<String, Object> addOrUpdateHjj(JSONObject hjj)throws Exception;

    /**
     * 删除目标
     * @param ids
     * @return
     */
    Map<String, Object> deleteHjj(JSONObject ids);

    /**
     * 查询目标详情
     * @param ids
     * @return
     */
    Map<String, Object> findDetail(JSONObject ids);

    /**
     * 多条件搜索目标
      * @param hjjParams
     * @return
     */
    Map findHjjList(JSONObject hjjParams);

}
