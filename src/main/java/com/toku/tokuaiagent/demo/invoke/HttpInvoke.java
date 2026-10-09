package com.toku.tokuaiagent.demo.invoke;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * http 请求方式
 */
public class HttpInvoke {

    public static void main(String[] args) {
        // 配置参数
        String workspaceId = "ws-k9jy5v6lbkv1iqxj";
        String apiKey = TestApiKey.API_KEY;
        String url = "https://" + workspaceId + ".cn-beijing.maas.aliyuncs.com/api/v1/services/aigc/multimodal-generation/generation";

        // 构建请求体
        JSONObject systemContent = new JSONObject()
                .set("text", "You are a helpful assistant.");

        JSONObject userContent = new JSONObject()
                .set("text", "你是谁？");

        JSONObject systemMessage = new JSONObject()
                .set("role", "system")
                .set("content", new JSONArray().put(systemContent));

        JSONObject userMessage = new JSONObject()
                .set("role", "user")
                .set("content", new JSONArray().put(userContent));

        JSONArray messages = new JSONArray()
                .put(systemMessage)
                .put(userMessage);

        JSONObject input = new JSONObject()
                .set("messages", messages);

        JSONObject parameters = new JSONObject()
                .set("result_format", "message");

        JSONObject body = new JSONObject()
                .set("model", "qwen3.8-max")
                .set("input", input)
                .set("parameters", parameters);

        // 发送请求
        try (HttpResponse response = HttpRequest.post(url)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .body(body.toString())
                .timeout(30000)
                .execute()) {

            System.out.println("状态码: " + response.getStatus());
            System.out.println("响应内容: " + response.body());

            // 如果需要解析响应为 JSON
            if (response.isOk()) {
                JSONObject result = JSONUtil.parseObj(response.body());
                System.out.println("解析结果: " + result);
            }
        }
    }
}