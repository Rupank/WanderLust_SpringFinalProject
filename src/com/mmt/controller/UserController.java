package com.mmt.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.mmt.model.bean.Admin;
import com.mmt.model.bean.Flight;
import com.mmt.model.bean.FlightBooking;
import com.mmt.model.bean.Hotel;
import com.mmt.model.bean.HotelBooking;
import com.mmt.model.bean.HotelRoom;
import com.mmt.model.bean.Promotion;
import com.mmt.model.bean.User;
import com.mmt.model.bean.Wallet;
import com.mmt.model.bl.AdminBlMMT;
import com.mmt.model.bl.FlightBookingBlMMT;
import com.mmt.model.bl.FlightPaymentBl;
import com.mmt.model.bl.HotelBlMMT;
import com.mmt.model.bl.HotelPaymentBl;
import com.mmt.model.bl.PromotionBlMMT;
import com.mmt.model.bl.UserBlMMT;
//sd
import com.mmt.model.bl.WalletBlMMT;

@Controller
@SessionAttributes({ "user", "adminSession", "userSession", "seats", "pickedFlight", "finalValuetobepaid",
		"moneyToBeAdded", "messageFlight", "departureDate", "balance", "msg", "viewedFlightDetails", "noOfRooms", "din",
		"dout", "duration", "pickedHotelID", "hotelRoomNo", "RoomPrice", "from", "to", "finalValuetobepaidHotel",
		"messageHotel", "hotelBooking" })
public class UserController {
	@Autowired
	AdminBlMMT adminBlMMT;
	@Autowired
	UserBlMMT blMMT;
	@Autowired
	FlightBookingBlMMT flightBookingBlMMT;
	@Autowired
	HotelBlMMT hotelBlMMT;
	@Autowired
	WalletBlMMT walletBl;
	@Autowired
	PromotionBlMMT promotionBlMMT;
	@Autowired
	HotelPaymentBl hotelPaymentBl;
	@Autowired
	UserBlMMT userBl;

	@RequestMapping("/")
	public String newRegistration(ModelMap model) {
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
	public ModelAndView returnDashboard(User user, ModelMap model)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
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

	@RequestMapping("/LoggInHotelForm")
	public ModelAndView returnLoggInHotelForm() {
		ModelAndView modelAndView = new ModelAndView("LoggInHotelForm", "hotelInfo", new Hotel());
		return modelAndView;

	}

	@RequestMapping("/HotelForm")
	public ModelAndView returnHotelForm() {
		ModelAndView modelAndView = new ModelAndView("HotelForm", "hotelInfo", new Hotel());
		return modelAndView;

	}

	@RequestMapping("/login")
	public ModelAndView returnLoginPage() {
		ModelAndView modelAndView = new ModelAndView("login", "user", new User());
		return modelAndView;

	}

	@RequestMapping("/AddMoney")
	public ModelAndView returnAddMoney() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("AddMoney");
		modelAndView.addObject("wallet", new Wallet());
		return modelAndView;
	}

	@RequestMapping("/UserLogin")
	public ModelAndView returnUserLogin() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("UserLogin");
		return modelAndView;
	}

	@RequestMapping("/DisplayWallet")
	public ModelAndView returnDisplayWallet(ModelMap model)

			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		WalletBlMMT walletBl = new WalletBlMMT();
		String preciseBalance = null;
		double balance = 0;
		User user = (User) model.get("userSession");
		balance = walletBl.walletBalance(user.getUserId());
		DecimalFormat df2 = new DecimalFormat(".##");
		preciseBalance = df2.format(balance);
		model.addAttribute("balance", preciseBalance);
		// modelAndView.addObject("flightBooking",flightBooking );
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
		// Set<Flight> flightSourceListSet=new HashSet<Flight>();

		return flightSourceList;
	}

	@ModelAttribute("flightDestList")
	public List<Flight> flightDestList() throws ClassNotFoundException, SQLException, IOException {

		List<Flight> flightDestList = new ArrayList<Flight>();

		flightDestList = flightBookingBlMMT.displayFlight();

		return flightDestList;
	}

	@ModelAttribute("hotelLocationList")
	public List<Hotel> hotelLocationList() throws ClassNotFoundException, SQLException, IOException {
		List<Hotel> hotelLocationList = new ArrayList<Hotel>();

		hotelLocationList = hotelBlMMT.getHotelLocationList();

		return hotelLocationList;
	}

	@RequestMapping("/SearchFlightBySnD")
	public ModelAndView userRegisterStatus(@ModelAttribute("FlightInfo") Flight flight, ModelMap model,
			HttpServletRequest request) throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		ArrayList<Flight> arrayListFlight = null;
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

	@RequestMapping("/SearchHotelByPlace")
	public ModelAndView returnSearchHotelByPlace(@ModelAttribute("hotelInfo") Hotel hotel, ModelMap model,
			HttpServletRequest request) throws ParseException, ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		// String pickedHotelID = hotel.getHotelId();
		// model.addAttribute("pickedHotelID", pickedHotelID);
		String place = request.getParameter("place");
		String from = request.getParameter("from");
		model.addAttribute("from", from);
		String to = request.getParameter("to");
		model.addAttribute("to", to);
		int noOfRooms = Integer.parseInt(request.getParameter("room"));
		model.addAttribute("noOfRooms", noOfRooms);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date d1 = null;
		d1 = (Date) dateFormat.parse(from);
		Date d2 = null;
		SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
		d2 = (Date) dateFormat1.parse(to);
		model.addAttribute("din", d1);
		model.addAttribute("dout", d2);
		long diff = d2.getTime() - d1.getTime();
		int duration = (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
		model.addAttribute("duration", duration);
		ArrayList<Hotel> arrayListHotel = hotelBlMMT.searchHotel1(hotel.getHotelLocation());

		if (arrayListHotel.isEmpty()) {
			String message = "No Hotels found in " + place;
			modelAndView.addObject("message", message);
			modelAndView.setViewName("NoHotelInPlace");
		} else {
			modelAndView.addObject("arrayListHotel", arrayListHotel);
			modelAndView.setViewName("DisplayAllHotelPlace");

		}
		return modelAndView;

	}

	@RequestMapping("/ChooseRoom")
	public ModelAndView returnChooseRoom(ModelMap model, HttpSession session)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		String pickedHotelID = (String) session.getAttribute("hotelId");
		System.out.println("pickedHotelID" + pickedHotelID);
		model.addAttribute("pickedHotelID", pickedHotelID);
		ArrayList<HotelRoom> arrayListHotelRoom = null;

		arrayListHotelRoom = hotelBlMMT.displayAvailHotelRoom(pickedHotelID);

		if (arrayListHotelRoom.isEmpty()) {
			String message = "No Hotel Rooms Available";
			modelAndView.addObject("message", message);
			modelAndView.setViewName("NoHotelRoomAvailable");

		} else {
			modelAndView.addObject("arrayListHotelRoom", arrayListHotelRoom);
			User user = (User) model.get("userSession");
			if (user != null) {
				modelAndView.setViewName("LoggedInChooseRoom");
			} else {
				modelAndView.setViewName("ChooseRoom");
			}

		}

		return modelAndView;
	}

	@RequestMapping("/ChoosePromoHotel")
	public ModelAndView returnChoosePromoHotel(ModelMap model, HttpSession session)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		User user = (User) model.get("userSession");
		int hotelRoomNo = (int) session.getAttribute("hotelRoomNo");
		model.addAttribute("hotelRoomNo", hotelRoomNo);
		double RoomPrice = (double) session.getAttribute("RoomPrice");
		model.addAttribute("RoomPrice", RoomPrice);
		if (user == null) {
			modelAndView.setViewName("loginUnregistered");
		}
		PromotionBlMMT promoBl = new PromotionBlMMT();
		ArrayList<Promotion> arrayListPromoHotel = null;

		arrayListPromoHotel = promoBl.displayPromotion("HOTEL");

		modelAndView.addObject("arrayListPromoHotel", arrayListPromoHotel);
		modelAndView.addObject("pickedPromoCode", new Promotion());
		modelAndView.setViewName("ChoosePromoCodeHotel");

		return modelAndView;

	}

	@RequestMapping("/ChoosePromoFlight")
	public ModelAndView ChoosePromoFlight(ModelMap model, HttpSession session)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		User user1 = (User) model.get("userSession");
		String pickedFlightID = (String) session.getAttribute("flightId");
		Flight pickedFlight = (Flight) flightBookingBlMMT.searchFlight(pickedFlightID);
		model.addAttribute("pickedFlight", pickedFlight);
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
	public ModelAndView returnPaymentWindow(ModelMap model, @ModelAttribute("pickedPromoCode") Promotion promotion1)
			throws IOException, ClassNotFoundException, SQLException {
		ModelAndView modelAndView = new ModelAndView();
		int noOfSeats = (int) model.get("seats");

		System.out.println();
		System.out.println("noOfSeats" + noOfSeats);
		System.out.println();

		FlightPaymentBl flightPaymentBl = new FlightPaymentBl();
		Flight pickedFlight = (Flight) model.get("pickedFlight");

		System.out.println();
		System.out.println("pickedFlight" + pickedFlight);
		System.out.println();

		double cartValue = 0;

		Promotion promotion = promotionBlMMT.searchPromotion(promotion1.getPromotionId());
		cartValue = flightPaymentBl.cartValue(pickedFlight.getFlightTicketPrice(), noOfSeats);

		double valueAfterPromotion = 0;
		User user = (User) model.get("userSession");
		if (promotion.getPromotionId() == null) {
			valueAfterPromotion = cartValue;
		} else {
			System.out.println("LOOP!1-----------------");
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
			System.out.println();
			System.out.println("NOT suffiecint funds");
			// Insufficient Funds
			// Redirect to Add money to wallet and then redirect to confirm
			// payment JSP Page
			model.addAttribute("finalValuetobepaid", valueAfterPromotion);
			// modelAndView.addObject("finalValuetobepaid",
			// valueAfterPromotion);
			double moneyToBeAdded = valueAfterPromotion - (walletBl.walletBalance(user.getUserId()));
			String message = "Add atleast " + moneyToBeAdded + " to Wallet to book flight seat";
			model.addAttribute("moneyToBeAdded", moneyToBeAdded);
			// modelAndView.addObject("moneyToBeAdded", moneyToBeAdded);
			model.addAttribute("messageFlight", message);
			// model.addAttribute("messageHotel", null);
			String departureDate = (String) model.get("departureDate");
			modelAndView.addObject("departureDate", departureDate);
			modelAndView.addObject("wallet", new Wallet());
			modelAndView.setViewName("AddMoney");
		}

		return modelAndView;

	}

	@RequestMapping("/PaymentHotel")
	public ModelAndView returnPaymentHotel(@ModelAttribute("pickedPromoCode") Promotion promotion1, ModelMap model)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		String hotelIDPicked = (String) model.get("pickedHotelID");
		Promotion promotion = promotionBlMMT.searchPromotion(promotion1.getPromotionId());
		User user = (User) model.get("userSession");
		int noOfRooms = (int) model.get("noOfRooms");
		double roomPrice = (double) model.get("RoomPrice");
		String dcheckIn = (String) model.get("from");
		String dcheckOut = (String) model.get("to");
		int duration = (int) model.get("duration");
		int roomNo = (int) model.get("hotelRoomNo");
		double cartValue = 0;
		cartValue = hotelPaymentBl.cartValue(roomPrice, duration) * noOfRooms;
		PromotionBlMMT promotionBlMMT = new PromotionBlMMT();
		double valueAfterPromotion = 0;

		if (promotion.getPromotionId() == null) {
			valueAfterPromotion = cartValue;
		} else {

			valueAfterPromotion = promotionBlMMT.applyPromotion(
					promotionBlMMT.searchPromotion(promotion.getPromotionId()), user.getUserId(), cartValue);

		}

		if (hotelPaymentBl.checkFunds(user.getUserId(), valueAfterPromotion)) {

			// THere is sufficient funds in account------------------
			// Redirect to Confirm Payment JSP Page
			modelAndView.addObject("finalValuetobepaidHotel", valueAfterPromotion);
			model.addAttribute("finalValuetobepaidHotel", valueAfterPromotion);
			Hotel hotel = hotelBlMMT.searchHotel(hotelIDPicked);
			modelAndView.addObject("hotel", hotel);
			modelAndView.addObject("roomNo", roomNo);
			modelAndView.addObject("dcheckIn", dcheckIn);
			modelAndView.addObject("dcheckOut", dcheckOut);
			modelAndView.setViewName("ConfirmHotelBooking");
		} else {
			// Insufficient Funds
			// Redirect to Add money to wallet and then redirect to confirm
			// payment JSP Page
			model.addAttribute("finalValuetobepaidHotel", valueAfterPromotion);

			double moneyToBeAdded = valueAfterPromotion - (walletBl.walletBalance(user.getUserId()));
			String message = "Add atleast " + moneyToBeAdded + " to Wallet to book hotel room";
			modelAndView.addObject("moneyToBeAdded", moneyToBeAdded);
			modelAndView.addObject("messageHotel", message);
			model.addAttribute("messageHotel", message);
			modelAndView.addObject("wallet", new Wallet());
			modelAndView.setViewName("AddMoney");

		}

		return modelAndView;
	}

	@RequestMapping("/MoneyAddded")
	public ModelAndView returnMoneyAddded(ModelMap model, @ModelAttribute("wallet") Wallet wallet)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();

		User user = (User) model.get("userSession");
		System.out.println();
		System.out.println("sdjkhsdbWALLET: " + wallet.getWalletBalance());
		System.out.println();
		boolean value = false;
		String msg = "";
		double balance = 0;
		String preciseBalance = null;

		value = walletBl.addWalletMoney(user.getUserId(), wallet.getWalletBalance());

		if (value == true) {
			msg = "Successfully Added";
			System.out.println("msg");
			balance = walletBl.walletBalance(user.getUserId());
			DecimalFormat df2 = new DecimalFormat(".##");
			preciseBalance = df2.format(balance);
			model.addAttribute("balance", preciseBalance);
			model.addAttribute("msg", msg);
			modelAndView.setViewName("Wallet");
		}

		return modelAndView;

	}

	@RequestMapping("/ConfirmFlightBooking")
	public ModelAndView returnConfirmBooking(ModelMap model, HttpSession session)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		WalletBlMMT walletBlMMT = new WalletBlMMT();
		boolean paymentStatus = false;
		double valueAfterPromotion = (double) model.get("finalValuetobepaid");
		System.out.println("valueAfterPromotion" + valueAfterPromotion);
		User user = (User) model.get("userSession");
		System.out.println("user:" + user);

		paymentStatus = walletBlMMT.subtractWalletMoney(user.getUserId(), valueAfterPromotion);
		System.out.println("paymentStatus" + paymentStatus);
		if (paymentStatus) {
			FlightBooking flightBooking = new FlightBooking();
			Flight flight = (Flight) model.get("pickedFlight");
			int seats = (int) model.get("seats");

			flightBooking = flightBookingBlMMT.bookFlight(user.getUserId(), flight.getFlightId(),
					flight.getFlightSource(), flight.getFlightDestination(), seats);
			System.out.println("flightBooking" + flightBooking);
			if (flightBooking != null) {
				model.remove("messageFlight");
				session.removeAttribute("messageFlight");
				String messageFlight = (String) model.get("messageFlight");
				model.addAttribute("flightBooking", flightBooking);
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

	@RequestMapping("/ConfirmHotelBooking")
	public ModelAndView returnConfirmHotelBooking( ModelMap model, HttpSession session)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		double valueAfterPromotion = (double) model.get("finalValuetobepaidHotel");
		User user = (User) model.get("userSession");
		boolean paymentStatus = false;

		paymentStatus = walletBl.subtractWalletMoney(user.getUserId(), (double) valueAfterPromotion);

		if (paymentStatus) {
			SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

			Date CheckInDate = null;
			CheckInDate = (Date) model.get("din");
			Date CheckOutDate = null;
			CheckOutDate = (Date) model.get("dout");
			String hotelId = (String) model.get("pickedHotelID");
			int hotelRoomNo = (int) model.get("hotelRoomNo");
			HotelBooking hotelBooking = null;
			hotelBooking = hotelBlMMT.bookHotel(user.getUserId(), hotelId, hotelRoomNo, CheckInDate, CheckOutDate);

			if (hotelBooking != null) {

				String messageHotel = (String) model.get("messageHotel");
				// messageHotel = null;
				model.addAttribute("hotelBooking", hotelBooking);
				model.remove("messageHotel");
				session.removeAttribute("messageHotel");
				// modelAndView.addObject("messageHotel", messageHotel);
				modelAndView.setViewName("FinalHotelStep");

			} else {

				paymentStatus = walletBl.addWalletMoney(user.getUserId(), valueAfterPromotion);

				String messageHotel = (String) model.get("messageHotel");
				model.remove("messageHotel");
				session.removeAttribute("messageHotel");
				modelAndView.setViewName("NoHotelBooking");
			}
		}

		return modelAndView;
	}

	@RequestMapping("/ConfirmFlightBooking2")
	public ModelAndView returnConfirmFlightBooking(ModelMap model) {
		ModelAndView modelAndView = new ModelAndView();
		System.out.println("Entered into Flight");
		Flight flightPicked = (Flight) model.get("pickedFlight");
		modelAndView.addObject("flightPicked", flightPicked);
		modelAndView.setViewName("ConfirmFlightBooking");
		return modelAndView;

	}

	@RequestMapping("/ConfirmHotelBooking2")
	public ModelAndView returnConfirmHotelBooking(ModelMap model)
			throws ClassNotFoundException, IOException, SQLException {
		System.out.println("Entered Into Htoel");
		ModelAndView modelAndView = new ModelAndView();
		String pickedHotelID = (String) model.get("pickedHotelID");
		Hotel hotel = hotelBlMMT.searchHotel(pickedHotelID);
		modelAndView.addObject("hotel", hotel);
		modelAndView.setViewName("ConfirmHotelBooking");
		return modelAndView;

	}

	@RequestMapping("/UserPastFlight")
	public ModelAndView returnUserPastFlight( ModelMap model)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		User user = (User) model.get("userSession");
		ArrayList<FlightBooking> bookedFlightList = blMMT.pastFbooking(user.getUserId());
		modelAndView.addObject("bookedFlightList", bookedFlightList);
		modelAndView.setViewName("UserPastFlight");
		return modelAndView;

	}

	@RequestMapping("/UserPastHotel")
	public ModelAndView returnUserPastHotel(ModelMap model)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		User user = (User) model.get("userSession");
		ArrayList<HotelBooking> bookedHotelList = blMMT.pastHbooking(user.getUserId());
		modelAndView.addObject("bookedHotelList", bookedHotelList);
		modelAndView.setViewName("UserPastHotel");
		return modelAndView;

	}

	@RequestMapping("/ViewPastFlight")
	public ModelAndView returnViewPastFlight(ModelMap model, HttpSession session)
			throws ClassNotFoundException, SQLException, IOException {
		ModelAndView modelAndView = new ModelAndView();
		Flight flight = new Flight();
		flight = flightBookingBlMMT.searchFlight((String) session.getAttribute("viewFlightId"));
		modelAndView.addObject("viewedFlightDetails", flight);
		modelAndView.setViewName("PrintFlightTicket");

		return modelAndView;

	}

	@RequestMapping("/Logout")
	public ModelAndView returnLogout(HttpSession session, ModelMap model) {
		ModelAndView modelAndVieW = new ModelAndView();
		session.invalidate();
		model.remove("userSession");
		modelAndVieW.setViewName("login");
		return modelAndVieW;

	}

	@RequestMapping("/Wallet")
	public ModelAndView returnWallet() {
		ModelAndView modelAndVieW = new ModelAndView();
		modelAndVieW.setViewName("Wallet");
		return modelAndVieW;

	}

	@RequestMapping("/UserProfile")
	public ModelAndView returnUserProfile(ModelMap model) {
		ModelAndView modelAndVieW= new ModelAndView();
		User user = (User) model.get("userSession");
		modelAndVieW.addObject("user", user);
		modelAndVieW.setViewName("UserProfile");
		return modelAndVieW;

	}

}
