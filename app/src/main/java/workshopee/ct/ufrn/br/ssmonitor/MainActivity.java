package workshopee.ct.ufrn.br.ssmonitor;

import android.app.Activity;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    LocationManager locationmanager;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        Criteria criteria = new Criteria();
        Location location;
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationmanager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        String provider = locationmanager.getBestProvider(criteria, true);
        if (!locationmanager.getLastKnownLocation(provider).equals(null)){
            location= locationmanager.getLastKnownLocation(provider);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }else{
            GPSTracker d=new GPSTracker(this);
            latitude = d.getLatitude();
            longitude =d.getLongitude();
        }
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude=location.getLatitude();
                longitude=location.getLongitude();

                Log.i("Called: ", "location changed. Lat: " + latitude + " lng: " + longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();

                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position))
                        .commit();

        Log.v("POSITION", "POSITION=" + position);
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.title_section1);
                break;
            case 1:
                mTitle = getString(R.string.title_section2);
                break;
            case 2:
                mTitle = getString(R.string.title_section3);
                break;
            case 3:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            Toast.makeText(this ,"Atualzando!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //A placeholder fragment containing a simple view.

    public static class PlaceholderFragment extends Fragment{

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
            if (!locationmanager.getLastKnownLocation(provider).equals(null)){
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
                        int cid = 0;
                        int lac = 0;
                        int mcc = 0;
                        int mnc = 0;
                        int torres;
                        double dbm;
                        TextView forcasinal = (TextView) rootView.findViewById(R.id.forca),
                                operadoraview = (TextView) rootView.findViewById(R.id.operadora),
                                mccview = (TextView) rootView.findViewById(R.id.mcc),
                                mncview = (TextView) rootView.findViewById(R.id.mnc),
                                cidview = (TextView) rootView.findViewById(R.id.cid),
                                lacview = (TextView) rootView.findViewById(R.id.lac);
                        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                        CellInfoWcdma cellinfowcdma = (CellInfoWcdma)telephonyManager.getAllCellInfo().get(0);
                        CellSignalStrengthWcdma cellSignalStrengthwcdma = cellinfowcdma.getCellSignalStrength();

                        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM){
                            GsmCellLocation gsmLocation = (GsmCellLocation)telephonyManager.getCellLocation();
                            cid = gsmLocation.getCid();
                            lac = gsmLocation.getLac();
                        }

                        if (telephonyManager.getNetworkOperator() != null) {
                            mcc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(0, 3));
                            mnc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(3));
                        }

                        torres = cellSignalStrengthwcdma.getLevel();
                        dbm = cellSignalStrengthwcdma.getDbm();

                        forcasinal.setText("Qualidade do sinal: " + torres + " traços. " + dbm + " dbm.");
                        operadoraview.setText("Operadora: "+ telephonyManager.getNetworkOperatorName());
                        cidview.setText("Cell ID (CID): " + cid );
                        lacview.setText("Location Area Code (LAC): " + lac );
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
}
