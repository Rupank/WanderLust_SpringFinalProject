package com.mmt.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.mmt.model.bean.Admin;
import com.mmt.model.bean.Flight;
import com.mmt.model.bean.Promotion;
import com.mmt.model.bean.User;
import com.mmt.model.bl.AdminBlMMT;
import com.mmt.model.bl.FlightBookingBlMMT;
import com.mmt.model.bl.FlightPaymentBl;
import com.mmt.model.bl.PromotionBlMMT;
import com.mmt.model.bl.UserBlMMT;
//sd
import com.mmt.model.bl.WalletBlMMT;

@Controller
@SessionAttributes({"user","adminSession","userSession","seats","pickedFlight","finalValuetobepaid","moneyToBeAdded","messageFlight"})
public class UserController {
	AdminBlMMT adminBlMMT = new AdminBlMMT();
	UserBlMMT blMMT = new UserBlMMT();
	FlightBookingBlMMT flightBookingBlMMT = new FlightBookingBlMMT();
	private UserBlMMT userBl = new UserBlMMT();

	@RequestMapping("/")
	public String newRegistration(ModelMap model) {
//		 User user = new User();
//		 model.addAttribute("user", user);
		return "BlackHeader";
	}

	@RequestMapping("/userRegister")
	public ModelAndView userRegisterStatus(@ModelAttribute("user") User user) {
		ModelAndView modelAndView = new ModelAndView();
		try {
			if (userBl.register(user)) {
				modelAndView.addObject("message", user.getUserName() + " Successfully registered");
				modelAndView.setViewName("loginUnregistered");

			} else {
				modelAndView.addObject("message", user.getUserName() + " Successfully  not registered");
				modelAndView.setViewName("index");
			}
		} catch (ClassNotFoundException | SQLException | IOException e) {

			e.printStackTrace();
		}
		return modelAndView;

	}

	@RequestMapping("/LoginCheck")
	public ModelAndView returnDashboard(ModelAndView modelAndView,User user,ModelMap model) throws ClassNotFoundException, SQLException, IOException {
		Admin admin = null;
		modelAndView.addObject("name",user.getUserName());
			admin = adminBlMMT.checkAdminLogin(user.getUserName(), user.getUserPassword());
			User user1=null;
			user1 = blMMT.checkLogin(user.getUserName(), user.getUserPassword());
		if (admin != null) {
			
			//modelAndView.addObject("adminSession", admin);
			model.addAttribute("adminSession", admin);
			modelAndView.setViewName("AdminLogin");

		} else if (user1 != null) {
			//modelAndView.addObject("userSession", user1);
			model.addAttribute("userSession", user1);
			//System.out.println("User Value--------------------------"+user1);
			modelAndView.setViewName("UserDashBoard");
		} else  {
			modelAndView.setViewName("InvalidUser");
		}
		return modelAndView;

	}

	@RequestMapping("/signup")
	public ModelAndView returnSignupForm() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("index");
		return modelAndView;

	}
	
	@RequestMapping("/LoogedInFlightForm")
	public ModelAndView returnLoogedInFlightForm() {
		ModelAndView modelAndView = new ModelAndView("LoogedInFlightForm","flightInfo",new Flight());
		return modelAndView;

	}
	

	@RequestMapping("/login")
	public ModelAndView returnLoginPage() {
		ModelAndView modelAndView = new ModelAndView("login","user",new User());
		return modelAndView;

	}

	@RequestMapping("/flightHomePage")
	public ModelAndView returnFlightHomePage() {
		ModelAndView modelAndView = new ModelAndView("FlightForm","FlightInfo",new Flight());
		return modelAndView;

	}

	@ModelAttribute("flightSourceList")
	public List<Flight> flightSourceList() throws ClassNotFoundException, SQLException, IOException {
		List<Flight> flightSourceList = new ArrayList<Flight>();

			flightSourceList = flightBookingBlMMT.displayFlight();

		return flightSourceList;
	}

	@ModelAttribute("flightDestList")
	public List<Flight> flightDestList() throws ClassNotFoundException, SQLException, IOException {

		List<Flight> flightDestList = new ArrayList<Flight>();
	

			flightDestList = flightBookingBlMMT.displayFlight();

		return flightDestList;
	}

	@RequestMapping("/SearchFlightBySnD")
	public ModelAndView userRegisterStatus(@ModelAttribute("FlightInfo") Flight flight,ModelMap model,HttpServletRequest request) throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		ArrayList<Flight> arrayListFlight = null;
		model.addAttribute("pickedFlight", flight);
		int seat=Integer.parseInt(request.getParameter("seats"));
		model.addAttribute("seats", seat);
			arrayListFlight = flightBookingBlMMT.searchFlight(flight.getFlightSource(), flight.getFlightDestination());
		

		if (arrayListFlight.isEmpty()) {
			modelAndView.addObject("message",
					"No Flights from  " + flight.getFlightSource() + " to " + flight.getFlightDestination());
			modelAndView.setViewName("NoFlightFromStD");
		} else {
			//User user = (User) modelAndView.getModel().get("userSession");
			User user=(User) model.get("userSession");
			System.out.println();
			System.out.println("hsdhsuhuidasudhasuidhuiashdUSER"+user);
			System.out.println();
			
			if (user != null) {
				System.out.println("Etnered Here-2-32-3-1232323-----------3123");
				modelAndView.addObject("arrayListFlight", arrayListFlight);
				modelAndView.setViewName("LoggedInDisplayAllFlightsStD");

			} else {
				modelAndView.addObject("arrayListFlight", arrayListFlight);
				modelAndView.setViewName("DisplayAllFlightsStD");
			}
		}
		return modelAndView;

	}
	

	@RequestMapping("/ChoosePromoFlight")
	public ModelAndView ChoosePromoFlight(ModelAndView modelAndView,ModelMap model) throws ClassNotFoundException, SQLException, IOException {
		User user1=(User) model.get("userSession");
		System.out.println();
		System.out.println("shduashdhasihd---------------------------------"+user1);
		System.out.println();
		if (user1 == null) {
			modelAndView.setViewName("loginUnregistered");
		}
		else{
			PromotionBlMMT promoBl = new PromotionBlMMT();
			ArrayList<Promotion> arrayListPromoFlight = null;
			arrayListPromoFlight = promoBl.displayPromotion("FLIGHT");
			modelAndView.addObject("arrayListPromoFlight", arrayListPromoFlight);
			modelAndView.addObject("pickedPromoCode", new Promotion());
			modelAndView.setViewName("ChoosePromoCodeFlight");
		}
		
		
		return modelAndView;
	}
	
	@RequestMapping("/Payment")
	public ModelAndView returnPaymentWindow(ModelMap model, ModelAndView modelAndView,
			@ModelAttribute("pickedPromoCode") Promotion promotion)
			throws IOException, ClassNotFoundException, SQLException {
		int noOfSeats=(int) model.get("seats");
		FlightBookingBlMMT flightBookingBlMMT = new FlightBookingBlMMT();
		FlightPaymentBl flightPaymentBl = new FlightPaymentBl();
		Flight pickedFlight=(Flight) model.get("pickedFlight");
		double cartValue = 0;
		cartValue = flightPaymentBl.cartValue(pickedFlight.getFlightTicketPrice(), noOfSeats);
		PromotionBlMMT promotionBlMMT = new PromotionBlMMT();
		double valueAfterPromotion = 0;
		User user=(User) model.get("userSession");
		if (promotion.getPromotionId().equals("-")) {
			valueAfterPromotion = cartValue;
		} else {
			
				valueAfterPromotion = promotionBlMMT.applyPromotion(promotionBlMMT.searchPromotion(promotion.getPromotionId()),user.getUserId(), cartValue);
			
		}
		
		
			if (flightPaymentBl.checkFunds(user.getUserId(), valueAfterPromotion)) {

				// THere is sufficient funds in account------------------
				// Redirect to Confirm Payment JSP Page
				model.addAttribute("finalValuetobepaid", valueAfterPromotion);
				modelAndView.addObject("finalValuetobepaid", valueAfterPromotion);
				modelAndView.addObject("flightPicked", pickedFlight);
				modelAndView.setViewName("ConfirmFlightBooking");
			} else {
				// Insufficient Funds
				// Redirect to Add money to wallet and then redirect to confirm
				// payment JSP Page
				model.addAttribute("finalValuetobepaid", valueAfterPromotion);
				modelAndView.addObject("finalValuetobepaid", valueAfterPromotion);
				WalletBlMMT walletBl = new WalletBlMMT();
				double moneyToBeAdded = valueAfterPromotion - (walletBl.walletBalance(user.getUserId()));
				String message = "Add atleast " + moneyToBeAdded + " to Wallet to book flight seat";
				model.addAttribute("moneyToBeAdded", moneyToBeAdded);
				modelAndView.addObject("moneyToBeAdded", moneyToBeAdded);
				model.addAttribute("messageFlight", message);
				//session.setAttribute("messageFlight", message);
//				RequestDispatcher dispatch = request.getRequestDispatcher("AddMoney.jsp");
//				dispatch.forward(request, response);
				modelAndView.setViewName("AddMoney");
			}
		
		
		return modelAndView;

	}
}
