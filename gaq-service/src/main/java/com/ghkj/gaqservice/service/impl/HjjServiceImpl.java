package com.ghkj.gaqservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ghkj.gaqcommons.untils.PageUtil;
import com.ghkj.gaqservice.service.HjjService;
import io.swagger.models.auth.In;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @version 1.0
 * @ClassName : HjjServiceImpl
 * @Description TODO
 * @Author : 吴璇璇
 * @Date : 2019/9/4 15:36
 */
@Service
public class HjjServiceImpl implements HjjService {

    @Autowired
   private TransportClient client;

    @Override
    public Map<String, Object> addOrUpdateHjj(JSONObject hjj) {
        Map<String,Object> map=new HashMap<>();
        Map data=(Map) hjj.get("data");
        if(hjj.get("id")!=null && !"".equals(hjj.get("id"))){
            UpdateResponse response=client.prepareUpdate("hjj","hjj",hjj.get("id").toString()).setRefreshPolicy("true").setDoc(data).get();
        }else{
            IndexResponse response1=client.prepareIndex("hjj","hjj").setRefreshPolicy("true").setSource(data).get();
        }
        map.put("state",200);
        return map;
    }

    @Override
    public Map<String, Object> deleteHjj(JSONObject ids) {
        Map<String, Object> map=new HashMap<>();
        String id=ids.get("id").toString();
        DeleteResponse response=client.prepareDelete("hjj","hjj",id).setRefreshPolicy("true").execute().actionGet();
        map.put("state",200);
        return map;
    }

    @Override
    public Map<String, Object> findDetail(JSONObject ids) {
        Map<String, Object> map=new HashMap<>();
        String id=ids.get("id").toString();
        GetResponse response=client.prepareGet("hjj","hjj",id).get();
        map.put("state",200);
        map.put("id",response.getId());
        return map;

    }

    @Override
    public Map findHjjList(JSONObject hjjParams) {
        SearchRequestBuilder requestBuilder=client.prepareSearch("hjj").setTypes("hjj");
        QueryBuilder queryBuilder=QueryBuilders.boolQuery();
        String country=hjjParams.get("country").toString();
        String type=hjjParams.get("type").toString();
        Integer pageNo=Integer.parseInt(hjjParams.get("pageNo").toString());
        Integer pageSize= Integer.parseInt(hjjParams.get("pageSize").toString());
        //模糊查询
        //QueryBuilders.fuzzyQuery("cehckname","发财");
        if(country!=null && !"".equals(country)){
            queryBuilder=((BoolQueryBuilder) queryBuilder).must(QueryBuilders.matchPhraseQuery("country",country));
        }
        if(type!=null && !"".equals(type)){
            queryBuilder=((BoolQueryBuilder) queryBuilder).must(QueryBuilders.matchPhraseQuery("type",type));
        }
        SearchSourceBuilder searchSourceBuilder=SearchSourceBuilder.searchSource()
                .fetchSource(new String[]{"name","checkname"},new String[]{})
                .query(queryBuilder);

        SearchResponse response=requestBuilder
                .setSource(searchSourceBuilder)
                .setQuery(queryBuilder)
                .setFrom((pageNo-1)*pageSize)
                .setSize(pageSize)
                .execute()
                .actionGet();
        return PageUtil.pageEs(response,pageNo,pageSize);
    }

    @Override
    public Map findDataTask(JSONObject hjjParams) {
       SearchRequestBuilder requestBuilder=client.prepareSearch("hjj").setTypes("hjj");
       QueryBuilder queryBuilder=QueryBuilders.boolQuery();
        List<String> country=(List<String>) hjjParams.get("country");
       String startTime=hjjParams.get("startTime").toString();
       String endTime=hjjParams.get("endTime").toString();
        Integer pageNo=Integer.parseInt(hjjParams.get("pageNo").toString());
        Integer pageSize= Integer.parseInt(hjjParams.get("pageSize").toString());
       if(country.size()>0){
           queryBuilder=((BoolQueryBuilder) queryBuilder).must(QueryBuilders.termsQuery("country",country));
       }
       if(startTime!=null && !"".equals(startTime)){
           queryBuilder=((BoolQueryBuilder) queryBuilder).must(QueryBuilders.rangeQuery("modifyTime_Start").from(startTime).to(endTime));
       }
        SearchResponse response=requestBuilder.setQuery(queryBuilder)
                .setFrom((pageNo-1)*pageSize)
                .setSize(pageSize)
                .execute()
                .actionGet();
        return PageUtil.pageEs(response,pageNo,pageSize);

    }
}
