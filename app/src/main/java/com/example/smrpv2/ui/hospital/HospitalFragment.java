package com.example.smrpv2.ui.hospital;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smrpv2.R;
import com.example.smrpv2.model.Hospital;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;
import com.kakao.kakaonavi.options.RpOption;
import com.kakao.kakaonavi.options.VehicleType;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Thread.sleep;

/**
 *
 * HospitalFragment : 병원 찾기
 * 아직 제대로 완성 x 다시 고칠 예정
 * 병원 목록 불러오는 서버 연결 x
 */

public class HospitalFragment extends Fragment implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    private View root;
    private static double latitude=0.0 ,longitude=0.0; //사용자 위치
    private double movelatititue=0.0, movelongitude=0.0;//지도 움직임 위치
    private Location location;
    private static MapView mapView;
    private ViewGroup mapViewContainer;
    private int radiuse=500;
    private MapPOIItem marker;
    private Hospital hospital;
    private ArrayList<String> total_hos = new ArrayList<String>();
    private static ArrayList<Hospital> list;

    private static HospitalAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager mlinearLayoutManager;
    private Handler handler;
    private int count=0, i=1;
    private FloatingActionButton location_fb,research_fb;
    private Dialog dialog;
    private boolean boolean_start=false;
    private MapCircle mapCircle;
    private Spinner dgsbjtCd_spinner, radiuse_spinner;
    private ArrayAdapter dgsbjtCd_adapter, radiuse_adapter;
    private String dgsbjtCd= ""; //진료과목
    private HashMap<String, String> hash_dgsbjtCd;
    private Button btn_dgsbjtCd;
    private boolean bool_start = false, bool_restart=false;
    private static PopupFragment_Hos popupFragment_hos;
    private static HospitalFragment hospitalFragment;
    public Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        GetLocation_Dialog getLocation_dialog = new GetLocation_Dialog();
        getLocation_dialog.execute();
        root = inflater.inflate(R.layout.fragment_hospital, container, false);

        hospitalFragment = this;
        context = getContext();
        btn_dgsbjtCd = root.findViewById(R.id.select_dgsbjtCd);

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());
        startLocationService();

        recyclerView = root.findViewById(R.id.recycle_view); //recyclerView 객체 선언
        location_fb = root.findViewById(R.id.floatingActionButton1);//내 위치
        research_fb = root.findViewById(R.id.floatingActionButton2);//재검색
        mlinearLayoutManager = new LinearLayoutManager(root.getContext()); // layout 매니저 객체 선언
        recyclerView.setLayoutManager(mlinearLayoutManager);
        recyclerView.setHasFixedSize(true); //리싸이클 뷰 안 아이템들의 크기를 가변적으로 바꿀건지(false) , 일정한 크기를 사용할 것인지(true)

        list = new ArrayList<>();

        adapter = new HospitalAdapter(list);
        recyclerView.setAdapter(adapter);


       // RecyclerDecoration spaceDecoration = new RecyclerDecoration(0);
       // recyclerView.addItemDecoration(spaceDecoration);


        btn_dgsbjtCd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupFragment_hos = PopupFragment_Hos.getInstance();
                popupFragment_hos.show(getActivity().getSupportFragmentManager(),"진료과목 선택");

            }
        });
        location_fb.setOnClickListener(new View.OnClickListener() { //내위치 주변 병원들을 찾기위함 / 내위치 버튼을 눌렀을경우
            @Override
            public void onClick(View v) {

                HospitalFragment.Dialog dialog = new HospitalFragment.Dialog();
                dialog.execute();
                mapView.removeAllPOIItems(); //mapview 의 marker 표시를 모두 지움(새로운 marker를 최신화 하기 위해)
                list.clear();
                mapView.removeAllCircles();
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);
                mapView.setCurrentLocationRadius(radiuse);
                mapCircle = new MapCircle(MapPoint.mapPointWithGeoCoord(latitude, longitude),radiuse, Color.argb(50,102,102,153), Color.argb(70,186,255,255));
                mapCircle.setTag(2);
                mapView.addCircle(mapCircle);
                mapView.setZoomLevel(3,true);
                parsingData(latitude,longitude,radiuse,dgsbjtCd);

            }
        });
        research_fb.setOnClickListener(new View.OnClickListener() { //재검색 버튼을 눌러 지도의 중심을 기준으로 병원을 검색
            @Override
            public void onClick(View v) {

                if(boolean_start){
                    bool_restart = false;
                    dialog = new HospitalFragment.Dialog();
                    dialog.execute();
                    mapCircle = new MapCircle(MapPoint.mapPointWithGeoCoord(movelatititue, movelongitude),radiuse, Color.argb(50,102,102,153), Color.argb(70,186,255,255));//MapCircle(MapPoint center, int radius, int strokeColor, int fillColor)
                    mapCircle.setTag(2);
                    mapView.removeAllCircles();
                    mapView.addCircle(mapCircle);
                    mapView.removeAllPOIItems(); //mapview 의 marker 표시를 모두 지움(새로운 marker를 최신화 하기 위해)
                    list.clear();
                    mapView.setCurrentLocationRadius(radiuse);
                    parsingData(movelatititue, movelongitude, radiuse,dgsbjtCd);
                }
            }
        });
        adapter.setOnitemClickListener(new HospitalAdapter.OnHospitalItemClickListener() {
            @Override
            public void onItemClick(HospitalAdapter.ViewHolder holder, View viewm, int position) {
                String lat = list.get(position).getxPos();
                String lon = list.get(position).getyPos();
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(lon), Double.parseDouble(lat)), true);
            }

            @Override
            public void onCallClick(int position) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+list.get(position).getTelno()));
                startActivity(intent);
                Toast.makeText(getActivity(),"통화 연결다이얼로그로 전환합니다.", Toast.LENGTH_LONG).show();
            }


            @Override
            public void onPath(int position) {
                if(KakaoNaviService.isKakaoNaviInstalled(getContext())){
                    Toast.makeText(getContext(),"카카오내비에 연결합니다.", Toast.LENGTH_SHORT).show();
                    com.kakao.kakaonavi.Location location = com.kakao.kakaonavi.Location.newBuilder(list.get(position).getYadmNm(), Double.parseDouble(list.get(position).getxPos()), Double.parseDouble(list.get(position).getyPos())).build();
                    NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST).setRpOption(RpOption.SHORTEST).build();
                    KakaoNaviParams parms = KakaoNaviParams.newBuilder(location).setNaviOptions(options).build();
                    KakaoNaviService.navigate(getActivity(),parms);
                }else{ //카카오 네비게이션 설치가 안되어 있을 경우
                    Toast.makeText(getContext(),"구글 스토어에 연결합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.locnall.KimGiSa"));
                    startActivity(intent);

                }

            }
        });

        parsingData(latitude,longitude,radiuse,dgsbjtCd);
        return root;
    }

    private void parsingData(final double latitude, final double longitude, final int radiuse,final String dgsbjtCd){
        /**
         * t서버. 나중에 추가할 거임
         */
    }

   @Override
    public void onResume() {
        super.onResume();

    }
    @Override
    public void onPause() {
        super.onPause();
    }

    private void createMapView(){
        if(mapView == null) {
            mapView = new MapView(getContext());
        }
        if(mapViewContainer!=null){

            mapViewContainer.removeAllViews();
        }
        i++;
        mapViewContainer = (ViewGroup)root.findViewById(R.id.hos_map_view); // mapViewContainer 선언
        mapViewContainer.addView(mapView);
        mapView.setMapViewEventListener(this); //MapView의 Event 처리를 위함
        mapView.setPOIItemEventListener(this); // MapView의 marker 표시를 위함
        mapView.setCurrentLocationEventListener(this); // MapView의 현재위치 리스너

        setMapView(latitude, longitude);
    }
    private void setMapView(double latitude, double longitude){ //MapView의 인터페이스 설정 클래스

        //중심적 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude),true);// 중심점 변경

        mapCircle = new MapCircle(MapPoint.mapPointWithGeoCoord(latitude, longitude),radiuse, Color.argb(50,102,102,153), Color.argb(70,186,255,255));//MapCircle(MapPoint center, int radius, int strokeColor, int fillColor)
        mapCircle.setTag(2);
        mapView.addCircle(mapCircle);

        //줌 레벨 변경
        mapView.setZoomLevel(3,true);

        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);
        // 트랙
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff); //트래킥 모등 on + 나침반 모드 on:TrackingModeOnWithoutHeading

    }


    private void addMarker(String yadmNm, String clCdNm, String addr, String hosurl, String telno, String xPos, String yPos, double distance){
        //mapView.removeAllPOIItems(); //mapview 의 marker 표시를 모두 지움(새로운 marker를 최신화 하기 위해)
        total_hos.clear(); //ArrayList total_hos 의 모든 값을 clear()


        marker= new MapPOIItem(); // 약국들을 mapview 에 표시하기 전에 marker를 생성함.
        marker.setItemName(yadmNm); //marker의 타이틀(제목)값을 부여
        marker.setTag(1);//MapView 객체에 등록된 POI Item들 중 특정 POI Item을 찾기 위한 식별자로 사용할 수 있음.

        marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(yPos), Double.parseDouble(xPos))); //mapview의 초점을 marker를 중심으로 함 latitude:127.0 longitude:37.0
        marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
        marker.setCustomImageResourceId(R.drawable.location_icon); //커스텀 icon 을 설정하기 위함
        marker.setCustomImageAutoscale(false);// hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌
        //marker2.setAlpha(0.2f);// marker 투명도
        mapView.addPOIItem(marker);//mapview위에 marker 띄우기

        if(addr.contains("("))
            addr = addr.substring(0, addr.indexOf("("));

       /* if(hosurl==null)
            hosurl="병원 사이트 없음.";*/
        hospital= new Hospital(yadmNm,clCdNm,addr,/*hosurl,*/telno,xPos,yPos,distance);
        list.add(hospital);

        //Collections.sort(list);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {//사용자가 MapView 에 등록된 POI Item 아이콘(마커)를 터치한 경우 호출된다.

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) { //사용하지 않은 메소드

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {//클릭한 Balloon의 정보를 가져온다.

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
    /*f
     *  현재 위치 업데이트(setCurrentLocationEventListener)
     */
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) { // Tracking 모드가 켜진경우 단말의 현위치 좌표값을 통보받을 수 있다.


    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) { //단말의 방향(Heading) 각도값을 통보받을 수 있다.

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) { //현위치 갱신 작업에 실패한 경우 호출된다.

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {//현위치 트랙킹 기능이 사용자에 의해 취소된 경우 호출된다.
        //처음 현위치를 찾는 동안에 현위치를 찾는 중이라는 Alert Dialog 인터페이스가 사용자에게 노출된다.
        //첫 현위치를 찾기전에 사용자가 취소 버튼을 누른 경우 호출 된다.
    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) { //지도 중심 좌표가 이동한 경우 호출 됨.
        movelatititue = mapPoint.getMapPointGeoCoord().latitude;
        movelongitude = mapPoint.getMapPointGeoCoord().longitude;
        boolean_start=true;
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) { //지도의 레벨이 변경되었을때 호출

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) { //사용자가 지도 위 한지점을 터치 한 경우 호출

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) { //사용자가 지도 위 한지점을 연속으로 두번 터치 한 경우 호출

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) { //사용자가 지도 한 지점을 길게 누른경우

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) { //사용자가 지도 드래그를 시작한 경우

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) { //사용자가 지도 드래그를 끝낸 경우
    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) { // 지도의 이동이 완료된 경우


    }


    ///////////////////////////  GPS 관련 클래스 ////////////////////////////////
    private void startLocationService(){ //사용자의 위치 좌표를 가져오기 위한 클래스
        LocationManager locationManager1 = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);//위치관리자 생성
        LocationManager locationManager2 = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);//위치관리자 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 1000;//단위 위치정보를 초기화 하기 위한 기준 설정 (millisecond , m)
        float minDistance = 10;//단위 m
        try {
            locationManager1.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener); //// 위치 기반을 GPS모듈을 이용함
            locationManager2.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance,gpsListener);//// 위치 기반을 네트워크 모듈을 이용함
            //5초 마다 or 10m 이동할떄마다 업데이트   network는 gps에 비해 정확도가 떨어짐
            location = locationManager2.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);///네트워크로 얻은 마지막 위치좌표를 이용
            if (location != null) {
                latitude = location.getLatitude(); // GPS 모듈 경도 값 ex) 37.30616958190577
                longitude = location.getLongitude(); //GPS 모듈 위도 값 ex) 127.92099856059595
                location=null;
            }else{
                location = locationManager1.getLastKnownLocation(LocationManager.GPS_PROVIDER);//최근 gps기록  실내에서는 안잡힐수가 있다 따라서 최근 GPS기록을 제공할 수 도 있다.
                latitude = location.getLatitude(); //네트워크 경도 값
                longitude = location.getLongitude(); // 네트워크 위도 값
                location=null;
            }
        } catch (SecurityException e) { //보안적인 예외처리 발생시 실행
            e.printStackTrace();
        }
    }
    protected void DgsbjtCd(String select_dgsbjtCd){

        dgsbjtCd = getDgsbjtCd(select_dgsbjtCd);
        mapView.removeAllCircles();
        mapView.removeAllPOIItems(); //mapview 의 marker 표시를 모두 지움(새로운 marker를 최신화 하기 위해)

        HospitalFragment.Dialog dialog = new HospitalFragment.Dialog();
        dialog.execute();

        list.clear();

        if(movelatititue==0.0 && movelongitude ==0.0){ //지도를 움직이지 않았을때
            mapCircle = new MapCircle(MapPoint.mapPointWithGeoCoord(latitude, longitude),radiuse, Color.argb(50,102,102,153), Color.argb(70,186,255,255));//MapCircle(MapPoint center, int radius, int strokeColor, int fillColor)
            mapCircle.setTag(2);
            mapView.addCircle(mapCircle);
            parsingData(latitude,longitude,radiuse,dgsbjtCd);
        }else { //지도를 움직였을때
            mapCircle = new MapCircle(MapPoint.mapPointWithGeoCoord(movelatititue, movelongitude),radiuse, Color.argb(50,102,102,153), Color.argb(70,186,255,255));//MapCircle(MapPoint center, int radius, int strokeColor, int fillColor)
            mapCircle.setTag(2);
            mapView.addCircle(mapCircle);
            parsingData(movelatititue, movelongitude, radiuse, dgsbjtCd);

        }

    }
    private String getDgsbjtCd(String dgsbjtCd){
        if(hash_dgsbjtCd==null){
            hash_dgsbjtCd = new HashMap<>();
            hash_dgsbjtCd.put("-전체-","");
            hash_dgsbjtCd.put("가정의학과","23");hash_dgsbjtCd.put("내과","01");hash_dgsbjtCd.put("외과","04");
            hash_dgsbjtCd.put("성형외과","08");hash_dgsbjtCd.put("소아과","11");hash_dgsbjtCd.put("이비인후과","13");
            hash_dgsbjtCd.put("안과","12");hash_dgsbjtCd.put("정형외과","05");hash_dgsbjtCd.put("정신건강의학과","03");
            hash_dgsbjtCd.put("피부과","14");hash_dgsbjtCd.put("치과","49");hash_dgsbjtCd.put("응급실","24");
            hash_dgsbjtCd.put("한의원","80");
        }
        String result=  hash_dgsbjtCd.get(dgsbjtCd);
        return result;
    }
    protected static HospitalFragment getInstance(){

            return hospitalFragment;
    }
    private class GPSListener implements LocationListener {//위치리너스 클래스

        @Override
        public void onLocationChanged(Location location) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }//위치 공급자의 상태가 바뀔 때 호출

        @Override
        public void onProviderEnabled(String provider) {

        } //위치 공급자가 사용 가능해질(enabled) 때 호출

        @Override
        public void onProviderDisabled(String provider) {

        }//위치 공급자가 사용 불가능해질(disabled) 때 호출
    }


    private class Dialog extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {

            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("로딩중입니다..");

            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
           try {
                sleep(2500); // 2초 지속

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           bool_restart = false;

            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();
            super.onPostExecute(result);
        }
    }

    private class GetLocation_Dialog extends AsyncTask<Void, Void, Void> {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("GPS정보를 가져오고 있습니다. \n잠시만 기다려 주세요.");

            progressDialog.show();
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                sleep(2500); // 2초 지속

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressDialog.dismiss();

            super.onPostExecute(result);
        }
    }


}