package workshopee.ct.ufrn.br.ssmonitor;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class PlaceholderFragment extends Fragment {

    // The fragment argument representing the section number for this
    //fragment.

    private static final String ARG_SECTION_NUMBER = "section_number";
    GoogleMap mMap;
    double latitude, longitude;
    static Context context;
    CellInfoWcdma cellinfowcdma;
    CellSignalStrengthWcdma cellSignalStrengthwcdma;
    CellInfoCdma cellInfoCdma;
    CellSignalStrengthCdma cellSignalStrengthCdma;
    CellInfoGsm cellInfoGsm;
    CellSignalStrengthGsm cellSignalStrengthGsm;
    CellInfoLte cellInfoLte;
    CellSignalStrengthLte cellSignalStrengthLte;

    // Returns a new instance of this fragment for the given section
    //number.

    public static PlaceholderFragment newInstance(int sectionNumber, Context _context) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        context = _context;
        fragment.setArguments(args);
        return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        View rootView;
        if (telephonyManager!= null) {
            LocationManager locationmanager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            rootView = inflater.inflate(R.layout.activity_location_services_off, container, false); //define o layout fragment_main
            //como layout a ser exibido
            //rootview->layout sendo exibido
            if ((locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))){

                int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
                //Encontra latitude e longitude
                Criteria criteria = new Criteria();
                Location location;
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                locationmanager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                String provider = locationmanager.getBestProvider(criteria, true);

                if (locationmanager.getLastKnownLocation(provider) != null) {
                    location = locationmanager.getLastKnownLocation(provider);
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                } else {
                    GPSTracker gpsTracker = new GPSTracker(getActivity());
                    location = gpsTracker.getLocation();
                    latitude = gpsTracker.getLatitude();
                    longitude= gpsTracker.getLongitude();
                }

                switch (sectionNumber) {
                    case 1:
                        rootView = inflater.inflate(R.layout.fragment_monitor, container, false);
                        TextView latitude_textview = (TextView) rootView.findViewById(R.id.lat);
                        latitude_textview.setText("Latitude: " + latitude);
                        TextView longitude_textview = (TextView) rootView.findViewById(R.id.lon);
                        longitude_textview.setText("Longitude: " + longitude);


                        //Encontra dados de conexão
                        String cid;
                        String lac;
                        int mcc = 0;
                        int mnc = 0;
                        int networkTypeCode;
                        String netWorkType;
                        TextView forcasinal = (TextView) rootView.findViewById(R.id.forca),
                                operadoraview = (TextView) rootView.findViewById(R.id.operadora),
                                mccview = (TextView) rootView.findViewById(R.id.mcc),
                                mncview = (TextView) rootView.findViewById(R.id.mnc),
                                cidview = (TextView) rootView.findViewById(R.id.cid),
                                lacview = (TextView) rootView.findViewById(R.id.lac),
                                netWork = (TextView) rootView.findViewById(R.id.tipoderede);

                        try {
                            int torres=0;
                            double dbm=0;
                            for (final CellInfo info : telephonyManager.getAllCellInfo()) {
                                if (info instanceof CellInfoGsm) {
                                    cellSignalStrengthGsm = ((CellInfoGsm) info).getCellSignalStrength();
                                    torres = cellSignalStrengthGsm.getLevel();
                                    dbm = cellSignalStrengthGsm.getDbm();
                                    Log.i("Cell Signal:", "tipo gsm");
                                } else if (info instanceof CellInfoCdma) {
                                    cellSignalStrengthCdma = ((CellInfoCdma) info).getCellSignalStrength();
                                    torres = cellSignalStrengthCdma.getLevel();
                                    dbm = cellSignalStrengthCdma.getDbm();
                                    Log.i("Cell Signal:", "tipo cdma");
                                } else if (info instanceof CellInfoLte) {
                                    cellSignalStrengthLte = ((CellInfoLte) info).getCellSignalStrength();
                                    torres = cellSignalStrengthLte.getLevel();
                                    dbm = cellSignalStrengthLte.getDbm();
                                    Log.i("Cell Signal:", "tipo lte");
                                }else if (info instanceof CellInfoWcdma){
                                    cellSignalStrengthwcdma= ((CellInfoWcdma) info).getCellSignalStrength();
                                    torres = cellSignalStrengthwcdma.getLevel();
                                    dbm = cellSignalStrengthwcdma.getDbm();
                                    Log.i("Cell Signal:", "tipo wcdma");
                                } else {
                                    throw new Exception("Unknown type of cell signal!");
                                }
                            }
                            forcasinal.setText("Qualidade do sinal: " + torres + " traços. " + dbm + " dbm.");
                        } catch (Exception e) {
                            Log.e("Find cell signal: ", "Unable to obtain cell signal information", e);
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


                        networkTypeCode = telephonyManager.getNetworkType();

                        switch (networkTypeCode) {
                            case TelephonyManager.NETWORK_TYPE_GPRS:
                                netWorkType = "GPRS - 2G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_EDGE:
                                netWorkType = "EDGE - 2G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_CDMA:
                                netWorkType = "CDMA - 2G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_1xRTT:
                                netWorkType = "1xRTT - 2G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_IDEN:
                                netWorkType = "IDEN - 2G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_UMTS:
                                netWorkType = "UMTS - 3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                                netWorkType = "EVDO_0 - 3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                                netWorkType = "EVDO_A - 3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_HSDPA:
                                netWorkType = "HSDPA - 3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_HSUPA:
                                netWorkType = "HSUPA - 3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_HSPA:
                                netWorkType = "HSPA - 3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                                netWorkType = "EVDO_B - 3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_EHRPD:
                                netWorkType = "EHRPD - 3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_HSPAP:
                                netWorkType = "HSPAP - 3G";
                                break;
                            case TelephonyManager.NETWORK_TYPE_LTE:
                                netWorkType = "LTE - 4G";
                                break;
                            default:
                                netWorkType = "Unknown";
                        }

                        String phoneType;
                        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                            GsmCellLocation gsmLocation = (GsmCellLocation) telephonyManager.getCellLocation();
                            cid = "Cell ID (CID): " + gsmLocation.getCid();
                            lac = "Location Area Code (LAC): " + gsmLocation.getLac();
                            phoneType = " - GSM";
                        } else if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();
                            cid = "Id da base: " + cdmaCellLocation.getBaseStationId() + "Id da rede: " + cdmaCellLocation.getNetworkId();
                            lac = "Latitude da base: " + cdmaCellLocation.getBaseStationLatitude() + " Longidutde da base:" + cdmaCellLocation.getBaseStationLongitude();
                            phoneType = " - CDMA";
                        } else {
                            cid = "Desconhecido";
                            lac = "Desconhecido";
                            phoneType = " - Desconhecido";
                        }

                        if (telephonyManager.getNetworkOperator() != null) {
                            mcc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(0, 3));
                            mnc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(3));
                        }

                        operadoraview.setText("Operadora: " + telephonyManager.getNetworkOperatorName() + phoneType);
                        netWork.setText("Tipo de rede: " + netWorkType);
                        cidview.setText(cid);
                        lacview.setText(lac);
                        mccview.setText("Mobile Country Code (MCC): " + mcc);
                        mncview.setText("Mobile Network Code (MNC): " + mnc);

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
                    default:
                        rootView = inflater.inflate(R.layout.fragment_main, container, false);
                }
            }
        }else{
            rootView=inflater.inflate(R.layout.semsim, container, false);
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
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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