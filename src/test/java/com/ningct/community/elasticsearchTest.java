package com.ningct.community;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ningct.community.entity.DiscussPost;
import com.ningct.community.mapper.DiscussPostMapper;
import com.ningct.community.service.ElasticSearchService;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@SpringBootTest
public class elasticsearchTest {
    @Resource
    private DiscussPostMapper discussPostMapper;

    @Resource
    private RestHighLevelClient client;
    @Resource
    private ElasticSearchService searchService;

    @Test
    public void testDelete(){
        searchService.deletePost(224);
    }
    @Test
    public void testAdd(){
        for(int i = 109; i < 287; i++){
            DiscussPost post = discussPostMapper.selectDiscussPostById(i);
            if(post != null){
                searchService.addPost(post);
            }
        }
    }

    @Test
    public void testSearchService(){
        System.out.println(searchService.findPostCount("?????????"));
        List<DiscussPost> posts = searchService.searchPosts("?????????", 0, 10000);
        for (DiscussPost post : posts) {
            System.out.println(post.toString());
        }
        System.out.println(posts.size());
    }

    public void init(){
        client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("localhost", 9200, "http")));

    }
    public void close(){
        try {
            client.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testConnect(){
        init();
        close();
    }
    @Test
    public void testIndexCreat(){
        //????????????

        //??????????????????
        CreateIndexRequest createIndexRequest = new CreateIndexRequest("discusspost");
        try {
            //es????????????????????????????????????
            CreateIndexResponse createIndexResponse =
                    client.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            //????????????
            boolean acknowledged = createIndexResponse.isAcknowledged();
            System.out.println("???????????????" +acknowledged);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testIndexSearch(){
        //????????????

        //??????????????????
        GetIndexRequest getIndexRequest = new GetIndexRequest("discusspost");
        try {
            //es????????????????????????????????????
            GetIndexResponse getIndexResponse =
                    client.indices().get(getIndexRequest, RequestOptions.DEFAULT);
            //????????????
            System.out.println(getIndexResponse.getAliases());
            System.out.println(getIndexResponse.getMappings());
            System.out.println(getIndexResponse.getSettings());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testIndexDelete(){
        //????????????

        //??????????????????
        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("discusspost");
        try {
            //es????????????????????????????????????
            AcknowledgedResponse response =
                    client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);
            //????????????
            System.out.println("???????????????"+response.isAcknowledged());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDocumentInert() {
        init();

        //????????????

        try {
            //???????????????json?????????
            DiscussPost post = discussPostMapper.selectDiscussPostById(231);
            post.setContent("??????????????????????????????");
            ObjectMapper objectMapper = new ObjectMapper();
            String userJson = objectMapper.writeValueAsString(post);

            //????????????
            IndexRequest indexRequest = new IndexRequest()
                    .index("discusspost")
                    .id(String.valueOf(231))
                    .source(userJson, XContentType.JSON);


            //???????????????????????????
            IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);


            //????????????
            System.out.println("?????????"+response.getResult());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        close();
    }
    @Test
    public void testDocUpdata(){
        init();

        //????????????

        try {
            //????????????
            UpdateRequest updateRequest = new UpdateRequest()
                    .index("discusspost")
                    .id("231")
                    .doc(XContentType.JSON,"content","????????????????????????");


            //???????????????????????????
            UpdateResponse response = client.update(updateRequest,RequestOptions.DEFAULT);


            //????????????
            System.out.println("?????????"+response.getResult());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        close();
    }

    @Test
    public void testDocSearch(){

        //????????????

        try {
            //????????????
            GetRequest getRequest = new GetRequest()
                    .index("discusspost")
                    .id("231");


            //???????????????????????????
            GetResponse response = client.get(getRequest, RequestOptions.DEFAULT);


            //????????????
            System.out.println(response.getSource());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testDocDelete(){
        init();

        //????????????

        try {
            //????????????
            DeleteRequest deleteRequest = new DeleteRequest()
                    .index("discusspost")
                            .id("231");

            //???????????????????????????
            DeleteResponse response = client.delete(deleteRequest, RequestOptions.DEFAULT);

            //????????????
            System.out.println("?????????"+response.status());


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        close();
    }
    @Test
    public void testDocBulkinsert(){
        init();

        //????????????

        try {
            //????????????
            BulkRequest bulkRequest = new BulkRequest()
                    .add(new DeleteRequest().index("discusspost").id("110"))
                    .add(new DeleteRequest().index("discusspost").id("116"))
                    .add(new DeleteRequest().index("discusspost").id("119"));

            //???????????????????????????
            BulkResponse response = client.bulk(bulkRequest, RequestOptions.DEFAULT);


            //????????????
            System.out.println(response.getTook());
            System.out.println(response.getItems());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        close();
    }
    @Test
    public  void testSearch(){

        try {
            //??????????????????
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .query(QueryBuilders.matchAllQuery());

            //????????????
            SearchRequest searchRequest = new SearchRequest()
                    .indices("discusspost")
                    .source(builder);
            //???????????????????????????
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            //????????????
            SearchHits hits = response.getHits();
            System.out.println(hits.getTotalHits());
            System.out.println(response.getTook());
            for (SearchHit hit : hits) {
                System.out.println(hit);
            }
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
    @Test
    public void testSearchCondition(){

        try {
            //??????????????????

                //??????
            TermsQueryBuilder termsQuery = QueryBuilders
                    .termsQuery("userId", new int[]{102, 103});
                //??????
            RangeQueryBuilder rangeQuery = QueryBuilders
                    .rangeQuery("id")
                    .lt(150)
                    .gt(100);
                //??????
            FuzzyQueryBuilder fuzzyQuery = QueryBuilders
                    .fuzzyQuery("title", "?????????")
                    .fuzziness(Fuzziness.TWO);

                //??????
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                    .must(rangeQuery)
                    .must(termsQuery)
                    .must(fuzzyQuery);

            //????????????
            String[] includes = {"id","userId","content"};
            String[] excludes = {};

            //??????
            HighlightBuilder highlightBuilder = new HighlightBuilder()
                    .field("content")
                    .preTags("<em>")
                    .postTags("</em>");

            //??????
            AggregationBuilder aggregationBuilder = AggregationBuilders
                    .terms("userGroup")
                    .field("userId");

            //??????????????????
            SearchSourceBuilder builder = new SearchSourceBuilder()
                    .fetchSource(includes,excludes)
                    .query(boolQuery)
                    .from(0)
                    .size(2)
                    .sort("id", SortOrder.DESC)
                    .highlighter(highlightBuilder)
                    .aggregation(aggregationBuilder);

            //????????????
            SearchRequest searchRequest = new SearchRequest()
                    .indices("discusspost")
                    .source(builder);

            //???????????????????????????
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);

            //????????????
            SearchHits hits = response.getHits();
            System.out.println(hits.getTotalHits());
            System.out.println(response.getTook());
            for (SearchHit hit : hits) {
                System.out.println(hit.getSourceAsString());
            }


        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}
