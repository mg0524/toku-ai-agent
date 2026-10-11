package com.toku.tokuaiagent.rag;

import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import jakarta.annotation.Resource;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * LoveAppVectorStoreConfig 内存向量数据库配置
 */
@Configuration
public class LoveAppVectorStoreConfig {

    @Resource
    private LoveAppDocumentLoader loveAppDocumentLoader;


    /**
     *初始化向量数据
     * @param dashScopeEmbeddingModel 向量模型
     * @return
     */
    @Bean
    VectorStore loveAppVectorStore(EmbeddingModel dashScopeEmbeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(dashScopeEmbeddingModel).build();
        //加载文档
        vectorStore.add(loveAppDocumentLoader.loadMarkdown());
        return vectorStore;
    }
}
