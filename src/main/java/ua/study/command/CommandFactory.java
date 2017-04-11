package ua.study.command;

import ua.study.command.impl.*;
import ua.study.command.validation.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dima on 30.03.17.
 */
public class CommandFactory {
    private static CommandFactory instance = new CommandFactory();

    private final Map<String, Command> commands = new HashMap<>();

    private final Validator validator = new Validator();

    public static CommandFactory getInstance(){
        return instance;
    }

    private CommandFactory(){
        commands.put("registration", new RegistrationCommand());
        commands.put("registration_handler", new RegistrationHandlerCommand(validator));
        commands.put("login", new LoginCommand());
        commands.put("login_handler", new LoginHandlerCommand(validator));
        commands.put("reservation", new ReservationCommand(validator));
        commands.put("reservation_handler", new ReservationHandlerCommand(validator));
        commands.put("bill", new BillCommand(validator));
    }

    public Command getCommand(HttpServletRequest request){
        String command = request.getRequestURI().replace("/service/", "");
        return commands.get(command);
    }
}