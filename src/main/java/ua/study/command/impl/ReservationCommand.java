package ua.study.command.impl;

import ua.study.command.Command;
import ua.study.domain.Dates;
import ua.study.domain.Reservation;
import ua.study.domain.RoomType;
import ua.study.service.ServiceFactory;
import ua.study.service.impl.RoomTypeService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

/**
 * Created by dima on 20.04.17.
 */
public class ReservationCommand implements Command {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Dates dates = (Dates) session.getAttribute("dates");

        if (dates == null) {
            session.setAttribute("error", "error.dates_correct");
            response.sendRedirect("/dates");
            return;
        }

        RoomTypeService roomTypeService = ServiceFactory.getInstance().getService(RoomTypeService.class);
        Map<RoomType, Integer> freeRoomTypes = roomTypeService.getFreeRoomTypes(dates);

        request.setAttribute("freeRoomTypes", freeRoomTypes);
        request.getRequestDispatcher("/WEB-INF/jsp/reservation.jsp").include(request, response);
    }
}