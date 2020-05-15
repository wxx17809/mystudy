package com.ghkj.gaqcommons.untils;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class EsBaseUtil {

    @Autowired
    private RestHighLevelClient client;

    private static RestHighLevelClient clientDouble;

    @PostConstruct
    public void init() {
        clientDouble=client;
    }

    /**
     *新增或者修改文档
     * @param jsonObject
     * @return
     * @throws Exception
     */
    public static Map<String, Object> addOrUpdateDoc(JSONObject jsonObject)throws Exception {
        Map<String,Object> map=new HashMap<>();
        Map data=(Map) jsonObject.get("data");
        String indexName=jsonObject.getString("indexName");
        if(jsonObject.get("id")!=null && !"".equals(jsonObject.get("id"))){
            UpdateRequest request = new UpdateRequest(indexName,jsonObject.getString("id")).setRefreshPolicy("true").doc(data);
            UpdateResponse response = clientDouble.update(request, RequestOptions.DEFAULT);
        }else{
            IndexRequest request = new IndexRequest(indexName).setRefreshPolicy("true").source(data);
            IndexResponse response = clientDouble.index(request, RequestOptions.DEFAULT);
        }
        map.put("state",200);
        return map;
    }
}
