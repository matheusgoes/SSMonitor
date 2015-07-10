package goes.com.br.ssmonitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaack05 on 19/04/15.
 */

//essa classe tem o intuito de servir como mediador entre o app e bb
    // ela compreende as funções de insersão, busca e delete dos dados do bd

public class Database_Acesso {
    private SQLiteDatabase db;
    private static final String table1 = "phone";

    //construtor
    public Database_Acesso(Context context) {
        Database aux_db = new Database(context);
        //um objeto dessa classe é um banco de dados onde se é possível escrever
        db = aux_db.getWritableDatabase();
    }

    //função inserir
    /*
    A função recebe como parâmetro um objeto da classe Phone e em seguida, add cada um dos seus
    valores na devida coluna do banco de dados
     */
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
        valores.put("asu",ph.getASU());
        db.insert("phone", null, valores);
    }

    //função buscar
    /*
    o intuito dessa função é retornar todos os dados salvos no bd
    para isso, ela deverá retornar uma lista de objetos phone
     */
    public List<Phone> buscar_phone () {
        List<Phone> lista = new ArrayList<Phone>();

        //nessa string iremos salvar os nomes das colunas da tabela
        String[] colunas = new String[]{"_id", "longitude", "latitude", "qtdtorres", "dbm",
                            "mcc", "mnc", "phone_type", "operadora", "networkType", "cid", "lac", "asu"};

        //a função query, que foi importada pelo SQLiteDatabeHelper retorna um cursor, o qual vai do primeiro ao ultimo
        //elemento encontrado na tabela segundo as caracteristicas da busca
        Cursor cursor = db.query("phone", colunas, null, null, null, null,"_id ASC");
        if (cursor.getCount() > 0) {
            //agora iremos adaptar esse cursor a uma lista de objetos phone
            cursor.moveToFirst(); //move o cursor para a primeira linha retornada na consulta
            do {
                //cria um objeto da classe phone, apenas para auxiliar o processo
                Phone p = new Phone();
                //insere-se no objeto da classe phone os dados obtidos na consulta de acordo com a posição do tipo na String colunas
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
                p.setASU(cursor.getInt(12));
                lista.add(p); //insere o objeto aux na lista

            } while (cursor.moveToNext()); //repete os passos anteriores enquanto o cursor puder ser movido a uma próxima posição
        }

        return lista;
    }

    //função clear
    //deleta todos os dados salvos no banco de dados.
    void clear(){
        db.delete("phone", "_id>?", new String [] { "-1" });
    }

}
