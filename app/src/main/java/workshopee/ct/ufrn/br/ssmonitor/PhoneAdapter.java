package workshopee.ct.ufrn.br.ssmonitor;

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
 */
public class PhoneAdapter extends ArrayAdapter<Phone> {

    TextView label1;
    TextView label2;
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

        label1 = (TextView) convertView.findViewById(R.id.label1);
        label2 = (TextView) convertView.findViewById(R.id.label2);
        imagem = (ImageView) convertView.findViewById(R.id.image);

        Phone item = getItem(position);
        if (item!= null) {

            label1.setText("id: " + String.valueOf(item.getId()) + " Pot: " + String.valueOf(item.getTorres()));
            label2.setText("Lat: " + String.valueOf(item.getLatitude()) + " Long: " + String.valueOf(item.getLongitude()));
            imagem.setImageResource(R.drawable.ic_launcher);

        }

        return convertView;
    }
}
