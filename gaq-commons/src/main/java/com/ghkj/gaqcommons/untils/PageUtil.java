package com.ghkj.gaqcommons.untils;

import org.apache.lucene.search.TotalHits;
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
     * 作者： 吴璇璇
     * 创建时间： 2019-4-20 10:30
     */
    public static Map<String, Object> pageEs(SearchResponse response,int pageIndex,int pageSize){
        Map<String,Object> map=new HashMap<>();
        List<Map> tarsysList =new ArrayList<>();
        SearchHit[] hits = response.getHits().getHits();
        long total=response.getHits().getTotalHits().value;
        for (SearchHit hit:hits){
            Map<String,Object> mapValue = hit.getSourceAsMap();
            mapValue.put("id",hit.getId());
            tarsysList.add(mapValue);
        }
        map.put("list",tarsysList);
        map.put("pageIndex",pageIndex);
        map.put("pageNum", getPageNum(pageSize,(int)total));
        map.put("totalNum",total);
        return map;
    }
}
