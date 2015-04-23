/**
 * Created by jaack05 on 18/04/15.
 */
package workshopee.ct.ufrn.br.ssmonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper{
    // Versão do db
    private static final int versao_db = 1;

    // Nome do db
    private static final String nome_db = "ssmonitor_db";

    // Nome das tabelas
    private static final String table1 = "phone";

    // Nomes das colunas da tabela phone
    private static final String id = "_id";
    private static final String longitude = "longitude";
    private static final String latitude = "latitude";
    private static final String forca_torres = "qtdtorres";
    private static final String forca_dbm = "dbm";
    private static final String mcc = "mcc";
    private static final String mnc = "mnc";
    private static final String phone_type = "phone_type";
    private static final String operadora = "operadora";
    private static final String network_type = "networkType";
    private static final String cid = "cid";
    private static final String lac = "lac";

   /*// Nomes das colunas da tabela phone_cdma
    private static final String id2 = "_id_table2";
    private static final String id_phone = "id_phone";
    private static final String baseid = "baseId";
    private static final String base_lng = "baseLng";
    private static final String base_lat = "baseLat";


    //Nomes das colunas da tabela phone_gsm
    private static final String id3 = "_id_table3";*/



    public Database(Context context) {
        super(context, nome_db, null, versao_db);
    }
    // Criando as tabelas
    @Override
    public void onCreate(SQLiteDatabase db) {
        //tabela phone
        String criarTabela = "CREATE TABLE " + table1 + "("
                + id + " INTEGER PRIMARY KEY AUTOINCREMENT," + longitude + " REAL,"
                + latitude + " REAL," + forca_torres + " INTEGER," + forca_dbm + " REAL," + mcc + " INTEGER,"
                + mnc + " INTEGER," + phone_type + " TEXT," + operadora + " TEXT," + network_type + " INTEGER," + cid + " INTEGER,"
                + lac + " INTEGER )";
        db.execSQL(criarTabela);

        /*//tabela phone_cdma
        criarTabela = "CREATE TABLE " + table2 + "("
                + id2 + " INTEGER PRIMARY KEY AUTOINCREMENT," + id_phone + " INTEGER," + baseid + " INTEGER,"
                + base_lng + " REAL," + base_lat + " REAL, FOREIGN KEY("+ id_phone +") REFERENCES " + table1 +"("+id+") ON DELETE RESTRICT ON UPDATE CASCADE )";
        db.execSQL(criarTabela);

        //tabela phone_gsm
        criarTabela = "CREATE TABLE " + table2 + "("
                + id3 + " INTEGER PRIMARY KEY AUTOINCREMENT," + id_phone + " INTEGER," + cid + " INTEGER,"
                + lac + " INTEGER, FOREIGN KEY("+ id_phone +") REFERENCES " + table1 +"("+id+") ON DELETE RESTRICT ON UPDATE CASCADE )";
        db.execSQL(criarTabela);*/
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int versao_ant, int versao_nv) {
        Log.w(Database.class.getName(),
                "Atualizando o banco de dados da versão " + versao_ant + " para "
                        + versao_nv + ", isso apagará os dados antigos.");
        db.execSQL("DROP TABLE IF EXISTS " + table1 + ";");
        onCreate(db);

    }

    public void clear (SQLiteDatabase db) {
        Log.w(Database.class.getName(),
                "Apagando informações salvas anteriormente.");
        db.execSQL("DROP TABLE IF EXISTS " + table1 + ";");
        onCreate(db);
    }

}
