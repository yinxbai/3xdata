package com.txdata.common.utils;

import it.sauronsoftware.jave.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class CvonvertVideo implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(CvonvertVideo.class);

    private File file;
    private File saveFile;
    private Integer width;
    private Integer height;
    /**
     * 视频压缩
     * @param file 视频源文件
     * @param saveFile 保存文件的
     * @throws EncoderException
     */
    public CvonvertVideo(File file, File saveFile){
        this.file = file;
        this.saveFile = saveFile;
    }

    /**
     * 视频压缩
     * @param file 视频源文件
     * @param saveFile 保存文件的
     * @param width 视频宽度
     * @param height 视频高度
     * @throws EncoderException
     */
    public CvonvertVideo(File file, File saveFile, Integer width, Integer height){
        this.file = file;
        this.saveFile = saveFile;
        this.width = width;
        this.height = height;
    }
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        AudioAttributes audioAttributes = new AudioAttributes(); // 音频属性
        audioAttributes.setCodec("libmp3lame"); // libfaac PGM编码
        audioAttributes.setBitRate(new Integer(56000)); // 音频比特率
        audioAttributes.setChannels(new Integer(2)); // 声道
        audioAttributes.setSamplingRate(new Integer(22050)); // 采样率
        VideoAttributes videoAttributes = new VideoAttributes(); // 视频属性
        videoAttributes.setCodec("mpeg4"); // 视屏编码
        videoAttributes.setBitRate(new Integer(3500000)); // 视频比特率
        videoAttributes.setFrameRate(new Integer(15)); // 视频帧率
//        videoAttributes.setSize(new VideoSize(width,height)); // 视频宽高
        EncodingAttributes attributes = new EncodingAttributes(); // 转码属性
        attributes.setFormat("mp4"); // 视频格式
        attributes.setAudioAttributes(audioAttributes); // 音频属性
        attributes.setVideoAttributes(videoAttributes); // 视频属性
        Encoder encoder = new Encoder();
        try {
            encoder.encode(file,saveFile,attributes);
            long endTime = System.currentTimeMillis(); //获取结束时间
            log.info("视频压缩成功！耗时：" + (endTime - startTime) + "ms");
        } catch (EncoderException e) {
            log.error("视频压缩失败！" + e.getMessage());
            e.printStackTrace();
        }finally {
            if (file.exists()){
                file.delete();
            }
        }
    }
}
