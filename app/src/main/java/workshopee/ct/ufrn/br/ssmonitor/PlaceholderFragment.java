package workshopee.ct.ufrn.br.ssmonitor;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class PlaceholderFragment extends Fragment {

    // The fragment argument representing the section number for this
    //fragment.
    public static final int MAP_TYPE_NONE = 0;
    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;
    public static final int MAP_TYPE_TERRAIN = 3;
    public static final int MAP_TYPE_HYBRID = 4;
    private static final String ARG_SECTION_NUMBER = "section_number";
    GoogleMap mMap;
    static double latitude, longitude;
    static int mapType=GoogleMap.MAP_TYPE_TERRAIN;


    // Returns a new instance of this fragment for the given section
    //number.

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

                forcasinal.setText("Qualidade do sinal: Torres: " + main.torres + " - DBM:  " + main.dbm);
                operadoraview.setText("Operadora: " + main.telephonyManager.getNetworkOperatorName() + main.phoneType);
                netWork.setText("Tipo de rede: " + main.netWorkType);
                cidview.setText("CID " +main.cid);
                lacview.setText("LAC " + main.lac);
                mccview.setText("Mobile Country Code (MCC): " + main.mcc);
                mncview.setText("Mobile Network Code (MNC): " + main.mnc);
                latitude_textview.setText("Latitude: " + main.latitude);
                longitude_textview.setText("Longitude: " + main.longitude);
                break;

            case 2:
                rootView = inflater.inflate(R.layout.activity_mapa, container, false);
                marcarPosicao();
                break;
            case 3:
                rootView = inflater.inflate(R.layout.fragment_graficos, container, false);
                break;
            case 4:
                rootView = inflater.inflate(R.layout.fragment_sobre, container, false);
                break;
            case 1000:
                rootView = inflater.inflate(R.layout.fragment_settings, container, false);
                Spinner spinner = (Spinner) rootView.findViewById(R.id.spinnerMapas);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
                        Log.i("MapType", "" + mapType);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        mapType = GoogleMap.MAP_TYPE_NORMAL;
                    }
                });
                break;
            default:
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                ImageView imageView = (ImageView) rootView.findViewById(R.id.logoInicio);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.i("Image click", "Clicked");
                        if (main.mNavigationDrawerFragment!=null){
                            if (!main.mNavigationDrawerFragment.isDrawerOpen()) {
                                main.drawerLayout.openDrawer(getActivity().findViewById(main.fragmentId));
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

    public void marcarPosicao(){
        mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setMapType(mapType);
        Log.i("MapType", ""+mMap.getMapType());
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        //mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        mMap.setMyLocationEnabled(true);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17);
        mMap.animateCamera(cameraUpdate);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        Location myLocation = mMap.getMyLocation();
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                Log.i("Chamada: ", "Localização do Google map alterada");
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                        location.getLongitude()), 17);
                mMap.animateCamera(cameraUpdate);
            }
        });


            /*PolylineOptions options = new PolylineOptions();

            options.color( Color.parseColor("#CC0000FF") );
            options.width( 5 );
            options.visible( true );
            options.add(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()));*/

            /*mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Estou aqui")
                    .snippet("Latitude "+latitude + " longitude " + longitude));*/
    }
}


//Encontra dados de conexão
                            /*if (telephonyManager.getAllCellInfo() != null){
                                if (telephonyManager.getAllCellInfo().get(0).getClass() == cellinfowcdma.getClass()) {
                                    cellinfowcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
                                    cellSignalStrengthwcdma = cellinfowcdma.getCellSignalStrength();
                                    torres = cellSignalStrengthwcdma.getLevel();
                                    dbm = cellSignalStrengthwcdma.getDbm();
                                    Log.i("Cell Signal:", "tipo wcdma");
                                    Toast.makeText(getApplicationContext() ,"WCDMA!", Toast.LENGTH_SHORT).show();
                                }else if (telephonyManager.getAllCellInfo().get(0).getClass() == cellInfoCdma.getClass()){
                                    cellInfoCdma = (CellInfoCdma) telephonyManager.getAllCellInfo().get(0);
                                    cellSignalStrengthCdma = cellInfoCdma.getCellSignalStrength();
                                    torres = cellSignalStrengthCdma.getLevel();
                                    dbm = cellSignalStrengthCdma.getDbm();
                                    Log.i("Cell Signal:", "tipo cdma");

                                }else if (telephonyManager.getAllCellInfo().get(0).getClass() == cellInfoGsm.getClass()){
                                    cellInfoGsm = (CellInfoGsm) telephonyManager.getAllCellInfo().get(0);
                                    cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();
                                    torres = cellSignalStrengthGsm.getLevel();
                                    dbm = cellSignalStrengthGsm.getDbm();
                                    Log.i("Cell Signal:", "tipo gsm");

                                }else if (telephonyManager.getAllCellInfo().get(0).getClass() == cellInfoLte.getClass()){
                                    cellInfoLte = (CellInfoLte) telephonyManager.getAllCellInfo().get(0);
                                    cellSignalStrengthLte = cellInfoLte.getCellSignalStrength();
                                    torres = cellSignalStrengthLte.getLevel();
                                    dbm = cellSignalStrengthLte.getDbm();
                                    Log.i("Cell Signal:", "tipo lte");

                                }
                            }*/

