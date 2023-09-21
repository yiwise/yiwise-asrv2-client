package com.yiwise.asr.protocol;

/**
 * Created by 昌夜 on 2023/6/25.
 */
public class Constant {
    public static String sdkVersion = "2.0.3";

    public static final String HEADER_TOKEN = "X-NLS-Token";

    public static final String PROP_CONTEXT_SDK = "sdk";

    public static final String PROP_APP_KEY = "appkey";
    public static final String PROP_NAMESPACE = "namespace";
    public static final String PROP_NAME = "name";
    public static final String PROP_STATUS = "status";
    public static final String PROP_STATUS_TEXT = "status_text";
    public static final String PROP_MESSAGE_ID = "message_id";
    public static final String PROP_TASK_ID = "task_id";

    public static final String PROP_ASR_FORMAT = "format";
    public static final String PROP_ASR_SAMPLE_RATE = "sample_rate";
    public static final String PROP_ASR_ENABLE_ITN = "enable_inverse_text_normalization";
    public static final String PROP_ASR_ENABLE_INTERMEDIATE_RESULT = "enable_intermediate_result";
    public static final String PROP_ASR_ENABLE_PUNCTUATION_PREDICTION = "enable_punctuation_prediction";

    public static final String VALUE_NAME_TASK_FAILE = "TaskFailed";
    public static final String VALUE_NAMESPACE_ASR = "SpeechRecognizer";
    public static final String VALUE_NAME_ASR_COMPLETE = "RecognitionCompleted";
    public static final String VALUE_NAME_ASR_START = "StartRecognition";
    public static final String VALUE_NAME_ASR_STARTED = "RecognitionStarted";
    public static final String VALUE_NAME_ASR_STOP = "StopRecognition";
    public static final String VALUE_NAME_ASR_RESULT_CHANGE = "RecognitionResultChanged";

    public static final String VALUE_NAMESPACE_ASR_TRANSCRIPTION = "SpeechTranscriber";
    public static final String VALUE_NAME_ASR_TRANSCRIPTION_START = "StartTranscription";
    public static final String VALUE_NAME_ASR_TRANSCRIPTION_STOP = "StopTranscription";
    public static final String VALUE_NAME_ASR_TRANSCRIPTION_STARTED = "TranscriptionStarted";
    public static final String VALUE_NAME_ASR_TRANSCRIPTION_RESULT_CHANGE = "TranscriptionResultChanged";
    public static final String VALUE_NAME_ASR_TRANSCRIPTION_COMPLETE = "TranscriptionCompleted";
    public static final String VALUE_NAME_ASR_SENTENCE_BEGIN = "SentenceBegin";
    public static final String VALUE_NAME_ASR_SENTENCE_END = "SentenceEnd";
}