package workshopee.ct.ufrn.br.ssmonitor;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.Gradient;
import com.google.maps.android.heatmaps.WeightedLatLng;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.PointsGraphSeries;
import com.google.maps.android.heatmaps.HeatmapTileProvider;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlaceholderFragment extends Fragment {

    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    public static final int MAP_TYPE_HYBRID = 4;
    private static final String ARG_SECTION_NUMBER = "section_number";
    static double latitude, longitude;
    int mapType=GoogleMap.MAP_TYPE_TERRAIN;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    CircleOptions options;
    GoogleMap mMap;

    public static PlaceholderFragment newInstance(int sectionNumber) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final MainActivity main = (MainActivity) getActivity();
        View rootView;

        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        switch (sectionNumber) {
            case 1:
                rootView = inflater.inflate(R.layout.fragment_monitor, container, false);

                TextView forcasinal = (TextView) rootView.findViewById(R.id.forca),
                        operadoraview = (TextView) rootView.findViewById(R.id.operadora),
                        mccview = (TextView) rootView.findViewById(R.id.mcc),
                        mncview = (TextView) rootView.findViewById(R.id.mnc),
                        cidview = (TextView) rootView.findViewById(R.id.cid),
                        lacview = (TextView) rootView.findViewById(R.id.lac),
                        latitude_textview = (TextView) rootView.findViewById(R.id.lat),
                        longitude_textview = (TextView) rootView.findViewById(R.id.lon),
                        netWork = (TextView) rootView.findViewById(R.id.tipoderede);

                forcasinal.setText("Qualidade do sinal: Torres: " + main.cell.getTorres() + " - DBM:  " + main.cell.getDbm());
                operadoraview.setText("Operadora: " + main.telephonyManager.getNetworkOperatorName() +" - "+ main.cell.getPhoneType());
                netWork.setText("Tipo de rede: " + main.cell.getNetWorkType());
                cidview.setText("CID " +main.cell.getCid());
                lacview.setText("LAC " + main.cell.getLac());
                mccview.setText("Mobile Country Code (MCC): " + main.cell.getMcc());
                mncview.setText("Mobile Network Code (MNC): " + main.cell.getMnc());
                latitude_textview.setText("Latitude: " + main.cell.getLatitude());
                longitude_textview.setText("Longitude: " + main.cell.getLongitude());
                break;

            case 2:
                rootView = inflater.inflate(R.layout.activity_mapa, container, false);
                mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
                List<Phone> listMap =  main.database_acesso.buscar_phone();
                mMap.setMapType(mapType);
                Log.i("MapType", ""+mMap.getMapType());
                mMap.setMyLocationEnabled(true);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17);
                mMap.animateCamera(cameraUpdate);
                mMap.getUiSettings().setCompassEnabled(true);
                mMap.getUiSettings().setAllGesturesEnabled(true);
                if (listMap.size()>0) {
                    //HEATMAP
                    int[] colors = {
                            Color.rgb(255, 0, 0),    // red
                            Color.rgb(0, 0, 255),   // blue
                            Color.rgb(102, 225, 0) // green
                    };

                    float[] startPoints = {
                            0.1f, 0.2f ,0.3f
                    };

                    ArrayList<WeightedLatLng> pontos = new ArrayList<>();

                    for (int i = 0; i < listMap.size(); i++) {
                        pontos.add(new WeightedLatLng(new LatLng(listMap.get(i).getLatitude(), listMap.get(i).getLongitude()), listMap.get(i).getDbm()));
                    }
                    mProvider = new HeatmapTileProvider
                            .Builder()
                            .weightedData(pontos)
                            .build();
                    mProvider.setGradient(new Gradient(colors, startPoints));
                    mProvider.setRadius(20);
                    mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                    // ATE AQUI

                }
                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {
                        Log.i("Chamada: ", "Localização do Google map alterada");
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                                location.getLongitude()), 17);
                        mMap.animateCamera(cameraUpdate);
                    }
                });
                break;
            case 3:
                rootView = inflater.inflate(R.layout.fragment_graficos, container, false);
                final GraphView graph = (GraphView) rootView.findViewById(R.id.graph);
                Spinner spinnerGraficos = (Spinner) rootView.findViewById(R.id.spinnerGraficos);
                spinnerGraficos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        List<Phone> list = main.database_acesso.buscar_phone();
                        switch (position) {
                            case 0:
                                graph.removeAllSeries();
                                PointsGraphSeries<DataPoint> series1;
                                DataPoint[] dataPoint1 = new DataPoint[list.size()];
                                for(int i=0; i<list.size(); i++) {
                                    dataPoint1[i] = new DataPoint(i, list.get(i).getLatitude());
                                }
                                series1 =new PointsGraphSeries<>(dataPoint1);
                                series1.setColor(Color.parseColor("#0099FF"));
                                series1.setShape(PointsGraphSeries.Shape.POINT);
                                graph.addSeries(series1);
                                break;
                            case 1:
                                graph.removeAllSeries();
                                PointsGraphSeries<DataPoint> series3;
                                DataPoint[] dataPoint3 = new DataPoint[list.size()];
                                for(int i=0; i<list.size(); i++) {
                                    dataPoint3[i] = new DataPoint(i, list.get(i).getLongitude());
                                }
                                series3 =new PointsGraphSeries<>(dataPoint3);
                                series3.setColor(Color.parseColor("#009900"));
                                series3.setShape(PointsGraphSeries.Shape.POINT);
                                graph.addSeries(series3);
                                break;
                            case 2:
                                PointsGraphSeries<DataPoint> serieSinal;
                                DataPoint[] dataPoint2 = new DataPoint[list.size()];
                                for(int i=0; i<list.size(); i++) {
                                    dataPoint2[i] = new DataPoint(i, list.get(i).getTorres());
                                }
                                graph.removeAllSeries();
                                serieSinal = new PointsGraphSeries<>(dataPoint2);
                                serieSinal.setColor(Color.parseColor("#990000"));
                                serieSinal.setShape(PointsGraphSeries.Shape.POINT);
                                graph.addSeries(serieSinal);
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        mapType = GoogleMap.MAP_TYPE_NORMAL;
                    }
                });
                break;
            case 4:
                rootView = inflater.inflate(R.layout.fragment_list, container, false);
                final ListView list_view = (ListView) rootView.findViewById(R.id.listView);
                List<Phone> list =  main.database_acesso.buscar_phone();
                Button dropButton = (Button) rootView.findViewById(R.id.drop);
                dropButton.setText(dropButton.getText() + "(" + list.size() + ")");
                PhoneAdapter adapter = new PhoneAdapter(main.getApplicationContext(),list);
                list_view.setAdapter(adapter);
                list_view.setDivider(new ColorDrawable(Color.parseColor("#19A3A3")));
                list_view.setDividerHeight(2);
                dropButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        main.database_acesso.clear();
                        List<Phone> list = main.database_acesso.buscar_phone();
                        PhoneAdapter adapter = new PhoneAdapter(main.getApplicationContext(),list);
                        list_view.setAdapter(adapter);
                    }
                });
                break;
            case 5:
                rootView = inflater.inflate(R.layout.fragment_sobre, container, false);
                break;
            case 1000:
                main.getSupportActionBar().setTitle("Configurações");
                rootView = inflater.inflate(R.layout.fragment_settings, container, false);
                Spinner spinnerMapas = (Spinner) rootView.findViewById(R.id.spinnerMapas);
                spinnerMapas.setSelection(mapType-1);
                final Switch mSwitch = (Switch) rootView.findViewById(R.id.ativarNotificacoesSwitch);
                mSwitch.setChecked(main.notifications_actived);
                spinnerMapas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        switch (position) {
                            case 0:
                                mapType = MAP_TYPE_NORMAL;
                                break;
                            case 1:
                                mapType = MAP_TYPE_SATELLITE;
                                break;
                            case 2:
                                mapType = MAP_TYPE_TERRAIN;
                                break;
                            case 3:
                                mapType = MAP_TYPE_HYBRID;
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        mapType = GoogleMap.MAP_TYPE_NORMAL;
                    }
                });
                mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        main.notifications_actived = isChecked;
                    }
                });

                break;
            default:
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                ImageView imageView = (ImageView) rootView.findViewById(R.id.logoInicio);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (main.mNavigationDrawerFragment!=null){
                            if (!main.mNavigationDrawerFragment.isDrawerOpen()) {
                                main.drawerLayout.openDrawer(Gravity.START);
                            }
                        }
                    }
                });
        }
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }
}

/* CODIGO DE CIRCULOS NO MAPA

                Location atual = new Location("Atual"), anterior = new Location("anterior");
                anterior.setLatitude(-5.7229835);
                anterior.setLongitude(-34.6779897);
                int cor = Color.RED;
                for (int i=0; i< listMap.size(); i++){
                    switch (listMap.get(i).getTorres()){
                        case 4:
                            cor = Color.GREEN;
                            break;
                        case 3:
                            cor = Color.BLUE;
                            break;
                        case 2:
                            cor = Color.YELLOW;
                            break;
                        case 1:
                            cor = Color.RED;
                            break;
                        case 0:
                            cor = Color.BLACK;
                    }
                    atual.setLatitude(listMap.get(i).getLatitude());
                    atual.setLongitude(listMap.get(i).getLongitude());
                    if (atual.distanceTo(anterior) > 10){
                        options = new CircleOptions().
                                center(new LatLng(listMap.get(i).getLatitude(),listMap.get(i).getLongitude()))
                                .fillColor(cor)
                                .radius(5.0)
                                .strokeWidth(5)
                                .strokeColor(Color.TRANSPARENT);
                        mMap.addCircle(options);
                        Log.i("Circulo", "desenhando");
                        anterior.setLatitude(atual.getLatitude());
                        anterior.setLongitude(atual.getLongitude());
                    }
                }
 */