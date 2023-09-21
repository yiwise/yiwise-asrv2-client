## yiwise-asr-sdk-java
该工程提供了一知ASR的client，目前仅支持PCM且采样率8k的音频

## 安装
下载最新版本sdk
```xml
<dependency>    
      <groupId>com.yiwise</groupId>  
      <artifactId>yiwise-asrv2-client</artifactId>   
      <version>1.0.0</version>
</dependency>
```

##  示例代码

```java
public static void asr(String filePath) throws IOException {
        // 创建客户端实例
        String asrGatewayUrl = "ws://127.0.0.1:7070";
        YiwiseAsrClient client = new YiwiseAsrClient(asrGatewayUrl);


        // 识别结果回调listener
        YiwiseSpeechTranscriberListener listener = new YiwiseSpeechTranscriberListener() {
            /**
             * 识别出一句话
             */
            @Override
            public void onSentenceEnd(YiwiseSpeechTranscriberResponse response) {
                System.out.println("name: " + response.getName() +
                        // 状态码 20000000 表示正常识别
                        ", status: " + response.getStatus() +
                        // 句子编号，从1开始递增
                        ", createDocument: " + response.getTransSentenceIndex() +
                        // 当前句子的完整识别结果
                        ", result: " + response.getTransSentenceText() +
                        // 当前已处理的音频时长，单位是毫秒
                        ", time: " + response.getTransSentenceTime() +
                        // SentenceBegin事件的时间，单位是毫秒
                        ", begin time: " + response.getSentenceBeginTime() +
                        // 识别结果置信度，取值范围[0.0, 1.0]，值越大表示置信度越高
                        ", confidence: " + response.getConfidence());
                System.out.flush();
            }

            /**
             * 识别完成
             */
            @Override
            public void onTranscriptionComplete(YiwiseSpeechTranscriberResponse response) {
                System.out.println("name: " + response.getName() +
                        ", status: " + response.getStatus());
                System.out.flush();
                try {
                    client.shutdown();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        YiwiseSpeechTranscriber transcriber = null;
        try {
            // 创建转录器实例
            transcriber = new YiwiseSpeechTranscriber(client, listener);
            // 开始识别
            transcriber.start();

            InputStream ins = Files.newInputStream(Paths.get(filePath));
            // 语音数据来自声音文件用此方法,控制发送速率;若语音来自实时录音,不需控制发送速率直接调用 recognizer.send(bytes)即可
            transcriber.send(ins, 3200, 200);

            // 通知服务端语音数据发送完毕,等待服务端处理完成
            transcriber.stop();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            client.shutdown();
        }
    }
```