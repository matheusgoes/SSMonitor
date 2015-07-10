package goes.com.br.ssmonitor;

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
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    final static String QUIT = "quit";
    public Phone cell = new Phone();
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
    MyPhoneStateListener MyListener;
    //private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Se existir alguma notificação, remova.
        if (n != null)
            mNotificationManager.cancelAll();
/*        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("ca-app-pub-3940256099942544/1033173712")
                .build();

        mInterstitialAd.loadAd(adRequest);*/
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        //Cria banco de dados
        database_acesso = new Database_Acesso(getApplicationContext());
        //Cria objeto de notificações
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //Cria objeto de gerenciamento de recursos do celular
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        MyListener   = new MyPhoneStateListener();
        telephonyManager.listen(MyListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        //Se o comando anterior tiver sido bem sucedido, prossiga...
        if (telephonyManager != null) {
            //Se o acesso ao SIM estiver disponível, prossiga...
            if (telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY) {
                //Obtem objeto para acessar localização
                locationmanager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                //Se os serviços de localização estiverem disponíveis, prossiga...
                if ((locationmanager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))) {
                    //Define ActivityMain como tela principal.
                    setContentView(R.layout.activity_main);

                    //Cria menu de navegação e action bar
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
                    //seleciona o melhor provedor de localização
                    provider = locationmanager.getBestProvider(criteria, true);
                    //Se houver ultima localização conhecida, prossiga
                    if (locationmanager.getLastKnownLocation(provider) != null) {
                        //Obtem ultima localização
                        location = locationmanager.getLastKnownLocation(provider);
                        //Obtem latitude
                        cell.setLatitude(location.getLatitude());
                        //Obtem longitude
                        cell.setLongitude(location.getLongitude());
                    // senão, obtenha usando a classe GPSTracker
                    } else {
                        gpsTracker = new GPSTracker(this);
                        //Obtem ultima localização
                        location = gpsTracker.getLocation();
                        //Obtem latitude
                        cell.setLatitude(gpsTracker.getLatitude());
                        //Obtem longitude
                        cell.setLongitude(gpsTracker.getLongitude());
                    }
                    //Executa função de obtenção de informações
                    getInfo(location);
                    //Define listener para variações de localização
                    locationListener = new LocationListener() {
                        //Quando houver mudança na localização, executar novamente função de coleta de dados
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

                    //Define atualizações de localização
                    locationmanager.requestLocationUpdates(provider, 1000, 1, locationListener);

                //senão, mostrar tela "Sem serviços de localização"
                } else {
                    setContentView(R.layout.activity_location_services_off);
                }
            //senão, mostrar tela "Sem sim"
            } else {
                setContentView(R.layout.semsim);
            }
        //senão, mostrar tela "Sem sim"
        } else {
            setContentView(R.layout.semsim);
        }
    }
    //Funcão para clique nos itens da lista de navegação
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // Trocar fragmento do content view principal
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position))
                .commit();
    }


    //Troca titulo do action bar de acordo com o item clicado na menu de navegação
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
    //Atualização action bar com o novo titulo obtido na função onSectionAttached
    public void restoreActionBar() {
        actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#19A3A3")));
        actionBar.setTitle(mTitle);
    }

    //Função de criação do menu de navegação
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (mNavigationDrawerFragment!=null){
            if (!mNavigationDrawerFragment.isDrawerOpen()) {
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
        mNotificationManager.cancelAll(); // Ao destruir aplicação, remover todas as notificações
    }

    @Override
    protected void onResume() {
        //Ao resumir, remover notificações e obter novas intents
        super.onResume();
        if (n != null){
            mNotificationManager.cancelAll();
        }
        Intent intent = getIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Se houver nova intent com action igual a QUIT, executar função quit()
        if (intent.getAction().equals(QUIT)){
            quit();
        }
    }

    //Ao perder o foco, configurar notificações
    @Override
    protected void onStop() {
        super.onStop();
        //Verifica se as notificações estão ativas (Tela de configurações)
        if (notifications_actived){
            //Define intent da notificação
            Intent intent = new Intent(this, MainActivity.class);
            //Define acão da intent
            intent.setAction(QUIT);
            Log.i("OnStop()", "Intent action: " + intent.getAction());
            PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
            //Constrói notificação
            n  = new Notification.Builder(this)
                    .setContentTitle("Ainda estamos aqui!")
                    .setContentText("Clique para encerrar")
                    .setSmallIcon(R.drawable.ic_notif)
                    .setContentIntent(pIntent)
                    .setAutoCancel(true)
                    .addAction(R.drawable.abc_ic_clear_mtrl_alpha, "Finalizar", pIntent).build();
            //Exibe notificação
            mNotificationManager.notify(0, n);
        }
    }

    public void quit(){
        //Destroi objetos inicializados para evitar que eles continuem executando em segundo plano
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
        //Fecha activity
        finish();
        Log.i("App"," FINISHING");
    }

    public void getInfo(Location location){
        //Obtem novos dados de localização
        cell.setLatitude(location.getLatitude());
        cell.setLongitude(location.getLongitude());
        /*
        //Obtem tipo de conexão
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
        }*/
        //Obtem nome da operadora e informações de rede enquanto constroi objeto cell (Phone)
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
        //insere novo objeto no banco de dados
        database_acesso.inserir_phone(cell);
        lastLocation = location;
    }

    private class MyPhoneStateListener extends PhoneStateListener {
        /* Get the Signal strength from the provider, each tiome there is an update */
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength){
            super.onSignalStrengthsChanged(signalStrength);
            int SignalStrength_ASU =  signalStrength.getGsmSignalStrength();
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99) {
                    int levelDbm, level;
                    int SignalStrength_dBm = SignalStrength_ASU * 2 - 113;
                    if (SignalStrength_dBm >= -75) levelDbm = 4;
                    else if (SignalStrength_dBm >= -85) levelDbm = 3;
                    else if (SignalStrength_dBm >= -95) levelDbm = 2;
                    else if (SignalStrength_dBm >= -100) levelDbm = 1;
                    else levelDbm = 0;

                    if (SignalStrength_ASU < 2 || SignalStrength_ASU==99) level = 0;
                    else if (SignalStrength_ASU >= 12) level = 4;
                    else if (SignalStrength_ASU >= 8) level = 3;
                    else if (SignalStrength_ASU >= 5) level = 2;
                    else level = 1;

                    cell.setTorres(level);
                    cell.setDbm(SignalStrength_dBm);
                    cell.setASU(SignalStrength_ASU);

                }else
                    SignalStrength_ASU = signalStrength.getGsmSignalStrength();
            } else {
                final int snr = signalStrength.getEvdoSnr();
                final int cdmaDbm = signalStrength.getCdmaDbm();
                final int cdmaEcio = signalStrength.getCdmaEcio();
                int levelDbm;
                int levelEcio;
                int level = 0;

                if (snr == -1) {
                    Log.i("cdmaDbm ", String.valueOf(cdmaDbm));

                    if (cdmaDbm >= -75) levelDbm = 4;
                    else if (cdmaDbm >= -85) levelDbm = 3;
                    else if (cdmaDbm >= -95) levelDbm = 2;
                    else if (cdmaDbm >= -100) levelDbm = 1;
                    else levelDbm = 0;

                    // Ec/Io are in dB*10
                    if (cdmaEcio >= -90) levelEcio = 4;
                    else if (cdmaEcio >= -110) levelEcio = 3;
                    else if (cdmaEcio >= -130) levelEcio = 2;
                    else if (cdmaEcio >= -150) levelEcio = 1;
                    else levelEcio = 0;

                    level = (levelDbm < levelEcio) ? levelDbm : levelEcio;
                } else {
                    if (snr == 7 || snr == 8) level =4;
                    else if (snr == 5 || snr == 6 ) level =3;
                    else if (snr == 3 || snr == 4) level = 2;
                    else if (snr ==1 || snr ==2) level =1;

                }
                Log.i("Bars ", String.valueOf(level));
            }

        }
    }
}

