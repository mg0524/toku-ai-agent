package com.toku.tokuaiagent.app;

import com.toku.tokuaiagent.advisor.MyLoggerAdvisor;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class LoveApp {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用\n" +
            "户可倾诉恋爱难题。“+\n" +
            "\"围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对篆的困扰；＂+“恋爱状态询问沟通，习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。”+\"引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";


    /**
     * 初始化AI客户端
     * @param dashscopeChatModel
     */
    public LoveApp(ChatModel dashscopeChatModel){
        ChatMemory chatMemory = MessageWindowChatMemory.builder()
                .maxMessages(10)
                .build();
        chatClient = ChatClient
                .builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),
                        new MyLoggerAdvisor())
                .build();

    }

    /**
     * AI基础会话
     * @param userMessage
     * @param chatId
     * @return
     */
    public String doChat(String userMessage,String chatId){
        String content = chatClient.prompt()
                .user(userMessage)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .content();
        log.info("content:{}",content);
        return content;
    }


    record LoveReport(String title, List<String> context){}
    /**
     * AI 实现结构化输出
     * @param userMessage
     * @param chatId
     * @return
     */
    public LoveReport doChatWithReport(String userMessage,String chatId){
        LoveReport loveReport = chatClient.prompt()
                .user(userMessage)
                .system(SYSTEM_PROMPT + "每次对话都要生成报告结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .entity(LoveReport.class);
        log.info("LoveReport:{}",loveReport);
        return loveReport;
    }


    @Resource
    private VectorStore vectorStore;

    @Resource
    private Advisor loveAppRagCloudAdvisor;

    /**
     * AI rag 检索回答
     * @param userMessage
     * @param chatId
     * @return
     */
    public String doChatWithRag(String userMessage,String chatId){
        String content = chatClient.prompt()
                .user(userMessage)
                .advisors(spec -> spec.param(ChatMemory.CONVERSATION_ID, chatId))
                //日志记录
                .advisors(new MyLoggerAdvisor())
                //知识库应答
//                .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
                .advisors(loveAppRagCloudAdvisor)
                .advisors()
                .call().content();

        log.info("content:{}",content);
        return content;
    }

}
