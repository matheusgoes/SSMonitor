package workshopee.ct.ufrn.br.ssmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaack05 on 19/04/15.
 */
public class Database_Acesso {
    private SQLiteDatabase db;
    private static final String table1 = "phone";

    public Database_Acesso(Context context) {
        Database aux_db = new Database(context);
        db = aux_db.getWritableDatabase();
    }
    public void inserir_phone (Phone ph) {

        ContentValues valores = new ContentValues();
        valores.put("longitude",ph.getLongitude());
        valores.put("latitude", ph.getLatitude());
        valores.put("qtdtorres", ph.getTorres());
        valores.put("dbm", ph.getDbm());
        valores.put("mcc", ph.getMcc());
        valores.put("mnc", ph.getMnc());
        valores.put("phone_type", ph.getPhoneType());
        valores.put("operadora", ph.getOperadora());
        valores.put("cid",ph.getCid());
        valores.put("lac",ph.getLac());
        db.insert("phone", null, valores);
    }


    public List<Phone> buscar_phone () {
        List<Phone> lista = new ArrayList<Phone>();

        String[] colunas = new String[]{"_id", "longitude", "latitude", "qtdtorres", "dbm",
                            "mcc", "mnc", "phone_type", "operadora", "networkType", "cid", "lac"};

        Cursor cursor = db.query("phone", colunas, null, null, null, null,"_id ASC");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Phone p = new Phone();
                p.setId(cursor.getLong(0));
                p.setLongitude(cursor.getDouble(1));
                p.setLatitude(cursor.getDouble(2));
                p.setTorres(cursor.getInt(3));
                p.setDbm(cursor.getInt(4));
                p.setMcc(cursor.getInt(5));
                p.setMnc(cursor.getInt(6));
                p.setPhoneType(cursor.getString(7));
                p.setOperadora(cursor.getString(8));
                p.setNetWorkType(cursor.getString(9));
                p.setCid(cursor.getInt(10));
                p.setLac(cursor.getInt(11));
                lista.add(p);

            } while (cursor.moveToNext());
        }

        return lista;
    }

    void clear(){
        db.delete("phone", "_id>?", new String [] { "-1" });
    }

}
