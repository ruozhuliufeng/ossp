package cn.aixuxi.ossp.common.es.utils;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

/**
 * ES查询构建
 * @author ruozhuliufeng
 * @date 2021-08-21
 */
@Getter
@Setter
public class SearchBuilder {

    /**
     * 高亮前缀
     */
    private static final String HIGHLIGHTER_PRE_TAGS = "<mark>";

    /**
     * 高亮后缀
     */
    private static final String HIGHLIGHTER_POST_TAGS = "</mark>";

    private SearchRequest searchRequest;

    private SearchSourceBuilder searchBuilder;

    private RestHighLevelClient client;

    private SearchBuilder(SearchRequest searchRequest,SearchSourceBuilder searchBuilder,
                          RestHighLevelClient client){
        this.searchRequest = searchRequest;
        this.searchBuilder = searchBuilder;
        this.client = client;
    }

    /**
     * 生成SearchBuilder
     * @param client client
     * @param indexName indexName
     * @return SearchBuilder实例
     */
    public static SearchBuilder builder(RestHighLevelClient client,String indexName){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(indexName);
        searchRequest.source(searchSourceBuilder);
        return new SearchBuilder(searchRequest,searchSourceBuilder,client);
    }

    /**
     * 生成SearchBuilder实例
     * @param client client
     * @return SearchBuilder实例
     */
    public static SearchBuilder builder(RestHighLevelClient client){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(searchSourceBuilder);
        return new SearchBuilder(searchRequest,searchSourceBuilder,client);
    }

    /**
     * 设置索引名
     * @param indices 索引名数组
     * @return SearchBuilder
     */
    public SearchBuilder setIndices(String... indices){
        if (ArrayUtil.isNotEmpty(indices)){
            searchRequest.indices(indices);
        }
        return this;
    }

    /**
     * 生成QueryStringQuery查询
     * @param queryStr 查询关键字
     * @return
     */
    public SearchBuilder setStringQuery(String queryStr){
        QueryBuilder queryBuilder;
        if (StrUtil.isNotEmpty(queryStr)){
            queryBuilder = QueryBuilders.queryStringQuery(queryStr);
        }else {
            queryBuilder = QueryBuilders.matchAllQuery();
        }
        searchBuilder.query(queryBuilder);
        return this;
    }

    /**
     * 设置分页
     * @param page 当前页数
     * @param limit 每页显示条数
     * @return
     */
    public SearchBuilder setPage(Integer page,Integer limit){
        setPage(page,limit,false);
        return this;
    }

    /**
     * 设置分页
     * @param page 当前页数
     * @param limit 每页显示数
     * @param trackTotalHits 分页总数是否显示所有条数，默认只显示10000
     * @return
     */
    public SearchBuilder setPage(Integer page,Integer limit,boolean trackTotalHits){
        if (page != null && limit != null){
            searchBuilder.from((page-1)*limit)
                    .size(limit);
            if (trackTotalHits){
                searchBuilder.trackTotalHits(trackTotalHits);
            }
        }
        return this;
    }

    /**
     * 增加排序
     * @param field 排序字段
     * @param order 顺序方向
     * @return
     */
    public SearchBuilder addSort(String field, SortOrder order){
        if (StrUtil.isNotEmpty(field) && order!=null){
            searchBuilder.sort(field,order);
        }
        return this;
    }

    /**
     * 拼凑数组为字符串
     * @param texts
     * @return
     */
    private String concat(Text[] texts){
        StringBuffer sb = new StringBuffer();
        for (Text text:texts){
            sb.append(text.toString());
        }
        return sb.toString();
    }
}
