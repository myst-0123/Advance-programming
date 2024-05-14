package timeline;

import java.util.TimerTask;
import java.util.ArrayList;

public class TimeLine {

    public TimeLineState stateTL;
    private int SHOW_MAXIMUM_TWIT = 10;
    
    // コンストラクタ, TLの最大表示数を設定
    // CLIとGUIで同じように使えるように設計
    public TimeLine(max_twits) {
        SHOW_MAXIMUM_TWIT = max;
    }

    // TimeLineの更新
    // SHOW_MAXIMUM_TWITの数になるように減らして返す
    public ArrayList<TwitCapsule> updateTL() {
        ArrayList<TwitCapsule> TL_list = new ArrayList<TwitCapsule>();

        // TLStateを渡して、TLを取得する。
        TL_list = geTwits(stateTL);

        /// エラー処理、DBから情報が取得できなかったら処理を終了する
        if(TL_list == null) {
            return null;
        }

        return TL_list;
    }

    // ダミーのメソッド
    ArrayList<TwitCapsule> geTwits() {}

}