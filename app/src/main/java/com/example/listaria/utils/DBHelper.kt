package com.example.listaria.utils
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.listaria.model.ListOfListsData
import kotlinx.coroutines.flow.*

class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    private val _dbUpdateFlow = MutableSharedFlow<MutableList<ListOfListsData>>(replay = 1)
    val dbUpdateFlow = _dbUpdateFlow.asSharedFlow()
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY, " +
                LIST_TITLE + " TEXT," +
                GPS_ENABLED + " INTEGER, " +
                STORE_CATEGORY + " TEXT, " +
                LIST_ITEMS + " TEXT" + ")")
        db.execSQL(query)
    }

    fun onDelete(listId: Int) {
        val db = this.writableDatabase
        val id = listId.toString()
        val query = "$ID_COL = $id"
        db.delete(TABLE_NAME, query, null )
        _dbUpdateFlow.tryEmit(readData())
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME)
        onCreate(db)
    }


    fun readData(): MutableList<ListOfListsData> {
        val list: MutableList<ListOfListsData> = mutableListOf()
        val db = this.readableDatabase
        val query = "Select * from $TABLE_NAME"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()) {
            do {
                val newList = ListOfListsData(
                    id = result.getString(result.getColumnIndexOrThrow(ID_COL)).toInt(),
                    title = result.getString(result.getColumnIndexOrThrow(LIST_TITLE)),
                    gpsOn = result.getString(result.getColumnIndexOrThrow(GPS_ENABLED)).toInt(),
                    storeCategory = result.getString(result.getColumnIndexOrThrow(STORE_CATEGORY)),
                    listContents = result.getString(result.getColumnIndexOrThrow(LIST_ITEMS)
                    ))
                list.add(newList)
            } while (result.moveToNext())
        }
        result.close()
        return list
    }

    fun addList(title: String, gps: Int, category: String, listSet: String): Long {
        val cv = ContentValues()
        cv.put(LIST_TITLE, title)
        cv.put(GPS_ENABLED, gps)
        cv.put(STORE_CATEGORY, category)
        cv.put(LIST_ITEMS, listSet)
        val result = this.writableDatabase.insert(TABLE_NAME, null, cv)
        _dbUpdateFlow.tryEmit(readData())
        return result
    }
    fun updateList(id: Int, title: String, gps: Int, category: String, listSet: String): Int {
        val cv = ContentValues()
        cv.put(LIST_TITLE, title)
        cv.put(GPS_ENABLED, gps)
        cv.put(STORE_CATEGORY, category)
        cv.put(LIST_ITEMS, listSet)
        val result = this.writableDatabase.update(TABLE_NAME, cv, "$ID_COL = $id", null)
        _dbUpdateFlow.tryEmit(readData())
        return result
    }
    companion object{
        private val DATABASE_NAME = "LISTS"
        private val DATABASE_VERSION = 1
        val TABLE_NAME = "list_table"
        val ID_COL = "id"
        val LIST_TITLE = "title"
        val GPS_ENABLED = "gpsOn"
        val STORE_CATEGORY = "storeCategory"
        val LIST_ITEMS = "items"
    }
}