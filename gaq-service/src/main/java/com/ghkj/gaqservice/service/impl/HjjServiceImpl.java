package com.ghkj.gaqservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.ghkj.gaqcommons.untils.EsBaseUtil;
import com.ghkj.gaqcommons.untils.PageUtil;
import com.ghkj.gaqservice.service.HjjService;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
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
    private RestHighLevelClient client;

    @Override
    public Map<String, Object> addOrUpdateHjj(JSONObject jsonObject)throws Exception {
        Map<String,Object> map=new HashMap<>();
        //放入要操作的索引名称
        jsonObject.put("indexName","hjj");
        map= EsBaseUtil.addOrUpdateDoc(jsonObject);
        map.put("state",200);
        return map;
    }

    @Override
    public Map<String, Object> deleteHjj(JSONObject ids) {
        Map<String, Object> map=new HashMap<>();
        String id=ids.get("id").toString();
        try {
            DeleteRequest request = new DeleteRequest("hjj",id).setRefreshPolicy("true");
            client.delete(request,RequestOptions.DEFAULT);
            map.put("state",200);
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public Map<String, Object> findDetail(JSONObject ids) {
        Map<String, Object> map=new HashMap<>();
        String id=ids.get("id").toString();
        try {
            GetRequest getRequest = new GetRequest("hjj",id);
            GetResponse response = client.get(getRequest,RequestOptions.DEFAULT);
            map.put("state",200);
            map.put("id",response.getId());
            map.put("list",response.getSourceAsMap());
        }catch (Exception e){
            e.printStackTrace();
        }
        return map;

    }

    @Override
    public Map findHjjList(JSONObject hjjParams) {
        SearchRequest searchRequest = new SearchRequest();
        String name=hjjParams.get("name").toString();
        String pwd=hjjParams.get("pwd").toString();
        Integer pageNo=Integer.parseInt(hjjParams.get("pageNo").toString());
        Integer pageSize= Integer.parseInt(hjjParams.get("pageSize").toString());
        try {
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            QueryBuilder matchPhraseQueryBuilder = QueryBuilders.matchPhraseQuery("name", name);
            QueryBuilder matchPhraseQueryBuilder1 = QueryBuilders.matchPhraseQuery("pwd", pwd);
            searchSourceBuilder.query(QueryBuilders.boolQuery()
                    .must(matchPhraseQueryBuilder)
                    .must(matchPhraseQueryBuilder1));
            searchRequest.source(searchSourceBuilder);
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            return PageUtil.pageEs(response,pageNo,pageSize);
        }catch (Exception e){
            e.printStackTrace();
        }
         return null;
    }

}
