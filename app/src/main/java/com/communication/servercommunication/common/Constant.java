package com.communication.servercommunication.common;

/**
 * Created by hwangem on 2017-01-20.
 */

/*클래스변수 선언*/
public final class Constant {

    ////////서버 관련////////
    /*sos누르미 게시판 리스트 URL 주소*/
    public static final String GET_BOARD_LIST_URL = "https://sos.openit.co.kr/board/getBoardList.json";

    /*sos누르미 게시판 내용 URL 주소*/
    public static final String GET_BOARD_CONTENT_URL = "https://sos.openit.co.kr/board/getBoardContent.json";

    /*기본 sos누르미 주소*/
    public static final String DEFAULT_SOS_URL = "https://sos.openit.co.kr";

    /*POST 방식*/
    public static final String METHOD_POST = "POST";


    ////////웹뷰에서 호출할 URL 주소//////////
    public static final String WEBVIEW_SOS_URL = "https://help.openit.co.kr/legal/sosnurmi/terms";

    public static final String WEBVIEW_NAVER_URL = "http://www.naver.com/";


    ////////Intent 관련////////
    /*Intent Result Code*/
    public static final int INTENT_VALUE_CODE = 100;

    /*Intent Request Code*/
    public static final String INTENT_REQUEST_CODE = "result";

    /*Intent boardSeq 플래그*/
    public static final String INTENT_BOARDSEQ_FLAG = "boardSeq";
}
