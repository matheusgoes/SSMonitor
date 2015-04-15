package workshopee.ct.ufrn.br.ssmonitor;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        View rootView=inflater.inflate(R.layout.fragment_main, container, false); //define o layout fragment_main
        //como layout a ser exibido
        //rootview->layout sendo exibido

        int sectionNumber = getArguments().getInt(ARG_SECTION_NUMBER);
        //Encontra latitude e longitude
        rootView = inflater.inflate(R.layout.fragment_monitor, container, false);
        Criteria criteria = new Criteria();
        Location location;
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        LocationManager locationmanager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        String provider = locationmanager.getBestProvider(criteria, true);

        if (locationmanager.getLastKnownLocation(provider)!=null){
            location= locationmanager.getLastKnownLocation(provider);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }else{
            GPSTracker d=new GPSTracker(getActivity());
            latitude = d.getLatitude();
            longitude =d.getLongitude();
        }

        switch (sectionNumber) {
            case 1:
                TextView latitude_textview = (TextView) rootView.findViewById(R.id.lat);
                latitude_textview.setText("Latitude: " + latitude);
                TextView longitude_textview = (TextView) rootView.findViewById(R.id.lon);
                longitude_textview.setText("Longitude: " + longitude);


                //Encontra dados de conexão
                String cid;
                String lac;
                int mcc = 0;
                int mnc = 0;
                int torres;
                double dbm;
                int networkTypeCode;
                String netWorkType;
                TextView forcasinal = (TextView) rootView.findViewById(R.id.forca),
                        operadoraview = (TextView) rootView.findViewById(R.id.operadora),
                        mccview = (TextView) rootView.findViewById(R.id.mcc),
                        mncview = (TextView) rootView.findViewById(R.id.mnc),
                        cidview = (TextView) rootView.findViewById(R.id.cid),
                        lacview = (TextView) rootView.findViewById(R.id.lac),
                        netWork = (TextView) rootView.findViewById(R.id.tipoderede);
                TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                CellInfoWcdma cellinfowcdma = (CellInfoWcdma)telephonyManager.getAllCellInfo().get(0);
                CellSignalStrengthWcdma cellSignalStrengthwcdma = cellinfowcdma.getCellSignalStrength();
                networkTypeCode = telephonyManager.getNetworkType();

                switch (networkTypeCode) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        netWorkType= "GPRS - 2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        netWorkType= "EDGE - 2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        netWorkType= "CDMA - 2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        netWorkType= "1xRTT - 2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_IDEN:
                        netWorkType= "IDEN - 2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        netWorkType= "UMTS - 3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        netWorkType= "EVDO_0 - 3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        netWorkType= "EVDO_A - 3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        netWorkType= "HSDPA - 3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        netWorkType= "HSUPA - 3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        netWorkType= "HSPA - 3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EVDO_B:
                        netWorkType= "EVDO_B - 3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_EHRPD:
                        netWorkType= "EHRPD - 3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_HSPAP:
                        netWorkType= "HSPAP - 3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        netWorkType= "LTE - 4G";
                        break;
                    default:
                        netWorkType= "Unknown";
                }

                String phoneType;
                if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM){
                    GsmCellLocation gsmLocation = (GsmCellLocation)telephonyManager.getCellLocation();
                    cid = "Cell ID (CID): " + gsmLocation.getCid();
                    lac = "Location Area Code (LAC): " + gsmLocation.getLac();
                    phoneType = " - GSM";
                }else if(telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA){
                    CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();
                    cid ="Id da base: " + cdmaCellLocation.getBaseStationId() + "Id da rede: "+cdmaCellLocation.getNetworkId();
                    lac= "Latitude da base: " + cdmaCellLocation.getBaseStationLatitude() + " Longidutde da base:" + cdmaCellLocation.getBaseStationLongitude();
                    phoneType = " - CDMA";
                }else{
                    cid="Desconhecido";
                    lac="Desconhecido";
                    phoneType=" - Desconhecido";
                }

                if (telephonyManager.getNetworkOperator() != null) {
                    mcc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(0, 3));
                    mnc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(3));
                }

                torres = cellSignalStrengthwcdma.getLevel();
                dbm = cellSignalStrengthwcdma.getDbm();

                forcasinal.setText("Qualidade do sinal: " + torres + " traços. " + dbm + " dbm.");
                operadoraview.setText("Operadora: "+ telephonyManager.getNetworkOperatorName() + phoneType);
                netWork.setText("Tipo de rede: "+ netWorkType);
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
                break;
            default:
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
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