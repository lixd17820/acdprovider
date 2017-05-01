package com.acd.simple.provider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.util.Log;

public class AcdSimpleProvider extends ContentProvider {

	private static final String TAG = "AcdSimpleProvider";

	public AcdSimpleDatabaseHelper dbAdapter;

	public static final UriMatcher uriMatcher;

	private static final int ADD_ACD = 0;

	private static final int DEL_ACD = 1;

	private static final int ADD_HUMAN = 2;

	private static final int DEL_HUMAN = 3;

	private static final int QUERY_ACD = 4;

	private static final int QUERY_HUMAN = 5;

	private static final int UPDATE_ACD = 6;

	private static final int UPDATE_HUMAN = 7;

	private static final int RAW_QUERY = 8;

	private static final int RAW_DEL = 9;

	private static final int CREATE_DB = 10;

	private static final int QUERY_ACD_LAW = 11;

	private static final int QUERY_REPAIR = 12;

	private static final int DEL_REPAIR = 13;

	private static final int UPDATE_REPAIR = 14;

	private static final int ADD_REPAIR = 15;

	private static final int DEL_ACD_PHOTO = 16;

	private static final int QUERY_ACD_PHOTO = 17;

	private static final int ADD_ACD_PHOTO = 18;

	private static final int DEL_ACD_FILE = 19;

	private static final int QUERY_ACD_FILE = 20;

	private static final int ADD_ACD_FILE = 21;

	private static final int UPDATE_ACD_PHOTO = 22;

	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.ADD_ACD, ADD_ACD);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.DEL_ACD, DEL_ACD);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.ADD_HUMAN, ADD_HUMAN);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.DEL_HUMAN, DEL_HUMAN);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.QUERY_ACD, QUERY_ACD);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.QUERY_HUMAN,
				QUERY_HUMAN);
		uriMatcher
				.addURI(AcdSimple.AUTHORITY, AcdSimple.UPDATE_ACD, UPDATE_ACD);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.UPDATE_HUMAN,
				UPDATE_HUMAN);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.RAW_QUERY, RAW_QUERY);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.RAW_DEL, RAW_DEL);

		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.CREATE_DB, CREATE_DB);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.QUERY_ACD_LAW,
				QUERY_ACD_LAW);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.QUERY_REPAIR,
				QUERY_REPAIR);
		uriMatcher
				.addURI(AcdSimple.AUTHORITY, AcdSimple.DEL_REPAIR, DEL_REPAIR);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.UPDATE_REPAIR,
				UPDATE_REPAIR);

		uriMatcher
				.addURI(AcdSimple.AUTHORITY, AcdSimple.ADD_REPAIR, ADD_REPAIR);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.ADD_ACD_PHOTO,
				ADD_ACD_PHOTO);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.DEL_ACD_PHOTO,
				DEL_ACD_PHOTO);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.QUERY_ACD_PHOTO,
				QUERY_ACD_PHOTO);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.UPDATE_ACD_PHOTO,
				UPDATE_ACD_PHOTO);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.ADD_ACD_FILE,
				ADD_ACD_FILE);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.DEL_ACD_FILE,
				DEL_ACD_FILE);
		uriMatcher.addURI(AcdSimple.AUTHORITY, AcdSimple.QUERY_ACD_FILE,
				QUERY_ACD_FILE);

	}

	// 创建数据库
	@Override
	public boolean onCreate() {
		int currentCode = 0;
		try {
			PackageManager pm = getContext().getPackageManager();
			PackageInfo pi = pm.getPackageInfo("com.acd.simple.provider", 0);
			currentCode = pi.versionCode;
		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}

		Log.e(TAG, "on create method");
		dbAdapter = AcdSimpleDatabaseHelper.getDBAdapterInstance(getContext(),
				currentCode);
		try {
			dbAdapter.createDataBase();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		dbAdapter.openDataBase();
		return true;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowId = 0;
		switch (uriMatcher.match(uri)) {
		// 删除一条简易程序事故
		case DEL_ACD:
			rowId += dbAdapter.deleteRecordInDB(
					AcdSimple.AcdDutySimple.TABLE_NAME, selection,
					selectionArgs);
			break;
		// 删除一条人员信息
		case DEL_HUMAN:
			rowId = dbAdapter.deleteRecordInDB(
					AcdSimple.AcdDutySimpleHuman.TABLE_NAME, selection,
					selectionArgs);
			break;
		case RAW_DEL:
			dbAdapter.executeRawSql(selection);
			break;
		case DEL_REPAIR:
			rowId = dbAdapter.deleteRecordInDB(AcdSimple.Repair.TABLE_NAME,
					selection, selectionArgs);
			break;
		case DEL_ACD_PHOTO:
			rowId = dbAdapter.deleteRecordInDB(
					AcdSimple.AcdPhotoRecode.TABLE_NAME, selection,
					selectionArgs);
			break;
		case DEL_ACD_FILE:
			rowId = dbAdapter
					.deleteRecordInDB(AcdSimple.AcdPhotoFile.TABLE_NAME,
							selection, selectionArgs);
			break;
		default:
			break;
		}
		return rowId;
	}

	@Override
	public String getType(Uri uri) {
		int code = uriMatcher.match(uri);
		switch (code) {
		case ADD_ACD:
		case ADD_HUMAN:
		case ADD_REPAIR:
			return AcdSimple.CONTENT_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// SQLiteDatabase db = databasehelper.getWritableDatabase();
		long rowId = 0;
		switch (uriMatcher.match(uri)) {
		case ADD_ACD:
			rowId = dbAdapter.insertRecordsInDB(
					AcdSimple.AcdDutySimple.TABLE_NAME, values);
			break;
		case ADD_HUMAN:
			rowId = dbAdapter.insertRecordsInDB(
					AcdSimple.AcdDutySimpleHuman.TABLE_NAME, values);
			break;
		case ADD_REPAIR:
			rowId = dbAdapter.insertRecordsInDB(AcdSimple.Repair.TABLE_NAME,
					values);
			break;
		case ADD_ACD_PHOTO:
			rowId = dbAdapter.insertRecordsInDB(
					AcdSimple.AcdPhotoRecode.TABLE_NAME, values);
			break;
		case ADD_ACD_FILE:
			rowId = dbAdapter.insertRecordsInDB(
					AcdSimple.AcdPhotoFile.TABLE_NAME, values);
			break;
		default:
			break;
		}
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(AcdSimple.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		Cursor c = null;

		switch (uriMatcher.match(uri)) {
		case QUERY_ACD:
			c = dbAdapter.selectRecordsFromDB(
					AcdSimple.AcdDutySimple.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		// 根据行政区划查询用户区域内的道路列表
		case QUERY_HUMAN:
			c = dbAdapter.selectRecordsFromDB(
					AcdSimple.AcdDutySimpleHuman.TABLE_NAME, projection,
					selection, selectionArgs, null, null, sortOrder);
			break;
		case RAW_QUERY:
			Log.e(TAG, "RAW QUERY");
			c = dbAdapter.executeRawQuerySql(selection);
			break;
		case CREATE_DB:
			copyDataBase();
			break;
		case QUERY_ACD_LAW:
			c = dbAdapter
					.selectRecordsFromDB(AcdSimple.AcdLaws.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		case QUERY_REPAIR:
			c = dbAdapter
					.selectRecordsFromDB(AcdSimple.Repair.TABLE_NAME,
							projection, selection, selectionArgs, null, null,
							sortOrder);
			break;
		case QUERY_ACD_PHOTO:
			c = dbAdapter.selectRecordsFromDB(
					AcdSimple.AcdPhotoRecode.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		case QUERY_ACD_FILE:
			c = dbAdapter.selectRecordsFromDB(
					AcdSimple.AcdPhotoFile.TABLE_NAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		int row = 0;
		switch (uriMatcher.match(uri)) {
		// 更新简易程序
		case UPDATE_ACD:
			row = dbAdapter.updateRecordsInDB(
					AcdSimple.AcdDutySimple.TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case UPDATE_HUMAN:
			row = dbAdapter.updateRecordsInDB(
					AcdSimple.AcdDutySimpleHuman.TABLE_NAME, values, selection,
					selectionArgs);
			break;
		case UPDATE_REPAIR:
			row = dbAdapter.updateRecordsInDB(AcdSimple.Repair.TABLE_NAME,
					values, selection, selectionArgs);
			break;
		case UPDATE_ACD_PHOTO:
			row = dbAdapter.updateRecordsInDB(
					AcdSimple.AcdPhotoRecode.TABLE_NAME, values, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		return row;
	}

	private void copyDataBase() {
		String DB_PATH = "/data/data/com.acd.simple.provider/databases/acdSimple.db";
		try {
			FileInputStream in = new FileInputStream(new File(DB_PATH));
			FileOutputStream out = new FileOutputStream(new File(
					"/sdcard/acdSimple.db"));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}
			in.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
