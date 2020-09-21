package com.example.smrpv2.ui.pharmacy;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    // TODO: Rename and change types of parameters
    private List<PharmacyItem> list;
    private ArrayList<PharmacyItem> list_inform;
    // TODO: Rename and change types of parameters
    private MapViewSingleton mapViewSingleton = null;
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
        recyclerView = root.findViewById(R.id.recycle_view);
        mlinearLayoutManager = new LinearLayoutManager(root.getContext()); // layout 매니저 객체 선언
        recyclerView.setLayoutManager(mlinearLayoutManager);
        recyclerView.setHasFixedSize(true); //리싸이클 뷰 안 아이템들의 크기를 가변적으로 바꿀건지(false) , 일정한 크기를 사용할 것인지(true)



        parsing = RetrofitHelper.getPharmacy().create(RetrofitService_pharmacy.class);

        /// item간에 거리
        RecyclerDecoration spaceDecoration = new RecyclerDecoration(0);
        recyclerView.addItemDecoration(spaceDecoration);

        createMapView();

        LocationValue locationValue = new LocationValue(getActivity());
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

        adapter.setOnItemClickListener(new OnPharmacyItemClickListener() {
            @Override
            public void onItemClick(PharmacyAdapter.ViewHolder holder, View view, int position) {

            }

            @Override
            public void onCallClick(int position) {

            }

            @Override
            public void onPath(int position) {

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
        mapView.setCurrentLocationTrackingMode(MapView.CurrentLocationTrackingMode.TrackingModeOnWithoutHeading); //트래킹 모드 on + 나침반 모드 on

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

                    int size = response.body().getBody().getItems().getItemsList().size();

                    List<PharmacyItem> list = response.body().getBody().getItems().getItemsList();
                    Log.d(TAG, "list.size(): "+list.size());

                    for(int i = 0 ; i < size; i++){
                        String addr = response.body().getBody().getItems().getItemsList().get(i).getAddr();
                        String distance = response.body().getBody().getItems().getItemsList().get(i).getDistance();
                        String postNo = response.body().getBody().getItems().getItemsList().get(i).getPostNo();
                        String telno = response.body().getBody().getItems().getItemsList().get(i).getTelNo();
                        String yadmNm = response.body().getBody().getItems().getItemsList().get(i).getYadmNm();
                        double latittude = response.body().getBody().getItems().getItemsList().get(i).getLatitude();
                        double longitude = response.body().getBody().getItems().getItemsList().get(i).getLongitude();

                        Log.d(TAG, "addr: "+addr +" distance: "+distance+" postNo: "+postNo+" telno: "+telno+" yadmNm: "+yadmNm+" latittude: "+latittude+" longitude: "+longitude );
                    }
                    addMarker(list);
                }
            }

            @Override
            public void onFailure(Call<Response_phy> call, Throwable t) {
                Log.d(TAG, "Faill: FaillFaillFaillFaill");
            }
        });
    }
    private void addMarker(List<PharmacyItem> totalList){
        this.list = totalList;
        list_inform = new ArrayList<>();
        adapter = new PharmacyAdapter(list_inform);
        recyclerView.setAdapter(adapter);
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
        }
        Log.d(TAG, "list.size: "+adapter.getItemCount());
        adapter.notifyDataSetChanged();
    }

    //////////////////      MapView 객체 이벤트 처리  ////////////////////////////
    @Override
    public void onCurrentLocationUpdate(MapView mapView, MapPoint mapPoint, float v) {

    }

    @Override
    public void onCurrentLocationDeviceHeadingUpdate(MapView mapView, float v) {

    }

    @Override
    public void onCurrentLocationUpdateFailed(MapView mapView) {

    }

    @Override
    public void onCurrentLocationUpdateCancelled(MapView mapView) {

    }

    @Override
    public void onMapViewInitialized(MapView mapView) {

    }

    @Override
    public void onMapViewCenterPointMoved(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewZoomLevelChanged(MapView mapView, int i) {

    }

    @Override
    public void onMapViewSingleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDoubleTapped(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewLongPressed(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragStarted(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewDragEnded(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onMapViewMoveFinished(MapView mapView, MapPoint mapPoint) {

    }

    @Override
    public void onPOIItemSelected(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem) {

    }

    @Override
    public void onCalloutBalloonOfPOIItemTouched(MapView mapView, MapPOIItem mapPOIItem, MapPOIItem.CalloutBalloonButtonType calloutBalloonButtonType) {

    }

    @Override
    public void onDraggablePOIItemMoved(MapView mapView, MapPOIItem mapPOIItem, MapPoint mapPoint) {

    }
}