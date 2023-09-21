package com.yiwise.asr.protocol;

import com.alibaba.fastjson.JSON;
import com.yiwise.utils.IdGen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public class SpeechReqProtocol {
    static Logger logger = LoggerFactory.getLogger(SpeechReqProtocol.class);
    protected String accessToken;
    protected Connection conn;
    protected String currentTaskId;

    protected State state = State.STATE_INIT;

    /**
     * 状态
     */
    public enum State {
        /**
         * 错误状态
         */
        STATE_FAIL(-1) {
            @Override
            public void checkStart() {
                throw new RuntimeException("can't start,current state is " + this);
            }

            @Override
            public void checkSend() {
                throw new RuntimeException("can't send,current state is " + this);
            }

            @Override
            public void checkStop() {
                throw new RuntimeException("can't stop,current state is " + this);
            }
        },
        /**
         * 初始状态
         */
        STATE_INIT(0) {
            @Override
            public void checkStart() {
                throw new RuntimeException("can't start,current state is " + this);
            }

            @Override
            public void checkSend() {
                throw new RuntimeException("can't send,current state is " + this);
            }

            @Override
            public void checkStop() {
                throw new RuntimeException("can't stop,current state is " + this);
            }
        },
        /**
         * 已连接状态
         */
        STATE_CONNECTED(10) {
            @Override
            public void checkSend() {
                throw new RuntimeException("can't send,current state is " + this);
            }

            @Override
            public void checkStart() {
                return;
            }

            @Override
            public void checkStop() {
                throw new RuntimeException("can't stop,current state is " + this);
            }
        },
        /**
         * 语音识别请求(json)已发送
         */
        STATE_REQUEST_SENT(20) {
            @Override
            public void checkSend() {
                throw new RuntimeException("can't send,current state is " + this);
            }

            @Override
            public void checkStart() {
                throw new RuntimeException("can't start,current state is " + this);
            }

            @Override
            public void checkStop() {
                throw new RuntimeException("can't stop,current state is " + this);
            }
        },
        /**
         * 已收到服务端对请求的确认,服务端准备接受音频
         */
        STATE_REQUEST_CONFIRMED(30) {
            @Override
            public void checkSend() {
                return;
            }

            @Override
            public void checkStart() {
                throw new RuntimeException("can't start,current state is " + this);
            }

            @Override
            public void checkStop() {
                return;
            }
        },
        /**
         * 语音发送完毕,识别结束指令已发送
         */
        STATE_STOP_SENT(40) {
            @Override
            public void checkSend() {
                throw new RuntimeException("only STATE_REQUEST_CONFIRMED can send,current state is " + this);
            }

            @Override
            public void checkStart() {
                throw new RuntimeException("can't start,current state is " + this);
            }

            @Override
            public void checkStop() {
                throw new RuntimeException("can't stop,current state is " + this);
            }
        },

        /**
         * 收到全部识别结果,识别结束
         */
        STATE_COMPLETE(50) {
            @Override
            public void checkSend() {
                throw new RuntimeException("can't send,current state is " + this);
            }

            @Override
            public void checkStart() {
                return;
            }

            @Override
            public void checkStop() {
                //开启静音监测时,服务端可能提前结束
                logger.warn("task is completed before sending stop command");
            }
        },
        /**
         * 连接关闭
         */
        STATE_CLOSED(60) {
            @Override
            public void checkSend() {
                throw new RuntimeException("can't send,current state is " + this);
            }

            @Override
            public void checkStart() {
                throw new RuntimeException("can't start,current state is " + this);
            }

            @Override
            public void checkStop() {
                throw new RuntimeException("can't stop,current state is " + this);
            }
        };

        int value;

        public abstract void checkSend();

        public abstract void checkStart();

        public abstract void checkStop();

        State(int value) {
            this.value = value;
        }
    }

    public Map<String, String> header = new HashMap<String, String>();
    public Map<String, Object> payload;
    public Map<String, Object> context = new HashMap<String, Object>();

    public SpeechReqProtocol() {
        header.put(Constant.PROP_MESSAGE_ID, IdGen.genId());
        SdkInfo sdk = new SdkInfo();
        sdk.version = Constant.sdkVersion;
        context.put(Constant.PROP_CONTEXT_SDK, sdk);
    }

    /**
     * 获取appkey
     * @return
     */
    public String getAppKey() {
        return header.get(Constant.PROP_APP_KEY);
    }

    /**
     * 设置appkey
     * @param appKey
     */
    public void setAppKey(String appKey) {
        header.put(Constant.PROP_APP_KEY, appKey);
    }

    /**
     * 获取task_id
     * @return
     */
    public String getTaskId() {
        return header.get(Constant.PROP_TASK_ID);
    }

    protected void setTaskId(String requestId) {
        header.put(Constant.PROP_TASK_ID, requestId);
    }

    /**
     * 设置上下文信息,如设备信息,位置信息等,用于需要使用或记录相关信息的场景
     * @param key
     * @param obj
     */
    public void putContext(String key, Object obj) {
        context.put(key, obj);
    }

    /**
     * 设置自定义请求参数,用于设置语音服务的高级属性,或新功能
     * @param key
     * @param value
     */
    public void addCustomedParam(String key, Object value) {
        payload.put(key, value);
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String serialize() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("header", header);
        if (payload != null) {
            result.put("payload", payload);
            result.put("context", context);
        }
        return JSON.toJSONString(result);
    }

    public Connection getConnection(){
        return conn;
    }

    public State getState(){
        return state;
    }

}