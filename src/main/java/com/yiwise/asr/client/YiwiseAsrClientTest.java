package com.yiwise.asr.client;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public class YiwiseAsrClientTest {
    public static YiwiseAsrClient client;

    public static void main(String[] args) throws FileNotFoundException {
        String asrGatewayUrl = "ws://116.62.114.179:7070";
        client = YiwiseAsrClientFactory.getAsrClient(asrGatewayUrl);

        InputStream ins = Thread.currentThread().getContextClassLoader().getResourceAsStream("pcm8k-files/user1.wav");
        // InputStream ins = new FileInputStream("/Users/sunguoyang/Desktop/user2.wav");
        if (null == ins) {
            System.err.println("open the audio file failed!");
            return;
        }

        InputStream ins2 = Thread.currentThread().getContextClassLoader().getResourceAsStream("pcm8k-files/user2.wav");
        // InputStream ins2 = new FileInputStream("/Users/sunguoyang/Desktop/mock_user_say.wav");
        if (null == ins2) {
            System.err.println("open the audio file failed!");
            return;
        }
        process(ins, ins2);
        shutdown();
    }

    private static YiwiseSpeechTranscriberListener getTranscriberListener() {
        YiwiseSpeechTranscriberListener listener = new YiwiseSpeechTranscriberListener() {
            // 识别出中间结果.服务端识别出一个字或词时会返回此消息.仅当setEnableIntermediateResult(true)时,才会有此类消息返回
            @Override
            public void onTranscriptionResultChange(YiwiseSpeechTranscriberResponse response) {
                System.out.println("name: " + response.getName() +
                        // 状态码 20000000 表示正常识别
                        ", status: " + response.getStatus() +
                        // 句子编号，从1开始递增
                        ", createDocument: " + response.getTransSentenceIndex() +
                        // 当前句子的中间识别结果
                        ", result: " + response.getTransSentenceText() +
                        // 当前已处理的音频时长，单位是毫秒
                        ", time: " + response.getTransSentenceTime());
            }

            // 识别出一句话.服务端会智能断句,当识别到一句话结束时会返回此消息
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
            }

            // 识别完毕
            @Override
            public void onTranscriptionComplete(YiwiseSpeechTranscriberResponse response) {
                System.out.println("name: " + response.getName() +
                        ", status: " + response.getStatus());
            }
        };

        return listener;
    }

    public static void process(InputStream ins, InputStream ins2) {
        YiwiseSpeechTranscriber transcriber = null;
        try {
            // Step1 创建实例,建立连接
            transcriber = new YiwiseSpeechTranscriber(client, getTranscriberListener());
            // 是否返回中间识别结果
            transcriber.setEnableIntermediateResult(false);
            // 是否生成并返回标点符号
            transcriber.setEnablePunctuation(true);
            // 是否将返回结果规整化,比如将一百返回为100
            transcriber.setEnableITN(true);

            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            transcriber.start();
            // Step3 语音数据来自声音文件用此方法,控制发送速率;若语音来自实时录音,不需控制发送速率直接调用 recognizer.sent(ins)即可
            transcriber.send(ins, 3200, 200);
            // Step4 通知服务端语音数据发送完毕,等待服务端处理完成
            transcriber.stop();

            System.out.println("--------------------------------");
            Thread.sleep(9000L);

            // Step2 此方法将以上参数设置序列化为json发送给服务端,并等待服务端确认
            transcriber.start();
            // Step3 语音数据来自声音文件用此方法,控制发送速率;若语音来自实时录音,不需控制发送速率直接调用 recognizer.sent(ins)即可
            transcriber.send(ins2, 3200, 200);
            // Step4 通知服务端语音数据发送完毕,等待服务端处理完成
            transcriber.stop();

        } catch (Exception e) {
            System.err.println(e.getMessage());
        } finally {
            // Step5 关闭连接
            if (null != transcriber) {
                transcriber.close();
            }
        }
    }

    public static void shutdown() {
        try {
            client.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
