package com.example.tmapprac;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.opencsv.CSVReader;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;
import com.skt.Tmap.poi_item.TMapPOIItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    List<InputStream> inputStreamList = null; // 가로등 정보 파일 스트림 저장하는 객체들 저장
    TMapView tMapView = null; // 티맵 보여주는 객체
    List<TMapMarkerItem> itemMarkerList = null; // 위의 파일 스트림에서 추출한 지도상 마커 정보 가진 객체들 저장
    EditText latitude = null; // 고장 가로등 신고 접수 시 사용자가 입력하는 위도 정보 추출
    EditText longitude = null; // 고장 가로등 신고 접수 시 사용자가 입력하는 경도 정보 추출

    FirebaseFirestore firestoreDatabase = FirebaseFirestore.getInstance(); // 신고 접수 데이터 보관할 데이터베이스

    //boolean on_off = true; // 마커 표시 온오프 체크

    // asset 파일에서 가로등 위치 정보 엑셀 파읿 불러오는 함수
    private void getData(){

        AssetManager manager = getResources().getAssets(); // 가로등 정보 파일 위치
        itemMarkerList = new LinkedList<>();
        try {
            inputStreamList = new LinkedList<>();

            InputStream inputStream = manager.open("서울특별시_동작구_보안등정보_20220707.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_강남구_보안등정보_20211213_1639811421924_1283906.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_강서구_보안등정보_20220316_1647408019525_1329351.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_광진구_보안등정보_20220701.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_구로구_보안등정보_20220531.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_금천구_보안등정보_20220226_1646216898840_927050.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_동작구_보안등정보_20220707.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_성북구_보안등정보_20220601.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_영등포구_보안등정보_20220627.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_종로구_보안등정보_20220708.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_도봉구_보안등정보_20180515.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_서대문구_보안등정보_20201020_1603176968804_1347837.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_관악구_보안등정보_20201207_1607497441004_1627562.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_중랑구_보안등정보_20201209_1607491212600_1153686.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_송파구_보안등정보_20210225_1614748045897_1117425.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_양천구_보안등정보_20210624_1624599558550_558983.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_마포구_보안등정보_20210812_1628746918141_722041.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_은평구_보안등정보_20210802_1629283244150_1158010.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_강동구_보안등정보_20210825_1629871644483_1161326.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_강북구_보안등정보_20210831_1630470954649_1644180.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_노원구_보안등정보_20210916_1631839126541_743398.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_용산구_보안등정보_20210927_1632816216661_1247006.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_성동구_보안등정보_20210930_1632987402974_910893.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_중구_보안등정보_20211018_1634524178367_920561.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("서울특별시_서초구_보안등정보_20211020_1634718575449_1768192.csv");
            inputStreamList.add(inputStream);

            inputStream = manager.open("경기도_화성시_보안등정보_20190503.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_부천시_보안등정보_20190910.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_용인시_보안등정보_20190711.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_광명시_보안등정보_20201214_1608099205674_357504.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_안성시_보안등정보_20210315_1615815438485_2410370.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_연천군_보안등정보_20211108_1636351841051_789503.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_군포시_보안등정보_20210525_1621922260247_334032.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_시흥시_보안등정보_20210601_1623126234274_1341587.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_오산시_보안등정보_20210810_1628584109091_947417.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_여주시_보안등정보_20210721_1628746859811_1202685.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_동두천시_보안등정보_20210901_1629943179698_382386.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_성남시_보안등정보_20210831_1631332561052_1540397.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_가평군_보안등정보_20210824_1631593363709_1138166.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_안산시_보안등정보_20210831_1632359235177_1703202.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_김포시_보안등정보_20210929_1632959767818_1149942.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_고양시_보안등정보_20210927_1632975501220_1552009.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_광주시_보안등정보_20210913_1631871201546_2108140.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_의정부시_보안등정보_20210930_1633418176723_573249.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_이천시_보안등정보_20211206_1639102716929_1478675.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_수원시_보안등정보_20220527.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_의왕시_보안등정보_20220613.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_구리시_보안등정보_20220614.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_양주시_보안등정보_20220620.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_평택시_보안등정보_20220613.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_하남시_보안등정보_20220704.csv");
            inputStreamList.add(inputStream);
            inputStream = manager.open("경기도_과천시_보안등정보_20220718.csv");
            inputStreamList.add(inputStream);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    // tmap api 키 설정하고 저장해놓은 마커 정보들 지도 상에 표시
    private void mapload(){
        tMapView = new TMapView(this);
        tMapView.setSKTMapApiKey("l7xx1ee83da12e334595b10d8658f0816106");

        int sum1 = 0, sum2 = 0; // 파일에서 불러오다보니까 엑셀 내에 파손된 데이터 있길래 그거 필터링하고 남은 좌표 개수 세려고. 무시 가능

        try{
            for(int rot=0;rot<inputStreamList.size();rot++) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStreamList.get(rot), Charset.forName("UTF-8")));
                String line = "";
                StringTokenizer st = null;
                for (int i = 0; (line = bufferedReader.readLine()) != null; i++) {
                    if (i == 0)
                        continue;
                    st = new StringTokenizer(line, ",");

                    String tmp;
                    st.nextToken();
                    st.nextToken();
                    st.nextToken();
                    st.nextToken();

                    String x = st.nextToken();
                    String y = st.nextToken();
                    if(x.length()<10 || y.length()<11)
                        continue;

                    boolean check = false;
                    for(int dx=0;dx<x.length();dx++) {
                        if ('0' <= x.charAt(dx) && x.charAt(dx) <= '9' || x.charAt(dx) == '.')
                            check = false;
                        else
                            check = true;

                        if (check)
                            break;
                    }

                    if(check)
                        continue;

                    for(int dx=0;dx<y.length();dx++) {
                        if ('0' <= y.charAt(dx) && y.charAt(dx) <= '9' || y.charAt(dx) == '.')
                            check = false;
                        else
                            check = true;

                        if(check)
                            break;
                    }

                    if(check)
                        continue;

                    double dx = Double.parseDouble(x.substring(0, x.length()-1));
                    double dy = Double.parseDouble(y.substring(0, y.length()-1));

                    TMapPoint point = new TMapPoint(dx, dy);

                    TMapMarkerItem item = new TMapMarkerItem();

                    item.setTMapPoint(point);
                    item.setCanShowCallout(true);
                    item.setCalloutTitle(dx+", "+dy);
                    item.setVisible(TMapMarkerItem.VISIBLE);

                    itemMarkerList.add(item);
                    tMapView.addMarkerItem(String.valueOf(i), item);
                }
            }
            Log.d("sum", sum1+"+"+sum2);
        } catch (IOException e){
            e.printStackTrace();
        }

        // 가로등 위치 정보 파일 asset에서 안 가져오고 firebase에 저장해놨다가 불러오게 해서 앱 용량 조금이라도 줄이려고 했는데...
        // 데이터 크기가 20만인가 200만 바이트였나 그래서 저장이 안 된답니다.... (좌표 수가 20만 개이긴 해)
        // document 당 용량 정해진 것 같아서 일부러 document도 분할해봤는데 그래도 안 돼 ....
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {

                Map<String, String> map = new HashMap<>();
                Map<String, String> map2 = new HashMap<>();
                Map<String, String> map3 = new HashMap<>();

                for(int i=0;i<itemList.size();i++) {
                    TMapPoint point = itemList.get(i).getTMapPoint();

                    if(0<=i && i<itemList.size()/3)
                        map.put(point.getLatitude()+"", point.getLongitude()+"");
                    else if(itemList.size()/3<=i && i<= itemList.size()/3*2)
                        map2.put(point.getLatitude()+"", point.getLongitude()+"");
                    else
                        map3.put(point.getLatitude()+"", point.getLongitude()+"");
                }

                database.collection("locations").document("location")
                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("success1", "success");
                    }
                });
                database.collection("locations").document("location2")
                        .set(map2).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("success2", "success");
                    }
                });
                database.collection("locations").document("location3")
                        .set(map3).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("success3", "success");
                    }
                });
            }
        }).start();
*/
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getData();  // 가로등 위치 정보 불러오고

        setContentView(R.layout.activity_main);

        mapload(); // 불러온 위치 정보 지도상에 표시

        //verifyStoragePermissions(MainActivity.this); // 원래 다른 경로에서 가져오려고 했는데 asset으로 정한 거라 그냥 무시

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.tmap);
        linearLayout.addView(tMapView);

        Button zoomIn = (Button)findViewById(R.id.zoomin);             // 줌 기능 있어야 편할 것 같아서 일단 넣어봤음
        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Zoom", "in");
                tMapView.MapZoomIn();
            }
        });

        Button zoomOut = (Button)findViewById(R.id.zoomout);            // 이것도 마찬가지
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Zoom", "out");
                tMapView.MapZoomOut();
            }
        });

        Button alertLocationButton = (Button) findViewById(R.id.signal);            // 고장난 가로등 위도, 경도 정보 입력 받고 firebase로 저장
        alertLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                View customDialogView = inflater.inflate(R.layout.complain_dialog, null);

                latitude = (EditText)customDialogView.findViewById(R.id.xmap);
                longitude = (EditText)customDialogView.findViewById(R.id.ymap);

                builder.setView(customDialogView).setTitle("가로등 고장 접수")
                .setMessage("고장 접수할 가로등 위치 정보를 입력해주세요.")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("latitude", latitude.getText()+"");
                        Log.d("longitude", longitude.getText()+"");

                        Map<String, String> map = new HashMap<>();
                        map.put(latitude.getText().toString(), longitude.getText().toString()); // 입력 받은 위치 정보 map 자료구조에 저장

                        firestoreDatabase.collection("alert").document("location") // 정해놓은 firebase 위치에 저장
                                .set(map, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Success", "success");
                                Toast.makeText(MainActivity.this, "정보가 전송되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("Failure", "Failure");
                                Toast.makeText(MainActivity.this, "정보 전송에 실패했습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                })
                .setNegativeButton("취소", null);

                builder.create().show();
            }
        });   // 이 부분은 순이가 UI 구현할 거니까 거기에 이어붙일 생각

        // 가로등 마커 삭제했다 표시했다 하는 거 구현하려고 했던 건데 삭제 기능은 api에 있고,
        // 표시 기능만 만들려다 쓰레드 써도 개느려터져서 포기...
        // tmap 다시 불러오면 너무 이상해져서 버림...
        // 이거 xml 코드도 지워놔서 실험해보고 싶으면 xml 코드 추가하고 해보시길..
        /*
        Button button3 = (Button)findViewById(R.id.onoff);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(on_off){
                    on_off = false;
                    tMapView.removeAllMarkerItem(); // 마커 표시 삭제
                }
                else{
                    on_off = true;
                    mapReload();
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    // 마커 재표시
                }
            }
        });

         */
    }
}