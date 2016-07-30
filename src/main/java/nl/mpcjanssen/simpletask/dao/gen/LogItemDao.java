package nl.mpcjanssen.simpletask.dao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import nl.mpcjanssen.simpletask.dao.gen.LogItem;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "LOG_ITEM".
*/
public class LogItemDao extends AbstractDao<LogItem, Long> {

    public static final String TABLENAME = "LOG_ITEM";

    /**
     * Properties of entity LogItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Timestamp = new Property(1, java.util.Date.class, "timestamp", false, "TIMESTAMP");
        public final static Property Severity = new Property(2, String.class, "severity", false, "SEVERITY");
        public final static Property Tag = new Property(3, String.class, "tag", false, "TAG");
        public final static Property Message = new Property(4, String.class, "message", false, "MESSAGE");
        public final static Property Exception = new Property(5, String.class, "exception", false, "EXCEPTION");
    };


    public LogItemDao(DaoConfig config) {
        super(config);
    }
    
    public LogItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"LOG_ITEM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TIMESTAMP\" INTEGER NOT NULL ," + // 1: timestamp
                "\"SEVERITY\" TEXT NOT NULL ," + // 2: severity
                "\"TAG\" TEXT NOT NULL ," + // 3: tag
                "\"MESSAGE\" TEXT NOT NULL ," + // 4: message
                "\"EXCEPTION\" TEXT NOT NULL );"); // 5: exception
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"LOG_ITEM\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, LogItem entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTimestamp().getTime());
        stmt.bindString(3, entity.getSeverity());
        stmt.bindString(4, entity.getTag());
        stmt.bindString(5, entity.getMessage());
        stmt.bindString(6, entity.getException());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public LogItem readEntity(Cursor cursor, int offset) {
        LogItem entity = new LogItem( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            new java.util.Date(cursor.getLong(offset + 1)), // timestamp
            cursor.getString(offset + 2), // severity
            cursor.getString(offset + 3), // tag
            cursor.getString(offset + 4), // message
            cursor.getString(offset + 5) // exception
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, LogItem entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTimestamp(new java.util.Date(cursor.getLong(offset + 1)));
        entity.setSeverity(cursor.getString(offset + 2));
        entity.setTag(cursor.getString(offset + 3));
        entity.setMessage(cursor.getString(offset + 4));
        entity.setException(cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(LogItem entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(LogItem entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
