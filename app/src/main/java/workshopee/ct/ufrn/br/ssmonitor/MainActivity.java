package workshopee.ct.ufrn.br.ssmonitor;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;


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
    CellSignalStrengthWcdma cellSignalStrengthwcdma;
    CellInfoWcdma cellinfowcdma;
    TelephonyManager telephonyManager;
    Criteria criteria;
    int cid = 0;
    int lac = 0;
    int mcc = 0;
    int mnc = 0;
    int torres;
    double dbm;
    String operadora, provider;

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

        criteria = new Criteria();
        Location location;
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationmanager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationmanager.getBestProvider(criteria, true);
        if (!locationmanager.getLastKnownLocation(provider).equals(null)){
            location= locationmanager.getLastKnownLocation(provider);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }else{
            GPSTracker d=new GPSTracker(this);
            latitude = d.getLatitude();
            longitude =d.getLongitude();
        }

        //Cria instancia de TelephonyManager e implementa o location listener
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                latitude=location.getLatitude();
                longitude=location.getLongitude();

                //Encontra dados de conexão
                cellinfowcdma = (CellInfoWcdma)telephonyManager.getAllCellInfo().get(0);
                cellSignalStrengthwcdma = cellinfowcdma.getCellSignalStrength();
                operadora = telephonyManager.getNetworkOperatorName();

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


                Log.i("Called: ", "location changed. Lat: " + latitude + " lng: " + longitude);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {  }

            @Override
            public void onProviderEnabled(String provider) {  }

            @Override
            public void onProviderDisabled(String provider) {  }
        };

        //Define atualização de localização
        locationmanager.requestLocationUpdates(provider, 0, 0, locationListener);

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
            case 4:
                mTitle = getString(R.string.title_section5);
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
}
