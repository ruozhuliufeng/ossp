package cn.aixuxi.ossp.search.server.admin.service.impl;

import cn.aixuxi.ossp.common.model.PageResult;
import cn.aixuxi.ossp.common.utils.JsonUtil;
import cn.aixuxi.ossp.search.server.admin.model.IndexDTO;
import cn.aixuxi.ossp.search.server.admin.service.IIndexService;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.xml.internal.bind.v2.model.core.TypeRef;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.AliasMetadata;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 索引服务
 * @author ruozhuliufeng
 * @version 1.0
 * @date 2021-09-04 14:05
 **/
@Service
public class IndexServiceImpl implements IIndexService {

    private ObjectMapper mapper = new ObjectMapper();

    private final RestHighLevelClient client;

    public IndexServiceImpl(RestHighLevelClient client){
        this.client = client;
    }
    /**
     * 创建索引
     *
     * @param indexDTO 索引数据
     * @return 是否创建成功
     * @throws IOException 异常
     */
    @Override
    public boolean create(IndexDTO indexDTO) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexDTO.getIndexName());
        request.settings(Settings.builder()
                .put("index.number_of_shards",indexDTO.getNumberOfShards())
                .put("index.number_of_replicas",indexDTO.getNumberOfReplicas())
        );
        if (StrUtil.isNotEmpty(indexDTO.getMappingsSource())){
            // mappings
            request.mapping(indexDTO.getMappingsSource(), XContentType.JSON);
        }
        CreateIndexResponse response = client
                .indices()
                .create(request, RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * 删除索引
     *
     * @param indexName 索引名
     * @return 是否删除成功
     * @throws IOException 异常
     */
    @Override
    public boolean delete(String indexName) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        AcknowledgedResponse response = client.indices().delete(request,RequestOptions.DEFAULT);
        return response.isAcknowledged();
    }

    /**
     * 索引列表
     *
     * @param queryStr 搜索字符串
     * @param indices  默认显示的索引名
     * @return PageResult
     * @throws IOException 异常
     */
    @Override
    public PageResult<Map<String, String>> list(String queryStr, String indices) throws IOException {
        if (StrUtil.isNotEmpty(queryStr)){
            indices = queryStr;
        }
        Response response = client.getLowLevelClient()
                .performRequest(
                        new Request(
                                "GET",
                                "/_cat/indices?h=health,status,index,docsCount,docsDeleted,storeSize&s=cds:desc&format=json&index="+StrUtil.nullToEmpty(indices)
                        )
                );

        List<Map<String,String>> listOfIndicesFromEs = null;
        if (response != null){
            String rawBody = EntityUtils.toString(response.getEntity());
            TypeReference<List<Map<String,String>>> typeReference = new TypeReference<List<Map<String, String>>>() {};
            listOfIndicesFromEs = mapper.readValue(rawBody,typeReference);
        }
        return PageResult.<Map<String, String>>builder().data(listOfIndicesFromEs).code(0).build();
    }

    /**
     * 显示索引明细
     *
     * @param indexName 索引名
     * @return 索引明细
     * @throws IOException 异常
     */
    @Override
    public Map<String, Object> show(String indexName) throws IOException {
        GetIndexRequest request = new GetIndexRequest(indexName);
        GetIndexResponse response = client
                .indices().get(request,RequestOptions.DEFAULT);
        MappingMetadata mappingMetadata = response.getMappings().get(indexName);
        Map<String,Object> mappOpenMap = mappingMetadata.getSourceAsMap();
        List<AliasMetadata> indexAliases = response.getAliases().get(indexName);

        String settingsStr = response.getSettings().get(indexName).toString();
        Object settingsObj = null;
        if (StrUtil.isNotEmpty(settingsStr)){
            settingsObj = JsonUtil.parse(settingsStr);
        }
        Map<String,Object> result = new HashMap<>(1);
        Map<String,Object> indexMap = new HashMap<>(3);
        List<String> aliasesList = new ArrayList<>(indexAliases.size());
        indexMap.put("aliases",aliasesList);
        indexMap.put("settings",settingsObj);
        indexMap.put("mappings",mappOpenMap);
        result.put(indexName,indexMap);
        // 获取aliases数据
        for (AliasMetadata aliasMetadata:indexAliases){
            aliasesList.add(aliasMetadata.getAlias());
        }
        return result;
    }
}
