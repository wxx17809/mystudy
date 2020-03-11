package com.ghkj.gaqcommons.untils;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageUtil {
    /**
     * 方法描述:计算公多少页，
     * @return map
     * 作者： 陈彦名
     * 创建时间： 2019-4-20 10:30
     */
    public static int getPageNum(int pageSize,int allNum){
        int pageNum = 0;
        if(allNum % pageSize == 0){
            pageNum = allNum / pageSize;
        }else{
            pageNum = allNum / pageSize + 1;
        }
        return pageNum;
    }
    /**
     * 方法描述:查询Es数据库的内容并带分页
     * @return map
     * 作者： 陈彦名
     * 创建时间： 2019-4-20 10:30
     */
    public static Map<String, Object> pageEs(SearchResponse response,int pageIndex,int pageSize){
        Map<String,Object> map=new HashMap<>();
        List<Map> tarsysList =new ArrayList<>();
        SearchHits searchHits = response.getHits();
        for (SearchHit hit:searchHits){
            Map<String,Object> mapValue = hit.getSourceAsMap();
            mapValue.put("id",hit.getId());
            tarsysList.add(mapValue);
        }
        map.put("list",tarsysList);
        map.put("pageIndex",pageIndex);
        map.put("pageNum", getPageNum(pageSize,(int)response.getHits().totalHits));
        map.put("number",response.getHits().totalHits);
        return map;
    }
}
