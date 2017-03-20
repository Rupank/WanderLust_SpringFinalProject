package com.mmt.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import com.mmt.model.bean.FlightBooking;
import com.mmt.model.bean.Promotion;
import com.mmt.model.bean.User;
import com.mmt.model.bean.Wallet;
import com.mmt.model.bl.AdminBlMMT;
import com.mmt.model.bl.FlightBookingBlMMT;
import com.mmt.model.bl.FlightPaymentBl;
import com.mmt.model.bl.PromotionBlMMT;
import com.mmt.model.bl.UserBlMMT;
//sd
import com.mmt.model.bl.WalletBlMMT;

@Controller
@SessionAttributes({ "user", "adminSession", "userSession", "seats", "pickedFlight", "finalValuetobepaid",
		"moneyToBeAdded", "messageFlight", "departureDate", "balance", "msg" })
public class UserController {
	AdminBlMMT adminBlMMT = new AdminBlMMT();
	UserBlMMT blMMT = new UserBlMMT();
	FlightBookingBlMMT flightBookingBlMMT = new FlightBookingBlMMT();
	private UserBlMMT userBl = new UserBlMMT();

	@RequestMapping("/")
	public String newRegistration(ModelMap model) {
		// User user = new User();
		// model.addAttribute("user", user);
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
	public ModelAndView returnDashboard(ModelAndView modelAndView, User user, ModelMap model)
			throws ClassNotFoundException, SQLException, IOException {
		Admin admin = null;
		modelAndView.addObject("name", user.getUserName());
		admin = adminBlMMT.checkAdminLogin(user.getUserName(), user.getUserPassword());
		User user1 = null;
		user1 = blMMT.checkLogin(user.getUserName(), user.getUserPassword());
		if (admin != null) {
			model.addAttribute("adminSession", admin);
			modelAndView.setViewName("AdminLogin");

		} else if (user1 != null) {
			model.addAttribute("userSession", user1);
			modelAndView.setViewName("UserDashBoard");
		} else {
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
		ModelAndView modelAndView = new ModelAndView("LoogedInFlightForm", "flightInfo", new Flight());
		return modelAndView;

	}

	@RequestMapping("/login")
	public ModelAndView returnLoginPage() {
		ModelAndView modelAndView = new ModelAndView("login", "user", new User());
		return modelAndView;

	}

	@RequestMapping("/AddMoney")
	public ModelAndView returnAddMoney(ModelAndView modelAndView) {
		modelAndView.setViewName("AddMoney");
		return modelAndView;
	}

	@RequestMapping("/UserLogin")
	public ModelAndView returnUserLogin(ModelAndView modelAndView) {
		modelAndView.setViewName("UserLogin");
		return modelAndView;
	}

	@RequestMapping("/DisplayWallet")
	public ModelAndView returnDisplayWallet(ModelAndView modelAndView, ModelMap model)
			throws ClassNotFoundException, SQLException, IOException {
		WalletBlMMT walletBl = new WalletBlMMT();
		String preciseBalance = null;
		double balance = 0;
		User user = (User) model.get("userSession");
		balance = walletBl.walletBalance(user.getUserId());
		DecimalFormat df2 = new DecimalFormat(".##");
		preciseBalance = df2.format(balance);
		model.addAttribute("balance", preciseBalance);
		//modelAndView.addObject("flightBooking",flightBooking );
		modelAndView.setViewName("Wallet");
		return modelAndView;
	}

	@RequestMapping("/flightHomePage")
	public ModelAndView returnFlightHomePage() {
		ModelAndView modelAndView = new ModelAndView("FlightForm", "FlightInfo", new Flight());
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
	public ModelAndView userRegisterStatus(@ModelAttribute("FlightInfo") Flight flight, ModelMap model,
			HttpServletRequest request) throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		ArrayList<Flight> arrayListFlight = null;
		//model.addAttribute("pickedFlight", flight);
		int seat = Integer.parseInt(request.getParameter("seats"));
		String departureDate = request.getParameter("departureDate");
		model.addAttribute("departureDate", departureDate);
		model.addAttribute("seats", seat);
		arrayListFlight = flightBookingBlMMT.searchFlight(flight.getFlightSource(), flight.getFlightDestination());

		if (arrayListFlight.isEmpty()) {
			modelAndView.addObject("message",
					"No Flights from  " + flight.getFlightSource() + " to " + flight.getFlightDestination());
			modelAndView.setViewName("NoFlightFromStD");
		} else {
			User user = (User) model.get("userSession");
			if (user != null) {
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
	public ModelAndView ChoosePromoFlight(ModelAndView modelAndView, ModelMap model,HttpSession session)
			throws ClassNotFoundException, SQLException, IOException {
		User user1 = (User) model.get("userSession");
		String pickedFlightID=(String) session.getAttribute("flightId");
		Flight pickedFlight=(Flight)flightBookingBlMMT.searchFlight(pickedFlightID);
		model.addAttribute("pickedFlight",pickedFlight);
		if (user1 == null) {
			modelAndView.setViewName("loginUnregistered");
		} else {
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
		int noOfSeats = (int) model.get("seats");
		FlightPaymentBl flightPaymentBl = new FlightPaymentBl();
		Flight pickedFlight = (Flight) model.get("pickedFlight");
		double cartValue = 0;
		cartValue = flightPaymentBl.cartValue(pickedFlight.getFlightTicketPrice(), noOfSeats);
		PromotionBlMMT promotionBlMMT = new PromotionBlMMT();
		double valueAfterPromotion = 0;
		User user = (User) model.get("userSession");
		if (promotion.getPromotionId() == null) {
			valueAfterPromotion = cartValue;
		} else {

			valueAfterPromotion = promotionBlMMT.applyPromotion(
					promotionBlMMT.searchPromotion(promotion.getPromotionId()), user.getUserId(), cartValue);

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
			String departureDate = (String) model.get("departureDate");
			modelAndView.addObject("departureDate", departureDate);
			modelAndView.addObject("wallet", new Wallet());
			modelAndView.setViewName("AddMoney");
		}

		return modelAndView;

	}

	@RequestMapping("/MoneyAddded")
	public ModelAndView returnMoneyAddded(ModelAndView modelAndView, ModelMap model,
			@ModelAttribute("wallet") Wallet wallet) throws ClassNotFoundException, SQLException, IOException {
		WalletBlMMT walletBl = new WalletBlMMT();
		User user = (User) model.get("userSession");
		boolean value = false;
		String msg = "";
		double balance = 0;
		String preciseBalance = null;

		value = walletBl.addWalletMoney(user.getUserId(), wallet.getWalletBalance());

		if (value == true) {
			msg = "Successfully Added";

			balance = walletBl.walletBalance(user.getUserId());
			DecimalFormat df2 = new DecimalFormat(".##");
			preciseBalance = df2.format(balance);
			model.addAttribute("balance", preciseBalance);
			model.addAttribute("msg", msg);
			modelAndView.setViewName("Wallet");
		}

		return modelAndView;

	}

	@RequestMapping("/ConfirmBooking")
	public ModelAndView returnConfirmBooking(ModelAndView modelAndView, ModelMap model)
			throws ClassNotFoundException, SQLException, IOException {
		WalletBlMMT walletBlMMT = new WalletBlMMT();
		boolean paymentStatus = false;
		double valueAfterPromotion = (double) model.get("finalValuetobepaid");
		System.out.println("valueAfterPromotion"+valueAfterPromotion);
		User user = (User) model.get("userSession");
		System.out.println("user:"+user);
		
		paymentStatus = walletBlMMT.subtractWalletMoney(user.getUserId(), valueAfterPromotion);
		System.out.println("paymentStatus"+paymentStatus);
		if (paymentStatus) {
			FlightBooking flightBooking = new FlightBooking();
			Flight flight = (Flight) model.get("pickedFlight");
			int seats = (int) model.get("seats");
			
			flightBooking = flightBookingBlMMT.bookFlight(user.getUserId(), flight.getFlightId(),
					flight.getFlightSource(), flight.getFlightDestination(), seats);
			System.out.println("flightBooking"+flightBooking);
			if (flightBooking != null) {
				String messageFlight = (String) model.get("messageFlight");
				messageFlight = null;
//				model.addAttribute("messageFlight", messageFlight);
				model.addAttribute("flightBooking", flightBooking);
				modelAndView.addObject("flightBooking",flightBooking );
				modelAndView.setViewName("FinalFlightStep");
			} else if (flightBooking == null) {

				paymentStatus = walletBlMMT.addWalletMoney(user.getUserId(), valueAfterPromotion);

				String messageFlight = (String) model.get("messageFlight");
				messageFlight = null;
				model.addAttribute("messageFlight", messageFlight);
				modelAndView.setViewName("NoFlightBooking");
			}

		}
		return modelAndView;

	}
}
