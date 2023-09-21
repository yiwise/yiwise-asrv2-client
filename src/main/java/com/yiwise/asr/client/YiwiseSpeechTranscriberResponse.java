package com.yiwise.asr.client;

import com.yiwise.asr.protocol.SpeechResProtocol;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public class YiwiseSpeechTranscriberResponse extends SpeechResProtocol {
    /**
     * 句子的index
     *
     * @return
     */
    public Integer getTransSentenceIndex() {
        return (Integer)payload.get("index");
    }

    /**
     * 当前已处理的音频时长，单位是毫秒
     *
     * @return
     */
    public Integer getTransSentenceTime() {
        return (Integer)payload.get("time");
    }

    /**
     * 结果置信度,0.0-1.0 值越大置信度越高
     *
     * @return
     */
    public Double getConfidence() {
        Object o=payload.get("confidence");
        if(o!=null){
            return Double.parseDouble(o.toString());
        }
        return null;
    }

    /**
     *  sentenceBegin事件对应的时间
     *
     * @return
     */
    public Integer getSentenceBeginTime() {
        return (Integer)payload.get("begin_time");
    }


    /**
     * 最终识别结果
     *
     * @return
     */
    public String getTransSentenceText() {
        return (String)payload.get("result");
    }
}