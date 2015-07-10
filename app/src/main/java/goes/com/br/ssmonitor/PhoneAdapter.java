package goes.com.br.ssmonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by jaack05 on 22/04/15.
 * classe para adaptar a lista de objetos phone ao layout
 */
public class PhoneAdapter extends ArrayAdapter<Phone> {

    TextView id;
    TextView lat;
    TextView lng;
    TextView torre;
    TextView dbm;
    ImageView imagem;

    private List<Phone> values;
    private Context context;

    public PhoneAdapter(Context context,List<Phone> itens) {
        super(context, R.layout.list_adapter, itens);

        this.values = itens;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(this.context).inflate(R.layout.list_adapter, parent, false);

        lat = (TextView) convertView.findViewById(R.id.lat);
        lng = (TextView) convertView.findViewById(R.id.lng);
        id = (TextView) convertView.findViewById(R.id.id);
        torre = (TextView) convertView.findViewById(R.id.pot);
        dbm = (TextView) convertView.findViewById(R.id.dbm);
        imagem = (ImageView) convertView.findViewById(R.id.image);

        Phone item = getItem(position);
        if (item!= null) {
            id.setText("ID: " + String.valueOf(item.getId()));
            torre.setText("Pot: " + String.valueOf(item.getTorres()));
            dbm.setText(" DBM: "+ String.valueOf(item.getDbm()));
            lat.setText("Lat: " + String.valueOf(item.getLatitude()));
            lng.setText(" Long: " + String.valueOf(item.getLongitude()));
            imagem.setImageResource(R.drawable.ic_notif);
        }

        return convertView;
    }
}
