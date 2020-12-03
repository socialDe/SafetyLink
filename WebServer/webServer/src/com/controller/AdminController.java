package com.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.frame.Biz;
import com.vo.AdminVO;

@Controller
public class AdminController {
	@Resource(name="abiz")
	Biz<String, AdminVO> abiz;
	
	// 로그인 (2020.11.27)
	@RequestMapping("/loginimpl.mc")
	public ModelAndView loginimpl(ModelAndView mv, HttpServletRequest request, HttpSession session) {
		String id = request.getParameter("id");
		String pwd = request.getParameter("password");
		System.out.println(id + ", " + pwd);
		
		try {
			AdminVO dbadmin = abiz.get(id);
			
			// 비밀번호 확인 후 로그인
			if(pwd.equals(dbadmin.getAdminpwd())) {
				session.setAttribute("admin", dbadmin);
				
				// 로그인 상태 설정, DB 수정
				dbadmin.setAdminstate("t");
				abiz.modify(dbadmin);
				
				// 로그인 완료 후 메인페이지로 대쉬보드 설정
				mv.addObject("centerpage", "dashboard");
			}else {
				// 로그인 실패 정보 전송
				mv.addObject("loginfail", "loginfail");
			}
		} catch (Exception e) {
			// 로그인 실패 정보 전송
			mv.addObject("loginfail", "loginfail");
			e.printStackTrace();
		}
		
		mv.setViewName("main");
		return mv;
	}
	
	// 로그아웃 (2020.11.27)
	@RequestMapping("/logout.mc")
	public ModelAndView logout(ModelAndView mv, HttpServletRequest request) {
		HttpSession session = request.getSession();
		AdminVO admin = (AdminVO) session.getAttribute("admin");
		
		if (session != null) {
			// 세선 정보 삭제
			session.invalidate();
			
			// 로그아웃 상태 설정
			admin.setAdminstate("f");
		}
		
		// 로그아웃 상태 DB 수정
		try {
			abiz.modify(admin);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mv.setViewName("main");
		return mv;
	}
	// Chart Search
	@ResponseBody
	@RequestMapping("/chartSearch.mc")
	public ModelAndView chartSearch(ModelAndView mv, HttpServletResponse res, String gender_check,
			String type_check, String searchBasis, String regionLists, String startPeriod, String endPeriod,
			String timeList1, String timeList2) {
		
		
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
			
		}else if(searchBasis.equals("Time")) {
			
		}
		for(LineData ldt: lineDatas) {
			System.out.println(ldt);
		}

		mv.addObject(lineDatas);
		mv.setViewName("chart");
		return mv;
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
	
}
