package com.example.smrpv2.ui.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.example.smrpv2.R;
import com.example.smrpv2.model.MedicineItem;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

/**
 * BottomSheetDialog : 알람 삭제, 알람을 설정하기 위해 약 추가한 거 삭제, 약 목록 삭제를 위한 Dialog
 * 1. 알람 삭제 : AlarmFragment에 보이는 알람 목록 중 하나 클릭 시 AlarmEditActivity가 호출되는데 거기서 클릭한 알람 삭제 가능 : ALARM
 * 2. 알람을 설정하기 위해 약 추가한 거 ㅏ삭제 : 알람 설정할 약을 추가하는 AlarmEditActivity와 AlarmSetActivity에 추가한 약 삭제 가능  : TEMP_ALARM
 *    => 실제는 앞에 두 가지 Activity에 추가한 약을 클릭 시 호출되는 MedicineDetailActivity에서 삭제 가능
 * 3. 약 목록 삭제 : MedicineFragment에 보이는 약 목록 중 하나 클릭 시 MedicineDetailActivity가 호출되는데 거기서 클릭한 약 삭제 가능 : MEDICINE
 *
 */
public class BottomSheetDialog extends BottomSheetDialogFragment {

    Intent intent;
    Context context;
    public static AlarmManager alarmManager=null;
    public static PendingIntent pendingIntent=null;

    private LinearLayout Lay_delete;
    private LinearLayout Lay_cancel;

    private String userId;
    private String itemSeq;
    private   long groupId;
    private ArrayList<MedicineItem> listViewItemArrayList;
    int delete_case;
    final int MEDICINE = 0;
    final int ALARM = 1;
    final int TEMP_ALARM=2;


    public  static BottomSheetDialog getInstance() {
        return new BottomSheetDialog();
    }

    /**
     * DELETE MEDICINE
     * @param userId
     * @param itemSeq
     * @param delete_case MEDICINE
     */
    public void init(String userId, String itemSeq, int delete_case){
        this.userId=userId;
        this.itemSeq=itemSeq;
        this.delete_case = delete_case;
    }

    /**
     * DELETE ALARM
     * @param groupId
     * @param delete_case ALARM
     */
    public void init(Long groupId, int delete_case){
        this.groupId=groupId;
        this.delete_case = delete_case;
    }

    /**
     * DELETE TEMP_ALARM (MEDICINE)
     * @param userId
     * @param itemSeq
     * @param list 알람을 설정하기 위해 추가한 약 리스트들
     * @param delete_case TEMP_ALARM
     */
    public void init(String userId, String itemSeq, ArrayList<MedicineItem> list, int delete_case){
        this.userId=userId;
        this.itemSeq=itemSeq;
        listViewItemArrayList = list;
        this.delete_case = delete_case;
    }


    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);

        //초기화...
        Lay_delete = (LinearLayout) view.findViewById(R.id.Lay_delete);
        Lay_cancel = (LinearLayout) view.findViewById(R.id.Lay_cancel);
        context= view.getContext();
        alarmManager = (AlarmManager)getContext().getSystemService(context.ALARM_SERVICE);

        Lay_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(delete_case == MEDICINE){
                    delete_medicine();
                }
                else if(delete_case ==TEMP_ALARM) {
                    delete_tempMedicine_alarm();
                }else if(delete_case==ALARM){
                    delete_alarm();
                }
            }
        });


        Lay_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        //intent = new Intent(context,Alarm_Reciver.class);
        return view;
    }

    void delete_medicine(){

        Toast.makeText(getContext(), "delete_medicine", Toast.LENGTH_SHORT).show();
        /**
         *
         *
         * 서버 연결이 안돼서 임시로 비어둠 연결 후 다시 수정 예정임
         *
         *
         *
         */
        getActivity().onBackPressed();
    }
    void delete_alarm(){
        Toast.makeText(getContext(), "delete_alarm", Toast.LENGTH_SHORT).show();
        /**
         *
         *
         * 서버 연결이 안돼서 임시로 비어둠 연결 후 다시 수정 예정임
         *
         *
         *
         */
        getActivity().onBackPressed();
    }
    void delete_tempMedicine_alarm(){
        for (int i = 0; i < listViewItemArrayList.size(); i++) {
            if (listViewItemArrayList.get(i).getItemSeq().equals(itemSeq)) {
                listViewItemArrayList.remove(i);

                Intent intent = new Intent();
                intent.putExtra("result", "some");
                intent.putExtra("listViewItemArrayList", listViewItemArrayList);

                getActivity().setResult(RESULT_OK, intent);
                getActivity().finish();
                break;
            }

        }
    }

}