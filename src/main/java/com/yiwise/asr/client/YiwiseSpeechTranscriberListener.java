package com.yiwise.asr.client;

import com.alibaba.fastjson.JSON;
import com.yiwise.asr.protocol.ConnectionListener;
import com.yiwise.asr.protocol.Constant;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.nio.ByteBuffer;

/**
 * @author lgy
 */
public class YiwiseSpeechTranscriberListener implements ConnectionListener {
    private static final Logger logger = LoggerFactory.getLogger(YiwiseSpeechTranscriberListener.class);

    private static final String mdcKey = "MDC_LOG_ID";

    private static final int SUCCESS = 20000000;

    private String identifyId;

    @Setter
    private YiwiseSpeechTranscriber yiwiseSpeechTranscriber;

    public YiwiseSpeechTranscriberListener() {
    }

    /**
     * Called when the connection is closed.
     * @param message the text message.
     */
    @Override
    public void onMessage(String message) {
        if (message == null || message.trim().length() == 0) {
            return;
        }
        logger.debug("on message:{}", message);
        YiwiseSpeechTranscriberResponse response = JSON.parseObject(message, YiwiseSpeechTranscriberResponse.class);
        if (isTranscriptionStarted(response)) {
            onTranscriberStart(response);
            yiwiseSpeechTranscriber.markTranscriberReady();
        } else if (isSentenceBegin(response)) {
            onSentenceBegin(response);
        } else if (isSentenceEnd(response)) {
            onSentenceEnd(response);
        } else if (isTranscriptionResultChanged(response)) {
            onTranscriptionResultChange(response);
        } else if (isTranscriptionCompleted(response)) {
            onTranscriptionComplete(response);
            yiwiseSpeechTranscriber.markTranscriberComplete();
        } else if (isTaskFailed(response)) {
            onFail(response.getStatus(), response.getStatusText());
            yiwiseSpeechTranscriber.markFail();
        } else {
            logger.error("can not process this message: {}", message);
        }
    }

    /**
     * Invoked after the connection was closed.
     * @param closeCode the RFC 6455 status code
     * @param reason    a string description for the reason of the close
     */
    @Override
    public void onClose(int closeCode, String reason) {
        if (yiwiseSpeechTranscriber != null) {
            yiwiseSpeechTranscriber.markClosed();
        }
        logger.info("connection is closed due to {},code:{}", reason, closeCode);
    }

    public void onSentenceBegin(YiwiseSpeechTranscriberResponse response) {

    }

    /**
     * 识别过程中返回的中间结果
     * @param response 识别结果
     */
    public void onTranscriptionResultChange(YiwiseSpeechTranscriberResponse response) {
        try {
            if (StringUtils.isEmpty(response.getTransSentenceText())) {
                return;
            }
            logger.debug("TransSentenceIndex={}, asrTaskId={}, 中间结果：{}", response.getTransSentenceIndex(), response.getTaskId(), response.getTransSentenceText());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * 识别过程中返回的最终结果
     * @param response 识别结果
     */
    public void onSentenceEnd(YiwiseSpeechTranscriberResponse response) {
        try {
            // 判断是否有文本和语义
            if (StringUtils.isEmpty(response.getTransSentenceText())) {
                return;
            }
            // 最终结果
            logger.debug("TransSentenceIndex={}, asrTaskId={}, 最终结果：{}", response.getTransSentenceIndex(), response.getTaskId(), response.getTransSentenceText());
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    /**
     * Invoded after fail
     * @param status 失败状态码
     * @param reason 失败原因
     */
    @Override
    public void onFail(int status, String reason) {
        try {
            MDC.put(mdcKey, this.identifyId);
            logger.error("nls fail status:{}, reasone:{}", status, reason);
        } finally {
            MDC.remove(mdcKey);
        }
    }

    /**
     *
     * @param message the binary message.
     */
    @Override
    public void onMessage(ByteBuffer message) {
    }

    @Override
    public void onOpen() {
        logger.debug("connection is ok");
    }


    /**
     * Invoked after an error.
     * @param throwable the cause
     */
    @Override
    public void onError(Throwable throwable) {
        logger.error(throwable.getMessage(), throwable);
    }

    /**
     *
     * @param response 识别结果
     * @return 是否开始识别
     */
    protected boolean isTranscriptionStarted(YiwiseSpeechTranscriberResponse response) {
        String name = response.getName();
        if (name.equals(Constant.VALUE_NAME_ASR_TRANSCRIPTION_STARTED)) {
            return true;
        }
        return false;
    }

    /**
     *
     * @param response 识别结果
     * @return 是否开始识别
     */
    protected boolean isSentenceBegin(YiwiseSpeechTranscriberResponse response) {
        String name = response.getName();
        if (name.equals(Constant.VALUE_NAME_ASR_SENTENCE_BEGIN)) {
            return true;
        }
        return false;
    }

    protected boolean isSentenceEnd(YiwiseSpeechTranscriberResponse response) {
        String name = response.getName();
        if (name.equals(Constant.VALUE_NAME_ASR_SENTENCE_END)) {
            return true;
        }
        return false;
    }

    protected boolean isTranscriptionResultChanged(YiwiseSpeechTranscriberResponse response) {
        String name = response.getName();
        if (name.equals(Constant.VALUE_NAME_ASR_TRANSCRIPTION_RESULT_CHANGE)) {
            return true;
        }
        return false;
    }

    protected boolean isTranscriptionCompleted(YiwiseSpeechTranscriberResponse response) {
        String name = response.getName();
        if (name.equals(Constant.VALUE_NAME_ASR_TRANSCRIPTION_COMPLETE)) {
            return true;
        }
        return false;
    }

    protected boolean isTaskFailed(YiwiseSpeechTranscriberResponse response) {
        String name = response.getName();
        if (name.equals(Constant.VALUE_NAME_TASK_FAILE)) {
            return true;
        }
        return false;
    }

    public void onTranscriberStart(YiwiseSpeechTranscriberResponse response) {

    }

    /**
     * 识别结束后返回的最终结果
     *
     * @param response 识别结果
     */
    public void onTranscriptionComplete(YiwiseSpeechTranscriberResponse response) {

    }
}
