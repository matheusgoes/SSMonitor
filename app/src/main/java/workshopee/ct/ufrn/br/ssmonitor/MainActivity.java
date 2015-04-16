package workshopee.ct.ufrn.br.ssmonitor;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrength;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthWcdma;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private CharSequence mTitle;
    LocationManager locationmanager;
    double latitude, longitude;
    CellInfoWcdma cellinfowcdma;
    CellSignalStrengthWcdma cellSignalStrengthwcdma;
    CellInfoCdma cellInfoCdma;
    CellSignalStrengthCdma cellSignalStrengthCdma;
    CellInfoGsm cellInfoGsm;
    CellSignalStrengthGsm cellSignalStrengthGsm;
    CellInfoLte cellInfoLte;
    CellSignalStrengthLte cellSignalStrengthLte;
    TelephonyManager telephonyManager;
    Criteria criteria;
    Location location;

    int cid;
    int lac;
    int baseId, netWorkID;
    double baseLat, baseLng;
    int mcc = 0;
    int mnc = 0;
    int torres = 0, networkTypeCode;
    double dbm;
    String operadora, provider;
    String phoneType, netWorkType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager!=null){
            if (telephonyManager.getSimState()==TelephonyManager.SIM_STATE_READY) {
                locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if ((locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))){
                    setContentView(R.layout.activity_main);

                    //Cria menu de navegação
                    mNavigationDrawerFragment = (NavigationDrawerFragment)
                            getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
                    mTitle = getTitle();

                    // Configura menu de navegação
                    mNavigationDrawerFragment.setUp(
                            R.id.navigation_drawer,
                            (DrawerLayout) findViewById(R.id.drawer_layout));


                    //Inicializa Localização
                    criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    provider = locationmanager.getBestProvider(criteria, true);
                    if (locationmanager.getLastKnownLocation(provider)!=null){
                        location = locationmanager.getLastKnownLocation(provider);
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }else{
                        GPSTracker gpsTracker = new GPSTracker(this);
                        location = gpsTracker.getLocation();
                        latitude = gpsTracker.getLatitude();
                        longitude= gpsTracker.getLongitude();
                    }

                    getInfo();


                    //Cria instancia de TelephonyManager e implementa o location listener
                    LocationListener locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location _location) {
                            getInfo();
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

                    //Define atualização de localização
                    locationmanager.requestLocationUpdates(provider, 1000, 1, locationListener);

                }else{
                    setContentView(R.layout.activity_location_services_off);
                }
            }else{
                setContentView(R.layout.semsim);
            }
        }else{
            setContentView(R.layout.semsim);
        }
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
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#19A3A3")));
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mNavigationDrawerFragment!=null){
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
                // Only show items in the action bar relevant to this screen
                // if the drawer is not showing. Otherwise, let the drawer
                // decide what to show in the action bar.
                getMenuInflater().inflate(R.menu.main, menu);
                restoreActionBar();
                return true;
            }
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
       switch (id){
        case R.id.action_settings:
            Toast.makeText(this ,"Config!", Toast.LENGTH_SHORT).show();
            return true;
           case R.id.action_close:
               this.onDestroy();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    public void getInfo(){
        //Obtem novos dados de localização
        latitude = location.getLatitude();
        longitude = location.getLongitude();

        try {
            for (final CellInfo info : telephonyManager.getAllCellInfo()) {
                String tipo;
                if (info instanceof CellInfoGsm) {
                    cellSignalStrengthGsm = ((CellInfoGsm) info).getCellSignalStrength();
                    torres = cellSignalStrengthGsm.getLevel();
                    dbm = cellSignalStrengthGsm.getDbm();
                    tipo = "GSM";
                } else if (info instanceof CellInfoCdma) {
                    cellSignalStrengthCdma = ((CellInfoCdma) info).getCellSignalStrength();
                    torres = cellSignalStrengthCdma.getLevel();
                    dbm = cellSignalStrengthCdma.getDbm();
                    tipo = "CDMA";
                } else if (info instanceof CellInfoLte) {
                    cellSignalStrengthLte = ((CellInfoLte) info).getCellSignalStrength();
                    torres = cellSignalStrengthLte.getLevel();
                    dbm = cellSignalStrengthLte.getDbm();
                    tipo = "LTE";
                }else if (info instanceof CellInfoWcdma){
                    cellSignalStrengthwcdma= ((CellInfoWcdma) info).getCellSignalStrength();
                    torres = cellSignalStrengthwcdma.getLevel();
                    dbm = cellSignalStrengthwcdma.getDbm();
                    tipo = "WCDMA";
                } else {
                    throw new Exception("Unknown type of cell signal!");
                }
                Log.i("Cell Info", "Tipo: "+ tipo + ". Torres: " + torres + ". DBM: "+ dbm);
                Toast.makeText(getApplicationContext() ,"Tipo: "+ tipo + " - Torres: " + torres + " - DBM: "+ dbm, Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e("Find cell signal: ", "Unable to obtain cell signal information", e);
        }

        //Encontra dados de conexão
                                    /*cellinfowcdma = (CellInfoWcdma) telephonyManager.getAllCellInfo().get(0);
                                    cellSignalStrengthwcdma = cellinfowcdma.getCellSignalStrength();
                                    torres = cellSignalStrengthwcdma.getLevel();
                                    dbm = cellSignalStrengthwcdma.getDbm();
                                    Log.i("Cell Signal:", "tipo wcdma");
                                    Toast.makeText(getApplicationContext() ,"WCDMA!", Toast.LENGTH_SHORT).show();*/


        operadora = telephonyManager.getNetworkOperatorName();

        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            GsmCellLocation gsmLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            cid = gsmLocation.getCid();
            lac = gsmLocation.getLac();
            phoneType = " - GSM";
        } else if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            CdmaCellLocation cdmaCellLocation = (CdmaCellLocation) telephonyManager.getCellLocation();
            baseLat = cdmaCellLocation.getBaseStationLatitude();
            baseLng = cdmaCellLocation.getBaseStationLongitude();
            baseId = cdmaCellLocation.getBaseStationId();
            netWorkID = cdmaCellLocation.getNetworkId();
            phoneType = " - CDMA";
        } else {
            cid = 0;
            lac = 0;
            phoneType = " - Desconhecido";
        }

        if (telephonyManager.getNetworkOperator() != null) {
            mcc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(0, 3));
            mnc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(3));
        }

        if (telephonyManager.getNetworkOperator() != null) {
            mcc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(0, 3));
            mnc = Integer.parseInt(telephonyManager.getNetworkOperator().substring(3));
        }

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
        //mensagem de Log.
        Log.i("Called: ", "location changed. Lat: " + latitude + " lng: " + longitude);
        Toast.makeText(getApplicationContext() ,"Location Changed!", Toast.LENGTH_SHORT).show();
    }
}

