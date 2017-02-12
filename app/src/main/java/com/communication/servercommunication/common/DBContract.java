package com.communication.servercommunication.common;

import android.provider.BaseColumns;


/**
 * Created by hwangem on 2017-01-25.
 */

/*DB용 Constant*/
public class DBContract implements BaseColumns {

    public interface SOSListItem {
        // SOS누르미 테이블 이름
        public static final String TABLE_NAME_SOS = "SOS";

        // SOS누르미 테이블 : 게시글 구분 SEQ
        public static final String COLUMN_NAME_SOS_SEQ = "SEQ";

        // SOS누르미 테이블 : 게시글 제목
        public static final String COLUMN_NAME_SOS_TITLE = "TITLE";

        // SOS누르미 테이블 : 게시글 내용
        public static final String COLUMN_NAME_SOS_CONTENT = "CONTENT";

        // SOS누르미 테이블 : boardSeq(게시판 번호)
        public static final String COLUMN_NAME_SOS_ANSIM_INFO_SEQ = "ANSIM_INFO_SEQ";

        // SOS누르미 테이블 : 바로가기용 숏키
        public static final String COLUMN_NAME_SOS_SHORT_KEY = "SHORT_KEY";
    }
}
