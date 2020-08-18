package com.coach.test.coachmw.protocol;

/**
 * Created by Administrator on 2017-02-17.
 */

public interface IViewComment{
    /**
     * KIST 엔진에서 나온 결과물의 callback interface.
     * count 행동 횟수
     * accuracy_Percent 행동 정확도(%)
     * calorie 소비 칼로리 (kcal)
     */
    public void onKISTOutput_Calorie(final float calorie, final int video_index);
    public void onKISTOutput_Accuracy(final int accuracy_Percent, final int video_index);
    public void onKISTOutput_Count(final int count, final int video_index);
    public void onKISTOutput_Point(final int point, final int video_index);

    public void onKISTOutput_CalorieD(final float calorie, final int video_index);
    public void onKISTOutput_AccuracyD(final int accuracy_Percent, final int video_index);
    public void onKISTOutput_CountD(final int count, final int video_index);
    public void onKISTOutput_PointD(final int point, final int video_index);

    public void onInstruction(final String txt);
    //public void onInstructionD(final String txt);
    public void onWarnning(final String txt);
    public void onWarnningD(final String txt);
    public void onLaptime(final String txt);

    public void onCommentSection(final long displayTime, final int point, final int count_percent, final int accuracy_percent, final String txt);

    public void onCommentR(final String txt);

    public void onDescription(final int video_index, final String txt);
    public void onBriefDescription(final int video_index, final String txt); // 현재 운동 이름 전달.

    public void onHeartRateCompared(final int heartrate); // 현재 운동 이름 전달.
}