package net.doyouhike.app.wildbird.util;

import java.io.File;
import java.util.Locale;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class CustomPathDatabaseContext extends ContextWrapper {

	private String mDirPath;

	public CustomPathDatabaseContext(Context base, String dirPath) {
		super(base);
		this.mDirPath = dirPath;
	}

	@Override
	public File getDatabasePath(String name) {
		File result = new File(mDirPath + File.separator + name);

		if (!result.getParentFile().exists()) {
			result.getParentFile().mkdirs();
		}

		return result;
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
		db.setLocale(Locale.CHINA);
		return db;
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode, CursorFactory factory,
			DatabaseErrorHandler errorHandler) {
		SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(), factory, errorHandler);
		db.setLocale(Locale.CHINA);
		return db;
	}

}
