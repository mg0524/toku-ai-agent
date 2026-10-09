package com.toku.tokuaiagent.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;

/**
 * langChain4j 调用
 */
public class LangChain4jAiInvoke {

    public static void main(String[] args) {
        QwenChatModel chatModel = QwenChatModel.builder()
                .apiKey(TestApiKey.API_KEY)
                .modelName("qwen-max")
                .build();
        String answer = chatModel.chat("您好，你是谁？");
        System.out.println(answer);
    }
}
