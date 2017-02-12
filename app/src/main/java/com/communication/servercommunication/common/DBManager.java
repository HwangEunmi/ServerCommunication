package com.communication.servercommunication.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.communication.servercommunication.MyApplication;
import com.communication.servercommunication.model.DBData;
import com.communication.servercommunication.model.SOSListData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hwangem on 2017-01-25.
 */

/* DB는 TABLE 구조로 Data를 관리하고 있으므로, 우선 해야 할 작업이 TABLE 구조를 만드는 일임 */
public class DBManager extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    private static final String DB_NAME = "sos_data_db"; // DB 이름

    private static final int DB_VERSION = 1; // DB 버전


    private static DBManager instance;

    public static DBManager getInstance(Context context) {
        if (instance == null) {
            instance = new DBManager(context);
        }
        return instance;
    }

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("TEST", "call oncreate");

        try {
            db.beginTransaction();
            StringBuffer sql = new StringBuffer();

            sql.append("CREATE TABLE ").append(DBContract.SOSListItem.TABLE_NAME_SOS).append(" (");
            sql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_SEQ).append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
            sql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ).append(" INTEGER NOT NULL, ");
            sql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_SHORT_KEY).append(" TEXT, "); // 얘도  not null인지 확인(again)
            sql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_TITLE).append(" TEXT NOT NULL, ");
            sql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_CONTENT).append(" TEXT NOT NULL)");

            db.execSQL(sql.toString());

            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    // 내일 시간 되면 하나 테이블 더 추가해서 마이그레이션 해보기
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // (디비내용의 변경으로)버전이 업데이트되면 db를 다시 만들어줘야함(기존의 table을 삭제하고 새로운 table만들기)
    }

    /**
     * SOS누르미 데이터 등록
     *
     * @param data
     * @return
     */
    /*boardSeq == ansim_info_seq*/
    public int insertSOSData(DBData data) {

        db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.clear();
        values.put(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ, data.getAnsim_info_seq());
        values.put(DBContract.SOSListItem.COLUMN_NAME_SOS_SHORT_KEY, data.getShort_key());
        values.put(DBContract.SOSListItem.COLUMN_NAME_SOS_TITLE, data.getTitle());
        values.put(DBContract.SOSListItem.COLUMN_NAME_SOS_CONTENT, data.getContent());

        long lRow = -1;

        try {
            db.beginTransaction();
            lRow = db.insert(DBContract.SOSListItem.TABLE_NAME_SOS, null, values);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return (int) lRow;
    }

    /**
     * SOS누르미 데이터 갱신
     *
     * @param data
     * @return
     */
    public int updateSOSData(DBData data) {

        db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.clear();
        values.put(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ, data.getAnsim_info_seq());
        values.put(DBContract.SOSListItem.COLUMN_NAME_SOS_SHORT_KEY, data.getShort_key());
        values.put(DBContract.SOSListItem.COLUMN_NAME_SOS_TITLE, data.getTitle());
        values.put(DBContract.SOSListItem.COLUMN_NAME_SOS_CONTENT, data.getContent());

        /*수정될 조건*/
        String strWhere = DBContract.SOSListItem.COLUMN_NAME_SOS_SEQ + " = ?";
        String[] strWhereArgs = {data.getAnsim_info_seq() + ""};

        long lRow = -1;

        try {
            db.beginTransaction();
            db.update(DBContract.SOSListItem.TABLE_NAME_SOS, values, strWhere, strWhereArgs);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return (int) lRow;
    }

    /**
     * SOS누르미 데이터 삭제 (조건 있는)
     *
     * @param data
     */
    public void deleteSOSData(DBData data) {
        db = getWritableDatabase();

        long lRow = -1;

        try {
            db.beginTransaction();

            StringBuffer sbSql = new StringBuffer();
            sbSql.append("DELETE FROM ");
            sbSql.append(DBContract.SOSListItem.TABLE_NAME_SOS);
            sbSql.append(" WHERE ")
                    .append(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ).append(" = ").append(data.getAnsim_info_seq());// ANSIM_INFO_SEQ

            db.execSQL(sbSql.toString());
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * SOS누르미 데이터 삭제 (조건 없는)
     */
    public void deleteAllData() {
        db = getWritableDatabase();

        try {
            db.beginTransaction();

            db.delete(DBContract.SOSListItem.TABLE_NAME_SOS, null, null);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * SOS누르미 데이터 조회 (조건 있는)
     *
     * @return
     */
    public Cursor getKeywordSOSData(int boardSeq) {
        db = getReadableDatabase();

        String[] strSelectionArgs = {boardSeq + ""};

        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_SEQ).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_SHORT_KEY).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_TITLE).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_CONTENT);

        sbSql.append(" FROM ").append(DBContract.SOSListItem.TABLE_NAME_SOS);
        sbSql.append(" WHERE ").append(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ).append(" = ?");
        sbSql.append(" ORDER BY ").append(DBContract.SOSListItem.COLUMN_NAME_SOS_SEQ).append(" ASC ");

        Cursor cursor = null;

        try {
            db.beginTransaction();

            cursor = db.rawQuery(sbSql.toString(), strSelectionArgs);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    /**
     * SOS누르미 데이터 조회 (조건 없는)
     *
     * @return
     */
    public Cursor getSOSData() {
        db = getReadableDatabase();

        Cursor cursor = null;

        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_SEQ).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_SHORT_KEY).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_TITLE).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_CONTENT);

        sbSql.append(" FROM ").append(DBContract.SOSListItem.TABLE_NAME_SOS);
        sbSql.append(" ORDER BY ").append(DBContract.SOSListItem.COLUMN_NAME_SOS_SEQ).append(" ASC ");

        try {
            db.beginTransaction();

            cursor = db.rawQuery(sbSql.toString(), null);
            db.setTransactionSuccessful();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return cursor;
    }

    /**
     * SOS누르미 데이터 리스트 조회 (조건 없는)
     *
     * @return
     */
    public List<DBData> getSOSListData() {
        db = getReadableDatabase();

        Cursor cursor = null;

        StringBuffer sbSql = new StringBuffer();
        sbSql.append("SELECT ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_SEQ).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_SHORT_KEY).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_TITLE).append(", ");
        sbSql.append(DBContract.SOSListItem.COLUMN_NAME_SOS_CONTENT);

        sbSql.append(" FROM ").append(DBContract.SOSListItem.TABLE_NAME_SOS);
        sbSql.append(" ORDER BY ").append(DBContract.SOSListItem.COLUMN_NAME_SOS_SEQ).append(" ASC ");

        List<DBData> listData = null;

        try {
            cursor = db.rawQuery(sbSql.toString(), null);

            listData = new ArrayList<>();

            if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
                do {
                    DBData dbData = new DBData();

                    dbData.setSosSEQ(cursor.getInt(cursor.getColumnIndex(DBContract.SOSListItem.COLUMN_NAME_SOS_SEQ)));
                    dbData.setAnsim_info_seq(cursor.getInt(cursor.getColumnIndex(DBContract.SOSListItem.COLUMN_NAME_SOS_ANSIM_INFO_SEQ)));
                    dbData.setShort_key(cursor.getString(cursor.getColumnIndex(DBContract.SOSListItem.COLUMN_NAME_SOS_SHORT_KEY)));
                    dbData.setTitle(cursor.getString(cursor.getColumnIndex(DBContract.SOSListItem.COLUMN_NAME_SOS_TITLE)));
                    dbData.setContent(cursor.getString(cursor.getColumnIndex(DBContract.SOSListItem.COLUMN_NAME_SOS_CONTENT)));

                    listData.add(dbData);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return listData;
    }
}
