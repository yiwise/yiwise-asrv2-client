package com.yiwise.asr.protocol;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public enum SampleRateEnum {
    /**
     * 8000
     */
    SAMPLE_RATE_8K(8000),
    /**
     * 16000
     */
    SAMPLE_RATE_16K(16000);
    public int value;

    SampleRateEnum(int value) {
        this.value = value;
    }
}
