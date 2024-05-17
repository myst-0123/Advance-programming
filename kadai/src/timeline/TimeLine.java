package timeline;

import java.util.*;
import java.sql.*;

import dbmanager.*;

public class TimeLine {

    // public TimeLineState stateTL;
    private int SHOW_MAXIMUM_TWIT = 10;

    // コンストラクタ, TLの最大表示数を設定
    // CLIとGUIで同じように使えるように設計
    public TimeLine(int max_twits) {
        SHOW_MAXIMUM_TWIT = max_twits;
    }

    // TimeLineの更新
    // SHOW_MAXIMUM_TWITの数になるように減らして返す
    public List<Twit> updateTL() {
        List<Twit> TL_list = new ArrayList<Twit>();

        // TLを取得
        // 返り値は, Created_atの降順リスト
        TL_list = DBManager.getInstance().getTwit();

        /// エラー処理、DBから情報が取得できなかったら処理を終了する
        if (TL_list == null) {
            return null;
        }

        // リストの先頭からSHOW_MAXIMUM_TWITの数だけスライスする
        int twitSize = TL_list.size();
        if (twitSize > SHOW_MAXIMUM_TWIT)
            twitSize = SHOW_MAXIMUM_TWIT;
        List<Twit> subList = TL_list.subList(0, twitSize);
        return subList;
    }
}