package com.yiwise.asr.protocol;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public class SpeechResProtocol {
    public Map<String, Object> header = new HashMap<String, Object>();
    public Map<String, Object> payload = new HashMap<String, Object>();

    public String getNameSpace() {
        return (String)header.get(Constant.PROP_NAMESPACE);
    }

    public String getName() {
        return (String)header.get(Constant.PROP_NAME);
    }

    public int getStatus() {
        return (Integer)header.get(Constant.PROP_STATUS);
    }

    public String getStatusText() {
        return (String)header.get(Constant.PROP_STATUS_TEXT);
    }

    public String getTaskId(){
        return (String)header.get(Constant.PROP_TASK_ID);
    }
}