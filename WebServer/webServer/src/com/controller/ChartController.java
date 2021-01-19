package com.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.CarVO;
import com.vo.UsersVO;

@Controller
public class ChartController {
	
	@Resource(name = "ubiz")
    Biz<String, String, UsersVO> ubiz;
	
	String url = "jdbc:hive2://192.168.111.120:10000/default";
	String id = "root";
	String password = "111111";

//	public ChartController() {
//		try {
//			Class.forName("org.apache.hive.jdbc.HiveDriver");
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		}
//	}
	
	
	// Chart Search
	@ResponseBody
	@RequestMapping("/chartSearch.mc")
	public ArrayList<LineData> chartSearch(ModelAndView mv, HttpServletResponse res, String gender_check,
			String type_check, String searchBasis, String regionLists, String startPeriod, String endPeriod,
			String timeLists1, String timeLists2) {
		
		ArrayList<LineData> lineDatas = new ArrayList<>();
		
		// For Test
		Random r = new Random();
		
		if(searchBasis.equals("Region")) {
			// 지역 선택 Dropdown 결과의 세부 지역 입력
			ArrayList<String> specificRegions = new ArrayList<>();
			if(regionLists.equals("seoul")) {
				String[] names = {"강남구", "강동구", "강북구", "강서구", "관악구", "광진구", "구로구", 
						"금천구", "노원구", "도봉구", "동대문구", "동작구", "마포구", "서대문구", "서초구", "성동구", 
						"성북구", "송파구", "양천구", "영등포구", "용산구", "은평구", "종로구", "중구", "중랑구"};
				for(String region: names) {
					specificRegions.add(region);
				}
			
			}
			
			if(gender_check.equals("Man")) {
				if(type_check.equals("Sedan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						// select count(*) from -- where vehicleType='sedan', gender='man', region='강남구'
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine(type_check+"/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("Van")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine(type_check+"/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("Truck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine(type_check+"/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVan")) {
					// select count(*) from -- where vehicleType='sedan', gender='man', region='강남구'
					// +
					// select count(*) from -- where vehicleType='Van', gender='man', region='강남구'
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Sedan&Van/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Sedan&Truck/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("VanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Van&Truck/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					// select count(*) from -- where vehicleType='sedan', gender='man', region='강남구'
					// +
					// select count(*) from -- where vehicleType='van', gender='man', region='강남구'
					// +
					// select count(*) from -- where vehicleType='truck', gender='man', region='강남구'
					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Sedan&Van&Truck/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("Vehicle_all")) {
					// VehicleType마다 하나의 선으로 지역별 밀집 분포를 송신
					/* select count(*) from -- where vehicleType='sedan', gender='man', region='강남구'
					 * select count(*) from -- where vehicleType='van', gender='man', region='강남구'
					 * select count(*) from -- where vehicleType='truck', gender='man', region='강남구'
					 * 
					 * HashMap Structure: <차종,<차량수,지역>>
					 */
					
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData();
							pointData.setQuantity(r.nextInt(3000)+2000);
							pointData.setBasisSpecific(region);
							pointDatas.add(pointData);
						}
						lineData.setLine(type+"/"+gender_check);
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
				}
			}else if(gender_check.equals("Woman")) {
				if(type_check.equals("Sedan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						// select count(*) from -- where vehicleType='sedan', gender='man', region='강남구'
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine(type_check+"/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("Van")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						// select count(*) from -- where vehicleType='sedan', gender='man', region='강남구'
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine(type_check+"/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("Truck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						// select count(*) from -- where vehicleType='sedan', gender='man', region='강남구'
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine(type_check+"/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Sedan&Van/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Sedan&Truck/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("VanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Van&Truck/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();

					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Sedan&Van&Truck/"+gender_check);
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("Vehicle_all")) {
					
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData();
							pointData.setQuantity(r.nextInt(3000)+2000);
							pointData.setBasisSpecific(region);
							pointDatas.add(pointData);
						}
						lineData.setLine(type+"/"+gender_check);
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
				}
			}else if(gender_check.equals("ManWoman")) {
				if(type_check.equals("Sedan")) {
					// select count(*) from -- where vehicleType='sedan', gender='man', region='강남구'
					// +
					// select count(*) from -- where vehicleType='sedan', gender='woman', region='강남구'
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine(type_check+"/Man&Woman");
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("Van")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine(type_check+"/Man&Woman");
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("Truck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine(type_check+"/Man&Woman");
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVan")) {
					// sedan&man + sedan+woman + van&man + van&woman
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Sedan&Van/Man&Woman");
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
					
				}else if(type_check.equals("SedanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Sedan&Truck/Man&Woman");
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("VanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Van&Truck/Man&Woman");
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVanTruck")) {
					// sedan&man + sedan+woman + van&man + van&woman + truck&man + truck&woman
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					for(String region: specificRegions) {
						PointData pointData = new PointData();
						pointData.setQuantity(r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000);
						pointData.setBasisSpecific(region);
						pointDatas.add(pointData);
					}
					lineData.setLine("Sedan&Van&Truck/Man&Woman");
					lineData.setDatas(pointDatas);
					lineDatas.add(lineData);
				}else if(type_check.equals("Vehicle_all")) {
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData(r.nextInt(3000)+2000+r.nextInt(3000)+2000, region);
							pointDatas.add(pointData);
						}
						lineData.setLine(type+"/Man&Woman");
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
				}
			}else if(gender_check.equals("Gender_all")) {
				String[] genderTypes = {"Man", "Woman"};
				if(type_check.equals("Sedan")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData(r.nextInt(3000)+2000, region);
							pointDatas.add(pointData);
						}
						lineData.setLine(type_check+"/"+gender);
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
					
				}else if(type_check.equals("Van")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData(r.nextInt(3000)+2000, region);
							pointDatas.add(pointData);
						}
						lineData.setLine(type_check+"/"+gender);
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("Truck")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData(r.nextInt(3000)+2000, region);
							pointDatas.add(pointData);
						}
						lineData.setLine(type_check+"/"+gender);
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("SedanVan")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData(r.nextInt(3000)+2000+r.nextInt(3000)+2000, region);
							pointDatas.add(pointData);
						}
						lineData.setLine("Sedan&Van/"+gender);
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("SedanTruck")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData(r.nextInt(3000)+2000+r.nextInt(3000)+2000, region);
							pointDatas.add(pointData);
						}
						lineData.setLine("Sedan&Truck/"+gender);
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("VanTruck")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData(r.nextInt(3000)+2000+r.nextInt(3000)+2000, region);
							pointDatas.add(pointData);
						}
						lineData.setLine("Van&Truck/"+gender);
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("SedanVanTruck")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						for(String region: specificRegions) {
							PointData pointData = new PointData(r.nextInt(3000)+2000+r.nextInt(3000)+2000+r.nextInt(3000)+2000, region);
							pointDatas.add(pointData);
						}
						lineData.setLine("Sedan&Van&Truck/"+gender);
						lineData.setDatas(pointDatas);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("Vehicle_all")) {
					for(String gender : genderTypes) {
						String[] vehicleTypes = {"Sedan", "Van", "Truck"};
						for(String type : vehicleTypes) {
							LineData lineData = new LineData();
							ArrayList<PointData> pointDatas = new ArrayList<>();
							for(String region: specificRegions) {
								PointData pointData = new PointData(r.nextInt(3000)+2000, region);
								pointDatas.add(pointData);
							}
							lineData.setLine(type+"/"+gender);
							lineData.setDatas(pointDatas);
							lineDatas.add(lineData);
						}
					}
				}
			}
		}else if(searchBasis.equals("Period")) {
			
			String[] startData = startPeriod.split("-");
			String[] endData = endPeriod.split("-");
			
			int startYear, startMonth;
			int endYear, endMonth;
			
			startYear = Integer.parseInt(startData[0]);
			startMonth = Integer.parseInt(startData[1]);
			endYear = Integer.parseInt(endData[0]);
			endMonth = Integer.parseInt(endData[1]);
			
			/* 연도 차이가 나는 경우,
			 * 시작 년도에는 시작 월부터 12월까지 데이터 생성,
			 * 시작 년도 이후 ~ 마지막 년도 전까지는 시작 월부터 12월까지 데이터 생성,
			 * 마지막 년도에는 1월부터 종료 월까지 데이터 생성
			 */
			
			if(gender_check.equals("Man")) {
				if(type_check.equals("Sedan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
					
				}else if(type_check.equals("Van")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Truck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVan")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van"+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanTruck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Truck"+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("VanTruck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Van&Truck"+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVanTruck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van&Truck"+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Vehicle_all")){
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type+"/"+gender_check);
						lineDatas.add(lineData);
					}
				}
			}else if(gender_check.equals("Woman")) {
				if(type_check.equals("Sedan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Van")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Truck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVan")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van"+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanTruck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Truck"+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("VanTruck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Van&Truck"+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVanTruck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van&Truck"+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Vehicle_all")){
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type+"/"+gender_check);
						lineDatas.add(lineData);
					}
				}
			}else if(gender_check.equals("ManWoman")) {
				if(type_check.equals("Sedan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+"Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("Van")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+"Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("Truck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+"Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVan")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van"+"/"+"Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanTruck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Truck"+"/"+"Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("VanTruck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Van&Truck"+"/"+"Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVanTruck")){
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
					
					if(startYear != endYear) {
						for(int j = startMonth; j <= 12; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						for(int i = startYear+1; i <= endYear-1; i++) {
							for(int j = 1; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						for(int j = 1; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}else if(startYear == endYear) {
						// 연도 차이가 나지 않는 경우,
						// 시작 월부터 종료 월까지 데이터 생성
						for(int j = startMonth; j <= endMonth; j++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van&Truck"+"/"+"Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("Vehicle_all")){
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type+"/"+"Man&Woman");
						lineDatas.add(lineData);
					}
				}
			}else if(gender_check.equals("Gender_all")) {
				String[] genderTypes = {"Man", "Woman"};
				if(type_check.equals("Sedan")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type_check+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("Van")){
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type_check+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("Truck")){
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type_check+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("SedanVan")){
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine("Sedan&Van"+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("SedanTruck")){
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine("Sedan&Truck"+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("VanTruck")){
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine("Van&Truck"+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("SedanVanTruck")){
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
						if(startYear != endYear) {
							for(int j = startMonth; j <= 12; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							for(int i = startYear+1; i <= endYear-1; i++) {
								for(int j = 1; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							for(int j = 1; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}else if(startYear == endYear) {
							// 연도 차이가 나지 않는 경우,
							// 시작 월부터 종료 월까지 데이터 생성
							for(int j = startMonth; j <= endMonth; j++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
						}
						lineData.setDatas(pointDatas);
						lineData.setLine("Sedan&Van&Truck"+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("Vehicle_all")){
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						for(String gender : genderTypes) {
							LineData lineData = new LineData();
							ArrayList<PointData> pointDatas = new ArrayList<>();
							if(startYear != endYear) {
								for(int j = startMonth; j <= 12; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(startYear)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
								for(int i = startYear+1; i <= endYear-1; i++) {
									for(int j = 1; j <= 12; j++) {
										PointData pointData = new PointData();
										pointData.setBasisSpecific(String.valueOf(i)+"/"+String.valueOf(j));
										pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
										pointDatas.add(pointData);
									}
								}
								for(int j = 1; j <= endMonth; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}else if(startYear == endYear) {
								// 연도 차이가 나지 않는 경우,
								// 시작 월부터 종료 월까지 데이터 생성
								for(int j = startMonth; j <= endMonth; j++) {
									PointData pointData = new PointData();
									pointData.setBasisSpecific(String.valueOf(endYear)+"/"+String.valueOf(j));
									pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
									pointDatas.add(pointData);
								}
							}
							lineData.setDatas(pointDatas);
							lineData.setLine(type+"/"+gender);
							lineDatas.add(lineData);
						}
					}
				}
			}
			
		}else if(searchBasis.equals("Time")) {
			
			int startTime = Integer.parseInt(timeLists1);
			int endTime = Integer.parseInt(timeLists2);
			
			if(gender_check.equals("Man")) {
				if(type_check.equals("Sedan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Van")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Truck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Truck/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("VanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Van&Truck/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van&Truck/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Vehicle_all")) {
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type+"/"+gender_check);
						lineDatas.add(lineData);
					}
				}
			}else if(gender_check.equals("Woman")) {
				if(type_check.equals("Sedan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Van")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Truck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Truck/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("VanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Van&Truck/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van&Truck/"+gender_check);
					lineDatas.add(lineData);
				}else if(type_check.equals("Vehicle_all")) {
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type+"/"+gender_check);
						lineDatas.add(lineData);
					}
				}
			}else if(gender_check.equals("ManWoman")) {
				if(type_check.equals("Sedan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("Van")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("Truck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine(type_check+"/Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVan")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van/Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Truck/Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("VanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Van&Truck/Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("SedanVanTruck")) {
					LineData lineData = new LineData();
					ArrayList<PointData> pointDatas = new ArrayList<>();
			
					for(int i = startTime ; i < endTime; i++) {
						PointData pointData = new PointData();
						pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
						pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
						pointDatas.add(pointData);
					}
					lineData.setDatas(pointDatas);
					lineData.setLine("Sedan&Van&Truck/Man&Woman");
					lineDatas.add(lineData);
				}else if(type_check.equals("Vehicle_all")) {
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type+"/Man&Woman");
						lineDatas.add(lineData);
					}
				}
			}else if(gender_check.equals("Gender_all")) {
				String[] genderTypes = {"Man", "Woman"};
				
				if(type_check.equals("Sedan")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type_check+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("Van")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type_check+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("Truck")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine(type_check+"/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("SedanVan")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine("Sedan&Van/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("SedanTruck")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine("Sedan&Truck/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("VanTruck")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine("Van&Truck/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("SedanVanTruck")) {
					for(String gender : genderTypes) {
						LineData lineData = new LineData();
						ArrayList<PointData> pointDatas = new ArrayList<>();
				
						for(int i = startTime ; i < endTime; i++) {
							PointData pointData = new PointData();
							pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
							pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000+r.nextInt(100000)+10000);
							pointDatas.add(pointData);
						}
						lineData.setDatas(pointDatas);
						lineData.setLine("Sedan&Van&Truck/"+gender);
						lineDatas.add(lineData);
					}
				}else if(type_check.equals("Vehicle_all")) {
					String[] vehicleTypes = {"Sedan", "Van", "Truck"};
					for(String type : vehicleTypes) {
						for(String gender : genderTypes) {
							LineData lineData = new LineData();
							ArrayList<PointData> pointDatas = new ArrayList<>();
					
							for(int i = startTime ; i < endTime; i++) {
								PointData pointData = new PointData();
								pointData.setBasisSpecific(String.valueOf(i)+"~"+String.valueOf(i+1)+"시");
								pointData.setQuantity(r.nextInt(100000)+10000+r.nextInt(100000)+10000);
								pointDatas.add(pointData);
							}
							lineData.setDatas(pointDatas);
							lineData.setLine(type+"/"+gender);
							lineDatas.add(lineData);
						}
					}
				}
			}
		}
		
		
		for(LineData ldt: lineDatas) {
			System.out.println(ldt);
		}
		return lineDatas;
	}
	
	public class LineData {
		String line;
		ArrayList<PointData> datas;
		public LineData() {
		}
		public LineData(String line, ArrayList<PointData> datas) {
			this.line = line;
			this.datas = datas;
		}
		public String getLine() {
			return line;
		}
		public void setLine(String line) {
			this.line = line;
		}
		public ArrayList<PointData> getDatas() {
			return datas;
		}
		public void setDatas(ArrayList<PointData> datas) {
			this.datas = datas;
		}
		@Override
		public String toString() {
			return "LineData [line=" + line + ", datas=" + datas + "]";
		}
	}
	
	public class PointData{
		int quantity;
		String basisSpecific;
		public PointData() {
		}
		public PointData(int quantity, String basisSpecific) {
			this.quantity = quantity;
			this.basisSpecific = basisSpecific;
		}
		public int getQuantity() {
			return quantity;
		}
		public void setQuantity(int quantity) {
			this.quantity = quantity;
		}
		public String getBasisSpecific() {
			return basisSpecific;
		}
		public void setBasisSpecific(String basisSpecific) {
			this.basisSpecific = basisSpecific;
		}
		@Override
		public String toString() {
			return "PointData [quantity=" + quantity + ", basisSpecific=" + basisSpecific + "]";
		}
	}
		
	
	@RequestMapping("/getFcmLog.mc")
	@ResponseBody
	public void getFcmLog(HttpServletResponse res) throws IOException, SQLException {
		
		Connection con = null;
		JSONArray ja = new JSONArray();
		
		try {
//			con = DriverManager.getConnection(url,id,password);
//			PreparedStatement pstmt = con.prepareStatement("SELECT * FROM hdi LIMIT 10");
//			ResultSet rset = pstmt.executeQuery();
//	
//			// [ {name: '' , data:[ , , ]} ,{}]
//			
//			while(rset.next()) {
					

				JSONObject data;
				data = new JSONObject();
				data.put("fcmnum", "1");
				data.put("fcmtype", "적재물 낙하 경보");
				data.put("carnum", "12가1234");
				data.put("date", "2020-12-10");
				data.put("time", "21:23:15");
				ja.add(data);
				
				data = new JSONObject();
				data.put("fcmnum", "2");
				data.put("fcmtype", "졸음 경보");
				data.put("carnum", "23가1454");
				data.put("date", "2020-12-11");
				data.put("time", "22:23:15");
				ja.add(data);
				
				data = new JSONObject();
				data.put("fcmnum", "3");
				data.put("fcmtype", "영유아 경보");
				data.put("carnum", "13나1334");
				data.put("date", "2020-12-13");
				data.put("time", "23:23:15");
				ja.add(data);
				
				data = new JSONObject();
				data.put("fcmnum", "4");
				data.put("fcmtype", "충돌 경보");
				data.put("carnum", "54다1234");
				data.put("date", "2020-12-14");
				data.put("time", "04:23:15");
				ja.add(data);
				
				data = new JSONObject();
				data.put("fcmnum", "5");
				data.put("fcmtype", "영유아 경보");
				data.put("carnum", "89라1234");
				data.put("date", "2020-12-15");
				data.put("time", "20:23:15");
				ja.add(data);

				
//			}
			
		}catch(Exception e) {
			throw e;
		}finally {
//			con.close();
		}
		

		
		res.setCharacterEncoding("UTF-8");
		res.setContentType("application/json");
		PrintWriter out = res.getWriter();
		out.print(ja.toJSONString());
		out.close();

	}
	
	

}
