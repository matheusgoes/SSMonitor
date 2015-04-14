package workshopee.ct.ufrn.br.ssmonitor;

import android.app.Activity;
import android.location.Criteria;
import android.location.Location;
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
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

      //A placeholder fragment containing a simple view.

    public static class PlaceholderFragment extends Fragment implements LocationListener, OnMapReadyCallback{

         // The fragment argument representing the section number for this
         //fragment.

        private static final String ARG_SECTION_NUMBER = "section_number";


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
            double latitude, longitude;
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

                        forcasinal.setText("Força do sinal: " + torres + " traços. " + dbm + " dbm.");
                        operadoraview.setText("Operadora: "+ telephonyManager.getNetworkOperatorName());
                        cidview.setText("Cell ID (CID): " + cid );
                        lacview.setText("Location Area Code (LAC): " + lac );
                        mccview.setText("Mobile Country Code (MCC): " + mcc);
                        mncview.setText("Mobile Network Code (MNC): " + mnc);

                        break;
                    case 2:
                        rootView = inflater.inflate(R.layout.activity_mapa, container, false);

                        GoogleMap mMap = ((SupportMapFragment) getActivity().getSupportFragmentManager()
                                .findFragmentById(R.id.map)).getMap();

                       /* GoogleMap map = ((SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map))
                                .getMap();

                        Marker marker = map.addMarker(new MarkerOptions()
                                .position(new LatLng(latitude, longitude))
                                .title("Estou aqui"));
                        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                              //.MAP_TYPE_HYBRID
                                              //.MAP_TYPE_NORMAL
                                              //.MAP_TYPE_SATELITE
                        map.getUiSettings().setZoomGesturesEnabled(true); //Permite zoom
                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 13));*/
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

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onMapReady(GoogleMap map) {

        }
    }
}
