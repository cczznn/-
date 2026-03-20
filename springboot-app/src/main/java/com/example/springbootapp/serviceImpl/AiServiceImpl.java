package com.example.springbootapp.serviceImpl;

import com.example.springbootapp.service.AiService;
import com.volcengine.ark.runtime.model.responses.constant.ResponsesConstants;
import com.volcengine.ark.runtime.model.responses.content.InputContentItemText;
import com.volcengine.ark.runtime.model.responses.item.ItemEasyMessage;
import com.volcengine.ark.runtime.model.responses.item.MessageContent;
import com.volcengine.ark.runtime.model.responses.request.CreateResponsesRequest;
import com.volcengine.ark.runtime.model.responses.request.ResponsesInput;
import com.volcengine.ark.runtime.model.responses.response.ResponseObject;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AiServiceImpl implements AiService {

    @Value("${ark.base-url:https://ark.cn-beijing.volces.com/api/v3}")
    private String baseUrl;

    @Value("${ark.endpoint-id:}")
    private String endpointId;

    @Override
    public String chat(String question) {
        String apiKey = System.getenv("ARK_API_KEY");
        if (apiKey == null || apiKey.isBlank()) {
            return "AI 服务尚未配置，请设置环境变量 ARK_API_KEY";
        }
        if (endpointId == null || endpointId.isBlank()) {
            return "AI 服务尚未配置，请在 application.properties 中配置 ark.endpoint-id";
        }

        ArkService arkService = ArkService.builder()
                .apiKey(apiKey)
                .baseUrl(baseUrl)
                .build();

        try {
            CreateResponsesRequest request = CreateResponsesRequest.builder()
                    .model(endpointId)
                    .input(ResponsesInput.builder().addListItem(
                            ItemEasyMessage.builder()
                                    .role(ResponsesConstants.MESSAGE_ROLE_USER)
                                    .content(MessageContent.builder()
                                            .addListItem(InputContentItemText.builder().text(question).build())
                                            .build())
                                    .build()
                    ).build())
                    .build();

            ResponseObject response = arkService.createResponse(request);
            String answer = extractAnswer(response);
            return answer == null || answer.isBlank() ? "AI 服务未返回答案" : answer;
        } finally {
            arkService.shutdownExecutor();
        }
    }


    private String extractAnswer(ResponseObject response) {
        if (response == null) {
            return null;
        }
        try {
            Object output = response.getClass().getMethod("getOutput").invoke(response);
            if (!(output instanceof Iterable<?> outputList)) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            for (Object item : outputList) {
                if (item == null) {
                    continue;
                }
                String className = item.getClass().getSimpleName();
                if (!className.contains("ItemOutputMessage")) {
                    continue;
                }
                Object content = item.getClass().getMethod("getContent").invoke(item);
                if (!(content instanceof Iterable<?> contentList)) {
                    continue;
                }
                for (Object contentItem : contentList) {
                    if (contentItem == null) {
                        continue;
                    }
                    String contentClass = contentItem.getClass().getSimpleName();
                    if (!contentClass.contains("OutputContentItemText")) {
                        continue;
                    }
                    Object text = contentItem.getClass().getMethod("getText").invoke(contentItem);
                    if (text != null) {
                        if (!sb.isEmpty()) {
                            sb.append("\n");
                        }
                        sb.append(text.toString());
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }
}

