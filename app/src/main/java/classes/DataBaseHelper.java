package classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


public class DataBaseHelper extends SQLiteOpenHelper {
    Context context;
    public DataBaseHelper(Context context)
    {
        super(context,"Movies",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS  user (id INTEGER PRIMARY KEY AUTOINCREMENT,username VARCHAR not null,email VARCHAR not null,password VARCHAR not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS user");
    }
    public boolean insert(String name, String email,String password)
    {
        SQLiteDatabase db;
        try{
            db=this.getReadableDatabase();
            String query = "Insert into user(username,email,password) values('"+name+"','"+email+"','"+password+"')";
            db.execSQL(query);
            db.close();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    public boolean checkExists(String email)
    {
        try{
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT * FROM user WHERE email=? AND password=?",new String[]{email});
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                cursor.close();
                return true;
            }
        }catch (Exception ex){
            Log.e("Error",ex.getMessage());
        }
        return false;
    }
    public boolean login(String email,String password)
    {
        try {
            SQLiteDatabase db=this.getReadableDatabase();
            Cursor cursor=db.rawQuery("SELECT id FROM user WHERE email=? AND password=?",new String[]{email,password});
            if(cursor.getCount()>0) {
                cursor.moveToFirst();
                cursor.close();
                return true;
            }
        }
        catch (Exception ex){
            Log.e("Error",ex.getMessage());
        }
        return false;
    }

}
