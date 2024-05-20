package timeline;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import dbmanager.*;

// コマンドラインでTwitを表示させる
public class ShowTimeLineCL {
    private TimeLine tl;
    private Timer timer;
    private TimerTask task;

    // コンストラクタ, TLの最大表示数と更新頻度を設定
    public ShowTimeLineCL(int max_twits) {
        // TimeLineクラスのインスタンスを生成
        tl = new TimeLine(max_twits);
    }

    // TimeLineの表示
    public void ShowTimeLine() {
        // Twitsの取得
        List<Twit> tl_list = tl.updateTL();

        if (tl_list == null) {
            System.out.println("ERROR : Twitを読み込めません");
            return;// null
        }

        // Twitsを表示
        for (Twit tw : tl_list) {
            System.out.println("------------------------------------------------------------------");
            System.out.println(tw.id + ". " + tw.name + "  " + tw.createdAt);
            System.out.println(tw.content);
        }
        System.out.println("------------------------------------------------------------------");
    }
}
