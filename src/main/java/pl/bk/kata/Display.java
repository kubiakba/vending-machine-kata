package pl.bk.kata;

import java.util.logging.Logger;

class Display {

    private static final Logger logger = Logger.getLogger(Display.class.getName());

    public void log(String message) {
        logger.info(message);
    }
}
