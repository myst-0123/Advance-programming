package timeline;

import java.util.Timer;
import java.util.TimerTask;

import dbmanager.*;

// コマンドラインでTwitを表示させる
public class ShowTimeLineCL {
    private TimeLine tl;
    private Timer timer;
    private TimerTask task;

    private int TL_UPDATE_TIME = 5;

    // コンストラクタ, TLの最大表示数と更新頻度を設定
    public ShowTimeLineCL(int max_twits, int update_time) {
        TL_UPDATE_TIME = update_time;
        // TimeLineクラスのインスタンスを生成
        tl = new TimeLine(max_twits);

        // 定期実行を行うためのTimer, TimerTask
        timer = new Timer(false);
        task = new TimerTask() {
            @override
            public void run() {
                ShowTimeLine();
            }
        }
        // TL_UPDATE_TIME毎にShowTimeLineを実行
        // 単位がmsのため、1000倍する
        timer.schedule(task, 0, TL_UPDATE_TIME * 1000);
    }

    // TimeLineの表示
    public void ShowTimeLine() {
        // Twitsの取得
        List<Twit> tl_list = tl.updateTL();

        if (tl_list == null) {
            System.out.println("ERROR : Twitを読み込めません");
            return null;
        }

        // Twitsを表示
        for (Twit tw : tl_list) {
            System.out.println("------------------------------------------------------------------");
            System.out.println(tw.name);
            System.out.println(tw.createdAt);
            System.out.println(tw.content);
        }
    }

    // TimeLineの更新の停止
    public void StopUpdateTL() {
        if (task == null) {
            System.out.println("停止中");
        }
        // Taskのキャンセル
        task.cancel();
        // 停止している判別が出来るようにTaskをnullにする
        task = null;
        System.out.println("停止");
    }

    // TimeLineの更新の再開
    public void RestartUpdaeteTL() {
        if(task != null) {
            System.out.println("更新中");
        }
        // Taskインスタンスの再生成
        task = new TimerTask() {
            @override
            public void run() {
                ShowTimeLine();
            }
        }
        // TL_UPDATE_TIME毎にShowTimeLineを実行
        timer.schedule(task, 0, TL_UPDATE_TIME * 1000);
        System.out.println("再開");
    }
}
