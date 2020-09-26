package com.example.smrpv2.ui.hospital;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smrpv2.R;
import com.example.smrpv2.ui.common.LocationValue;
import com.example.smrpv2.model.hospital_model.Response_hos;
import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_hospital;
import com.example.smrpv2.ui.pharmacy.RecyclerDecoration;
import com.example.smrpv2.model.hospital_model.Item;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.lang.Thread.sleep;

/**
 *
 * HospitalFragment : 병원 찾기
 * 아직 제대로 완성 x 다시 고칠 예정
 * 병원 목록 불러오는 서버 연결 x
 */

public class HospitalFragment extends Fragment implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    /**
     * int형 변수
     **/
    private int radiuse = 500;
    private int zoomLevel = 3;
    private int select_zoomLevel=1;//줌을 눌렀을 경우
    private int dealut_zoomLevel=3;//zoomlevel 디폴트값
    /**
     * Duble형 변수
     **/
    private double movelatititue = 0.0, movelongitude = 0.0;//지도 움직임 위치

    private double latitude = 0.0, longitude = 0.0; //사용자 위치
    /**
     * String형 변수
     **/
    private String dgsbjtCd = ""; //진료과목



    private RetrofitService_hospital retrofit;
    private LocationValue locationValue;
    private HospitalAdapter adapter;
    private PopupHospital popupHospital;
    private static HospitalFragment hospitalFragment;
    private View root;
    private ViewGroup mapViewContainer;

    private MapView mapView;
    private MapCircle mapCircle;
    private RecyclerView recyclerView;
    private List<Item> list;
    private ArrayList<Item> arr_list;

    private HashMap<String, String> hash_dgsbjtCd = new HashMap<>();
    private final String TAG = "TAG";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        /*GetLocation_Dialog getLocation_dialog = new GetLocation_Dialog();
        getLocation_dialog.execute();*/


        root = inflater.inflate(R.layout.fragment_hospital, container, false);
        /** 진료과목 선택 버튼 **/
        hospitalFragment = this;

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(getContext());



        /**Mapview객체 위 버튼 객체 선언**/
        FloatingActionButton location_fb = root.findViewById(R.id.mylocationtn);//내 위치
        FloatingActionButton research_fb = root.findViewById(R.id.relocationBtn);//재검색
        Button hos_plusBtn = root.findViewById(R.id.hos_plusBtn); //지도 확대 버튼
        Button hos_minusBtn = root.findViewById(R.id.hos_minusBtn); //지도 축소 버튼
        Button btn_dgsbjtCd = root.findViewById(R.id.dgsbjtCd);

        recyclerView = root.findViewById(R.id.recycle_view); //recyclerView 객체 선언
        LinearLayoutManager mlinearLayoutManager = new LinearLayoutManager(root.getContext()); // layout 매니저 객체 선언
        recyclerView.setLayoutManager(mlinearLayoutManager);
        recyclerView.setHasFixedSize(true); //리싸이클 뷰 안 아이템들의 크기를 가변적으로 바꿀건지(false) , 일정한 크기를 사용할 것인지(true)

        arr_list = new ArrayList<>();

        adapter = new HospitalAdapter(arr_list);


        RecyclerDecoration spaceDecoration = new RecyclerDecoration(0);
        recyclerView.addItemDecoration(spaceDecoration);


        locationValue = new LocationValue(getActivity());//위치 값을 가져오기 위해 현재 Activity값을 매개 인자로 넘겨준다
        locationValue.startMoule();//GPS나 네트워크를 통한 위치정보값을 가져온다
        allocateLocation(locationValue);// 모듈을 통해 위치정보값을 가져와서 변수값에 저장


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                createMapView();//지도 생성
            }
        },500);



        retrofit = RetrofitHelper.getHos().create(RetrofitService_hospital.class);


        adapter.setOnitemClickListener(new OnHospitalItemClickListener() {
            @Override
            public void onItemClick(HospitalAdapter.ViewHolder holder, View viewm, int position) {
                String lat = list.get(position).getXPos();
                String lon = list.get(position).getYPos();
                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(lon), Double.parseDouble(lat)), true);
                mapView.setZoomLevel(select_zoomLevel,true);
            }

            @Override
            public void onCallClick(int position) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + list.get(position).getTelno()));
                startActivity(intent);
                show("통화 연결다이얼로그로 전환합니다.");

            }


            @Override
            public void onPathClick(int position) {
                if (KakaoNaviService.isKakaoNaviInstalled(getContext())) {
                    show("카카오내비에 연결합니다.");
                    com.kakao.kakaonavi.Location location = com.kakao.kakaonavi.Location.newBuilder(list.get(position).getYadmNm(), Double.parseDouble(list.get(position).getXPos()), Double.parseDouble(list.get(position).getYPos())).build();
                    NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST).setRpOption(RpOption.SHORTEST).build();
                    KakaoNaviParams parms = KakaoNaviParams.newBuilder(location).setNaviOptions(options).build();
                    KakaoNaviService.navigate(getActivity(), parms);
                } else { //카카오 네비게이션 설치가 안되어 있을 경우
                    show("구글 스토어에 연결합니다.");
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.locnall.KimGiSa"));
                    startActivity(intent);

                }

            }
        });

        location_fb.setOnClickListener(new View.OnClickListener() { // 내 위치
            @Override
            public void onClick(View view) {
                movelongitude=0.0;
                movelatititue=0.0;
                locationValue.startMoule();
                allocateLocation(locationValue);
                mapView.removeAllCircles();
                setMapView(latitude,longitude);
                parsingData(latitude,longitude,radiuse,dgsbjtCd);

            }
        });
        research_fb.setOnClickListener(new View.OnClickListener() { //지도상 보이는 위치
            @Override
            public void onClick(View view) {

                if(movelatititue != 0.0 && movelongitude != 0.0){
                    mapView.removeAllCircles();
                    setMapView(movelatititue,movelongitude);
                    parsingData(movelatititue,movelongitude,radiuse,dgsbjtCd);
                }else{
                    show("지도를 움직인 후 다시 클릭해 주세요.");
                }
            }
        });

        hos_plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zoomLevel>0)
                    mapView.setZoomLevel(--zoomLevel,true);
                else
                    show("더 이상 축소할 수 없습니다.");
            }
        });

        hos_minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(zoomLevel<11)
                    mapView.setZoomLevel(++zoomLevel,true);
                else
                    show("더 이상 확대할 수 없습니다.");
            }
        });
        btn_dgsbjtCd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupHospital = PopupHospital.getInstance();
                popupHospital.show(getActivity().getSupportFragmentManager(),"진료과목선택");
            }
        });
        return root;
    }

    private void parsingData(double latitude, double longitude, int radiuse, final String dgsbjtCd) {
        /**
         *  병원정보서비스 API와 통신하는 메소드
         *
         **/

        final double lat = latitude;
        final double lng = longitude;
        final int rad = radiuse;
        final String dgsb = dgsbjtCd;
        Call<Response_hos> call = retrofit.getList(latitude, longitude, radiuse, dgsbjtCd);
        call.enqueue(new Callback<Response_hos>() {
            @Override
            public void onResponse(Call<Response_hos> call, Response<Response_hos> response) {
                Log.d(TAG, "onResponse.size(): " + response.body().getResponse().getBody().getItems().getItemsList().size());
                addMarker(response.body().getResponse().getBody().getItems().getItemsList());
            }

            @Override
            public void onFailure(Call<Response_hos> call, Throwable t) {
                show("잠시만 기다려 주세요.");
                parsingData(lat,lng,rad,dgsbjtCd);
            }
        });
    }

    private void addMarker(List<Item>list){
        this.list = list;
        arr_list.clear();
        recyclerView.setAdapter(adapter);
        mapView.removeAllPOIItems();
        int size = list.size();

        show("총 "+size+"건이 검색되었습니다.");
        MapPOIItem marker = new MapPOIItem();

        for(int i = 0; i < size;i++){
            marker.setItemName(list.get(i).getYadmNm()); //아이콘
            marker.setTag(1);
            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(Double.parseDouble(list.get(i).getYPos()),Double.parseDouble(list.get(i).getXPos())));
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage);
            marker.setCustomImageResourceId(R.drawable.location_icon);

            marker.setCustomImageAutoscale(false);
            mapView.addPOIItem(marker);
            arr_list.add(list.get(i));
            adapter.notifyDataSetChanged();
        }
    }

    private void createMapView() { //MapView 객체 선언과 이벤트 설정하는 클래스


        if (mapView == null)  //mapView 객체가 선언되어있지 않을경우
            mapView = new MapView(getContext());

        if (mapViewContainer != null)
            mapViewContainer.removeAllViews();
        else {
            mapViewContainer = (ViewGroup) root.findViewById(R.id.hos_map_view); // mapViewContainer 선언
            mapViewContainer.addView(mapView);
        }


        mapView.setMapViewEventListener(this); //MapView의 Event 처리를 위함
        mapView.setPOIItemEventListener(this); // MapView의 marker 표시를 위함
        mapView.setCurrentLocationEventListener(this); // MapView의 현재위치 리스너


        setMapView(latitude, longitude);

    }

    private void setMapView(double latitude, double longitude) { //MapView의 인터페이스 설정 클래스
        //중심적 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true);// 중심점 변경

        mapCircle = new MapCircle(MapPoint.mapPointWithGeoCoord(latitude, longitude),radiuse, Color.argb(128,95,0,255),Color.argb(128,186,255,255));//MapCircle(MapPoint center, int radius, int strokeColor, int fillColor)
        mapCircle.setTag(2);
        mapView.addCircle(mapCircle);

        //줌 레벨 변경
        mapView.setZoomLevel(dealut_zoomLevel, true);

        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);
        // 트랙
        //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOff); //트래킥 모등 on + 나침반 모드 on:TrackingModeOnWithoutHeading
        parsingData(latitude, longitude, radiuse, dgsbjtCd);
    }

    public void allocateLocation(LocationValue locationValue) {
        /**
         * 모듈을 통해 위치정보값을 가져와 해당 변수에 값을 저장
         **/
        latitude = locationValue.getLatitude(); //위도
        longitude = locationValue.getLongitude();//경도
    }

    protected void DgsbjtCd(String select_dgsbjtCd) {

        dgsbjtCd = getDgsbjtCd(select_dgsbjtCd);

        if (movelatititue == 0.0 && movelongitude == 0.0) { //지도를 움직이지 않았을때
            parsingData(latitude, longitude, radiuse, dgsbjtCd);
        } else { //지도를 움직였을때
            parsingData(movelatititue, movelongitude, radiuse, dgsbjtCd);

        }

    }

    private String getDgsbjtCd(String dgsbjtCd) {
        if (hash_dgsbjtCd.size()==0) {
            hash_dgsbjtCd = new HashMap<>();
            hash_dgsbjtCd.put("-전체-", "");
            hash_dgsbjtCd.put("가정의학과", "23");
            hash_dgsbjtCd.put("일반내과", "01");
            hash_dgsbjtCd.put("침구과", "85");
            hash_dgsbjtCd.put("외과", "04");
            hash_dgsbjtCd.put("성형외과", "08");
            hash_dgsbjtCd.put("소아과", "11");
            hash_dgsbjtCd.put("이비인후과", "13");
            hash_dgsbjtCd.put("안과", "12");
            hash_dgsbjtCd.put("정형외과", "05");
            hash_dgsbjtCd.put("정신건강의학과", "03");
            hash_dgsbjtCd.put("피부과", "14");
            hash_dgsbjtCd.put("치과", "49");
            hash_dgsbjtCd.put("응급실", "24");
            hash_dgsbjtCd.put("한의원", "80");
        }
        String result = hash_dgsbjtCd.get(dgsbjtCd);
        return result;
    }
    public void show(String s){
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
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
    protected static HospitalFragment getInstance(){
        return hospitalFragment;
    }
}
