package com.coach.test.coachmw.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import com.coach.test.kistaart.KIST_AART;
import com.coach.test.kistaart.KIST_AART_output;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.coach.test.coachmw.datastructure.DataBase;
import com.coach.test.coachmw.protocol.INordicFormat;
import com.coach.test.coachmw.protocol.IViewComment;
import com.coach.test.coachmw.util.DataBaseUtil;
import com.coach.test.coachmw.util.ListQueueUtil;
import com.coach.test.coachmw.util.NumberFormatUtil;


/**
 * Created by Administrator on 2017-02-17.
 */

/**
 * 필라테스 비디오 재생과 관련하여 Surface를 제어하는 Manager. 재생되는 video에 따라 엔진의 구동이 달라질것으로 생각되며,
 * 그 제어를 하는 Manager. 즉, Kist 엔진과 App간의 연동.
 *
 * @author user
 *
 */
public final class VideoManager {
    private static final String tag = "VideoManager";

    /// Locale 수정///
    private static int LANGUAGE_SET = 1;
    /// 까지///

    /** Variable **/
    public static final String VIDEO_NAME = "com.coach.test.coachmw.manager.video_name";
    public static final String FILE_NAME = "com.coach.test.coachmw.manager.file_name";
    public static final String CHK_SAVE = "com.coach.test.coachmw.manager.chk_save";
    public static final String CHK_DEBUG_PLAY = "com.coach.test.coachmw.manager.chk_debug_play";
    public static final String PACKAGE = "com.coach.test.coachmw.manager";

    private static final String version = "1.1.8.13";

    private static final int PHASE_0 = 0;
    private static final int PHASE_1 = 10;
    private static final int PHASE_2 = 30;
    private static final int PHASE_3 = 50;
    private static final int PHASE_4 = 70;
    private static final int PHASE_5 = 90;

    private static final String name_v_1_1 = "Rolling like a Ball";
    private static final String name_v_1_8 = "Open Leg Rocker";
    private static final String name_v_1_3 = "Double Leg Pull";
    private static final String name_v_1_6 = "Criss Cross";
    private static final String name_v_1_16 = "Swimmings";

    private static final String name_v_2_1 = "Side Step Jacks";
    private static final String name_v_2_2 = "Criss Cross Squats";
    private static final String name_v_2_3 = "Curtsy Lunges";
    private static final String name_v_2_4 = "Alteration Toe Tab Squats";
    private static final String name_v_2_6 = "Squat Hops";

    private static final String name_v_3_1 = "점프하며 손발털기";
    private static final String name_v_3_2 = "팔굽혀펴기";
    private static final String name_v_3_3 = "제자리 높이뛰기";
    private static final String name_v_3_4 = "스쿼트";
    private static final String name_v_3_5 = "제자리 걷기";
    private static final String name_v_3_6 = "크런치";
    private static final String name_v_3_7 = "사이드킥";
    private static final String name_v_3_8 = "사이드런지";
    private static final String name_v_3_9 = "팔벌려 뛰기";
    private static final String name_v_3_10 = "등배운동";

    /// 일본어 추가 String///
    private static final String name_v_3_1_J = "ジャンプしながら手足を払う";
    private static final String name_v_3_2_J = "プッシュ・アップ";
    private static final String name_v_3_3_J = "立ち高飛び";
    private static final String name_v_3_4_J = "スクワット";
    private static final String name_v_3_5_J = "元歩き";
    private static final String name_v_3_6_J = "クランチ";
    private static final String name_v_3_7_J = "サイドキック";
    private static final String name_v_3_8_J = "サイドロンジ";
    private static final String name_v_3_9_J = "ジャンピングジャック";
    private static final String name_v_3_10_J = "背中運動";
    /// 까지
    private static final String name_v_3_1_EN = "Jump and Waist Ankle Circle";
    private static final String name_v_3_2_EN = "Push-up";
    private static final String name_v_3_3_EN = "Standing high jump";
    private static final String name_v_3_4_EN = "Squat";
    private static final String name_v_3_5_EN = "Standing walking";
    private static final String name_v_3_6_EN = "Crunches";
    private static final String name_v_3_7_EN = "Side Kick";
    private static final String name_v_3_8_EN = "Side Lunges";
    private static final String name_v_3_9_EN = "Jumping Jacks";
    private static final String name_v_3_10_EN = "Abs and back workout";

    private static final String name_v_4_1 = "Jumping Jacks";
    private static final String name_v_4_2 = "Arm Walking";
    private static final String name_v_4_3 = "High Knee Freeze";
    private static final String name_v_4_4 = "Hip Bridge";
    private static final String name_v_4_5 = "Squats";
    private static final String name_v_4_6 = "Push Up";
    private static final String name_v_4_7 = "Heel Touch";
    private static final String name_v_4_8 = "Reverse Lunge";
    private static final String name_v_4_9 = "Sky Reach";
    private static final String name_v_4_10 = "Sumo Walks";
    // private static final String name_v_4_11 = "";
    private static final String name_v_5_1 = "Jumping Jacks";
    private static final String name_v_5_2 = "Spiderman Climb with a Reach";
    private static final String name_v_5_3 = "Reaching Oblique Crunch";
    private static final String name_v_5_4 = "Pushup Mountain Climber Combo";
    private static final String name_v_5_5 = "Double Dip Squat";
    private static final String name_v_5_6 = "T Push Up";
    private static final String name_v_5_7 = "Russian Twist";
    private static final String name_v_5_8 = "Front Lunge";
    private static final String name_v_5_9 = "Superman Back Extension";
    private static final String name_v_5_10 = "Side Crunch";

    private static final String name_v_6_1 = "Wide Squat Plie";
    private static final String name_v_6_2 = "Roll up";
    private static final String name_v_6_3 = "Double leg circle";
    private static final String name_v_6_4 = "C Curved Crunch";
    private static final String name_v_6_5 = "Heel Touch";
    private static final String name_v_6_6 = "Criss Cross";
    private static final String name_v_6_7 = "Oblique Side band";
    private static final String name_v_6_8 = "O Balance";
    private static final String name_v_6_9 = "Triangle Holding";
    private static final String name_v_6_10 = "Plank Push Up";

    private static final String name_v_7_1 = "Ballerina Wide Squat";
    private static final String name_v_7_2 = "Hip Lifting";
    private static final String name_v_7_3 = "Single Leg Bridge";
    private static final String name_v_7_4 = "Back Extention";
    private static final String name_v_7_5 = "Swimming";
    private static final String name_v_7_6 = "Single Leg Circle";
    private static final String name_v_7_7 = "Double Leg Kick";
    private static final String name_v_7_8 = "Stand Hip Extension";
    private static final String name_v_7_9 = "Lunge";
    private static final String name_v_7_10 = "Hug A Tree Squat";

    private static final String name_v_9_1 = "Roll Up Down"; // 출산부 2-1
    private static final String name_v_9_2 = "Hip Kick"; // 출산부 2-2
    private static final String name_v_9_3 = "Hack Squat"; // 출산부 2-3
    private static final String name_v_9_4 = "Deep Lunge"; // 출산부 2-4
    private static final String name_v_9_5 = "Lunge Kick"; // 출산부 2-5
    private static final String name_v_9_6 = "Jump Squat"; // 출산부 2-7
    private static final String name_v_9_7 = "Boat"; // 출산부 2-8
    private static final String name_v_9_8 = "Flying"; // 출산부 2-9
    private static final String name_v_9_9 = "Lunge Press"; // 출산부 2-10
    private static final String name_v_9_10 = "Hyper Extension"; // 출산부 2-11
    private static final String name_v_9_11 = "Step Squat"; // 출산부 2-12
    private static final String name_v_9_12 = "Lift Twist"; // 출산부 2-13

    private static final String name_v_8_1 = "Side Stretching"; // 출산부 1-1
    private static final String name_v_8_2 = "Spine Twist"; // 출산부 1-2
    private static final String name_v_8_3 = "Chin Up"; // 출산부 1-3
    private static final String name_v_8_4 = "Side Crunch"; // 출산부 1-4
    private static final String name_v_8_5 = "Roll Up Down"; // 출산부 1-5
    private static final String name_v_8_6 = "Cat Pose"; // 출산부 1-6
    private static final String name_v_8_7 = "Lift Twist"; // 출산부 1-7
    private static final String name_v_8_8 = "Wide Squat"; // 출산부 1-8
    private static final String name_v_8_9 = "Body Balance"; // 출산부 1-9
    private static final String name_v_8_10 = "Flying"; // 출산부 1-10
    private static final String name_v_8_11 = "Bridge"; // 출산부 1-11

    private static final String description_v_1_1 = "몸을 최대한 웅크리고 앞뒤로 움직여주세요";
    private static final String description_v_1_8 = "무릎은 구부리지 말고 45도 정도만 움직여주세요";
    private static final String description_v_1_3 = "팔과 다리를 최대한 크게 움직여주세요";
    private static final String description_v_1_6 = "어깨는 고정시키고 몸통만 회전시켜주세요";
    private static final String description_v_1_16 = "팔과 다리를 최대한 빠르게 움직여주세요";

    private static final String description_v_2_1 = "사이드 스텝을 경쾌하게 해주세요";
    private static final String description_v_2_2 = "크로스 스텝을 경쾌하게 하시고 무릎이 90도가 되게 앉아주세요";
    private static final String description_v_2_3 = "뒷다리를 대각선으로 최대한 뒤로 빼주면서 무릎을 구부려주세요";
    private static final String description_v_2_4 = "무릎을 90도가 되도록 구부리면서 손으로 엄지발가락을 찍어주세요";
    private static final String description_v_2_6 = "바닥에서 10cm정도 뛰고 무릎이 90도가 되게 앉아주세요";

    private static final String description_v_3_1 = "손과 발을 가볍게 풀어주세요";
    private static final String description_v_3_2 = "무릎을 꿇고 팔을 굽혔다 펴 주세요";
    private static final String description_v_3_3 = "제자리에서 무릎과 팔꿈치를 90도로 들면서 뛰어주세요";
    private static final String description_v_3_4 = "무릎이 발 앞으로 나가지 않도록 등과 허리를 편 자세로 앉아 주세요";
    private static final String description_v_3_5 = "제자리에서 무릎과 팔꿈치를 90도로 들면서 걸어주세요";
    private static final String description_v_3_6 = "팔꿈치를 들고 윗몸을 살짝 일으켜 주세요";
    private static final String description_v_3_7 = "양발을 번갈아 옆으로 들면서 양손을 모아 위로 올려주세요";
    private static final String description_v_3_8 = "양손을 허리에 둔 채 다리를 대각선으로 최대한 옆으로 내밀면서 무릎을 구부려주세요";
    private static final String description_v_3_9 = "양팔을 벌렸다 제자리로 돌아와 주세요";
    private static final String description_v_3_10 = "하체는 고정한 채 양팔과 상체를 들어 주세요";

    private static final String description_v_3_1_EN = "Simply stretch your hands and feet";
    private static final String description_v_3_2_EN = "Kneel and try to do a push-up.";
    private static final String description_v_3_3_EN = "Try to jump with knees and elbow to 90 degrees while you are standing.";
    private static final String description_v_3_4_EN = "Try not to push forward to knee than the feet while you are sitting with your back straight.";
    private static final String description_v_3_5_EN = "Try to walk with knees and elbow to 90 degrees while you are standing";
    private static final String description_v_3_6_EN = "Move your upper- body slightly while holding up your elbows";
    private static final String description_v_3_7_EN = "Move your leg by using side-up by one by one with your hand-holding up";
    private static final String description_v_3_8_EN = "While putting your hand to your waist, try to move your legs crossed with bending your knees";
    private static final String description_v_3_9_EN = "Move your two arms up and down";
    private static final String description_v_3_10_EN = "Don't move your lower body  while moving your two arms and upper body";

    /// 일본어 추가 String///
    private static final String description_v_1_1_J = "体を最大限縮めて前後に動かしてください";
    private static final String description_v_1_8_J = "膝は曲げないで45度程度だけ動いてください";
    private static final String description_v_1_3_J = "腕と足を最大限大きく動いてください";
    private static final String description_v_1_6_J = "肩は固定させて胴体だけ回転させてください";
    private static final String description_v_1_16_J = "腕と足を最大早く動いてください";
    private static final String description_v_2_1_J = "サイドステップを軽くしてください";
    private static final String description_v_2_2_J = "クロスステップを軽くしながら膝が90度になるように座ってください";
    private static final String description_v_2_3_J = "後ろ足を対角線で最大後ろに出し、膝を曲げてください";
    private static final String description_v_2_4_J = "膝を90度になるように曲げ、手で親指をタッチしてください";
    private static final String description_v_2_6_J = "床から10cmほどのジュンプしながら、膝が90度になるように座ってください";
    private static final String description_v_3_1_J = "手と足を軽くストレッチングしてください";
    private static final String description_v_3_2_J = "ひざまずいて腕を曲げたり伸ばしてください";
    private static final String description_v_3_3_J = "膝と肘を90度にあげながら走ってください";
    private static final String description_v_3_4_J = "膝が足の前に出ないように背中と腰を展開した姿勢で座ってください";
    private static final String description_v_3_5_J = "膝と肘を90度に挙げながら歩いてください";
    private static final String description_v_3_6_J = "ひじを持って上体を少し起こしてください";
    private static final String description_v_3_7_J = "両足を交代に横にあげながら、両手を合わせて上にあげてください";
    private static final String description_v_3_8_J = "両手を腰に置いたまま、足を対角線で最大横に出しながら膝を曲げてください";
    private static final String description_v_3_9_J = "両腕を広げてから、まとに戻ってください。";
    private static final String description_v_3_10_J = "下半身は固定したまま両腕と上体をあげてください";

    /// 까지

    /* New Comment */
    private static final String[] comment_E = { "좋아요 잘하고 있어요", "아주 좋아요 이 상태로 유지해주세요", "훌륭해요 아주 잘하고 계시는데요", "아주 잘하시네요 조금만 더 힘내주세요" };
    private static final String comment_PASS = "PASS:정확도80% 이상 달성";
    private static final String comment_BAD = "동작이 정확하지 않습니다";
    private static final String comment_MOVE = "조금만 더 힘내주세요";
    private static final String comment_REST = "무리하지 마시고 휴식을 취해주세요";

    /// 일본어 추가 String///
    private static final String[] comment_F = new String[] { "いいです", "とてもいいんです、維持してください", "今のように維持してください", "とても上手ですね、もっと頑張ってください" };

    private static final String comment_BAD_J = "動作が正確ではないのでカウントされません";
    private static final String comment_MOVE_J = "もう少し頑張って";
    private static final String comment_REST_J = "無理しないで休息をとってください";
    /// 까지
    private static final String[] comment_G = new String[] { "Your doing well", "Very good try to focus more", "Excellent your really good at it", "well done try to push more power" };

    private static final String comment_BAD_EN = "The movement is not accurate, the points will not be counted";
    private static final String comment_MOVE_EN = "Try to focus more";
    private static final String comment_REST_EN = "Do not get a hurry and take a rest";

    // 총점 코멘트.
    private final class CommentSection {
        private final class Korea {
            private static final String ALL_100 = "정확도 횟수 모두 완벽해요. 정말 좋아요!";
            private static final String ALL_90_OVER = "아주 좋아요! 현재 상태를 유지해주세요.";
            private static final String ALL_90_60_BETWEEN = "횟수와 정확도 모두 나쁘지 않아요. 조금만 더 자세를 연습하시면 더욱 좋아질 거에요!";
            private static final String ALL_60_30_BETWEEN = "꾸준한 연습과 반복은 운동 능력 향상뿐만 아니라 몸매의 변화도 가지고 와요. 힘드시겠지만 조금만 더 힘내시고 화이팅 하세요!";
            private static final String ALL_30_UNDER = "혹시 오늘 컨디션이 안 좋으시거나 이번 세트는 운동을 하지 않으셨나요? 몸이 너무 지치고 힘든 날은 운동도 좋지만, 약간의 휴식을 취해 보는 것은 어떨까요?";
            private static final String ALL_10_UNDER = "이번 세트는 쉬셨군요!\n다음 세트는 힘내서 다시 해보아요!";

            private static final String ACC_90_OVER_COUNT_60_UNDER = "자세는 정확한데 횟수를 못 채우셨군요. 자세가 정확한 것은 아주 좋은 것입니다. 횟수를 조금씩 늘려 보세요!";
            private static final String ACC_90_OVER_COUNT_90_60_BETWEEN = "거의 다 왔어요! 한두 개만 더 늘려보세요!";
            private static final String ACC_90_OVER_COUNT_20_UNDER = "정확도가 정말 좋은데 횟수가 너무 적어요. 반복된 연습을 통하여 체력을 길러보아요.";

            private static final String ACC_90_60_BETWEEN_COUNT_60_30_BETWEEN = "정확도 횟수 둘 다 나쁘지 않아요. 하지만 조금 더 연습이 필요할 것 같아요.";
            private static final String ACC_90_60_BETWEEN_COUNT_30_UNDER = "정확도는 나쁘지 않습니다.\n조금 화이팅해서 횟수를 늘려볼까요?";
            private static final String ACC_60_UNDER_COUNT_30_UNDER = "정확도가 횟수보다 약간 좋네요. 점점 좋아지고 있으니 조금 더 힘내보아요.";
            private static final String ACC_30_10_BETWEEN_COUNT_10_UNDER = ALL_30_UNDER;

            private static final String COUNT_90_OVER_ACC_60_UNDER = "횟수는 채우셨는데 동작이 정확하지 않아요.\n반복된 연습을 통하여 정확한 자세를\n만들어 보아요.";
            private static final String COUNT_90_OVER_ACC_90_60_BETWEEN = "좋아지고 있어요! 조금만 더 정확한 자세를\n취하면 더욱 나은 효과를 보실 수 있어요.";
            private static final String COUNT_90_OVER_ACC_20_UNDER = "너무 단순하게 따라만 하고 있어요!\n조금 더 정확한 자세를 연습할 필요가 있어요!";

            private static final String COUNT_90_60_BETWEEN_ACC_60_30_BETWEEN = ACC_90_60_BETWEEN_COUNT_60_30_BETWEEN;
            private static final String COUNT_90_60_BETWEEN_ACC_30_UNDER = "횟수는 나쁘지 않습니다.\n조금 더 정확하게 따라 해보는 것은 어떨까요?";
            private static final String COUNT_60_UNDER_ACC_30_UNDER = ACC_60_UNDER_COUNT_30_UNDER;
            private static final String COUNT_30_10_BETWEEN_ACC_10_UNDER = ALL_30_UNDER;
        }

        private final class Japan {
            private static final String ALL_100 = "正確度回数全て完璧です。";
            private static final String ALL_90_OVER = "とても良いです!\n現在の状態を維持してください。";
            private static final String ALL_90_60_BETWEEN = "回数と正確度も悪くないです。\n回数と正確度も悪くないです。";
            private static final String ALL_60_30_BETWEEN = "練習と反復は運動能力向上だけでなくスタイルのよくなります。難しいですけどもうちょっと頑張ってください!";
            private static final String ALL_30_UNDER = "今日は コンディションが良くないですか？ 体の調子が悪い日には運動もいいですけど、若干の休息を取ってみるのはどうですか？";
            private static final String ALL_10_UNDER = "今回のセットは休みましたね!\n次のセットは頑張って運動してください!";

            private static final String ACC_90_OVER_COUNT_60_UNDER = "姿勢は正確ですが、回数が足りないですね。姿勢が正確なのはとても良いことです。回数を少しずつ増やしてみてください!";
            private static final String ACC_90_OVER_COUNT_90_60_BETWEEN = "もう少しです！１,２回もっとしてみてください";
            private static final String ACC_90_OVER_COUNT_20_UNDER = "正確度は良いですけど回数が足りないです。練習を通じて体力を育ててみてください。";

            private static final String ACC_90_60_BETWEEN_COUNT_60_30_BETWEEN = "正確度回数両方とも悪くないです。しかし、もうちょっと練習が必要です。";
            private static final String ACC_90_60_BETWEEN_COUNT_30_UNDER = "正確度は悪くないです。\nもう少し頑張って回数を増やしてみましょうか。";
            private static final String ACC_60_UNDER_COUNT_30_UNDER = "正確度が回数より少し良いですね。\n段々良くなっているからもう少し頑張ってみてください。";
            private static final String ACC_30_10_BETWEEN_COUNT_10_UNDER = ALL_30_UNDER;

            private static final String COUNT_90_OVER_ACC_60_UNDER = "回数は十分ですが動作が正しくありません。 練習を通じて正確な姿勢を作ってみてください。";
            private static final String COUNT_90_OVER_ACC_90_60_BETWEEN = "良くなっています！もう少し正確な姿勢で運動をすると効果を見ることができます。";
            private static final String COUNT_90_OVER_ACC_20_UNDER = "単純に運動しています！\nもう少し正確な姿勢を練習する必要があります!";

            private static final String COUNT_90_60_BETWEEN_ACC_60_30_BETWEEN = ACC_90_60_BETWEEN_COUNT_60_30_BETWEEN;
            private static final String COUNT_90_60_BETWEEN_ACC_30_UNDER = "回数は悪くないです。\nもう少し正確にして見るのはどうですか?";
            private static final String COUNT_60_UNDER_ACC_30_UNDER = ACC_60_UNDER_COUNT_30_UNDER;
            private static final String COUNT_30_10_BETWEEN_ACC_10_UNDER = ALL_30_UNDER;
        }

        private final class English {
            private static final String ALL_100 = "The accuracy and number are all perfect. Excellent";
            private static final String ALL_90_OVER = "Very good, try to stand on your position";
            private static final String ALL_90_60_BETWEEN = "The accuracy and number are not bad, if you keep trying, you will be much better";
            private static final String ALL_60_30_BETWEEN = "Practice makes perfect! Recurring exercise would build your ability and make your body beautiful. The first step is always hard to do! Let us try";
            private static final String ALL_30_UNDER = "Not every day is for you to exercise, maybe you did not do the sets of the exercise? If you don’t feel well on doing exercise, why don’t you take a little bit of resting?";
            private static final String ALL_10_UNDER = "You didn’t do this sets! Why don’t we focus on the new sets?";

            private static final String ACC_90_OVER_COUNT_60_UNDER = "The accuracy is perfect, but it was very close to fulfill the number . posture is the most important part while doing exercise. Try to more focus on the number";
            private static final String ACC_90_OVER_COUNT_90_60_BETWEEN = "We are almost finished, let’s focus!!";
            private static final String ACC_90_OVER_COUNT_20_UNDER = "The accuracy is perfect enough, but the number is so less. Let us practice more and strength the body";

            private static final String ACC_90_60_BETWEEN_COUNT_60_30_BETWEEN = "Accuracy and number are not bad. But try to do more practice";
            private static final String ACC_90_60_BETWEEN_COUNT_30_UNDER = "The Accuracy is not that bad. Why don’t we more focus on the number?";
            private static final String ACC_60_UNDER_COUNT_30_UNDER = "The number is little bit  weaker than accuracy. You’re doing very good, why don’t you try more?";
            private static final String ACC_30_10_BETWEEN_COUNT_10_UNDER = ALL_30_UNDER;

            private static final String COUNT_90_OVER_ACC_60_UNDER = "You have done all of the number of exercises, but the accuracy are not perfect. Try to be careful on making more perfect accuracy";
            private static final String COUNT_90_OVER_ACC_90_60_BETWEEN = "Getting better, if you try more on the accuracy that you will get much better results";
            private static final String COUNT_90_OVER_ACC_20_UNDER = "You are not focusing on the posture, try to do with accurate posture while you’re doing exercise";

            private static final String COUNT_90_60_BETWEEN_ACC_60_30_BETWEEN = ACC_90_60_BETWEEN_COUNT_60_30_BETWEEN;
            private static final String COUNT_90_60_BETWEEN_ACC_30_UNDER = "The number is not bad. Why don’t you focus more on the accuracy?";
            private static final String COUNT_60_UNDER_ACC_30_UNDER = ACC_60_UNDER_COUNT_30_UNDER;
            private static final String COUNT_30_10_BETWEEN_ACC_10_UNDER = ALL_30_UNDER;
        }
    }

    // 필라테스 - 매트운동
    // Rolling Like a Ball
    private static final String comment_GOOD_1_1 = "조금 더 크게 굴러주세요";
    private static final String comment_GREAT_1_1 = "좋아요 잘하고 있어요";
    // Open Leg Rocker
    private static final String comment_GOOD_1_8 = "조금 더 크게 굴러주세요";
    private static final String comment_GREAT_1_8 = "좋아요 아주 잘 따라 하고 있어요";
    // Double Leg Pull
    private static final String comment_GOOD_1_3 = "팔과 다리를 쭉 펴주세요";
    private static final String comment_GREAT_1_3 = "아주 좋아요 잘하고 있어요";
    // Criss Cross
    private static final String comment_GOOD_1_6 = "팔꿈치와 무릎이 닿는다는 느낌으로 해주세요";
    private static final String comment_GREAT_1_6 = "좋아요 운동을 확실하게 하고 있어요";
    // Swimmings
    private static final String comment_GOOD_1_16 = "좀 더 높이 들며, 빠르게 해주세요";
    private static final String comment_GREAT_1_16 = "아주 좋아요 이 상태로 유지해주세요";

    // 필라테스 - 액티브운동
    // Side Step Jacks
    private static final String comment_GOOD_2_1 = "조금 더 경쾌하게 스텝을 밟으며 정확히 따라 해주세요";
    private static final String comment_GREAT_2_1 = "좋아요 잘하고 있어요";
    // CrissCross Squarte
    private static final String comment_GOOD_2_2 = "스텝을 경쾌하게 밟으며 최대한 많이 내려가주세요";
    private static final String comment_GREAT_2_2 = "훌륭해요 아주 잘하시는데요";
    // Quray Lunges
    private static final String comment_GOOD_2_3 = "팔 동작을 부드럽게 하고 더 멀리 점프해주세요";
    private static final String comment_GREAT_2_3 = "좋아요 잘 따라 하고 있어요";
    // Alt Toe Tab Squats
    private static final String comment_GOOD_2_4 = "손으로 발등을 터치하면서, 더 깊이 앉아주세요";
    private static final String comment_GREAT_2_4 = "아주 좋아요 잘 따라 하고 있어요";
    // Squart Hops
    private static final String comment_GOOD_2_6 = "조금만 더 높이 뛰어주세요";
    private static final String comment_GREAT_2_6 = "좋은 자세에요 훌륭해요";

    // 홍두한 1
    private static final String comment_GOOD_4_1 = "트레이너의 템포에 맞춰 동작을 더 크게 해주세요";
    private static final String comment_GREAT_4_1 = "좋아요 잘하고 있어요";

    private static final String comment_GOOD_4_2 = "상체의 힘을 이용하여 더 멀리 뻗어주세요";
    private static final String comment_GREAT_4_2 = "아주 잘하시네요 조금만 더 힘내주세요";

    private static final String comment_GOOD_4_3 = "속도와 몸의 균형에 집중해주세요";
    private static final String comment_GREAT_4_3 = "아주 좋아요 이 상태로 유지해주세요";

    private static final String comment_GOOD_4_4 = "엉덩이와 복부에 집중하여 올라와주세요";
    private static final String comment_GREAT_4_4 = "좋은 자세에요 조금만 더 유지해주세요";

    private static final String comment_GOOD_4_5 = "천천히 정확하게 내려가주세요";
    private static final String comment_GREAT_4_5 = "훌륭해요 잘 따라 하고 있어요";

    private static final String comment_GOOD_4_6 = "팔을 높이 들었다 내리며, 상체를 더 힘차게 올려주세요";
    private static final String comment_GREAT_4_6 = "아주 좋아요 잘하고 있어요";

    private static final String comment_GOOD_4_7 = "어깨를 고정한 상태에서 복부의 힘을 이용해 좌우로 손을 뒤꿈치에 대주세요";
    private static final String comment_GREAT_4_7 = "좋아요 잘 따라 하고 있어요";

    private static final String comment_GOOD_4_8 = "코치 동작에 맞춰 다리를 뒤로 뻗고 최대한 내려가주세요";
    private static final String comment_GREAT_4_8 = "아주 좋아요 잘하고 있어요";

    private static final String comment_GOOD_4_9 = "복부의 힘으로 팔을 더 높이 올려 발끝을 터치해주세요";
    private static final String comment_GREAT_4_9 = "훌륭해요 잘 따라 하고 있어요";

    private static final String comment_GOOD_4_10 = "무릎 각도를 90도로 유지해주세요";
    private static final String comment_GREAT_4_10 = "아주 좋아요 운동을 확실하게 하고 있네요";

    // 홍두한 2
    private static final String comment_GOOD_5_1 = "트레이너의 템포에 맞춰 동작을 더 크게 해주세요";
    private static final String comment_GREAT_5_1 = "좋아요 아주 잘하고 있어요";

    private static final String comment_GOOD_5_2 = "허리를 돌리며 팔을 쭉 뻗어주세요";
    private static final String comment_GREAT_5_2 = "아주 좋아요 이 상태로 유지해주세요";

    private static final String comment_GOOD_5_3 = "복부의 힘을 더 주며, 손을 앞으로 쭉 뻗어주세요";
    private static final String comment_GREAT_5_3 = "좋아요 아주 잘하고 있어요";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_5_4 = "팔을 더 힘차게 들어 올리고 무릎을 더 당겨주세요";
    private static final String comment_GREAT_5_4 = "좋아요 이 상태로 유지해주세요";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_5_5 = "팔 동작과 스쿼트를 정확히 따라 해주세요";
    private static final String comment_GREAT_5_5 = "좋은 자세에요 조금만 더 유지해주세요";
    private static final String comment_SOSO_5_5 = "엉덩이와 허벅지의 힘을 이용해주세요";

    private static final String comment_GOOD_5_6 = "푸쉬업 동작을 정확히 한 후, 팔을 힘껏 올려주세요";
    private static final String comment_GREAT_5_6 = "아주 좋아요 잘하고 있어요";

    private static final String comment_GOOD_5_7 = "복부의 힘을 이용하여, 천천히 몸을 비틀어주세요";
    private static final String comment_GREAT_5_7 = "아주 좋아요 이대로 유지해주세요";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_5_8 = "팔 동작을 정확히 하면서 깊숙히 내려가 주세요";
    private static final String comment_GREAT_5_8 = "아주 좋아요 잘하고 있어요";

    private static final String comment_GOOD_5_9 = "팔과 다리를 높이 들어 버텨주세요";
    private static final String comment_GREAT_5_9 = "좋아요 운동을 확실하게 하고 있네요";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_5_10 = "상체를 더 들어 올려주세요";
    private static final String comment_GREAT_5_10 = "좋아요 아주 잘하고 있어요";

    // 이주영 1
    private static final String comment_GOOD_6_1 = "동작을 더 크게 하며, 정확하게 따라 해주세요";
    private static final String comment_GREAT_6_1 = "좋아요 잘하고 있어요";

    private static final String comment_GOOD_6_2 = "더욱 천천히 내려가주세요";
    private static final String comment_GREAT_6_2 = "좋아요 아주 잘 따라 하고 있어요";

    private static final String comment_GOOD_6_3 = "팔과 다리를 최대한 넓게 펴주세요";
    private static final String comment_GREAT_6_3 = "아주 좋아요 그대로 유지해주세요";

    private static final String comment_GOOD_6_4 = "복부의 힘을 주어 천천히 내려갔다, 올라와주세요";
    private static final String comment_GREAT_6_4 = "좋아요 잘 따라 하고 있어요";

    private static final String comment_GOOD_6_5 = "어깨를 고정한 상태로 손을 뒤꿈치에 대주세요";
    private static final String comment_GREAT_6_5 = "좋아요 아주 잘하고 있어요";

    private static final String comment_GOOD_6_6 = "팔꿈치와 무릎이 닿는다는 느낌으로 해주세요";
    private static final String comment_GREAT_6_6 = "훌륭해요 잘 따라 하고 있어요";

    private static final String comment_GOOD_6_7 = "천천히 복부로 버티며 팔 동작을 실시해주세요";
    private static final String comment_GREAT_6_7 = "좋아요 아주 잘하시는데요";

    private static final String comment_GOOD_6_8 = "복부에 힘을 주며, 팔을 크게 돌려주세요";
    private static final String comment_GREAT_6_8 = "좋아요 아주 잘 따라 하고 있어요";

    private static final String comment_GOOD_6_9 = "복부의 힘으로 조금 더 버텨주세요";
    private static final String comment_GREAT_6_9 = "훌륭해요 아주 잘하고 있어요";

    private static final String comment_GOOD_6_10 = "복부의 힘을 유지하며, 정확히 따라 해주세요";
    private static final String comment_GREAT_6_10 = "아주 좋아요 지금처럼 해주세요";

    // 이주영 2
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_7_1 = "동작을 더 크게 하며, 정확하게 따라 해주세요";
    private static final String comment_GREAT_7_1 = "좋아요 잘하고 있어요";

    private static final String comment_GOOD_7_2 = "팔, 상체, 다리가 일자가 되게 해주세요";
    private static final String comment_GREAT_7_2 = "훌륭해요 아주 잘하시는데요";

    private static final String comment_GOOD_7_3 = "팔, 상체, 다리가 일자가 되게 해주세요";
    private static final String comment_GREAT_7_3 = "좋아요 아주 잘하고 있어요";

    private static final String comment_GOOD_7_4 = "팔과 다리를 더 높이 들어주세요";
    private static final String comment_GREAT_7_4 = "아주 좋아요 잘하고 있어요";

    private static final String comment_GOOD_7_5 = "허리와 엉덩이에 힘을 준 상태로 팔을 크게 움직여주세요";
    private static final String comment_GREAT_7_5 = "좋아요 아주 잘하고 있어요";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_7_6 = "팔 동작을 정확하게 따라 해주세요";
    private static final String comment_GREAT_7_6 = "훌륭해요 아주 잘하시는데요";

    private static final String comment_GOOD_7_7 = "허리에 힘을 유지한 상태로 팔과 다리를 쭉 뻗어주세요";
    private static final String comment_GREAT_7_7 = "좋아요 아주 잘 따라 하고 있어요";

    private static final String comment_GOOD_7_8 = "상체를 곧게 피고, 동작을 크게 해주세요";
    private static final String comment_GREAT_7_8 = "좋아요 아주 잘하고 있어요";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_7_9 = "팔 동작을 정확하게 더 깊숙히 내려가주세요";
    private static final String comment_GREAT_7_9 = "좋아요 잘 따라 하고 있어요";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_7_10 = "팔 동작을 크게 하고 더 깊숙히 앉아주세요";
    private static final String comment_GREAT_7_10 = "아주 좋아요 지금처럼 해주세요";

    // 출산부 코멘트. 정리 필요.

    // 2-1번
    private static final String comment_BAD_9_1 = "양손이 발끝에 닿는다는 느낌으로 내려가주세요";
    private static final String comment_GOOD_9_1 = "상체를 정확하게 펴주세요";
    private static final String comment_GREAT_9_1 = "아주 좋아요 유지해주세요";
    // 2-2번
    private static final String comment_BAD_9_2 = "코치와 같이 손을 확실하게 뻗어주세요";
    private static final String comment_GOOD_9_2 = "두 손을 최대한 올리며 몸이 일자가 되게 해주세요";
    private static final String comment_GREAT_9_2 = "아주 좋아요 잘하고 있어요";
    // 2-3번
    private static final String comment_BAD_9_3 = "천천히 앉으며 엉덩이를 의자에 터치하듯 일어나주세요";
    private static final String comment_GRAET_9_3 = "좋아요 아주 잘 따라 하고 있어요";
    // 2-4번
    private static final String comment_BAD_9_4 = "무릎이 'ㄱ'자가 되게 내려가주시고 올라올 때는 상체와 엉덩이에 힘을주어 올라와주세요";
    private static final String comment_GREAT_9_4 = "훌륭해요 잘하고 있어요";
    // 2-5번
    private static final String comment_BAD_9_5 = "엉덩이와 허벅지 힘으로 올라가주세요";
    private static final String comment_GREAT_9_5 = "훌륭해요 잘하고 있어요";
    // 2-7번
    private static final String comment_BAD_9_6 = "더욱 높게 뛰어주세요";
    private static final String comment_GOOD_9_6 = "조금만 더 높이 뛰어주세요";
    private static final String comment_GREAT_9_6 = "아주 좋아요 지금처럼 해주세요";
    // 2-8번
    private static final String comment_BAD_9_7 = "팔을 더 곧게 뻗고 박자에 맞춰 따라 해주세요";
    private static final String comment_GREAT_9_7 = "좋아요 아주 잘 따라 하고 있어요";
    // 2-9번
    private static final String comment_BAD_9_8 = "박자를 맞추어 천천히 실행해 주세요";
    private static final String comment_GREAT_9_8 = "좋아요 아주 잘 하고 있어요";
    // 2-10번
    private static final String comment_BAD_9_9 = "코치와 같이 최대한 앉고 팔을 쭉 펴주세요";
    private static final String comment_GREAT_9_9 = "좋아요 아주 잘 따라 하고 있어요";
    // 2-11번
    private static final String comment_BAD_9_10 = "박자를 맞추어 천천히 실행해 주세요";
    private static final String comment_GREAT_9_10 = "훌륭해요 잘하고 있어요";
    // 2-12번
    private static final String comment_BAD_9_11 = "조금 더 깊게 내려가주세요";
    private static final String comment_GREAT_9_11 = "아주 좋아요 잘하고 있어요";
    // 2-13번
    private static final String comment_BAD_9_12 = "코치와 같이 팔을 쭉 뻗으며 몸을 돌려주세요";
    private static final String comment_GOOD_9_12 = "팔을 크게 돌려주세요";
    private static final String comment_GREAT_9_12 = "아주 좋아요 잘하고 있어요";

    // 1-1번
    private static final String comment_BAD_8_1 = "손끝이 발가락에 닿는다는 느낌으로 스트레칭 해주세요";
    private static final String comment_GREAT_8_1 = "좋아요 아주 잘 따라 하고 있어요";
    // 1-2번
    private static final String comment_GOOD_8_2_1 = "허리를 곧게 펴고 좌우로 스트레칭해주세요";
    private static final String comment_GOOD_8_2_2 = "양손을 곧게 피고 한쪽 손으로 발목을 터치한다는 느낌으로 스트레칭 해주세요";
    private static final String comment_GREAT_8_2 = "아주 잘하고 있어요 훌륭하네요";
    // 1-3번
    private static final String comment_BAD_8_3 = "조금 더 정확하게 올라와주세요";
    private static final String comment_GOOD_8_3 = "천천히 올바르게 따라 해주세요";
    private static final String comment_GREAT_8_3 = "좋아요 잘 따라 하고 있어요";
    // 1-4번
    private static final String comment_BAD_8_4 = "상체를 좀 더 들고 손을 앞으로 뻗어주세요";
    private static final String comment_GOOD_8_4 = "조금 더 빠르게 해주세요";
    private static final String comment_GREAT_8_4 = "좋아요 아주 잘 따라 하고 있어요";
    // 1-5번
    private static final String comment_BAD_8_5 = "더욱 천천히 내려가주세요";
    private static final String comment_GREAT_8_5 = "훌륭합니다 잘하고 있어요";
    // 1-6번
    private static final String comment_BAD_8_6 = "손바닥을 바닥에 대고, 어깨를 최대한 내려주세요";
    private static final String comment_GREAT_8_6 = "좋아요 잘 따라 하고 있어요";
    // 1-7번
    private static final String comment_BAD_8_7 = "허리 스트레칭을 정확하게 하고, 엉덩이를 뒤로 빼며 앞으로 숙여주세요";
    private static final String comment_GREAT_8_7 = "아주 좋아요 잘하고 있어요";
    // 1-8번
    private static final String comment_BAD_8_8 = "천천히 정확하게 내려가주세요";
    private static final String comment_GREAT_8_8 = "훌륭하네요 잘하고 있어요";
    // 1-9번
    private static final String comment_BAD_8_9 = "팔, 상체, 다리가 일자가 되게 해주세요";
    private static final String comment_GREAT_8_9 = "아주 좋아요 잘하고 있어요";
    // 1-10번
    private static final String comment_BAD_8_10 = "박자를 맞추어 천천히 실행해 주세요";
    private static final String comment_GREAT_8_10 = "아주 좋아요 잘하고 있어요";
    // 1-11번
    private static final String comment_BAD_8_11 = "손과 상체를 곧게 피고, 엉덩이를 최대한 올려주세요";
    private static final String comment_GREAT_8_11 = "아주 좋아요 잘하고 있어요";
    // 출산부 end...

    /// 15분 순황 최신수정 멘트 160328///
    private static final String comment_GOOD_3_1 = "동작을 크게 해주세요";
    private static final String comment_GOOD_3_2 = "팔을 좀 더 벌리고 내려가 주세요";
    private static final String comment_GOOD_3_3 = "손발을 더 크게 흔들어 주세요";
    private static final String comment_GOOD_3_4 = "자세를 좀 더 낮춰주세요";
    private static final String comment_GOOD_3_5 = "손발을 더 크게 흔들어 주세요";
    private static final String comment_GOOD_3_6 = "복부에 힘을 주어 상체를 더 들고, 자세를 유지해주세요";
    private static final String comment_GOOD_3_7 = "손과 발을 더 올려주세요";
    private static final String comment_GOOD_3_8 = "다리를 더 멀리 내디뎌 주세요";
    private static final String comment_GOOD_3_9 = "트레이너의 템포에 맞춰 동작을 더 크게 해주세요";
    private static final String comment_GOOD_3_10 = "상체를 더 들고, 자세를 유지해주세요";

    /// 일본어 추가String///
    /// 매트 운동 ///
    private static final String comment_GOOD_1_1_J = "もう少し大きく転んでてください";

    private static final String comment_GOOD_1_8_J = "もう少し大きく転んでてください";

    private static final String comment_GOOD_1_3_J = "腕と足をのばしてください";

    private static final String comment_GOOD_1_6_J = "肘と膝が触れるようにして下さい";

    private static final String comment_GOOD_1_16_J = "手足をよりあげて、はやく動作してください";

    /// 액티브 운동///
    private static final String comment_GOOD_2_1_J = "拍子に合わせて動作をより大きくしてください";
    private static final String comment_GREAT_2_1_J = "良くやっています";

    private static final String comment_GOOD_2_2_J = "拍子を合わせてもっと深く座ってください";
    private static final String comment_GREAT_2_2_J = "とてもいいですね";

    private static final String comment_GOOD_2_3_J = "腕の動作をソフトにしながら、もっと遠くジャンプしてください";
    private static final String comment_GREAT_2_3_J = "いいです、維持してください";

    private static final String comment_GOOD_2_4_J = "手で足元をタッチしながら、より深くの座ってください";
    private static final String comment_GREAT_2_4_J = "良くやっています";

    private static final String comment_GOOD_2_6_J = "もっと高くジャンプして下さい";
    private static final String comment_GREAT_2_6_J = "良くやっています";

    /// TBT 1///
    private static final String comment_GOOD_4_1_J = "拍子を合わせて動作をもっと大きくしてください";
    private static final String comment_GREAT_4_1_J = "良くやっています";

    private static final String comment_GOOD_4_2_J = "上体の力を利用してもっと伸びてください";
    private static final String comment_GREAT_4_2_J = "とてもいいです、もう少し頑張ってください";

    private static final String comment_GOOD_4_3_J = "速度と体のバランスに集中してください";
    private static final String comment_GREAT_4_3_J = "この状態で維持してください";

    private static final String comment_GOOD_4_4_J = "腕の動作を大きくし、腰をもっとあげてください";
    private static final String comment_GREAT_4_4_J = "良い姿勢です、維持してください";

    private static final String comment_GOOD_4_5_J = "ゆっくり、正確に下がってください";
    private static final String comment_GREAT_4_5_J = "良くやっています";

    private static final String comment_GOOD_4_6_J = "腕を高く持ち上げ下りながら、上体をより力強く上げてください";
    private static final String comment_GREAT_4_6_J = "良くやっています";

    private static final String comment_GOOD_4_7_J = "肩を固定した状態で腹部の力を利用して、左右に手をかかとにタッチして下さい";
    private static final String comment_GREAT_4_7_J = "良くやっています";

    private static final String comment_GOOD_4_8_J = "コーチの動作に合わせて足を後ろに伸ばして最大に下がってください";
    private static final String comment_GREAT_4_8_J = "良い姿勢です、維持してください";

    private static final String comment_GOOD_4_9_J = "腹筋の力で腕をもっと高く上げてつま先をタッチしてください";
    private static final String comment_GREAT_4_9_J = "良い姿勢です、維持してください";

    private static final String comment_GOOD_4_10_J = "膝の角度を90度で維持してください";
    private static final String comment_GREAT_4_10_J = "この状態で維持してください";

    /// TBT2///
    private static final String comment_GOOD_5_1_J = "拍子をあわせて、さらに大きくしてください";
    private static final String comment_GREAT_5_1_J = "良くやっています";

    private static final String comment_GOOD_5_2_J = "腰を回しながら腕を伸ばしてください";
    private static final String comment_GREAT_5_2_J = "良い姿勢です、維持してください";

    private static final String comment_GOOD_5_3_J = "腹部のさらに力を与え、手を前に伸ばしてください";
    private static final String comment_GREAT_5_3_J = "そのまま維持してください";

    private static final String comment_GOOD_5_4_J = "腕をもっと持ち上げながら膝をさらに引っ張ってください";
    private static final String comment_GREAT_5_4_J = "とても良くやっています";

    private static final String comment_GOOD_5_5_J = "腕の動作とスクワットを正確に実施してください";
    private static final String comment_GREAT_5_5_J = "良い姿勢です,維持してください";
    private static final String comment_SOSO_5_5_J = "ヒップと太ももの力を利用してください";

    private static final String comment_GOOD_5_6_J = "上体を力強く上げて腕をもっと高く上げてください";
    private static final String comment_GREAT_5_6_J = "とても良くやっています";

    private static final String comment_GOOD_5_7_J = "もっとゆっくり大きく体を捩ってください";
    private static final String comment_GREAT_5_7_J = "そのまま維持してください";

    private static final String comment_GOOD_5_8_J = "腕の動作を正確にし、もっと下がってください";
    private static final String comment_GREAT_5_8_J = "とても良くやっています";

    private static final String comment_GOOD_5_9_J = "手足をもっと高く上げてください";
    private static final String comment_GREAT_5_9_J = "そのまま維持してください";;

    private static final String comment_GOOD_5_10_J = "わき腹をもっと高く上げてください";
    private static final String comment_GREAT_5_10_J = "いいです、とても良くやっています";

    /// 이주영 초고속 복근 만들기///
    private static final String comment_GOOD_6_1_J = "腕の動作が大きく、正確に実施してください";
    private static final String comment_GREAT_6_1_J = "とても良くやっています";

    private static final String comment_GOOD_6_2_J = "もっとゆっくり下げてください";
    private static final String comment_GREAT_6_2_J = "とても良くやっています";

    private static final String comment_GOOD_6_3_J = "手足の動作をより大きくしてください";
    private static final String comment_GREAT_6_3_J = "いいです、そのまま維持してください";

    private static final String comment_GOOD_6_4_J = "腹部の力を入れてゆっくり実施して下さい";
    private static final String comment_GREAT_6_4_J = "いいです、そのまま維持してください";

    private static final String comment_GOOD_6_5_J = "肩を固定した状態で手をかかとをタッチして下さい";
    private static final String comment_GREAT_6_5_J = "そのまま維持してください";

    private static final String comment_GOOD_6_6_J = "肘と膝をタッチするように実施して下さい";
    private static final String comment_GREAT_6_6_J = "この状態で維持してください";

    private static final String comment_GOOD_6_7_J = "ゆっくり腹部で支え、腕の動作を実施してください";
    private static final String comment_GREAT_6_7_J = "いいです、とても上手ですね";

    private static final String comment_GOOD_6_8_J = "腹部に力を与えながら、両手を大きく回してください";
    private static final String comment_GREAT_6_8_J = "この状態で維持してください";

    private static final String comment_GOOD_6_9_J = "腹筋でもうちょっと堪えてください";
    private static final String comment_GREAT_6_9_J = "いいです、とても上手ですね";

    private static final String comment_GOOD_6_10_J = "腹部の力を維持し、正確に実施して下さい";
    private static final String comment_GREAT_6_10_J = "いいです、そのまま維持してください";

    /// 이주영 뒤태 만들기 운동///
    private static final String comment_GOOD_7_1_J = "腕動作をもっと大きく、正確に実施してください";
    private static final String comment_GREAT_7_1_J = "いいです、良くやっています";

    private static final String comment_GOOD_7_2_J = "腕、上体、足をまっすぐにして下さい";
    private static final String comment_GREAT_7_2_J = "とてもいいです";

    private static final String comment_GOOD_7_3_J = "腕、上体、足をまっすぐにして下さい";
    private static final String comment_GREAT_7_3_J = "そのまま維持してください";

    private static final String comment_GOOD_7_4_J = "腕と脚をもっと高くあげてください";
    private static final String comment_GREAT_7_4_J = "この状態で維持してください";

    private static final String comment_GOOD_7_5_J = "腰、ヒップに力を入れた状態で腕を大きく動かしてください";
    private static final String comment_GREAT_7_5_J = "そのまま維持してください";

    private static final String comment_GOOD_7_6_J = "腕の動作を正確に実施してください";
    private static final String comment_GREAT_7_6_J = "とてもいいです";

    private static final String comment_GOOD_7_7_J = "腰に力を維持した状態で腕と足を伸びて下さい";
    private static final String comment_GREAT_7_7_J = "そのまま維持してください";

    private static final String comment_GOOD_7_8_J = "腕の動作が大きく、力強くしてください";
    private static final String comment_GREAT_7_8_J = "この状態で維持してください";

    private static final String comment_GOOD_7_9_J = "腕の動作を正確にもっと下げてください";
    private static final String comment_GREAT_7_9_J = "いいです、良くやっています";

    private static final String comment_GOOD_7_10_J = "腕の動作を大きくしてもっと下げてださい";
    private static final String comment_GREAT_7_10_J = "そのまま維持してください";

    /// 15분 순환운동 160328///
    private static final String comment_GOOD_3_1_J = "動作を大きくしてください";
    private static final String comment_GOOD_3_2_J = "腕をもっと広げて下がってください";
    private static final String comment_v3_3_J = "両手足をもっと大きく振ってください";
    private static final String comment_v3_4_J = "もっと座ってください";
    private static final String comment_v3_5_J = "手足をさらに大きく振ってください";
    private static final String comment_v3_6_J = "腹部に力を入れて上体をもっと上げ、姿勢を維持してください\";";
    private static final String comment_v3_7_J = "手足をもっと上げてください";
    private static final String comment_v3_8_J = "脚をもっと遠くに踏み出してください";
    private static final String comment_v3_9_1_J = "トレーナーのテンポに合わせて動作をもっと大きくしてください";
    private static final String comment_v3_10_1_J = "上体をもっと上げて、姿勢を維持してください";

    // 출산부 코멘트(일본어 작성자 신상기)

    // 2-1번
    private static final String comment_BAD_9_1_J = "両手がつま先に触れる感じで下がってください";
    private static final String comment_GOOD_9_1_J = "上体を正確に伸ばしてください";
    private static final String comment_GREAT_9_1_J = "良いです。維持してください";
    // 2-2번
    private static final String comment_BAD_9_2_J = "トレーナーのように手を伸びてください";
    private static final String comment_GOOD_9_2_J = "両手を最大限引き上げながら体を伸びてください";
    private static final String comment_GREAT_9_2_J = "とても良いです！";
    // 2-3번
    private static final String comment_BAD_9_3_J = "ゆっくりと座ってヒップを椅子にタッチするように起きてください";
    private static final String comment_GRAET_9_3_J = "とても正しく実施しています";
    // 2-4번
    private static final String comment_BAD_9_4_J = "膝が９０度になるように下がってください。その後上半身とヒップに力を入れて上がってください";
    private static final String comment_GREAT_9_4_J = "とても正しく実施しています";
    // 2-5번
    private static final String comment_BAD_9_5_J = "ヒップと太ももの力で上がってください";
    private static final String comment_GREAT_9_5_J = "とても良いです！";
    // 2-7번
    private static final String comment_BAD_9_6_J = "もっと高くジャンプーしてください";
    private static final String comment_GOOD_9_6_J = "もう少し高くジャンプしてください";
    private static final String comment_GREAT_9_6_J = "良いです。今のように維持してください";
    // 2-8번
    private static final String comment_BAD_9_7_J = "腕をもっとまっすぐに伸びながら拍子合あわせて実施してください";
    private static final String comment_GREAT_9_7_J = "良いです。維持してください。";
    // 2-9번
    private static final String comment_BAD_9_8_J = "拍子をあわせてゆっくり実施してください";
    private static final String comment_GREAT_9_8_J = "とても良いです！";
    // 2-10번
    private static final String comment_BAD_9_9_J = "トレーナーのように最大に下がりながら手を伸びてください";
    private static final String comment_GREAT_9_9_J = "とても良いです！";
    // 2-11번
    private static final String comment_BAD_9_10_J = "拍子をあわせてゆっくり実施してください";
    private static final String comment_GREAT_9_10_J = "とても正しく実施しています";
    // 2-12번
    private static final String comment_BAD_9_11_J = "もう少し下がってください";
    private static final String comment_GREAT_9_11_J = "とても良いです！";
    // 2-13번
    private static final String comment_BAD_9_12_J = "トレーナーように腕を伸ばしながら体をまわしてください";
    private static final String comment_GOOD_9_12_J = "腕を大きく回してください";
    private static final String comment_GREAT_9_12_J = "とても正しく実施しています";

    // 1-1번
    private static final String comment_BAD_8_1_J = "指先が足指を触る感じでストレッチングしてください";
    private static final String comment_GREAT_8_1_J = "良いです、維持してください。";
    // 1-2번
    private static final String comment_GOOD_8_2_1_J = "腰をまっすぐにのばし左右にストレッチングしてください";
    private static final String comment_GOOD_8_2_2_J = "両手をまっすぐにのばし片方の手で足をタッチする感じでストレッチングしてください";
    private static final String comment_GREAT_8_2_J = "とても正しく実施しています";
    // 1-3번
    private static final String comment_BAD_8_3_J = "もう少し正確に上げてください";
    private static final String comment_GOOD_8_3_J = "ゆっくり正しく実行してください";
    private static final String comment_GREAT_8_3_J = "良いです。維持してください";
    // 1-4번
    private static final String comment_BAD_8_4_J = "上体をもっと上げて手を前に伸びてください";
    private static final String comment_GOOD_8_4_J = "もう少し速くしてください";
    private static final String comment_GREAT_8_4_J = "良いです。維持してください";
    // 1-5번
    private static final String comment_BAD_8_5_J = "もっとゆっくり下げてください";
    private static final String comment_GREAT_8_5_J = "とても正しく実施しています";
    // 1-6번
    private static final String comment_BAD_8_6_J = "手のひらを床に付けて、肩を最大限降ろしてください";
    private static final String comment_GREAT_8_6_J = "良いです、維持してください";
    // 1-7번
    private static final String comment_BAD_8_7_J = "手のひらを床に付けて、肩を最大限降ろしてください";
    private static final String comment_GREAT_8_7_J = "とても良いです";
    // 1-8번
    private static final String comment_BAD_8_8_J = "正しくゆっくり下げてください";
    private static final String comment_GREAT_8_8_J = "とても正しく実施しています";
    // 1-9번
    private static final String comment_BAD_8_9_J = "腕、上体、足が一になるようにしてください";
    private static final String comment_GREAT_8_9_J = "とても良いです！";
    // 1-10번
    private static final String comment_BAD_8_10_J = "拍子をあわせてゆっくり実施してください";
    private static final String comment_GREAT_8_10_J = "とても良いです！";
    // 1-11번
    private static final String comment_BAD_8_11_J = "手と上体を真っ直ぐに伸ばし、お尻を最大限引き上げてください";
    private static final String comment_GREAT_8_11_J = "とても良いです！";

    /// 까지

    // 필라테스 - 매트운동
    // Rolling Like a Ball
    private static final String comment_GOOD_1_1_EN = "Why don’t you roll your body more widely";
    private static final String comment_GREAT_1_1_EN = "You are doing good";
    // Open Leg Rocker
    private static final String comment_GOOD_1_8_EN = "Why don’t you roll your body more widely";
    private static final String comment_GREAT_1_8_EN = "You are doing good, well followed";
    // Double Leg Pull
    private static final String comment_GOOD_1_3_EN = "Move your arms and legs straight";
    private static final String comment_GREAT_1_3_EN = "Very nice!";
    // Criss Cross
    private static final String comment_GOOD_1_6_EN = "Try to stick your elbow to your knee";
    private static final String comment_GREAT_1_6_EN = "Wow, you are posture is getting better";
    // Swimmings
    private static final String comment_GOOD_1_16_EN = "Raise more and speed up the moves";
    private static final String comment_GREAT_1_16_EN = "Good, try to focus on the exercise";

    // 필라테스 - 액티브운동
    // Side Step Jacks
    private static final String comment_GOOD_2_1_EN = "Move your step dynamically and follow the trainers";
    private static final String comment_GREAT_2_1_EN = "You are doing good";
    // CrissCross Squarte
    private static final String comment_GOOD_2_2_EN = "Move your step dynamically and try to go down more";
    private static final String comment_GREAT_2_2_EN = "Wow ,you are really good at it";
    // Quray Lunges
    private static final String comment_GOOD_2_3_EN = "Move your arm more smoothly and jump much harder";
    private static final String comment_GREAT_2_3_EN = "You are really good";
    // Alt Toe Tab Squats
    private static final String comment_GOOD_2_4_EN = "Touch the top of the foot with your hand, and sit down deeply";
    private static final String comment_GREAT_2_4_EN = "Good, you are doing goodjob";
    // Squart Hops
    private static final String comment_GOOD_2_6_EN = "Try to jump more";
    private static final String comment_GREAT_2_6_EN = "Well followed!";

    // 홍두한 1
    private static final String comment_GOOD_4_1_EN = "Try to keep on the pace of trainer and move your posture much bigger";
    private static final String comment_GREAT_4_1_EN = "Welldone, you are really doing good";

    private static final String comment_GOOD_4_2_EN = "With using an upper body try to reach out more";
    private static final String comment_GREAT_4_2_EN = "Are you professional? Let us focus more";

    private static final String comment_GOOD_4_3_EN = "Try to focus on the speed and body balance";
    private static final String comment_GREAT_4_3_EN = "You are really doing good. Stay the posture";

    private static final String comment_GOOD_4_4_EN = "Try to tense and focus on the bottom and stomach";
    private static final String comment_GREAT_4_4_EN = "Good posture, focus more!";

    private static final String comment_GOOD_4_5_EN = "Go down slowly with caring with the accuracy";
    private static final String comment_GREAT_4_5_EN = "Excellent your doing good job";

    private static final String comment_GOOD_4_6_EN = "Raise your arm and down with more focus on the upper body";
    private static final String comment_GREAT_4_6_EN = "Excellent your doing good job";

    private static final String comment_GOOD_4_7_EN = "Do not move your shoulder and move your hands right and left touching the heel with base on the strength of the stomach";
    private static final String comment_GREAT_4_7_EN = "Excellent your doing good job";

    private static final String comment_GOOD_4_8_EN = "Follow the trainer with the leg back-straight and down";
    private static final String comment_GREAT_4_8_EN = "Excellent your doing good job";

    private static final String comment_GOOD_4_9_EN = "Try to touch the toe with using the strength of the stomach";
    private static final String comment_GREAT_4_9_EN = "Excellent your doing good job";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_4_10_EN = "Try to make a 90 degrees of knee";
    private static final String comment_GREAT_4_10_EN = "Excellent your doing good job";

    // 홍두한 2
    private static final String comment_GOOD_5_1_EN = "Follow the tempo with the trainer and move more widely";
    private static final String comment_GREAT_5_1_EN = "Excellent your doing good job";

    private static final String comment_GOOD_5_2_EN = "Twist your waist with your hand straight";
    private static final String comment_GREAT_5_2_EN = "Excellent your doing good job. Try to focus more on the posture";

    private static final String comment_GOOD_5_3_EN = "Use the strength of the stomach and straight your arm";
    private static final String comment_GREAT_5_3_EN = "Excellent your doing good job";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_5_4_EN = "Move your arm more and pull up your knees";
    private static final String comment_GREAT_5_4_EN = "Stay focus on this posture";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_5_5_EN = "Try to focus on the accuracy of arm movement and squat";
    private static final String comment_GREAT_5_5_EN = "Good, stay on the focus";
    private static final String comment_SOSO_5_5_EN = "Use the strength of the bottom and thigh";

    private static final String comment_GOOD_5_6_EN = "After the push-up, try to pull up your arm";
    private static final String comment_GREAT_5_6_EN = "Excellent your doing good job";

    private static final String comment_GOOD_5_7_EN = "With using a strength of the stomach, slowly move your body to be twisted";
    private static final String comment_GREAT_5_7_EN = "Good stay in this posture";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_5_8_EN = "With caring the posture of arm moving and try to get down";
    private static final String comment_GREAT_5_8_EN = "Excellent your doing good job";

    private static final String comment_GOOD_5_9_EN = "Raise your arms and leg with holding out";
    private static final String comment_GREAT_5_9_EN = "Excellent your doing good job";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_5_10_EN = "Move your upper body more";
    private static final String comment_GREAT_5_10_EN = "Excellent your doing good job";

    // 이주영 1
    private static final String comment_GOOD_6_1_EN = "Move more widely, and try to follow the posture";
    private static final String comment_GREAT_6_1_EN = "Excellent your doing good job";

    private static final String comment_GOOD_6_2_EN = "Go down more slowly";
    private static final String comment_GREAT_6_2_EN = "Excellent your doing good job";

    private static final String comment_GOOD_6_3_EN = "Move your arms and leg in widespread";
    private static final String comment_GREAT_6_3_EN = "Excellent your doing good job";

    private static final String comment_GOOD_6_4_EN = "With using the strength of the stomach go down slowly and up";
    private static final String comment_GREAT_6_4_EN = "Excellent your doing good job";

    private static final String comment_GOOD_6_5_EN = "By holding your shoulder touch the heel using your hand";
    private static final String comment_GREAT_6_5_EN = "Excellent your doing good job";

    private static final String comment_GOOD_6_6_EN = "Try to touch the knee with the elbow";
    private static final String comment_GREAT_6_6_EN = "Excellent your doing good job";

    private static final String comment_GOOD_6_7_EN = "By using a strength of the stomach, follow the posture";
    private static final String comment_GREAT_6_7_EN = "Excellent your doing good job";

    private static final String comment_GOOD_6_8_EN = "By using a strength of the stomach, raise your hand up";
    private static final String comment_GREAT_6_8_EN = "Excellent your doing good job";

    private static final String comment_GOOD_6_9_EN = "Try to restrain with using a strength of your stomach";
    private static final String comment_GREAT_6_9_EN = "Excellent your doing good job";

    private static final String comment_GOOD_6_10_EN = "Restrain the stomach strength and try to focus on the accuracy";
    private static final String comment_GREAT_6_10_EN = "Excellent your doing good job";

    // 이주영 2
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_7_1_EN = "Make your move wider while following the trainer";
    private static final String comment_GREAT_7_1_EN = "Excellent your doing good job";

    private static final String comment_GOOD_7_2_EN = "Make the arm, upper body leg to be straight forward";
    private static final String comment_GREAT_7_2_EN = "Excellent your doing good job";

    private static final String comment_GOOD_7_3_EN = "Make the arm, upper body leg to be straight forward";
    private static final String comment_GREAT_7_3_EN = "Excellent your doing good job";

    private static final String comment_GOOD_7_4_EN = "Move your leg and arm to the sky";
    private static final String comment_GREAT_7_4_EN = "Excellent your doing good job";

    private static final String comment_GOOD_7_5_EN = "Strength the waist and button while moving your arm";
    private static final String comment_GREAT_7_5_EN = "Excellent your doing good job";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_7_6_EN = "Focus on the movement of the arm";
    private static final String comment_GREAT_7_6_EN = "Excellent your doing good job";

    private static final String comment_GOOD_7_7_EN = "By holding the strength of your waist and straight the arm and leg";
    private static final String comment_GREAT_7_7_EN = "Excellent your doing good job";

    private static final String comment_GOOD_7_8_EN = "Straighten the upper body and move more widely";
    private static final String comment_GREAT_7_8_EN = "Excellent your doing good job";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_7_9_EN = "Try to focus on the movement of the arm and go down deeply";
    private static final String comment_GREAT_7_9_EN = "Excellent your doing good job";
    // 운동 변수 바꾼후 코멘트 수정
    private static final String comment_GOOD_7_10_EN = "Try to focus on the movement of the arm and go down deeply";
    private static final String comment_GREAT_7_10_EN = "Excellent your doing good job";

    // 출산부 코멘트.

    // 2-1번
    private static final String comment_BAD_9_1_EN = "Try to touch the toe with your two hands";
    private static final String comment_GOOD_9_1_EN = "Straighten the upper body";
    private static final String comment_GREAT_9_1_EN = "Excellent your doing good job";
    // 2-2번
    private static final String comment_BAD_9_2_EN = "Try to follow the trainer to be in the same posture";
    private static final String comment_GOOD_9_2_EN = "Make your body straight while raising your two hands";
    private static final String comment_GREAT_9_2_EN = "Excellent your doing good job";
    // 2-3번
    private static final String comment_BAD_9_3_EN = "Slowly go down, and pretended to sit down and go up";
    private static final String comment_GRAET_9_3_EN = "Excellent your doing good job";
    // 2-4번
    private static final String comment_BAD_9_4_EN = "Bend your knee with using the same pattern with the squat and to get up use the strength of the upper body and bottom";
    private static final String comment_GREAT_9_4_EN = "Excellent your doing good job";
    // 2-5번
    private static final String comment_BAD_9_5_EN = "Go up with using the strength of the bottom and thigh";
    private static final String comment_GREAT_9_5_EN = "Excellent your doing good job";
    // 2-7번
    private static final String comment_BAD_9_6_EN = "Jump more higher";
    private static final String comment_GOOD_9_6_EN = "Jump little bit higher";
    private static final String comment_GREAT_9_6_EN = "Excellent your doing good job";
    // 2-8번
    private static final String comment_BAD_9_7_EN = "Try the follow the pace of the exercise and straighten the arm";
    private static final String comment_GREAT_9_7_EN = "Excellent your doing good job";
    // 2-9번
    private static final String comment_BAD_9_8_EN = "Follow the rhythm and slowly do the exercise";
    private static final String comment_GREAT_9_8_EN = "Excellent your doing good job";
    // 2-10번
    private static final String comment_BAD_9_9_EN = "With following the trainer sit and straighten the arm";
    private static final String comment_GREAT_9_9_EN = "Excellent your doing good job";
    // 2-11번
    private static final String comment_BAD_9_10_EN = "Follow the rhythm and slowly do the exercise";
    private static final String comment_GREAT_9_10_EN = "Excellent your doing good job";
    // 2-12번
    private static final String comment_BAD_9_11_EN = "Try to go down a little bit more deeply";
    private static final String comment_GREAT_9_11_EN = "Excellent your doing good job";
    // 2-13번
    private static final String comment_BAD_9_12_EN = "Try to follow the trainer while straightens the arm with twisting body";
    private static final String comment_GOOD_9_12_EN = "Move your arm more actively";
    private static final String comment_GREAT_9_12_EN = "Excellent your doing good job";

    // 1-1번
    private static final String comment_BAD_8_1_EN = "Try to stretch with touching the toe with the fingertip";
    private static final String comment_GREAT_8_1_EN = "Excellent your doing good job";
    // 1-2번
    private static final String comment_GOOD_8_2_1_EN = "Straighten the back and stretch left and right side";
    private static final String comment_GOOD_8_2_2_EN = "Stretch with your arm wide and try to touch the ankle with each hands";
    private static final String comment_GREAT_8_2_EN = "Excellent your doing good job";
    // 1-3번
    private static final String comment_BAD_8_3_EN = "Try to focus on the accuracy while you’re getting up";
    private static final String comment_GOOD_8_3_EN = "Slowly go up straight";
    private static final String comment_GREAT_8_3_EN = "Excellent your doing good job";
    // 1-4번
    private static final String comment_BAD_8_4_EN = "Move your upper body more and straighten your arm forward";
    private static final String comment_GOOD_8_4_EN = "Try to put more speed";
    private static final String comment_GREAT_8_4_EN = "Excellent your doing good job";
    // 1-5번
    private static final String comment_BAD_8_5_EN = "Try go down in slow speed";
    private static final String comment_GREAT_8_5_EN = "Excellent your doing good job";
    // 1-6번
    private static final String comment_BAD_8_6_EN = "Touch the ground with the palm and try to move down the shoulder";
    private static final String comment_GREAT_8_6_EN = "Excellent your doing good job";
    // 1-7번
    private static final String comment_BAD_8_7_EN = "Please do accurate stretching of the waist and withdraw the bottom while bending down";
    private static final String comment_GREAT_8_7_EN = "Excellent your doing good job";
    // 1-8번
    private static final String comment_BAD_8_8_EN = "Try to go down with caring of accuracy";
    private static final String comment_GREAT_8_8_EN = "Excellent your doing good job";
    // 1-9번
    private static final String comment_BAD_8_9_EN = "Try to make your arm, upper body and leg to straight";
    private static final String comment_GREAT_8_9_EN = "Excellent your doing good job";
    // 1-10번
    private static final String comment_BAD_8_10_EN = "Try to follow the rhythm and do the exercise";
    private static final String comment_GREAT_8_10_EN = "Excellent your doing good job";
    // 1-11번
    private static final String comment_BAD_8_11_EN = "Try to make your hands and upper body straight while holding up your bottom";
    private static final String comment_GREAT_8_11_EN = "Excellent your doing good job";

    // 출산부 end...


    private static final String comment_v1_1_EN = "Tense the stomach and roll down powerfully";
    private static final String comment_v1_8_EN = "Try to make a straight arm and leg with holding the posture";
    private static final String comment_v1_3_EN = "Move your leg and arm more widely";
    private static final String comment_v1_6_EN = "Widen the elbow and twist the whole body";
    // private static final String comment_v1_16 = "";

    private static final String comment_v2_1_EN = "To follow the trainer’s pace";
    private static final String comment_v2_2_1_EN = "Try to follow the trainer’s pace ";
    private static final String comment_v2_2_2_EN = "Go down a little bit more";
    private static final String comment_v2_3_1_EN = "Follow the pace of the trainer";
    private static final String comment_v2_3_2_EN = "Try to move your posture in smooth way";
    private static final String comment_v2_4_EN = "Excellent!";
    private static final String comment_v2_6_1_EN = "Follow the pace of the trainers speed";
    private static final String comment_v2_6_2_EN = "Focus on the accuracy";

    /// 15분 순황 최신수정 멘트 160328///
    private static final String comment_GOOD_3_1_EN = "Try to make the movement more widely";
    private static final String comment_GOOD_3_2_EN = "Widen your arm and go down";
    private static final String comment_GOOD_3_3_EN = "Move your leg and arm more actively";
    private static final String comment_GOOD_3_4_EN = "Bend it down the posture";
    private static final String comment_GOOD_3_5_EN = "Move your leg and arm more widely";
    private static final String comment_GOOD_3_6_EN = "Try to stay in posture with the strength of the stomach";
    private static final String comment_GOOD_3_7_EN = "Raise your arm and leg more";
    private static final String comment_GOOD_3_8_EN = "Try to do big step forward";
    private static final String comment_GOOD_3_9_EN = "Follow the tempo of the posture of the trainer and move more actively";
    private static final String comment_GOOD_3_10_EN = "Raise your upper body and stay on your posture";

    private int Count_V_2_1 = 0;
    private int Count_V_2_4 = 0;
    private int Count_V_2_6 = 0;
    private int Count_V_3_1 = 0;
    private int Count_V_3_3 = 0;
    private int Count_V_3_4 = 0;
    private int Count_V_3_5 = 0;
    private int Count_V_3_8 = 0;
    private int Count_V_3_9 = 0;

    private int Count_V_4_1 = 0;
    private int Count_V_4_3 = 0;
    private int Count_V_4_8 = 0;
    private int Count_V_4_10 = 0;

    private int Count_V_5_1 = 0;

    private double SumAccuracy_V_2_1 = 0;
    private double SumAccuracy_V_2_4 = 0;
    private double SumAccuracy_V_2_6 = 0;
    private double SumAccuracy_V_3_1 = 0;
    private double SumAccuracy_V_3_3 = 0;
    private double SumAccuracy_V_3_4 = 0;
    private double SumAccuracy_V_3_5 = 0;
    private double SumAccuracy_V_3_8 = 0;
    private double SumAccuracy_V_3_9 = 0;

    private double SumAccuracy_V_4_1 = 0;
    private double SumAccuracy_V_4_3 = 0;
    private double SumAccuracy_V_4_3_1 = 0;
    private double SumAccuracy_V_4_8 = 0;
    private double SumAccuracy_V_4_8_1 = 0;
    private double SumAccuracy_V_4_10 = 0;

    private double SumAccuracy_V_5_1 = 0;

    /* comment vairable */
    private float count_1_1_A = 0;
    private float count_1_1_B = 0;
    private float count_1_1_C = 0;
    private float count_1_1_D = 0;
    private float count_1_8_A = 0;
    private float count_1_8_B = 0;
    private float count_1_8_C = 0;
    private float count_1_8_D = 0;
    private float count_1_3_A = 0;
    private float count_1_3_B = 0;
    private float count_1_3_C = 0;
    private float count_1_3_D = 0;
    private float count_1_6_A = 0;
    private float count_1_6_B = 0;
    private float count_1_6_C = 0;
    private float count_1_6_D = 0;
    private float count_1_16_A = 0;
    private float count_1_16_B = 0;
    private float count_1_16_C = 0;
    private float count_1_16_D = 0;

    private float count_2_1_A = 0;
    private float count_2_1_B = 0;
    private float count_2_1_C = 0;
    private float count_2_1_D = 0;
    private float count_2_2_A = 0;
    private float count_2_2_B = 0;
    private float count_2_2_C = 0;
    private float count_2_2_D = 0;
    private float count_2_3_A = 0;
    private float count_2_3_B = 0;
    private float count_2_3_C = 0;
    private float count_2_3_D = 0;
    private float count_2_4_A = 0;
    private float count_2_4_B = 0;
    private float count_2_4_C = 0;
    private float count_2_4_D = 0;
    private float count_2_6_A = 0;
    private float count_2_6_B = 0;
    private float count_2_6_C = 0;
    private float count_2_6_D = 0;

    private float count_4_1_A = 0;
    private float count_4_1_B = 0;
    private float count_4_1_C = 0;
    private float count_4_1_D = 0;
    private float count_4_2_A = 0;
    private float count_4_2_B = 0;
    private float count_4_2_C = 0;
    private float count_4_2_D = 0;
    private float count_4_3_A = 0;
    private float count_4_3_B = 0;
    private float count_4_3_C = 0;
    private float count_4_3_D = 0;
    private float count_4_4_A = 0;
    private float count_4_4_B = 0;
    private float count_4_4_C = 0;
    private float count_4_4_D = 0;
    private float count_4_5_A = 0;
    private float count_4_5_B = 0;
    private float count_4_5_C = 0;
    private float count_4_5_D = 0;
    private float count_4_6_A = 0;
    private float count_4_6_B = 0;
    private float count_4_6_C = 0;
    private float count_4_6_D = 0;
    private float count_4_7_A = 0;
    private float count_4_7_B = 0;
    private float count_4_7_C = 0;
    private float count_4_7_D = 0;
    private float count_4_8_A = 0;
    private float count_4_8_B = 0;
    private float count_4_8_C = 0;
    private float count_4_8_D = 0;
    private float count_4_9_A = 0;
    private float count_4_9_B = 0;
    private float count_4_9_C = 0;
    private float count_4_9_D = 0;
    private float count_4_10_A = 0;
    private float count_4_10_B = 0;
    private float count_4_10_C = 0;
    private float count_4_10_D = 0;

    private float count_5_1_A = 0;
    private float count_5_1_B = 0;
    private float count_5_1_C = 0;
    private float count_5_1_D = 0;
    private float count_5_2_A = 0;
    private float count_5_2_B = 0;
    private float count_5_2_C = 0;
    private float count_5_2_D = 0;
    private float count_5_3_A = 0;
    private float count_5_3_B = 0;
    private float count_5_3_C = 0;
    private float count_5_3_D = 0;
    private float count_5_4_A = 0;
    private float count_5_4_B = 0;
    private float count_5_4_C = 0;
    private float count_5_4_D = 0;
    private float count_5_5_A = 0;
    private float count_5_5_B = 0;
    private float count_5_5_C = 0;
    private float count_5_5_D = 0;
    private float count_5_6_A = 0;
    private float count_5_6_B = 0;
    private float count_5_6_C = 0;
    private float count_5_6_D = 0;
    private float count_5_7_A = 0;
    private float count_5_7_B = 0;
    private float count_5_7_C = 0;
    private float count_5_7_D = 0;
    private float count_5_8_A = 0;
    private float count_5_8_B = 0;
    private float count_5_8_C = 0;
    private float count_5_8_D = 0;
    private float count_5_9_A = 0;
    private float count_5_9_B = 0;
    private float count_5_9_C = 0;
    private float count_5_9_D = 0;
    private float count_5_10_A = 0;
    private float count_5_10_B = 0;
    private float count_5_10_C = 0;
    private float count_5_10_D = 0;

    private float count_6_1_A = 0;
    private float count_6_1_B = 0;
    private float count_6_1_C = 0;
    private float count_6_1_D = 0;
    private float count_6_2_A = 0;
    private float count_6_2_B = 0;
    private float count_6_2_C = 0;
    private float count_6_2_D = 0;
    private float count_6_3_A = 0;
    private float count_6_3_B = 0;
    private float count_6_3_C = 0;
    private float count_6_3_D = 0;
    private float count_6_4_A = 0;
    private float count_6_4_B = 0;
    private float count_6_4_C = 0;
    private float count_6_4_D = 0;
    private float count_6_5_A = 0;
    private float count_6_5_B = 0;
    private float count_6_5_C = 0;
    private float count_6_5_D = 0;
    private float count_6_6_A = 0;
    private float count_6_6_B = 0;
    private float count_6_6_C = 0;
    private float count_6_6_D = 0;
    private float count_6_7_A = 0;
    private float count_6_7_B = 0;
    private float count_6_7_C = 0;
    private float count_6_7_D = 0;
    private float count_6_8_A = 0;
    private float count_6_8_B = 0;
    private float count_6_8_C = 0;
    private float count_6_8_D = 0;
    private float count_6_9_A = 0;
    private float count_6_9_B = 0;
    private float count_6_9_C = 0;
    private float count_6_9_D = 0;
    private float count_6_10_A = 0;
    private float count_6_10_B = 0;
    private float count_6_10_C = 0;
    private float count_6_10_D = 0;

    private float count_7_1_A = 0;
    private float count_7_1_B = 0;
    private float count_7_1_C = 0;
    private float count_7_1_D = 0;
    private float count_7_2_A = 0;
    private float count_7_2_B = 0;
    private float count_7_2_C = 0;
    private float count_7_2_D = 0;
    private float count_7_3_A = 0;
    private float count_7_3_B = 0;
    private float count_7_3_C = 0;
    private float count_7_3_D = 0;
    private float count_7_4_A = 0;
    private float count_7_4_B = 0;
    private float count_7_4_C = 0;
    private float count_7_4_D = 0;
    private float count_7_5_A = 0;
    private float count_7_5_B = 0;
    private float count_7_5_C = 0;
    private float count_7_5_D = 0;
    private float count_7_6_A = 0;
    private float count_7_6_B = 0;
    private float count_7_6_C = 0;
    private float count_7_6_D = 0;
    private float count_7_7_A = 0;
    private float count_7_7_B = 0;
    private float count_7_7_C = 0;
    private float count_7_7_D = 0;
    private float count_7_8_A = 0;
    private float count_7_8_B = 0;
    private float count_7_8_C = 0;
    private float count_7_8_D = 0;
    private float count_7_9_A = 0;
    private float count_7_9_B = 0;
    private float count_7_9_C = 0;
    private float count_7_9_D = 0;
    private float count_7_10_A = 0;
    private float count_7_10_B = 0;
    private float count_7_10_C = 0;
    private float count_7_10_D = 0;

    private float count_8_1_A = 0;
    private float count_8_1_B = 0;
    private float count_8_1_C = 0;
    private float count_8_1_D = 0;

    private float count_9_1_A = 0;
    private float count_9_1_B = 0;
    private float count_9_1_C = 0;
    private float count_9_1_D = 0;
    private float count_9_2_A = 0;
    private float count_9_2_B = 0;
    private float count_9_2_C = 0;
    private float count_9_2_D = 0;
    private float count_9_3_A = 0;
    private float count_9_3_B = 0;
    private float count_9_3_C = 0;
    private float count_9_3_D = 0;
    private float count_9_4_A = 0;
    private float count_9_4_B = 0;
    private float count_9_4_C = 0;
    private float count_9_4_D = 0;
    private float count_9_5_A = 0;
    private float count_9_5_B = 0;
    private float count_9_5_C = 0;
    private float count_9_5_D = 0;
    private float count_9_6_A = 0;
    private float count_9_6_B = 0;
    private float count_9_6_C = 0;
    private float count_9_6_D = 0;
    private float count_9_7_A = 0;
    private float count_9_7_B = 0;
    private float count_9_7_C = 0;
    private float count_9_7_D = 0;
    private float count_9_8_A = 0;
    private float count_9_8_B = 0;
    private float count_9_8_C = 0;
    private float count_9_8_D = 0;
    private float count_9_9_A = 0;
    private float count_9_9_B = 0;
    private float count_9_9_C = 0;
    private float count_9_9_D = 0;
    private float count_9_10_A = 0;
    private float count_9_10_B = 0;
    private float count_9_10_C = 0;
    private float count_9_10_D = 0;
    private float count_9_11_A = 0;
    private float count_9_11_B = 0;
    private float count_9_11_C = 0;
    private float count_9_11_D = 0;
    private float count_9_12_A = 0;
    private float count_9_12_B = 0;
    private float count_9_12_C = 0;
    private float count_9_12_D = 0;

    private float count_10_1_A = 0;
    private float count_10_1_B = 0;
    private float count_10_1_C = 0;
    private float count_10_1_D = 0;

    private float count_22_1_A = 0;
    private float count_22_1_B = 0;
    private float count_22_1_C = 0;
    private float count_22_1_D = 0;

    private float count_23_1_A = 0;
    private float count_23_1_B = 0;
    private float count_23_1_C = 0;
    private float count_23_1_D = 0;

    private float count_24_1_A = 0;
    private float count_24_1_B = 0;
    private float count_24_1_C = 0;
    private float count_24_1_D = 0;

    private float count_25_1_A = 0;
    private float count_25_1_B = 0;
    private float count_25_1_C = 0;
    private float count_25_1_D = 0;

    private float count_26_1_A = 0;
    private float count_26_1_B = 0;
    private float count_26_1_C = 0;
    private float count_26_1_D = 0;

    private float count_27_1_A = 0;
    private float count_27_1_B = 0;
    private float count_27_1_C = 0;
    private float count_27_1_D = 0;

    private float count_28_1_A = 0;
    private float count_28_1_B = 0;
    private float count_28_1_C = 0;
    private float count_28_1_D = 0;

    private float count_29_1_A = 0;
    private float count_29_1_B = 0;
    private float count_29_1_C = 0;
    private float count_29_1_D = 0;

    private float count_30_1_A = 0;
    private float count_30_1_B = 0;
    private float count_30_1_C = 0;
    private float count_30_1_D = 0;

    /** 출산부 운동 전역 변수 **/
    private double x_buffer = 0, y_buffer = 0, z_buffer = 0;
    private int count = 0, var_chk_y = 0, pre_var_chk_y = 0, stay_time_y = 0, var_chk_x = 0, pre_var_chk_x = 0, stay_time_x = 0, stay_time_z = 0, var_chk_z = 0, pre_var_chk_z = 0;
    private double pre_acc_smooth_x = 0, pre_acc_smooth_z = 0, pre_var_chk_zy = 0, stay_time_zy = 0, var_chk_zy = 0, value_chk_z = 0, value_chk_z2 = 0, pre_value_chk_z = 0, pre_value_chk_z2 = 0,
            up_stay_time_z = 0, up_stay_time_z2 = 0, turn_chk_xz = 0, pre_turn_chk_xz = 0, pre_turn_time = 0, turn_time = 0, buffer_PVy = 0, old_grade = 0, grade = 0, grade_chk = 0, buffer_y = 0;
    private int duplicate_idx_count = 0, idx_count = 0, idx_count_dup = 0;
    private double ampbuffer = 0;
    private static final int THR_NOT_MOVE = 200; // 10초

    private int t_point = 0, t_count_percent = 0, t_accuracy_percent;
    private String t_comment = null;
    /** 출산부 변수 end **/

    private int sumAccuracyD = 0;

    private int sumAccuracy = 0;
    private int avgAccuracy = 0;
    private int minAccuracy = 0;
    private int maxAccuracy = 0;
    private int maxHeartRate = 0;
    private int minHeartRate = 0;
    private int avgHeartRate = 0;
    private int cmpHeartRate = 0;
    private int sumHeartRate = 0;
    private int count_percent = 0;
    private float sumCountPerecnt = 0;
    private int size_hr_queue = 0;

    private float sumCalorie = 0;
    private float sumCalorieD = 0;
    private int sumInterval = 2; // 초단위
    private boolean setFormula = false;
    private boolean setFormulaD = false;

    private long save_start_time = 0;

    private PreferencesManager mPre;
    private ConfigManager mConfig;

    private boolean isAction = false;
    private boolean is_R_Video = false;
    private boolean isDisableUI = false;
    private int is_R_vid = 0;
    private boolean is_Save = false;

    // Tag
    private boolean setTag = false;

    private float preCalorie = 0;

    private int videoID = 0;
    private boolean isPlaying = false;
    private static boolean setSave;
    private boolean setDebugPlay = false;
    private boolean setPlay = true;

    private BluetoothManager mBle;
    private String mac1 = null, mac2 = null;
    private boolean setDual = false;

    // private int NOW_REFERENCE = PreferencesManager.NOVICE_REFERENCE;

    private int currentPlayVideoIndex = -1;
    // private int currentPlayVideoIndexEX = 0;
    private boolean setLaptime = false;
    private boolean setLaptimeD = false;

    private static String mSdPath;
    private static String realFileName;
    private String recordName = "";
    private String hyphen = "-";
    public static String mFolder = "/coachData/";
    private int currentPosition;

    private Context mContext;

    private float preCount = 0;

    private int delay_brief = 5;

    private int interval_AccuracyLock = 1;// 정확도 1초뒤에 지움
    private boolean set_AccuracyLock = false;
    private boolean set_AccuracyLockD = false;
    private boolean set_HRLock = false;
    private boolean set_vibrateLock = false;
    private long[] motionVibrate = new long[] { 200, 100, 200, 100, 200, 2000/* dummy */ };
    // Timer t;

    private int[] main_HrArray = new int[5];

    private AsyncTask<Integer, Void, Void> mTask;

    private ArrayList<Long> arrayStartTime;

    private static VideoManager mVManager = null;

    public final static int MODE_NEW_START = 1;
    public final static int MODE_RESUME = 2;

    private final static int LEE_TRAINER_NUMBER = 1;
    private final static int HONG_TRAINER_NUMBER = 2;
    private final static int CHILDBIRTH_NUMBER = 3;

    public final static int SET_VIDEO_ID_1 = 3;
    public final static int SET_VIDEO_ID_2 = 4;
    public final static int SET_VIDEO_ID_3 = 2;
    public final static int SET_VIDEO_ID_4 = HONG_TRAINER_NUMBER * 100 + 1;
    public final static int SET_VIDEO_ID_5 = HONG_TRAINER_NUMBER * 100 + 2;
    public final static int SET_VIDEO_ID_6 = LEE_TRAINER_NUMBER * 100 + 1;
    public final static int SET_VIDEO_ID_7 = LEE_TRAINER_NUMBER * 100 + 2;
    public final static int SET_VIDEO_ID_8 = CHILDBIRTH_NUMBER * 100 + 1; // 1~4주차
    public final static int SET_VIDEO_ID_9 = CHILDBIRTH_NUMBER * 100 + 2; // full
    // 5~8주차

    private final static int V_SKIP_INDEX = -10;

    /**
     * 각 운동별 포인트 1-1 max: 끝까지 구른다. min: 구르다 말기. 1-8 max: 발끝을 크게 구른다. min: 구르다
     * 말기. 1-3 max: 회전 반경을 크고 빠르게. min: 반경은 작게 느리게. 1-6 max: 상체를 살짝 들고, 무릎 방향으로
     * 이동. min: 상체를 들지 않고 몸만 튼다. 1-16 max: 강하게! 빨리! min: ...
     *
     * 2-1 max: 최대한 손을 멀리 뻗는다. min: 손을 살짝 뻗는둥 마는둥. 2-2 max: 점프를 경쾌하게 하고, 앉으면서 손을
     * 빨리 땅까지 뻗고, 일어나면서 점프. min: 점프를 살짝하고 손을 느리게 뻗으며 땅까지 뻗지 않는다. 일어나면서 점프 x 2-3
     * max: 무릎을 구부리면서 팔을 크게 흔담. min: 무릎을 구부리지 않으며 팔을 작게 흔듬. 2-4 max: 점프를 경쾌하게,
     * 발끝까지 빨리 뻗음. min: 점프를 약하게, 무릎근처까지만 팔을 뻗음. 2-6 max: 정확하게 손을 털어주며 점프한다. min:
     * 점프를 하지 않고 손만 약하게 턴다.
     */
    /** Inner class **/
    public final class VideoParameter {

        /* 필라테스 - 매트운동 */
        public final class Group1 {

            public final class Index {
                public static final int V_1_1_R = 1;
                public static final int V_1_1 = 2;

                public static final int V_1_8_R = 3;
                public static final int V_1_8 = 4;

                public static final int V_1_3_R = 5;
                public static final int V_1_3 = 6;

                public static final int V_1_6_R = 7;
                public static final int V_1_6 = 8;

                public static final int V_1_16_R = 9;
                public static final int V_1_16 = 10;

                private static final int C_1_1 = 301;
                private static final int C_1_8 = 302;
                private static final int C_1_3 = 303;
                private static final int C_1_6 = 304;
                private static final int C_1_16 = 305;
            }

            public final class Ref {

                public final class Count {
                    public static final int V_1_1 = 15;
                    public static final int V_1_8 = 15;
                    public static final int V_1_3 = 15;
                    public static final int V_1_6 = 15;
                    public static final int V_1_16 = 60; // 예외 '초'를 기준으로 한 운동.
                }
            }

            private final class Section {
                private static final int VIDEO_1_1_START_SEC = 22; // 15
                private static final int VIDEO_1_1_END_SEC = 86;
                private static final int VIDEO_1_8_START_SEC = 111; // 92
                private static final int VIDEO_1_8_END_SEC = 185;
                private static final int VIDEO_1_3_START_SEC = 209; // 179
                private static final int VIDEO_1_3_END_SEC = 292;
                private static final int VIDEO_1_6_START_SEC = 317; // 274
                private static final int VIDEO_1_6_END_SEC = 407;
                private static final int VIDEO_1_16_START_SEC = 432; // 377
                private static final int VIDEO_1_16_END_SEC = 479;

                private static final int VIDEO_1_1_EXTRA_DELAY = 9;
                private static final int VIDEO_1_8_EXTRA_DELAY = 20;
                private static final int VIDEO_1_3_EXTRA_DELAY = 15;
                private static final int VIDEO_1_6_EXTRA_DELAY = 13;
                private static final int VIDEO_1_16_EXTRA_DELAY = 14;
            }

            private final class PlayTime {
                private static final int V_1_1 = (Section.VIDEO_1_1_END_SEC - Section.VIDEO_1_1_START_SEC);
                private static final int V_1_8 = (Section.VIDEO_1_8_END_SEC - Section.VIDEO_1_8_START_SEC);
                private static final int V_1_3 = (Section.VIDEO_1_3_END_SEC - Section.VIDEO_1_3_START_SEC);
                private static final int V_1_6 = (Section.VIDEO_1_6_END_SEC - Section.VIDEO_1_6_START_SEC);
                private static final int V_1_16 = (Section.VIDEO_1_16_END_SEC - Section.VIDEO_1_16_START_SEC);
            }

            private final class G1_1 {
                private static final float AMP_THR = -7.0f;
                private static final float AMP_REF = -23.0f;
                // private static final float AMP_REF = -18.3051f;
            }

            private final class G1_8 {
                private static final float AMP_THR = -8.0f;
                private static final float AMP_REF = -16f;
            }

            private final class G1_3 {
                private static final float AMP_THR = -5f;
                private static final float PEAK_THR = 0f;
                private static final float AMP_REF = -12f;
                private static final float PTP_REF = -55f;
            }

            private final class G1_6 {

                private static final float AMP_THR = -4f;
                private static final float PTP_THR = -100f;
                private static final float PTP_REF = -90f;
                private static final float PEAK_REF = -1f;

				/*
				 * private static final float AMP_THR = 4.0f; private static
				 * final float PTP_THR = 30f; private static final float PTP_REF
				 * = 90f; private static final float AMP_REF = 9f; private
				 * static final float PEAK_REF = -4f;
				 */
            }

            private final class G1_16 {
                private static final float AMP_THR = -2.0f;
                private static final float AMP_REF = -5f;
                private static final float PTP_THR = -6.0f;
                private static final float PTP_REF = -9.4194f;
            }
        }

        /* 필라테스 - 액티브운동 */
        public final class Group2 {

            public final class Index {
                public static final int V_2_1_R = 101;
                public static final int V_2_1 = 102;

                public static final int V_2_2_R = 103;
                public static final int V_2_2 = 104;

                public static final int V_2_3_R = 105;
                public static final int V_2_3 = 106;

                public static final int V_2_4_R = 107;
                public static final int V_2_4 = 108;

                public static final int V_2_6_R = 109;
                public static final int V_2_6 = 110;

                private static final int C_2_1 = 401;
                private static final int C_2_2 = 402;
                private static final int C_2_3 = 403;
                private static final int C_2_4 = 404;
                private static final int C_2_6 = 405;
            }

            public final class Ref {

                public final class Count {
                    public static final int V_2_1 = 15;
                    public static final int V_2_2 = 15;
                    public static final int V_2_3 = 15;
                    public static final int V_2_4 = 15;
                    public static final int V_2_6 = 15;
                }
            }

            private final class Section {
                private static final int VIDEO_2_1_START_SEC = 31;
                private static final int VIDEO_2_1_END_SEC = 88;
                private static final int VIDEO_2_2_START_SEC = 120;
                private static final int VIDEO_2_2_END_SEC = 172;
                private static final int VIDEO_2_3_START_SEC = 204;
                private static final int VIDEO_2_3_END_SEC = 257;
                private static final int VIDEO_2_4_START_SEC = 289;
                private static final int VIDEO_2_4_END_SEC = 336;
                private static final int VIDEO_2_6_START_SEC = 370;
                private static final int VIDEO_2_6_END_SEC = 407;

                private static final int VIDEO_2_1_EXTRA_DELAY = 1;
                private static final int VIDEO_2_2_EXTRA_DELAY = 1;
                private static final int VIDEO_2_3_EXTRA_DELAY = 1;
                private static final int VIDEO_2_4_EXTRA_DELAY = 0;
                private static final int VIDEO_2_6_EXTRA_DELAY = 8;
            }

            private final class PlayTime {
                private static final int V_2_1 = (Section.VIDEO_2_1_END_SEC - Section.VIDEO_2_1_START_SEC);
                private static final int V_2_2 = (Section.VIDEO_2_2_END_SEC - Section.VIDEO_2_2_START_SEC);
                private static final int V_2_3 = (Section.VIDEO_2_3_END_SEC - Section.VIDEO_2_3_START_SEC);
                private static final int V_2_4 = (Section.VIDEO_2_4_END_SEC - Section.VIDEO_2_4_START_SEC);
                private static final int V_2_6 = (Section.VIDEO_2_6_END_SEC - Section.VIDEO_2_6_START_SEC);
            }

            private final class G2_1 {
                private static final float AMP_THR = 3f;
                private static final float AMP_REF = 5.5f;
                private static final float PTP_REF = 8.98f;
            }

            private final class G2_2 {
                private static final float AMP_THR = 4.5f;
                private static final float AMP_REF = 7.0122f;
                private static final float PTP_THR = 10.0f;
                private static final float PTP_REF = 21.1333f;
                private static final float CROSS_PTP_THR = 6.0f;
                private static final float CROSS_PTP_REF = 10.7333f;
                private static final float SURFACE_REF = 50f;
            }

            private final class G2_3 {
                private static final float AMP_REF = 7.9670f;
                private static final float PTP_THR = 8f;
                private static final float PTP_REF = 28.0f;
                private static final float SURFACE_THR = 30f;
                private static final float SURFACE_REF = 120f;
            }

            private final class G2_4 {
                private static final float AMP_THR = -2.0f;
                private static final float AMP_REF = -7f;
            }

            private final class G2_6 {
                private static final float PEAK_MIN = -10f;
                private static final float PEAK_REF = -23f;
                private static final float AMP_THR = -8f;
                private static final float AMP_REF = -10f;
                private static final float PTP_THR = -16f;
            }

        }

        /* 15분 순환운동 */
        public final class Group3 {

            public final class Index {
                public static final int V_3_0_R = 201;
                public static final int V_3_0 = 202;

                public static final int V_3_1_R = 203;
                public static final int V_3_1 = 204;

                public static final int V_3_4_R = 209;
                public static final int V_3_4 = 210;

                public static final int V_3_5_R = 211;
                public static final int V_3_5 = 212;

                private static final int C_3_1 = 201;
                private static final int C_3_4 = 204;
                private static final int C_3_5 = 205;
            }

            public final class Ref {

                public final class Count {
                    public static final int V_3_1 = 10;
                    public static final int V_3_4 = 10;
                    public static final int V_3_5 = 10;
                }
            }

            private final class Section {
                // private static final int VIDEO_3_0_START_SEC = 0; // 쉬는 시간
                // private static final int VIDEO_3_0_END_SEC = 10;
                private static final int VIDEO_3_1_START_SEC = 5;
                private static final int VIDEO_3_1_END_SEC = 25;
                private static final int VIDEO_3_4_START_SEC = 31; //95
                private static final int VIDEO_3_4_END_SEC = 73;  //137
                private static final int VIDEO_3_5_START_SEC = 76;//140;
                private static final int VIDEO_3_5_END_SEC = 107; //171

                private static final int VIDEO_3_1_EXTRA_DELAY = 0;
                private static final int VIDEO_3_4_EXTRA_DELAY = 0;
                private static final int VIDEO_3_5_EXTRA_DELAY = 0;

                private static final int VIDEO_3_5_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_3_5_TOTAL_SCORE_DISPLAY_TIME = 6;
            }

            private final class PlayTime {
                private static final int V_3_1 = (Section.VIDEO_3_1_END_SEC - Section.VIDEO_3_1_START_SEC);
                private static final int V_3_4 = (Section.VIDEO_3_4_END_SEC - Section.VIDEO_3_4_START_SEC);
                private static final int V_3_5 = (Section.VIDEO_3_5_END_SEC - Section.VIDEO_3_5_START_SEC);
            }

            private final class G3_1 {
                private static final float AMP_THR = 3.0f;
                private static final float CPTP_THR = 5f;
                private static final float AMP_REF = 9f;
            }

            private final class G3_4 {
                private static final float CROSS_PTP_REF = 16.90f;
                private static final float SURFACE_THR = 10.0f;
                private static final float SURFACE_REF = 20.1883f;
            }

            private final class G3_5 {
                private static final float AMP_THR = -6f;
                private static final float PEAK_MIN = 3.5f;
                private static final float AMP_REF = -10.5f;
            }
        }

        /* 홍두한 코치 1 */
        public final class Group4 {

            public final class Index {
                public static final int V_4_1_R = 301;
                public static final int V_4_1 = 302;

                public static final int V_4_2_R = 303;
                public static final int V_4_2 = 304;

                public static final int V_4_3_R = 305;
                public static final int V_4_3 = 306;

                public static final int V_4_4_R = 307;
                public static final int V_4_4 = 308;

                public static final int V_4_5_R = 309;
                public static final int V_4_5 = 310;

                public static final int V_4_6_R = 311;
                public static final int V_4_6 = 312;

                public static final int V_4_7_R = 313;
                public static final int V_4_7 = 314;

                public static final int V_4_8_R = 315;
                public static final int V_4_8 = 316;

                public static final int V_4_9_R = 317;
                public static final int V_4_9 = 318;

                public static final int V_4_10_R = 319;
                public static final int V_4_10 = 320;

                private static final int C_4_1 = 20101;
                private static final int C_4_2 = 20102;
                private static final int C_4_3 = 20103;
                private static final int C_4_4 = 20104;
                private static final int C_4_5 = 20105;
                private static final int C_4_6 = 20106;
                private static final int C_4_7 = 20107;
                private static final int C_4_8 = 20108;
                private static final int C_4_9 = 20109;
                private static final int C_4_10 = 20110;
            }

            public final class Ref {
                public final class Count {
                    public static final int V_4_1 = 20;
                    public static final int V_4_2 = 10;
                    public static final int V_4_3 = 10;// <- 20
                    public static final int V_4_4 = 15;
                    public static final int V_4_5 = 20;
                    public static final int V_4_6 = 15;
                    public static final int V_4_7 = 10;
                    public static final int V_4_8 = 10;// <- 20
                    public static final int V_4_9 = 10;// <- 20
                    public static final int V_4_10 = 10;// <- 20
                }
            }

            private final class Section {
                private static final int VIDEO_4_1_START_SEC = 30;
                private static final int VIDEO_4_1_END_SEC = 75;
                private static final int VIDEO_4_2_START_SEC = 108;
                private static final int VIDEO_4_2_END_SEC = 164;
                private static final int VIDEO_4_3_START_SEC = 198;
                private static final int VIDEO_4_3_END_SEC = 259;
                private static final int VIDEO_4_4_START_SEC = 292;
                private static final int VIDEO_4_4_END_SEC = 352;
                private static final int VIDEO_4_5_START_SEC = 385;
                private static final int VIDEO_4_5_END_SEC = 436;
                private static final int VIDEO_4_6_START_SEC = 470;
                private static final int VIDEO_4_6_END_SEC = 523;
                private static final int VIDEO_4_7_START_SEC = 558;
                private static final int VIDEO_4_7_END_SEC = 593;
                private static final int VIDEO_4_8_START_SEC = 628;
                private static final int VIDEO_4_8_END_SEC = 684;
                private static final int VIDEO_4_9_START_SEC = 717;
                private static final int VIDEO_4_9_END_SEC = 761;
                private static final int VIDEO_4_10_START_SEC = 794;
                private static final int VIDEO_4_10_END_SEC = 880;

                private static final int VIDEO_4_3_SKIP_START_SEC = 228;
                private static final int VIDEO_4_3_SKIP_END_SEC = 230;

                private static final int VIDEO_4_1_EXTRA_DELAY = 0;
                private static final int VIDEO_4_2_EXTRA_DELAY = 0;
                private static final int VIDEO_4_3_EXTRA_DELAY = 1;
                private static final int VIDEO_4_4_EXTRA_DELAY = 5;
                private static final int VIDEO_4_5_EXTRA_DELAY = 0;
                private static final int VIDEO_4_6_EXTRA_DELAY = 3;
                private static final int VIDEO_4_7_EXTRA_DELAY = 7;
                private static final int VIDEO_4_8_EXTRA_DELAY = 0;
                private static final int VIDEO_4_9_EXTRA_DELAY = 8;
                private static final int VIDEO_4_10_EXTRA_DELAY = 4;
            }

            private final class PlayTime {
                private static final int V_4_1 = (Section.VIDEO_4_1_END_SEC - Section.VIDEO_4_1_START_SEC);
                private static final int V_4_2 = (Section.VIDEO_4_2_END_SEC - Section.VIDEO_4_2_START_SEC);
                private static final int V_4_3 = (Section.VIDEO_4_3_END_SEC - Section.VIDEO_4_3_START_SEC);
                private static final int V_4_4 = (Section.VIDEO_4_4_END_SEC - Section.VIDEO_4_4_START_SEC);
                private static final int V_4_5 = (Section.VIDEO_4_5_END_SEC - Section.VIDEO_4_5_START_SEC);
                private static final int V_4_6 = (Section.VIDEO_4_6_END_SEC - Section.VIDEO_4_6_START_SEC);
                private static final int V_4_7 = (Section.VIDEO_4_7_END_SEC - Section.VIDEO_4_7_START_SEC);
                private static final int V_4_8 = (Section.VIDEO_4_8_END_SEC - Section.VIDEO_4_8_START_SEC);
                private static final int V_4_9 = (Section.VIDEO_4_9_END_SEC - Section.VIDEO_4_9_START_SEC);
                private static final int V_4_10 = (Section.VIDEO_4_10_END_SEC - Section.VIDEO_4_10_START_SEC);
            }

            private final class G4_1 {
                private static final float AMP_THR = -4f;
                private static final float AMP_REF = -20f;
                private static final float CPTP_THR = -4.5f;
            }

            private final class G4_2 {
				/*
				 * private static final float AMP_THR = 3.0f; private static
				 * final float AMP_REF = 6f; private static final float PTP_THR
				 * = -12.0f; private static final float PTP_MAX = -30.0f;
				 * private static final float PEAK_MIN = 8.5f; private static
				 * final float PEAK_MAX = 14.5f; private static final float
				 * SURFACE_THR = 15f;
				 */

                private static final float AMP_THR = -4.0f;
                private static final float AMP_REF = -6f;
                private static final float PTP_THR = -12.0f;
                private static final float PTP_MAX = -30.0f;
                private static final float PEAK_MIN = 8.5f;
                private static final float PEAK_MAX = 11.5f;
                private static final float SURFACE_THR = 15f;
            }

            private final class G4_3 {
                private static final float PEAK_MAX = 2f;
                private static final float PEAK_REF = 9f;
                private static final float PTP_REF = 40f;
                private static final float PTP_THR = 20f;

            }

            public final class G4_4 {
                public static final float PEAK_MIN = -3.0f;
                public static final float AMP_THR = -4f;
                public static final float AMP_REF = -13.6908f;
                public static final float CROSS_PTP_REF = -40f;
                public static final float SURFACE_REF = -222.7467f;
            }

            public final class G4_5 {
                public static final float AMP_THR = 7.0f;
                public static final float AMP_REF = 18f;
                public static final float PTP_REF = 50f;
                public static final float SURFACE_REF = 144.1732f;
            }

            private final class G4_6 {
                private static final float AMP_THR = 1.5f;
                private static final float PEAK_MAX = 10.5f;
                private static final float AMP_REF = 4.1112f;
                public static final float SURFACE_REF = 5.4798f;
            }

            private final class G4_7 {
                private static final float AMP_THR = -1f;
                private static final float AMP_REF = -4f;
            }

            public final class G4_8 {
                public static final float AMP_THR = 10f;
                public static final float AMP_REF = 15f;
                public static final float CPTP_REF = 23f;
            }

            private final class G4_9 {
                private static final float AMP_THR = -2.0f;
                private static final float AMP_REF = -5.5f;
                private static final float PEAK_MIN = 10f;
                private static final float SURFACE_REF = 102.8703f;
            }

            private final class G4_10 {
                private static final float AMP_THR = 2.0f;
                private static final float AMP_REF = 2.7407f;
                private static final float SURFACE_THR = 1.5f;
                private static final float SURFACE_REF = 25.0178f;
            }
        }

        /* 홍두한 코치 2 */
        public final class Group5 {

            public final class Index {
                public static final int V_5_1_R = 401;
                public static final int V_5_1 = 402;

                public static final int V_5_2_R = 403;
                public static final int V_5_2 = 404;

                public static final int V_5_3_R = 405;
                public static final int V_5_3 = 406;

                public static final int V_5_4_R = 407;
                public static final int V_5_4 = 408;

                public static final int V_5_5_R = 409;
                public static final int V_5_5 = 410;

                public static final int V_5_6_R = 411;
                public static final int V_5_6 = 412;

                public static final int V_5_7_R = 413;
                public static final int V_5_7 = 414;

                public static final int V_5_8_R = 415;
                public static final int V_5_8 = 416;

                public static final int V_5_9_R = 417;
                public static final int V_5_9 = 418;

                public static final int V_5_10_R = 419;
                public static final int V_5_10 = 420;

                public static final int V_5_11_R = 523;
                public static final int V_5_11 = 524;

                public static final int V_5_12_R = 525;
                public static final int V_5_12 = 526;

                private static final int C_5_1 = 20201;
                private static final int C_5_2 = 20202;
                private static final int C_5_3 = 20203;
                private static final int C_5_4 = 20204;
                private static final int C_5_5 = 20205;
                private static final int C_5_6 = 20206;
                private static final int C_5_7 = 20207;
                private static final int C_5_8 = 20208;
                private static final int C_5_9 = 20209;
                private static final int C_5_10 = 20210;
            }

            public final class Ref {
                public final class Count {
                    public static final int V_5_1 = 20;
                    public static final int V_5_2 = 10;
                    public static final int V_5_3 = 20;
                    public static final int V_5_4 = 10;
                    public static final int V_5_5 = 20;
                    public static final int V_5_6 = 15; // 실제는 7 또는 8
                    public static final int V_5_7 = 20;
                    public static final int V_5_8 = 20;
                    public static final int V_5_9 = 15;
                    public static final int V_5_10 = 20;
                }
            }

            private final class Section {
                private static final int VIDEO_5_1_START_SEC = 30;
                private static final int VIDEO_5_1_END_SEC = 76;
                private static final int VIDEO_5_2_START_SEC = 110;
                private static final int VIDEO_5_2_END_SEC = 214;
                private static final int VIDEO_5_3_START_SEC = 246;
                private static final int VIDEO_5_3_END_SEC = 302;
                private static final int VIDEO_5_4_START_SEC = 336;
                private static final int VIDEO_5_4_END_SEC = 401;
                private static final int VIDEO_5_5_START_SEC = 439;
                private static final int VIDEO_5_5_END_SEC = 523;
                private static final int VIDEO_5_6_START_SEC = 557;
                private static final int VIDEO_5_6_END_SEC = 639;
                private static final int VIDEO_5_7_START_SEC = 676;
                private static final int VIDEO_5_7_END_SEC = 734;
                private static final int VIDEO_5_8_START_SEC = 768;
                private static final int VIDEO_5_8_END_SEC = 843;
                private static final int VIDEO_5_9_START_SEC = 877;
                private static final int VIDEO_5_9_END_SEC = 951;
                private static final int VIDEO_5_10_START_SEC = 984;
                private static final int VIDEO_5_10_END_SEC = 1058;

                private static final int VIDEO_5_5_SKIP_START_SEC = 487;
                private static final int VIDEO_5_5_SKIP_END_SEC = 490;
                private static final int VIDEO_5_10_SKIP_START_SEC = 1023;
                private static final int VIDEO_5_10_SKIP_END_SEC = 1031;

                private static final int VIDEO_5_10_EXTRA_SEPERATE_TIME = VIDEO_5_10_SKIP_END_SEC;

                private static final int VIDEO_5_1_EXTRA_DELAY = 0;
                private static final int VIDEO_5_2_EXTRA_DELAY = 14;
                private static final int VIDEO_5_3_EXTRA_DELAY = 12;
                private static final int VIDEO_5_4_EXTRA_DELAY = 5;
                private static final int VIDEO_5_5_EXTRA_DELAY = 12;
                private static final int VIDEO_5_6_EXTRA_DELAY = 9;
                private static final int VIDEO_5_7_EXTRA_DELAY = 12;
                private static final int VIDEO_5_8_EXTRA_DELAY = 5;
                private static final int VIDEO_5_9_EXTRA_DELAY = 11;
                private static final int VIDEO_5_10_EXTRA_DELAY = 12;
            }

            private final class PlayTime {
                private static final int V_5_1 = (Section.VIDEO_5_1_END_SEC - Section.VIDEO_5_1_START_SEC);
                private static final int V_5_2 = (Section.VIDEO_5_2_END_SEC - Section.VIDEO_5_2_START_SEC);
                private static final int V_5_3 = (Section.VIDEO_5_3_END_SEC - Section.VIDEO_5_3_START_SEC);
                private static final int V_5_4 = (Section.VIDEO_5_4_END_SEC - Section.VIDEO_5_4_START_SEC);
                private static final int V_5_5 = (Section.VIDEO_5_5_END_SEC - Section.VIDEO_5_5_START_SEC);
                private static final int V_5_6 = (Section.VIDEO_5_6_END_SEC - Section.VIDEO_5_6_START_SEC);
                private static final int V_5_7 = (Section.VIDEO_5_7_END_SEC - Section.VIDEO_5_7_START_SEC);
                private static final int V_5_8 = (Section.VIDEO_5_8_END_SEC - Section.VIDEO_5_8_START_SEC);
                private static final int V_5_9 = (Section.VIDEO_5_9_END_SEC - Section.VIDEO_5_9_START_SEC);
                private static final int V_5_10 = (Section.VIDEO_5_10_END_SEC - Section.VIDEO_5_10_START_SEC);
            }

            private final class G5_1 {
                private static final float AMP_THR = -4f;
                private static final float CPTP_THR = -4.5f;
                private static final float AMP_REF = -20f;
            }

            private final class G5_2 {
                private static final float AMP_THR = 2f;
                private static final float PEAK_MAX = 0.1f;
                private static final float PEAK_MAX_REF = 4f;
                private static final float CPTP_REF = 25f;
            }

            private final class G5_3 {
                private static final float PEAK_MIN = -0.1f;
                private static final float PEAK_MIN_REF = -3.6f;
                private static final float AMP_THR = -3f;
                private static final float CROSS_PTP_REF = -20f;
            }

            private final class G5_4 {
                private static final float AMP_THR = 2.5f;
                private static final float AMP_REF = 4f;
            }

            private final class G5_5 {
                private static final float AMP_THR = -10f;
                private static final float PEAK_MIN = -5f;
                private static final float AMP_REF = -16.4715f;
                private static final float PTP_REF = -38f;
            }

            private final class G5_6 {
                private static final float AMP_THR = 10.0f;
                private static final float PEAK_MAX = -5f;
                private static final float AMP_REF = 15f;
                private static final float CPTP_REF = 14f;
            }

            private final class G5_7 {
                private static final float AMP_THR = 3.5f;
                private static final float PEAK_MAX = 3f;
                private static final float PEAK_MAX_REF = 8f;
                private static final float PTP_REF = 32f;
            }

            public final class G5_8 {
                public static final float AMP_THR = 7.0f;
                private static final float PEAK_MAX = 1f;
                public static final float AMP_REF = 12.9122f;
                public static final float CPTP_REF = 34f;
                private static final float PEAK_MAX_REF = 6f;
            }

            private final class G5_9 {
                private static final float AMP_THR = 0.4f;
                private static final float PTP_THR = 20f;
                private static final float PEAK_MAX = 3f;
                private static final float CPTP_REF = 30f;
            }

            private final class G5_10 {
                private static final float PEAK_MAX_1 = -6f;
                private static final float PEAK_MAX_2 = -6f;
                private static final float AMP_THR_1 = 4f;
                private static final float AMP_THR_2 = 3f;
                private static final float PTP_THR = 30f;
                private static final float AMP_REF_1 = 7.5f;
                private static final float AMP_REF_2 = 11f;
                private static final float PTP_REF = 45f;

            }
        }

        /* 이주영 코치 1 */
        public final class Group6 {

            public final class Index {
                public static final int V_6_1_R = 501;
                public static final int V_6_1 = 502;

                public static final int V_6_2_R = 503;
                public static final int V_6_2 = 504;

                public static final int V_6_3_R = 505;
                public static final int V_6_3 = 506;

                public static final int V_6_4_R = 507;
                public static final int V_6_4 = 508;

                public static final int V_6_5_R = 509;
                public static final int V_6_5 = 510;

                public static final int V_6_6_R = 511;
                public static final int V_6_6 = 512;

                public static final int V_6_7_R = 513;
                public static final int V_6_7 = 514;

                public static final int V_6_8_R = 515;
                public static final int V_6_8 = 516;

                public static final int V_6_9_R = 517;
                public static final int V_6_9 = 518;

                public static final int V_6_10_R = 519;
                public static final int V_6_10 = 520;

                private static final int C_6_1 = 10101;
                private static final int C_6_2 = 10102;
                private static final int C_6_3 = 10103;
                private static final int C_6_4 = 10104;
                private static final int C_6_5 = 10105;
                private static final int C_6_6 = 10106;
                private static final int C_6_7 = 10107;
                private static final int C_6_8 = 10108;
                private static final int C_6_9 = 10109;
                private static final int C_6_10 = 10110;
            }

            public final class Ref {
                public final class Count {
                    public static final int V_6_1 = 25;
                    public static final int V_6_2 = 10;
                    public static final int V_6_3 = 10;
                    public static final int V_6_4 = 10;
                    public static final int V_6_5 = 10;
                    public static final int V_6_6 = 10;
                    public static final int V_6_7 = 10;
                    public static final int V_6_8 = 10;
                    public static final int V_6_9 = 10;
                    public static final int V_6_10 = 10;
                }
            }

            private final class Section {
                private static final int VIDEO_6_1_START_SEC = 32;
                private static final int VIDEO_6_1_END_SEC = 85;
                private static final int VIDEO_6_2_START_SEC = 120;
                private static final int VIDEO_6_2_END_SEC = 187;
                private static final int VIDEO_6_3_START_SEC = 223;
                private static final int VIDEO_6_3_END_SEC = 268;
                private static final int VIDEO_6_4_START_SEC = 302;
                private static final int VIDEO_6_4_END_SEC = 351;
                private static final int VIDEO_6_5_START_SEC = 386;
                private static final int VIDEO_6_5_END_SEC = 407;
                private static final int VIDEO_6_6_START_SEC = 444;
                private static final int VIDEO_6_6_END_SEC = 473;
                private static final int VIDEO_6_7_START_SEC = 509;
                private static final int VIDEO_6_7_END_SEC = 584;
                private static final int VIDEO_6_8_START_SEC = 619;
                private static final int VIDEO_6_8_END_SEC = 653;
                private static final int VIDEO_6_9_START_SEC = 687;
                private static final int VIDEO_6_9_END_SEC = 742;
                private static final int VIDEO_6_10_START_SEC = 776;
                private static final int VIDEO_6_10_END_SEC = 812;

                private static final int VIDEO_6_1_EXTRA_DELAY = 1;
                private static final int VIDEO_6_2_EXTRA_DELAY = 3;
                private static final int VIDEO_6_3_EXTRA_DELAY = 6;
                private static final int VIDEO_6_4_EXTRA_DELAY = 5;
                private static final int VIDEO_6_5_EXTRA_DELAY = 3;
                private static final int VIDEO_6_6_EXTRA_DELAY = 5;
                private static final int VIDEO_6_7_EXTRA_DELAY = 6;
                private static final int VIDEO_6_8_EXTRA_DELAY = 4;
                private static final int VIDEO_6_9_EXTRA_DELAY = 3;
                private static final int VIDEO_6_10_EXTRA_DELAY = 5;
            }

            private final class PlayTime {
                private static final int V_6_1 = (Section.VIDEO_6_1_END_SEC - Section.VIDEO_6_1_START_SEC);
                private static final int V_6_2 = (Section.VIDEO_6_2_END_SEC - Section.VIDEO_6_2_START_SEC);
                private static final int V_6_3 = (Section.VIDEO_6_3_END_SEC - Section.VIDEO_6_3_START_SEC);
                private static final int V_6_4 = (Section.VIDEO_6_4_END_SEC - Section.VIDEO_6_4_START_SEC);
                private static final int V_6_5 = (Section.VIDEO_6_5_END_SEC - Section.VIDEO_6_5_START_SEC);
                private static final int V_6_6 = (Section.VIDEO_6_6_END_SEC - Section.VIDEO_6_6_START_SEC);
                private static final int V_6_7 = (Section.VIDEO_6_7_END_SEC - Section.VIDEO_6_7_START_SEC);
                private static final int V_6_8 = (Section.VIDEO_6_8_END_SEC - Section.VIDEO_6_8_START_SEC);
                private static final int V_6_9 = (Section.VIDEO_6_9_END_SEC - Section.VIDEO_6_9_START_SEC);
                private static final int V_6_10 = (Section.VIDEO_6_10_END_SEC - Section.VIDEO_6_10_START_SEC);
            }

            public final class G6_1 {
                private static final float PEAK_MIN = -10.0f;
                private static final float AMP_REF = -16f;
            }

            public final class G6_2 {
                private static final float PEAK_MIN = -5f;
                private static final float AMP_THR = -10f;
                private static final float AMP_REF = 0;
            }

            public final class G6_3 {
                private static final float AMP_THR = -4.0f;
                private static final float CPTP_THR = -15f;
                private static final float AMP_REF = -12f;
                private static final float PTP_REF = -58f;
            }

            public final class G6_4 {
                private static final float PEAK_MIN = -0.2f;
                private static final float CPTP_THR = -13f;
                private static final float PEAK_MIN_REF = -2.8f;
            }

            public final class G6_5 {
                private static final float AMP_THR = -1f;
                private static final float AMP_REF = -4f;
            }

            public final class G6_6 {
                private static final float AMP_THR = 0f;
                private static final float PTP_THR = -100f;
                private static final float PTP_REF = -55f;
                private static final float PEAK_REF = -1f;
            }

            public final class G6_7 {
                private static final float PTP_THR = 30f;
                private static final float PEAK_MAX = 3.5f;
                private static final float PEAK_MAX_REF = 6.2f;
                private static final float PTP_REF = 58f;
            }

            public final class G6_8 {
                private static final float AMP_THR = -3f;
                private static final float PEAK_MIN = 2f;
                private static final float AMP_REF = -7f;
                private static final float PTP_REF = -58f;
            }

            public final class G6_9 {
                private static final float AMP_THR = 1.5f;
                private static final float CPTP_THR = 12f;
                private static final float PEAK_MAX = 4f;
                private static final float CPTP_REF = 33f;
                private static final float SURFACE_REF = -425.8985f;
            }

            public final class G6_10 {
                private static final float PTP_THR = 30f;
                private static final float PTP_REF = 70f;
                private static final float PEAK_MAX = -2.3f;
            }

        }

        /* 이주영 코치 2 */
        public final class Group7 {

            public final class Index {
                public static final int V_7_1_R = 601;
                public static final int V_7_1 = 602;

                public static final int V_7_2_R = 603;
                public static final int V_7_2 = 604;

                public static final int V_7_3_R = 605;
                public static final int V_7_3 = 606;

                public static final int V_7_4_R = 607;
                public static final int V_7_4 = 608;

                public static final int V_7_5_R = 609;
                public static final int V_7_5 = 610;

                public static final int V_7_6_R = 611;
                public static final int V_7_6 = 612;

                public static final int V_7_7_R = 613;
                public static final int V_7_7 = 614;

                public static final int V_7_8_R = 615;
                public static final int V_7_8 = 616;

                public static final int V_7_9_R = 617;
                public static final int V_7_9 = 618;

                public static final int V_7_10_R = 619;
                public static final int V_7_10 = 620;

                private static final int C_7_1 = 10201;
                private static final int C_7_2 = 10202;
                private static final int C_7_3 = 10203;
                private static final int C_7_4 = 10204;
                private static final int C_7_5 = 10205;
                private static final int C_7_6 = 10206;
                private static final int C_7_7 = 10207;
                private static final int C_7_8 = 10208;
                private static final int C_7_9 = 10209;
                private static final int C_7_10 = 10210;
            }

            public final class Ref {
                public final class Count {
                    public static final int V_7_1 = 25;
                    public static final int V_7_2 = 10;
                    public static final int V_7_3 = 20;
                    public static final int V_7_4 = 10;
                    public static final int V_7_5 = 10;
                    public static final int V_7_6 = 5;
                    public static final int V_7_7 = 10;
                    public static final int V_7_8 = 20;
                    public static final int V_7_9 = 20;
                    public static final int V_7_10 = 10;
                }
            }

            private final class Section {
                private static final int VIDEO_7_1_START_SEC = 32;
                private static final int VIDEO_7_1_END_SEC = 102;
                private static final int VIDEO_7_2_START_SEC = 136;
                private static final int VIDEO_7_2_END_SEC = 207;
                private static final int VIDEO_7_3_START_SEC = 243;
                private static final int VIDEO_7_3_END_SEC = 335;
                private static final int VIDEO_7_4_START_SEC = 369;
                private static final int VIDEO_7_4_END_SEC = 424;
                private static final int VIDEO_7_5_START_SEC = 458;
                private static final int VIDEO_7_5_END_SEC = 517;
                private static final int VIDEO_7_6_START_SEC = 551;
                private static final int VIDEO_7_6_END_SEC = 634;
                private static final int VIDEO_7_7_START_SEC = 669;
                private static final int VIDEO_7_7_END_SEC = 725;
                private static final int VIDEO_7_8_START_SEC = 759;
                private static final int VIDEO_7_8_END_SEC = 818;
                private static final int VIDEO_7_9_START_SEC = 853;
                private static final int VIDEO_7_9_END_SEC = 922;
                private static final int VIDEO_7_10_START_SEC = 957;
                private static final int VIDEO_7_10_END_SEC = 993;

                private static final int VIDEO_7_8_SKIP_START_SEC = 790;
                private static final int VIDEO_7_8_SKIP_END_SEC = 791;
                private static final int VIDEO_7_9_SKIP_START_SEC = 886;
                private static final int VIDEO_7_9_SKIP_END_SEC = 890;

                private static final int VIDEO_7_1_EXTRA_DELAY = 1;
                private static final int VIDEO_7_2_EXTRA_DELAY = 7;
                private static final int VIDEO_7_3_EXTRA_DELAY = 6;
                private static final int VIDEO_7_4_EXTRA_DELAY = 4;
                private static final int VIDEO_7_5_EXTRA_DELAY = 5;
                private static final int VIDEO_7_6_EXTRA_DELAY = 12;
                private static final int VIDEO_7_7_EXTRA_DELAY = 5;
                private static final int VIDEO_7_8_EXTRA_DELAY = 3;
                private static final int VIDEO_7_9_EXTRA_DELAY = 3;
                private static final int VIDEO_7_10_EXTRA_DELAY = 3;
            }

            private final class PlayTime {
                private static final int V_7_1 = (Section.VIDEO_7_1_END_SEC - Section.VIDEO_7_1_START_SEC);
                private static final int V_7_2 = (Section.VIDEO_7_2_END_SEC - Section.VIDEO_7_2_START_SEC);
                private static final int V_7_3 = (Section.VIDEO_7_3_END_SEC - Section.VIDEO_7_3_START_SEC);
                private static final int V_7_4 = (Section.VIDEO_7_4_END_SEC - Section.VIDEO_7_4_START_SEC);
                private static final int V_7_5 = (Section.VIDEO_7_5_END_SEC - Section.VIDEO_7_5_START_SEC);
                private static final int V_7_6 = (Section.VIDEO_7_6_END_SEC - Section.VIDEO_7_6_START_SEC);
                private static final int V_7_7 = (Section.VIDEO_7_7_END_SEC - Section.VIDEO_7_7_START_SEC);
                private static final int V_7_8 = (Section.VIDEO_7_8_END_SEC - Section.VIDEO_7_8_START_SEC);
                private static final int V_7_9 = (Section.VIDEO_7_9_END_SEC - Section.VIDEO_7_9_START_SEC);
                private static final int V_7_10 = (Section.VIDEO_7_10_END_SEC - Section.VIDEO_7_10_START_SEC);
            }

            public final class G7_1 {
                private static final float AMP_THR = 8;
                private static final float AMP_REF = 16f;
                private static final float PTP_REF = 62f;
                private static final float SURFACE_REF = 204.3485f;
            }

            public final class G7_2 {
                private static final float AMP_THR = 3f;
                private static final float PEAK_MAX = -3f;
                private static final float PEAK_MAX_REF = 1f;
                private static final float CPTP_REF = 25f;
            }

            public final class G7_3 {
                private static final float PEAK_MIN = 5.0f;
                private static final float CPTP_THR = -25f;
                private static final float CPTP_REF = -55f;
            }

            public final class G7_4 {
                private static final float AMP_THR = 1f;
                private static final float PEAK_MAX = 1f;
                private static final float SURFACE_REF = 100f;
            }

            public final class G7_5 {
                private static final float AMP_THR = -3f;
                private static final float PEAK_MIN = -3f;
                private static final float PTP_REF = -42f;
				/*
				 * private static final float AMP_THR = -4.0f; private static
				 * final float PEAK_MIN = -4f; private static final float
				 * AMP_REF = 7.1741f; private static final float CPTP_REF = 18f;
				 */
            }

            public final class G7_6 {
                private static final float SURFACE_THR = 200f;
                private static final float AMP_THR = 8f;
                private static final float SURFACE_REF = 440f;
				/*
				 * private static final float AMP_THR = -7f; private static
				 * final float PEAK_MAX = 9f; private static final float PTP_REF
				 * = 72f; private static final float SURFACE_THR = -50f;
				 */
            }

            public final class G7_7 {
                private static final float AMP_THR = -2.0f;
                private static final float PEAK_MIN = -0.5f;
                private static final float AMP_REF = -4f;
            }

            public final class G7_8 {
                private static final float PEAK_MAX = 3.5f;
                private static final float PTP_REF = 50f;
                private static final float PEAK_MAX_REF = 8f;
            }

            public final class G7_9 {
                private static final float PEAK_MAX = 3.5f;
                private static final float AMP_THR = 7.8f;
                private static final float PEAK_MAX_REF = 11f;
                private static final float CPTP_REF = 23f;
                private static final float PEAK_MIN = 0;
            }

            public final class G7_10 {
                private static final float AMP_THR = -1.5f;
                private static final float PTP_THR = -30f;
                private static final float PTP_REF = -55f;
            }
        }

        /* 출산부 1 */
        public final class Group8 {

            public final class Index {
                public static final int V_8_1_R = 701;
                public static final int V_8_1 = 702;
                public static final int V_8_2_R = 703;
                public static final int V_8_2 = 704;
                public static final int V_8_3_R = 705;
                public static final int V_8_3 = 706;
                public static final int V_8_4_R = 707;
                public static final int V_8_4 = 708;
                public static final int V_8_5_R = 709;
                public static final int V_8_5 = 710;
                public static final int V_8_6_R = 711;
                public static final int V_8_6 = 712;
                public static final int V_8_7_R = 713;
                public static final int V_8_7 = 714;
                public static final int V_8_8_R = 715;
                public static final int V_8_8 = 716;
                public static final int V_8_9_R = 717;
                public static final int V_8_9 = 718;
                public static final int V_8_10_R = 719;
                public static final int V_8_10 = 720;
                public static final int V_8_11_R = 721;
                public static final int V_8_11 = 722;

                private static final int C_8_1 = 30101;
                private static final int C_8_2 = 30102;
                private static final int C_8_3 = 30103;
                private static final int C_8_4 = 30104;
                private static final int C_8_5 = 30105;
                private static final int C_8_6 = 30106;
                private static final int C_8_7 = 30107;
                private static final int C_8_8 = 30108;
                private static final int C_8_9 = 30109;
                private static final int C_8_10 = 30110;
                private static final int C_8_11 = 30111;
            }

            private final class Comment {
                private final class Section5 {
                    private static final String V_8_1 = "잘하셨어요! 오늘도 끝까지 지금 같은 컨디션으로 열심히 해주세요.";
                    private static final String V_8_2 = "유연성이 굉장히 좋아요. 신체 유연성을 늘리는 것은 산후 운동 중 가장 중요한 포인트에요.";
                    private static final String V_8_3 = "아주 좋아요. 현재도 좋지만 산후에 너무 무리한 복근운동은 자칫  좋지 않을 수도 있어요. 자신의 몸 상태를 체크하며 운동을 실시해주세요.";
                    private static final String V_8_4 = "어려운 동작인데 뛰어난 점수를 얻고 있어요! 현상태를 유지하는 것도 좋지만 혹시 무리하고 있는 것이 아닌지 걱정되네요. 항상 자신의 몸상태에 유념하여 운동을 실시해주세요.";
                    private static final String V_8_5 = "굉장히 좋은 자세를 유지하고 있어요. 수건을 잡지 않고 자세를 유지한다면 더 좋은 효과를 볼 수 있어요.";
                    private static final String V_8_6 = "엄청 유연하시네요! 지금과 같은 유연성을 계속 유지해주세요.";
                    private static final String V_8_7 = "유연성이 뛰어나네요! 현재 상태를 유지해주세요.";
                    private static final String V_8_8 = "완벽한 스쿼드 자세에요. 텐션을 조금 더 넓게 벌리거나 더 깊숙히 앉는다면 탄탄한 허벅지를 가질 수 있어요.";
                    private static final String V_8_9 = "상체균형이 매우 좋아요. 현재 상태를 유지해주세요.";
                    private static final String V_8_10 = "완벽한 자세에요! 힙업이 되는게 느껴지시나요?";
                    private static final String V_8_11 = "오늘도 마지막까지 고생하셨어요! 아직 회복기이니 몸을 처음부터 다시 만든다는 느낌으로 운동하시면 좋아요.";

                    private static final int V_8_1_REF = 90;
                    private static final int V_8_2_REF = 80;
                    private static final int V_8_3_REF = 75;
                    private static final int V_8_4_REF = 75;
                    private static final int V_8_5_REF = 80;
                    private static final int V_8_6_REF = 80;
                    private static final int V_8_7_REF = 80;
                    private static final int V_8_8_REF = 80;
                    private static final int V_8_9_REF = 90;
                    private static final int V_8_10_REF = 90;
                    private static final int V_8_11_REF = 80;
                }

                private final class Section4 {
                    private static final String V_8_1 = "좋아지고 있어요! 오늘은 더 높은 점수를 획득해보세요.";
                    private static final String V_8_2 = "아직 몸이 완벽하게 돌아오지 않으신 것 같아요. 유연성을 기르기 위해서는 꾸준한 스트레칭이 필요해요.";
                    private static final String V_8_3 = "출산 후 복근운동은 매우 힘들지만 가치 있는 일이에요. 단순히 몸매가 좋아지는 것이 아닌 신체의 밸런스를 잡아 줄 수 있어요. 횟수보다는 정확도에 신경을 쓰며 천천히 꾸준하게 운동해주세요.";
                    private static final String V_8_4 = "점수가 낮다고 실망하지 마세요. 매우 어려운 동작이에요. 복근 운동의 가장 중요한 점은 꾸준함이에요. 오늘 점수가 낮다고 실망하지 말고 내일 조금 더 파이팅 해서 도전해보는 것은 어떨까요?";
                    private static final String V_8_5 = "수건으로 자세 유지가 힘들다면 봉 같은 것을 이용해 보는 것은 어떨까요? 자세를 잡지가 훨씬 수월합니다. 익숙해진다면 수건으로 바꿔보시는 것도 좋아요.";
                    private static final String V_8_6 = "유연성은 하루아침에 늘어나진 않아요. 점수가 낮다고 너무 실망하지 마시고 하루에 2cm만 더 내려간다는 생각으로 유연성을 길러주세요.";
                    private static final String V_8_7 = "엉덩이를 뒤로 빼며 상체를 구부리지않고 발끝을 찍는다는 느낌을 주셔야해요. 유연성을 기르기 위해서는 꾸준한 연습이 필요해요.";
                    private static final String V_8_8 = "점수가 낮다고 너무 실망하지마세요. 처음에는 어깨넓이정도로 다리를 벌리고 스쿼트를 실행해보세요.";
                    private static final String V_8_9 = "팔과 상체 그리고 다리가 1자 형태가 되도록 쭉 뻗어주세요.";
                    private static final String V_8_10 = "플라잉은 무엇보다 오래 버티는 것이 중요해요. 천천히 그리고 여유 있게 실행해주세요.";
                    private static final String V_8_11 = "오늘도 끝까지 마무리 잘하셨어요! 다음에는 조금 더 나은 점수를 받을 수 있도록 이미지 트레이닝을 해보는 것은 어떨까요?";

                    private static final int V_8_1_REF = 50;
                    private static final int V_8_2_REF = 0;
                    private static final int V_8_3_REF = 0;
                    private static final int V_8_4_REF = 0;
                    private static final int V_8_5_REF = 0;
                    private static final int V_8_6_REF = 0;
                    private static final int V_8_7_REF = 0;
                    private static final int V_8_8_REF = 0;
                    private static final int V_8_9_REF = 0;
                    private static final int V_8_10_REF = 0;
                    private static final int V_8_11_REF = 0;
                }

                private final class Section3 {
                    private static final String V_8_1 = "오늘 컨디션이 좋지 않으시군요. 무리하지 않게 천천히 운동을 해주세요.";
                    private static final String V_8_2 = "";
                    private static final String V_8_3 = "";
                    private static final String V_8_4 = "";
                    private static final String V_8_5 = "";
                    private static final String V_8_6 = "";
                    private static final String V_8_7 = "";
                    private static final String V_8_8 = "";
                    private static final String V_8_9 = "";
                    private static final String V_8_10 = "";
                    private static final String V_8_11 = "";

                    private static final int V_8_1_REF = 0;
                    private static final int V_8_2_REF = -1;
                    private static final int V_8_3_REF = -1;
                    private static final int V_8_4_REF = -1;
                    private static final int V_8_5_REF = -1;
                    private static final int V_8_6_REF = -1;
                    private static final int V_8_7_REF = -1;
                    private static final int V_8_8_REF = -1;
                    private static final int V_8_9_REF = -1;
                    private static final int V_8_10_REF = -1;
                    private static final int V_8_11_REF = -1;
                }
            }

            public final class Ref {
                public final class Count {
                    public static final int V_8_1 = 20;
                    public static final int V_8_2 = 20;
                    public static final int V_8_3 = 10;
                    public static final int V_8_4 = 10;
                    public static final int V_8_5 = 10;
                    public static final int V_8_6 = 5;
                    public static final int V_8_7 = 10;
                    public static final int V_8_8 = 10;
                    public static final int V_8_9 = 5;
                    public static final int V_8_10 = 10;
                    public static final int V_8_11 = 10;
                }
            }

            private final class Section {
                private static final int VIDEO_8_1_START_SEC = 42;
                private static final int VIDEO_8_1_END_SEC = 107;
                private static final int VIDEO_8_2_START_SEC = 140;
                private static final int VIDEO_8_2_END_SEC = 199;
                private static final int VIDEO_8_3_START_SEC = 232;
                private static final int VIDEO_8_3_END_SEC = 263;
                private static final int VIDEO_8_4_START_SEC = 296;
                private static final int VIDEO_8_4_END_SEC = 338;
                private static final int VIDEO_8_5_START_SEC = 372;
                private static final int VIDEO_8_5_END_SEC = 420;
                private static final int VIDEO_8_6_START_SEC = 454;
                private static final int VIDEO_8_6_END_SEC = 489;
                private static final int VIDEO_8_7_START_SEC = 523;
                private static final int VIDEO_8_7_END_SEC = 569;
                private static final int VIDEO_8_8_START_SEC = 602;
                private static final int VIDEO_8_8_END_SEC = 632;
                private static final int VIDEO_8_9_START_SEC = 666;
                private static final int VIDEO_8_9_END_SEC = 692;
                private static final int VIDEO_8_10_START_SEC = 726;
                private static final int VIDEO_8_10_END_SEC = 759;
                private static final int VIDEO_8_11_START_SEC = 793;
                private static final int VIDEO_8_11_END_SEC = 833;

                private static final int VIDEO_8_1_SKIP_START_SEC = 73;
                private static final int VIDEO_8_1_SKIP_END_SEC = 76;
                private static final int VIDEO_8_2_SKIP_START_SEC = 168;
                private static final int VIDEO_8_2_SKIP_END_SEC = 170;

                private static final int VIDEO_8_1_EXTRA_SEPERATE_TIME = VIDEO_8_1_SKIP_END_SEC;
                private static final int VIDEO_8_2_EXTRA_SEPERATE_TIME = VIDEO_8_2_SKIP_END_SEC;

                private static final int VIDEO_8_1_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_8_2_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_8_3_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_8_4_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_8_5_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_8_6_TOTAL_SCORE_DELAY = 4;
                private static final int VIDEO_8_7_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_8_8_TOTAL_SCORE_DELAY = 4;
                private static final int VIDEO_8_9_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_8_10_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_8_11_TOTAL_SCORE_DELAY = 3;

                private static final int VIDEO_8_1_TOTAL_SCORE_DISPLAY_TIME = 17;
                private static final int VIDEO_8_2_TOTAL_SCORE_DISPLAY_TIME = 18;
                private static final int VIDEO_8_3_TOTAL_SCORE_DISPLAY_TIME = 18;
                private static final int VIDEO_8_4_TOTAL_SCORE_DISPLAY_TIME = 15;
                private static final int VIDEO_8_5_TOTAL_SCORE_DISPLAY_TIME = 20;
                private static final int VIDEO_8_6_TOTAL_SCORE_DISPLAY_TIME = 18;
                private static final int VIDEO_8_7_TOTAL_SCORE_DISPLAY_TIME = 17;
                private static final int VIDEO_8_8_TOTAL_SCORE_DISPLAY_TIME = 21;
                private static final int VIDEO_8_9_TOTAL_SCORE_DISPLAY_TIME = 19;
                private static final int VIDEO_8_10_TOTAL_SCORE_DISPLAY_TIME = 17;
                private static final int VIDEO_8_11_TOTAL_SCORE_DISPLAY_TIME = 8;

                private static final int VIDEO_8_1_EXTRA_DELAY = 0;
                private static final int VIDEO_8_2_EXTRA_DELAY = 0;
                private static final int VIDEO_8_3_EXTRA_DELAY = 0;
                private static final int VIDEO_8_4_EXTRA_DELAY = 0;
                private static final int VIDEO_8_5_EXTRA_DELAY = 0;
                private static final int VIDEO_8_6_EXTRA_DELAY = 0;
                private static final int VIDEO_8_7_EXTRA_DELAY = 0;
                private static final int VIDEO_8_8_EXTRA_DELAY = 0;
                private static final int VIDEO_8_9_EXTRA_DELAY = 0;
                private static final int VIDEO_8_10_EXTRA_DELAY = 0;
                private static final int VIDEO_8_11_EXTRA_DELAY = 0;
            }

            private final class PlayTime {
                private static final int V_8_1 = (Section.VIDEO_8_1_END_SEC - Section.VIDEO_8_1_START_SEC);
                private static final int V_8_2 = (Section.VIDEO_8_2_END_SEC - Section.VIDEO_8_2_START_SEC);
                private static final int V_8_3 = (Section.VIDEO_8_3_END_SEC - Section.VIDEO_8_3_START_SEC);
                private static final int V_8_4 = (Section.VIDEO_8_4_END_SEC - Section.VIDEO_8_4_START_SEC);
                private static final int V_8_5 = (Section.VIDEO_8_5_END_SEC - Section.VIDEO_8_5_START_SEC);
                private static final int V_8_6 = (Section.VIDEO_8_6_END_SEC - Section.VIDEO_8_6_START_SEC);
                private static final int V_8_7 = (Section.VIDEO_8_7_END_SEC - Section.VIDEO_8_7_START_SEC);
                private static final int V_8_8 = (Section.VIDEO_8_8_END_SEC - Section.VIDEO_8_8_START_SEC);
                private static final int V_8_9 = (Section.VIDEO_8_9_END_SEC - Section.VIDEO_8_9_START_SEC);
                private static final int V_8_10 = (Section.VIDEO_8_10_END_SEC - Section.VIDEO_8_10_START_SEC);
                private static final int V_8_11 = (Section.VIDEO_8_11_END_SEC - Section.VIDEO_8_11_START_SEC);
            }

            public final class G8_1 {
                private static final float SURFACE_THR_1 = -4.0f;
                private static final float PEAK_VALUE_REF_1 = -0.1f;
                private static final float PEAK_VALUE_THR_1 = 1f;
                private static final float AMP_THR_1 = -8f;

                private static final float PEAK_MIN_2 = -3f;
                private static final float AMP_THR_2 = 1f;
                private static final float PTP_THR_2 = -4f;
                private static final float PEAK_VALUE_REF_2 = -1f;
                private static final float PEAK_VALUE_MIN_2 = 1f;
            }

            public final class G8_2 {
                private static final float PEAK_MAX_1 = -2f;
                private static final float AMP_THR_1 = -1f;
                private static final float SURFACE_REF_1 = -80f;

                private static final float PEAK_MIN_2 = -6f;
                private static final float AMP_THR_2 = -13f;
                private static final float SURFACE_REF_2 = -150f;
            }

            public final class G8_3 {
                private static final float PEAK_MAX = -4f;
                private static final float AMP_THR = -8f;
            }

            public final class G8_4 {
                private static final float PEAK_MAX = -4f;
                private static final float VECTOR_REF = 18f;
                private static final float AMP_THR = -8f;
            }

            public final class G8_5 {
                private static final float PEAK_MIN = -5f;
                private static final float AMP_THR = -10f;
                private static final float AMP_REF = 0;
            }

            public final class G8_6 {
                private static final float PEAK_MAX = -5f;
                private static final float AMP_THR = 2f;
                private static final float PEAK_MAX_REF = 0f;
            }

            public final class G8_7 {
                private static final float PEAK_MAX = -7f;
                private static final float AMP_THR = 6f;
                private static final float PEAK_MAX_REF = 0f;
            }

            public final class G8_8 {
                private static final float PEAK_MIN = -6f;
                private static final float PTP_THR = 5f;
                private static final float SURFACE_REF = 100f;
            }

            public final class G8_9 {
                private static final float PTP_THR = 30f;
                private static final float AMP_THR = 10f;
                private static final float PEAK_MAX_REF = 8f;
            }

            public final class G8_10 {
                private static final float PEAK_MAX = -1f;
                private static final float AMP_THR = 5f;
                private static final float PTP_REF = 50f;
                private static final float AMP_REF = 12f;
            }

            public final class G8_11 {
                private static final float PEAK_MIN = -5f;
                private static final float AMP_THR = -10f;
                private static final float PEAK_MIN_REF = 0;
            }

        }

        /* 출산부 2 */
        public final class Group9 {

            public final class Index {
                public static final int V_9_1_R = 801; // 2-1
                public static final int V_9_1 = 802;
                public static final int V_9_2_R = 803; // 2-2
                public static final int V_9_2 = 804;
                public static final int V_9_3_R = 805; // 2-3
                public static final int V_9_3 = 806;
                public static final int V_9_4_R = 807; // 2-4
                public static final int V_9_4 = 808;
                public static final int V_9_5_R = 809; // 2-5
                public static final int V_9_5 = 810;
                public static final int V_9_6_R = 811; // 2-7
                public static final int V_9_6 = 812;
                public static final int V_9_7_R = 813; // 2-8
                public static final int V_9_7 = 814;
                public static final int V_9_8_R = 815; // 2-9
                public static final int V_9_8 = 816;
                public static final int V_9_9_R = 817; // 2-10
                public static final int V_9_9 = 818;
                public static final int V_9_10_R = 819; // 2-11
                public static final int V_9_10 = 820;
                public static final int V_9_11_R = 821; // 2-12
                public static final int V_9_11 = 822;
                public static final int V_9_12_R = 823; // 2-13
                public static final int V_9_12 = 824;

                private static final int C_9_1 = 30201;
                private static final int C_9_2 = 30202;
                private static final int C_9_3 = 30203;
                private static final int C_9_4 = 30204;
                private static final int C_9_5 = 30205;
                private static final int C_9_6 = 30206;
                private static final int C_9_7 = 30207;
                private static final int C_9_8 = 30208;
                private static final int C_9_9 = 30209;
                private static final int C_9_10 = 30210;
                private static final int C_9_11 = 30211;
                private static final int C_9_12 = 30212;
            }

            private final class Comment {
                private final class Section5 {
                    private static final String V_9_1 = "잘하셨어요! 오늘도 끝까지 지금 같은 컨디션으로 열심히 해주세요.";
                    private static final String V_9_2 = "상체 밸런스발달과 힙업이 완벽하게 되고 있어요!";
                    private static final String V_9_3 = "완벽해요! 의자 없이 실행해 보는 것은 어떨까요?";
                    private static final String V_9_4 = "아주 좋아요. 완벽한 런지 동작을 하고 있어요. 무릎에 무리가 가지 않도록 항상 주의해주세요.";
                    private static final String V_9_5 = "완벽해요. 하체 미녀가 되어 가고 있어요! 하체 운동 후 다리를 마사지해주면 더욱 건강하고 매끈한 다리를 가꿀 수 있어요.";
                    private static final String V_9_6 = "아주 좋군요. 혹시 점프를 너무 높게 뛰고 계시진 않나요? 무릎에 무리가 가지 않도록 지면에서 약 15cm정도만 뛰어주세요.";
                    private static final String V_9_7 = "어려운 운동인데 완벽하게 소화하고 있네요! 이번 여름에는 완벽한 복근으로 비키니를 소화해보세요!";
                    private static final String V_9_8 = "완벽한 자세에요! 힙업이 되는게 느껴지시나요?";
                    private static final String V_9_9 = "아주 좋습니다! 상체 밸런스가 아주 좋아요.";
                    private static final String V_9_10 = "완벽한 자세에요! 올해는 뒷태 미녀로 거듭나실거에요.";
                    private static final String V_9_11 = "와! 정말 대단하시네요! 완벽한 스쿼드 자세를 취하고 있어요. 텐션을 넓혀서 스쿼드를 시도해보시겠어요?. 더욱 나은 결과를 얻으실 수 있을 거에요.";
                    private static final String V_9_12 = "오늘도 마지막까지 고생하셨어요! 오늘같은 집중력으로 일주일에 3번 이상 운동을 하시면 멋진 몸매를 만드실 수 있을거에요!";

                    private static final int V_9_1_REF = 90;
                    private static final int V_9_2_REF = 80;
                    private static final int V_9_3_REF = 80;
                    private static final int V_9_4_REF = 75;
                    private static final int V_9_5_REF = 75;
                    private static final int V_9_6_REF = 90;
                    private static final int V_9_7_REF = 80;
                    private static final int V_9_8_REF = 90;
                    private static final int V_9_9_REF = 80;
                    private static final int V_9_10_REF = 90;
                    private static final int V_9_11_REF = 80;
                    private static final int V_9_12_REF = 80;
                }

                private final class Section4 {
                    private static final String V_9_1 = "좋아지고 있어요! 오늘은 더 높은 점수를 획득해보세요.";
                    private static final String V_9_2 = "좋은 자세입니다. 조금 더 상체와 하체 밸런스에 집중해주세요.";
                    private static final String V_9_3 = "허벅지에 근육이 생기는 것이 느껴지시나요? 조금만 더 힘차게 실행해주세요.";
                    private static final String V_9_4 = "혹시 의자를 너무 높을 것을 사용하지 않으셨나요? 허리 정도 오는 의자가 운동에 적합해요.";
                    private static final String V_9_5 = "혹시 의자를 너무 높을 것을 사용하지 않으셨나요? 허리 정도 오는 의자가 운동에 적합해요.";
                    private static final String V_9_6 = "열심히하고 게신데 점수가 높지 않으신가요? 점핑스쿼드는 무엇보다 힘차게 도약하는 것이 중요한 운동입니다.";
                    private static final String V_9_7 = "조금만 더 천천히 그리고 정확하게 실행 보는 것은 어떨까요? 더 높은 점수를 얻으실 수 있을 거에요.";
                    private static final String V_9_8 = "플라잉은 무엇보다 오래 버티는 것이 중요해요. 천천히 그리고 여유 있게 실행해주세요.";
                    private static final String V_9_9 = "어깨를 정확히 90도 각도로 꺾으면 힘차게 뻗어주세요. 상체 밸런스가 좋아지는 것을 느낄 수 있을 거에요.";
                    private static final String V_9_10 = "하이퍼 익스텐션은 무엇보다 자세를 천천히 하며 버티는 게 중요해요. 조금 더 여유를 가지고 운동을 하면 높은 점수를 획득할 수 있어요.";
                    private static final String V_9_11 = "정확도가 조금 아쉽네요. 하지만 연습을 통해서 좋아질 수 있어요. 스쿼트는 하체의 자세도 중요하지만 상체를 꼿꼿이 피는 것도 매우 중요합니다.";
                    private static final String V_9_12 = "오늘도 끝까지 마무리 잘하셨어요! 내일은 조금 더 나은 집중력으로 몸매를 가꾸어 보아요.";

                    private static final int V_9_1_REF = 50;
                    private static final int V_9_2_REF = 60;
                    private static final int V_9_3_REF = 50;
                    private static final int V_9_4_REF = 0;
                    private static final int V_9_5_REF = 0;
                    private static final int V_9_6_REF = 70;
                    private static final int V_9_7_REF = 60;
                    private static final int V_9_8_REF = 0;
                    private static final int V_9_9_REF = 0;
                    private static final int V_9_10_REF = 0;
                    private static final int V_9_11_REF = 50;
                    private static final int V_9_12_REF = 0;
                }

                private final class Section3 {
                    private static final String V_9_1 = "오늘 컨디션이 좋지 않으시군요. 무리하지 않게 천천히 운동을 해주세요.";
                    private static final String V_9_2 = "나쁘지 않아요. 상체가 흔들리지 않고 밸런스에 더욱 신경을 써주세요.";
                    private static final String V_9_3 = "나쁘지 않아요. 일어날 때 엉덩이와 척추에 힘을 주어 바르게 일어나는 연습을 해주세요.";
                    private static final String V_9_4 = "";
                    private static final String V_9_5 = "";
                    private static final String V_9_6 = "많이 힘드신가보내요. 무리하지 않는 범위에서 횟수를 조금 씩 늘려 보는 것은 어떨까요?";
                    private static final String V_9_7 = "점수가 높지 않아서 실망하셨나요? 어려운 운동이기 때문에 꾸준한 연습이 필요합니다. 힘드시면 횟수보다는 동작의 정확성에 신경 써보는 것은 어떨까요?";
                    private static final String V_9_8 = "";
                    private static final String V_9_9 = "";
                    private static final String V_9_10 = "";
                    private static final String V_9_11 = "아직 많이 어려울 수 있어요. 스쿼트는 꾸준한 연습을 통해서 좋아질 수 있어요. 횟수보다는 동작 자체에 집중해주세요.";
                    private static final String V_9_12 = "";

                    private static final int V_9_1_REF = 0;
                    private static final int V_9_2_REF = 0;
                    private static final int V_9_3_REF = 0;
                    private static final int V_9_4_REF = -1;
                    private static final int V_9_5_REF = -1;
                    private static final int V_9_6_REF = 0;
                    private static final int V_9_7_REF = 40;
                    private static final int V_9_8_REF = -1;
                    private static final int V_9_9_REF = -1;
                    private static final int V_9_10_REF = -1;
                    private static final int V_9_11_REF = 0;
                    private static final int V_9_12_REF = -1;
                }

                private final class Section2 {
                    private static final String V_9_7 = "실망하실 필요 없어요! 천리 길도 한 걸음부터! 우선은 천천히 정확하게 따라 하는 것이 중요해요.";

                    private static final int V_9_7_REF = 0;
                }
            }

            public final class Ref {
                public final class Count {
                    public static final int V_9_1 = 10;
                    public static final int V_9_2 = 10;
                    public static final int V_9_3 = 10;
                    public static final int V_9_4 = 20;
                    public static final int V_9_5 = 20;
                    public static final int V_9_6 = 10;
                    public static final int V_9_7 = 10;
                    public static final int V_9_8 = 10;
                    public static final int V_9_9 = 20;
                    public static final int V_9_10 = 10;
                    public static final int V_9_11 = 10;
                    public static final int V_9_12 = 5;
                }
            }

            private final class Section {
                private static final int VIDEO_9_1_START_SEC = 32;
                private static final int VIDEO_9_1_END_SEC = 79;
                private static final int VIDEO_9_2_START_SEC = 113;
                private static final int VIDEO_9_2_END_SEC = 148;
                private static final int VIDEO_9_3_START_SEC = 180;
                private static final int VIDEO_9_3_END_SEC = 207;
                private static final int VIDEO_9_4_START_SEC = 239;
                private static final int VIDEO_9_4_END_SEC = 289;
                private static final int VIDEO_9_5_START_SEC = 322;
                private static final int VIDEO_9_5_END_SEC = 370;
                private static final int VIDEO_9_6_START_SEC = 403;
                private static final int VIDEO_9_6_END_SEC = 425;
                private static final int VIDEO_9_7_START_SEC = 458;
                private static final int VIDEO_9_7_END_SEC = 488;
                private static final int VIDEO_9_8_START_SEC = 521;
                private static final int VIDEO_9_8_END_SEC = 557;
                private static final int VIDEO_9_9_START_SEC = 591;
                private static final int VIDEO_9_9_END_SEC = 643;
                private static final int VIDEO_9_10_START_SEC = 677;
                private static final int VIDEO_9_10_END_SEC = 718;
                private static final int VIDEO_9_11_START_SEC = 751;
                private static final int VIDEO_9_11_END_SEC = 795;
                private static final int VIDEO_9_12_START_SEC = 828;
                private static final int VIDEO_9_12_END_SEC = 856;

                private static final int VIDEO_9_4_SKIP_START_SEC = 263;
                private static final int VIDEO_9_4_SKIP_END_SEC = 265;
                private static final int VIDEO_9_5_SKIP_START_SEC = 345;
                private static final int VIDEO_9_5_SKIP_END_SEC = 347;
                private static final int VIDEO_9_9_SKIP_START_SEC = 616;
                private static final int VIDEO_9_9_SKIP_END_SEC = 618;

                private static final int VIDEO_9_1_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_9_2_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_9_3_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_9_4_TOTAL_SCORE_DELAY = 2;
                private static final int VIDEO_9_5_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_9_6_TOTAL_SCORE_DELAY = 4;
                private static final int VIDEO_9_7_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_9_8_TOTAL_SCORE_DELAY = 4;
                private static final int VIDEO_9_9_TOTAL_SCORE_DELAY = 4;
                private static final int VIDEO_9_10_TOTAL_SCORE_DELAY = 3;
                private static final int VIDEO_9_11_TOTAL_SCORE_DELAY = 4;
                private static final int VIDEO_9_12_TOTAL_SCORE_DELAY = 3;

                private static final int VIDEO_9_1_TOTAL_SCORE_DISPLAY_TIME = 21;
                private static final int VIDEO_9_2_TOTAL_SCORE_DISPLAY_TIME = 18;
                private static final int VIDEO_9_3_TOTAL_SCORE_DISPLAY_TIME = 8;
                private static final int VIDEO_9_4_TOTAL_SCORE_DISPLAY_TIME = 18;
                private static final int VIDEO_9_5_TOTAL_SCORE_DISPLAY_TIME = 12;
                private static final int VIDEO_9_6_TOTAL_SCORE_DISPLAY_TIME = 19;
                private static final int VIDEO_9_7_TOTAL_SCORE_DISPLAY_TIME = 20;
                private static final int VIDEO_9_8_TOTAL_SCORE_DISPLAY_TIME = 16;
                private static final int VIDEO_9_9_TOTAL_SCORE_DISPLAY_TIME = 16;
                private static final int VIDEO_9_10_TOTAL_SCORE_DISPLAY_TIME = 20;
                private static final int VIDEO_9_11_TOTAL_SCORE_DISPLAY_TIME = 19;
                private static final int VIDEO_9_12_TOTAL_SCORE_DISPLAY_TIME = 6;

                private static final int VIDEO_9_1_EXTRA_DELAY = 0;
                private static final int VIDEO_9_2_EXTRA_DELAY = 0;
                private static final int VIDEO_9_3_EXTRA_DELAY = 0;
                private static final int VIDEO_9_4_EXTRA_DELAY = 0;
                private static final int VIDEO_9_5_EXTRA_DELAY = 0;
                private static final int VIDEO_9_6_EXTRA_DELAY = 0;
                private static final int VIDEO_9_7_EXTRA_DELAY = 0;
                private static final int VIDEO_9_8_EXTRA_DELAY = 0;
                private static final int VIDEO_9_9_EXTRA_DELAY = 0;
                private static final int VIDEO_9_10_EXTRA_DELAY = 0;
                private static final int VIDEO_9_11_EXTRA_DELAY = 0;
                private static final int VIDEO_9_12_EXTRA_DELAY = 0;
            }

            private final class PlayTime {
                private static final int V_9_1 = (Section.VIDEO_9_1_END_SEC - Section.VIDEO_9_1_START_SEC);
                private static final int V_9_2 = (Section.VIDEO_9_2_END_SEC - Section.VIDEO_9_2_START_SEC);
                private static final int V_9_3 = (Section.VIDEO_9_3_END_SEC - Section.VIDEO_9_3_START_SEC);
                private static final int V_9_4 = (Section.VIDEO_9_4_END_SEC - Section.VIDEO_9_4_START_SEC);
                private static final int V_9_5 = (Section.VIDEO_9_5_END_SEC - Section.VIDEO_9_5_START_SEC);
                private static final int V_9_6 = (Section.VIDEO_9_6_END_SEC - Section.VIDEO_9_6_START_SEC);
                private static final int V_9_7 = (Section.VIDEO_9_7_END_SEC - Section.VIDEO_9_7_START_SEC);
                private static final int V_9_8 = (Section.VIDEO_9_8_END_SEC - Section.VIDEO_9_8_START_SEC);
                private static final int V_9_9 = (Section.VIDEO_9_9_END_SEC - Section.VIDEO_9_9_START_SEC);
                private static final int V_9_10 = (Section.VIDEO_9_10_END_SEC - Section.VIDEO_9_10_START_SEC);
                private static final int V_9_11 = (Section.VIDEO_9_11_END_SEC - Section.VIDEO_9_11_START_SEC);
                private static final int V_9_12 = (Section.VIDEO_9_12_END_SEC - Section.VIDEO_9_12_START_SEC);
            }

            public final class G9_1 {
                private static final float AMP_THR = -20f;
                private static final float PTP_THR = 20f;
                private static final float PEAK_MIN = 4f;
                private static final float SURFACE_REF = 400f;
            }

            public final class G9_2 {
                private static final float SURFACE_THR = 32f;
                private static final float AMP_THR = 4.5f;
                private static final float PEAK_MAX = -7f;
                private static final float AMP_REF = 11f;
            }

            public final class G9_3 {
                private static final float PEAK_MAX = -4f;
                private static final float AMP_THR = 4f;
                private static final float SURFACE_REF = 140f;
            }

            public final class G9_4 {
                private static final float PEAK_MIN = -3f;
                private static final float AMP_THR = -1.2f;
                private static final float AMP_REF = -12f;
            }

            public final class G9_5 {
                private static final float PEAK_MIN = -3f;
                private static final float AMP_THR = -1.2f;
                private static final float PTP_THR = -20f;
                private static final float AMP_REF = -12f;
            }

            public final class G9_6 {
                private static final float PEAK_MIN = -6f;
                private static final float PEAK_REF = -19f;
                private static final float AMP_THR = -7f;
                private static final float AMP_REF = -10f;
                private static final float PTP_THR = -22f;
            }

            public final class G9_7 {
                private static final float PEAK_MIN2 = 2f;
                private static final float PEAK_MIN = 10f;
                private static final float AMP_THR = -3f;
                private static final float PTP_THR = -30f;
            }

            public final class G9_8 {
                private static final float PEAK_MIN = -3f;
                private static final float PEAK_MIN_Z = 8f;
                private static final float AMP_THR = -7f;
                private static final float AMP_REF = 9f;
            }

            public final class G9_9 {
                private static final float PEAK_MAX = 8f;
                private static final float AMP_THR = 2f;
                private static final float AMP_REF = 8f;
            }

            public final class G9_10 {
                private static final float PEAK_MIN = -6f;
                private static final float PTP_THR = -15f;
            }

            public final class G9_11 {
                private static final float AMP_THR = 4f;
                private static final float PTP_THR = 15f;
                private static final float AMP_REF = 13f;
            }

            public final class G9_12 {
                private static final float SURFACE_THR = 1f;
                private static final float PEAK_MAX = 5f;
                private static final float SURFACE_REF = 25f;
            }
        }
    }

    /** Array **/
    private final ListQueueUtil<Integer> HRQueue = new ListQueueUtil<Integer>();
    private final ListQueueUtil<Integer> HRQueueD = new ListQueueUtil<Integer>();
    private final ListQueueUtil<Integer> reset_HRQueue = new ListQueueUtil<Integer>();
    private final ListQueueUtil<Integer> reset_HRQueueD = new ListQueueUtil<Integer>();
    private final ListQueueUtil<Integer> accuracyQueue = new ListQueueUtil<Integer>();
    private final ListQueueUtil<Integer> accuracyQueueD = new ListQueueUtil<Integer>();
    //---raw데이터 파일을 저장하는 어레이 리스트들
    private final ArrayList<Float> parseACC_X = new ArrayList<Float>();
    private final ArrayList<Float> parseACC_Y = new ArrayList<Float>();
    private final ArrayList<Float> parseACC_Z = new ArrayList<Float>();
    private final ArrayList<Float> parseHR = new ArrayList<Float>();

    private final double[] toDouble = new double[BluetoothManager.arrayLength];
    private final double[] toDoubleD = new double[BluetoothManager.arrayLength];
    private final int[] out = new int[2];
    private final int[] outD = new int[2];

    /* AART Engine */
    KIST_AART m_KIST = new KIST_AART();
    KIST_AART_output mOut = new KIST_AART_output();
    KIST_AART m_KISTD = new KIST_AART();
    KIST_AART_output mOutD = new KIST_AART_output();

    /** Handler **/
    private Handler mHandler, mHandlerD;
    private Handler mHandler_AccuracyLock, mHandler_AccuracyLockD;
    private Handler mHandler_avgHR;

    /** Runnable **/
    private Runnable mRunnable_AccuracyLock, mRunnable_AccuracyLockD;
    private Runnable mRunnable_avgHR;

    /** Interface **/
    private static IViewComment mIView;

    public static void registCallback(IViewComment cb) {
        mIView = cb;
    }

    public static void unregistCallback() {
        mIView = null;
    }

    protected static void initInstance(Context context) {
        if (mVManager == null)
            mVManager = new VideoManager(context);
    }

    public static VideoManager getInstance() {
        return mVManager;
    }

    private VideoManager(Context context) {
        Log.d(tag, "coach version : " + version);

        mContext = context;
        mConfig = ConfigManager.getInstance();
        mPre = new PreferencesManager(context);
        mBle = BluetoothManager.getInstance(context);
        mSdPath = getSDPath();
        if (mHandler == null)
            mHandler = new Handler();
        if (mHandlerD == null)
            mHandlerD = new Handler();
        if (mHandler_avgHR == null)
            mHandler_avgHR = new Handler();

        if (mRunnable_avgHR == null) {
            mRunnable_avgHR = new Runnable() {
                @Override
                public void run() {
                    set_HRLock = false;
                }
            };
        }
        if (mRunnable_AccuracyLockD == null) {
            mRunnable_AccuracyLockD = new Runnable() {

                @Override
                public void run() {
                    set_AccuracyLockD = false;
                }
            };
        }
        if (mRunnable_AccuracyLock == null) {
            mRunnable_AccuracyLock = new Runnable() {
                @Override
                public void run() {
                    set_AccuracyLock = false;
                }
            };
        }

        arrayStartTime = new ArrayList<Long>();

		/*
		 * t = new Timer(); t.schedule(new TimerTask() {
		 *
		 * @Override public void run() { startVibrate(); } }, 0, 5000);
		 */
    }

    /**
     * 동영상 ID 설정.
     *
     * @param videoID
     *            재생하는 동영상 ID.
     * @return true:성공, false:실패(이미 재생중. end function을 실행 시켜주세요.)
     */
    public boolean setVideoID(int videoID) {
        if (!isPlaying) {
            this.videoID = videoID;
            return true;
        } else
            return false;
    }

    /**
     * Raw 데이터 저장 여부를 결정.
     *
     *            true:저장, false:저장하지 않음.
     */
    public void setChkSave(boolean setSave) {
        Log.d(tag,"setChkSave:"+setSave);
        this.setSave = setSave;
    }

    /**
     * Debug를 위해 하단 정보 표시줄을 2줄로 표시.
     *
     *
     */
    public void setChkDebugPlay(boolean setDebugPlay) {
        // Log.d(tag,"setChkSave:"+setSave);
        this.setDebugPlay = setDebugPlay;
    }

    /**
     * 2인용 플레이. Debug 모드 중단.
     *
     *
     */
    public void setDualMode(boolean setDual) {
        // Log.d(tag,"setChkSave:"+setSave);
        this.setDual = setDual;
        if (setDual)
            setDebugPlay = false;
    }

    private void setMac() {
        mac1 = mBle.getRemoteMac(BluetoothManager.REMOTE_COUNT_1);
        mac2 = mBle.getRemoteMac(BluetoothManager.REMOTE_COUNT_2);
    }

    private String getMac(int n) {
        if (n == BluetoothManager.REMOTE_COUNT_1)
            return mac1;
        else
            return mac2;
    }

    /**
     * Raw 데이터 기록을 위한 파일명 설정.
     *
     * @param name
     *            파일명.
     */
    public void setRecordName(String name) {
        recordName = name;
    }

    private String toString(int videoID) {
        String name = "";
        switch (videoID) {
            case SET_VIDEO_ID_1:
                name = "M1";
                break;
            case SET_VIDEO_ID_2:
                name = "M2";
                break;
            case SET_VIDEO_ID_3:
                name = "M3";
                break;
            case SET_VIDEO_ID_4:
                name = "M4";
                break;
            case SET_VIDEO_ID_5:
                name = "M5";
                break;
            case SET_VIDEO_ID_6:
                name = "M6";
                break;
            case SET_VIDEO_ID_7:
                name = "M7";
                break;
        }
        return name;
    }

    /*
	 * private void setMode() { PreferencesManager mPre = new
	 * PreferencesManager(mContext); NOW_REFERENCE = mPre.getUserReference(); }
	 */
    private String setStartTag(int vid) {
        String tag = null;
        switch (vid) {
            case VideoParameter.Group1.Index.V_1_1:
                tag = "[1-1] 시작";
                break;
            case VideoParameter.Group1.Index.V_1_8:
                tag = "[1-8] 시작";
                break;
            case VideoParameter.Group1.Index.V_1_3:
                tag = "[1-3] 시작";
                break;
            case VideoParameter.Group1.Index.V_1_6:
                tag = "[1-6] 시작";
                break;
            case VideoParameter.Group1.Index.V_1_16:
                tag = "[1-16] 시작";
                break;

            case VideoParameter.Group2.Index.V_2_1:
                tag = "[2-1] 시작";
                break;
            case VideoParameter.Group2.Index.V_2_2:
                tag = "[2-2] 시작";
                break;
            case VideoParameter.Group2.Index.V_2_3:
                tag = "[2-3] 시작";
                break;
            case VideoParameter.Group2.Index.V_2_4:
                tag = "[2-4] 시작";
                break;
            case VideoParameter.Group2.Index.V_2_6:
                tag = "[2-6] 시작";
                break;

            case VideoParameter.Group3.Index.V_3_1:
                tag = "[3-1] 시작";
                break;
            case VideoParameter.Group3.Index.V_3_4:
                tag = "[3-4] 시작";
                break;
            case VideoParameter.Group3.Index.V_3_5:
                tag = "[3-5] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_1:
                tag = "[4-1] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_2:
                tag = "[4-2] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_3:
                tag = "[4-3] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_4:
                tag = "[4-4] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_5:
                tag = "[4-5] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_6:
                tag = "[4-6] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_7:
                tag = "[4-7] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_8:
                tag = "[4-8] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_9:
                tag = "[4-9] 시작";
                break;
            case VideoParameter.Group4.Index.V_4_10:
                tag = "[4-10] 시작";
                break;

            case VideoParameter.Group5.Index.V_5_1:
                tag = "[5-1] 시작";
                break;
            case VideoParameter.Group5.Index.V_5_2:
                tag = "[5-2] 시작";
                break;
            case VideoParameter.Group5.Index.V_5_3:
                tag = "[5-3] 시작";
                break;
            case VideoParameter.Group5.Index.V_5_4:
                tag = "[5-4] 시작";
                break;
            case VideoParameter.Group5.Index.V_5_5:
                tag = "[5-5] 시작";
                break;
            case VideoParameter.Group5.Index.V_5_6:
                tag = "[5-6] 시작";
                break;
            case VideoParameter.Group5.Index.V_5_7:
                tag = "[5-7] 시작";
                break;
            case VideoParameter.Group5.Index.V_5_8:
                tag = "[5-8] 시작";
                break;
            case VideoParameter.Group5.Index.V_5_9:
                tag = "[5-9] 시작";
                break;
            case VideoParameter.Group5.Index.V_5_10:
                tag = "[5-10] 시작";
                break;

            case VideoParameter.Group6.Index.V_6_1:
                tag = "[6-1] 시작";
                break;
            case VideoParameter.Group6.Index.V_6_2:
                tag = "[6-2] 시작";
                break;
            case VideoParameter.Group6.Index.V_6_3:
                tag = "[6-3] 시작";
                break;
            case VideoParameter.Group6.Index.V_6_4:
                tag = "[6-4] 시작";
                break;
            case VideoParameter.Group6.Index.V_6_5:
                tag = "[6-5] 시작";
                break;
            case VideoParameter.Group6.Index.V_6_6:
                tag = "[6-6] 시작";
                break;
            case VideoParameter.Group6.Index.V_6_7:
                tag = "[6-7] 시작";
                break;
            case VideoParameter.Group6.Index.V_6_8:
                tag = "[6-8] 시작";
                break;
            case VideoParameter.Group6.Index.V_6_9:
                tag = "[6-9] 시작";
                break;
            case VideoParameter.Group6.Index.V_6_10:
                tag = "[6-10] 시작";
                break;

            case VideoParameter.Group7.Index.V_7_1:
                tag = "[7-1] 시작";
                break;
            case VideoParameter.Group7.Index.V_7_2:
                tag = "[7-2] 시작";
                break;
            case VideoParameter.Group7.Index.V_7_3:
                tag = "[7-3] 시작";
                break;
            case VideoParameter.Group7.Index.V_7_4:
                tag = "[7-4] 시작";
                break;
            case VideoParameter.Group7.Index.V_7_5:
                tag = "[7-5] 시작";
                break;
            case VideoParameter.Group7.Index.V_7_6:
                tag = "[7-6] 시작";
                break;
            case VideoParameter.Group7.Index.V_7_7:
                tag = "[7-7] 시작";
                break;
            case VideoParameter.Group7.Index.V_7_8:
                tag = "[7-8] 시작";
                break;
            case VideoParameter.Group7.Index.V_7_9:
                tag = "[7-9] 시작";
                break;
            case VideoParameter.Group7.Index.V_7_10:
                tag = "[7-10] 시작";
                break;
        }
        return tag;
    }

    private String setEndTag(int vid) {
        String tag = null;
        switch (vid) {
            case VideoParameter.Group1.Index.V_1_1:
                tag = "[1-1] 끝";
                break;
            case VideoParameter.Group1.Index.V_1_8:
                tag = "[1-8] 끝";
                break;
            case VideoParameter.Group1.Index.V_1_3:
                tag = "[1-3] 끝";
                break;
            case VideoParameter.Group1.Index.V_1_6:
                tag = "[1-6] 끝";
                break;
            case VideoParameter.Group1.Index.V_1_16:
                tag = "[1-16] 끝";
                break;

            case VideoParameter.Group2.Index.V_2_1:
                tag = "[2-1] 끝";
                break;
            case VideoParameter.Group2.Index.V_2_2:
                tag = "[2-2] 끝";
                break;
            case VideoParameter.Group2.Index.V_2_3:
                tag = "[2-3] 끝";
                break;
            case VideoParameter.Group2.Index.V_2_4:
                tag = "[2-4] 끝";
                break;
            case VideoParameter.Group2.Index.V_2_6:
                tag = "[2-6] 끝";
                break;

            case VideoParameter.Group3.Index.V_3_1:
                tag = "[3-1] 끝";
                break;
            case VideoParameter.Group3.Index.V_3_4:
                tag = "[3-4] 끝";
                break;
            case VideoParameter.Group3.Index.V_3_5:
                tag = "[3-5] 끝";
                break;

            case VideoParameter.Group4.Index.V_4_1:
                tag = "[4-1] 끝";
                break;
            case VideoParameter.Group4.Index.V_4_2:
                tag = "[4-2] 끝";
                break;
            case VideoParameter.Group4.Index.V_4_3:
                tag = "[4-3] 끝";
                break;
            case VideoParameter.Group4.Index.V_4_4:
                tag = "[4-4] 끝";
                break;
            case VideoParameter.Group4.Index.V_4_5:
                tag = "[4-5] 끝";
                break;
            case VideoParameter.Group4.Index.V_4_6:
                tag = "[4-6] 끝";
                break;
            case VideoParameter.Group4.Index.V_4_7:
                tag = "[4-7] 끝";
                break;
            case VideoParameter.Group4.Index.V_4_8:
                tag = "[4-8] 끝";
                break;
            case VideoParameter.Group4.Index.V_4_9:
                tag = "[4-9] 끝";
                break;
            case VideoParameter.Group4.Index.V_4_10:
                tag = "[4-10] 끝";
                break;

            case VideoParameter.Group5.Index.V_5_1:
                tag = "[5-1] 끝";
                break;
            case VideoParameter.Group5.Index.V_5_2:
                tag = "[5-2] 끝";
                break;
            case VideoParameter.Group5.Index.V_5_3:
                tag = "[5-3] 끝";
                break;
            case VideoParameter.Group5.Index.V_5_4:
                tag = "[5-4] 끝";
                break;
            case VideoParameter.Group5.Index.V_5_5:
                tag = "[5-5] 끝";
                break;
            case VideoParameter.Group5.Index.V_5_6:
                tag = "[5-6] 끝";
                break;
            case VideoParameter.Group5.Index.V_5_7:
                tag = "[5-7] 끝";
                break;
            case VideoParameter.Group5.Index.V_5_8:
                tag = "[5-8] 끝";
                break;
            case VideoParameter.Group5.Index.V_5_9:
                tag = "[5-9] 끝";
                break;
            case VideoParameter.Group5.Index.V_5_10:
                tag = "[5-10] 끝";
                break;

            case VideoParameter.Group6.Index.V_6_1:
                tag = "[6-1] 끝";
                break;
            case VideoParameter.Group6.Index.V_6_2:
                tag = "[6-2] 끝";
                break;
            case VideoParameter.Group6.Index.V_6_3:
                tag = "[6-3] 끝";
                break;
            case VideoParameter.Group6.Index.V_6_4:
                tag = "[6-4] 끝";
                break;
            case VideoParameter.Group6.Index.V_6_5:
                tag = "[6-5] 끝";
                break;
            case VideoParameter.Group6.Index.V_6_6:
                tag = "[6-6] 끝";
                break;
            case VideoParameter.Group6.Index.V_6_7:
                tag = "[6-7] 끝";
                break;
            case VideoParameter.Group6.Index.V_6_8:
                tag = "[6-8] 끝";
                break;
            case VideoParameter.Group6.Index.V_6_9:
                tag = "[6-9] 끝";
                break;
            case VideoParameter.Group6.Index.V_6_10:
                tag = "[6-10] 끝";
                break;

            case VideoParameter.Group7.Index.V_7_1:
                tag = "[7-1] 끝";
                break;
            case VideoParameter.Group7.Index.V_7_2:
                tag = "[7-2] 끝";
                break;
            case VideoParameter.Group7.Index.V_7_3:
                tag = "[7-3] 끝";
                break;
            case VideoParameter.Group7.Index.V_7_4:
                tag = "[7-4] 끝";
                break;
            case VideoParameter.Group7.Index.V_7_5:
                tag = "[7-5] 끝";
                break;
            case VideoParameter.Group7.Index.V_7_6:
                tag = "[7-6] 끝";
                break;
            case VideoParameter.Group7.Index.V_7_7:
                tag = "[7-7] 끝";
                break;
            case VideoParameter.Group7.Index.V_7_8:
                tag = "[7-8] 끝";
                break;
            case VideoParameter.Group7.Index.V_7_9:
                tag = "[7-9] 끝";
                break;
            case VideoParameter.Group7.Index.V_7_10:
                tag = "[7-10] 끝";
                break;
        }
        return tag;
    }

    private int convertExerIdx(int exer_idx) {
        switch (exer_idx) {
            case VideoParameter.Group1.Index.V_1_1:
                return VideoParameter.Group1.Index.C_1_1;
            case VideoParameter.Group1.Index.V_1_8:
                return VideoParameter.Group1.Index.C_1_8;
            case VideoParameter.Group1.Index.V_1_3:
                return VideoParameter.Group1.Index.C_1_3;
            case VideoParameter.Group1.Index.V_1_6:
                return VideoParameter.Group1.Index.C_1_6;
            case VideoParameter.Group1.Index.V_1_16:
                return VideoParameter.Group1.Index.C_1_16;

            case VideoParameter.Group2.Index.V_2_1:
                return VideoParameter.Group2.Index.C_2_1;
            case VideoParameter.Group2.Index.V_2_2:
                return VideoParameter.Group2.Index.C_2_2;
            case VideoParameter.Group2.Index.V_2_3:
                return VideoParameter.Group2.Index.C_2_3;
            case VideoParameter.Group2.Index.V_2_4:
                return VideoParameter.Group2.Index.C_2_4;
            case VideoParameter.Group2.Index.V_2_6:
                return VideoParameter.Group2.Index.C_2_6;

            case VideoParameter.Group3.Index.V_3_1:
                return VideoParameter.Group3.Index.C_3_1;
            case VideoParameter.Group3.Index.V_3_4:
                return VideoParameter.Group3.Index.C_3_4;
            case VideoParameter.Group3.Index.V_3_5:
                return VideoParameter.Group3.Index.C_3_5;

            case VideoParameter.Group4.Index.V_4_1:
                return VideoParameter.Group4.Index.C_4_1;
            case VideoParameter.Group4.Index.V_4_2:
                return VideoParameter.Group4.Index.C_4_2;
            case VideoParameter.Group4.Index.V_4_3:
                return VideoParameter.Group4.Index.C_4_3;
            case VideoParameter.Group4.Index.V_4_4:
                return VideoParameter.Group4.Index.C_4_4;
            case VideoParameter.Group4.Index.V_4_5:
                return VideoParameter.Group4.Index.C_4_5;
            case VideoParameter.Group4.Index.V_4_6:
                return VideoParameter.Group4.Index.C_4_6;
            case VideoParameter.Group4.Index.V_4_7:
                return VideoParameter.Group4.Index.C_4_7;
            case VideoParameter.Group4.Index.V_4_8:
                return VideoParameter.Group4.Index.C_4_8;
            case VideoParameter.Group4.Index.V_4_9:
                return VideoParameter.Group4.Index.C_4_9;
            case VideoParameter.Group4.Index.V_4_10:
                return VideoParameter.Group4.Index.C_4_10;

            case VideoParameter.Group5.Index.V_5_1:
                return VideoParameter.Group5.Index.C_5_1;
            case VideoParameter.Group5.Index.V_5_2:
                return VideoParameter.Group5.Index.C_5_2;
            case VideoParameter.Group5.Index.V_5_3:
                return VideoParameter.Group5.Index.C_5_3;
            case VideoParameter.Group5.Index.V_5_4:
                return VideoParameter.Group5.Index.C_5_4;
            case VideoParameter.Group5.Index.V_5_5:
                return VideoParameter.Group5.Index.C_5_5;
            case VideoParameter.Group5.Index.V_5_6:
                return VideoParameter.Group5.Index.C_5_6;
            case VideoParameter.Group5.Index.V_5_7:
                return VideoParameter.Group5.Index.C_5_7;
            case VideoParameter.Group5.Index.V_5_8:
                return VideoParameter.Group5.Index.C_5_8;
            case VideoParameter.Group5.Index.V_5_9:
                return VideoParameter.Group5.Index.C_5_9;
            case VideoParameter.Group5.Index.V_5_10:
                return VideoParameter.Group5.Index.C_5_10;

            case VideoParameter.Group6.Index.V_6_1:
                return VideoParameter.Group6.Index.C_6_1;
            case VideoParameter.Group6.Index.V_6_2:
                return VideoParameter.Group6.Index.C_6_2;
            case VideoParameter.Group6.Index.V_6_3:
                return VideoParameter.Group6.Index.C_6_3;
            case VideoParameter.Group6.Index.V_6_4:
                return VideoParameter.Group6.Index.C_6_4;
            case VideoParameter.Group6.Index.V_6_5:
                return VideoParameter.Group6.Index.C_6_5;
            case VideoParameter.Group6.Index.V_6_6:
                return VideoParameter.Group6.Index.C_6_6;
            case VideoParameter.Group6.Index.V_6_7:
                return VideoParameter.Group6.Index.C_6_7;
            case VideoParameter.Group6.Index.V_6_8:
                return VideoParameter.Group6.Index.C_6_8;
            case VideoParameter.Group6.Index.V_6_9:
                return VideoParameter.Group6.Index.C_6_9;
            case VideoParameter.Group6.Index.V_6_10:
                return VideoParameter.Group6.Index.C_6_10;

            case VideoParameter.Group7.Index.V_7_1:
                return VideoParameter.Group7.Index.C_7_1;
            case VideoParameter.Group7.Index.V_7_2:
                return VideoParameter.Group7.Index.C_7_2;
            case VideoParameter.Group7.Index.V_7_3:
                return VideoParameter.Group7.Index.C_7_3;
            case VideoParameter.Group7.Index.V_7_4:
                return VideoParameter.Group7.Index.C_7_4;
            case VideoParameter.Group7.Index.V_7_5:
                return VideoParameter.Group7.Index.C_7_5;
            case VideoParameter.Group7.Index.V_7_6:
                return VideoParameter.Group7.Index.C_7_6;
            case VideoParameter.Group7.Index.V_7_7:
                return VideoParameter.Group7.Index.C_7_7;
            case VideoParameter.Group7.Index.V_7_8:
                return VideoParameter.Group7.Index.C_7_8;
            case VideoParameter.Group7.Index.V_7_9:
                return VideoParameter.Group7.Index.C_7_9;
            case VideoParameter.Group7.Index.V_7_10:
                return VideoParameter.Group7.Index.C_7_10;

            case VideoParameter.Group8.Index.V_8_1:
                return VideoParameter.Group8.Index.C_8_1;
            case VideoParameter.Group8.Index.V_8_2:
                return VideoParameter.Group8.Index.C_8_2;
            case VideoParameter.Group8.Index.V_8_3:
                return VideoParameter.Group8.Index.C_8_3;
            case VideoParameter.Group8.Index.V_8_4:
                return VideoParameter.Group8.Index.C_8_4;
            case VideoParameter.Group8.Index.V_8_5:
                return VideoParameter.Group8.Index.C_8_5;
            case VideoParameter.Group8.Index.V_8_6:
                return VideoParameter.Group8.Index.C_8_6;
            case VideoParameter.Group8.Index.V_8_7:
                return VideoParameter.Group8.Index.C_8_7;
            case VideoParameter.Group8.Index.V_8_8:
                return VideoParameter.Group8.Index.C_8_8;
            case VideoParameter.Group8.Index.V_8_9:
                return VideoParameter.Group8.Index.C_8_9;
            case VideoParameter.Group8.Index.V_8_10:
                return VideoParameter.Group8.Index.C_8_10;
            case VideoParameter.Group8.Index.V_8_11:
                return VideoParameter.Group8.Index.C_8_11;

            case VideoParameter.Group9.Index.V_9_1:
                return VideoParameter.Group9.Index.C_9_1;
            case VideoParameter.Group9.Index.V_9_2:
                return VideoParameter.Group9.Index.C_9_2;
            case VideoParameter.Group9.Index.V_9_3:
                return VideoParameter.Group9.Index.C_9_3;
            case VideoParameter.Group9.Index.V_9_4:
                return VideoParameter.Group9.Index.C_9_4;
            case VideoParameter.Group9.Index.V_9_5:
                return VideoParameter.Group9.Index.C_9_5;
            case VideoParameter.Group9.Index.V_9_6:
                return VideoParameter.Group9.Index.C_9_6;
            case VideoParameter.Group9.Index.V_9_7:
                return VideoParameter.Group9.Index.C_9_7;
            case VideoParameter.Group9.Index.V_9_8:
                return VideoParameter.Group9.Index.C_9_8;
            case VideoParameter.Group9.Index.V_9_9:
                return VideoParameter.Group9.Index.C_9_9;
            case VideoParameter.Group9.Index.V_9_10:
                return VideoParameter.Group9.Index.C_9_10;
            case VideoParameter.Group9.Index.V_9_11:
                return VideoParameter.Group9.Index.C_9_11;
            case VideoParameter.Group9.Index.V_9_12:
                return VideoParameter.Group9.Index.C_9_12;
            default:
                return 0;
        }
    }
    public void initFileManager(String accname, String hrname) {
        //--- RAWDATA 파일 읽기
        mSdPath = getSDPath();
        //File fpAcc = new File(mSdPath + "/coachData/"+accname+".txt");
        Log.i(tag,"accname="+accname+"\n"+"hrname="+hrname);
        //File fpHr = new File(mSdPath + "/coachData/"+hrname+".txt");
        new Handler().post(new Runnable() {
            @Override
            public void run() {
               File fpHr = new File(hrname);
               try {
                   Scanner scan = new Scanner(fpHr);
                   String line;
                   while (scan.hasNextLine()) {
                       line = scan.nextLine();
                       if (line.startsWith("[") && scan.hasNextLine()) {
                           line = scan.nextLine();
                           Log.i(tag, "HR PARSE=" + line);
                           continue;
                       } else if (!scan.hasNextLine()) {
                           return;
                       }
                       else {
                       String parseLine[] = line.split(" ");
                       parseHR.add(Float.parseFloat(parseLine[0]));
                       }
                   }
                   scan.close();
               } catch (FileNotFoundException e1) {
                   // TODO Auto-generated catch block
                   e1.printStackTrace();
               }
               //--- RAWDATA 파일 읽기
           }
       });

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                File fpAcc = new File(accname);
                try {
                    Scanner scan = new Scanner(fpAcc);
                    String line;
                    while (scan.hasNextLine()) {
                        line = scan.nextLine();
                        if (line.startsWith("[") && scan.hasNextLine()) {
                            line = scan.nextLine();
                            continue;
                        } else if (!scan.hasNextLine()) {
                            return;
                        }else{
                        String parseLine[] = line.split(" ");
                        parseACC_X.add(Float.parseFloat(parseLine[0]));
                        parseACC_Y.add(Float.parseFloat(parseLine[1]));
                        parseACC_Z.add(Float.parseFloat(parseLine[2]));
                        }
                    }
                    scan.close();
                } catch (FileNotFoundException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
    }

    private void init() {
        KIST_AART m_KIST = new KIST_AART();
        KIST_AART_output mOut = new KIST_AART_output();

        try {
            HRQueue.clear();
            accuracyQueue.clear();
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        arrayStartTime.clear();

        setFormula = false;
        isAction = false;
        is_R_Video = false;
        isDisableUI = false;
        is_R_vid = 0;
        is_Save = false;

        isPlaying = false;
        //setSave = false;
        setDebugPlay = false;
        setPlay = true;

        releaseAccuracyLock();

        set_vibrateLock = false;
        set_HRLock = false;
        mHandler_avgHR.removeCallbacks(mRunnable_avgHR);
        if (main_HrArray != null)
            for (int i = 0; i < main_HrArray.length; i++)
                main_HrArray[i] = 0;

        videoID = 0;

        for (int i = 0; i < out.length; i++) {
            out[i] = 0;
        }

        currentPlayVideoIndex = -1;
        currentPosition = 0;

        Count_V_2_1 = 0;
        Count_V_2_4 = 0;
        Count_V_2_6 = 0;
        Count_V_3_1 = 0;
        Count_V_3_4 = 0;
        Count_V_3_5 = 0;

        Count_V_4_3 = 0;
        Count_V_4_8 = 0;
        Count_V_4_10 = 0;

        SumAccuracy_V_2_1 = 0;
        SumAccuracy_V_2_4 = 0;
        SumAccuracy_V_2_6 = 0;
        SumAccuracy_V_3_1 = 0;
        SumAccuracy_V_3_4 = 0;
        SumAccuracy_V_3_5 = 0;

        SumAccuracy_V_4_3 = 0;
        SumAccuracy_V_4_8 = 0;
        SumAccuracy_V_4_10 = 0;

		/* comment vairable */
        count_1_1_A = 0;
        count_1_1_B = 0;
        count_1_1_C = 0;
        count_1_1_D = 0;
        count_1_8_A = 0;
        count_1_8_B = 0;
        count_1_8_C = 0;
        count_1_8_D = 0;
        count_1_3_A = 0;
        count_1_3_B = 0;
        count_1_3_C = 0;
        count_1_3_D = 0;
        count_1_6_A = 0;
        count_1_6_B = 0;
        count_1_6_C = 0;
        count_1_6_D = 0;
        count_1_16_A = 0;
        count_1_16_B = 0;
        count_1_16_C = 0;
        count_1_16_D = 0;

        count_2_1_A = 0;
        count_2_1_B = 0;
        count_2_1_C = 0;
        count_2_1_D = 0;
        count_2_2_A = 0;
        count_2_2_B = 0;
        count_2_2_C = 0;
        count_2_2_D = 0;
        count_2_3_A = 0;
        count_2_3_B = 0;
        count_2_3_C = 0;
        count_2_3_D = 0;
        count_2_4_A = 0;
        count_2_4_B = 0;
        count_2_4_C = 0;
        count_2_4_D = 0;
        count_2_6_A = 0;
        count_2_6_B = 0;
        count_2_6_C = 0;
        count_2_6_D = 0;

        count_4_1_A = 0;
        count_4_1_B = 0;
        count_4_1_C = 0;
        count_4_1_D = 0;
        count_4_2_A = 0;
        count_4_2_B = 0;
        count_4_2_C = 0;
        count_4_2_D = 0;
        count_4_3_A = 0;
        count_4_3_B = 0;
        count_4_3_C = 0;
        count_4_3_D = 0;
        count_4_4_A = 0;
        count_4_4_B = 0;
        count_4_4_C = 0;
        count_4_4_D = 0;
        count_4_5_A = 0;
        count_4_5_B = 0;
        count_4_5_C = 0;
        count_4_5_D = 0;
        count_4_6_A = 0;
        count_4_6_B = 0;
        count_4_6_C = 0;
        count_4_6_D = 0;
        count_4_7_A = 0;
        count_4_7_B = 0;
        count_4_7_C = 0;
        count_4_7_D = 0;
        count_4_8_A = 0;
        count_4_8_B = 0;
        count_4_8_C = 0;
        count_4_8_D = 0;
        count_4_9_A = 0;
        count_4_9_B = 0;
        count_4_9_C = 0;
        count_4_9_D = 0;
        count_4_10_A = 0;
        count_4_10_B = 0;
        count_4_10_C = 0;
        count_4_10_D = 0;

        count_5_1_A = 0;
        count_5_1_B = 0;
        count_5_1_C = 0;
        count_5_1_D = 0;
        count_5_2_A = 0;
        count_5_2_B = 0;
        count_5_2_C = 0;
        count_5_2_D = 0;
        count_5_3_A = 0;
        count_5_3_B = 0;
        count_5_3_C = 0;
        count_5_3_D = 0;
        count_5_4_A = 0;
        count_5_4_B = 0;
        count_5_4_C = 0;
        count_5_4_D = 0;
        count_5_5_A = 0;
        count_5_5_B = 0;
        count_5_5_C = 0;
        count_5_5_D = 0;
        count_5_6_A = 0;
        count_5_6_B = 0;
        count_5_6_C = 0;
        count_5_6_D = 0;
        count_5_7_A = 0;
        count_5_7_B = 0;
        count_5_7_C = 0;
        count_5_7_D = 0;
        count_5_8_A = 0;
        count_5_8_B = 0;
        count_5_8_C = 0;
        count_5_8_D = 0;
        count_5_9_A = 0;
        count_5_9_B = 0;
        count_5_9_C = 0;
        count_5_9_D = 0;
        count_5_10_A = 0;
        count_5_10_B = 0;
        count_5_10_C = 0;
        count_5_10_D = 0;

        count_6_1_A = 0;
        count_6_1_B = 0;
        count_6_1_C = 0;
        count_6_1_D = 0;
        count_6_2_A = 0;
        count_6_2_B = 0;
        count_6_2_C = 0;
        count_6_2_D = 0;
        count_6_3_A = 0;
        count_6_3_B = 0;
        count_6_3_C = 0;
        count_6_3_D = 0;
        count_6_4_A = 0;
        count_6_4_B = 0;
        count_6_4_C = 0;
        count_6_4_D = 0;
        count_6_5_A = 0;
        count_6_5_B = 0;
        count_6_5_C = 0;
        count_6_5_D = 0;
        count_6_6_A = 0;
        count_6_6_B = 0;
        count_6_6_C = 0;
        count_6_6_D = 0;
        count_6_7_A = 0;
        count_6_7_B = 0;
        count_6_7_C = 0;
        count_6_7_D = 0;
        count_6_8_A = 0;
        count_6_8_B = 0;
        count_6_8_C = 0;
        count_6_8_D = 0;
        count_6_9_A = 0;
        count_6_9_B = 0;
        count_6_9_C = 0;
        count_6_9_D = 0;
        count_6_10_A = 0;
        count_6_10_B = 0;
        count_6_10_C = 0;
        count_6_10_D = 0;

        count_7_1_A = 0;
        count_7_1_B = 0;
        count_7_1_C = 0;
        count_7_1_D = 0;
        count_7_2_A = 0;
        count_7_2_B = 0;
        count_7_2_C = 0;
        count_7_2_D = 0;
        count_7_3_A = 0;
        count_7_3_B = 0;
        count_7_3_C = 0;
        count_7_3_D = 0;
        count_7_4_A = 0;
        count_7_4_B = 0;
        count_7_4_C = 0;
        count_7_4_D = 0;
        count_7_5_A = 0;
        count_7_5_B = 0;
        count_7_5_C = 0;
        count_7_5_D = 0;
        count_7_6_A = 0;
        count_7_6_B = 0;
        count_7_6_C = 0;
        count_7_6_D = 0;
        count_7_7_A = 0;
        count_7_7_B = 0;
        count_7_7_C = 0;
        count_7_7_D = 0;
        count_7_8_A = 0;
        count_7_8_B = 0;
        count_7_8_C = 0;
        count_7_8_D = 0;
        count_7_9_A = 0;
        count_7_9_B = 0;
        count_7_9_C = 0;
        count_7_9_D = 0;
        count_7_10_A = 0;
        count_7_10_B = 0;
        count_7_10_C = 0;
        count_7_10_D = 0;

        count_8_1_A = 0;
        count_8_1_B = 0;
        count_8_1_C = 0;
        count_8_1_D = 0;

        count_9_1_A = 0;
        count_9_1_B = 0;
        count_9_1_C = 0;
        count_9_1_D = 0;
        count_9_2_A = 0;
        count_9_2_B = 0;
        count_9_2_C = 0;
        count_9_2_D = 0;
        count_9_3_A = 0;
        count_9_3_B = 0;
        count_9_3_C = 0;
        count_9_3_D = 0;
        count_9_4_A = 0;
        count_9_4_B = 0;
        count_9_4_C = 0;
        count_9_4_D = 0;
        count_9_5_A = 0;
        count_9_5_B = 0;
        count_9_5_C = 0;
        count_9_5_D = 0;
        count_9_6_A = 0;
        count_9_6_B = 0;
        count_9_6_C = 0;
        count_9_6_D = 0;
        count_9_7_A = 0;
        count_9_7_B = 0;
        count_9_7_C = 0;
        count_9_7_D = 0;
        count_9_8_A = 0;
        count_9_8_B = 0;
        count_9_8_C = 0;
        count_9_8_D = 0;
        count_9_9_A = 0;
        count_9_9_B = 0;
        count_9_9_C = 0;
        count_9_9_D = 0;
        count_9_10_A = 0;
        count_9_10_B = 0;
        count_9_10_C = 0;
        count_9_10_D = 0;
        count_9_11_A = 0;
        count_9_11_B = 0;
        count_9_11_C = 0;
        count_9_11_D = 0;
        count_9_12_A = 0;
        count_9_12_B = 0;
        count_9_12_C = 0;
        count_9_12_D = 0;

        count_10_1_A = 0;
        count_10_1_B = 0;
        count_10_1_C = 0;
        count_10_1_D = 0;

        count_22_1_A = 0;
        count_22_1_B = 0;
        count_22_1_C = 0;
        count_22_1_D = 0;
        count_23_1_A = 0;
        count_23_1_B = 0;
        count_23_1_C = 0;
        count_23_1_D = 0;
        count_24_1_A = 0;
        count_24_1_B = 0;
        count_24_1_C = 0;
        count_24_1_D = 0;
        count_25_1_A = 0;
        count_25_1_B = 0;
        count_25_1_C = 0;
        count_25_1_D = 0;
        count_26_1_A = 0;
        count_26_1_B = 0;
        count_26_1_C = 0;
        count_26_1_D = 0;
        count_27_1_A = 0;
        count_27_1_B = 0;
        count_27_1_C = 0;
        count_27_1_D = 0;
        count_28_1_A = 0;
        count_28_1_B = 0;
        count_28_1_C = 0;
        count_28_1_D = 0;
        count_29_1_A = 0;
        count_29_1_B = 0;
        count_29_1_C = 0;
        count_29_1_D = 0;
        count_30_1_A = 0;
        count_30_1_B = 0;
        count_30_1_C = 0;
        count_30_1_D = 0;

        x_buffer = y_buffer = z_buffer = pre_acc_smooth_x = pre_acc_smooth_z = pre_var_chk_zy = ampbuffer = stay_time_zy = var_chk_zy = value_chk_z = value_chk_z2 = pre_value_chk_z = pre_value_chk_z2 = up_stay_time_z = up_stay_time_z2 = turn_chk_xz = pre_turn_chk_xz = pre_turn_time = turn_time = buffer_PVy = old_grade = grade = grade_chk = buffer_y = 0;
        count = var_chk_y = pre_var_chk_y = stay_time_y = var_chk_x = pre_var_chk_x = stay_time_x = stay_time_z = var_chk_z = pre_var_chk_z = idx_count = duplicate_idx_count = idx_count_dup = 0;
        t_count_percent = t_accuracy_percent = t_point = 0;
        t_comment = null;

        sumAccuracyD = 0;

        sumAccuracy = 0;
        avgAccuracy = 0;
        minAccuracy = 0;
        maxAccuracy = 0;
        maxHeartRate = 0;
        minHeartRate = 0;
        avgHeartRate = 0;
        cmpHeartRate = 0;
        sumHeartRate = 0;
        count_percent = 0;
        sumCountPerecnt = 0;
        size_hr_queue = 0;

        preCalorie = 0;

        sumCalorie = 0;
        save_start_time = 0;
    }

    /**
     * 재생 모드 설정.
     *
     * @param mode
     *            resume : 변수 보존, new : 변수 초기화.
     */
    public void setPlayMode(int mode) {
        if (mode == MODE_NEW_START) {
            init();
        }
    }

    /**
     * 재생을 담당하며 자동으로 Raw 데이터를 기록. 재생 시, 설정된 파일명으로 Raw 데이터를 기록한다. Kist 엔진과의 연동
     * 시작을 담당.
     *
     * @return true:성공, false:실패(이미 재생중. end function을 실행 시켜주세요.)
     */
    public boolean play() {
        if (isPlaying)
            return false;
        else {
            isPlaying = true;
            realFileName = getFileNameDateFormat(toString(videoID) + hyphen + recordName);

            log(tag, "play()");
			/*
			 * if(!setDebugPlay) setMode(); else NOW_REFERENCE =
			 * PreferencesManager.MID_REFERENCE;
			 */

            if (setDual)
                setMac();

            BluetoothManager.registDataCallback(new INordicFormat() {
                /**
                 * float[] sensor로 raw 데이터가 들어옴.
                 */
                @Override
                public void onSensor(float[] sensor) {
                    /**** float[] sensor 내용 ****/
                    /** 배열의 0,1,2 = 가속도 x,y,z **/
                    /** 배열의 3,4,5 = 자이로 x,y,z **/
                    /** 배열의 6 = 기압 **/
                    /** 배열의 7 = 심박 **/

                    if (!setPlay)
                        return;

                    if (videoID == 0)
                        return;

                    /**
                     * KIST 엔진 실행
                     */
                    for (int i = 0; i < sensor.length; i++) {
                        if (i < 3)
                            toDouble[i] = sensor[i] * 2;
                        else
                            toDouble[i] = sensor[i];
                    }
                    //Log.d("HR","hr:"+toDouble[7]);
                    mOut = m_KIST.fn_AART_Cal_parameter(toDouble);
                    //Log.d(tag,"setSave:"+setSave);
                    /** save data **/
                    if (setSave) {
                        //Log.i(tag,"mSdPath"+mSdPath+"realFileName"+realFileName);
                        saveDb(mSdPath, toDouble, realFileName);
                    }
                    /** Raw 데이터 기록 **/

                    /** 심박수 **/
                    int hearteRate = (int) sensor[7];
                    final int videoN = switchVideo(videoID, currentPosition);
                    switchUI(videoID, currentPosition);

                    //Log.d(tag, "videoN:" + videoN + " videoID:" + videoID);
                    if (hearteRate != 0)
                        HRQueue.insert(hearteRate);
                    else
                        log(tag, "HR is zero vid:" + videoN);

                    /**
                     * 최대 심박수 대비 현재 심박수 계산(%)
                     */
                    int avgHR = avgHeartRate(hearteRate);
                    if (!set_HRLock) {
                        onHeartRateCompared(avgHR);
                        /**
                         * 심박수 감시.
                         */
                        onHeartRateWarnning(avgHR);
                        set_HRLock = true;
                        mHandler_avgHR.postDelayed(mRunnable_avgHR, 5 * 1000);
                    }

                    /**
                     * KIST 엔진에서 어느정도 간격으로 데이터를 갱신하려는지 모름. KIST 엔진의 갱신에 맞추어서
                     * 심박수를 계산해야 함. 현재는 임시로 5초 간격으로 구성해놓음. sumInterval = 5;
                     */
                    if (!setFormula) {
                        setFormula = true;
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int sumHR = 0;
                                int sumAccuracy = 0;
                                int HR = 0;
                                int acc = 0;

                                int size_hr_queue = HRQueue.size();
                                VideoManager.this.size_hr_queue += size_hr_queue;
                                // int size_accuracy_queue =
                                // accuracyQueue.size();
                                int minus = 0;
                                if (size_hr_queue > 0) {
                                    for (int i = 0; i < size_hr_queue; i++) {
                                        try {
                                            HR = HRQueue.remove();
                                        } catch (ArrayIndexOutOfBoundsException e) {
                                            if (VideoManager.this.size_hr_queue > 0)
                                                VideoManager.this.size_hr_queue--;
                                            minus++;
                                            continue;
                                        }
                                        sumHR += HR;
                                        if (maxHeartRate < HR)
                                            maxHeartRate = HR;
                                        if (minHeartRate > HR)
                                            minHeartRate = HR;

                                        if (minHeartRate == 0)
                                            minHeartRate = HR;
                                    }
                                }
                                size_hr_queue -= minus;

                                /** 5초 딜레이를 주면서, 수집한 데이터를 계산함 **/
                                float retCal = 0;
                                if (size_hr_queue > 0)
                                    retCal = formulaHeartRate(sumHR / size_hr_queue);
                                if (retCal > 0)
                                    sumCalorie += retCal;

                                sumHeartRate += sumHR;
                                if (VideoManager.this.size_hr_queue != 0)
                                    avgHeartRate = sumHeartRate / VideoManager.this.size_hr_queue;

                                // speed
                                if (mIView != null) {
                                    mIView.onKISTOutput_Calorie(NumberFormatUtil.convertPoint(sumCalorie), videoN);
                                }

                                setFormula = false;
                            }
                        }, sumInterval * 1000);
                    }

                    onActivityName(videoID, currentPosition);
                    onTotalScore(videoID, currentPosition);

                    if (is_R_Video) {
                        if (!is_Save) {
                            if (is_R_vid != 0 && (videoN != VideoParameter.Group3.Index.V_3_0_R && videoN != VideoParameter.Group3.Index.V_3_0)) {
                                is_Save = true;
                                cmpHeartRate = getHeartRateCompared(avgHeartRate);
                                int exer_idx = convertExerIdx(is_R_vid);
                                int point = getPoint(count_percent, avgAccuracy);
                                mConfig.addUserExerciseData(videoID, toFullCount(videoID), exer_idx, toCount(videoN), save_start_time, System.currentTimeMillis(), Math.round(sumCalorie - preCalorie),
                                        out[0], count_percent,
                                        0/* perfect count */, minAccuracy, maxAccuracy, avgAccuracy, minHeartRate, maxHeartRate, avgHeartRate, cmpHeartRate, point, 0/* r1 */, 0/* r2 */);

                                setTotalScore(point, count_percent, avgAccuracy, getCommentSection(count_percent, avgAccuracy));

                                /** 전역 변수 운동 종료마다 init 출산부.. **/
                                x_buffer = y_buffer = z_buffer = pre_acc_smooth_x = pre_acc_smooth_z = pre_var_chk_zy = ampbuffer = stay_time_zy = var_chk_zy = value_chk_z = value_chk_z2 = pre_value_chk_z = pre_value_chk_z2 = up_stay_time_z = up_stay_time_z2 = turn_chk_xz = pre_turn_chk_xz = pre_turn_time = turn_time = buffer_PVy = old_grade = grade = grade_chk = buffer_y = 0;
                                count = var_chk_y = pre_var_chk_y = stay_time_y = var_chk_x = pre_var_chk_x = stay_time_x = stay_time_z = var_chk_z = pre_var_chk_z = idx_count = duplicate_idx_count = idx_count_dup = 0;

                                Log.i(tag, "add-> videoID:" + videoID + " video_full_count:" + toFullCount(videoID) + " exer_idx:" + exer_idx + " exer_count:" + toCount(videoN) + " start_time:"
                                        + save_start_time + " end_time:" + System.currentTimeMillis() + " count:" + out[0] + " count_percent:" + count_percent + " perfect count:" + 0 + " minAccuracy:" + minAccuracy + " maxAccuracy:" + maxAccuracy + " avgAccuracy:" + avgAccuracy
                                        + " point:" + point);

                                //Log.i(tag, "add-> videoID:" + videoID + " video_full_count:" + toFullCount(videoID) + " exer_idx:" + exer_idx + " exer_count:" + toCount(videoN) + " start_time:"
                                        //+ save_start_time + " end_time:" + System.currentTimeMillis() + " consume_calorie:" + Math.round(sumCalorie - preCalorie) + " count:" + out[0]
                                        //+ " count_percent:" + count_percent + " perfect count:" + 0 + " minAccuracy:" + minAccuracy + " maxAccuracy:" + maxAccuracy + " avgAccuracy:" + avgAccuracy
                                        //+ " minHeartRate:" + minHeartRate + " maxHeartRate:" + maxHeartRate + " avgHeartRate:" + avgHeartRate + " cmpHeartRate:" + cmpHeartRate + " point:" + point);

                                // Log.d(tag,"preCalorie:"+preCalorie+"
                                // sumCalorie:"+sumCalorie);
                                preCalorie = sumCalorie;

                                arrayStartTime.add(save_start_time); // User가
                                // 동영상을
                                // 종료하면
                                // 저장된
                                // DB를
                                // 삭제하기
                                // 위해...
                                // is R vid 를 보고 종료 String Tag.
								/*
								 * if(videoN !=
								 * VideoParameter.Group3.Index.V_3_0_R || videoN
								 * != VideoParameter.Group3.Index.V_3_11_R ||
								 * videoN != VideoParameter.Group3.Index.V_3_0
								 * || videoN !=
								 * VideoParameter.Group3.Index.V_3_11)
								 */
                                if (setSave)
                                    saveTag(mSdPath, setEndTag(is_R_vid), realFileName);
                                // Laptime flag reset
                                setLaptime = false;
                            }
                        }
                        // 휴식기간인 경우, 앞선 동작의 칼로리를 계산한다.
                        // reset_HRQueue.insert(hearteRate);
                        // Log.d(tag, "videoN is ZERO!!!");
                        for (int i = 0; i < out.length; i++) {
                            out[i] = 0;
                        }

                        Count_V_2_1 = 0;
                        Count_V_2_4 = 0;
                        Count_V_2_6 = 0;
                        Count_V_3_8 = 0;
                        SumAccuracy_V_2_1 = 0;
                        SumAccuracy_V_2_4 = 0;
                        SumAccuracy_V_2_6 = 0;
                        SumAccuracy_V_3_8 = 0;

                        maxHeartRate = 0;
                        minHeartRate = 0;
                        avgHeartRate = 0;
                        sumHeartRate = 0;
                        size_hr_queue = 0;
                        maxAccuracy = 0;
                        minAccuracy = 0;
                        avgAccuracy = 0;
                        cmpHeartRate = 0;
                        sumAccuracy = 0;
                        count_percent = 0;

                        if (mIView != null) {
                            mIView.onKISTOutput_Calorie(NumberFormatUtil.convertPoint(sumCalorie), videoN);
                            mIView.onKISTOutput_Accuracy(0, videoN);
                            mIView.onKISTOutput_Count(0, videoN);
                            mIView.onKISTOutput_Point(0, videoN);
                            log(tag, "reset Graph data vid:" + is_R_vid);
                        }
                        return;
                    } else {
                        is_Save = false;
                    }

                    /**
                     * Laptime comment : Laptime comment는 우선순위가 가장 높은 comment.
                     */
                    // onLaptime(videoN, currentPosition);

                    /**
                     * KIST 엔진에서 나온 결과물의 callback. VideoPlayActivity에서 받아서 처리하기
                     * 위한 interface 영상에 바로 표시되는 횟수와 강도를 전달해야한다. 때문에, 횟수와 강도를
                     * 전달하기 전에 무엇인가 처리가 이루어져야 함. 데이터를 받자마자 바로 전달하기 때문에, 실시간으로
                     * 데이터가 전달된다. 따로 버퍼링이 필요하다면 처리를 넣어주어야 함.
                     */
                    if (mIView != null) {

                        // mOut = m_KIST.fn_AART_Cal_parameter(toDouble);

                        /** coach 도중의 불필요한 동작의 count를 방지하기 위한 코드 **/
                        int nV_Index = onSkip(videoID, videoN, currentPosition);

                        actionOuput(nV_Index, mOut, avgHR);
                        mIView.onKISTOutput_Count(out[0], videoN);

                        int acc = 0;
                        int size_accuracy_queue = accuracyQueue.size();
                        if (size_accuracy_queue > 0)
                            for (int i = 0; i < size_accuracy_queue; i++) {
                                acc = accuracyQueue.remove();
                                acc = acc > 100 ? 100 : acc;

                                sumAccuracy += acc;
                                if (maxAccuracy < acc)
                                    maxAccuracy = acc;
                                if (minAccuracy > acc)
                                    minAccuracy = acc;

                                if (minAccuracy == 0)
                                    minAccuracy = acc;
                            }

                        if (out[0] != 0)
                            avgAccuracy = VideoManager.this.sumAccuracy / out[0] > 100 ? 100 : VideoManager.this.sumAccuracy / out[0];
                        else
                            avgAccuracy = 0;
                        count_percent = (int) (100f * out[0] / toCount(videoN) > 100 ? 100 : 100f * out[0] / toCount(videoN));
                        // if(out[1] != PHASE_0) {// 0으로 가지 않아서 그래프가 사라지지 않음. 다른
                        // 처리 필요.
                        if (out[0] != preCount) {// 0으로 가지 않아서 그래프가 사라지지 않음. 다른
                            // 처리 필요.
                            releaseAccuracyLock();
                            mIView.onKISTOutput_Accuracy(out[1], videoN);
                            mIView.onKISTOutput_Point(getPoint(count_percent, avgAccuracy), videoN);
                            // Log.d(tag,"out[0] != preCount");

                            setAccuracyLock();
                        } else if (!getAccuracyLock()) {
                            // Log.d(tag,"!getAccuracyLock()");
                            out[1] = 0;
                            mIView.onKISTOutput_Accuracy(out[1], videoN);
                            mIView.onKISTOutput_Point(getPoint(count_percent, avgAccuracy), videoN);
                        }
                        // Log.d(tag,"out[0]:"+out[0] +" out[1]:"+out[1]);
                        preCount = out[0];
                    }
                    /** KIST end **/
                    /**************/

                    /** save data **/

					 //if (setSave)
                     //    saveDb(mSdPath, toDouble, realFileName);

                    /** Raw 데이터 기록 **/
                }

                // dual mode
                @Override
                public void onSensorD(float[] sensor) {
                    {

                    }
                }
            });

            return true;
        }
    }

    public boolean play2() {
        if (isPlaying)
            return false;
        else {
            isPlaying = true;
            //realFileName = getFileNameDateFormat(toString(videoID) + hyphen + recordName);

            log(tag, "play2()");

			/*if(!setDebugPlay)
				setMode();
			else
				NOW_REFERENCE = PreferencesManager.MID_REFERENCE;*/

            //if(setDual)
            //	setMac();
            //runOnUiThread(new Runnable()

            //---FILE IO로 인해 콜백 인터페이스에서 쓰레드로 변경
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub

                    while (!parseACC_X.isEmpty() || !parseACC_Y.isEmpty() || !parseACC_Z.isEmpty() || !parseHR.isEmpty()) {
                        //if(parseACC_X.isEmpty() == true|| parseACC_Y.isEmpty()== true || parseACC_Z.isEmpty()== true){
                        //    return;
                        //}
                        mHandlerD.post(new Runnable() {
                            @Override
                            public void run() {
                                /**
                                 * parseACC_X,Y,Z ParseHR sensor로 raw 데이터가 들어옴.
                                 */
                                if (parseACC_X.isEmpty() || parseACC_Y.isEmpty() || parseACC_Z.isEmpty() || parseHR.isEmpty()) {
                                    Log.i(tag,"RAW데이터 종료로 인한 MW진입불가");
                                    return;
                                }
                                if (!setPlay)
                                    return;

                                if (videoID == 0)
                                    return;
                                //Log.d("불러온 횟수", String.format("onSensorCB 횟수:%d", count++));
                                /**
                                 * KIST 엔진 실행
                                 */
                                // 검색이 끝나면 다시 버튼 활성화
                                //if (parseACC_X.isEmpty()== true && parseACC_Y.isEmpty()== true && parseACC_Z.isEmpty()== true) {
                                //	Log.d("ACC Raw", "Raw ACC 데이터 끝");
                                //}
                                //else {
                                //Log.i(tag, "parseACC_X"+parseACC_X.size() + "parseACC_X"+parseACC_X.indexOf(0));
                                //Log.i(tag, "parseACC_y"+parseACC_Y);
                                //Log.i(tag, "parseACC_z"+parseACC_Z);
                                //Log.i(tag, "parseHR"+parseHR + " "+ parseHR.size());

                                toDouble[0] = parseACC_X.remove(0);
                                toDouble[1] = parseACC_Y.remove(0);
                                toDouble[2] = parseACC_Z.remove(0);

                                //}

                                //Log.d("X축 센서", String.format("X축%f", toDouble[0]));
                                //Log.d("Y축 센서", String.format("Y축%f", toDouble[1]));
                                //Log.d("Z축 센서", String.format("Z축%f", toDouble[2]));

                                mOut = m_KIST.fn_AART_Cal_parameter(toDouble);

                                /** save data **/
                                // if (setSave)
                                // saveDb(mSdPath, toDouble, realFileName);
                                /** Raw 데이터 기록 **/

                                /** 심박수 **/

                                float fhearteRate = parseHR.remove(0);

                                int hearteRate = (int) fhearteRate;

                                //Log.d("심박 센서", String.format("심박값%d", hearteRate));

                                final int videoN = switchVideo(videoID, currentPosition);
                                switchUI(videoID, currentPosition);

                                if (hearteRate != 0)
                                    HRQueue.insert(hearteRate);
                                else
                                    log(tag, "HR is zero vid:" + videoN);

                                /**
                                 * 최대 심박수 대비 현재 심박수 계산(%)
                                 */
                                int avgHR = avgHeartRate(hearteRate);
                                if (!set_HRLock) {
                                    onHeartRateCompared(avgHR);
                                    /**
                                     * 심박수 감시.
                                     */
                                    onHeartRateWarnning(avgHR);
                                    set_HRLock = true;
                                    mHandler_avgHR.postDelayed(mRunnable_avgHR, 5 * 1000);
                                }

                                /**
                                 * KIST 엔진에서 어느정도 간격으로 데이터를 갱신하려는지 모름. KIST 엔진의
                                 * 갱신에 맞추어서 심박수를 계산해야 함. 현재는 임시로 5초 간격으로 구성해놓음.
                                 * sumInterval = 5;
                                 */
                                if (!setFormula) {
                                    setFormula = true;
                                    mHandler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            int sumHR = 0;
                                            int sumAccuracy = 0;
                                            int HR = 0;
                                            int acc = 0;

                                            int size_hr_queue = HRQueue.size();
                                            VideoManager.this.size_hr_queue += size_hr_queue;
                                            // int size_accuracy_queue =
                                            // accuracyQueue.size();
                                            int minus = 0;
                                            if (size_hr_queue > 0) {
                                                for (int i = 0; i < size_hr_queue; i++) {
                                                    try {
                                                        HR = HRQueue.remove();
                                                    } catch (ArrayIndexOutOfBoundsException e) {
                                                        if (VideoManager.this.size_hr_queue > 0)
                                                            VideoManager.this.size_hr_queue--;
                                                        minus++;
                                                        continue;
                                                    }
                                                    sumHR += HR;
                                                    if (maxHeartRate < HR)
                                                        maxHeartRate = HR;
                                                    if (minHeartRate > HR)
                                                        minHeartRate = HR;

                                                    if (minHeartRate == 0)
                                                        minHeartRate = HR;
                                                }
                                            }
                                            size_hr_queue -= minus;

                                            /** 5초 딜레이를 주면서, 수집한 데이터를 계산함 **/
                                            float retCal = 0;
                                            if (size_hr_queue > 0)
                                                retCal = formulaHeartRate(sumHR / size_hr_queue);
                                            if (retCal > 0)
                                                sumCalorie += retCal;

                                            sumHeartRate += sumHR;
                                            if (VideoManager.this.size_hr_queue != 0)
                                                avgHeartRate = sumHeartRate / VideoManager.this.size_hr_queue;

                                            // speed
                                            if (mIView != null) {
                                                mIView.onKISTOutput_Calorie(NumberFormatUtil.convertPoint(sumCalorie),
                                                        videoN);
                                            }

                                            setFormula = false;
                                        }
                                    }, sumInterval * 1000);
                                }

                                onActivityName(videoID, currentPosition);
                                // 15분 순환 운동만 예외처리.
								/*
								 if (currentPlayVideoIndex != videoN ) { if
								  (mIView != null) {
								  mIView.onDescription(videoN,setDescription(videoN));
								  mIView.onBriefDescription(videoN,setDescription(videoN)); } }
								*/

                                // currentPlayVideoIndex = videoN;

                                if (is_R_Video) {
                                    if (!is_Save) {
                                        if (is_R_vid != 0 && (videoN != VideoParameter.Group3.Index.V_3_0_R && videoN != VideoParameter.Group3.Index.V_3_0)) {
                                            is_Save = true;
                                            cmpHeartRate = getHeartRateCompared(avgHeartRate);
                                            int exer_idx = convertExerIdx(is_R_vid);
                                            int point = getPoint(count_percent, avgAccuracy);
                                            mConfig.addUserExerciseData(videoID, toFullCount(videoID), exer_idx, toCount(videoN), save_start_time, System.currentTimeMillis(), Math.round(sumCalorie - preCalorie),
                                                    out[0], count_percent,
                                                    0/* perfect count */, minAccuracy, maxAccuracy, avgAccuracy, minHeartRate, maxHeartRate, avgHeartRate, cmpHeartRate, point, 0/* r1 */, 0/* r2 */);

                                            setTotalScore(point, count_percent, avgAccuracy, getCommentSection(count_percent, avgAccuracy));

                                            /** 전역 변수 운동 종료마다 init 출산부.. **/
                                            x_buffer = y_buffer = z_buffer = pre_acc_smooth_x = pre_acc_smooth_z = pre_var_chk_zy = ampbuffer = stay_time_zy = var_chk_zy = value_chk_z = value_chk_z2 = pre_value_chk_z = pre_value_chk_z2 = up_stay_time_z = up_stay_time_z2 = turn_chk_xz = pre_turn_chk_xz = pre_turn_time = turn_time = buffer_PVy = old_grade = grade = grade_chk = buffer_y = 0;
                                            count = var_chk_y = pre_var_chk_y = stay_time_y = var_chk_x = pre_var_chk_x = stay_time_x = stay_time_z = var_chk_z = pre_var_chk_z = idx_count = duplicate_idx_count = idx_count_dup = 0;

                                            Log.i(tag, "add-> videoID:" + videoID + " video_full_count:" + toFullCount(videoID) + " exer_idx:" + exer_idx + " exer_count:" + toCount(videoN) + " start_time:"
                                                    + save_start_time + " end_time:" + System.currentTimeMillis() + " count:" + out[0] + " count_percent:" + count_percent + " perfect count:" + 0 + " minAccuracy:" + minAccuracy + " maxAccuracy:" + maxAccuracy + " avgAccuracy:" + avgAccuracy
                                                    + " point:" + point);
                                            /*Log.i(tag, "add-> videoID:" + videoID + " video_full_count:"
                                                    + toFullCount(videoID) + " exer_idx:" + exer_idx + " exer_count:"
                                                    + toCount(videoN) + " start_time:" + save_start_time + " end_time:"
                                                    + System.currentTimeMillis() + " consume_calorie:"
                                                    + Math.round(sumCalorie - preCalorie) + " count:" + out[0]
                                                    + " count_percent:" + count_percent + " perfect count:" + 0
                                                    + " minAccuracy:" + minAccuracy + " maxAccuracy:" + maxAccuracy
                                                    + " avgAccuracy:" + avgAccuracy + " minHeartRate:" + minHeartRate
                                                    + " maxHeartRate:" + maxHeartRate + " avgHeartRate:" + avgHeartRate
                                                    + " cmpHeartRate:" + cmpHeartRate + " point:"
                                                    + getPoint(count_percent, avgAccuracy));*/

                                            // Log.d(tag,"preCalorie:"+preCalorie+"sumCalorie:"+sumCalorie);
                                            preCalorie = sumCalorie;

                                            arrayStartTime.add(save_start_time);
                                            // User가 동영상을 종료하면저장된DB를삭제하기위해...
                                            // is R vid 를 보고 종료 String Tag.
											/*
											  if(videoN != VideoParameter.Group3.Index.V_3_0_R || videoN != VideoParameter.Group3.Index.V_3_11_R ||
											  videoN != VideoParameter.Group3.Index.V_3_0  || videoN != VideoParameter.Group3.Index.V_3_11)
											 */
                                            if (setSave)
                                                saveTag(mSdPath, setEndTag(is_R_vid), realFileName);
                                            // Laptime flag reset
                                            setLaptime = false;
											/*
											 UserExerciseData[] test = mConfig.getUserExerciseData();
											 if(test != null) {
												Log.d(tag,"get User exer DATA");
												for(UserExerciseData d : test) {
													Log.i(tag, "get-> videoID:"+d.getVideo_idx()+" video_full_count:"+d.getVideo_full_count() +" exer_idx:"+d.getExer_idx()+" exer_count:"+ d.getExer_count()
											 		+" start_time:"+d.getStart_time()+ " end_time:"+d.getEnd_time()+" consume_calorie:"+d.getConsume_calorie() +" count:"+d.getCount() +" count_percent:"+
											 		d.getCount_percent()+" perfect count:"+d.getPerfect_count()+" minAccuracy:"+d.getMin_accuracy() +" maxAccuracy:"+d.getMax_accuracy()+" avgAccuracy:"
													+d.getAvg_accuracy()+" minHeartRate:"+d.getMin_heartrate()+" maxHeartRate:"+d.getMax_heartrate() +" avgHeartRate:"+d.getAvg_heartrate()+" cmpHeartRate:"
													+d.getCmp_accuracy()+" point:"+d.getPoint());
													}
											 }
											 */
                                        }
                                    }
                                    // 휴식기간인 경우, 앞선 동작의 칼로리를 계산한다.
                                    // reset_HRQueue.insert(hearteRate);
                                    // Log.d(tag, "videoN is ZERO!!!");
                                    for (int i = 0; i < out.length; i++) {
                                        out[i] = 0;
                                    }

                                    Count_V_2_1 = 0;
                                    Count_V_2_4 = 0;
                                    Count_V_2_6 = 0;
                                    Count_V_3_8 = 0;
                                    SumAccuracy_V_2_1 = 0;
                                    SumAccuracy_V_2_4 = 0;
                                    SumAccuracy_V_2_6 = 0;
                                    SumAccuracy_V_3_8 = 0;

                                    maxHeartRate = 0;
                                    minHeartRate = 0;
                                    avgHeartRate = 0;
                                    sumHeartRate = 0;
                                    size_hr_queue = 0;
                                    maxAccuracy = 0;
                                    minAccuracy = 0;
                                    avgAccuracy = 0;
                                    cmpHeartRate = 0;
                                    sumAccuracy = 0;
                                    count_percent = 0;

                                    if (mIView != null) {
                                        mIView.onKISTOutput_Calorie(NumberFormatUtil.convertPoint(sumCalorie), videoN);
                                        mIView.onKISTOutput_Accuracy(0, videoN);
                                        mIView.onKISTOutput_Count(0, videoN);
                                        mIView.onKISTOutput_Point(0, videoN);
                                        log(tag, "reset Graph data vid:" + is_R_vid);
                                    }
                                    // break;
                                } else {
                                    is_Save = false;
                                }

                                /**
                                 * Laptime comment : Laptime comment는 우선순위가 가장
                                 * 높은 comment.
                                 */
                                // onLaptime(videoN, currentPosition);

                                /**
                                 * KIST 엔진에서 나온 결과물의 callback.
                                 * VideoPlayActivity에서 받아서 처리하기 위한 interface 영상에
                                 * 바로 표시되는 횟수와 강도를 전달해야한다. 때문에, 횟수와 강도를 전달하기 전에
                                 * 무엇인가 처리가 이루어져야 함. 데이터를 받자마자 바로 전달하기 때문에,
                                 * 실시간으로 데이터가 전달된다. 따로 버퍼링이 필요하다면 처리를 넣어주어야 함.
                                 */
                                if (mIView != null) {

                                    // mOut = m_KIST.fn_AART_Cal_parameter(toDouble);

                                    /** coach 도중의 불필요한 동작의 count를 방지하기 위한 코드 **/
                                    int nV_Index = onSkip(videoID, videoN, currentPosition);

                                    actionOuput(nV_Index, mOut, avgHR);
                                    mIView.onKISTOutput_Count(out[0], videoN);

                                    int acc = 0;
                                    int size_accuracy_queue = accuracyQueue.size();
                                    if (size_accuracy_queue > 0)
                                        for (int i = 0; i < size_accuracy_queue; i++) {
                                            acc = accuracyQueue.remove();
                                            sumAccuracy += acc;
                                            if (maxAccuracy < acc)
                                                maxAccuracy = acc;
                                            if (minAccuracy > acc)
                                                minAccuracy = acc;

                                            if (minAccuracy == 0)
                                                minAccuracy = acc;
                                        }

                                    if (out[0] != 0)
                                        avgAccuracy = VideoManager.this.sumAccuracy / out[0] > 100 ? 100
                                                : VideoManager.this.sumAccuracy / out[0];
                                    else
                                        avgAccuracy = 0;
                                    count_percent = (int) (100f * out[0] / toCount(videoN) > 100 ? 100
                                            : 100f * out[0] / toCount(videoN));
                                    // if(out[1] != PHASE_0) {// 0으로 가지 않아서 그래프가 사라지지 않음. 다른 처리 필요.
                                    if (out[0] != preCount) {// 0으로 가지 않아서 그래프가 사라지지 않음. 다른 처리 필요.
                                        releaseAccuracyLock();
                                        mIView.onKISTOutput_Accuracy(out[1], videoN);
                                        mIView.onKISTOutput_Point(getPoint(count_percent, avgAccuracy), videoN);
                                        // Log.d(tag,"out[0] != preCount");

                                        setAccuracyLock();
                                    } else if (!getAccuracyLock()) {
                                        // Log.d(tag,"!getAccuracyLock()");
                                        out[1] = 0;
                                        mIView.onKISTOutput_Accuracy(out[1], videoN);
                                        mIView.onKISTOutput_Point(getPoint(count_percent, avgAccuracy), videoN);
                                    }
                                    // Log.d(tag,"out[0]:"+out[0] +"out[1]:"+out[1]);
                                    preCount = out[0];
                                }
                                /** KIST end **/
                                /**************/
                            }
                        });
                        //--- 밴드 전송주기인 50ms 간격설정
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                }
            })).start();

            return true;
        }

    }


    private String getCommentSection(int count_percent, int accuracy_percent) {
        String comment = null;
        short set = 0;

        if (count_percent == 100 && accuracy_percent == 100)
            if (LANGUAGE_SET == 1) {
                return CommentSection.Korea.ALL_100;
            } else if (LANGUAGE_SET == 2) {
                return CommentSection.Japan.ALL_100;
            } else if (LANGUAGE_SET == 3) {
                return CommentSection.English.ALL_100;
            }

        if (count_percent >= 90) { // 100~90
            set = 0x01;
        } else if (count_percent >= 60) { // 90~60
            set = 0x02;
        } else if (count_percent >= 30) { // 60~30
            set = 0x04;
        } else if (count_percent >= 20) { // 30~20
            set = 0x08;
        } else if (count_percent >= 10) { // 20~10
            set = 0x10;
        } else { // 10~
            set = 0x20;
        }

        if (accuracy_percent >= 90) { // 100~90
            set |= 0x40;
        } else if (accuracy_percent >= 60) { // 90~60
            set |= 0x80;
        } else if (accuracy_percent >= 30) { // 60~30
            set |= 0x100;
        } else if (accuracy_percent >= 20) { // 30~20
            set |= 0x200;
        } else if (accuracy_percent >= 10) { // 20~10
            set |= 0x400;
        } else { // 10~
            set |= 0x800;
        }
        set &= 0xffff;

        switch (set) {
            case (short) 0x41: // 양쪽다 90 이상.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ALL_90_OVER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ALL_90_OVER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ALL_90_OVER;
                }
                break;
            case (short) 0x82: // 양쪽다 90~60.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ALL_90_60_BETWEEN;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ALL_90_60_BETWEEN;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ALL_90_60_BETWEEN;
                }
                break;
            case (short) 0x104: // 양쪽다 60~30.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ALL_60_30_BETWEEN;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ALL_60_30_BETWEEN;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ALL_60_30_BETWEEN;
                }
                break;
            case (short) 0x208: // 양쪽다 30~20, 20~10
            case (short) 0x210: // 양쪽다 30~20, 20~10
            case (short) 0x408: // 양쪽다 30~20, 20~10
            case (short) 0x410: // 양쪽다 30~20, 20~10
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ALL_30_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ALL_30_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ALL_30_UNDER;
                }
                break;
            case (short) 0x820: // 양쪽다 10미만
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ALL_10_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ALL_10_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ALL_10_UNDER;
                }
                break;

            case (short) 0x101: // 횟수 90이상, 정확도 60미만.
            case (short) 0x201: // 횟수 90이상, 정확도 60미만.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.COUNT_90_OVER_ACC_60_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.COUNT_90_OVER_ACC_60_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.COUNT_90_OVER_ACC_60_UNDER;
                }
                break;
            case (short) 0x81: // 횟수 90이상, 정확도 90~60.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.COUNT_90_OVER_ACC_90_60_BETWEEN;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.COUNT_90_OVER_ACC_90_60_BETWEEN;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.COUNT_90_OVER_ACC_90_60_BETWEEN;
                }
                break;
            case (short) 0x401: // 횟수 90이상, 정확도 20미만.
            case (short) 0x801: // 횟수 90이상, 정확도 20미만.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.COUNT_90_OVER_ACC_20_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.COUNT_90_OVER_ACC_20_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.COUNT_90_OVER_ACC_20_UNDER;
                }
                break;

            case (short) 0x102: // 횟수 90~60, 정확도 60~30.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.COUNT_90_60_BETWEEN_ACC_60_30_BETWEEN;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.COUNT_90_60_BETWEEN_ACC_60_30_BETWEEN;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.COUNT_90_60_BETWEEN_ACC_60_30_BETWEEN;
                }
                break;

            case (short) 0x202: // 횟수 90~60, 정확도 30미만.
            case (short) 0x402:
            case (short) 0x802:
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.COUNT_90_60_BETWEEN_ACC_30_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.COUNT_90_60_BETWEEN_ACC_30_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.COUNT_90_60_BETWEEN_ACC_30_UNDER;
                }
                break;

            case (short) 0x204: // 횟수 60~30, 정확도 30미만.
            case (short) 0x404:
            case (short) 0x804:
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.COUNT_60_UNDER_ACC_30_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.COUNT_60_UNDER_ACC_30_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.COUNT_60_UNDER_ACC_30_UNDER;
                }
                break;

            case (short) 0x808: // 횟수 30~10, 정확도 10미만.
            case (short) 0x810:
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.COUNT_30_10_BETWEEN_ACC_10_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.COUNT_30_10_BETWEEN_ACC_10_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.COUNT_30_10_BETWEEN_ACC_10_UNDER;
                }
                break;

            case (short) 0x44: // 정확도 90이상, 횟수 60미만.
            case (short) 0x48: // 정확도 90이상, 횟수 60미만.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ACC_90_OVER_COUNT_60_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ACC_90_OVER_COUNT_60_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ACC_90_OVER_COUNT_60_UNDER;
                }
                break;
            case (short) 0x42: // 정확도 90이상, 횟수 90~60.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ACC_90_OVER_COUNT_90_60_BETWEEN;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ACC_90_OVER_COUNT_90_60_BETWEEN;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ACC_90_OVER_COUNT_90_60_BETWEEN;
                }
                break;
            case (short) 0x50: // 정확도 90이상, 횟수 20미만.
            case (short) 0x60: // 정확도 90이상, 횟수 20미만.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ACC_90_OVER_COUNT_20_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ACC_90_OVER_COUNT_20_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ACC_90_OVER_COUNT_20_UNDER;
                }
                break;

            case (short) 0x84: // 정확도 90~60, 횟수 60~30.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ACC_90_60_BETWEEN_COUNT_60_30_BETWEEN;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ACC_90_60_BETWEEN_COUNT_60_30_BETWEEN;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ACC_90_60_BETWEEN_COUNT_60_30_BETWEEN;
                }
                break;

            case (short) 0x88: // 정확도 90~60, 횟수 30미만.
            case (short) 0x90: // 정확도 90~60, 횟수 30미만.
            case (short) 0xa0: // 정확도 90~60, 횟수 30미만.
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ACC_90_60_BETWEEN_COUNT_30_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ACC_90_60_BETWEEN_COUNT_30_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ACC_90_60_BETWEEN_COUNT_30_UNDER;
                }
                break;

            case (short) 0x108: // 정확도 60~30, 횟수 30미만.
            case (short) 0x110:
            case (short) 0x120:
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ACC_60_UNDER_COUNT_30_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ACC_60_UNDER_COUNT_30_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ACC_60_UNDER_COUNT_30_UNDER;
                }
                break;

            case (short) 0x220: // 정확도 30~10, 횟수 10미만.
            case (short) 0x420:
                if (LANGUAGE_SET == 1) {
                    return CommentSection.Korea.ACC_30_10_BETWEEN_COUNT_10_UNDER;
                } else if (LANGUAGE_SET == 2) {
                    return CommentSection.Japan.ACC_30_10_BETWEEN_COUNT_10_UNDER;
                } else if (LANGUAGE_SET == 3) {
                    return CommentSection.English.ACC_30_10_BETWEEN_COUNT_10_UNDER;
                }
                break;
        }

        return comment;
    }

    private String toCommentSection(int point, int videoID) {
        String comment = null;
        switch (videoID) {
            case VideoManager.VideoParameter.Group8.Index.V_8_1:
            case VideoManager.VideoParameter.Group8.Index.V_8_1_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_1_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_1;
                else if (VideoManager.VideoParameter.Group8.Comment.Section4.V_8_1_REF <= point && point < VideoManager.VideoParameter.Group8.Comment.Section5.V_8_1_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_1;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section3.V_8_1;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_2:
            case VideoManager.VideoParameter.Group8.Index.V_8_2_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_2_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_2;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_2;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_3:
            case VideoManager.VideoParameter.Group8.Index.V_8_3_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_3_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_3;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_3;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_4:
            case VideoManager.VideoParameter.Group8.Index.V_8_4_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_4_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_4;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_4;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_5:
            case VideoManager.VideoParameter.Group8.Index.V_8_5_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_5_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_5;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_5;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_6:
            case VideoManager.VideoParameter.Group8.Index.V_8_6_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_6_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_6;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_6;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_7:
            case VideoManager.VideoParameter.Group8.Index.V_8_7_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_7_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_7;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_7;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_8:
            case VideoManager.VideoParameter.Group8.Index.V_8_8_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_8_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_8;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_8;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_9:
            case VideoManager.VideoParameter.Group8.Index.V_8_9_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_9_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_9;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_9;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_10:
            case VideoManager.VideoParameter.Group8.Index.V_8_10_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_10_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_10;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_10;
                break;
            case VideoManager.VideoParameter.Group8.Index.V_8_11:
            case VideoManager.VideoParameter.Group8.Index.V_8_11_R:
                if (point >= VideoManager.VideoParameter.Group8.Comment.Section5.V_8_11_REF)
                    comment = VideoManager.VideoParameter.Group8.Comment.Section5.V_8_11;
                else
                    comment = VideoManager.VideoParameter.Group8.Comment.Section4.V_8_11;
                break;

            case VideoManager.VideoParameter.Group9.Index.V_9_1:
            case VideoManager.VideoParameter.Group9.Index.V_9_1_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_1_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_1;
                else if (VideoManager.VideoParameter.Group9.Comment.Section4.V_9_1_REF <= point && point < VideoManager.VideoParameter.Group9.Comment.Section5.V_9_1_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_1;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section3.V_9_1;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_2:
            case VideoManager.VideoParameter.Group9.Index.V_9_2_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_2_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_2;
                else if (VideoManager.VideoParameter.Group9.Comment.Section4.V_9_2_REF <= point && point < VideoManager.VideoParameter.Group9.Comment.Section5.V_9_2_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_2;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section3.V_9_2;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_3:
            case VideoManager.VideoParameter.Group9.Index.V_9_3_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_3_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_3;
                else if (VideoManager.VideoParameter.Group9.Comment.Section4.V_9_3_REF <= point && point < VideoManager.VideoParameter.Group9.Comment.Section5.V_9_3_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_3;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section3.V_9_3;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_4:
            case VideoManager.VideoParameter.Group9.Index.V_9_4_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_4_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_4;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_4;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_5:
            case VideoManager.VideoParameter.Group9.Index.V_9_5_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_5_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_5;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_5;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_6:
            case VideoManager.VideoParameter.Group9.Index.V_9_6_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_6_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_6;
                else if (VideoManager.VideoParameter.Group9.Comment.Section4.V_9_6_REF <= point && point < VideoManager.VideoParameter.Group9.Comment.Section5.V_9_6_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_6;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section3.V_9_6;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_7:
            case VideoManager.VideoParameter.Group9.Index.V_9_7_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_7_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_7;
                else if (VideoManager.VideoParameter.Group9.Comment.Section4.V_9_7_REF <= point && point < VideoManager.VideoParameter.Group9.Comment.Section5.V_9_7_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_7;
                else if (VideoManager.VideoParameter.Group9.Comment.Section3.V_9_7_REF <= point && point < VideoManager.VideoParameter.Group9.Comment.Section4.V_9_7_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section3.V_9_7;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section2.V_9_7;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_8:
            case VideoManager.VideoParameter.Group9.Index.V_9_8_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_8_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_8;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_8;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_9:
            case VideoManager.VideoParameter.Group9.Index.V_9_9_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_9_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_9;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_9;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_10:
            case VideoManager.VideoParameter.Group9.Index.V_9_10_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_10_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_10;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_10;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_11:
            case VideoManager.VideoParameter.Group9.Index.V_9_11_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_11_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_11;
                else if (VideoManager.VideoParameter.Group9.Comment.Section4.V_9_11_REF <= point && point < VideoManager.VideoParameter.Group9.Comment.Section5.V_9_11_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_11;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section3.V_9_11;
                break;
            case VideoManager.VideoParameter.Group9.Index.V_9_12:
            case VideoManager.VideoParameter.Group9.Index.V_9_12_R:
                if (point >= VideoManager.VideoParameter.Group9.Comment.Section5.V_9_12_REF)
                    comment = VideoManager.VideoParameter.Group9.Comment.Section5.V_9_12;
                else
                    comment = VideoManager.VideoParameter.Group9.Comment.Section4.V_9_12;
                break;
            default:
                comment = "";
        }

        return comment;
    }

    private int toFullCount(int videoID) {
        switch (videoID) {
            case SET_VIDEO_ID_1:
                return VideoManager.VideoParameter.Group1.Ref.Count.V_1_1 + VideoManager.VideoParameter.Group1.Ref.Count.V_1_8 + VideoManager.VideoParameter.Group1.Ref.Count.V_1_3
                        + VideoManager.VideoParameter.Group1.Ref.Count.V_1_6 + VideoManager.VideoParameter.Group1.Ref.Count.V_1_16;

            case SET_VIDEO_ID_2:
                return VideoManager.VideoParameter.Group2.Ref.Count.V_2_1 + VideoManager.VideoParameter.Group2.Ref.Count.V_2_2 + VideoManager.VideoParameter.Group2.Ref.Count.V_2_3
                        + VideoManager.VideoParameter.Group2.Ref.Count.V_2_4 + VideoManager.VideoParameter.Group2.Ref.Count.V_2_6;

            case SET_VIDEO_ID_3: // 손발털기
                return VideoManager.VideoParameter.Group3.Ref.Count.V_3_1 + VideoManager.VideoParameter.Group3.Ref.Count.V_3_4 + VideoManager.VideoParameter.Group3.Ref.Count.V_3_5;

            case SET_VIDEO_ID_4:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_1 + VideoManager.VideoParameter.Group4.Ref.Count.V_4_2 + VideoManager.VideoParameter.Group4.Ref.Count.V_4_3
                        + VideoManager.VideoParameter.Group4.Ref.Count.V_4_4 + VideoManager.VideoParameter.Group4.Ref.Count.V_4_5 + VideoManager.VideoParameter.Group4.Ref.Count.V_4_6
                        + VideoManager.VideoParameter.Group4.Ref.Count.V_4_7 + VideoManager.VideoParameter.Group4.Ref.Count.V_4_8 + VideoManager.VideoParameter.Group4.Ref.Count.V_4_9
                        + VideoManager.VideoParameter.Group4.Ref.Count.V_4_10;

            case SET_VIDEO_ID_5:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_1 + VideoManager.VideoParameter.Group5.Ref.Count.V_5_2 + VideoManager.VideoParameter.Group5.Ref.Count.V_5_3
                        + VideoManager.VideoParameter.Group5.Ref.Count.V_5_4 + VideoManager.VideoParameter.Group5.Ref.Count.V_5_5 + VideoManager.VideoParameter.Group5.Ref.Count.V_5_6
                        + VideoManager.VideoParameter.Group5.Ref.Count.V_5_7 + VideoManager.VideoParameter.Group5.Ref.Count.V_5_8 + VideoManager.VideoParameter.Group5.Ref.Count.V_5_9
                        + VideoManager.VideoParameter.Group5.Ref.Count.V_5_10;
            case SET_VIDEO_ID_6:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_1 + VideoManager.VideoParameter.Group6.Ref.Count.V_6_2 + VideoManager.VideoParameter.Group6.Ref.Count.V_6_3
                        + VideoManager.VideoParameter.Group6.Ref.Count.V_6_4 + VideoManager.VideoParameter.Group6.Ref.Count.V_6_5 + VideoManager.VideoParameter.Group6.Ref.Count.V_6_6
                        + VideoManager.VideoParameter.Group6.Ref.Count.V_6_7 + VideoManager.VideoParameter.Group6.Ref.Count.V_6_8 + VideoManager.VideoParameter.Group6.Ref.Count.V_6_9
                        + VideoManager.VideoParameter.Group6.Ref.Count.V_6_10;
            case SET_VIDEO_ID_7:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_1 + VideoManager.VideoParameter.Group7.Ref.Count.V_7_2 + VideoManager.VideoParameter.Group7.Ref.Count.V_7_3
                        + VideoManager.VideoParameter.Group7.Ref.Count.V_7_4 + VideoManager.VideoParameter.Group7.Ref.Count.V_7_5 + VideoManager.VideoParameter.Group7.Ref.Count.V_7_6
                        + VideoManager.VideoParameter.Group7.Ref.Count.V_7_7 + VideoManager.VideoParameter.Group7.Ref.Count.V_7_8 + VideoManager.VideoParameter.Group7.Ref.Count.V_7_9
                        + VideoManager.VideoParameter.Group7.Ref.Count.V_7_10;
            case SET_VIDEO_ID_8: // 출산부 1-4
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_1 + VideoManager.VideoParameter.Group8.Ref.Count.V_8_2 + VideoManager.VideoParameter.Group8.Ref.Count.V_8_3
                        + VideoManager.VideoParameter.Group8.Ref.Count.V_8_4 + VideoManager.VideoParameter.Group8.Ref.Count.V_8_5 + VideoManager.VideoParameter.Group8.Ref.Count.V_8_6
                        + VideoManager.VideoParameter.Group8.Ref.Count.V_8_7 + VideoManager.VideoParameter.Group8.Ref.Count.V_8_8 + VideoManager.VideoParameter.Group8.Ref.Count.V_8_9
                        + VideoManager.VideoParameter.Group8.Ref.Count.V_8_10 + VideoManager.VideoParameter.Group8.Ref.Count.V_8_11;
            case SET_VIDEO_ID_9: // 출산부 5~8주차
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_1 + VideoManager.VideoParameter.Group9.Ref.Count.V_9_2 + VideoManager.VideoParameter.Group9.Ref.Count.V_9_3
                        + VideoManager.VideoParameter.Group9.Ref.Count.V_9_4 + VideoManager.VideoParameter.Group9.Ref.Count.V_9_5 + VideoManager.VideoParameter.Group9.Ref.Count.V_9_6
                        + VideoManager.VideoParameter.Group9.Ref.Count.V_9_7 + VideoManager.VideoParameter.Group9.Ref.Count.V_9_8 + VideoManager.VideoParameter.Group9.Ref.Count.V_9_9
                        + VideoManager.VideoParameter.Group9.Ref.Count.V_9_10 + VideoManager.VideoParameter.Group9.Ref.Count.V_9_11 + VideoManager.VideoParameter.Group9.Ref.Count.V_9_12;
            default:
                return 100;
        }
    }

    private int toCount(int videoID) {
        switch (videoID) {
            case VideoManager.VideoParameter.Group1.Index.V_1_1:
            case VideoManager.VideoParameter.Group1.Index.V_1_1_R:
                return VideoManager.VideoParameter.Group1.Ref.Count.V_1_1;
            case VideoManager.VideoParameter.Group1.Index.V_1_8:
            case VideoManager.VideoParameter.Group1.Index.V_1_8_R:
                return VideoManager.VideoParameter.Group1.Ref.Count.V_1_8;
            case VideoManager.VideoParameter.Group1.Index.V_1_3:
            case VideoManager.VideoParameter.Group1.Index.V_1_3_R:
                return VideoManager.VideoParameter.Group1.Ref.Count.V_1_3;
            case VideoManager.VideoParameter.Group1.Index.V_1_6:
            case VideoManager.VideoParameter.Group1.Index.V_1_6_R:
                return VideoManager.VideoParameter.Group1.Ref.Count.V_1_6;
            case VideoManager.VideoParameter.Group1.Index.V_1_16:
            case VideoManager.VideoParameter.Group1.Index.V_1_16_R:
                return VideoManager.VideoParameter.Group1.Ref.Count.V_1_16;

            case VideoManager.VideoParameter.Group2.Index.V_2_1:
            case VideoManager.VideoParameter.Group2.Index.V_2_1_R:
                return VideoManager.VideoParameter.Group2.Ref.Count.V_2_1;
            case VideoManager.VideoParameter.Group2.Index.V_2_2:
            case VideoManager.VideoParameter.Group2.Index.V_2_2_R:
                return VideoManager.VideoParameter.Group2.Ref.Count.V_2_2;
            case VideoManager.VideoParameter.Group2.Index.V_2_3:
            case VideoManager.VideoParameter.Group2.Index.V_2_3_R:
                return VideoManager.VideoParameter.Group2.Ref.Count.V_2_3;
            case VideoManager.VideoParameter.Group2.Index.V_2_4:
            case VideoManager.VideoParameter.Group2.Index.V_2_4_R:
                return VideoManager.VideoParameter.Group2.Ref.Count.V_2_4;
            case VideoManager.VideoParameter.Group2.Index.V_2_6:
            case VideoManager.VideoParameter.Group2.Index.V_2_6_R:
                return VideoManager.VideoParameter.Group2.Ref.Count.V_2_6;

            case VideoManager.VideoParameter.Group3.Index.V_3_1: // 손발털기
            case VideoManager.VideoParameter.Group3.Index.V_3_1_R: // 손발털기
                return VideoManager.VideoParameter.Group3.Ref.Count.V_3_1;
            case VideoManager.VideoParameter.Group3.Index.V_3_4: // 스쿼트
            case VideoManager.VideoParameter.Group3.Index.V_3_4_R: // 스쿼트
                return VideoManager.VideoParameter.Group3.Ref.Count.V_3_4;
            case VideoManager.VideoParameter.Group3.Index.V_3_5: // 제자리걷기
            case VideoManager.VideoParameter.Group3.Index.V_3_5_R: // 제자리걷기
                return VideoManager.VideoParameter.Group3.Ref.Count.V_3_5;

            case VideoManager.VideoParameter.Group4.Index.V_4_1:
            case VideoManager.VideoParameter.Group4.Index.V_4_1_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_1;
            case VideoManager.VideoParameter.Group4.Index.V_4_2:
            case VideoManager.VideoParameter.Group4.Index.V_4_2_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_2;
            case VideoManager.VideoParameter.Group4.Index.V_4_3:
            case VideoManager.VideoParameter.Group4.Index.V_4_3_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_3;
            case VideoManager.VideoParameter.Group4.Index.V_4_4:
            case VideoManager.VideoParameter.Group4.Index.V_4_4_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_4;
            case VideoManager.VideoParameter.Group4.Index.V_4_5:
            case VideoManager.VideoParameter.Group4.Index.V_4_5_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_5;
            case VideoManager.VideoParameter.Group4.Index.V_4_6:
            case VideoManager.VideoParameter.Group4.Index.V_4_6_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_6;
            case VideoManager.VideoParameter.Group4.Index.V_4_7:
            case VideoManager.VideoParameter.Group4.Index.V_4_7_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_7;
            case VideoManager.VideoParameter.Group4.Index.V_4_8:
            case VideoManager.VideoParameter.Group4.Index.V_4_8_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_8;
            case VideoManager.VideoParameter.Group4.Index.V_4_9:
            case VideoManager.VideoParameter.Group4.Index.V_4_9_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_9;
            case VideoManager.VideoParameter.Group4.Index.V_4_10:
            case VideoManager.VideoParameter.Group4.Index.V_4_10_R:
                return VideoManager.VideoParameter.Group4.Ref.Count.V_4_10;

            case VideoManager.VideoParameter.Group5.Index.V_5_1:
            case VideoManager.VideoParameter.Group5.Index.V_5_1_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_1;
            case VideoManager.VideoParameter.Group5.Index.V_5_2:
            case VideoManager.VideoParameter.Group5.Index.V_5_2_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_2;
            case VideoManager.VideoParameter.Group5.Index.V_5_3:
            case VideoManager.VideoParameter.Group5.Index.V_5_3_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_3;
            case VideoManager.VideoParameter.Group5.Index.V_5_4:
            case VideoManager.VideoParameter.Group5.Index.V_5_4_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_4;
            case VideoManager.VideoParameter.Group5.Index.V_5_5:
            case VideoManager.VideoParameter.Group5.Index.V_5_5_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_5;
            case VideoManager.VideoParameter.Group5.Index.V_5_6:
            case VideoManager.VideoParameter.Group5.Index.V_5_6_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_6;
            case VideoManager.VideoParameter.Group5.Index.V_5_7:
            case VideoManager.VideoParameter.Group5.Index.V_5_7_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_7;
            case VideoManager.VideoParameter.Group5.Index.V_5_8:
            case VideoManager.VideoParameter.Group5.Index.V_5_8_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_8;
            case VideoManager.VideoParameter.Group5.Index.V_5_9:
            case VideoManager.VideoParameter.Group5.Index.V_5_9_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_9;
            case VideoManager.VideoParameter.Group5.Index.V_5_10:
            case VideoManager.VideoParameter.Group5.Index.V_5_10_R:
                return VideoManager.VideoParameter.Group5.Ref.Count.V_5_10;

            case VideoManager.VideoParameter.Group6.Index.V_6_1:
            case VideoManager.VideoParameter.Group6.Index.V_6_1_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_1;
            case VideoManager.VideoParameter.Group6.Index.V_6_2:
            case VideoManager.VideoParameter.Group6.Index.V_6_2_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_2;
            case VideoManager.VideoParameter.Group6.Index.V_6_3:
            case VideoManager.VideoParameter.Group6.Index.V_6_3_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_3;
            case VideoManager.VideoParameter.Group6.Index.V_6_4:
            case VideoManager.VideoParameter.Group6.Index.V_6_4_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_4;
            case VideoManager.VideoParameter.Group6.Index.V_6_5:
            case VideoManager.VideoParameter.Group6.Index.V_6_5_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_5;
            case VideoManager.VideoParameter.Group6.Index.V_6_6:
            case VideoManager.VideoParameter.Group6.Index.V_6_6_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_6;
            case VideoManager.VideoParameter.Group6.Index.V_6_7:
            case VideoManager.VideoParameter.Group6.Index.V_6_7_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_7;
            case VideoManager.VideoParameter.Group6.Index.V_6_8:
            case VideoManager.VideoParameter.Group6.Index.V_6_8_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_8;
            case VideoManager.VideoParameter.Group6.Index.V_6_9:
            case VideoManager.VideoParameter.Group6.Index.V_6_9_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_9;
            case VideoManager.VideoParameter.Group6.Index.V_6_10:
            case VideoManager.VideoParameter.Group6.Index.V_6_10_R:
                return VideoManager.VideoParameter.Group6.Ref.Count.V_6_10;

            case VideoManager.VideoParameter.Group7.Index.V_7_1:
            case VideoManager.VideoParameter.Group7.Index.V_7_1_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_1;
            case VideoManager.VideoParameter.Group7.Index.V_7_2:
            case VideoManager.VideoParameter.Group7.Index.V_7_2_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_2;
            case VideoManager.VideoParameter.Group7.Index.V_7_3:
            case VideoManager.VideoParameter.Group7.Index.V_7_3_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_3;
            case VideoManager.VideoParameter.Group7.Index.V_7_4:
            case VideoManager.VideoParameter.Group7.Index.V_7_4_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_4;
            case VideoManager.VideoParameter.Group7.Index.V_7_5:
            case VideoManager.VideoParameter.Group7.Index.V_7_5_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_5;
            case VideoManager.VideoParameter.Group7.Index.V_7_6:
            case VideoManager.VideoParameter.Group7.Index.V_7_6_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_6;
            case VideoManager.VideoParameter.Group7.Index.V_7_7:
            case VideoManager.VideoParameter.Group7.Index.V_7_7_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_7;
            case VideoManager.VideoParameter.Group7.Index.V_7_8:
            case VideoManager.VideoParameter.Group7.Index.V_7_8_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_8;
            case VideoManager.VideoParameter.Group7.Index.V_7_9:
            case VideoManager.VideoParameter.Group7.Index.V_7_9_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_9;
            case VideoManager.VideoParameter.Group7.Index.V_7_10:
            case VideoManager.VideoParameter.Group7.Index.V_7_10_R:
                return VideoManager.VideoParameter.Group7.Ref.Count.V_7_10;

            // 이하 출산부
            case VideoManager.VideoParameter.Group8.Index.V_8_1:
            case VideoManager.VideoParameter.Group8.Index.V_8_1_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_1;
            case VideoManager.VideoParameter.Group8.Index.V_8_2:
            case VideoManager.VideoParameter.Group8.Index.V_8_2_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_2;
            case VideoManager.VideoParameter.Group8.Index.V_8_3:
            case VideoManager.VideoParameter.Group8.Index.V_8_3_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_3;
            case VideoManager.VideoParameter.Group8.Index.V_8_4:
            case VideoManager.VideoParameter.Group8.Index.V_8_4_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_4;
            case VideoManager.VideoParameter.Group8.Index.V_8_5:
            case VideoManager.VideoParameter.Group8.Index.V_8_5_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_5;
            case VideoManager.VideoParameter.Group8.Index.V_8_6:
            case VideoManager.VideoParameter.Group8.Index.V_8_6_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_6;
            case VideoManager.VideoParameter.Group8.Index.V_8_7:
            case VideoManager.VideoParameter.Group8.Index.V_8_7_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_7;
            case VideoManager.VideoParameter.Group8.Index.V_8_8:
            case VideoManager.VideoParameter.Group8.Index.V_8_8_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_8;
            case VideoManager.VideoParameter.Group8.Index.V_8_9:
            case VideoManager.VideoParameter.Group8.Index.V_8_9_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_9;
            case VideoManager.VideoParameter.Group8.Index.V_8_10:
            case VideoManager.VideoParameter.Group8.Index.V_8_10_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_10;
            case VideoManager.VideoParameter.Group8.Index.V_8_11:
            case VideoManager.VideoParameter.Group8.Index.V_8_11_R:
                return VideoManager.VideoParameter.Group8.Ref.Count.V_8_11;

            // 5~8주차
            case VideoManager.VideoParameter.Group9.Index.V_9_1:
            case VideoManager.VideoParameter.Group9.Index.V_9_1_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_1;
            case VideoManager.VideoParameter.Group9.Index.V_9_2:
            case VideoManager.VideoParameter.Group9.Index.V_9_2_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_2;
            case VideoManager.VideoParameter.Group9.Index.V_9_3:
            case VideoManager.VideoParameter.Group9.Index.V_9_3_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_3;
            case VideoManager.VideoParameter.Group9.Index.V_9_4:
            case VideoManager.VideoParameter.Group9.Index.V_9_4_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_4;
            case VideoManager.VideoParameter.Group9.Index.V_9_5:
            case VideoManager.VideoParameter.Group9.Index.V_9_5_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_5;
            case VideoManager.VideoParameter.Group9.Index.V_9_6:
            case VideoManager.VideoParameter.Group9.Index.V_9_6_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_6;
            case VideoManager.VideoParameter.Group9.Index.V_9_7:
            case VideoManager.VideoParameter.Group9.Index.V_9_7_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_7;
            case VideoManager.VideoParameter.Group9.Index.V_9_8:
            case VideoManager.VideoParameter.Group9.Index.V_9_8_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_8;
            case VideoManager.VideoParameter.Group9.Index.V_9_9:
            case VideoManager.VideoParameter.Group9.Index.V_9_9_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_9;
            case VideoManager.VideoParameter.Group9.Index.V_9_10:
            case VideoManager.VideoParameter.Group9.Index.V_9_10_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_10;
            case VideoManager.VideoParameter.Group9.Index.V_9_11:
            case VideoManager.VideoParameter.Group9.Index.V_9_11_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_11;
            case VideoManager.VideoParameter.Group9.Index.V_9_12:
            case VideoManager.VideoParameter.Group9.Index.V_9_12_R:
                return VideoManager.VideoParameter.Group9.Ref.Count.V_9_12;
            default:
                return 15;
        }
    }

    private void onActivityName(int videoID, int currentPosition) {
        if (mIView == null)
            return;
        int video_Index = currentPlayVideoIndex;

        switch (videoID) {
            case SET_VIDEO_ID_1: // 원래 리소스 ID를 기준으로 동작시켰는데.. 미들웨어로 빠진다면..? 리소스는 없어.
                if (VideoParameter.Group1.Section.VIDEO_1_1_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_1_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group1.Index.V_1_1;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group1.Section.VIDEO_1_8_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_8_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group1.Index.V_1_8;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group1.Section.VIDEO_1_3_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_3_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group1.Index.V_1_3;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group1.Section.VIDEO_1_6_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_6_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group1.Index.V_1_6;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group1.Section.VIDEO_1_16_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_16_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group1.Index.V_1_16;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else
                    video_Index = 0;

                break;

            case SET_VIDEO_ID_2:
                if (VideoParameter.Group2.Section.VIDEO_2_1_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_1_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group2.Index.V_2_1;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group2.Section.VIDEO_2_2_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_2_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group2.Index.V_2_2;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group2.Section.VIDEO_2_3_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_3_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group2.Index.V_2_3;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group2.Section.VIDEO_2_4_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_4_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group2.Index.V_2_4;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group2.Section.VIDEO_2_6_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_6_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group2.Index.V_2_6;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else
                    video_Index = 0;
                break;

            case SET_VIDEO_ID_3:
                if (VideoParameter.Group3.Section.VIDEO_3_1_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_1_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group3.Index.V_3_1;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group3.Section.VIDEO_3_4_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_4_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group3.Index.V_3_4;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group3.Section.VIDEO_3_5_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_5_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group3.Index.V_3_5;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else
                    video_Index = 0;
                break;

            case SET_VIDEO_ID_4: // 홍두한 1
                if (VideoParameter.Group4.Section.VIDEO_4_1_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_1_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_1;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group4.Section.VIDEO_4_2_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_2_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_2;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group4.Section.VIDEO_4_3_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_3_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_3;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group4.Section.VIDEO_4_4_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_4_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_4;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group4.Section.VIDEO_4_5_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_5_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_5;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group4.Section.VIDEO_4_6_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_6_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_6;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group4.Section.VIDEO_4_7_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_7_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_7;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group4.Section.VIDEO_4_8_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_8_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_8;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group4.Section.VIDEO_4_9_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_9_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_9;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group4.Section.VIDEO_4_10_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_10_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group4.Index.V_4_10;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else
                    video_Index = 0;
                break;
            case SET_VIDEO_ID_5: // 홍두한 2
                if (VideoParameter.Group5.Section.VIDEO_5_1_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_1_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_1;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group5.Section.VIDEO_5_2_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_2_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_2;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group5.Section.VIDEO_5_3_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_3_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_3;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group5.Section.VIDEO_5_4_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_4_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_4;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group5.Section.VIDEO_5_5_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_5_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_5;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group5.Section.VIDEO_5_6_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_6_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_6;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group5.Section.VIDEO_5_7_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_7_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_7;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group5.Section.VIDEO_5_8_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_8_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_8;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group5.Section.VIDEO_5_9_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_9_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_9;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group5.Section.VIDEO_5_10_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_10_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group5.Index.V_5_10;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else
                    video_Index = 0;
                break;

            case SET_VIDEO_ID_6: // 이주영 1
                if (VideoParameter.Group6.Section.VIDEO_6_1_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_1_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_1;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group6.Section.VIDEO_6_2_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_2_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_2;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group6.Section.VIDEO_6_3_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_3_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_3;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group6.Section.VIDEO_6_4_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_4_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_4;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group6.Section.VIDEO_6_5_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_5_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_5;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group6.Section.VIDEO_6_6_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_6_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_6;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group6.Section.VIDEO_6_7_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_7_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_7;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group6.Section.VIDEO_6_8_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_8_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_8;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group6.Section.VIDEO_6_9_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_9_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_9;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group6.Section.VIDEO_6_10_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_10_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group6.Index.V_6_10;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else
                    video_Index = 0;
                break;

            case SET_VIDEO_ID_7: // 이주영 2
                if (VideoParameter.Group7.Section.VIDEO_7_1_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_1_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_1;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group7.Section.VIDEO_7_2_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_2_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_2;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group7.Section.VIDEO_7_3_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_3_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_3;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group7.Section.VIDEO_7_4_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_4_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_4;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group7.Section.VIDEO_7_5_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_5_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_5;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group7.Section.VIDEO_7_6_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_6_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_6;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group7.Section.VIDEO_7_7_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_7_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_7;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group7.Section.VIDEO_7_8_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_8_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_8;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group7.Section.VIDEO_7_9_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_9_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_9;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else if (VideoParameter.Group7.Section.VIDEO_7_10_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_10_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group7.Index.V_7_10;
                    // mIView.onBriefDescription(video_Index,
                    // setDescription(video_Index));
                } else
                    video_Index = 0;
                break;

            case SET_VIDEO_ID_8: // 출산부 1-4
                if (VideoParameter.Group8.Section.VIDEO_8_1_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_1_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_1;
                } else if (VideoParameter.Group8.Section.VIDEO_8_2_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_2_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_2;
                } else if (VideoParameter.Group8.Section.VIDEO_8_3_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_3_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_3;
                } else if (VideoParameter.Group8.Section.VIDEO_8_4_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_4_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_4;
                } else if (VideoParameter.Group8.Section.VIDEO_8_5_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_5_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_5;
                } else if (VideoParameter.Group8.Section.VIDEO_8_6_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_6_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_6;
                } else if (VideoParameter.Group8.Section.VIDEO_8_7_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_7_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_7;
                } else if (VideoParameter.Group8.Section.VIDEO_8_8_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_8_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_8;
                } else if (VideoParameter.Group8.Section.VIDEO_8_9_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_9_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_9;
                } else if (VideoParameter.Group8.Section.VIDEO_8_10_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_10_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_10;
                } else if (VideoParameter.Group8.Section.VIDEO_8_11_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_11_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group8.Index.V_8_11;
                } else
                    video_Index = 0;
                break;
            case SET_VIDEO_ID_9: // 출산부 5~8주차
                if (VideoParameter.Group9.Section.VIDEO_9_1_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_1_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_1;
                } else if (VideoParameter.Group9.Section.VIDEO_9_2_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_2_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_2;
                } else if (VideoParameter.Group9.Section.VIDEO_9_3_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_3_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_3;
                } else if (VideoParameter.Group9.Section.VIDEO_9_4_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_4_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_4;
                } else if (VideoParameter.Group9.Section.VIDEO_9_5_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_5_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_5;
                } else if (VideoParameter.Group9.Section.VIDEO_9_6_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_6_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_6;
                } else if (VideoParameter.Group9.Section.VIDEO_9_7_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_7_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_7;
                } else if (VideoParameter.Group9.Section.VIDEO_9_8_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_8_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_8;
                } else if (VideoParameter.Group9.Section.VIDEO_9_9_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_9_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_9;
                } else if (VideoParameter.Group9.Section.VIDEO_9_10_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_10_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_10;
                } else if (VideoParameter.Group9.Section.VIDEO_9_11_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_11_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_11;
                } else if (VideoParameter.Group9.Section.VIDEO_9_12_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_12_START_SEC + delay_brief) {
                    video_Index = VideoParameter.Group9.Index.V_9_12;
                } else
                    video_Index = 0;
                break;
        }
        if (currentPlayVideoIndex == video_Index)
            return;
        mIView.onBriefDescription(video_Index, setDescription(video_Index));
        currentPlayVideoIndex = video_Index;
    }

    /**
     * 심박수 zone을 계산.
     *
     * @return
     */
    private float[] getHeartRateDangerZone() {
        DataBase.UserProfile profile = mConfig.getUserProfile();
        if (profile == null)
            return new float[] { 0, 0 };

        float maxHR = 220 - profile.getAge();
        float largeHR = maxHR * 0.8f;

        return new float[] { maxHR, largeHR };
    }

    /**
     * 정확한 심박수가 들어온다는 가정하에 comment를 하기 위한 함수.
     *
     * @param hr
     */
    private void onHeartRateWarnning(int hr) {
        if (mIView == null)
            return;
        float[] hrZone = getHeartRateDangerZone();
        if (hrZone[1] < hr) {// 최대 심박수의 80%
            startVibrate();
            /// 언어팩 추가///
            if (LANGUAGE_SET == 1) {
                mIView.onWarnning("심박수가 너무 높으니 멈추고 안정을 취하세요");
            } else if (LANGUAGE_SET == 2) {
                mIView.onWarnning("心拍数がとても高いです。運動を中止して休んでください");
            } else if (LANGUAGE_SET == 3) {
                mIView.onWarnning("Your heart rate is too high, please stop the exercise and take a rest");
            }
            /// 까지///
        }
    }

    /**
     * 정확한 심박수가 들어온다는 가정하에 comment를 하기 위한 함수.
     *
     * @param hr
     */
    private void onHeartRateWarnningD(int hr) {
        if (mIView == null)
            return;
        float[] hrZone = getHeartRateDangerZone();
        if (hrZone[1] < hr) {// 최대 심박수의 80%
            /// 언어팩 추가///
            if (LANGUAGE_SET == 1) {
                mIView.onWarnningD("심박수가 너무 높으니 멈추고 안정을 취하세요");
            } else if (LANGUAGE_SET == 2) {
                mIView.onWarnningD("心拍数がとても高いです。運動を中止して休んでください");
            } else if (LANGUAGE_SET == 3) {
                mIView.onWarnning("Your heart rate is too high, please stop the exercise and take a rest");
            }
            /// 까지///
        }
    }

    /**
     * 진동 시작. 시작 중에 다시 시작되면 안됨. 2초,1초,2초
     */
    private void startVibrate() {
        if (set_vibrateLock)
            return;

        mTask = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {
                for (int i = 0; i < motionVibrate.length; i++) {
                    if (i % 2 == 0)
                        mBle.startVibrate();
                    else
                        mBle.stopVibrate();

                    try {
                        synchronized (this) {
                            wait(motionVibrate[i]);
                        }
                    } catch (InterruptedException e) {
                    }
                }

                set_vibrateLock = false;
                return null;
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
                set_vibrateLock = false;
                stopVibrate();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                set_vibrateLock = true;
            }
        };
        mTask.execute();
    }

    /**
     * 진동 중지. 동영상 end할때 핸들러 죽여야함.
     */
    private void stopVibrate() {
        mBle.stopVibrate();
    }

    /**
     * 동영상 종료. 재생이 끝나면 항상 실행해야 함.
     */
    public void end() {
        log(tag, "end()");
        isPlaying = false;
        releaseAccuracyLock();

        set_HRLock = false;
        mHandler_avgHR.removeCallbacks(mRunnable_avgHR);

        set_vibrateLock = false;
        if (mTask != null)
            mTask.cancel(true);

        // t.cancel();
        try {
            parseACC_X.clear();
            parseACC_Y.clear();
            parseACC_Z.clear();
            parseHR.clear();
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        BluetoothManager.unregistDataCallback();
    }

    /**
     * 사용자가 직접 동영상을 종료할 경우 수행.
     */
    public void userExit() {
        for (Long startTime : arrayStartTime)
            mConfig.deleteUserExerciseData(startTime);

        arrayStartTime.clear();
    }

    /**
     * 현재 동영상 재생을 알리는 flag setting
     *
     * @param setPlay
     *            현재 동영상의 재생 상태. (true:재생중, false:중지)
     */
    public void setPlaying(boolean setPlay) {
        this.setPlay = setPlay;
    }

    /**
     * 현 동영상의 재생 위치를 알림.
     *
     * @param currentPosition
     *            현재 동영상의 재생 위치. (sec 단위)
     */
    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public boolean isRVideo() {
        return is_R_Video;
    }

    public boolean isDisableUI() {
        return isDisableUI;
    }

    /**
     * 현재 재생 중인 동영상의 Index를 반환한다.
     *
     * @param videoID
     *            현재 재생중인 동영상 ID
     * @param currentPosition
     *            현재 재생 위치
     * @return 동영상 Index
     */
    private int switchVideo(int videoID, int currentPosition) {
        switch (videoID) {
            case SET_VIDEO_ID_1: // 원래 리소스 ID를 기준으로 동작시켰는데.. 미들웨어로 빠진다면..? 리소스는 없어.
                if (currentPosition <= VideoParameter.Group1.Section.VIDEO_1_1_START_SEC + VideoParameter.Group1.Section.VIDEO_1_1_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    return VideoParameter.Group1.Index.V_1_1_R;
                } else if (VideoParameter.Group1.Section.VIDEO_1_1_START_SEC + VideoParameter.Group1.Section.VIDEO_1_1_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_1_END_SEC) {
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group1.Index.V_1_1), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }

                    is_R_Video = false;
                    return VideoParameter.Group1.Index.V_1_1;
                } else if (VideoParameter.Group1.Section.VIDEO_1_1_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_8_START_SEC + VideoParameter.Group1.Section.VIDEO_1_8_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group1.Index.V_1_1;
                    return VideoParameter.Group1.Index.V_1_8_R;
                } else if (VideoParameter.Group1.Section.VIDEO_1_8_START_SEC + VideoParameter.Group1.Section.VIDEO_1_8_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_8_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group1.Index.V_1_8), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group1.Index.V_1_8;
                } else if (VideoParameter.Group1.Section.VIDEO_1_8_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_3_START_SEC + VideoParameter.Group1.Section.VIDEO_1_3_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group1.Index.V_1_8;
                    return VideoParameter.Group1.Index.V_1_3_R;
                } else if (VideoParameter.Group1.Section.VIDEO_1_3_START_SEC + VideoParameter.Group1.Section.VIDEO_1_3_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_3_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group1.Index.V_1_3), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group1.Index.V_1_3;
                } else if (VideoParameter.Group1.Section.VIDEO_1_3_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_6_START_SEC + VideoParameter.Group1.Section.VIDEO_1_6_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group1.Index.V_1_3;
                    return VideoParameter.Group1.Index.V_1_6_R;
                } else if (VideoParameter.Group1.Section.VIDEO_1_6_START_SEC + VideoParameter.Group1.Section.VIDEO_1_6_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_6_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group1.Index.V_1_6), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group1.Index.V_1_6;
                } else if (VideoParameter.Group1.Section.VIDEO_1_6_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_16_START_SEC + VideoParameter.Group1.Section.VIDEO_1_16_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group1.Index.V_1_6;
                    return VideoParameter.Group1.Index.V_1_16_R;
                } else if (VideoParameter.Group1.Section.VIDEO_1_16_START_SEC + VideoParameter.Group1.Section.VIDEO_1_16_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_16_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group1.Index.V_1_16), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group1.Index.V_1_16;
                } else if (VideoParameter.Group1.Section.VIDEO_1_16_END_SEC < currentPosition) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group1.Index.V_1_16;
                }

                break;

            case SET_VIDEO_ID_2:
                if (currentPosition <= VideoParameter.Group2.Section.VIDEO_2_1_START_SEC + VideoParameter.Group2.Section.VIDEO_2_1_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    return VideoParameter.Group2.Index.V_2_1_R;
                } else if (VideoParameter.Group2.Section.VIDEO_2_1_START_SEC + VideoParameter.Group2.Section.VIDEO_2_1_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_1_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group2.Index.V_2_1), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group2.Index.V_2_1;
                } else if (VideoParameter.Group2.Section.VIDEO_2_1_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_2_START_SEC + VideoParameter.Group2.Section.VIDEO_2_2_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group2.Index.V_2_1;
                    return VideoParameter.Group2.Index.V_2_2_R;
                } else if (VideoParameter.Group2.Section.VIDEO_2_2_START_SEC + VideoParameter.Group2.Section.VIDEO_2_2_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_2_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group2.Index.V_2_2), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group2.Index.V_2_2;
                } else if (VideoParameter.Group2.Section.VIDEO_2_2_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_3_START_SEC + VideoParameter.Group2.Section.VIDEO_2_3_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group2.Index.V_2_2;
                    return VideoParameter.Group2.Index.V_2_3_R;
                } else if (VideoParameter.Group2.Section.VIDEO_2_3_START_SEC + VideoParameter.Group2.Section.VIDEO_2_3_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_3_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group2.Index.V_2_3), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group2.Index.V_2_3;
                } else if (VideoParameter.Group2.Section.VIDEO_2_3_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_4_START_SEC + VideoParameter.Group2.Section.VIDEO_2_4_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group2.Index.V_2_3;
                    return VideoParameter.Group2.Index.V_2_4_R;
                } else if (VideoParameter.Group2.Section.VIDEO_2_4_START_SEC + VideoParameter.Group2.Section.VIDEO_2_4_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_4_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group2.Index.V_2_4), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group2.Index.V_2_4;
                } else if (VideoParameter.Group2.Section.VIDEO_2_4_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_6_START_SEC + VideoParameter.Group2.Section.VIDEO_2_6_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group2.Index.V_2_4;
                    return VideoParameter.Group2.Index.V_2_6_R;
                } else if (VideoParameter.Group2.Section.VIDEO_2_6_START_SEC + VideoParameter.Group2.Section.VIDEO_2_6_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_6_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group2.Index.V_2_6), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group2.Index.V_2_6;
                } else if (VideoParameter.Group2.Section.VIDEO_2_6_END_SEC < currentPosition) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group2.Index.V_2_6;
                }

                break;

            case SET_VIDEO_ID_3:
			/*
			 * if (currentPosition <=
			 * VideoParameter.Group3.Section.VIDEO_3_0_START_SEC) { is_R_Video =
			 * true; setTag = false; return VideoParameter.Group3.Index.V_3_0_R;
			 * } else if (VideoParameter.Group3.Section.VIDEO_3_0_START_SEC <
			 * currentPosition && currentPosition <=
			 * VideoParameter.Group3.Section.VIDEO_3_0_END_SEC) { if(!setTag) {
			 * if(setSave)
			 * saveTag(mSdPath,setStartTag(VideoParameter.Group3.Index.V_3_0),
			 * realFileName); setTag = true; }
			 *
			 * is_R_Video = false; return VideoParameter.Group3.Index.V_3_0; }
			 * else if (VideoParameter.Group3.Section.VIDEO_3_0_END_SEC <
			 * currentPosition && currentPosition <=
			 * VideoParameter.Group3.Section.VIDEO_3_1_START_SEC) { is_R_Video =
			 * true; setTag = false; return VideoParameter.Group3.Index.V_3_1_R;
			 * }
			 */
                if (currentPosition <= VideoParameter.Group3.Section.VIDEO_3_1_START_SEC + VideoParameter.Group3.Section.VIDEO_3_1_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    return VideoParameter.Group3.Index.V_3_1_R;
                } else if (VideoParameter.Group3.Section.VIDEO_3_1_START_SEC + VideoParameter.Group3.Section.VIDEO_3_1_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_1_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group3.Index.V_3_1), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group3.Index.V_3_1;
                } else if (VideoParameter.Group3.Section.VIDEO_3_1_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_4_START_SEC + VideoParameter.Group3.Section.VIDEO_3_4_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group3.Index.V_3_1;
                    return VideoParameter.Group3.Index.V_3_4_R;
                } else if (VideoParameter.Group3.Section.VIDEO_3_4_START_SEC + VideoParameter.Group3.Section.VIDEO_3_4_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_4_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group3.Index.V_3_4), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group3.Index.V_3_4;
                } else if (VideoParameter.Group3.Section.VIDEO_3_4_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_5_START_SEC + VideoParameter.Group3.Section.VIDEO_3_5_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group3.Index.V_3_4;
                    return VideoParameter.Group3.Index.V_3_5_R;
                } else if (VideoParameter.Group3.Section.VIDEO_3_5_START_SEC + VideoParameter.Group3.Section.VIDEO_3_5_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_5_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group3.Index.V_3_5), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group3.Index.V_3_5;
                } else if (VideoParameter.Group3.Section.VIDEO_3_5_END_SEC < currentPosition) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group3.Index.V_3_5;
                }

                break;

            case SET_VIDEO_ID_4: // 홍두한 1
                if (currentPosition <= VideoParameter.Group4.Section.VIDEO_4_1_START_SEC + VideoParameter.Group4.Section.VIDEO_4_1_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    return VideoParameter.Group4.Index.V_4_1_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_1_START_SEC + VideoParameter.Group4.Section.VIDEO_4_1_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_1_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_1), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group4.Index.V_4_1;
                } else if (VideoParameter.Group4.Section.VIDEO_4_1_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_2_START_SEC + VideoParameter.Group4.Section.VIDEO_4_2_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_1;
                    return VideoParameter.Group4.Index.V_4_2_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_2_START_SEC + VideoParameter.Group4.Section.VIDEO_4_2_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_2_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_2), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group4.Index.V_4_2;
                } else if (VideoParameter.Group4.Section.VIDEO_4_2_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_3_START_SEC + VideoParameter.Group4.Section.VIDEO_4_3_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_2;
                    return VideoParameter.Group4.Index.V_4_3_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_3_START_SEC + VideoParameter.Group4.Section.VIDEO_4_3_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_3_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_3), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group4.Index.V_4_3;
                } else if (VideoParameter.Group4.Section.VIDEO_4_3_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_4_START_SEC + VideoParameter.Group4.Section.VIDEO_4_4_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_3;
                    return VideoParameter.Group4.Index.V_4_4_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_4_START_SEC + VideoParameter.Group4.Section.VIDEO_4_4_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_4_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_4), realFileName);
                        setTag = true;
                    }
                    return VideoParameter.Group4.Index.V_4_4;
                } else if (VideoParameter.Group4.Section.VIDEO_4_4_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_5_START_SEC + VideoParameter.Group4.Section.VIDEO_4_5_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_4;
                    return VideoParameter.Group4.Index.V_4_5_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_5_START_SEC + VideoParameter.Group4.Section.VIDEO_4_5_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_5_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_5), realFileName);
                        setTag = true;
                    }
                    return VideoParameter.Group4.Index.V_4_5;
                } else if (VideoParameter.Group4.Section.VIDEO_4_5_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_6_START_SEC + VideoParameter.Group4.Section.VIDEO_4_6_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_5;
                    return VideoParameter.Group4.Index.V_4_6_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_6_START_SEC + VideoParameter.Group4.Section.VIDEO_4_6_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_6_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_6), realFileName);
                        setTag = true;
                    }
                    return VideoParameter.Group4.Index.V_4_6;
                } else if (VideoParameter.Group4.Section.VIDEO_4_6_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_7_START_SEC + VideoParameter.Group4.Section.VIDEO_4_7_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_6;
                    return VideoParameter.Group4.Index.V_4_7_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_7_START_SEC + VideoParameter.Group4.Section.VIDEO_4_7_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_7_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_7), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group4.Index.V_4_7;
                } else if (VideoParameter.Group4.Section.VIDEO_4_7_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_8_START_SEC + VideoParameter.Group4.Section.VIDEO_4_8_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_7;
                    return VideoParameter.Group4.Index.V_4_8_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_8_START_SEC + VideoParameter.Group4.Section.VIDEO_4_8_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_8_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_8), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group4.Index.V_4_8;
                } else if (VideoParameter.Group4.Section.VIDEO_4_8_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_9_START_SEC + VideoParameter.Group4.Section.VIDEO_4_9_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_8;
                    return VideoParameter.Group4.Index.V_4_9_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_9_START_SEC + VideoParameter.Group4.Section.VIDEO_4_9_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_9_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_9), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group4.Index.V_4_9;
                } else if (VideoParameter.Group4.Section.VIDEO_4_9_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_10_START_SEC + VideoParameter.Group4.Section.VIDEO_4_10_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_9;
                    return VideoParameter.Group4.Index.V_4_10_R;
                } else if (VideoParameter.Group4.Section.VIDEO_4_10_START_SEC + VideoParameter.Group4.Section.VIDEO_4_10_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_10_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group4.Index.V_4_10), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group4.Index.V_4_10;
                } else if (VideoParameter.Group4.Section.VIDEO_4_10_END_SEC < currentPosition) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group4.Index.V_4_10;
                }

                break;
            case SET_VIDEO_ID_5: // 홍두한 2
                if (currentPosition <= VideoParameter.Group5.Section.VIDEO_5_1_START_SEC + VideoParameter.Group5.Section.VIDEO_5_1_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    return VideoParameter.Group5.Index.V_5_1_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_1_START_SEC + VideoParameter.Group5.Section.VIDEO_5_1_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_1_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_1), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_1;
                } else if (VideoParameter.Group5.Section.VIDEO_5_1_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_2_START_SEC + VideoParameter.Group5.Section.VIDEO_5_2_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_1;
                    return VideoParameter.Group5.Index.V_5_2_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_2_START_SEC + VideoParameter.Group5.Section.VIDEO_5_2_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_2_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_2), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_2;
                } else if (VideoParameter.Group5.Section.VIDEO_5_2_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_3_START_SEC + VideoParameter.Group5.Section.VIDEO_5_3_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_2;
                    return VideoParameter.Group5.Index.V_5_3_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_3_START_SEC + VideoParameter.Group5.Section.VIDEO_5_3_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_3_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_3), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_3;
                } else if (VideoParameter.Group5.Section.VIDEO_5_3_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_4_START_SEC + VideoParameter.Group5.Section.VIDEO_5_4_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_3;
                    return VideoParameter.Group5.Index.V_5_4_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_4_START_SEC + VideoParameter.Group5.Section.VIDEO_5_4_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_4_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_4), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_4;
                } else if (VideoParameter.Group5.Section.VIDEO_5_4_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_5_START_SEC + VideoParameter.Group5.Section.VIDEO_5_5_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_4;
                    return VideoParameter.Group5.Index.V_5_5_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_5_START_SEC + VideoParameter.Group5.Section.VIDEO_5_5_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_5_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_5), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_5;
                } else if (VideoParameter.Group5.Section.VIDEO_5_5_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_6_START_SEC + VideoParameter.Group5.Section.VIDEO_5_6_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_5;
                    return VideoParameter.Group5.Index.V_5_6_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_6_START_SEC + VideoParameter.Group5.Section.VIDEO_5_6_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_6_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_6), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_6;
                } else if (VideoParameter.Group5.Section.VIDEO_5_6_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_7_START_SEC + VideoParameter.Group5.Section.VIDEO_5_7_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_6;
                    return VideoParameter.Group5.Index.V_5_7_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_7_START_SEC + VideoParameter.Group5.Section.VIDEO_5_7_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_7_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_7), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_7;
                } else if (VideoParameter.Group5.Section.VIDEO_5_7_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_8_START_SEC + VideoParameter.Group5.Section.VIDEO_5_8_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_7;
                    return VideoParameter.Group5.Index.V_5_8_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_8_START_SEC + VideoParameter.Group5.Section.VIDEO_5_8_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_8_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_8), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_8;
                } else if (VideoParameter.Group5.Section.VIDEO_5_8_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_9_START_SEC + VideoParameter.Group5.Section.VIDEO_5_9_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_8;
                    return VideoParameter.Group5.Index.V_5_9_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_9_START_SEC + VideoParameter.Group5.Section.VIDEO_5_9_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_9_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_9), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_9;
                } else if (VideoParameter.Group5.Section.VIDEO_5_9_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_10_START_SEC + VideoParameter.Group5.Section.VIDEO_5_10_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_9;
                    return VideoParameter.Group5.Index.V_5_10_R;
                } else if (VideoParameter.Group5.Section.VIDEO_5_10_START_SEC + VideoParameter.Group5.Section.VIDEO_5_10_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_10_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group5.Index.V_5_10), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group5.Index.V_5_10;
                } else if (VideoParameter.Group5.Section.VIDEO_5_10_END_SEC < currentPosition) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group5.Index.V_5_10;
                }
                break;

            case SET_VIDEO_ID_6: // 이주영 1
                if (currentPosition <= VideoParameter.Group6.Section.VIDEO_6_1_START_SEC + VideoParameter.Group6.Section.VIDEO_6_1_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    return VideoParameter.Group6.Index.V_6_1_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_1_START_SEC + VideoParameter.Group6.Section.VIDEO_6_1_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_1_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_1), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_1;
                } else if (VideoParameter.Group6.Section.VIDEO_6_1_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_2_START_SEC + VideoParameter.Group6.Section.VIDEO_6_2_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_1;
                    return VideoParameter.Group6.Index.V_6_2_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_2_START_SEC + VideoParameter.Group6.Section.VIDEO_6_2_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_2_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_2), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_2;
                } else if (VideoParameter.Group6.Section.VIDEO_6_2_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_3_START_SEC + VideoParameter.Group6.Section.VIDEO_6_3_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_2;
                    return VideoParameter.Group6.Index.V_6_3_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_3_START_SEC + VideoParameter.Group6.Section.VIDEO_6_3_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_3_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_3), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_3;
                } else if (VideoParameter.Group6.Section.VIDEO_6_3_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_4_START_SEC + VideoParameter.Group6.Section.VIDEO_6_4_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_3;
                    return VideoParameter.Group6.Index.V_6_4_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_4_START_SEC + VideoParameter.Group6.Section.VIDEO_6_4_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_4_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_4), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_4;
                } else if (VideoParameter.Group6.Section.VIDEO_6_4_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_5_START_SEC + VideoParameter.Group6.Section.VIDEO_6_5_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_4;
                    return VideoParameter.Group6.Index.V_6_5_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_5_START_SEC + VideoParameter.Group6.Section.VIDEO_6_5_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_5_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_5), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_5;
                } else if (VideoParameter.Group6.Section.VIDEO_6_5_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_6_START_SEC + VideoParameter.Group6.Section.VIDEO_6_6_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_5;
                    return VideoParameter.Group6.Index.V_6_6_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_6_START_SEC + VideoParameter.Group6.Section.VIDEO_6_6_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_6_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_6), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_6;
                } else if (VideoParameter.Group6.Section.VIDEO_6_6_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_7_START_SEC + VideoParameter.Group6.Section.VIDEO_6_7_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_6;
                    return VideoParameter.Group6.Index.V_6_7_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_7_START_SEC + VideoParameter.Group6.Section.VIDEO_6_7_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_7_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_7), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_7;
                } else if (VideoParameter.Group6.Section.VIDEO_6_7_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_8_START_SEC + VideoParameter.Group6.Section.VIDEO_6_8_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_7;
                    return VideoParameter.Group6.Index.V_6_8_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_8_START_SEC + VideoParameter.Group6.Section.VIDEO_6_8_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_8_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_8), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_8;
                } else if (VideoParameter.Group6.Section.VIDEO_6_8_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_9_START_SEC + VideoParameter.Group6.Section.VIDEO_6_9_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_8;
                    return VideoParameter.Group6.Index.V_6_9_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_9_START_SEC + VideoParameter.Group6.Section.VIDEO_6_9_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_9_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_9), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_9;
                } else if (VideoParameter.Group6.Section.VIDEO_6_9_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_10_START_SEC + VideoParameter.Group6.Section.VIDEO_6_10_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_9;
                    return VideoParameter.Group6.Index.V_6_10_R;
                } else if (VideoParameter.Group6.Section.VIDEO_6_10_START_SEC + VideoParameter.Group6.Section.VIDEO_6_10_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_10_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group6.Index.V_6_10), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group6.Index.V_6_10;
                } else if (VideoParameter.Group6.Section.VIDEO_6_10_END_SEC < currentPosition) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group6.Index.V_6_10;
                }
                break;

            case SET_VIDEO_ID_7: // 이주영 2
                if (currentPosition <= VideoParameter.Group7.Section.VIDEO_7_1_START_SEC + VideoParameter.Group7.Section.VIDEO_7_1_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    return VideoParameter.Group7.Index.V_7_1_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_1_START_SEC + VideoParameter.Group7.Section.VIDEO_7_1_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_1_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_1), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_1;
                } else if (VideoParameter.Group7.Section.VIDEO_7_1_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_2_START_SEC + VideoParameter.Group7.Section.VIDEO_7_2_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_1;
                    return VideoParameter.Group7.Index.V_7_2_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_2_START_SEC + VideoParameter.Group7.Section.VIDEO_7_2_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_2_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_2), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_2;
                } else if (VideoParameter.Group7.Section.VIDEO_7_2_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_3_START_SEC + VideoParameter.Group7.Section.VIDEO_7_3_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_2;
                    return VideoParameter.Group7.Index.V_7_3_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_3_START_SEC + VideoParameter.Group7.Section.VIDEO_7_3_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_3_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_3), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_3;
                } else if (VideoParameter.Group7.Section.VIDEO_7_3_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_4_START_SEC + VideoParameter.Group7.Section.VIDEO_7_4_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_3;
                    return VideoParameter.Group7.Index.V_7_4_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_4_START_SEC + VideoParameter.Group7.Section.VIDEO_7_4_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_4_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_4), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_4;
                } else if (VideoParameter.Group7.Section.VIDEO_7_4_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_5_START_SEC + VideoParameter.Group7.Section.VIDEO_7_5_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_4;
                    return VideoParameter.Group7.Index.V_7_5_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_5_START_SEC + VideoParameter.Group7.Section.VIDEO_7_5_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_5_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_5), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_5;
                } else if (VideoParameter.Group7.Section.VIDEO_7_5_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_6_START_SEC + VideoParameter.Group7.Section.VIDEO_7_6_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_5;
                    return VideoParameter.Group7.Index.V_7_6_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_6_START_SEC + VideoParameter.Group7.Section.VIDEO_7_6_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_6_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_6), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_6;
                } else if (VideoParameter.Group7.Section.VIDEO_7_6_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_7_START_SEC + VideoParameter.Group7.Section.VIDEO_7_7_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_6;
                    return VideoParameter.Group7.Index.V_7_7_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_7_START_SEC + VideoParameter.Group7.Section.VIDEO_7_7_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_7_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_7), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_7;
                } else if (VideoParameter.Group7.Section.VIDEO_7_7_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_8_START_SEC + VideoParameter.Group7.Section.VIDEO_7_8_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_7;
                    return VideoParameter.Group7.Index.V_7_8_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_8_START_SEC + VideoParameter.Group7.Section.VIDEO_7_8_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_8_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_8), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_8;
                } else if (VideoParameter.Group7.Section.VIDEO_7_8_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_9_START_SEC + VideoParameter.Group7.Section.VIDEO_7_9_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_8;
                    return VideoParameter.Group7.Index.V_7_9_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_9_START_SEC + VideoParameter.Group7.Section.VIDEO_7_9_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_9_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_9), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_9;
                } else if (VideoParameter.Group7.Section.VIDEO_7_9_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_10_START_SEC + VideoParameter.Group7.Section.VIDEO_7_10_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_9;
                    return VideoParameter.Group7.Index.V_7_10_R;
                } else if (VideoParameter.Group7.Section.VIDEO_7_10_START_SEC + VideoParameter.Group7.Section.VIDEO_7_10_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_10_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group7.Index.V_7_10), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group7.Index.V_7_10;
                } else if (VideoParameter.Group7.Section.VIDEO_7_10_END_SEC < currentPosition) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group7.Index.V_7_10;
                }
                break;

            case SET_VIDEO_ID_8: // 출산부 1-4주차
                if (currentPosition <= VideoParameter.Group8.Section.VIDEO_8_1_START_SEC + VideoParameter.Group8.Section.VIDEO_8_1_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    return VideoParameter.Group8.Index.V_8_1_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_1_START_SEC + VideoParameter.Group8.Section.VIDEO_8_1_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_1_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_1), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_1;
                } else if (VideoParameter.Group8.Section.VIDEO_8_1_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_2_START_SEC + VideoParameter.Group8.Section.VIDEO_8_2_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_1;
                    return VideoParameter.Group8.Index.V_8_2_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_2_START_SEC + VideoParameter.Group8.Section.VIDEO_8_2_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_2_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_2), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_2;
                } else if (VideoParameter.Group8.Section.VIDEO_8_2_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_3_START_SEC + VideoParameter.Group8.Section.VIDEO_8_3_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_2;
                    return VideoParameter.Group8.Index.V_8_3_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_3_START_SEC + VideoParameter.Group8.Section.VIDEO_8_3_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_3_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_3), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_3;
                } else if (VideoParameter.Group8.Section.VIDEO_8_3_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_4_START_SEC + VideoParameter.Group8.Section.VIDEO_8_4_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_3;
                    return VideoParameter.Group8.Index.V_8_4_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_4_START_SEC + VideoParameter.Group8.Section.VIDEO_8_4_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_4_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_4), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_4;
                } else if (VideoParameter.Group8.Section.VIDEO_8_4_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_5_START_SEC + VideoParameter.Group8.Section.VIDEO_8_5_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_4;
                    return VideoParameter.Group8.Index.V_8_5_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_5_START_SEC + VideoParameter.Group8.Section.VIDEO_8_5_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_5_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_5), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_5;
                } else if (VideoParameter.Group8.Section.VIDEO_8_5_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_6_START_SEC + VideoParameter.Group8.Section.VIDEO_8_6_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_5;
                    return VideoParameter.Group8.Index.V_8_6_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_6_START_SEC + VideoParameter.Group8.Section.VIDEO_8_6_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_6_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_6), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_6;
                } else if (VideoParameter.Group8.Section.VIDEO_8_6_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_7_START_SEC + VideoParameter.Group8.Section.VIDEO_8_7_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_6;
                    return VideoParameter.Group8.Index.V_8_7_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_7_START_SEC + VideoParameter.Group8.Section.VIDEO_8_7_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_7_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_7), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_7;
                } else if (VideoParameter.Group8.Section.VIDEO_8_7_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_8_START_SEC + VideoParameter.Group8.Section.VIDEO_8_8_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_7;
                    return VideoParameter.Group8.Index.V_8_8_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_8_START_SEC + VideoParameter.Group8.Section.VIDEO_8_8_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_8_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_8), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_8;
                } else if (VideoParameter.Group8.Section.VIDEO_8_8_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_9_START_SEC + VideoParameter.Group8.Section.VIDEO_8_9_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_8;
                    return VideoParameter.Group8.Index.V_8_9_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_8_START_SEC + VideoParameter.Group8.Section.VIDEO_8_9_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_9_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_9), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_9;
                } else if (VideoParameter.Group8.Section.VIDEO_8_9_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_10_START_SEC + VideoParameter.Group8.Section.VIDEO_8_10_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_9;
                    return VideoParameter.Group8.Index.V_8_10_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_10_START_SEC + VideoParameter.Group8.Section.VIDEO_8_10_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_10_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_10), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_10;
                } else if (VideoParameter.Group8.Section.VIDEO_8_10_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_11_START_SEC + VideoParameter.Group8.Section.VIDEO_8_11_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_10;
                    return VideoParameter.Group8.Index.V_8_11_R;
                } else if (VideoParameter.Group8.Section.VIDEO_8_11_START_SEC + VideoParameter.Group8.Section.VIDEO_8_11_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_11_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group8.Index.V_8_11), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group8.Index.V_8_11;
                } else if (VideoParameter.Group8.Section.VIDEO_8_11_END_SEC < currentPosition) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group8.Index.V_8_11;
                    // return VideoParameter.Group8.Index.V_8_11; // 마지막 총점 뿌리기 위함.
                }
                break;

            case SET_VIDEO_ID_9: // 출산부 5~8주차
                if (currentPosition <= VideoParameter.Group9.Section.VIDEO_9_1_START_SEC + VideoParameter.Group9.Section.VIDEO_9_1_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    return VideoParameter.Group9.Index.V_9_1_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_1_START_SEC + VideoParameter.Group9.Section.VIDEO_9_1_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_1_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_1), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_1;
                } else if (VideoParameter.Group9.Section.VIDEO_9_1_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_2_START_SEC + VideoParameter.Group9.Section.VIDEO_9_2_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_1;
                    return VideoParameter.Group9.Index.V_9_2_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_2_START_SEC + VideoParameter.Group9.Section.VIDEO_9_2_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_2_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_2), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_2;
                } else if (VideoParameter.Group9.Section.VIDEO_9_2_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_3_START_SEC + VideoParameter.Group9.Section.VIDEO_9_3_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_2;
                    return VideoParameter.Group9.Index.V_9_3_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_3_START_SEC + VideoParameter.Group9.Section.VIDEO_9_3_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_3_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_3), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_3;
                } else if (VideoParameter.Group9.Section.VIDEO_9_3_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_4_START_SEC + VideoParameter.Group9.Section.VIDEO_9_4_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_3;
                    return VideoParameter.Group9.Index.V_9_4_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_4_START_SEC + VideoParameter.Group9.Section.VIDEO_9_4_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_4_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_4), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_4;
                } else if (VideoParameter.Group9.Section.VIDEO_9_4_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_5_START_SEC + VideoParameter.Group9.Section.VIDEO_9_5_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_4;
                    return VideoParameter.Group9.Index.V_9_5_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_5_START_SEC + VideoParameter.Group9.Section.VIDEO_9_5_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_5_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_5), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_5;
                } else if (VideoParameter.Group9.Section.VIDEO_9_5_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_6_START_SEC + VideoParameter.Group9.Section.VIDEO_9_6_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_5;
                    return VideoParameter.Group9.Index.V_9_6_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_6_START_SEC + VideoParameter.Group9.Section.VIDEO_9_6_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_6_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_6), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_6;
                } else if (VideoParameter.Group9.Section.VIDEO_9_6_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_7_START_SEC + VideoParameter.Group9.Section.VIDEO_9_7_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_6;
                    return VideoParameter.Group9.Index.V_9_7_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_7_START_SEC + VideoParameter.Group9.Section.VIDEO_9_7_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_7_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_7), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_7;
                } else if (VideoParameter.Group9.Section.VIDEO_9_7_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_8_START_SEC + VideoParameter.Group9.Section.VIDEO_9_8_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_7;
                    return VideoParameter.Group9.Index.V_9_8_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_8_START_SEC + VideoParameter.Group9.Section.VIDEO_9_8_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_8_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_8), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_8;
                } else if (VideoParameter.Group9.Section.VIDEO_9_8_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_9_START_SEC + VideoParameter.Group9.Section.VIDEO_9_9_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_8;
                    return VideoParameter.Group9.Index.V_9_9_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_9_START_SEC + VideoParameter.Group9.Section.VIDEO_9_9_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_9_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_9), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_9;
                } else if (VideoParameter.Group9.Section.VIDEO_9_9_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_10_START_SEC + VideoParameter.Group9.Section.VIDEO_9_10_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_9;
                    return VideoParameter.Group9.Index.V_9_10_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_10_START_SEC + VideoParameter.Group9.Section.VIDEO_9_10_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_10_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_10), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_10;
                } else if (VideoParameter.Group9.Section.VIDEO_9_10_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_11_START_SEC + VideoParameter.Group9.Section.VIDEO_9_11_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_10;
                    return VideoParameter.Group9.Index.V_9_11_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_11_START_SEC + VideoParameter.Group9.Section.VIDEO_9_11_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_11_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_11), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_11;
                } else if (VideoParameter.Group9.Section.VIDEO_9_11_END_SEC < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_12_START_SEC + VideoParameter.Group9.Section.VIDEO_9_12_EXTRA_DELAY) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_11;
                    return VideoParameter.Group9.Index.V_9_12_R;
                } else if (VideoParameter.Group9.Section.VIDEO_9_12_START_SEC + VideoParameter.Group9.Section.VIDEO_9_12_EXTRA_DELAY < currentPosition
                        && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_12_END_SEC) {
                    is_R_Video = false;
                    if (!setTag) {
                        if (setSave)
                            saveTag(mSdPath, setStartTag(VideoParameter.Group9.Index.V_9_12), realFileName);
                        setTag = true;
                        save_start_time = System.currentTimeMillis();
                    }
                    return VideoParameter.Group9.Index.V_9_12;
                } else if (VideoParameter.Group9.Section.VIDEO_9_12_END_SEC < currentPosition) {
                    is_R_Video = true;
                    setTag = false;
                    is_R_vid = VideoParameter.Group9.Index.V_9_12;
                    // return VideoParameter.Group9.Index.V_9_12; // 마지막 총점 뿌리기 위함.
                }
                break;
        }

        return 0;
    }

    /**
     * 영상의 UI를 표시하기 위해, 시간을 결정해줌.
     */
    private void switchUI(int videoID, int currentPosition) {
        switch (videoID) {
            case SET_VIDEO_ID_1: // 원래 리소스 ID를 기준으로 동작시켰는데.. 미들웨어로 빠진다면..? 리소스는 없어.
                if (currentPosition <= VideoParameter.Group1.Section.VIDEO_1_1_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group1.Section.VIDEO_1_1_START_SEC < currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_1_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group1.Section.VIDEO_1_1_END_SEC < currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_8_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group1.Section.VIDEO_1_8_START_SEC < currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_8_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group1.Section.VIDEO_1_8_END_SEC < currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_3_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group1.Section.VIDEO_1_3_START_SEC < currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_3_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group1.Section.VIDEO_1_3_END_SEC < currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_6_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group1.Section.VIDEO_1_6_START_SEC < currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_6_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group1.Section.VIDEO_1_6_END_SEC < currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_16_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group1.Section.VIDEO_1_16_START_SEC < currentPosition && currentPosition <= VideoParameter.Group1.Section.VIDEO_1_16_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group1.Section.VIDEO_1_16_END_SEC < currentPosition) {
                    isDisableUI = true;
                }

                break;

            case SET_VIDEO_ID_2:
                if (currentPosition <= VideoParameter.Group2.Section.VIDEO_2_1_START_SEC + VideoParameter.Group2.Section.VIDEO_2_1_EXTRA_DELAY) {
                    isDisableUI = true;
                } else if (VideoParameter.Group2.Section.VIDEO_2_1_START_SEC < currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_1_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group2.Section.VIDEO_2_1_END_SEC < currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_2_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group2.Section.VIDEO_2_2_START_SEC < currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_2_END_SEC - 1) {
                    isDisableUI = false;
                } else if (VideoParameter.Group2.Section.VIDEO_2_2_END_SEC < currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_3_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group2.Section.VIDEO_2_3_START_SEC < currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_3_END_SEC - 1) {
                    isDisableUI = false;
                } else if (VideoParameter.Group2.Section.VIDEO_2_3_END_SEC < currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_4_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group2.Section.VIDEO_2_4_START_SEC < currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_4_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group2.Section.VIDEO_2_4_END_SEC < currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_6_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group2.Section.VIDEO_2_6_START_SEC < currentPosition && currentPosition <= VideoParameter.Group2.Section.VIDEO_2_6_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group2.Section.VIDEO_2_6_END_SEC < currentPosition) {
                    isDisableUI = true;
                }

                break;

            case SET_VIDEO_ID_3:
                if (currentPosition <= VideoParameter.Group3.Section.VIDEO_3_1_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group3.Section.VIDEO_3_1_START_SEC < currentPosition && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_1_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group3.Section.VIDEO_3_1_END_SEC < currentPosition && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_4_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group3.Section.VIDEO_3_4_START_SEC < currentPosition && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_4_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group3.Section.VIDEO_3_4_END_SEC < currentPosition && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_5_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group3.Section.VIDEO_3_5_START_SEC < currentPosition && currentPosition <= VideoParameter.Group3.Section.VIDEO_3_5_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group3.Section.VIDEO_3_5_END_SEC < currentPosition) {
                    isDisableUI = true;
                }

                break;

            case SET_VIDEO_ID_4: // 홍두한 1
                if (currentPosition <= VideoParameter.Group4.Section.VIDEO_4_1_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_1_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_1_END_SEC - 1) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_1_END_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_2_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_2_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_2_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_2_END_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_3_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_3_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_3_END_SEC - 1) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_3_END_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_4_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_4_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_4_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_4_END_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_5_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_5_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_5_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_5_END_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_6_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_6_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_6_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_6_END_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_7_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_7_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_7_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_7_END_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_8_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_8_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_8_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_8_END_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_9_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_9_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_9_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_9_END_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_10_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group4.Section.VIDEO_4_10_START_SEC < currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_10_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group4.Section.VIDEO_4_10_END_SEC < currentPosition) {
                    isDisableUI = true;
                }

                break;
            case SET_VIDEO_ID_5: // 홍두한 2
                if (currentPosition <= VideoParameter.Group5.Section.VIDEO_5_1_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_1_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_1_END_SEC - 1) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_1_END_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_2_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_2_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_2_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_2_END_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_3_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_3_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_3_END_SEC - 1) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_3_END_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_4_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_4_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_4_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_4_END_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_5_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_5_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_5_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_5_END_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_6_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_6_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_6_END_SEC + 1) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_6_END_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_7_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_7_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_7_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_7_END_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_8_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_8_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_8_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_8_END_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_9_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_9_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_9_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_9_END_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_10_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_10_START_SEC < currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_10_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group5.Section.VIDEO_5_10_END_SEC < currentPosition) {
                    isDisableUI = true;
                }
                break;

            case SET_VIDEO_ID_6: // 이주영 1
                if (currentPosition <= VideoParameter.Group6.Section.VIDEO_6_1_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_1_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_1_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_1_END_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_2_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_2_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_2_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_2_END_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_3_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_3_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_3_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_3_END_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_4_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_4_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_4_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_4_END_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_5_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_5_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_5_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_5_END_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_6_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_6_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_6_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_6_END_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_7_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_7_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_7_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_7_END_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_8_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_8_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_8_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_8_END_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_9_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_9_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_9_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_9_END_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_10_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group6.Section.VIDEO_6_10_START_SEC < currentPosition && currentPosition <= VideoParameter.Group6.Section.VIDEO_6_10_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group6.Section.VIDEO_6_10_END_SEC < currentPosition) {
                    isDisableUI = true;
                }
                break;

            case SET_VIDEO_ID_7: // 이주영 2
                if (currentPosition <= VideoParameter.Group7.Section.VIDEO_7_1_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_1_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_1_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_1_END_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_2_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_2_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_2_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_2_END_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_3_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_3_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_3_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_3_END_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_4_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_4_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_4_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_4_END_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_5_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_5_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_5_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_5_END_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_6_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_6_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_6_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_6_END_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_7_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_7_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_7_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_7_END_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_8_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_8_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_8_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_8_END_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_9_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_9_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_9_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_9_END_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_10_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_10_START_SEC < currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_10_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group7.Section.VIDEO_7_10_END_SEC < currentPosition) {
                    isDisableUI = true;
                }
                break;

            case SET_VIDEO_ID_9: // 출산부 5~8주차
                if (currentPosition <= VideoParameter.Group9.Section.VIDEO_9_1_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_1_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_1_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_1_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_2_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_2_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_2_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_2_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_3_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_3_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_3_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_3_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_4_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_4_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_4_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_4_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_5_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_5_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_5_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_5_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_6_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_6_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_6_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_6_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_7_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_7_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_7_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_7_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_8_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_8_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_8_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_8_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_9_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_9_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_9_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_9_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_10_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_10_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_10_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_10_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_11_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_11_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_11_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_11_END_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_12_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_12_START_SEC < currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_12_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group9.Section.VIDEO_9_12_END_SEC < currentPosition) {
                    isDisableUI = true;
                }
                break;

            case SET_VIDEO_ID_8: // 출산부 1-4주차
                if (currentPosition <= VideoParameter.Group8.Section.VIDEO_8_1_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_1_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_1_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_1_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_2_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_2_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_2_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_2_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_3_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_3_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_3_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_3_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_4_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_4_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_4_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_4_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_5_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_5_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_5_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_5_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_6_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_6_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_6_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_6_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_7_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_7_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_7_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_7_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_8_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_8_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_8_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_8_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_9_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_9_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_9_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_9_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_10_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_10_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_10_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_10_END_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_11_START_SEC) {
                    isDisableUI = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_11_START_SEC < currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_11_END_SEC) {
                    isDisableUI = false;
                } else if (VideoParameter.Group8.Section.VIDEO_8_11_END_SEC < currentPosition) {
                    isDisableUI = true;
                }
                break;
        }
    }

    private void setTotalScore(int point, int count_percent, int accuracy_percent, String comment) {
        t_point = point;
        t_count_percent = count_percent;
        t_accuracy_percent = accuracy_percent;
        t_comment = comment;
    }

    private void onTotalScore(int videoID, int currentPosition) {
        if (mIView == null)
            return;
        boolean go = false;

        long time = 0;
        int delay = 0;
        switch (videoID) {
            case SET_VIDEO_ID_8: // 출산부 1-4
                switch (currentPosition) {
                    case VideoParameter.Group8.Section.VIDEO_8_1_END_SEC + VideoParameter.Group8.Section.VIDEO_8_1_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_1_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_2_END_SEC + VideoParameter.Group8.Section.VIDEO_8_2_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_2_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_3_END_SEC + VideoParameter.Group8.Section.VIDEO_8_3_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_3_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_4_END_SEC + VideoParameter.Group8.Section.VIDEO_8_4_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_4_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_5_END_SEC + VideoParameter.Group8.Section.VIDEO_8_5_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_5_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_6_END_SEC + VideoParameter.Group8.Section.VIDEO_8_6_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_6_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_7_END_SEC + VideoParameter.Group8.Section.VIDEO_8_7_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_7_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_8_END_SEC + VideoParameter.Group8.Section.VIDEO_8_8_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_8_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_9_END_SEC + VideoParameter.Group8.Section.VIDEO_8_9_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_9_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_10_END_SEC + VideoParameter.Group8.Section.VIDEO_8_10_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_10_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group8.Section.VIDEO_8_11_END_SEC + VideoParameter.Group8.Section.VIDEO_8_11_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group8.Section.VIDEO_8_11_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                }
                break;
            case SET_VIDEO_ID_9: // 출산부 1-4
                switch (currentPosition) {
                    case VideoParameter.Group9.Section.VIDEO_9_1_END_SEC + VideoParameter.Group9.Section.VIDEO_9_1_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_1_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_2_END_SEC + VideoParameter.Group9.Section.VIDEO_9_2_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_2_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_3_END_SEC + VideoParameter.Group9.Section.VIDEO_9_3_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_3_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_4_END_SEC + VideoParameter.Group9.Section.VIDEO_9_4_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_4_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_5_END_SEC + VideoParameter.Group9.Section.VIDEO_9_5_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_5_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_6_END_SEC + VideoParameter.Group9.Section.VIDEO_9_6_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_6_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_7_END_SEC + VideoParameter.Group9.Section.VIDEO_9_7_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_7_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_8_END_SEC + VideoParameter.Group9.Section.VIDEO_9_8_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_8_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_9_END_SEC + VideoParameter.Group9.Section.VIDEO_9_9_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_9_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_10_END_SEC + VideoParameter.Group9.Section.VIDEO_9_10_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_10_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_11_END_SEC + VideoParameter.Group9.Section.VIDEO_9_11_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_11_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                    case VideoParameter.Group9.Section.VIDEO_9_12_END_SEC + VideoParameter.Group9.Section.VIDEO_9_12_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group9.Section.VIDEO_9_12_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                }
                break;
            case SET_VIDEO_ID_3: // 시험운동
                switch (currentPosition) {
                    case VideoParameter.Group3.Section.VIDEO_3_5_END_SEC + VideoParameter.Group3.Section.VIDEO_3_5_TOTAL_SCORE_DELAY:
                        go = true;
                        time = VideoParameter.Group3.Section.VIDEO_3_5_TOTAL_SCORE_DISPLAY_TIME;
                        break;
                }
                break;
            default:
                return;
        }

        if (!go)
            return;

        if (t_comment == null)
            return;

        mIView.onCommentSection(time * 1000, t_point, t_count_percent, t_accuracy_percent, "");

        t_count_percent = t_accuracy_percent = t_point = 0;
        t_comment = null;
    }

    /**
     * 각 영상에 따른 간략 설명 설정.
     *
     * @param videoIndex
     *            현재 재생 중인 영상 idx
     * @return 해당 영상의 설명
     */
    private String setDescription(int videoIndex) {
        switch (videoIndex) {
            case VideoParameter.Group1.Index.V_1_1_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_1_1;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_1_1_J;
                }
            case VideoParameter.Group1.Index.V_1_1:
                return name_v_1_1;
            case VideoParameter.Group1.Index.V_1_8_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_1_8;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_1_8_J;
                }
            case VideoParameter.Group1.Index.V_1_8:
                return name_v_1_8;
            case VideoParameter.Group1.Index.V_1_3_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_1_3;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_1_3_J;
                }
            case VideoParameter.Group1.Index.V_1_3:
                return name_v_1_3;
            case VideoParameter.Group1.Index.V_1_6_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_1_6;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_1_6_J;
                }
            case VideoParameter.Group1.Index.V_1_6:
                return name_v_1_6;
            case VideoParameter.Group1.Index.V_1_16_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_1_16;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_1_16_J;
                }
            case VideoParameter.Group1.Index.V_1_16:
                return name_v_1_16;

            case VideoParameter.Group2.Index.V_2_1_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_2_1;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_2_1_J;
                }
            case VideoParameter.Group2.Index.V_2_1:
                return name_v_2_1;
            case VideoParameter.Group2.Index.V_2_2_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_2_2;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_2_2_J;
                }
            case VideoParameter.Group2.Index.V_2_2:
                return name_v_2_2;
            case VideoParameter.Group2.Index.V_2_3_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_2_3;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_2_3_J;
                }
            case VideoParameter.Group2.Index.V_2_3:
                return name_v_2_3;
            case VideoParameter.Group2.Index.V_2_4_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_2_4;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_2_4_J;
                }
            case VideoParameter.Group2.Index.V_2_4:
                return name_v_2_4;
            case VideoParameter.Group2.Index.V_2_6_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_2_6;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_2_6_J;
                }
            case VideoParameter.Group2.Index.V_2_6:
                return name_v_2_6;

            case VideoParameter.Group3.Index.V_3_1_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_3_1;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_3_1_J;
                }
            case VideoParameter.Group3.Index.V_3_1:
                if (LANGUAGE_SET == 1) {
                    return name_v_3_1;
                } else if (LANGUAGE_SET == 2) {
                    return name_v_3_1_J;
                } else if (LANGUAGE_SET == 3) {
                    return name_v_3_1_EN;
                }
            case VideoParameter.Group3.Index.V_3_4_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_3_4;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_3_4_J;
                }
            case VideoParameter.Group3.Index.V_3_4:
                if (LANGUAGE_SET == 1) {
                    return name_v_3_4;
                } else if (LANGUAGE_SET == 2) {
                    return name_v_3_4_J;
                } else if (LANGUAGE_SET == 3) {
                    return name_v_3_4_EN;
                }
            case VideoParameter.Group3.Index.V_3_5_R:
                if (LANGUAGE_SET == 1) {
                    return description_v_3_5;
                } else if (LANGUAGE_SET == 2) {
                    return description_v_3_5_J;
                }
            case VideoParameter.Group3.Index.V_3_5:
                if (LANGUAGE_SET == 1) {
                    return name_v_3_5;
                } else if (LANGUAGE_SET == 2) {
                    return name_v_3_5_J;
                } else if (LANGUAGE_SET == 3) {
                    return name_v_3_5_EN;
                }

                // 홍코치 ~ 이주영 운동 pr 추가되었으므로 description 없음.
            case VideoParameter.Group4.Index.V_4_1_R:
            case VideoParameter.Group4.Index.V_4_1:
                return name_v_4_1;
            case VideoParameter.Group4.Index.V_4_2_R:
            case VideoParameter.Group4.Index.V_4_2:
                return name_v_4_2;
            case VideoParameter.Group4.Index.V_4_3_R:
            case VideoParameter.Group4.Index.V_4_3:
                return name_v_4_3;
            case VideoParameter.Group4.Index.V_4_4_R:
            case VideoParameter.Group4.Index.V_4_4:
                return name_v_4_4;
            case VideoParameter.Group4.Index.V_4_5_R:
            case VideoParameter.Group4.Index.V_4_5:
                return name_v_4_5;
            case VideoParameter.Group4.Index.V_4_6_R:
            case VideoParameter.Group4.Index.V_4_6:
                return name_v_4_6;
            case VideoParameter.Group4.Index.V_4_7_R:
            case VideoParameter.Group4.Index.V_4_7:
                return name_v_4_7;
            case VideoParameter.Group4.Index.V_4_8_R:
            case VideoParameter.Group4.Index.V_4_8:
                return name_v_4_8;
            case VideoParameter.Group4.Index.V_4_9_R:
            case VideoParameter.Group4.Index.V_4_9:
                return name_v_4_9;
            case VideoParameter.Group4.Index.V_4_10_R:
            case VideoParameter.Group4.Index.V_4_10:
                return name_v_4_10;

            case VideoParameter.Group5.Index.V_5_1_R:
            case VideoParameter.Group5.Index.V_5_1:
                return name_v_5_1;
            case VideoParameter.Group5.Index.V_5_2_R:
            case VideoParameter.Group5.Index.V_5_2:
                return name_v_5_2;
            case VideoParameter.Group5.Index.V_5_3_R:
            case VideoParameter.Group5.Index.V_5_3:
                return name_v_5_3;
            case VideoParameter.Group5.Index.V_5_4_R:
            case VideoParameter.Group5.Index.V_5_4:
                return name_v_5_4;
            case VideoParameter.Group5.Index.V_5_5_R:
            case VideoParameter.Group5.Index.V_5_5:
                return name_v_5_5;
            case VideoParameter.Group5.Index.V_5_6_R:
            case VideoParameter.Group5.Index.V_5_6:
                return name_v_5_6;
            case VideoParameter.Group5.Index.V_5_7_R:
            case VideoParameter.Group5.Index.V_5_7:
                return name_v_5_7;
            case VideoParameter.Group5.Index.V_5_8_R:
            case VideoParameter.Group5.Index.V_5_8:
                return name_v_5_8;
            case VideoParameter.Group5.Index.V_5_9_R:
            case VideoParameter.Group5.Index.V_5_9:
                return name_v_5_9;
            case VideoParameter.Group5.Index.V_5_10_R:
            case VideoParameter.Group5.Index.V_5_10:
                return name_v_5_10;

            case VideoParameter.Group6.Index.V_6_1_R:
            case VideoParameter.Group6.Index.V_6_1:
                return name_v_6_1;
            case VideoParameter.Group6.Index.V_6_2_R:
            case VideoParameter.Group6.Index.V_6_2:
                return name_v_6_2;
            case VideoParameter.Group6.Index.V_6_3_R:
            case VideoParameter.Group6.Index.V_6_3:
                return name_v_6_3;
            case VideoParameter.Group6.Index.V_6_4_R:
            case VideoParameter.Group6.Index.V_6_4:
                return name_v_6_4;
            case VideoParameter.Group6.Index.V_6_5_R:
            case VideoParameter.Group6.Index.V_6_5:
                return name_v_6_5;
            case VideoParameter.Group6.Index.V_6_6_R:
            case VideoParameter.Group6.Index.V_6_6:
                return name_v_6_6;
            case VideoParameter.Group6.Index.V_6_7_R:
            case VideoParameter.Group6.Index.V_6_7:
                return name_v_6_7;
            case VideoParameter.Group6.Index.V_6_8_R:
            case VideoParameter.Group6.Index.V_6_8:
                return name_v_6_8;
            case VideoParameter.Group6.Index.V_6_9_R:
            case VideoParameter.Group6.Index.V_6_9:
                return name_v_6_9;
            case VideoParameter.Group6.Index.V_6_10_R:
            case VideoParameter.Group6.Index.V_6_10:
                return name_v_6_10;

            case VideoParameter.Group7.Index.V_7_1_R:
            case VideoParameter.Group7.Index.V_7_1:
                return name_v_7_1;
            case VideoParameter.Group7.Index.V_7_2_R:
            case VideoParameter.Group7.Index.V_7_2:
                return name_v_7_2;
            case VideoParameter.Group7.Index.V_7_3_R:
            case VideoParameter.Group7.Index.V_7_3:
                return name_v_7_3;
            case VideoParameter.Group7.Index.V_7_4_R:
            case VideoParameter.Group7.Index.V_7_4:
                return name_v_7_4;
            case VideoParameter.Group7.Index.V_7_5_R:
            case VideoParameter.Group7.Index.V_7_5:
                return name_v_7_5;
            case VideoParameter.Group7.Index.V_7_6_R:
            case VideoParameter.Group7.Index.V_7_6:
                return name_v_7_6;
            case VideoParameter.Group7.Index.V_7_7_R:
            case VideoParameter.Group7.Index.V_7_7:
                return name_v_7_7;
            case VideoParameter.Group7.Index.V_7_8_R:
            case VideoParameter.Group7.Index.V_7_8:
                return name_v_7_8;
            case VideoParameter.Group7.Index.V_7_9_R:
            case VideoParameter.Group7.Index.V_7_9:
                return name_v_7_9;
            case VideoParameter.Group7.Index.V_7_10_R:
            case VideoParameter.Group7.Index.V_7_10:
                return name_v_7_10;

            case VideoParameter.Group8.Index.V_8_1_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_1:
                return name_v_8_1;
            case VideoParameter.Group8.Index.V_8_2_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_2:
                return name_v_8_2;
            case VideoParameter.Group8.Index.V_8_3_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_3:
                return name_v_8_3;
            case VideoParameter.Group8.Index.V_8_4_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_4:
                return name_v_8_4;
            case VideoParameter.Group8.Index.V_8_5_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_5:
                return name_v_8_5;
            case VideoParameter.Group8.Index.V_8_6_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_6:
                return name_v_8_6;
            case VideoParameter.Group8.Index.V_8_7_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_7:
                return name_v_8_7;
            case VideoParameter.Group8.Index.V_8_8_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_8:
                return name_v_8_8;
            case VideoParameter.Group8.Index.V_8_9_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_9:
                return name_v_8_9;
            case VideoParameter.Group8.Index.V_8_10_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_10:
                return name_v_8_10;
            case VideoParameter.Group8.Index.V_8_11_R: // 출산부 1-4주차
            case VideoParameter.Group8.Index.V_8_11:
                return name_v_8_11;

            case VideoParameter.Group9.Index.V_9_1_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_1:
                return name_v_9_1;
            case VideoParameter.Group9.Index.V_9_2_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_2:
                return name_v_9_2;
            case VideoParameter.Group9.Index.V_9_3_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_3:
                return name_v_9_3;
            case VideoParameter.Group9.Index.V_9_4_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_4:
                return name_v_9_4;
            case VideoParameter.Group9.Index.V_9_5_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_5:
                return name_v_9_5;
            case VideoParameter.Group9.Index.V_9_6_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_6:
                return name_v_9_6;
            case VideoParameter.Group9.Index.V_9_7_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_7:
                return name_v_9_7;
            case VideoParameter.Group9.Index.V_9_8_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_8:
                return name_v_9_8;
            case VideoParameter.Group9.Index.V_9_9_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_9:
                return name_v_9_9;
            case VideoParameter.Group9.Index.V_9_10_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_10:
                return name_v_9_10;
            case VideoParameter.Group9.Index.V_9_11_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_11:
                return name_v_9_11;
            case VideoParameter.Group9.Index.V_9_12_R: // 출산부 5~8주차
            case VideoParameter.Group9.Index.V_9_12:
                return name_v_9_12;
            default:
                return null;
        }
    }

    private void getOutput_G1(final int videoNumber, KIST_AART_output pOut, int avgHR) {

        idx_count_dup++;
        double accuracy = 0;
        switch (videoNumber) {
            case VideoParameter.Group1.Index.V_1_1: // Rolling Like a Ball
                if (pOut.peak_x == 2 && pOut.amplitude_x <= VideoParameter.Group1.G1_1.AMP_THR) {
                    accuracy = (pOut.amplitude_x / VideoParameter.Group1.G1_1.AMP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_1_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_F[((int) accuracy) % 4]);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_1_1_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_1_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_1_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_1_1_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }

                break;

            case VideoParameter.Group1.Index.V_1_8: // Open Leg Rocker
                if (pOut.peak_x == 2 && pOut.amplitude_x <= VideoParameter.Group1.G1_8.AMP_THR) {
                    accuracy = (pOut.amplitude_x / VideoParameter.Group1.G1_8.AMP_REF) * 100;
                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_1_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_F[((int) accuracy) % 4]);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_1_8_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_1_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_1_8_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_1_8_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }

                break;

            case VideoParameter.Group1.Index.V_1_3: // Double Leg Pull
                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group1.G1_3.AMP_THR) {
                    accuracy = ((pOut.amplitude_y / VideoParameter.Group1.G1_3.AMP_REF) * 10) * ((pOut.peak_to_peak_y / VideoParameter.Group1.G1_3.PTP_REF) * 10);

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_1_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_F[((int) accuracy) % 4]);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_1_3_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_1_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_1_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_1_3_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }

                break;
            case VideoParameter.Group1.Index.V_1_6: // Criss Cross

                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group1.G1_6.AMP_THR && (double) pOut.peak_to_peak_y >= VideoParameter.Group1.G1_6.PTP_THR) {

                    if (pOut.peak_y >= VideoParameter.Group1.G1_6.PEAK_REF) {
                        accuracy = (10) * (((double) pOut.peak_to_peak_y / VideoParameter.Group1.G1_6.PTP_REF) * 10);
                    } else if (pOut.peak_y < VideoParameter.Group1.G1_6.PEAK_REF) {
                        accuracy = ((pOut.peak_y / VideoParameter.Group1.G1_6.PEAK_REF) * 10) * (((double) pOut.peak_to_peak_y / VideoParameter.Group1.G1_6.PTP_REF) * 10);
                    }
                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_1_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_F[((int) accuracy) % 4]);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_1_6_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_1_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_1_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_1_6_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }

                break;

            case VideoParameter.Group1.Index.V_1_16: // Swimmings
                if (pOut.peak_norm == 2 && pOut.amplitude_norm <= VideoParameter.Group1.G1_16.AMP_THR && pOut.peak_to_peak_norm < VideoParameter.Group1.G1_16.PTP_THR) {
                    accuracy = Math.abs(pOut.amplitude_norm / VideoParameter.Group1.G1_16.AMP_REF) * 100;
                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_1_16);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_F[((int) accuracy) % 4]);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_1_16_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_1_16);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_1_16_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_1_16_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;
            default:
                break;
        }

        idx_count++;
        if (idx_count > THR_NOT_MOVE) {
            idx_count = 0;
            int hr_percent = getHeartRateCompared(avgHR);
            if (hr_percent <= 20) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_MOVE);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_MOVE_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_MOVE_EN);
                }
            } else if (hr_percent >= 60) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_REST);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_REST_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_REST_EN);
                }
            }
        }
    }

    private void getOutput_G2(final int videoNumber, KIST_AART_output pOut, int avgHR) {

        idx_count_dup++;
        double accuracy = 0;
        switch (videoNumber) {
            case VideoParameter.Group2.Index.V_2_1:
                if (pOut.peak_z == 1 && pOut.amplitude_z >= VideoParameter.Group2.G2_1.AMP_THR) {
                    Count_V_2_1++;
                    SumAccuracy_V_2_1 += pOut.amplitude_z;

                    idx_count = 0;
                }
                if (Count_V_2_1 == 4) {
                    accuracy = (SumAccuracy_V_2_1 / (4 * VideoParameter.Group2.G2_1.AMP_REF)) * 100;
                    Count_V_2_1 = 0;
                    SumAccuracy_V_2_1 = 0;
                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_2_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_2_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_2_1_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_2_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_2_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_2_1_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group2.Index.V_2_2:
                if (pOut.peak_norm == 1 && pOut.peak_to_peak_norm >= VideoParameter.Group2.G2_2.PTP_THR && pOut.cross_peak_to_peak_norm > VideoParameter.Group2.G2_2.CROSS_PTP_THR
                        && pOut.amplitude_norm > VideoParameter.Group2.G2_2.AMP_THR) {

                    accuracy = (pOut.surface_norm / VideoParameter.Group2.G2_2.SURFACE_REF) * 100;
                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_2_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_2_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_2_2_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_2_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_2_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_2_2_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group2.Index.V_2_3:
                if (pOut.peak_x == 1 && pOut.surface_x >= VideoParameter.Group2.G2_3.SURFACE_THR && pOut.peak_to_peak_x >= VideoParameter.Group2.G2_3.PTP_THR) {
                    accuracy = (pOut.surface_x / VideoParameter.Group2.G2_3.SURFACE_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_2_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_2_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_2_3_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_2_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_2_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_2_3_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group2.Index.V_2_4:
                if (pOut.peak_norm == 2 && pOut.amplitude_norm <= VideoParameter.Group2.G2_4.AMP_THR) {
                    Count_V_2_4++;
                    SumAccuracy_V_2_4 += pOut.amplitude_norm;

                    idx_count = 0;
                }
                if (Count_V_2_4 == 2) {
                    accuracy = (SumAccuracy_V_2_4 / (2 * VideoParameter.Group2.G2_4.AMP_REF)) * 100;
                    Count_V_2_4 = 0;
                    SumAccuracy_V_2_4 = 0;
                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_2_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_2_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_2_4_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_2_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_2_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_2_4_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;
            case VideoParameter.Group2.Index.V_2_6:
                if (pOut.peak_y == 2 && pOut.peak_value_y < VideoParameter.Group2.G2_6.PEAK_MIN && pOut.amplitude_y <= VideoParameter.Group2.G2_6.AMP_THR
                        && pOut.peak_to_peak_y <= VideoParameter.Group2.G2_6.PTP_THR) {
                    accuracy = (pOut.peak_value_y / VideoParameter.Group2.G2_6.PEAK_REF) * 100;

                    if (accuracy >= 1 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }
                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_2_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_2_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_2_6_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_2_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_2_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_2_6_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            default:
                break;
        }

        idx_count++;
        if (idx_count > THR_NOT_MOVE) {
            idx_count = 0;
            int hr_percent = getHeartRateCompared(avgHR);
            if (hr_percent <= 20) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_MOVE);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_MOVE_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_MOVE_EN);
                }
            } else if (hr_percent >= 60) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_REST);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_REST_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_REST_EN);
                }
            }
        }
    }

    private void getOutput_G3(final int videoNumber, KIST_AART_output pOut, int avgHR) {
        //Log.i(tag,"액션G3!!!!");
        idx_count_dup++;
        double accuracy = 0;
        switch (videoNumber) {
            case VideoParameter.Group3.Index.V_3_1:
                if (pOut.peak_y == 1 && pOut.amplitude_y > VideoParameter.Group3.G3_1.AMP_THR && (double) pOut.cross_peak_to_peak_y >= VideoParameter.Group3.G3_1.CPTP_THR) {
                    Count_V_3_1++;
                    SumAccuracy_V_3_1 += pOut.amplitude_y;
                    Log.i("운동1", "peak_y: "+pOut.peak_y+ " == 1"+" amplitude_y:"+pOut.amplitude_y+ " > 3.0f");
                    Log.i("운동1", "cross_peak_to_peak_y: "+pOut.cross_peak_to_peak_y+ " >= 5.0f");
                    idx_count = 0;
                }

                if (Count_V_3_1 == 2) {
                    accuracy = (SumAccuracy_V_3_1 / (2 * VideoParameter.Group3.G3_1.AMP_REF)) * 100;
                    Log.i("운동1", "accuracy: "+accuracy + " = (amplitude_y_before + now / (2 * 9.0)) * 100");
                    Count_V_3_1 = 0;
                    SumAccuracy_V_3_1 = 0;

                    if (accuracy >= 80) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        Log.i("운동1","인지 성공 횟수: "+out[0]);
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 80) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {

                                mIView.onInstruction(comment_PASS );
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_F[((int) accuracy) % 4]);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_G[((int) accuracy) % 4]);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_BAD);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_3_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_3_1_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;
            case VideoParameter.Group3.Index.V_3_4:
                if (pOut.peak_norm == 1 && pOut.surface_norm > VideoParameter.Group3.G3_4.SURFACE_THR) {
                    Count_V_3_4++;
                    SumAccuracy_V_3_4 += pOut.surface_norm;
                    Log.i("운동2", "peak_norm: "+pOut.peak_norm+ " == 1 " + " surface_norm: "+pOut.surface_norm+ " >= 10.0f");
                    //Log.i("운동2", "SumAccuracy_V_3_4= "+SumAccuracy_V_3_4);
                }

                if (Count_V_3_4 == 2) {
                    accuracy = (SumAccuracy_V_3_4 / (2 * VideoParameter.Group3.G3_4.SURFACE_REF)) * 100;
                    //Log.i(tag, "SumAccuracy_V_3_4= "+SumAccuracy_V_3_4+" /(2 * VideoParameter.Group3.G3_4.SURFACE_REF)) * 100"+((2 * VideoParameter.Group3.G3_4.SURFACE_REF) * 100));
                    //Log.i("운동2", "SumAccuracy_V_3_4= "+SumAccuracy_V_3_4+ " accuracy= "+accuracy);
                    Log.i("운동2", "accuracy: "+accuracy + " = (surface_norm_before + now / (2 * 20.1883)) * 100");
                    Count_V_3_4 = 0;
                    SumAccuracy_V_3_4 = 0;

                    if (accuracy >= 80) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        Log.i("운동2","인지 성공 횟수: "+out[0]);
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 50) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_PASS );
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_F[((int) accuracy) % 4]);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_G[((int) accuracy) % 4]);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_BAD);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_v3_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_3_4_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;
            case VideoParameter.Group3.Index.V_3_5: // 다리들어 올려 걷기
                if (pOut.peak_x == 2 && pOut.amplitude_x <= VideoParameter.Group3.G3_5.AMP_THR && pOut.peak_value_x <= VideoParameter.Group3.G3_5.PEAK_MIN) {
                    Log.i("운동3", "peak_x: "+pOut.peak_x+ " == 2 "+ " amplitude_x: "+pOut.amplitude_x+ " <= -6.0f");
                    Log.i("운동3", "peak_value_x: "+pOut.peak_value_x+ " <= 3.5f");
                    Count_V_3_5++;
                    SumAccuracy_V_3_5 += pOut.amplitude_x;
                }
                if (Count_V_3_5 == 2) {
                    accuracy = (SumAccuracy_V_3_5 / (2 * VideoParameter.Group3.G3_5.AMP_REF)) * 100;
                    //Log.i(tag, "SumAccuracy_V_3_5:"+SumAccuracy_V_3_5+" (2 * VideoParameter.Group3.G3_5.AMP_REF)) * 100"+((2 * VideoParameter.Group3.G3_5.AMP_REF)) * 100);
                    //Log.i("운동3", "SumAccuracy_V_3_5= "+SumAccuracy_V_3_5+ " accuracy= "+accuracy);
                    Log.i("운동3", "accuracy: "+accuracy + " = (amplitude_x_before + now / (2 * -10.5)) * 100");
                    Count_V_3_5 = 0;
                    SumAccuracy_V_3_5 = 0;

                    if (accuracy >= 80) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        Log.i("운동3","인지 성공 횟수: "+out[0]);
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 50) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_PASS );
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_F[((int) accuracy) % 4]);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_G[((int) accuracy) % 4]);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_BAD);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_v3_5_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_3_5_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            default:
                break;
        }

        idx_count++;
        if (idx_count > THR_NOT_MOVE)

        {
            idx_count = 0;
            int hr_percent = getHeartRateCompared(avgHR);
            if (hr_percent <= 20) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_MOVE);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_MOVE_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_MOVE_EN);
                }
            } else if (hr_percent >= 60) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_REST);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_REST_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_REST_EN);
                }
            }
        }
    }

    private void getOutput_G4(final int videoNumber, KIST_AART_output pOut, int avgHR) {

        idx_count_dup++;
        double accuracy = 0;
        switch (videoNumber) {
            case VideoParameter.Group4.Index.V_4_1:
                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group4.G4_1.AMP_THR && pOut.cross_peak_to_peak_y <= VideoParameter.Group4.G4_1.CPTP_THR) {

                    Count_V_4_1++;
                    SumAccuracy_V_4_1 += pOut.amplitude_y;

                    idx_count = 0;
                }
                if (Count_V_4_1 == 2) {
                    accuracy = (SumAccuracy_V_4_1 / VideoParameter.Group4.G4_1.AMP_REF) * 100;
                    Count_V_4_1 = 0;
                    SumAccuracy_V_4_1 = 0;

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_1_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_1_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;
            case VideoParameter.Group4.Index.V_4_2:
			/*
			 * if (pOut.peak_norm == 1 && pOut.amplitude_norm >=
			 * VideoParameter.Group4.G4_2.AMP_THR && pOut.peak_value_norm >=
			 * VideoParameter.Group4.G4_2.PEAK_MAX && pOut.surface_norm >=
			 * VideoParameter.Group4.G4_2.SURFACE_THR) {
			 */

                if (pOut.peak_norm == 2 && pOut.amplitude_norm <= VideoParameter.Group4.G4_2.AMP_THR && VideoParameter.Group4.G4_2.PTP_MAX < pOut.peak_to_peak_norm
                        && pOut.peak_to_peak_norm < VideoParameter.Group4.G4_2.PTP_THR && VideoParameter.Group4.G4_2.PEAK_MIN < pOut.peak_value_norm
                        && pOut.peak_value_norm < VideoParameter.Group4.G4_2.PEAK_MAX) {

                    accuracy = (pOut.amplitude_norm / VideoParameter.Group4.G4_2.AMP_REF) * 100;

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_2_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_2_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group4.Index.V_4_3:
                if (pOut.peak_y == 1 && pOut.peak_value_y >= VideoParameter.Group4.G4_3.PEAK_MAX && ((double) pOut.peak_to_peak_y) >= VideoParameter.Group4.G4_3.PTP_THR) {

                    Count_V_4_3++;
                    SumAccuracy_V_4_3 += pOut.peak_value_y;
                    SumAccuracy_V_4_3_1 += ((double) pOut.peak_to_peak_y);
                    idx_count = 0;
                }
                if (Count_V_4_3 == 2) {
                    accuracy = ((SumAccuracy_V_4_3 / (2 * VideoParameter.Group4.G4_3.PEAK_REF)) * 5) * ((SumAccuracy_V_4_3_1 / (2 * VideoParameter.Group4.G4_3.PTP_REF)) * 20);
                    Count_V_4_3 = 0;
                    SumAccuracy_V_4_3 = 0;
                    SumAccuracy_V_4_3_1 = 0;

                    if (accuracy >= 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_3_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_3_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group4.Index.V_4_4:
                if (pOut.peak_x == 2 && pOut.peak_value_x <= VideoParameter.Group4.G4_4.PEAK_MIN && pOut.amplitude_x <= VideoParameter.Group4.G4_4.AMP_THR) {
                    accuracy = ((double) pOut.cross_peak_to_peak_x / VideoParameter.Group4.G4_4.CROSS_PTP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_4_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_4_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group4.Index.V_4_5:
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group4.G4_5.AMP_THR) {
                    accuracy = ((pOut.amplitude_y / VideoParameter.Group4.G4_5.AMP_REF) * 10) * ((pOut.peak_to_peak_y / VideoParameter.Group4.G4_5.PTP_REF) * 10);

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_5);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_5_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_5_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_5);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_5_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_5_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group4.Index.V_4_6:
                if (pOut.peak_norm == 1 && pOut.amplitude_norm >= VideoParameter.Group4.G4_6.AMP_THR && pOut.peak_value_norm >= VideoParameter.Group4.G4_6.PEAK_MAX) {
                    accuracy = (pOut.amplitude_norm / VideoParameter.Group4.G4_6.AMP_REF) * 100;

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_6_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_6_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group4.Index.V_4_7:
                if ((pOut.peak_x == 2 || pOut.peak_z == 1) && (pOut.amplitude_x - pOut.amplitude_z) <= VideoParameter.Group4.G4_7.AMP_THR) {
                    accuracy = ((pOut.amplitude_x - pOut.amplitude_z) / VideoParameter.Group4.G4_7.AMP_REF) * 100;
                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_7);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_7_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_7_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_7);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_7_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_7_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group4.Index.V_4_8:
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group4.G4_8.AMP_THR) {

                    Count_V_4_8++;
                    SumAccuracy_V_4_8 += pOut.amplitude_y;
                    SumAccuracy_V_4_8_1 += pOut.cross_peak_to_peak_y;
                    idx_count = 0;
                }
                if (Count_V_4_8 == 2) {
                    accuracy = ((SumAccuracy_V_4_8 / (2 * VideoParameter.Group4.G4_8.AMP_REF)) * 5) * ((SumAccuracy_V_4_8_1 / (2 * VideoParameter.Group4.G4_8.CPTP_REF)) * 20);
                    Count_V_4_8 = 0;
                    SumAccuracy_V_4_8 = 0;
                    SumAccuracy_V_4_8_1 = 0;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_8_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_8_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_8_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_8_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group4.Index.V_4_9:
                if (pOut.peak_norm == 2 && pOut.amplitude_norm <= VideoParameter.Group4.G4_9.AMP_THR && pOut.peak_value_norm <= VideoParameter.Group4.G4_9.PEAK_MIN) {
                    accuracy = (pOut.amplitude_norm / VideoParameter.Group4.G4_9.AMP_REF) * 100;
				/*
				 * if (pOut.peak_x == 1 && pOut.amplitude_x >=
				 * VideoParameter.Group4.G4_9.AMP_THR && pOut.surface_x >
				 * VideoParameter.Group4.G4_9.SURFACE_THR) { accuracy =
				 * (pOut.surface_x / VideoParameter.Group4.G4_9.SURFACE_REF) *
				 * 100;
				 */

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_9);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_9_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_9_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_9);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_9_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_9_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group4.Index.V_4_10:

                if (pOut.peak_z == 1 && pOut.amplitude_z >= VideoParameter.Group4.G4_10.AMP_THR && pOut.surface_z > VideoParameter.Group4.G4_10.SURFACE_THR) {

                    Count_V_4_10++;
                    SumAccuracy_V_4_10 += pOut.surface_z;
                    idx_count = 0;
                }
                if (Count_V_4_10 == 2) {

                    accuracy = (SumAccuracy_V_4_10 / (2 * VideoParameter.Group4.G4_10.SURFACE_REF)) * 100;// or
                    // amp
                    Count_V_4_10 = 0;
                    SumAccuracy_V_4_10 = 0;
                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_4_10);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_4_10_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_4_10_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_4_10);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_4_10_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_4_10_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;
            default:
                break;
        }

        idx_count++;
        if (idx_count > THR_NOT_MOVE) {
            idx_count = 0;
            int hr_percent = getHeartRateCompared(avgHR);
            if (hr_percent <= 20) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_MOVE);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_MOVE_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_MOVE_EN);
                }
            } else if (hr_percent >= 60) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_REST);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_REST_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_REST_EN);
                }
            }
        }
    }

    private void getOutput_G5(final int videoNumber, KIST_AART_output pOut, int avgHR) {

        idx_count_dup++;
        double accuracy = 0;
        int index = 0;
        boolean SET_1 = false, SET_2 = false;
        boolean POS_1 = false, POS_2 = false;
        switch (videoNumber) {
            case VideoParameter.Group5.Index.V_5_1: // jumping jacks
                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group5.G5_1.AMP_THR && (double) pOut.cross_peak_to_peak_y <= VideoParameter.Group5.G5_1.CPTP_THR) {

                    Count_V_5_1++;
                    SumAccuracy_V_5_1 += pOut.amplitude_y;

                    idx_count = 0;
                }
                if (Count_V_5_1 == 2) {
                    accuracy = (SumAccuracy_V_5_1 / VideoParameter.Group5.G5_1.AMP_REF) * 100;
                    Count_V_5_1 = 0;
                    SumAccuracy_V_5_1 = 0;

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_5_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_5_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_5_1_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_5_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_5_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_5_1_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group5.Index.V_5_2:
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group5.G5_2.AMP_THR && pOut.peak_value_y >= VideoParameter.Group5.G5_2.PEAK_MAX) {
                    accuracy = ((pOut.peak_value_y / VideoParameter.Group5.G5_2.PEAK_MAX_REF) * 10) * (((double) pOut.cross_peak_to_peak_y / VideoParameter.Group5.G5_2.CPTP_REF) * 10);

				/*
				 * if (pOut.amplitude_y > VideoParameter.Group5.G5_1.AMP_REF *
				 * 1.3) { // mIView.onInstruction(comment_v5_2); }
				 */

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_5_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_5_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_5_2_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_5_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_5_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_5_2_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group5.Index.V_5_3:
                if (pOut.peak_y == 2 && pOut.peak_value_y <= VideoParameter.Group5.G5_3.PEAK_MIN && pOut.amplitude_y <= VideoParameter.Group5.G5_3.AMP_THR) {
                    accuracy = ((pOut.peak_value_y / VideoParameter.Group5.G5_3.PEAK_MIN_REF) * 10) * (((double) pOut.cross_peak_to_peak_y / VideoParameter.Group5.G5_3.CROSS_PTP_REF) * 10);

				/*
				 * 0) if (pOut.surface_x <
				 * VideoParameter.Group5.G5_3.SURFACE_REF * 0.7) { //
				 * mIView.onInstruction(comment_v5_3); }
				 */
                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_5_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_5_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_5_3_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_5_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_5_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_5_3_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group5.Index.V_5_4:
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group5.G5_4.AMP_THR) {
                    accuracy = (pOut.amplitude_y / VideoParameter.Group5.G5_4.AMP_REF) * 100;

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_5_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_5_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_5_4_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_5_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_5_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_5_4_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group5.Index.V_5_5:
                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group5.G5_5.AMP_THR && pOut.peak_value_y <= VideoParameter.Group5.G5_5.PEAK_MIN) {

                    accuracy = ((double) pOut.peak_to_peak_y / VideoParameter.Group5.G5_5.PTP_REF) * 100;

                    if (pOut.peak_to_peak_y <= -45) {
                        accuracy = 59;
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        if (LANGUAGE_SET == 1) {
                            mIView.onInstruction(comment_SOSO_5_5);
                        } else if (LANGUAGE_SET == 2) {
                            mIView.onInstruction(comment_SOSO_5_5_J); // 일본어 번역 추가
                        } else if (LANGUAGE_SET == 3) {
                            mIView.onInstruction(comment_SOSO_5_5_EN);
                        }

                    } else if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;

                        if (accuracy >= 80) {
                            grade = 2;
                        } else {
                            grade = 1;
                        }
                        if (grade == old_grade) {
                            grade_chk++;
                            if (grade_chk == 2) {
                                grade_chk = 0;
                            }
                        } else {
                            grade_chk = 0;
                        }
                        if (out[0] == 1 || grade_chk == 0) {
                            if (grade == 2) {
                                if (LANGUAGE_SET == 1) {
                                    mIView.onInstruction(comment_GREAT_5_5);
                                } else if (LANGUAGE_SET == 2) {
                                    mIView.onInstruction(comment_GREAT_5_5_J);
                                } else if (LANGUAGE_SET == 3) {
                                    mIView.onInstruction(comment_GREAT_5_5_EN);
                                }
                            } else if (grade == 1) {
                                if (LANGUAGE_SET == 1) {
                                    mIView.onInstruction(comment_GOOD_5_5);
                                } else if (LANGUAGE_SET == 2) {
                                    mIView.onInstruction(comment_GOOD_5_5_J);
                                } else if (LANGUAGE_SET == 3) {
                                    mIView.onInstruction(comment_GOOD_5_5_EN);
                                }
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group5.Index.V_5_6: // T push up
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group5.G5_6.AMP_THR && pOut.peak_value_y >= VideoParameter.Group5.G5_6.PEAK_MAX) {

                    accuracy = ((pOut.amplitude_y / VideoParameter.Group5.G5_6.AMP_REF) * 10) * (((double) pOut.cross_peak_to_peak_y / VideoParameter.Group5.G5_6.CPTP_REF) * 10);

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_5_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_5_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_5_6_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_5_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_5_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_5_6_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group5.Index.V_5_7: // Russian twist
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group5.G5_7.AMP_THR && pOut.peak_value_y >= VideoParameter.Group5.G5_7.PEAK_MAX) {

                    accuracy = ((pOut.peak_value_y / VideoParameter.Group5.G5_7.PEAK_MAX_REF) * 10) * (((double) pOut.peak_to_peak_y / VideoParameter.Group5.G5_7.PTP_REF) * 10);

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_5_7);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_5_7_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_5_7_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_5_7);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_5_7_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_5_7_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group5.Index.V_5_8:
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group5.G5_8.AMP_THR && pOut.peak_value_y >= VideoParameter.Group5.G5_8.PEAK_MAX) {

                    accuracy = (((double) pOut.cross_peak_to_peak_y / VideoParameter.Group5.G5_8.CPTP_REF) * 10) * ((pOut.peak_value_y / VideoParameter.Group5.G5_8.PEAK_MAX_REF) * 10);

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_5_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_5_8_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_5_8_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_5_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_5_8_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_5_8_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group5.Index.V_5_9:
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group5.G5_9.AMP_THR && pOut.peak_to_peak_y >= VideoParameter.Group5.G5_9.PTP_THR) {

                    accuracy = ((pOut.peak_value_y / VideoParameter.Group5.G5_9.PEAK_MAX) * 10) * (((double) pOut.cross_peak_to_peak_y / VideoParameter.Group5.G5_9.CPTP_REF) * 10);

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_5_9);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_5_9_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_5_9_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_5_9);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_5_9_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_5_9_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group5.Index.V_5_10:
                if (currentPosition < VideoParameter.Group5.Section.VIDEO_5_10_EXTRA_SEPERATE_TIME) {
                    if (pOut.peak_z == 1 && pOut.peak_value_z >= VideoParameter.Group5.G5_10.PEAK_MAX_1 && pOut.amplitude_z >= VideoParameter.Group5.G5_10.AMP_THR_1
                            && pOut.peak_to_peak_z >= VideoParameter.Group5.G5_10.PTP_THR) {
                        accuracy = (pOut.amplitude_z / VideoParameter.Group5.G5_10.AMP_REF_1) * 100;
                        SET_1 = true;
                    }
                    POS_1 = true;
                } else { // 두번째 동작.
                    if (pOut.peak_y == 1 && pOut.peak_value_y >= VideoParameter.Group5.G5_10.PEAK_MAX_2 && pOut.amplitude_y >= VideoParameter.Group5.G5_10.AMP_THR_2) {
                        accuracy = ((pOut.amplitude_y / VideoParameter.Group5.G5_10.AMP_REF_2) * 10) * ((pOut.peak_to_peak_y / VideoParameter.Group5.G5_10.PTP_REF) * 10);
                        SET_2 = true;
                    }
                    POS_2 = true;
                }
                if (SET_1 || SET_2) {
                    if (accuracy >= 1) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_5_10);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_5_10_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_5_10_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_5_10);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_5_10_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_5_10_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;
            default:
                break;
        }

        idx_count++;
        if (idx_count > THR_NOT_MOVE)

        {
            idx_count = 0;
            int hr_percent = getHeartRateCompared(avgHR);
            if (hr_percent <= 20) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_MOVE);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_MOVE_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_MOVE_EN);
                }
            } else if (hr_percent >= 60) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_REST);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_REST_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_REST_EN);
                }
            }
        }

    }

    private void getOutput_G6(final int videoNumber, KIST_AART_output pOut, int avgHR) {

        idx_count_dup++;
        double accuracy = 0;
        switch (videoNumber) {
            case VideoParameter.Group6.Index.V_6_1:
                if (pOut.peak_y == 2 && pOut.peak_value_y <= VideoParameter.Group6.G6_1.PEAK_MIN) {
                    accuracy = (pOut.peak_value_y / VideoParameter.Group6.G6_1.AMP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_1_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_1_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group6.Index.V_6_2:

                if ((pOut.acc_var_z + pOut.acc_var_x <= 100) && (pOut.acc_diff_y < 0) && (pOut.acc_smooth_z + pOut.acc_smooth_x > 6)) {
                    var_chk_zy = (pre_var_chk_zy + 1);
                } else if ((pOut.acc_smooth_z + pOut.acc_smooth_x <= 0) && (pre_acc_smooth_z + pOut.acc_smooth_x > 0)) {
                    var_chk_zy = 0;
                } else {
                    var_chk_zy = pre_var_chk_zy;
                }

                if ((var_chk_zy == 0) && (pre_var_chk_zy > 0)) {
                    stay_time_zy = pre_var_chk_zy;
                }

                pre_var_chk_zy = var_chk_zy;
                pre_acc_smooth_z = pOut.acc_smooth_z + pOut.acc_smooth_x;

                if (((pOut.peak_z == 2) || (pOut.peak_x == 2)) && (pOut.amplitude_z + pOut.amplitude_x <= VideoParameter.Group6.G6_2.AMP_THR)
                        && (pOut.peak_value_z + pOut.peak_value_x <= VideoParameter.Group6.G6_2.PEAK_MIN)) {
                    accuracy = (stay_time_zy / 25) * 100;

                    if (accuracy >= 30) {
                        out[0]++;
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_2_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_2_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group6.Index.V_6_3:
                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group6.G6_3.AMP_THR && pOut.cross_peak_to_peak_y <= VideoParameter.Group6.G6_3.CPTP_THR) {
                    accuracy = ((pOut.amplitude_y / VideoParameter.Group6.G6_3.AMP_REF) * 10) * ((pOut.peak_to_peak_y / VideoParameter.Group6.G6_3.PTP_REF) * 10);

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_3_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_3_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group6.Index.V_6_4:
                if (pOut.peak_y == 2 && pOut.peak_value_y <= VideoParameter.Group6.G6_4.PEAK_MIN && pOut.cross_peak_to_peak_y <= VideoParameter.Group6.G6_4.CPTP_THR) {
                    accuracy = (pOut.peak_value_y / VideoParameter.Group6.G6_4.PEAK_MIN_REF) * 100;

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_4_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_4_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group6.Index.V_6_5: // 힐터치

                if ((pOut.peak_x == 2 || pOut.peak_z == 1) && (pOut.amplitude_x - pOut.amplitude_z) <= VideoParameter.Group6.G6_5.AMP_THR) {
                    accuracy = ((pOut.amplitude_x - pOut.amplitude_z) / VideoParameter.Group6.G6_5.AMP_REF) * 100;// (((double)pOut.peak_to_peak_x
                    // /
                    // VideoParameter.Group6.G6_5.PTP_REF)*10);

                    if (accuracy >= 1 && idx_count_dup - duplicate_idx_count > 30) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_5);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_5_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_5_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_5);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_5_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_5_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group6.Index.V_6_6:

                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group6.G6_6.AMP_THR && (double) pOut.peak_to_peak_y >= VideoParameter.Group6.G6_6.PTP_THR) {

                    if (pOut.peak_y >= VideoParameter.Group6.G6_6.PEAK_REF) {
                        accuracy = (10) * (((double) pOut.peak_to_peak_y / VideoParameter.Group6.G6_6.PTP_REF) * 10);
                    } else if (pOut.peak_y < VideoParameter.Group6.G6_6.PEAK_REF) {
                        accuracy = ((pOut.peak_y / VideoParameter.Group6.G6_6.PEAK_REF) * 10) * (((double) pOut.peak_to_peak_y / VideoParameter.Group6.G6_6.PTP_REF) * 10);
                    }

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_6_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_6_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group6.Index.V_6_7:

                if (pOut.peak_x == 1 && pOut.peak_value_x >= VideoParameter.Group6.G6_7.PEAK_MAX && (double) pOut.peak_to_peak_x >= VideoParameter.Group6.G6_7.PTP_THR) {
                    accuracy = ((pOut.peak_value_x / VideoParameter.Group6.G6_7.PEAK_MAX_REF) * 10) * (((double) pOut.peak_to_peak_x / VideoParameter.Group6.G6_7.PTP_REF) * 10);

				/*
				 * if (pOut.peak_y == 1 && pOut.peak_to_peak_y >=
				 * VideoParameter.Group6.G6_7.PTP_THR && pOut.peak_value_y >=
				 * VideoParameter.Group6.G6_7.PEAK_MAX) {
				 *
				 * accuracy = (pOut.peak_to_peak_y /
				 * VideoParameter.Group6.G6_7.PTP_REF) * 100;
				 */

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 40) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_7);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_7_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_7_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_7);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_7_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_7_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group6.Index.V_6_8:
                if (pOut.peak_y == 2 && pOut.peak_value_y <= VideoParameter.Group6.G6_8.PEAK_MIN && pOut.amplitude_y <= VideoParameter.Group6.G6_8.AMP_THR) {

                    accuracy = ((pOut.amplitude_y / VideoParameter.Group6.G6_8.AMP_REF) * 5) * ((pOut.peak_to_peak_y / VideoParameter.Group6.G6_8.PTP_REF) * 20);

				/*
				 * if (pOut.peak_y == 1 && pOut.amplitude_y >
				 * VideoParameter.Group6.G6_8.AMP_THR && pOut.surface_y >
				 * VideoParameter.Group6.G6_8.SURFACE_THR) { accuracy =
				 * (pOut.amplitude_y / VideoParameter.Group6.G6_8.AMP_REF) *
				 * 100;
				 */

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_8_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_8_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_8_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_8_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group6.Index.V_6_9: // 정확도 다시 한번 확인 해봐야할듯..
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group6.G6_9.AMP_THR && (double) pOut.cross_peak_to_peak_y >= VideoParameter.Group6.G6_9.CPTP_THR
                        && pOut.peak_value_y >= VideoParameter.Group6.G6_9.PEAK_MAX) {
                    accuracy = ((double) pOut.cross_peak_to_peak_y / VideoParameter.Group6.G6_9.CPTP_REF) * 100;

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_9);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_9_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_9_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_9);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_9_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_9_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group6.Index.V_6_10:
                if (pOut.peak_y == 1 && pOut.peak_to_peak_y >= VideoParameter.Group6.G6_10.PTP_THR && pOut.peak_value_y >= VideoParameter.Group6.G6_10.PEAK_MAX) {

                    accuracy = (pOut.peak_to_peak_y / VideoParameter.Group6.G6_10.PTP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_6_10);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_6_10_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_6_10_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_6_10);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_6_10_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_6_10_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            default:
                break;
        }

        idx_count++;
        if (idx_count > THR_NOT_MOVE) {
            idx_count = 0;
            int hr_percent = getHeartRateCompared(avgHR);
            if (hr_percent <= 20) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_MOVE);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_MOVE_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_MOVE_EN);
                }
            } else if (hr_percent >= 60) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_REST);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_REST_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_REST_EN);
                }
            }
        }
    }

    private void getOutput_G7(final int videoNumber, KIST_AART_output pOut, int avgHR) {

        idx_count_dup++;
        double accuracy = 0;
        switch (videoNumber) {
            case VideoParameter.Group7.Index.V_7_1:
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group7.G7_1.AMP_THR) {

                    accuracy = ((pOut.amplitude_y / VideoParameter.Group7.G7_1.AMP_REF) * 10) * ((pOut.peak_to_peak_y / VideoParameter.Group7.G7_1.PTP_REF) * 10);
                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_1_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_1);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_1_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_1_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group7.Index.V_7_2:
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group7.G7_2.AMP_THR && pOut.peak_value_y >= VideoParameter.Group7.G7_2.PEAK_MAX) {

                    accuracy = ((pOut.peak_value_y / VideoParameter.Group7.G7_2.PEAK_MAX_REF) * 10) * (((double) pOut.cross_peak_to_peak_y / VideoParameter.Group7.G7_2.CPTP_REF) * 10);

                    if (accuracy >= 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_2_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_2);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_2_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_2_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group7.Index.V_7_3:
                if (pOut.peak_y == 2 && pOut.peak_value_y <= VideoParameter.Group7.G7_3.PEAK_MIN && pOut.cross_peak_to_peak_y <= VideoParameter.Group7.G7_3.CPTP_THR) {

                    accuracy = (pOut.cross_peak_to_peak_y / VideoParameter.Group7.G7_3.CPTP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_3_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_3);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_3_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_3_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group7.Index.V_7_4:
                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group7.G7_4.AMP_THR && pOut.peak_value_y >= VideoParameter.Group7.G7_4.PEAK_MAX) {

                    accuracy = (pOut.surface_y / VideoParameter.Group7.G7_4.SURFACE_REF) * 100;

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 20) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_4_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_4);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_4_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_4_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group7.Index.V_7_5:

                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group7.G7_5.AMP_THR && pOut.peak_value_y <= VideoParameter.Group7.G7_5.PEAK_MIN) {

                    accuracy = (pOut.peak_to_peak_y / VideoParameter.Group7.G7_5.PTP_REF) * 100;

				/*
				 * if (pOut.peak_y == 2 && pOut.amplitude_y <=
				 * VideoParameter.Group7.G7_5.AMP_THR && pOut.peak_value_y <=
				 * VideoParameter.Group7.G7_5.PEAK_MIN) { accuracy =
				 * ((pOut.amplitude_y /
				 * VideoParameter.Group7.G7_5.AMP_REF)*10)*((pOut.
				 * cross_peak_to_peak_y / VideoParameter.Group7.G7_5.CPTP_REF));
				 */

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_5);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_5_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_5_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_5);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_5_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_5_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group7.Index.V_7_6:
                if (pOut.peak_z == 1 && pOut.surface_z >= VideoParameter.Group7.G7_6.SURFACE_THR && pOut.amplitude_z >= VideoParameter.Group7.G7_6.AMP_THR) {
                    accuracy = (pOut.surface_z / VideoParameter.Group7.G7_6.SURFACE_REF) * 100;

				/*
				 * if (pOut.peak_y == 2 && pOut.peak_value_y <=
				 * VideoParameter.Group7.G7_6.AMP_THR &&
				 * (double)pOut.peak_to_peak_y <=
				 * VideoParameter.Group7.G7_6.SURFACE_THR) {
				 *
				 * accuracy = ((pOut.peak_value_y /
				 * VideoParameter.Group7.G7_6.PEAK_MAX) * 5) *
				 * (((double)pOut.peak_to_peak_y /
				 * VideoParameter.Group7.G7_6.PTP_REF) * 20);
				 */

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_6_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_6);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_6_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_6_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group7.Index.V_7_7:
                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group7.G7_7.AMP_THR && pOut.peak_value_y <= VideoParameter.Group7.G7_7.PEAK_MIN) {

                    accuracy = (pOut.amplitude_y / VideoParameter.Group7.G7_7.AMP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_7);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_7_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_7_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_7);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_7_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_7_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group7.Index.V_7_8:
                if (pOut.peak_y == 1 && pOut.peak_value_y >= VideoParameter.Group7.G7_8.PEAK_MAX) {

                    accuracy = ((pOut.peak_value_y / VideoParameter.Group7.G7_8.PEAK_MAX_REF) * 10) * (((double) pOut.peak_to_peak_y / VideoParameter.Group7.G7_8.PTP_REF) * 10);

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_8_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_8_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_8);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_8_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_8_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group7.Index.V_7_9:
                if (pOut.peak_y == 1 && pOut.peak_value_y >= VideoParameter.Group7.G7_9.PEAK_MAX) {

                    accuracy = ((pOut.peak_value_y / VideoParameter.Group7.G7_9.PEAK_MAX_REF) * 5) * ((pOut.cross_peak_to_peak_y / VideoParameter.Group7.G7_9.CPTP_REF) * 20);

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_9);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_9_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_9_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_9);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_9_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_9_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group7.Index.V_7_10:
                if (pOut.peak_y == 2 && pOut.amplitude_y <= VideoParameter.Group7.G7_10.AMP_THR && pOut.peak_to_peak_y <= VideoParameter.Group7.G7_10.PTP_THR) {

                    accuracy = (pOut.peak_to_peak_y / VideoParameter.Group7.G7_10.PTP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (grade == 2) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GREAT_7_10);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GREAT_7_10_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GREAT_7_10_EN);
                            }
                        } else if (grade == 1) {
                            if (LANGUAGE_SET == 1) {
                                mIView.onInstruction(comment_GOOD_7_10);
                            } else if (LANGUAGE_SET == 2) {
                                mIView.onInstruction(comment_GOOD_7_10_J);
                            } else if (LANGUAGE_SET == 3) {
                                mIView.onInstruction(comment_GOOD_7_10_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            default:
                break;
        }

        idx_count++;
        if (idx_count > THR_NOT_MOVE) {
            idx_count = 0;
            int hr_percent = getHeartRateCompared(avgHR);
            if (hr_percent <= 20) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_MOVE);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_MOVE_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_MOVE_EN);
                }
            } else if (hr_percent >= 60) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_REST);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_REST_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_REST_EN);
                }
            }
        }
    }

    private void getOutput_G8(int videoNumber, KIST_AART_output pOut, int avgHR) {
        idx_count_dup++;
        double accuracy = 0;
        int index = 0;
        boolean SET_1 = false, SET_2 = false;
        boolean POS_1 = false, POS_2 = false;
        switch (videoNumber) {
            case VideoParameter.Group8.Index.V_8_1: // 1-1
                if (currentPosition < VideoParameter.Group8.Section.VIDEO_8_1_EXTRA_SEPERATE_TIME) {
                    if ((pOut.peak_y == 1) && (pOut.peak_value_y > VideoParameter.Group8.G8_1.PEAK_VALUE_THR_1)) {
                        count++;
                        if (count == 2) {
                            count = 0;
                        }
                        if (count == 0) {
                            accuracy = pOut.surface_y / 14 * 100;

                            SET_1 = true;
                        }
                    }
                    POS_1 = true;
                } else {
                    // 두번째 동작.
                    if ((pOut.peak_y == 1) && (pOut.peak_to_peak_y > VideoParameter.Group8.G8_1.PTP_THR_2) && (pOut.amplitude_y > VideoParameter.Group8.G8_1.AMP_THR_2)) {
                        if (pOut.peak_value_y < -1) {
                            accuracy = Math.abs((-3.5 - pOut.peak_value_y) / -2.5) * 100;
                        } else if (pOut.peak_value_y >= -2) {
                            accuracy = 100;
                        }

                        SET_2 = true;
                    }
                    POS_2 = true;
                }

                if (SET_1 || SET_2) {
                    if (accuracy >= 30) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_1);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_1);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_1_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_1_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_1_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_1_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group8.Index.V_8_2: // 1-2
                if (currentPosition < VideoParameter.Group8.Section.VIDEO_8_2_EXTRA_SEPERATE_TIME) {
                    if ((pOut.peak_y == 2) && (pOut.amplitude_y < VideoParameter.Group8.G8_2.AMP_THR_1) && (pOut.peak_value_y < VideoParameter.Group8.G8_2.PEAK_MAX_1)) {
                        accuracy = pOut.surface_y / VideoParameter.Group8.G8_2.SURFACE_REF_1 * 100.0D;

                        SET_1 = true;
                    }
                    POS_1 = true;
                } else {
                    if ((pOut.peak_y == 2) && (pOut.peak_value_y < VideoParameter.Group8.G8_2.PEAK_MIN_2) && (pOut.amplitude_y < VideoParameter.Group8.G8_2.AMP_THR_2)) {
                        accuracy = pOut.surface_y / VideoParameter.Group8.G8_2.SURFACE_REF_2 * 100.0D;

                        SET_2 = true;
                    }
                    POS_2 = true;
                }

                if ((SET_1) || (SET_2)) {
                    if (accuracy >= 30) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_2);
                            } else if (grade == 1) {
                                if (SET_1) {
                                    mIView.onInstruction(comment_GOOD_8_2_1);
                                } else {
                                    mIView.onInstruction(comment_GOOD_8_2_2);
                                }
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_2_J);
                            } else if (grade == 1) {
                                if (SET_1) {
                                    mIView.onInstruction(comment_GOOD_8_2_1_J);
                                } else {
                                    mIView.onInstruction(comment_GOOD_8_2_2_J);
                                }
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_2_EN);
                            } else if (grade == 1) {
                                if (SET_1) {
                                    mIView.onInstruction(comment_GOOD_8_2_1_EN);
                                } else {
                                    mIView.onInstruction(comment_GOOD_8_2_2_EN);
                                }
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group8.Index.V_8_3: // 1-3
                if (pOut.acc_smooth_z > 9) {
                    value_chk_z = pre_value_chk_z + 0.9;
                    value_chk_z2 = pre_value_chk_z2 + 1;
                } else if (pOut.acc_smooth_z > 8) {
                    value_chk_z = pre_value_chk_z + 0.8;
                    value_chk_z2 = pre_value_chk_z2 + 1;
                } else if (pOut.acc_smooth_z > 7) {
                    value_chk_z = pre_value_chk_z + 0.7;
                    value_chk_z2 = pre_value_chk_z2 + 1;
                } else if (pOut.acc_smooth_z > 6) {
                    value_chk_z = pre_value_chk_z + 0.6;
                    value_chk_z2 = pre_value_chk_z2 + 1;
                } else if ((pOut.acc_smooth_z > 0) && (pre_acc_smooth_z < 0)) {
                    value_chk_z = 0;
                    value_chk_z2 = 0;
                } else {
                    value_chk_z = pre_value_chk_z;
                    value_chk_z2 = pre_value_chk_z2;
                }

                if ((value_chk_z == 0) && (pre_value_chk_z > 0)) {
                    up_stay_time_z = pre_value_chk_z;
                    up_stay_time_z2 = pre_value_chk_z2;
                }
                pre_value_chk_z = value_chk_z;
                pre_value_chk_z2 = value_chk_z;
                pre_acc_smooth_z = pOut.acc_smooth_z;

                if (((pOut.peak_z == 2) && (pOut.peak_value_z <= VideoParameter.Group8.G8_3.PEAK_MAX) && (pOut.amplitude_z <= VideoParameter.Group8.G8_3.AMP_THR))
                        || ((pOut.peak_x == 2) && (pOut.peak_value_x <= VideoParameter.Group8.G8_3.PEAK_MAX) && (pOut.amplitude_x <= VideoParameter.Group8.G8_3.AMP_THR))) {
                    accuracy = (up_stay_time_z * up_stay_time_z2) / 170 * 100;

                    if (accuracy >= 40 && idx_count_dup - duplicate_idx_count > 20) {
                        out[0]++;
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 50) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_3);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_8_3);
                            } else {
                                mIView.onInstruction(comment_BAD_8_3);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_3_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_8_3_J);
                            } else {
                                mIView.onInstruction(comment_BAD_8_3_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_3_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_8_3_EN);
                            } else {
                                mIView.onInstruction(comment_BAD_8_3_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }

                break;
            case VideoParameter.Group8.Index.V_8_4: // 1-4
                if (((pOut.peak_z == 2) && (pOut.peak_value_z <= VideoParameter.Group8.G8_4.PEAK_MAX) && (pOut.amplitude_z <= VideoParameter.Group8.G8_4.AMP_THR))
                        || ((pOut.peak_x == 2) && (pOut.peak_value_x <= VideoParameter.Group8.G8_4.PEAK_MAX) && (pOut.amplitude_x <= VideoParameter.Group8.G8_4.AMP_THR))) {
                    count++;
                    if (count == 2) {
                        count = 0;
                    }
                    if (count == 0) {
                        accuracy = (Math.sqrt(x_buffer * x_buffer + z_buffer * z_buffer) + Math.sqrt(pOut.amplitude_x * pOut.amplitude_x + pOut.amplitude_z * pOut.amplitude_z)) / 20 * 100;
                    }

                    if (accuracy >= 50 && count == 0 && idx_count_dup - duplicate_idx_count > 20) {
                        out[0]++;
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 50) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || (grade_chk == 0 && count == 0)) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_4);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_8_4);
                            } else {
                                mIView.onInstruction(comment_BAD_8_4);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_4_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_8_4_J);
                            } else {
                                mIView.onInstruction(comment_BAD_8_4_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_4_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_8_4_EN);
                            } else {
                                mIView.onInstruction(comment_BAD_8_4_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group8.Index.V_8_5: // 1-5
                if ((pOut.acc_var_z <= 50) && (pOut.acc_diff_y < 0) && (pOut.acc_smooth_z > 3)) {
                    var_chk_zy = (pre_var_chk_zy + 1);
                } else if ((pOut.acc_smooth_z <= 0) && (pre_acc_smooth_z > 0)) {
                    var_chk_zy = 0;
                } else {
                    var_chk_zy = pre_var_chk_zy;
                }

                if ((var_chk_zy == 0) && (pre_var_chk_zy > 0)) {
                    stay_time_zy = pre_var_chk_zy;
                }

                pre_var_chk_zy = var_chk_zy;
                pre_acc_smooth_z = pOut.acc_smooth_z;

                if ((pOut.peak_z == 2) && (pOut.amplitude_z <= VideoParameter.Group8.G8_5.AMP_THR) && (pOut.peak_value_z <= VideoParameter.Group8.G8_5.PEAK_MIN)) {
                    accuracy = (stay_time_zy / 25) * 100;

                    if (accuracy >= 30) {
                        out[0]++;
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }

                    if (grade == old_grade) {
                        grade_chk += 1;
                        if (grade_chk == 2)
                            grade_chk = 0;
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || (grade_chk == 0 && count == 0)) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_5);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_5);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_5_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_5_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_5_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_5_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group8.Index.V_8_6: // 1-6
                if ((pOut.peak_y == 1) && (pOut.amplitude_y > VideoParameter.Group8.G8_6.AMP_THR) && (pOut.peak_value_y > VideoParameter.Group8.G8_6.PEAK_MAX)) {
                    if (pOut.peak_value_y >= -2)
                        accuracy = 100;
                    else {
                        accuracy = ((-5 - pOut.peak_value_y) / (-5 - (-2))) * 100;
                    }

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_6);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_6);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_6_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_6_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_6_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_6_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group8.Index.V_8_7: // 1-7
                if ((Math.sqrt(pOut.acc_smooth_x * pOut.acc_smooth_x + pOut.acc_smooth_z * pOut.acc_smooth_z) > 6) && (pOut.acc_smooth_y > 4)) {
                    turn_chk_xz = (pre_turn_chk_xz + 1);
                } else if (pOut.acc_smooth_y < 0) {
                    turn_chk_xz = 0;
                } else {
                    turn_chk_xz = pre_turn_chk_xz;
                }

                if ((turn_chk_xz == 0) && (pre_turn_chk_xz > 0))
                    turn_time = pre_turn_chk_xz;
                else {
                    turn_time = pre_turn_time;
                }
                pre_turn_time = turn_time;
                pre_turn_chk_xz = turn_chk_xz;

                if (pOut.peak_y == 1 && pOut.amplitude_y >= VideoParameter.Group8.G8_7.AMP_THR) {
                    ampbuffer = pOut.amplitude_y;
                }
                if ((pOut.acc_smooth_y <= 0) && (buffer_y > 0)) {
                    Log.d(tag, "V_8_7 ::" + String.format("ampbuffer:%f turn_time:%f", ampbuffer, turn_time));
                    accuracy = ((turn_time / 40) * (ampbuffer == 0 ? 5 : ampbuffer / 20)) * 100;

                    if (accuracy >= 10) {
                        out[0]++;
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_7);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_7);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_7_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_7_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_7_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_7_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }

                buffer_y = pOut.acc_smooth_y;

                break;

            case VideoParameter.Group8.Index.V_8_8: // 1-8
                if ((pOut.peak_y == 1) && (pOut.peak_value_y > VideoParameter.Group8.G8_8.PEAK_MIN) && (pOut.peak_to_peak_y > VideoParameter.Group8.G8_8.PTP_THR)) {
                    accuracy = (pOut.surface_y / VideoParameter.Group8.G8_8.SURFACE_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_8);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_8);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_8_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_8_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_8_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_8_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group8.Index.V_8_9: // 1-9
                if ((pOut.peak_z == 1) && (pOut.peak_to_peak_z > VideoParameter.Group8.G8_9.PTP_THR) && (pOut.amplitude_z > VideoParameter.Group8.G8_9.AMP_THR)) {
                    accuracy = (pOut.peak_value_z / VideoParameter.Group8.G8_9.PEAK_MAX_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_9);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_9);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_9_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_9_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_9_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_9_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group8.Index.V_8_10: // 1-10
                if (pOut.peak_y == 1 && pOut.peak_value_y > VideoParameter.Group8.G8_10.PEAK_MAX && pOut.amplitude_y > VideoParameter.Group8.G8_10.AMP_THR) {
                    accuracy = ((pOut.peak_to_peak_y / VideoParameter.Group8.G8_10.PTP_REF) * 10) * ((pOut.amplitude_y / VideoParameter.Group8.G8_10.AMP_REF) * 10);

                    if (accuracy >= 50) {
                        out[0]++;
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_10);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_10);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_10_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_10_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_10_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_10_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;
            case VideoParameter.Group8.Index.V_8_11: // 1-11
                if (((pOut.peak_z == 2) && (pOut.amplitude_z <= VideoParameter.Group8.G8_11.AMP_THR) && (pOut.peak_value_z <= VideoParameter.Group8.G8_11.PEAK_MIN))
                        || ((pOut.peak_x == 2) && (pOut.amplitude_x <= VideoParameter.Group8.G8_11.AMP_THR) && (pOut.peak_value_x <= VideoParameter.Group8.G8_11.PEAK_MIN))) {
                    Log.d(tag, "V_8_11 ::" + String.format("pOut.acc_smooth_x:%f pOut.acc_smooth_z:%f", pOut.acc_smooth_x, pOut.acc_smooth_z));

                    accuracy = Math.sqrt((pOut.acc_smooth_x * pOut.acc_smooth_x + pOut.acc_smooth_z * pOut.acc_smooth_z)) / 13 * 100;

                    if (accuracy >= 30 && count == 0 && idx_count_dup - duplicate_idx_count > 20) {
                        out[0]++;
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_11);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_11);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_11_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_11_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_8_11_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_8_11_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;
            default:
                break;
        }

        idx_count++;
        if (idx_count > THR_NOT_MOVE)

        {
            idx_count = 0;
            int hr_percent = getHeartRateCompared(avgHR);
            if (hr_percent <= 20) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_MOVE);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_MOVE_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_MOVE_EN);
                }
            } else if (hr_percent >= 60) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_REST);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_REST_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_REST_EN);
                }
            }
        }

    }

    private void getOutput_G9(int videoNumber, KIST_AART_output pOut, int avgHR) {
        idx_count_dup++;
        double accuracy = 0;
        int index = 0;
        switch (videoNumber) {

            case VideoParameter.Group9.Index.V_9_1: // 2-1
                if (pOut.peak_y == 1 && pOut.peak_value_y > VideoParameter.Group9.G9_1.PEAK_MIN && pOut.peak_to_peak_y > VideoParameter.Group9.G9_1.PTP_THR) {
                    accuracy = Math.abs(pOut.surface_y / VideoParameter.Group9.G9_1.SURFACE_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 50) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_1);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_1);
                            } else {
                                mIView.onInstruction(comment_BAD_9_1);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_1_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_1_J);
                            } else {
                                mIView.onInstruction(comment_BAD_9_1_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_1_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_1_EN);
                            } else {
                                mIView.onInstruction(comment_BAD_9_1_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_2: // 2-2
                if (pOut.peak_y == 1 && pOut.surface_y > VideoParameter.Group9.G9_2.SURFACE_THR) {

                    count++;
                    if (count == 2) {
                        count = 0;
                    }
                    if (count == 0) {
                        // 정확도 계산 (올라가는 가속도 10에 가까울수록 정확도 높아짐.)
                        if (pOut.amplitude_y >= VideoParameter.Group9.G9_2.AMP_REF) {
                            accuracy = 100;
                        } else if (pOut.amplitude_y < VideoParameter.Group9.G9_2.AMP_REF) {
                            accuracy = Math.abs((pOut.amplitude_y) / (VideoParameter.Group9.G9_2.AMP_REF)) * 100;
                        }

                        if (accuracy >= 50 && count == 0) {
                            accuracyQueue.insert((int) accuracy);
                            out[1] = (int) accuracy;
                            out[0]++;
                        }
                    }
                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 50) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade && count == 0) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || (grade_chk == 0 && count == 0)) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_2);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_2);
                            } else {
                                mIView.onInstruction(comment_BAD_9_2);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_2_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_2_J);
                            } else {
                                mIView.onInstruction(comment_BAD_9_2_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_2_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_2_EN);
                            } else {
                                mIView.onInstruction(comment_BAD_9_2_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_3: // 2-3
                if (pOut.peak_y == 1 && pOut.peak_value_y > VideoParameter.Group9.G9_3.PEAK_MAX && pOut.amplitude_y > VideoParameter.Group9.G9_3.AMP_THR) {
                    accuracy = (pOut.surface_y / VideoParameter.Group9.G9_3.SURFACE_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }

                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GRAET_9_3);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_3);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GRAET_9_3_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_3_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GRAET_9_3_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_3_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_4: // 2-4
                if (pOut.peak_y == 2 && pOut.peak_value_y < VideoParameter.Group9.G9_4.PEAK_MIN && pOut.amplitude_y < VideoParameter.Group9.G9_4.AMP_THR) {
                    accuracy = (pOut.amplitude_y / VideoParameter.Group9.G9_4.AMP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_4);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_4);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_4_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_4_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_4_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_4_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_5: // 2-5
                if (pOut.peak_y == 2 && pOut.peak_value_y < VideoParameter.Group9.G9_5.PEAK_MIN && pOut.amplitude_y < VideoParameter.Group9.G9_5.AMP_THR
                        && pOut.peak_to_peak_y < VideoParameter.Group9.G9_5.PTP_THR) {
                    accuracy = (pOut.amplitude_y / VideoParameter.Group9.G9_5.AMP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_5);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_5);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_5_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_5_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_5_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_5_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_6: // 2-7
                if (pOut.peak_y == 2 && pOut.peak_value_y < VideoParameter.Group9.G9_6.PEAK_MIN && pOut.amplitude_y < VideoParameter.Group9.G9_6.AMP_THR
                        && pOut.peak_to_peak_y < VideoParameter.Group9.G9_6.PTP_THR) {
                    accuracy = (pOut.peak_value_y / VideoParameter.Group9.G9_6.PEAK_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 50) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_6);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_6);
                            } else {
                                mIView.onInstruction(comment_BAD_9_6);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_6_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_6_J);
                            } else {
                                mIView.onInstruction(comment_BAD_9_6_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_6_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_6_EN);
                            } else {
                                mIView.onInstruction(comment_BAD_9_6_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_7: // 2-8
                if (pOut.acc_smooth_y > VideoParameter.Group9.G9_7.PEAK_MIN2) {
                    var_chk_y = pre_var_chk_y + 1;
                } else {
                    var_chk_y = 0;
                }
                if (var_chk_y == 0 && pre_var_chk_y > 0) {
                    stay_time_y = pre_var_chk_y;
                }
                pre_var_chk_y = var_chk_y;

                Log.d(tag, "V_9_7 1:: " + String.format("pOut.peak_y:%d pOut.peak_value_y:%f pOut.peak_to_peak_y:%d", stay_time_y, pOut.peak_value_y, pOut.peak_to_peak_y));

                if (pOut.peak_y == 2 && pOut.peak_value_y < VideoParameter.Group9.G9_7.PEAK_MIN && pOut.peak_to_peak_y < -30) {
                    accuracy = ((double) stay_time_y / 50) * 100;

                    Log.d(tag, "V_9_7 2:: " + String.format("stay_time_y:%d idx_count_dup:%d duplicate_idx_count:%d", stay_time_y, idx_count_dup, duplicate_idx_count));

                    if (accuracy >= 20 && idx_count_dup - duplicate_idx_count > 20) {
                        out[0]++;
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_7);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_7);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_7_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_7_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_7_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_7_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_8: // 2-9
                Log.d(tag,
                        "V_9_8 1:: " + String.format("pOut.peak_x:%d pOut.peak_value_x:%f pOut.amplitude_x:%f pOut.peak_value_z:%f", pOut.peak_x, pOut.peak_value_x, pOut.amplitude_x, pOut.peak_value_z));
                if (pOut.peak_x == 2 && pOut.peak_value_x < VideoParameter.Group9.G9_8.PEAK_MIN && pOut.amplitude_x < VideoParameter.Group9.G9_8.AMP_THR
                        || pOut.peak_value_z > VideoParameter.Group9.G9_8.PEAK_MIN_Z) {
                    accuracy = (Math.sqrt((pOut.acc_smooth_x * pOut.acc_smooth_x) + (pOut.acc_smooth_z * pOut.acc_smooth_z)) / VideoParameter.Group9.G9_8.AMP_REF) * 100;

                    Log.d(tag, "V_9_8 2:: " + String.format("pOut.acc_smooth_x:%f pOut.acc_smooth_z:%f acc:%f", pOut.acc_smooth_x, pOut.acc_smooth_z, accuracy));

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 40) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 90) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }

                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_8);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_8);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_8_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_8_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_8_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_8_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_9: // 2-10
                if (pOut.peak_y == 1 && pOut.peak_value_y > VideoParameter.Group9.G9_9.PEAK_MAX && pOut.amplitude_y > VideoParameter.Group9.G9_9.AMP_THR) {
                    accuracy = (pOut.amplitude_y / VideoParameter.Group9.G9_9.AMP_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_9);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_9);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_9_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_9_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_9_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_9_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_10: // 2-11
                Log.d(tag,
                        "V_9_9 1:: " + String.format("pOut.peak_x:%d pOut.peak_value_x:%f pOut.amplitude_x:%f pOut.peak_value_z:%f", pOut.peak_x, pOut.peak_value_x, pOut.amplitude_x, pOut.peak_value_z));
                if (pOut.peak_x == 2 && pOut.peak_value_x < VideoParameter.Group9.G9_8.PEAK_MIN && pOut.amplitude_x < VideoParameter.Group9.G9_8.AMP_THR
                        || pOut.peak_value_z > VideoParameter.Group9.G9_8.PEAK_MIN_Z) {
                    accuracy = (Math.sqrt((pOut.acc_smooth_x * pOut.acc_smooth_x) + (pOut.acc_smooth_z * pOut.acc_smooth_z)) / VideoParameter.Group9.G9_8.AMP_REF) * 100;

                    Log.d(tag, "V_9_9 2:: " + String.format("pOut.acc_smooth_x:%f pOut.acc_smooth_z:%f acc:%f", pOut.acc_smooth_x, pOut.acc_smooth_z, accuracy));

                    if (accuracy >= 50 && idx_count_dup - duplicate_idx_count > 60) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                        duplicate_idx_count = idx_count_dup;
                    }

                    if (accuracy >= 90) {
                        grade = 2;
                    } else {
                        grade = 1;
                    }

                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_10);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_10);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_10_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_10_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_10_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_10_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_11: // 2-12
                if (pOut.peak_y == 1 && pOut.amplitude_y > VideoParameter.Group9.G9_11.AMP_THR && pOut.peak_to_peak_y > VideoParameter.Group9.G9_11.PTP_THR) {
                    count++;
                    if (count == 2) {
                        count = 0;
                    }
                    if (count == 0) {
                        accuracy = (pOut.amplitude_y / VideoParameter.Group9.G9_11.AMP_REF) * 100;

                        if (accuracy >= 50 && count == 0) {
                            accuracyQueue.insert((int) accuracy);
                            out[1] = (int) accuracy;
                            out[0]++;
                        }
                    }
                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 50) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || (grade_chk == 0 && count == 0)) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_11);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_11);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_11_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_11_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_11_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_BAD_9_11_EN);
                            }
                        }
                    }
                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            case VideoParameter.Group9.Index.V_9_12: // 2-13
                if (pOut.peak_x == 1 && pOut.surface_x > VideoParameter.Group9.G9_12.SURFACE_THR && pOut.peak_value_x > VideoParameter.Group9.G9_12.PEAK_MAX) {
                    accuracy = (pOut.surface_x / VideoParameter.Group9.G9_12.SURFACE_REF) * 100;

                    if (accuracy >= 50) {
                        accuracyQueue.insert((int) accuracy);
                        out[1] = (int) accuracy;
                        out[0]++;
                    }

                    if (accuracy >= 80) {
                        grade = 2;
                    } else if (accuracy > 50) {
                        grade = 1;
                    } else {
                        grade = 0;
                    }
                    if (grade == old_grade) {
                        grade_chk++;
                        if (grade_chk == 2) {
                            grade_chk = 0;
                        }
                    } else {
                        grade_chk = 0;
                    }
                    if (out[0] == 1 || grade_chk == 0) {
                        if (LANGUAGE_SET == 1) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_12);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_12);
                            } else {
                                mIView.onInstruction(comment_BAD_9_12);
                            }
                        } else if (LANGUAGE_SET == 2) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_12_J);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_12_J);
                            } else {
                                mIView.onInstruction(comment_BAD_9_12_J);
                            }
                        } else if (LANGUAGE_SET == 3) {
                            if (grade == 2) {
                                mIView.onInstruction(comment_GREAT_9_12_EN);
                            } else if (grade == 1) {
                                mIView.onInstruction(comment_GOOD_9_12_EN);
                            } else {
                                mIView.onInstruction(comment_BAD_9_12_EN);
                            }
                        }
                    }

                    old_grade = grade;

                    idx_count = 0;
                }
                break;

            default:
                break;
        }

        idx_count++;
        if (idx_count > THR_NOT_MOVE) {
            idx_count = 0;
            int hr_percent = getHeartRateCompared(avgHR);
            if (hr_percent <= 20) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_MOVE);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_MOVE_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_MOVE_EN);
                }
            } else if (hr_percent >= 60) {
                if (LANGUAGE_SET == 1) {
                    mIView.onInstruction(comment_REST);
                } else if (LANGUAGE_SET == 2) {
                    mIView.onInstruction(comment_REST_J);
                } else if (LANGUAGE_SET == 3) {
                    mIView.onInstruction(comment_REST_EN);
                }
            }
        }
    }

    private void actionOuput(final int videoNumber, KIST_AART_output pOut, int avgHR) {
        if (videoNumber < 100) {
            getOutput_G1(videoNumber, pOut, avgHR);
        } else if (100 < videoNumber && videoNumber < 200) {
            getOutput_G2(videoNumber, pOut, avgHR);
        } else if (200 < videoNumber && videoNumber < 300) {
            getOutput_G3(videoNumber, pOut, avgHR);
        } else if (300 < videoNumber && videoNumber < 400) {
            getOutput_G4(videoNumber, pOut, avgHR);
        } else if (400 < videoNumber && videoNumber < 500) {
            getOutput_G5(videoNumber, pOut, avgHR);
        } else if (500 < videoNumber && videoNumber < 600) {
            getOutput_G6(videoNumber, pOut, avgHR);
        } else if (600 < videoNumber && videoNumber < 700) {
            getOutput_G7(videoNumber, pOut, avgHR);
        }
        // 이하 출산부 운동.
        else if (700 < videoNumber && videoNumber < 800) {
            getOutput_G8(videoNumber, pOut, avgHR); // 출산부 1-4
        } else if (800 < videoNumber && videoNumber < 900) {
            getOutput_G9(videoNumber, pOut, avgHR); // 출산부 5~8주차
        }
    }

    /**
     * 영상 중, 쓸데없는 count를 방지하여 순수 운동만 coach하기 위함.
     *
     * @param videoID
     *            videoID 영상 idx. (1~7)
     * @param video_index
     *            현재 재생 중인 각 운동에 대한 idx.
     * @param currentPosition
     *            현 영상의 재생 위치.(sec)
     * @return skip이 필요한 구간인 경우, skip index 반환.
     */
    private int onSkip(int videoID, int video_index, int currentPosition) {
        boolean set = false;
        switch (videoID) {
            case SET_VIDEO_ID_1:
                break;
            case SET_VIDEO_ID_2:
                break;
            case SET_VIDEO_ID_3: // 15분
                break;
            case SET_VIDEO_ID_4: // 홍1
                if (VideoParameter.Group4.Section.VIDEO_4_3_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group4.Section.VIDEO_4_3_SKIP_END_SEC) {
                    set = true;
                }
                break;
            case SET_VIDEO_ID_5: // 홍2
                if (VideoParameter.Group5.Section.VIDEO_5_5_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_5_SKIP_END_SEC) {
                    set = true;
                } else if (VideoParameter.Group5.Section.VIDEO_5_10_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group5.Section.VIDEO_5_10_SKIP_END_SEC) {
                    set = true;
                }
                break;
            case SET_VIDEO_ID_6: // 이1
                break;
            case SET_VIDEO_ID_7: // 이2
                if (VideoParameter.Group7.Section.VIDEO_7_8_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_8_SKIP_END_SEC) {
                    set = true;
                } else if (VideoParameter.Group7.Section.VIDEO_7_9_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group7.Section.VIDEO_7_9_SKIP_END_SEC) {
                    set = true;
                }
                break;
            // 이하 출산부
            case SET_VIDEO_ID_8:
                if (VideoParameter.Group8.Section.VIDEO_8_1_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_1_SKIP_END_SEC) {
                    set = true;
                } else if (VideoParameter.Group8.Section.VIDEO_8_2_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group8.Section.VIDEO_8_2_SKIP_END_SEC) {
                    set = true;
                }
                break;
            case SET_VIDEO_ID_9:
                if (VideoParameter.Group9.Section.VIDEO_9_4_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_4_SKIP_END_SEC) {
                    set = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_5_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_5_SKIP_END_SEC) {
                    set = true;
                } else if (VideoParameter.Group9.Section.VIDEO_9_9_SKIP_START_SEC <= currentPosition && currentPosition <= VideoParameter.Group9.Section.VIDEO_9_9_SKIP_END_SEC) {
                    set = true;
                }
                break;
        }

        if (set)
            return V_SKIP_INDEX;
        else
            return video_index;
    }

    /**
     * 정확도 lock 설정. 6초 안으로 0의 값이 들어오면 무시.
     */
    private void setAccuracyLock() {
        if (mHandler_AccuracyLock != null) {
            return;
        }

        mHandler_AccuracyLock = new Handler(Looper.getMainLooper());
        mHandler_AccuracyLock.postDelayed(mRunnable_AccuracyLock, interval_AccuracyLock * 1000);
        set_AccuracyLock = true;
    }

    /**
     * 정확도 lock 해제.
     */
    private void releaseAccuracyLock() {
        if (mHandler_AccuracyLock == null)
            return;

        mHandler_AccuracyLock.removeCallbacks(mRunnable_AccuracyLock);
        mHandler_AccuracyLock = null;
        set_AccuracyLock = false;
    }

    /**
     * 현재 정확도 lock 확인.
     *
     * @return 현재 정확도 lock flag.
     */
    private boolean getAccuracyLock() {
        return set_AccuracyLock;
    }

    /**
     * 정확도 lock 설정. 6초 안으로 0의 값이 들어오면 무시.
     */
    private void setAccuracyLockD() {
        if (mHandler_AccuracyLockD != null) {
            return;
        }

        mHandler_AccuracyLockD = new Handler();
        mHandler_AccuracyLockD.postDelayed(mRunnable_AccuracyLockD, interval_AccuracyLock * 1000);
        set_AccuracyLockD = true;
    }

    /**
     * 정확도 lock 해제.
     */
    private void releaseAccuracyLockD() {
        if (mHandler_AccuracyLockD == null)
            return;

        mHandler_AccuracyLockD.removeCallbacks(mRunnable_AccuracyLockD);
        mHandler_AccuracyLockD = null;
        set_AccuracyLockD = false;
    }

    /**
     * 현재 정확도 lock 확인.
     *
     * @return 현재 정확도 lock flag.
     */
    private boolean getAccuracyLockD() {
        return set_AccuracyLockD;
    }

    // s4-2
    /**
     * 각 동영상에 맞는 정확도 range 설정
     *
     * @param videoID
     *            현 재생 동영상 ID
     * @param percent
     *            parameter로 출력된 정확도
     * @return range 변경된 정확도
     */
    private int getAccuracyPerAction(int videoID, int percent) {
        switch (videoID) {
            case VideoParameter.Group1.Index.V_1_1:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 85)
                    return PHASE_3;
                else if (85 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group1.Index.V_1_8:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 85)
                    return PHASE_3;
                else if (85 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group1.Index.V_1_3:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 85)
                    return PHASE_3;
                else if (85 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group1.Index.V_1_6:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 85)
                    return PHASE_3;
                else if (85 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group1.Index.V_1_16:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 85)
                    return PHASE_3;
                else if (85 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group2.Index.V_2_1:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 95)
                    return PHASE_3;
                else if (95 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group2.Index.V_2_2:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 95)
                    return PHASE_3;
                else if (95 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group2.Index.V_2_3:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 85)
                    return PHASE_3;
                else if (85 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group2.Index.V_2_4:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 95)
                    return PHASE_3;
                else if (95 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group2.Index.V_2_6:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 95)
                    return PHASE_3;
                else if (95 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;

            case VideoParameter.Group3.Index.V_3_1:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 95)
                    return PHASE_3;
                else if (95 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group3.Index.V_3_4:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 95)
                    return PHASE_3;
                else if (95 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            case VideoParameter.Group3.Index.V_3_5:
                if (percent == 0)
                    return PHASE_0;
                else if (0 < percent && percent < 20)
                    return PHASE_1;
                else if (20 <= percent && percent < 40)
                    return PHASE_2;
                else if (40 <= percent && percent < 95)
                    return PHASE_3;
                else if (95 <= percent && percent < 100)
                    return PHASE_4;
                else
                    return PHASE_5;
            default:
                return PHASE_0;
        }
    }

    /**
     * SD-card의 경로를 얻음.
     *
     * @return SD-card의 경로.
     */
    public static String getSDPath() {
        String ext = Environment.getExternalStorageState();
        if (ext.equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            return Environment.MEDIA_UNMOUNTED;
        }
    }

    private void saveTag(String sdPath, String tag, String fileName) {
        if (sdPath != Environment.MEDIA_UNMOUNTED) {
            // short[] saveData = data;

            File dir = new File(sdPath + mFolder);
            dir.mkdir();

			/* ACC, HR 파일에 tag 삽입 */
            String fullPath_ACC = sdPath + mFolder + fileName + "-ACC.txt";
            String fullPath_HR = sdPath + mFolder + fileName + "-HR.txt";
            String fullPath_PRESS = sdPath + mFolder + fileName + "-PRESS.txt";
            String fullPath_GYRO = sdPath + mFolder + fileName + "-GYRO.txt";

            File fileAcc = new File(fullPath_ACC);
            File fileHR = new File(fullPath_HR);
            File filePRESS = new File(fullPath_PRESS);
            File fileGYRO = new File(fullPath_GYRO);

            FileOutputStream fosAcc = null;
            FileOutputStream fosHR = null;
            FileOutputStream fosPRESS = null;
            FileOutputStream fosGYRO = null;

            try {
                fosAcc = new FileOutputStream(fileAcc, true);
                fosHR = new FileOutputStream(fileHR, true);
                fosPRESS = new FileOutputStream(filePRESS, true);
                fosGYRO = new FileOutputStream(fileGYRO, true);

                /** 배열의 0,1,2 = 가속도 x,y,z **/
                /** 배열의 3,4,5 = 자이로 x,y,z **/
                /** 배열의 6 = 기압 **/
                /** 배열의 7 = 심박 **/
                String sumAcc = tag + "\n";

                fosAcc.write(sumAcc.getBytes());
                fosHR.write(sumAcc.getBytes());
                fosPRESS.write(sumAcc.getBytes());
                fosGYRO.write(sumAcc.getBytes());

            } catch (FileNotFoundException e) {
				/*
				 * Toast.makeText(this, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT)
				 * .show();
				 */
            } catch (Exception e) {
				/*
				 * Toast.makeText(this, "파일 생성에 문제가 발생했습니다.",
				 * Toast.LENGTH_SHORT) .show();
				 */
                e.printStackTrace();
            } finally {
                try {
                    fosAcc.close();
                    fosHR.close();
                    fosPRESS.close();
                    fosGYRO.close();

					/*
					 * Toast.makeText(this, fullPath + "파일이 생성되었습니다.",
					 * Toast.LENGTH_SHORT).show();
					 */
                } catch (IOException e) {
					/*
					 * Toast.makeText(this, "파일을 제대로 닫지 못했습니다.",
					 * Toast.LENGTH_SHORT).show();
					 */
                    e.printStackTrace();
                }
            }
        } else {
            // Toast.makeText(this, "외부 저장소가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Raw 데이터를 기록.
     *
     * @param sdPath
     *            Sd-card 경로.
     * @param data
     *            Raw 데이터.
     * @param fileName
     *            최종 파일명.
     */
    private void saveDb(String sdPath, double[] data, String fileName) {
        //Log.i(tag,"sdPath"+sdPath+"data"+data+"fileName"+fileName);
        if (sdPath != Environment.MEDIA_UNMOUNTED) {
            // short[] saveData = data;
            //Log.i(tag,"sdPath"+sdPath+"data"+data+"fileName"+fileName);
            File dir = new File(sdPath + mFolder);
            dir.mkdir();

			/* ACC, HR 파일 데이터 기록 */
            String fullPath_ACC = sdPath + mFolder + fileName + "-ACC.txt";
            String fullPath_HR = sdPath + mFolder + fileName + "-HR.txt";
            String fullPath_PRESS = sdPath + mFolder + fileName + "-PRESS.txt";
            String fullPath_GYRO = sdPath + mFolder + fileName + "-GYRO.txt";

            File fileAcc = new File(fullPath_ACC);
            File fileHR = new File(fullPath_HR);
            File filePRESS = new File(fullPath_PRESS);
            File fileGYRO = new File(fullPath_GYRO);

            FileOutputStream fosAcc = null;
            FileOutputStream fosHR = null;
            FileOutputStream fosPRESS = null;
            FileOutputStream fosGYRO = null;

            try {
                fosAcc = new FileOutputStream(fileAcc, true);
                fosHR = new FileOutputStream(fileHR, true);
                fosPRESS = new FileOutputStream(filePRESS, true);
                fosGYRO = new FileOutputStream(fileGYRO, true);

                /** 배열의 0,1,2 = 가속도 x,y,z **/
                /** 배열의 3,4,5 = 자이로 x,y,z **/
                /** 배열의 6 = 기압 **/
                /** 배열의 7 = 심박 **/
                String sumAcc = data[0] + " " + data[1] + " " + data[2] + "\n";
                String sumHR = data[7] + "\n";
                String sumPRESS = data[6] + "\n";
                String sumGYRO = data[3] + " " + data[4] + " " + data[5] + "\n";

                fosAcc.write(sumAcc.getBytes());
                fosHR.write(sumHR.getBytes());
                fosPRESS.write(sumPRESS.getBytes());
                fosGYRO.write(sumGYRO.getBytes());

            } catch (FileNotFoundException e) {
				/*
				 * Toast.makeText(this, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT)
				 * .show();
				 */
            } catch (Exception e) {
				/*
				 * Toast.makeText(this, "파일 생성에 문제가 발생했습니다.",
				 * Toast.LENGTH_SHORT) .show();
				 */
                e.printStackTrace();
            } finally {
                try {
                    fosAcc.close();
                    fosHR.close();
                    fosPRESS.close();
                    fosGYRO.close();
					/*
					 * Toast.makeText(this, fullPath + "파일이 생성되었습니다.",
					 * Toast.LENGTH_SHORT).show();
					 */
                } catch (IOException e) {
					/*
					 * Toast.makeText(this, "파일을 제대로 닫지 못했습니다.",
					 * Toast.LENGTH_SHORT).show();
					 */
                    e.printStackTrace();
                }
            }
        } else {
            // Toast.makeText(this, "외부 저장소가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDb(String sdPath, KIST_AART_output out, String fileName) {
        if (sdPath != Environment.MEDIA_UNMOUNTED) {
            // short[] saveData = data;

            File dir = new File(sdPath + mFolder);
            dir.mkdir();
            String fullPath_ACC = sdPath + mFolder + fileName + ".txt";
            File fileAcc = new File(fullPath_ACC);

            FileOutputStream fosAcc = null;
            try {
                fosAcc = new FileOutputStream(fileAcc, true);

                /** 배열의 0,1,2 = 가속도 x,y,z **/
                /** 배열의 3,4,5 = 자이로 x,y,z **/
                /** 배열의 6 = 기압 **/
                /** 배열의 7 = 심박 **/
                String sumFlag = "";
                String sumParam = "";

				/*
				 * for(int i=0; i<24; i++) { sumFlag +=
				 * Boolean.toString(out.parameter_flags[i]) + " "; } sumFlag +=
				 * "\n";
				 *
				 * for(int i=0; i<24; i++) { sumParam +=
				 * Double.toString(out.parameter_values[i]) + " "; }
				 */
                sumParam += "\n";
                sumParam += "\n";

                sumFlag += sumParam;

                fosAcc.write(sumFlag.getBytes());
            } catch (FileNotFoundException e) {
				/*
				 * Toast.makeText(this, "파일을 찾을 수 없습니다.", Toast.LENGTH_SHORT)
				 * .show();
				 */
            } catch (Exception e) {
				/*
				 * Toast.makeText(this, "파일 생성에 문제가 발생했습니다.",
				 * Toast.LENGTH_SHORT) .show();
				 */
                e.printStackTrace();
            } finally {
                try {
                    fosAcc.close();
					/*
					 * Toast.makeText(this, fullPath + "파일이 생성되었습니다.",
					 * Toast.LENGTH_SHORT).show();
					 */
                } catch (IOException e) {
					/*
					 * Toast.makeText(this, "파일을 제대로 닫지 못했습니다.",
					 * Toast.LENGTH_SHORT).show();
					 */
                    e.printStackTrace();
                }
            }
        } else {
            // Toast.makeText(this, "외부 저장소가 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private static String getFileNameDateFormat(String fileName) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
        return CurDateFormat.format(date) + fileName;
    }

    private static String getFileNameDateFormat2(String fileName) {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return CurDateFormat.format(date) + fileName;
    }

    /**
     * sumInterval 간격으로 칼로리를 합산.
     *
     * @param pulseRate
     *            sumInterval 간격의 평균 값으로 계산된 심박수.
     * @return 계산된 소비 칼로리.(kcal)
     */
    private float formulaHeartRate(int pulseRate) {
        DataBase.UserProfile profile = mConfig.getUserProfile();
        DataBase.UserWeightProfile wProfile = mConfig.getUserWeightProfile();
        float age = profile.getAge();
        float height = profile.getHeight();
        float weight = wProfile.getWeight();

        float calorie = 0;
        if (profile.getSex() == DataBaseUtil.SEX_MALE) {
            if (pulseRate > 90)
                calorie = (float) (-8477.604 + (weight * 6.481) + (pulseRate * 51.426) + (weight * pulseRate * 1.018)) * ((float) sumInterval / 60 / 1000);
            else
                calorie = (float) ((-0.0001 * pulseRate * pulseRate * pulseRate) + (0.0545 * pulseRate * pulseRate) - (5.00999 * pulseRate) + 181.2) * ((float) sumInterval / 60 / 60);
            return calorie;
        } else {
            if (pulseRate > 90)
                calorie = (float) (100.127 + (weight * -106.729) + (pulseRate * 12.580) + (weight * pulseRate * 1.251)) * ((float) sumInterval / 60 / 1000);
            else
                calorie = (float) ((-0.0001 * pulseRate * pulseRate * pulseRate) + (0.0545 * pulseRate * pulseRate) - (5.00999 * pulseRate) + 181.2) * ((float) sumInterval / 60 / 60);
            return calorie;
        }
    }

    /**
     * point 계산. 정확도의 횟수의 평균치를 계산한다.
     */
    private int getPoint(int count, int accuracy) {
        // 카운트와 정확도는 항상 100%를 넘을 수 있음. 넘으면 100%로 계산.
        // Log.d(tag,"getPoint : "+((accuracy * count)/100) + " count:"+count+"
        // acc:"+accuracy);
        return (accuracy * count) / 100;
    }

    /**
     * 최대 심박수 대비 현재 심박수를 반환.(%)
     *
     * @param hr
     *            현재 심박수
     * @return 반환하는 심박수(%)
     */
    private int getHeartRateCompared(int hr) {
        float[] zone = getHeartRateDangerZone();
        if (zone == null)
            return 0;

        return (int) (100f * hr / zone[0]);
    }

    private void onHeartRateCompared(int hr) {
        if (mIView == null)
            return;

        mIView.onHeartRateCompared(getHeartRateCompared(hr));
    }

    private int avgHeartRate(int hr) {
        int ret = 0, count = 0;
        for (int i = 0; i < main_HrArray.length - 1; i++) {
            main_HrArray[i] = main_HrArray[i + 1];
        }
        main_HrArray[4] = hr;

        for (int i : main_HrArray) {
            if (i != 0) {
                ret += i;
                count++;
            }
        }

        return count == 0 ? 0 : ret / count;
    }

    // for debug
    private static SimpleDateFormat CurDateFormat = new SimpleDateFormat("[yyyy-MM-dd HH.mm.SS]:: ");
    private static Date date = new Date();

    protected static void log(String tag, String log) {
		/*
		 * String sdPath = getSDPath(); if (sdPath !=
		 * Environment.MEDIA_UNMOUNTED) { long time =
		 * System.currentTimeMillis(); File dir = new File(sdPath + mFolder);
		 * dir.mkdir(); String fullPath_ACC = sdPath + mFolder +
		 * getFileNameDateFormat2("log-ble") + ".txt"; File fileAcc = new
		 * File(fullPath_ACC);
		 *
		 * FileOutputStream fosAcc = null; try { fosAcc = new
		 * FileOutputStream(fileAcc, true);
		 *
		 * String sumAcc = "";
		 *
		 * date.setTime(time); sumAcc = CurDateFormat.format(date) + log+"\n";
		 *
		 *
		 * fosAcc.write(sumAcc.getBytes()); } catch (Exception e) { } finally {
		 * try { if(fosAcc != null) fosAcc.close(); } catch (IOException e) { }
		 * } } else { }
		 */
    }

    /// Locale 수정///
    public int setLocale(String language) {

        int localenumber = 0;
        if (language.equals("ko")) {
            localenumber = 1; // 한국어
            LANGUAGE_SET = 1;
        } else if (language.equals("ja")) {
            localenumber = 2; // 일본어
            LANGUAGE_SET = 2;
        } else if (language.equals("en")) {
            localenumber = 3; // 미국
            LANGUAGE_SET = 3;
        } else {
            localenumber = 4; // null일경우
            LANGUAGE_SET = 4;
        }

        return localenumber;
    }
    /// 까지///
}