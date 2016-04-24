package services;

import dao.UserDAO;
import play.Logger;

import javax.inject.Inject;

/**
 * Created by giangdaika on 24/04/2016.
 */
public class AbstractService {
    Logger.ALogger logger = Logger.of(AbstractService.class);
    @Inject
    public UserDAO userDAO;
}
