/**
 * Created by jaack05 on 18/04/15.
 */
package goes.com.br.ssmonitor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Database extends SQLiteOpenHelper{
    // Versão do db
    private static final int versao_db = 1;

    // Nome do db
    private static final String nome_db = "ssmonitor_db";

    // Nome da tabela
    private static final String table1 = "phone";

    // Nomes das colunas da tabela phone
    private static final String id = "_id";
    private static final String longitude = "longitude";
    private static final String latitude = "latitude";
    private static final String forca_torres = "qtdtorres";
    private static final String forca_dbm = "dbm";
    private static final String forca_asu = "asu";
    private static final String mcc = "mcc";
    private static final String mnc = "mnc";
    private static final String phone_type = "phone_type";
    private static final String operadora = "operadora";
    private static final String network_type = "networkType";
    private static final String cid = "cid";
    private static final String lac = "lac";

    //construtor do banco
    public Database(Context context) {
        super(context, nome_db, null, versao_db);
    }
    // Criando as tabelas
    //caso a versao seja a mesma da já criada o construtor executará essa função
    @Override
    public void onCreate(SQLiteDatabase db) {
        //tabela phone
        //na String abaixo o comando sql que responsável pela criação da tabela
        String criarTabela = "CREATE TABLE " + table1 + "("
                + id + " INTEGER PRIMARY KEY AUTOINCREMENT," + longitude + " REAL,"
                + latitude + " REAL," + forca_torres + " INTEGER," + forca_dbm + " REAL," + mcc + " INTEGER,"
                + mnc + " INTEGER," + phone_type + " TEXT," + operadora + " TEXT," + network_type + " INTEGER," + cid + " INTEGER,"
                + lac + " INTEGER," + forca_asu + " INTEGER )";

        //execução do comando SQL
        db.execSQL(criarTabela);
    }

    //caso a versão seja diferente da já criada o construtor executa essa função
    @Override
    public void onUpgrade(SQLiteDatabase db, int versao_ant, int versao_nv) {
        //mensagem de log para avisar sobre a mudança da versão
        Log.w(Database.class.getName(),
                "Atualizando o banco de dados da versão " + versao_ant + " para "
                        + versao_nv + ", isso apagará os dados antigos.");
        //comando para deletar a tabela
        db.execSQL("DROP TABLE IF EXISTS " + table1 + ";");
        //criar novamente a tabela
        onCreate(db);

    }

}
