package com.yiwise.asr.protocol;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public enum InputFormatEnum {
    /**
     * pcm
     */
    PCM("pcm", 1),
    /**
     * opus
     */
    OPUS("opus", 2),
    /**
     * opu
     */
    OPU("opu", 3),
    /**
     * speex
     */
    SPEEX("speex", 4);
    String name;
    int index;

    public String getName() {
        return name;
    }


    InputFormatEnum(String name, int index) {
        this.name = name;
        this.index = index;
    }
}
