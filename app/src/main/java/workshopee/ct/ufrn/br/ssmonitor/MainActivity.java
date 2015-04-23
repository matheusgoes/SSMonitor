package workshopee.ct.ufrn.br.ssmonitor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    final static String QUIT = "quit";
    Phone cell = new Phone();
    Database_Acesso database_acesso;
    NavigationDrawerFragment mNavigationDrawerFragment;
    CharSequence mTitle;
    Notification n;
    LocationManager locationmanager;
    CellSignalStrengthWcdma cellSignalStrengthwcdma;
    CellSignalStrengthCdma cellSignalStrengthCdma;
    CellSignalStrengthGsm cellSignalStrengthGsm;
    CellSignalStrengthLte cellSignalStrengthLte;
    TelephonyManager telephonyManager;
    Criteria criteria;
    Location location;
    ActionBar actionBar;
    FragmentManager fragmentManager;
    NotificationManager mNotificationManager;
    LocationListener locationListener;
    GPSTracker gpsTracker;
    DrawerLayout drawerLayout;
    Bundle bundle;
    Menu _menu;
    String provider;
    Location lastLocation;
    boolean notifications_actived = true;
    int fragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (n != null)
            mNotificationManager.cancelAll();

        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        database_acesso = new Database_Acesso(getApplicationContext());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        if (telephonyManager != null) {
            if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if ((locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
                    setContentView(R.layout.activity_main);

                    //Cria menu de navegação
                    mNavigationDrawerFragment = (NavigationDrawerFragment)
                            getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
                    mTitle = getTitle();

                    fragmentId = R.id.navigation_drawer;
                    drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
                    // Configura menu de navegação
                    mNavigationDrawerFragment.setUp(
                            fragmentId,
                            drawerLayout, this);


                    //Inicializa Localização
                    criteria = new Criteria();
                    criteria.setAccuracy(Criteria.ACCURACY_FINE);
                    provider = locationmanager.getBestProvider(criteria, true);
                    if (locationmanager.getLastKnownLocation(provider) != null) {
                        location = locationmanager.getLastKnownLocation(provider);
                        cell.setLatitude(location.getLatitude());
                        cell.setLongitude(location.getLongitude());
                    } else {
                        gpsTracker = new GPSTracker(this);
                        location = gpsTracker.getLocation();
                        cell.setLatitude(gpsTracker.getLatitude());
                        cell.setLongitude(gpsTracker.getLongitude());
                    }

                    getInfo(location);
                    locationListener = new LocationListener() {
                        @Override
                        public void onLocationChanged(Location _location) {
                            getInfo(_location);
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
                } else {
                    setContentView(R.layout.activity_location_services_off);
                }
            } else {
                setContentView(R.layout.semsim);
            }
        } else {
            setContentView(R.layout.semsim);
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();
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
        actionBar = getSupportActionBar();
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
                _menu = menu;
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
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(1000))
                        .commit();
                return true;
            case R.id.action_close:
                quit();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mNotificationManager.cancelAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (n != null){
            mNotificationManager.cancelAll();
        }
        Intent intent = getIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getAction().equals(QUIT)){
            quit();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (notifications_actived){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setAction(QUIT);
            Log.i("OnStop()", "Intent action: " + intent.getAction());
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);

            n  = new Notification.Builder(this)
                    .setContentTitle("Ainda estamos aqui!")
                    .setContentText("Clique para encerrar")
                    .setSmallIcon(R.drawable.ic_notif)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.abc_ic_clear_mtrl_alpha, "Finalizar", pIntent).build();

            mNotificationManager.notify(0, n);
        }
    }

    public void quit(){
        if (locationmanager!=null){
            locationmanager.removeUpdates(locationListener);
            Log.i("App"," Updates removed.");
        }
        if (n != null){
            mNotificationManager.cancel(0);
            Log.i("App"," Notification canceled");

        }
        gpsTracker = null;
        locationmanager=null;
        location = null;
        telephonyManager = null;
        finish();
        Log.i("App"," FINISHING");
    }

    public void getInfo(Location location){
        //Obtem novos dados de localização
        cell.setLatitude(location.getLatitude());
        cell.setLongitude(location.getLongitude());

        String tipo;
        try {
            CellInfo info = telephonyManager.getAllCellInfo().get(0);
            if (info instanceof CellInfoGsm) {
                cellSignalStrengthGsm = ((CellInfoGsm) info).getCellSignalStrength();
                cell.setTorres(cellSignalStrengthGsm.getLevel());
                cell.setDbm(cellSignalStrengthGsm.getDbm());
                tipo = "GSM";
            } else if (info instanceof CellInfoCdma) {
                cellSignalStrengthCdma = ((CellInfoCdma) info).getCellSignalStrength();
                cell.setTorres(cellSignalStrengthCdma.getLevel());
                cell.setDbm(cellSignalStrengthCdma.getDbm());
                tipo = "CDMA";
            } else if (info instanceof CellInfoLte) {
                cellSignalStrengthLte = ((CellInfoLte) info).getCellSignalStrength();
                cell.setTorres(cellSignalStrengthLte.getLevel());
                cell.setDbm(cellSignalStrengthLte.getDbm());
                tipo = "LTE";
            }else if (info instanceof CellInfoWcdma){
                cellSignalStrengthwcdma= ((CellInfoWcdma) info).getCellSignalStrength();
                cell.setTorres(cellSignalStrengthwcdma.getLevel());
                cell.setDbm(cellSignalStrengthwcdma.getDbm());
                tipo = "WCDMA";
            } else {
                tipo = "Desconhecido";
            }
            Log.i("Cell Info", "Tipo: " + tipo + ". Torres: " + cell.getTorres() + ". DBM: " + cell.getDbm());
        }catch (Exception e) {
            Log.e("CELL INFO: ", "Unable to obtain cell signal information", e);
        }

        cell.setOperadora(telephonyManager.getNetworkOperatorName());

        if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
            GsmCellLocation gsmLocation = (GsmCellLocation) telephonyManager.getCellLocation();
            cell.setCid(gsmLocation.getCid());
            cell.setLac(gsmLocation.getLac());

            cell.setPhoneType("GSM");
        } else if (telephonyManager.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
            cell.setPhoneType("CDMA");
            Log.i("Cell type", "CDMA");
        } else {
            cell.setCid(0);
            cell.setLac(0);
            cell.setPhoneType("Desconhecido");
        }

        if (telephonyManager.getNetworkOperator() != null) {
            cell.setMcc(Integer.parseInt(telephonyManager.getNetworkOperator().substring(0, 3)));
            cell.setMnc(Integer.parseInt(telephonyManager.getNetworkOperator().substring(3)));
        }

        cell.setNetworkTypeCode(telephonyManager.getNetworkType());

        switch (cell.getNetworkTypeCode()) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
                cell.setNetWorkType("GPRS - 2G");
                break;
            case TelephonyManager.NETWORK_TYPE_EDGE:
                cell.setNetWorkType("EDGE - 2G");
                break;
            case TelephonyManager.NETWORK_TYPE_CDMA:
                cell.setNetWorkType("CDMA - 2G");
                break;
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                cell.setNetWorkType("1xRTT - 2G");
                break;
            case TelephonyManager.NETWORK_TYPE_IDEN:
                cell.setNetWorkType("IDEN - 2G");
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
                cell.setNetWorkType("UMTS - 3G");
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                cell.setNetWorkType("EVDO_0 - 3G");
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                cell.setNetWorkType("EVDO_A - 3G");
                break;
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                cell.setNetWorkType("HSDPA - 3G");
                break;
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                cell.setNetWorkType("HSUPA - 3G");
                break;
            case TelephonyManager.NETWORK_TYPE_HSPA:
                cell.setNetWorkType("HSPA - 3G");
                break;
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                cell.setNetWorkType("EVDO_B - 3G");
                break;
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                cell.setNetWorkType("EHRPD - 3G");
                break;
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                cell.setNetWorkType("HSPAP - 3G");
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                cell.setNetWorkType("LTE - 4G");
                break;
            default:
                cell.setNetWorkType("Desconhecido");
        }
        //mensagem de Log.
        Log.i("Called: ", "location changed. Lat: " + cell.getLatitude() + " lng: " + cell.getLongitude());
        database_acesso.inserir_phone(cell);
        lastLocation = location;
    }
}

