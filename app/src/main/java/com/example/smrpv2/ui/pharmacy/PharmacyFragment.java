package com.example.smrpv2.ui.pharmacy;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.smrpv2.R;
import com.example.smrpv2.common.location.LocationValue;

import com.example.smrpv2.model.pharmcy_model.PharmacyItem;
import com.example.smrpv2.model.pharmcy_model.PharmacyItems;

import com.example.smrpv2.retrofit.RetrofitHelper;
import com.example.smrpv2.retrofit.RetrofitService_pharmacy;

import net.daum.mf.map.api.MapCircle;
import net.daum.mf.map.api.MapPOIItem;
import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.smrpv2.model.pharmcy_model.Response_phy;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kakao.kakaonavi.KakaoNaviParams;
import com.kakao.kakaonavi.KakaoNaviService;
import com.kakao.kakaonavi.NaviOptions;
import com.kakao.kakaonavi.options.CoordType;
import com.kakao.kakaonavi.options.RpOption;
import com.kakao.kakaonavi.options.VehicleType;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PharmacyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PharmacyFragment extends Fragment implements MapView.MapViewEventListener, MapView.POIItemEventListener, MapView.CurrentLocationEventListener {

    private static String TAG ="TAG";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types of parameters
    private int radiuse = 500;

    // TODO: Rename and change types of parameters
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Double movelatititue = 0.0;
    private Double movelongitude = 0.0;
    // TODO: Rename and change types of parameters
    private FloatingActionButton locaton_Btn,reLocation_Btn;    // location_Btn: 내 위치 재 검색
                                                                // reLocation_Btn: 지도에 표시된 곳 재 검색

    // TODO: Rename and change types of parameters
    private List<PharmacyItem> list;
    private ArrayList<PharmacyItem> list_inform;
    // TODO: Rename and change types of parameters
    private MapViewSingleton mapViewSingleton = null;
    private LocationValue locationValue;
    private PharmacyItems pharmacyItems;
    private ViewGroup mapViewContainer = null;
    private View root = null;

    private MapView mapView;
    private MapCircle mapCircle;

    private PharmacyAdapter adapter; // 약국리스트에 대한 adapter
    private RetrofitService_pharmacy parsing;
    private MapPOIItem marker; //마커 표시를 위한 객체 선언
    private RecyclerView recyclerView;
    private LinearLayoutManager mlinearLayoutManager;
    public PharmacyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PharmacyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PharmacyFragment newInstance(String param1, String param2) {
        PharmacyFragment fragment = new PharmacyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_pharmacy, container, false);

        /** FloationActionButton 객체 할당 **/
        locaton_Btn = root.findViewById(R.id.floatingActionButton1);
        reLocation_Btn = root.findViewById(R.id.floatingActionButton2);

        /** ReCyclerView 부분 (시작)**/
        recyclerView = root.findViewById(R.id.recycle_view);
        mlinearLayoutManager = new LinearLayoutManager(root.getContext()); // layout 매니저 객체 선언
        recyclerView.setLayoutManager(mlinearLayoutManager);
        recyclerView.setHasFixedSize(true); //리싸이클 뷰 안 아이템들의 크기를 가변적으로 바꿀건지(false) , 일정한 크기를 사용할 것인지(true)
        /** ReCyclerView 부분 (끝)**/


        /** 데이터를 얻고자 하는 서버 주소값이 설정 되어있는 객체를 할당 **/
        parsing = RetrofitHelper.getPharmacy().create(RetrofitService_pharmacy.class);

        /// item간에 거리
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(0);
        recyclerView.addItemDecoration(spaceDecoration);

        createMapView(); //지도 객체 생성

        locationValue = new LocationValue(getActivity());
        locationValue.startMoule(); // 모듈을 이용하여 자신의 위치값을 가져온다.

        allocateLocation(locationValue); //위치변수에 값을 할당



        if(latitude != 0.0 && longitude != 0.0){
            Log.d(TAG, "latitude2: "+latitude);
            Log.d(TAG, "longitude2: "+longitude);
            setMapView(latitude,longitude);
            parsingData(latitude,longitude,radiuse);
        }else{
            locationValue.startMoule();
            allocateLocation(locationValue);
        }


        /** 검색된 약국 리스트를 담고 이 리스트를 adapter에 설정**/
        list_inform = new ArrayList<>();
        adapter = new PharmacyAdapter(list_inform);

        /** adapter 클릭 이벤트 처리(시작) **/
        adapter.setOnItemClickListener(new OnPharmacyItemClickListener() {
            @Override
            public void onItemClick(PharmacyAdapter.ViewHolder holder, View view, int position) {
                double lat = list.get(position).getLongitude();
                double lon = list.get(position).getLatitude();

                mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(lat, lon), true);
            }

            @Override
            public void onCallClick(int position) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+list.get(position).getTelNo()));
                //ACTION_DIAL: 전화 다이얼로그 Action_call:전화 연결
                //startActivity(intent);
                Toast.makeText(getActivity(),"통화 연결다이얼로그로 전환합니다.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onPath(int position) {
                if(KakaoNaviService.isKakaoNaviInstalled(getContext())){
                    Toast.makeText(getContext(),"카카오내비에 연결합니다.",Toast.LENGTH_SHORT).show();
                    com.kakao.kakaonavi.Location location = com.kakao.kakaonavi.Location.newBuilder(list.get(position).getAddr(),list.get(position).getLatitude(),
                            list.get(position).getLongitude()).build();
                    NaviOptions options = NaviOptions.newBuilder().setCoordType(CoordType.WGS84).setVehicleType(VehicleType.FIRST).setRpOption(RpOption.SHORTEST).build(); //setCoordType: 좌표계  setVehicleType: 차종  setRpOption: 경로 옵션
                    KakaoNaviParams parms = KakaoNaviParams.newBuilder(location).setNaviOptions(options).build();
                    KakaoNaviService.navigate(getActivity(),parms);
                }else{ //카카오 네비게이션 설치가 안되어 있을 경우
                    Toast.makeText(getContext(),"구글 스토어에 연결합니다.",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=com.locnall.KimGiSa"));
                    startActivity(intent);

                }

            }
        });

        /** adapter 클릭 이벤트 처리(끝) **/


        locaton_Btn.setOnClickListener(new View.OnClickListener() {// 현재 위치값 재 할당 후 주변 검색
            @Override
            public void onClick(View view) {


                locationValue.startMoule(); //모듈을 이용한 위치값 추출
                allocateLocation(locationValue); // 위치값을 변수에 재정의
                mapView.removeAllCircles();
                setMapView(latitude,longitude);
                parsingData(latitude,longitude,radiuse);
            }
        });

        reLocation_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(movelatititue != 0.0 && movelongitude != 0.0){
                    mapView.removeAllCircles();
                    setMapView(movelatititue,movelongitude);
                    parsingData(movelatititue,movelongitude,radiuse);
                }else{
                    Toast.makeText(getActivity(),"지도를 움직인 후 다시 클릭해 주세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return root;
    }

    private void createMapView() { //MapView 객체 선언과 이벤트 설정하는 클래스


        if (mapView == null) { //mapView 객체가 선언되어있지 않을경우
            mapViewSingleton = new MapViewSingleton(getContext()); //MapViewSingleton 클래스를 호출과 동시에 자신의 context값을 매개변수를 넘김
            mapView = mapViewSingleton.getMapview();// 생성한 mapView객체를 받음
        }

        if (mapViewContainer != null) {

            mapViewContainer.removeAllViews();
        }else{
            mapViewContainer = (ViewGroup) root.findViewById(R.id.phy_map_view); // mapViewContainer 선언
            mapViewContainer.addView(mapView);
        }


        /*btn_location.setVisibility(View.VISIBLE);
        btn_research.setVisibility(View.VISIBLE);*/
        mapView.setMapViewEventListener(this); //MapView의 Event 처리를 위함
        mapView.setPOIItemEventListener(this); // MapView의 marker 표시를 위함
        mapView.setCurrentLocationEventListener(this); // MapView의 현재위치 리스너


        setMapView(latitude, longitude);

    }

    private void setMapView(double latitude, double longitude){
        //하이브리드 맵 설정
        //mapView.setMapType(MapView.MapType.Hybrid); //Standard ,Statllite, Hybrid

        // 내 현재위치 원 그리기
        mapView.setCurrentLocationRadius(radiuse);
        //mapView.setHDMapTileEnabled(true);

        //중심적 변경
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude,longitude),true);// 중심점 변경
        mapCircle = new MapCircle(MapPoint.mapPointWithGeoCoord(latitude, longitude),radiuse, Color.argb(128,95,0,255),Color.argb(128,186,255,255));//MapCircle(MapPoint center, int radius, int strokeColor, int fillColor)
        mapCircle.setTag(2);
        mapView.addCircle(mapCircle);
        //줌 레벨 변경
        mapView.setZoomLevel(3,true);
        // 줌 인
        mapView.zoomIn(true);
        // 줌 아웃
        mapView.zoomOut(true);
        // 트랙
        //mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading); //트래킹 모드 on + 나침반 모드 on

        // 중심점에 Marker 로 표시해줍니다
        //CenterMarker(latitude, longitude);
        //Toast.makeText(getActivity().getApplicationContext(),"사용자 위치 반경 "+pharmacyViewModel.radius+"m 약국을 검색합니다.",Toast.LENGTH_LONG);
        //원 색상 변경
        /*mapView.setCurrentLocationRadiusFillColor(Color.argb(128,186,255,255));
        // 원 테두리 색상 적용
        mapView.setCurrentLocationRadiusStrokeColor(Color.argb(128,95,0,255));*/
    }//MapView의 인터페이스 설정 클래스

    public void allocateLocation(LocationValue locationValue){
        latitude = locationValue.getLatitude();
        longitude = locationValue.getLongitude();
    }

    private void parsingData(double latitude,double longitude,int radiuse){
        Log.d(TAG, "latitude: "+latitude);
        Log.d(TAG, "longitude: "+longitude);


        Call<Response_phy> call = parsing.getList(longitude,latitude,radiuse);

        call.enqueue(new Callback<Response_phy>() {
            @Override
            public void onResponse(Call<Response_phy> call, Response<Response_phy> response) {
                Log.d(TAG, "onResponse: "+response.body().getHeader().getResultMsg());
                if(response.isSuccessful()){
                    pharmacyItems = response.body().getBody().getItems();
                    list = pharmacyItems.getItemsList();
                    addMarker(list);

                }
            }

            @Override
            public void onFailure(Call<Response_phy> call, Throwable t) {
                Log.d(TAG, "Faill: FaillFaillFaillFaill");

            }
        });
    }
    private void addMarker(List<PharmacyItem> totalList) {
        this.list = totalList;
        list_inform.clear();
        recyclerView.setAdapter(adapter);
        mapView.removeAllPOIItems();

        Log.d(TAG, "totalList.size(): "+totalList.size());
        Toast.makeText(getActivity(),"총"+totalList.size()+"건이 검색되었습니다.",Toast.LENGTH_SHORT).show();
        for(int i = 0 ; i < list.size(); i++){
            marker= new MapPOIItem(); // 약국들을 mapview 에 표시하기 전에 marker를 생성함.
            marker.setItemName(list.get(i).getYadmNm()); //marker의 타이틀(제목)값을 부여
            marker.setTag(1);//MapView 객체에 등록된 POI Item들 중 특정 POI Item을 찾기 위한 식별자로 사용할 수 있음.

            marker.setMapPoint(MapPoint.mapPointWithGeoCoord(list.get(i).getLongitude(), list.get(i).getLatitude())); //mapview의 초점을 marker를 중심으로 함 latitude:127.0 longitude 37.0
            marker.setMarkerType(MapPOIItem.MarkerType.CustomImage); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.
            marker.setCustomImageResourceId(R.drawable.location_icon); //커스텀 icon 을 설정하기 위함
            //marker2.setMarkerType(MapPOIItem.MarkerType.BluePin); // 기본으로 제공하는 BluePin 마커 모양.
            //marker2.setSelectedMarkerType(MapPOIItem.MarkerType.RedPin); // 마커를 클릭했을때, 기본으로 제공하는 RedPin 마커 모양.

            marker.setCustomImageAutoscale(false);// hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이브러리의 스케일 기능을 꺼줌
            //marker2.setAlpha(0.2f);// marker 투명도
            mapView.addPOIItem(marker);//mapview위에 marker 띄우기
            list_inform.add(list.get(i));
            adapter.notifyDataSetChanged();
        }



    }


    //////////////////      MapView 객체 이벤트 처리  ////////////////////////////
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
        //단말 사용자가 길게 누른 후(long press) 끌어서(dragging) 위치 이동이 가능한 POI Item의 위치를 이동시킨 경우 호출된다
        /*GeoCoordinate geoCoordinate = mapView.getMapCenterPoint();
        double latitude = geoCoordinate.latitude; // 위도
        double longitude = geoCoordinate.longitude; // 경도*/


    }

    /** 지도 객체에 대한 메소드 **/
    /*
     *  현재 위치 업데이트(setCurrentLocationEventListener)
     */
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) { // Tracking 모드가 켜진경우 단말의 현위치 좌표값을 통보받을 수 있다.
        /*MapPoint.GeoCoordinate mPointGeo = mapPoint.getMapPointGeoCoord();
        longitude = mPointGeo.longitude;
        latitude = mPointGeo.latitude;*/
        //Toast.makeText(getActivity().getApplicationContext(),"현재 위치 좌표 업데이트 됨",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) { //단말의 방향(Heading) 각도값을 통보받을 수 있다.
        //Toast.makeText(getActivity().getApplicationContext(), "단말기의 방향 각도값:"+v , Toast.LENGTH_LONG).show();
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
        //Toast.makeText(getActivity().getApplicationContext(),"지도 중심 좌표 변경",Toast.LENGTH_LONG).show();
        movelatititue = mapPoint.getMapPointGeoCoord().latitude;
        movelongitude = mapPoint.getMapPointGeoCoord().longitude;
    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) { //지도의 레벨이

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
        //Toast.makeText(getContext().getApplicationContext(),"end of move",Toast.LENGTH_LONG).show();
    }
}